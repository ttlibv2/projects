import { join } from 'node:path';
import { Argv } from 'yargs';
import {
  CommandModuleImplementation,
  CommandScope,
  Options,
  OtherOptions,
} from '../core/command-module';
import {
  DEFAULT_SCHEMATICS_COLLECTION,
  SchematicsCommandArgs,
  SchematicsCommandModule,
} from '../core/schematics-command-module';
import { VERSION } from '../../utilities/version';
import { RootCommands } from '../command-config';

interface NewCommandArgs extends SchematicsCommandArgs {
  collection?: string;
}

export default class NewCommandModule
  extends SchematicsCommandModule
  implements CommandModuleImplementation<NewCommandArgs>
{
  private readonly schematicName = 'ng-new';
  override scope = CommandScope.Out;
  protected override allowPrivateSchematics = true;

  command = 'new [name]';
  aliases = RootCommands['new'].aliases;
  describe = 'Creates a new Angular workspace.';
  longDescriptionPath = join(__dirname, 'long-description.md');

  override async builder(argv: Argv): Promise<Argv<NewCommandArgs>> {
    const localYargs = (await super.builder(argv)).option('collection', {
      alias: 'c',
      describe: 'A collection of schematics to use in generating the initial application.',
      type: 'string',
    });

    const {
      options: { collection: collectionNameFromArgs },
    } = this.context.args;

    const collectionName =
      typeof collectionNameFromArgs === 'string'
        ? collectionNameFromArgs
        : await this.getCollectionFromConfig();

    const workflow = this.getOrCreateWorkflowForBuilder(collectionName);
    const collection = workflow.engine.createCollection(collectionName);
    const options = await this.getSchematicOptions(collection, this.schematicName, workflow);

    return this.addSchemaOptionsToCommand(localYargs, options);
  }

  async run(options: Options<NewCommandArgs> & OtherOptions): Promise<number | void> {
    // Register the version of the CLI in the registry.
    const collectionName = options.collection ?? (await this.getCollectionFromConfig());
    const { force, interactive, defaults, collection, ...schematicOptions } = options;
    const dryRun: boolean = options['dry-run'];
    const workflow = await this.getOrCreateWorkflowForExecution(collectionName, {
      'dry-run': dryRun, force, interactive,defaults,
    });
    
    workflow.registry.addSmartDefaultProvider('ng-cli-version', () => VERSION.full);

    return this.runSchematic({
      collectionName,
      schematicName: this.schematicName,
      schematicOptions,
      executionOptions: {
       'dry-run': dryRun,
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

    return DEFAULT_SCHEMATICS_COLLECTION;
  }
}