import { PkgManagerFactory } from '@ngdev/devkit-core/pkgmanager';
import { RunnerFactory } from '@ngdev/devkit-core/runners';
import { SchematicArg, SchematicsCommand } from './core/schematics.cmd';
import { ArgOption, CommandScope, LocalArgv } from './core/abstract.cmd';
import { RootCommands } from './command.list';
import { Collection } from '../collection';

interface NewArgs extends SchematicArg {
    collection?: string;
    packageManager?: 'pnpm';
    appName?: string;
    libName?: string;
    name?: string;
}

export default class NewCommand extends SchematicsCommand<NewArgs> {
    readonly command = 'new <name>';
    readonly aliases = RootCommands['new'].aliases;
    readonly describe = 'Creates a new monorepo workspace.';
    readonly scope = CommandScope.Out;
    readonly schematicName = 'dev-new';

    async builder(argv: LocalArgv): Promise<LocalArgv<NewArgs>> {
        argv = await super.builder(argv);

        // add default option
        argv.positional('name', {type: 'string', describe: 'The name monorepo'});
        argv.option('collection', { alias: ['c'], type: 'string', describe: 'A collection of collection to use in generating the initial project.' });
        argv.option('packageManager', { type: 'string', choices: ['pnpm'], default: 'pnpm', describe: 'The package manager project' });
        argv.option('appName', { type: 'string', describe: 'The name application' });
        argv.option('libName', { type: 'string', describe: 'The name library' });

        // add option from schematic [new]
        const {options: {collection}} = this.context.args;
        const collectionName = <string>collection ?? await this.findCollectionBy();
        const workflow = this.getOrCreateWorkflowBuilder(collectionName);
        const schematicOption = await workflow.getSchematicOption(this.schematicName);
        return this.addSchemaOptionsToCommand<any>(argv, schematicOption.values());
    }

    async run(options: ArgOption<NewArgs>): Promise<number | void> {
        const collectionName = options.collection ?? await this.findCollectionBy();
        const { dryRun, force, interactive, defaults, collection, packageManager, appName, libName, ...schematicOptions } = options;

        const packageName = <any>packageManager ?? 'pnpm';
        const pnpmVersion = await PkgManagerFactory.create(packageName).version;
        const ngVersion  = await RunnerFactory.angular().getVersion();

        const workflow = await this.getOrCreateWorkflowExecution(collectionName, {   dryRun, force, interactive, defaults });


        await this.addSmartDefaultProvider({
            dryRun, force, interactive, defaults,
            collectionName,
            providers: {
                "package-version": () => pnpmVersion,
                "package-manager": () => packageName,
                "ngcli-version": () => ngVersion
            }

        });

        schematicOptions.appsDir = schematicOptions.appsDir ?? 'apps';
        schematicOptions.libsDir = schematicOptions.libsDir ?? 'packages';

        if(!schematicOptions.packageManager) {
           // schematicOptions.packageManager = packageName;
        }

        return await this.runSchematic({
            collectionName, schematicOptions,
            schematicName: this.schematicName,
            executionOptions: { dryRun, force, interactive, defaults }
        });
    }

    /** Find a collection from config that has an `dev-new` schematic. */
    private async findCollectionBy(): Promise<string> {
       return this.findCollectionBySchematic(this.schematicName, Collection.NgDevSC);
    }

}