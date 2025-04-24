import { CommandScope, RunOptions, OtherOptions, LocalArgv } from "./abstract.cmd";
import { SchematicsCommandArgs, SchematicsCommandModule } from "./schematics.cmd";
import { RootCommands } from "./command.list";
import { Collection } from "../collection";
import { PkgManagerFactory } from "@ngdev/devkit-core/pkgmanager";
import { RunnerFactory } from "@ngdev/devkit-core/runners";
import { paths } from "./helper/paths";

interface NewCommandArgs extends SchematicsCommandArgs {
  collection?: string;
}

export default class NewCommandModule extends SchematicsCommandModule<NewCommandArgs> {
  private readonly schematicName = "new";
  override scope = CommandScope.Out;
  protected override allowPrivateSchematics = true;

  command = "new [name]";
  aliases = RootCommands["new"].aliases;
  describe = "Creates a new workspace.";

  async builder(argv: LocalArgv): Promise<LocalArgv<NewCommandArgs>> {
    const localYargs = (await super.builder(argv))
      .option("collection", {
        alias: ["c"], type: "string",
        describe: "A collection of collection to use in generating the initial project."
      })
      .option('packageManager', {
        type: "string",
        choices: ["pnpm"],
        default: "pnpm"
      })
      .option('appName', {
        type: "string",
      })
      .option('libName', {
        type: "string",
      });

    const { options: { collection: collectionNameFromArgs } } = this.context.args;
    const collectionName = typeof collectionNameFromArgs === "string" ? collectionNameFromArgs : await this.getCollectionFromConfig(this.schematicName);
    const workflow = this.getOrCreateWorkflowForBuilder(collectionName);
    const collection = workflow.engine.createCollection(collectionName);
    const options = await this.getSchematicOptions(collection, this.schematicName, workflow);
    return this.addSchemaOptionsToCommand(localYargs, options);
  }

  async run(options: RunOptions<NewCommandArgs> & OtherOptions): Promise<number | void> {
    const collectionName = options.collection ?? (await this.getCollectionFromConfig(this.schematicName));
    const { dryRun, force, interactive, defaults, collection, packageManager, appName, libName, ...schematicOptions } = options;

    const packageName = <any>packageManager ?? 'pnpm';
    const pnpmVersion = await PkgManagerFactory.create(packageName).version;
    const ngVersion  = await RunnerFactory.angular().version;

    const workflow = await this.getOrCreateWorkflowForExecution(collectionName, { dryRun, force, interactive, defaults });
    workflow.registry.addSmartDefaultProvider("package-version", () => pnpmVersion);
    workflow.registry.addSmartDefaultProvider("package-manager", () => packageName);
    workflow.registry.addSmartDefaultProvider("ngcli-version", () => ngVersion);

    schematicOptions.appsDir = schematicOptions.appsDir ?? 'apps';
    schematicOptions.libsDir = schematicOptions.libsDir ?? 'packages';

    const number = await this.runSchematic({
      collectionName, schematicOptions,
      schematicName: this.schematicName,
      executionOptions: { dryRun, force, interactive, defaults }
    });

    if(number == 0) {
      this.createDir(schematicOptions);
    }

    return number;
  }

  private createDir(schematicOptions: any) {
    let dir = <any>schematicOptions.directory;

    // If scoped project (i.e. "@foo/bar"), convert directory to "foo/bar".
    if (!dir && schematicOptions.name) {
      const name: string = <any>schematicOptions.name;
      dir = name.startsWith('@') ? name.slice(1) : schematicOptions.name;
    }

    paths.mkdir(paths.join(dir, <string>schematicOptions.appsDir));
    paths.mkdir(paths.join(dir, <string>schematicOptions.libsDir));
  }

}