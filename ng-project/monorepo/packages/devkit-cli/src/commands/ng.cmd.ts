import { getLocalPackageUrl, memoize } from "@ngdev/devkit-core/utilities";
import { RunnerFactory } from "@ngdev/devkit-core/runners";
import { Argv } from "yargs";
import { LocalArgv, OtherOptions, RunOptions } from "./abstract.cmd";
import { SchematicsCommandArgs, SchematicsCommandModule } from "./schematics.cmd";
import { demandCommandFailureMessage } from "./helper/add-cmd-to-args";
import { Option } from "./helper/json-schema";
import { RootCommands } from "./command.list";
import { Collection as ECollection } from "../collection";
import { paths } from "./helper/paths";

interface NgCommandArgs extends SchematicsCommandArgs {
  schematic?: string;
}

function loadCmdConfigByAngularSc(currentDir: string) {
  const packageUrl = getLocalPackageUrl('@angular/cli', currentDir);
  if(!packageUrl || !paths.existsSync(packageUrl)) throw new Error(`The package [@angular/cli] not install`);
  else return require(paths.join(packageUrl, 'src/commands/command-config'));

}

export default class NgCliCommandModule extends SchematicsCommandModule<NgCommandArgs> {
  command = "ng <cmd_name>";
  aliases = RootCommands["ng"].aliases;
  describe = "Generates and/or modifies files based on a schematic.";

  override async builder(localYargs: Argv): Promise<Argv<NgCommandArgs>> {
    localYargs = await super.builder(localYargs);

    if(!this.context.workspace) {
      throw new Error(`This command is not available when running the NGDEV CLI outside a workspace.`);
    }

    // create command [new-app | new-lib]
    localYargs.command({
      command: "app <name>",
      describe: "Creates and initializes a new Angular application that is the default project for a new workspace.",
      builder: b => this.applyYargsBuilderForAppLib(b, "application"),
      handler: (options: any) => this.handler(options, 'app')
    });

    // create command [new-app | new-lib]
    localYargs.command({
      command: "lib <name>",
      describe: "Creates and initializes a new Angular library that is the default project for a new workspace.",
      builder: b => this.applyYargsBuilderForAppLib(b, "library"),
      handler: (options: any) => this.handler(options, 'lib')
    });

    // command [generate] of @angular/cli
    localYargs.command({
      command: "generate <schematic>",
      aliases: ["g", "gen"],
      describe: "Run the provided schematic.",
      builder: b => this.applyBuilderYargsForGenerate(b),
      handler: (options: any) => this.handler(options, 'generate')
    });

    localYargs = await this.applyBuilderYargsForAngular(localYargs);
    return localYargs.demandCommand(1, demandCommandFailureMessage) as any;
  }

  async run(argOptions: RunOptions<NgCommandArgs> & OtherOptions, key?: string): Promise<number | void> {
    const {interactive, dryRun, defaults, force, ...options} = argOptions;

    if('app' == key) await this.createAngularApp(options);
    else if('lib' == key) this.createAngularLib(argOptions);
    else if('generate' == key) this.runAngularGen(argOptions);
    else this.runAngularOther(argOptions, key);
    return 1;
  }

  private runAngularOther(argOptions: RunOptions<NgCommandArgs> & OtherOptions, key: string) {

  }

  private runAngularGen(argOptions: RunOptions<NgCommandArgs> & OtherOptions) {

  }

  private createAngularLib(argOptions: RunOptions<NgCommandArgs> & OtherOptions) {

  }

  private async createAngularApp(options: any) {
    console.log(`createAngularApp`, options);
    const {collection} = options;
    const cwd = paths.join(this.context.root, this.context.workspace.appsDir);

    const defaults = this.context.workspace.getSchematics('@ngdev/sc:app');
    const newOptions = Object.assign({...defaults}, options);
    await RunnerFactory.angular().new(newOptions, cwd);
  }

  @memoize
  protected async getCollectionFromConfig(schematicName: string): Promise<string> {
    return super.getCollectionFromConfig(schematicName, ECollection.Angular);
  }

  @memoize
  protected async getSchematicCollections(): Promise<Set<string>> {
    return new Set([ECollection.Angular]);
  }

  @memoize
  private async getSchematicOptionsByName(collectionName: string, schematicName: string): Promise<{
    [name: string]: Option
  }> {
    const workflow = this.getOrCreateWorkflowForBuilder(collectionName);
    const collection = workflow.engine.createCollection(collectionName);
    const options = await this.getSchematicOptions(collection, schematicName, workflow);
    return options.reduce((json, option) => ({ ...json, [option.name]: option }), {});
  }

  private async applyYargsBuilderForAppLib(localYargs: Argv, schematicName: string): Promise<Argv> {
    const collectionName: string = <any>this.context.args.options["collection"] ?? (await this.getCollectionFromConfig("ng-new"));


    const ngNewOption = await this.getSchematicOptionsByName(collectionName, "ng-new");
    const ngAppOption = await this.getSchematicOptionsByName(collectionName, schematicName);

    const names = Object.keys(ngAppOption).filter(name => !(name in ngNewOption));
    names.forEach(name => ngNewOption[name] = ngAppOption[name]);

    const deleteOptions = ["packageManager", "directory", "createApplication"];
    const includeKeys = Object.keys(ngNewOption).filter(name => !deleteOptions.includes(name));

    const options = includeKeys.map(name => ngNewOption[name]);
    options.push(
      {
        name: "prime", type: "boolean", default: true,
        description: "Use primeng framework"
      },
      {
        name: "tailwind", type: "boolean", default: true,
        description: "Use Tailwind CSS"
      });

    localYargs = localYargs.positional("name", { type: "string" });
    localYargs = localYargs.option("collection", {
      alias: "c", type: "string",
      description: "A collection of schematics to use"
    });

    return this.addSchemaOptionsToCommand(localYargs, options);
  }

  private async applyBuilderYargsForGenerate(localYargs: LocalArgv): Promise<LocalArgv<any>> {
    localYargs = localYargs.positional("schematic", {
      describe: "The [collection:schematic] to run.",
      type: "string",
      demandOption: true
    });

    const schematicsToRegister = await this.getSchematicsToRegister();
    for (const [schematicName, collectionName] of schematicsToRegister) {

      const workflow = this.getOrCreateWorkflowForBuilder(collectionName);
      const collection = workflow.engine.createCollection(collectionName);
      const { description } = collection.createSchematic(schematicName, true);

      const {
        schemaJson, aliases: schematicAliases,
        hidden: schematicHidden,
        description: schematicDescription
      } = description;

      if (schemaJson) {
        const {
          "x-deprecated": xDeprecated,
          description = schematicDescription,
          hidden = schematicHidden
        } = schemaJson;
        let options: Option[] = await this.getSchematicOptions(collection, schematicName, workflow);

        localYargs = localYargs.command({
          command: (await this.generateCommandString(collectionName, schematicName, options)), //
          describe: hidden === true ? false : typeof description === "string" ? description : "",
          deprecated: xDeprecated === true || typeof xDeprecated === "string" ? xDeprecated : false,
          aliases: Array.isArray(schematicAliases) ? await this.generateCommandAliasesStrings(collectionName, schematicAliases) : undefined,
          builder: (localYargs) => this.addSchemaOptionsToCommand(localYargs, options).strict(),
          handler: (options) => this.handler({ ...options, schematic: `${collectionName}:${schematicName}` } as any)
        });
      }

    }
    return localYargs;
  }

  private async applyBuilderYargsForAngular(localYargs: LocalArgv) {

    // generate command default of @angular/cli
    const { RootCommands } = loadCmdConfigByAngularSc(process.cwd());// require("D:\\Angular_CLI\\Tool\\node_modules\\@angular\\cli\\src\\commands\\command-config");
    const ALL_COMMAND: { [key: string]: { factory: any, aliases: string[] } } = RootCommands;

    for (const [key, { factory, aliases = [] }] of Object.entries(ALL_COMMAND)) {
      if (["generate", "new"].includes(key)) continue;

      const { default: commandModule } = await factory();
      const newCls = new commandModule(this.context);

      localYargs.command({
        command: newCls.command,
        describe: newCls.describe,
        aliases: [...aliases],
        builder: b => newCls.builder(b),
        handler: (options: any) => this.handler(options, 'key')
      });
    }

    return localYargs;
  }


}