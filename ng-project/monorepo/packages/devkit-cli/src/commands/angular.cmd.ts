import { RunSchematicOption, SchematicArg, SchematicsCommand } from './core/schematics.cmd';
import { ArgOption, CommandScope, LocalArgv } from './core/abstract.cmd';
import { loadCmdConfig } from '../utilities/load-cmd-config';

interface AngularCommandArg extends SchematicArg {
    command?: string;
}

export default class AngularCommand extends SchematicsCommand<AngularCommandArg> {
    readonly command = 'ng';
    readonly describe = 'The command angular cli';
    readonly scope = CommandScope.In;

    async builder(argv: LocalArgv): Promise<LocalArgv<AngularCommandArg>> {
        let localYargs: LocalArgv<any> = await super.builder(argv);

        // validate angular cli version in all project..
        await this.validateAngularCliVersion();

        // build all command angular cli exclude [new, generate]
        localYargs = await this.applyBuilderYargsForAngular(localYargs);

        // create command [new-app | new-lib]
        localYargs.command({
            command: 'new-app <name>', aliases: ['app'],
            describe: 'Create angular application project.',
            builder: b => this.applyYargsBuilderForAppLib(b, 'app'),
            handler: (options: any) => this.handler({ command: 'new-app', ...options })
        });

        // create command [new-app | new-lib]
        localYargs.command({
            command: 'new-lib <name>', aliases: ['lib'],
            describe: 'Create angular library project.',
            builder: b => this.applyYargsBuilderForAppLib(b, 'lib'),
            handler: (options: any) => this.handler({ command: 'new-lib', ...options })
        });


        return localYargs as any;
    }

    async run(options: ArgOption<AngularCommandArg>): Promise<number | void> {
        const workingDir = await this.determineWorkDir(options);
        console.warn(`workingDir: `, workingDir);
        return Promise.resolve(undefined);
    }

    private async applyYargsBuilderForAppLib(localYargs: LocalArgv, name: 'app' | 'lib') {
        const { collection: collectionArg = '@schematics/angular' } = this.context.args.options;
        const schematicNgNew = `ng-new-${name}`, ngNewName = 'ng-new';
        const ngAppLibSchematicName = name == 'app' ? 'application' : 'library';

        console.warn(`collectionArg`, collectionArg);


        if (collectionArg) {
            const workflow = this.getOrCreateWorkflowBuilder(<string>collectionArg);

            // Step1: trial apply schematic [ng-new-app] if exist
            if (workflow.existSchematic(schematicNgNew)) {
                const map = await workflow.getSchematicOption(schematicNgNew);
                this.addSchemaOptionsToCommand(localYargs, map.values());
            }

            // Step2: check schematic [application + ng-new]
            else {
                const hasNgNew = workflow.existSchematic(ngNewName);
                if(!hasNgNew) throw this.createErrorSchematic404(collectionArg, ngNewName);

                const hasAppLib = workflow.existSchematic(ngAppLibSchematicName);
                if(!hasAppLib) throw this.createErrorSchematic404(collectionArg, ngAppLibSchematicName);

                const mapNgNew = await workflow.getSchematicOption(ngNewName);

                // copy schematic to [mapNgNew]
                const mapNgAppLib = await workflow.getSchematicOption(ngAppLibSchematicName);
                mapNgAppLib.forEach((opt, name) => mapNgNew.putIfAbsent(name, opt));

                // delete + update schematic
                mapNgNew.removeAll('createApplication');
                mapNgNew.update('packageManager', s => {s.choices = ['pnpm'];s.default = 'pnpm';});
                mapNgNew.set('primeng', {name: 'primeng', type: 'boolean', default: true, describe: ''} as any);
                mapNgNew.set('tailwind', {name: 'tailwind', type: 'boolean', default: true, describe: ''} as any);

                // build option
                this.addSchemaOptionsToCommand(localYargs, [...mapNgNew.values()]);
            }
        }


        // apply custom option
       //localYargs.option('collection', {type: 'string', alias: ['c']})

        return localYargs;
    }

    private async applyBuilderYargsForAngular(localYargs: LocalArgv) {
        const { RootCommands } = await loadCmdConfig(process.cwd());
        const ALL_COMMAND: { [key: string]: { factory: any, aliases: string[] } } = RootCommands;

        for (const [command, { factory, aliases = [] }] of Object.entries(ALL_COMMAND)) {
            // if (['generate', 'new'].includes(command)) continue;

            const { default: commandModule } = await factory();
            const newCls = new commandModule(this.context);

            localYargs.command({
                command: newCls.command,
                describe: newCls.describe,
                aliases: [...aliases],
                builder: b => newCls.builder(b),
                handler: (options: any) => this.handler({ command, ...options })
            });
        }

        return localYargs;

    }

    private async determineWorkDir(options: any): Promise<string> {
        const { command, project: projectName } = options;

        if (['new-lib', 'new-app'].includes(command)) {
            const wb = this.context.workspace;
            if (!wb) throw new Error('Angular CLI outside folder [NgDev]');
            else if ('new-lib' == command) return wb.libsDir;
            else return wb.appsDir;
        }//
        else if (projectName) {
            const project = this.context.workspace.getProject(projectName);
            if (!project) throw new Error(`The project [${projectName}] not exists.`);
            else return project.root;
        }//
        else {
            throw new Error(`Please input option [--project].`);
        }
    }

    private async validateAngularCliVersion(): Promise<void> {
    }

    private async applyCollectionToArgv(localYargs: LocalArgv) {
    }

    private createErrorSchematic404(collection: any, schematic: string): Error {
        return new Error(`The schematic [${schematic}] not exist in collection [${collection}]`);
    }

}