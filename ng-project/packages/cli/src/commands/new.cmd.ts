import {Argv} from 'src/typings/yargs';
import {CommandModuleImplementation, CommandScope, Options, OtherOptions,} from './abstract.cmd';
import {SchematicsCommandArgs, SchematicsCommandModule,} from './schematics.cmd';
import {VERSION} from '../help/version';
import {RootCommands} from "./command.list";
import {Collection} from "../collection/collection";

interface NewCommandArgs extends SchematicsCommandArgs {
  collection?: string;
}

export default class NewCommandModule extends SchematicsCommandModule implements CommandModuleImplementation<NewCommandArgs> {
  private readonly schematicName = 'pnew';
  override scope = CommandScope.Out;
  protected override allowPrivateSchematics = true;

  command = 'new [name]';
  aliases = RootCommands['new'].aliases;
  describe = 'Creates a new workspace.';

  async builder(argv: Argv): Promise<Argv<NewCommandArgs>> {
    const localYargs = (await super.builder(argv)).option('collection', {
      alias: 'c', type: 'string',
      describe: 'A collection of schematics to use in generating the initial project.'
    });

    const {options: {collection: collectionNameFromArgs}} = this.context.args;

    const collectionName =
      typeof collectionNameFromArgs === 'string' ? collectionNameFromArgs
        : await this.getCollectionFromConfig();

    const workflow = this.getOrCreateWorkflowForBuilder(collectionName);
    const collection = workflow.engine.createCollection(collectionName);
    const options = await this.getSchematicOptions(collection, this.schematicName, workflow);

    return this.addSchemaOptionsToCommand(localYargs, options);
  }

  async run(options: Options<NewCommandArgs> & OtherOptions): Promise<number | void> {
    // Register the version of the CLI in the registry.
    const collectionName = options.collection ?? (await this.getCollectionFromConfig());
    const {dryRun, force, interactive, defaults, collection, ...schematicOptions} = options;
    const workflow = await this.getOrCreateWorkflowForExecution(collectionName, {
      dryRun, force,interactive, defaults});

    workflow.registry.addSmartDefaultProvider('ng-lib-version', () => VERSION.full);

    return this.runSchematic({
      collectionName,
      schematicName: this.schematicName,
      schematicOptions,
      executionOptions: {
        dryRun,
        force,
        interactive,
        defaults,
      },
    });
  }

  /** Find a collection from config that has an `ng-new` schematic. */
  private async getCollectionFromConfig(): Promise<string> {
    for (const collectionName of await this.getSchematicCollections()) {
      const workflow = this.getOrCreateWorkflowForBuilder(collectionName);
      const collection = workflow.engine.createCollection(collectionName);
      const schematicsInCollection = collection.description.schematics;

      if (Object.keys(schematicsInCollection).includes(this.schematicName)) {
        return collectionName;
      }
    }

    return Collection.NgDevSC;
  }
}
