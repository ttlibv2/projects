import { JsonValue, normalize as devkitNormalize, schema, strings } from "@angular-devkit/core";
import { Collection as NgCollection, UnsuccessfulWorkflowExecution, formats} from "@angular-devkit/schematics";
import { FileSystemCollectionDescription, FileSystemSchematicDescription, NodeWorkflow, } from "@angular-devkit/schematics/tools";
import { relative } from "node:path";
import { assertIsError } from "../utilities/error";
import { isTTY } from "../utilities/tty";
import { CommandModule, CommandScope, RunOptions, OtherOptions, LocalArgv } from "./abstract.cmd";
import { Option, parseJsonSchemaToOptions } from "./helper/json-schema";
import { EngineHost } from "./helper/engine-host";
import { subscribeToWorkflow } from "./helper/schematic-workflow";
import { Collection } from "../collection";
import { getProjectByCwd, getSchematicDefaults } from "../workspace";
import { memoize } from "@ngdev/devkit-core/utilities";
import { SmartDefaultProvider } from '@angular-devkit/core/src/json/schema/interface';

export type SchematicCollection = NgCollection<FileSystemCollectionDescription, FileSystemSchematicDescription>;

export interface SchematicsCommandArgs {
  interactive: boolean;
  force: boolean;
  dryRun: boolean;
  defaults: boolean;
  workflowRoot?: string;
}

export interface SchematicsExecutionOptions extends RunOptions<SchematicsCommandArgs> {
  packageRegistry?: string;
}

export interface SmartDefaultProviderOption extends SchematicsExecutionOptions {
  collectionName: string;
  providers: Record<string, SmartDefaultProvider<any>>
}

type SchematicsToRegister = [schematicName: string, collectionName: string];

export abstract class SchematicsCommandModule<T extends SchematicsCommandArgs> extends CommandModule<T> {
  override scope = CommandScope.In;
  protected readonly allowPrivateSchematics: boolean = false;

  builder(argv: LocalArgv): Promise<LocalArgv<T>> | LocalArgv<T> {
    return argv
      .option("interactive", {
        describe: "Enable interactive input prompts.",
        type: "boolean",
        default: true,
      })
      .option("dryRun", {
        describe:
          "Run through and reports activity without writing out results.",
        type: "boolean",
        alias: ["d"],
        default: false,
      })
      .option("defaults", {
        describe:
          "Disable interactive input prompts for options with a default.",
        type: "boolean",
        default: false,
      })
      .option("force", {
        describe: "Force overwriting of existing files.",
        type: "boolean",
        default: false,
      })
      .strict() as LocalArgv<T>;
  }

  /** Get schematic schema options.*/
  protected async getSchematicOptions(collection: SchematicCollection, schematicName: string, workflow: NodeWorkflow): Promise<Option[]> {
    const { schemaJson } = collection.createSchematic(schematicName, true).description;
    return schemaJson ?  parseJsonSchemaToOptions(workflow.registry, schemaJson) : [];
  }

  @memoize
  protected getOrCreateWorkflowForBuilder(collectionName: string, workflowRoot: string = this.context.root): NodeWorkflow {
    return new NodeWorkflow(workflowRoot, {
      resolvePaths: this.getResolvePaths(collectionName),
      engineHostCreator: (options) => new EngineHost(options.resolvePaths),
    });
  }

  @memoize
  protected async getOrCreateWorkflowForExecution(collectionName: string, options: SchematicsExecutionOptions): Promise<NodeWorkflow> {
    const { logger, root: contextRoot, packageManager } = this.context;
    const { force, dryRun, packageRegistry, workflowRoot } = options;

    const cwdRoot = workflowRoot || contextRoot;
    const workflow = new NodeWorkflow(cwdRoot, {
      force, dryRun, packageRegistry,
      packageManager: packageManager.name.toLowerCase(),
      registry: new schema.CoreSchemaRegistry(formats.standardFormats),
      resolvePaths: this.getResolvePaths(collectionName),
      engineHostCreator: (options) => new EngineHost(options.resolvePaths),
      schemaValidation: true,
      optionTransforms: [ // Add configuration file defaults
        async (schematic, current) => {
          const projectName = typeof current?.project === "string" ? current.project : this.getProjectName();
          const scDefaults = await getSchematicDefaults(schematic.collection.name, schematic.name, projectName);
          return { ...scDefaults, ...current};
        }
      ]
    });

    workflow.registry.addPostTransform(schema.transforms.addUndefinedDefaults);
    workflow.registry.useXDeprecatedProvider((msg) => logger.warn(msg));
    workflow.registry.addSmartDefaultProvider("projectName", () =>
      this.getProjectName(),
    );

    const workingDir = devkitNormalize(relative(cwdRoot, process.cwd()),);
    workflow.registry.addSmartDefaultProvider("workingDirectory", () =>
      workingDir === "" ? undefined : workingDir,
    );

    workflow.engineHost.registerOptionsTransform(async (schematic, options) => options);

    if (options.interactive !== false && isTTY()) {
      workflow.registry.usePromptProvider(
        async (definitions: Array<schema.PromptDefinition>) => {
          let prompts: typeof import("@inquirer/prompts") | undefined;
          const answers: Record<string, JsonValue> = {};

          for (const definition of definitions) {
            if (options.defaults && definition.default !== undefined) {
              continue;
            }

            // Only load prompt package if needed
            prompts ??= await import("@inquirer/prompts");

            switch (definition.type) {
              case "confirmation":
                answers[definition.id] = await prompts.confirm({
                  message: definition.message,
                  default: definition.default as boolean | undefined,
                });
                break;
              case "list":
                if (!definition.items?.length) {
                  continue;
                }

                answers[definition.id] = await (
                  definition.multiselect ? prompts.checkbox : prompts.select
                )({
                  message: definition.message,
                  validate: (values) => {
                    if (!definition.validator) {
                      return true;
                    }

                    return definition.validator(
                      Object.values(values).map(({ value }) => value),
                    );
                  },
                  default: definition.multiselect
                    ? undefined
                    : definition.default,
                  choices: definition.items?.map((item) =>
                    typeof item == "string"
                      ? {
                          name: item,
                          value: item,
                        }
                      : {
                          ...item,
                          name: item.label,
                          value: item.value,
                        },
                  ),
                });
                break;
              case "input": {
                let finalValue: JsonValue | undefined;
                answers[definition.id] = await prompts.input({
                  message: definition.message,
                  default: definition.default as string | undefined,
                  async validate(value) {
                    if (definition.validator === undefined) {
                      return true;
                    }

                    let lastValidation: ReturnType<
                      typeof definition.validator
                    > = false;
                    for (const type of definition.propertyTypes) {
                      let potential;
                      switch (type) {
                        case "string":
                          potential = String(value);
                          break;
                        case "integer":
                        case "number":
                          potential = Number(value);
                          break;
                        default:
                          potential = value;
                          break;
                      }
                      lastValidation = await definition.validator(potential);

                      // Can be a string if validation fails
                      if (lastValidation === true) {
                        finalValue = potential;

                        return true;
                      }
                    }

                    return lastValidation;
                  },
                });

                // Use validated value if present.
                // This ensures the correct type is inserted into the final schema options.
                if (finalValue !== undefined) {
                  answers[definition.id] = finalValue;
                }
                break;
              }
            }
          }

          return answers;
        },
      );
    }

    return workflow;
  }

  /** Find a collection from config that has an `ng-new` schematic. */
  protected async getCollectionFromConfig(schematicName: string, defaultCollection?: string): Promise<string> {
    for (const collectionName of await this.getSchematicCollections()) {
      const workflow = this.getOrCreateWorkflowForBuilder(collectionName);
      const collection = workflow.engine.createCollection(collectionName);
      const schematicsInCollection = collection.description.schematics;
      if (Object.keys(schematicsInCollection).includes(schematicName)) {
        return collectionName;
      }
    }

    return defaultCollection ?? Collection.NgDevSC;
  }

  @memoize
  protected async getSchematicCollections(): Promise<Set<string>> {
    // const getSchematicCollections = (configSection: Record<string, unknown> | undefined): Set<string> | undefined => {
    //   if (!configSection) return undefined;
    //
    //   const { collections } = configSection;
    //
    //   if (Array.isArray(collections)) {
    //     return new Set(...collections, DEFAULT_SCHEMATICS_COLLECTION);
    //   }
    //
    //   return undefined;
    // };
    //
    //
    //
    // const { workspace, globalConfiguration } = this.context;
    // if (workspace) {
    //   const project = getProjectByCwd(workspace);
    //   if (project) {
    //     const value = getSchematicCollections(workspace.getProjectCli(project));
    //     if (value) {
    //       return value;
    //     }
    //   }
    // }
    //
    // const value = getSchematicCollections(workspace?.getCli()) ??
    //   getSchematicCollections(globalConfiguration.getCli());
    // if (value) {
    //   return value;
    // }

   return new Set([Collection.NgDevSC]);
  }

  protected parseSchematicInfo(schematic: string | undefined): [collectionName: string | undefined, schematicName: string | undefined] {
    if (schematic?.includes(":")) {
      const [collectionName, schematicName] = schematic.split(":", 2);
      return [collectionName, schematicName];
    }

    return [undefined, schematic];
  }

  protected async runSchematic(options: {
    executionOptions: SchematicsExecutionOptions;
    schematicOptions: OtherOptions;
    collectionName: string;
    schematicName: string;
  }): Promise<number> {
    const { logger } = this.context;
    const { schematicOptions, executionOptions, collectionName, schematicName, } = options;
    const workflow = await this.getOrCreateWorkflowForExecution(collectionName, executionOptions);

    if (!schematicName) {
      throw new Error("schematicName cannot be undefined.");
    }

    const { unsubscribe, files } = subscribeToWorkflow(workflow, logger);

    try {
      await workflow
        .execute({
          collection: collectionName,
          schematic: schematicName,
          options: schematicOptions,
          logger: logger.scLogger,
          allowPrivate: this.allowPrivateSchematics,
        })
        .toPromise();

      if (!files.size) {
        logger.info("Nothing to be done.");
      }

      if (executionOptions.dryRun) {
        logger.warn(
          `\nNOTE: The "--dry-run" option means no changes were made.`,
        );
      }
    } catch (err) {
      //
      // In case the workflow was not successful, show an appropriate error message.
      if (err instanceof UnsuccessfulWorkflowExecution) {
        // "See above" because we already printed the error.
        logger.fatal("The Schematic workflow failed. See above.");
      } //
      else {
        assertIsError(err);
        logger.fatal(err.message);
        throw err;
      }

      return 1;
    } finally {
      unsubscribe();
    }

    return 0;
  }

  private getProjectName(): string | undefined {
    const { workspace } = this.context;
    if (!workspace) {
      return undefined;
    }

    const projectName = getProjectByCwd(workspace);
    if (projectName) {
      return projectName;
    }

    return undefined;
  }

  private getResolvePaths(collectionName: string): string[] {
    const { workspace, root } = this.context;
    if (collectionName[0] === ".") {
      // Resolve relative collections from the location of `angular.json`
      return [root];
    }

    return workspace
      ? // Workspace
        collectionName === Collection.NgDevSC
        ? // Favor __dirname for @collection/angular to use the build-in version
          [__dirname, process.cwd(), root]
        : [process.cwd(), root, __dirname]
      : // Global
        [__dirname, process.cwd()];
  }



  /**
   * Get collection that should to be registered as subcommands.
   *
   * @returns a sorted list of schematic that needs to be registered as subcommands.
   */
  protected async getSchematicsToRegister(): Promise<SchematicsToRegister[]> {
    const schematicsToRegister: SchematicsToRegister[] = [];
    const [, schematicNameFromArgs] = this.parseSchematicInfo(
      // positional = [generate, component] or [generate]
      this.context.args.positional[1]
    );

    for await (const { schematicName, collectionName, schematicAliases} of this.getSchematics()) {
      if (schematicNameFromArgs && (schematicName === schematicNameFromArgs || schematicAliases?.has(schematicNameFromArgs))) {
        return [[schematicName, collectionName]];
      }

      schematicsToRegister.push([schematicName, collectionName]);
    }

    // Didn't find the schematic or no schematic name was provided Ex: `ng generate --utilities`.
    return schematicsToRegister.sort(([nameA], [nameB]) =>
      nameA.localeCompare(nameB, undefined, { sensitivity: 'accent' })
    );
  }


  protected async getCollectionNames(): Promise<string[]> {
    const [collectionName] = this.parseSchematicInfo(
      // positional = [generate, component] or [generate]
      this.context.args.positional[1]
    );

    return collectionName
      ? [collectionName]
      : [...(await this.getSchematicCollections())];
  }


  /**
   * Get collection that can to be registered as subcommands.
   */
  protected async* getSchematics(): AsyncGenerator<{
    schematicName: string;
    schematicAliases?: Set<string>;
    collectionName: string;
  }> {
    const seenNames = new Set<string>();
    for (const collectionName of await this.getCollectionNames()) {
      const workflow = this.getOrCreateWorkflowForBuilder(collectionName);
      const collection = workflow.engine.createCollection(collectionName);

      for (const schematicName of collection.listSchematicNames(
        true /** includeHidden */
      )) {
        // If a schematic with this same name is already registered skip.
        if (!seenNames.has(schematicName)) {
          seenNames.add(schematicName);

          yield {
            schematicName,
            collectionName,
            schematicAliases: this.listSchematicAliases(
              collection,
              schematicName
            )
          };
        }
      }
    }
  }


  private listSchematicAliases(collection: SchematicCollection, schematicName: string): Set<string> | undefined {
    const description = collection.description.schematics[schematicName];

    if (description) {
      return description.aliases && new Set(description.aliases);
    }

    // Extended collections
    if (collection.baseDescriptions) {
      for (const base of collection.baseDescriptions) {
        const description = base.schematics[schematicName];
        if (description) {
          return description.aliases && new Set(description.aliases);
        }
      }
    }

    return undefined;
  }


  /**
   * Generate an aliases string array to be passed to the command builder.
   *
   * @example `[component]` or `[@collection/angular:component]`.
   */
  protected async generateCommandAliasesStrings(
    collectionName: string,
    schematicAliases: string[]
  ): Promise<string[]> {
    // Only add the collection name as part of the command when it's not a known
    // collection collection or when it has been provided via the CLI.
    // Ex:`ng generate @collection/angular:c`
    return (await this.shouldAddCollectionNameAsPartOfCommand())
      ? schematicAliases.map((alias) => `${collectionName}:${alias}`)
      : schematicAliases;
  }

  /**
   * Generate a command string to be passed to the command builder.
   *
   * @example `component [name]` or `@collection/angular:component [name]`.
   */
  protected async generateCommandString(collectionName: string, schematicName: string, options: Option[]): Promise<string> {
    const dasherSchematicName = strings.dasherize(schematicName);

    // Only add the collection name as part of the command when it's not a known
    // collection collection or when it has been provided via the CLI.
    // Ex:`ng generate @collection/angular:component`
    const shouldAdd = await this.shouldAddCollectionNameAsPartOfCommand();
    const commandName = (shouldAdd ? `${collectionName}:` : '') + dasherSchematicName;

    const positionalArgs = options
      .filter((o) => o.positional !== undefined)
      .map((o) => {
        const label = `${strings.dasherize(o.name)}${o.type === 'array' ? ' ..' : ''}`;
        return o.required ? `<${label}>` : `[${label}]`;
      })
      .join(' ');

    return `${commandName}${positionalArgs ? ' ' + positionalArgs : ''}`;
  }


  private async shouldAddCollectionNameAsPartOfCommand(): Promise<boolean> {
    const [collectionNameFromArgs] = this.parseSchematicInfo(
      // positional = [generate, component] or [generate]
      this.context.args.positional[1]
    );

    const schematicCollectionsFromConfig = await this.getSchematicCollections();
    const collectionNames = await this.getCollectionNames();

    // Only add the collection name as part of the command when it's not a known
    // collection collection or when it has been provided via the CLI.
    // Ex:`ng generate @collection/angular:c`
    return (
      !!collectionNameFromArgs ||
      !collectionNames.some((c) => schematicCollectionsFromConfig.has(c))
    );
  }

  protected async addSmartDefaultProvider(options: SmartDefaultProviderOption, ) {
    const {providers, collectionName, ...workflowOptions } = options;
    const workflow = await this.getOrCreateWorkflowForExecution(collectionName, workflowOptions);
    Object.entries(providers).forEach(provider => workflow.registry.addSmartDefaultProvider(provider[0], provider[1]));
  }

}