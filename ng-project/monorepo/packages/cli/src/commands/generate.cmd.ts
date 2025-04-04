import { strings } from '@angular-devkit/core';
import { Collection } from '@angular-devkit/schematics';
import {FileSystemCollectionDescription, FileSystemSchematicDescription,} from '@angular-devkit/schematics/tools';
import { ArgumentsCamelCase, Argv } from 'yargs';
import {CommandModuleError, CommandModuleImplementation, Options, OtherOptions} from './abstract.cmd';
import {SchematicsCommandArgs, SchematicsCommandModule,} from './schematics.cmd';
import { demandCommandFailureMessage } from './helper/add-cmd-to-args';
import { Option } from './helper/json-schema';
import {RootCommands} from "./command.list";

interface GenerateCommandArgs extends SchematicsCommandArgs {
  schematic?: string;
}

export default class GenerateCommandModule extends SchematicsCommandModule
  implements CommandModuleImplementation<GenerateCommandArgs>
{
  command = 'generate|gen <schematic>';
  aliases = RootCommands['generate'].aliases;
  describe = 'Generates and/or modifies files based on a schematic.';

  override async builder(argv: Argv): Promise<Argv<GenerateCommandArgs>> {
    let localYargs = (await super.builder(argv)).command({
      command: '$0 <schematic>',
      describe: 'Run the provided schematic.',
      builder: (localYargs) =>
        localYargs
          .positional('schematic', {
            describe: 'The [collection:schematic] to run.',
            type: 'string',
            demandOption: true,
          })
          .strict(),
      handler: (options) => this.handler(options as ArgumentsCamelCase<GenerateCommandArgs>),
    });

    for (const [schematicName, collectionName] of await this.getSchematicsToRegister()) {
      const workflow = this.getOrCreateWorkflowForBuilder(collectionName);
      const collection = workflow.engine.createCollection(collectionName);

      const {
        description: {
          schemaJson,
          aliases: schematicAliases,
          hidden: schematicHidden,
          description: schematicDescription,
        },
      } = collection.createSchematic(schematicName, true);

      if (!schemaJson) {
        continue;
      }

      const {
        'x-deprecated': xDeprecated,
        description = schematicDescription,
        hidden = schematicHidden,
      } = schemaJson;
      const options = await this.getSchematicOptions(collection, schematicName, workflow);

      localYargs = localYargs.command({
        command: await this.generateCommandString(collectionName, schematicName, options),
        // When 'describe' is set to false, it results in a hidden command.
        describe: hidden === true ? false : typeof description === 'string' ? description : '',
        deprecated: xDeprecated === true || typeof xDeprecated === 'string' ? xDeprecated : false,
        aliases: Array.isArray(schematicAliases)
          ? await this.generateCommandAliasesStrings(collectionName, schematicAliases)
          : undefined,
        builder: (localYargs) => this.addSchemaOptionsToCommand(localYargs, options).strict(),
        handler: (options) =>
          this.handler({
            ...options,
            schematic: `${collectionName}:${schematicName}`,
          } as ArgumentsCamelCase<
            SchematicsCommandArgs & {
              schematic: string;
            }
          >),
      });
    }

    return localYargs.demandCommand(1, demandCommandFailureMessage);
  }

  async run(options: Options<GenerateCommandArgs> & OtherOptions): Promise<number | void> {
    const { dryRun, schematic, defaults, force, interactive, ...schematicOptions } = options;

    const [collectionName, schematicName] = this.parseSchematicInfo(schematic);

    if (!collectionName || !schematicName) {
      throw new CommandModuleError('A collection and schematic is required during execution.');
    }





    return this.runSchematic({
      collectionName,
      schematicName,
      schematicOptions,
      executionOptions: {
        dryRun,
        defaults,
        force,
        interactive,
      },
    });
  }

  private async getCollectionNames(): Promise<string[]> {
    const [collectionName] = this.parseSchematicInfo(
      // positional = [generate, component] or [generate]
      this.context.args.positional[1],
    );

    return collectionName ? [collectionName] : [...(await this.getSchematicCollections())];
  }

  private async shouldAddCollectionNameAsPartOfCommand(): Promise<boolean> {
    const [collectionNameFromArgs] = this.parseSchematicInfo(
      // positional = [generate, component] or [generate]
      this.context.args.positional[1],
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

  /**
   * Generate an aliases string array to be passed to the command builder.
   *
   * @example `[component]` or `[@collection/angular:component]`.
   */
  private async generateCommandAliasesStrings(
    collectionName: string,
    schematicAliases: string[],
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
  private async generateCommandString(
    collectionName: string,
    schematicName: string,
    options: Option[],
  ): Promise<string> {
    const dasherizedSchematicName = strings.dasherize(schematicName);

    // Only add the collection name as part of the command when it's not a known
    // collection collection or when it has been provided via the CLI.
    // Ex:`ng generate @collection/angular:component`
    const commandName = (await this.shouldAddCollectionNameAsPartOfCommand())
      ? collectionName + ':' + dasherizedSchematicName
      : dasherizedSchematicName;

    const positionalArgs = options
      .filter((o) => o.positional !== undefined)
      .map((o) => {
        const label = `${strings.dasherize(o.name)}${o.type === 'array' ? ' ..' : ''}`;

        return o.required ? `<${label}>` : `[${label}]`;
      })
      .join(' ');

    return `${commandName}${positionalArgs ? ' ' + positionalArgs : ''}`;
  }

  /**
   * Get collection that can to be registered as subcommands.
   */
  private async *getSchematics(): AsyncGenerator<{
    schematicName: string;
    schematicAliases?: Set<string>;
    collectionName: string;
  }> {
    const seenNames = new Set<string>();
    for (const collectionName of await this.getCollectionNames()) {
      const workflow = this.getOrCreateWorkflowForBuilder(collectionName);
      const collection = workflow.engine.createCollection(collectionName);

      for (const schematicName of collection.listSchematicNames(true /** includeHidden */)) {
        // If a schematic with this same name is already registered skip.
        if (!seenNames.has(schematicName)) {
          seenNames.add(schematicName);

          yield {
            schematicName,
            collectionName,
            schematicAliases: this.listSchematicAliases(collection, schematicName),
          };
        }
      }
    }
  }

  private listSchematicAliases(
    collection: Collection<FileSystemCollectionDescription, FileSystemSchematicDescription>,
    schematicName: string,
  ): Set<string> | undefined {
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
   * Get collection that should to be registered as subcommands.
   *
   * @returns a sorted list of schematic that needs to be registered as subcommands.
   */
  private async getSchematicsToRegister(): Promise<
    [schematicName: string, collectionName: string][]
  > {
    const schematicsToRegister: [schematicName: string, collectionName: string][] = [];
    const [, schematicNameFromArgs] = this.parseSchematicInfo(
      // positional = [generate, component] or [generate]
      this.context.args.positional[1],
    );

    for await (const { schematicName, collectionName, schematicAliases } of this.getSchematics()) {
      if (
        schematicNameFromArgs &&
        (schematicName === schematicNameFromArgs || schematicAliases?.has(schematicNameFromArgs))
      ) {
        return [[schematicName, collectionName]];
      }

      schematicsToRegister.push([schematicName, collectionName]);
    }

    // Didn't find the schematic or no schematic name was provided Ex: `ng generate --utilities`.
    return schematicsToRegister.sort(([nameA], [nameB]) =>
      nameA.localeCompare(nameB, undefined, { sensitivity: 'accent' }),
    );
  }
}