"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
const abstract_cmd_1 = require("./abstract.cmd");
const schematics_cmd_1 = require("./schematics.cmd");
const version_1 = require("../utilities/version");
const command_list_1 = require("./command.list");
const collection_1 = require("../collection/collection");
class NewCommandModule extends schematics_cmd_1.SchematicsCommandModule {
    schematicName = 'pnew';
    scope = abstract_cmd_1.CommandScope.Out;
    allowPrivateSchematics = true;
    command = 'new [name]';
    aliases = command_list_1.RootCommands['new'].aliases;
    describe = 'Creates a new workspace.';
    async builder(argv) {
        const localYargs = (await super.builder(argv)).option('collection', {
            alias: 'c', type: 'string',
            describe: 'A collection of schematics to use in generating the initial project.'
        });
        const { options: { collection: collectionNameFromArgs } } = this.context.args;
        const collectionName = typeof collectionNameFromArgs === 'string' ? collectionNameFromArgs
            : await this.getCollectionFromConfig();
        const workflow = this.getOrCreateWorkflowForBuilder(collectionName);
        const collection = workflow.engine.createCollection(collectionName);
        const options = await this.getSchematicOptions(collection, this.schematicName, workflow);
        return this.addSchemaOptionsToCommand(localYargs, options);
    }
    async run(options) {
        // Register the version of the CLI in the registry.
        const collectionName = options.collection ?? (await this.getCollectionFromConfig());
        const { dryRun, force, interactive, defaults, collection, ...schematicOptions } = options;
        const workflow = await this.getOrCreateWorkflowForExecution(collectionName, {
            dryRun, force, interactive, defaults
        });
        workflow.registry.addSmartDefaultProvider('ng-lib-version', () => version_1.VERSION.full);
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
    async getCollectionFromConfig() {
        for (const collectionName of await this.getSchematicCollections()) {
            const workflow = this.getOrCreateWorkflowForBuilder(collectionName);
            const collection = workflow.engine.createCollection(collectionName);
            const schematicsInCollection = collection.description.schematics;
            if (Object.keys(schematicsInCollection).includes(this.schematicName)) {
                return collectionName;
            }
        }
        return collection_1.Collection.NgDevSC;
    }
}
exports.default = NewCommandModule;
//# sourceMappingURL=new.cmd.js.map