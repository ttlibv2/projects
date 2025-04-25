import { getLocalPackageUrl, memoize, colors, JSONFile } from '@ngdev/devkit-core/utilities';
import { RunnerFactory } from '@ngdev/devkit-core/runners';
import { Argv } from 'yargs';
import { LocalArgv, RunOptions } from './abstract.cmd';
import { SchematicsCommandArgs, SchematicsCommandModule, SchematicsExecutionOptions } from './schematics.cmd';
import { demandCommandFailureMessage } from './helper/add-cmd-to-args';
import { Option } from './helper/json-schema';
import { RootCommands } from './command.list';
import { Collection as ECollection } from '../collection';
import { paths } from './helper/paths';
import * as path from 'node:path';
import { SmartDefaultProvider } from '@angular-devkit/core/src/json/schema/interface';

interface NgCommandArgs extends SchematicsCommandArgs {
    schematic?: string;
    command?: string;
}



function loadCmdConfigByAngularSc(currentDir: string) {
    const packageUrl = getLocalPackageUrl('@angular/cli', currentDir);
    if (!packageUrl || !paths.existsSync(packageUrl)) throw new Error(`The package [@angular/cli] not install`);
    else return require(paths.join(packageUrl, 'src/commands/command-config'));

}

export default class NgCliCommandModule extends SchematicsCommandModule<NgCommandArgs> {
    command = 'ng <cmd_name>';
    aliases = RootCommands['ng'].aliases;
    describe = 'Generates and/or modifies files based on a schematic.';

    override async builder(localYargs: Argv): Promise<Argv<NgCommandArgs>> {
        localYargs = await super.builder(localYargs);

        const { workspace, args: { options: { help, jsonHelp } } } = this.context;
        if (!workspace && !(help || jsonHelp)) {
            throw new Error(`This command is not available when running the NGDEV CLI outside a workspace.`);
        }

        // create command [new-app | new-lib]
        localYargs.command({
            command: 'app <name>',
            describe: 'Creates and initializes a new Angular application that is the default project for a new workspace.',
            builder: b => this.applyYargsBuilderForAppLib(b, 'application'),
            handler: (options: any) => this.handler({ command: 'app', ...options })
        });

        // create command [new-app | new-lib]
        localYargs.command({
            command: 'lib <name>',
            describe: 'Creates and initializes a new Angular library that is the default project for a new workspace.',
            builder: b => this.applyYargsBuilderForAppLib(b, 'library'),
            handler: (options: any) => this.handler({ command: 'lib', ...options })
        });

        // command [generate] of @angular/cli
        localYargs.command({
            command: 'generate <schematic>',
            aliases: ['g', 'gen'],
            describe: 'Run the provided schematic.',
            builder: b => this.applyBuilderYargsForGenerate(b),
            handler: (options: any) => this.handler({ command: 'generate', ...options })
        });

        localYargs = await this.applyBuilderYargsForAngular(localYargs);
        return localYargs.demandCommand(1, demandCommandFailureMessage) as any;
    }

    async run(argOptions: RunOptions<NgCommandArgs>): Promise<number | void> {
        const { command } = argOptions;
        if (['app', 'lib'].includes(command)) {
           await this.createAngularProjectAppOrLib(argOptions)
        }


        return 1;
    }

    @memoize
    private async getAngularVersion() {
        return await RunnerFactory.angular().getVersion();
    }

    private async createAngularProjectAppOrLib(argOptions: RunOptions<NgCommandArgs>) {
        const projectName: string = argOptions['name'];
        const { root, logger, workspace: { appsDir, libsDir, projects, cli: {ngVersion} } } = this.context;

        if(projects.has(projectName)) {
            let path = colors.bold.red(projects.get(projectName).root);
            let name = colors.bold.red(projectName);
            throw new Error(`Project name [${name}] already exists in path [${path}].`);
        }

        // check version angular
        const globalNgVersion = await this.getAngularVersion();
        if(ngVersion != globalNgVersion) {
            logger.warn(`The Version [Angular CLI] of project and global reference [project:${ngVersion}] != [global:${globalNgVersion}]`);
        }

        const { interactive, dryRun, defaults, force, command, ...options } = argOptions;
        const { collection, ...argsOption } = options;

        const isCreateApp = 'app' == command;
        const baseDir = paths.join(root, isCreateApp ? appsDir : libsDir);
        const projectDir = paths.join(baseDir, projectName);
        if(!paths.existsSync(projectDir)) {
           logger.debug(`Create direction: ${projectDir}`);
            paths.mkdir(projectDir);
        }

        try {
            let collectionName = typeof collection == 'string' ? collection : await this.getCollectionFromConfig('ng-new');
            let defaultOptions = this.context.workspace.getSchematics(`${collectionName}:new-${command}`);
            let angularOption: any =  { ...defaultOptions, ...argsOption, createApplication: false, collection: collectionName };

            //-- Create empty angular project...
            let ngNewOption = await this.copyTargetOption(collectionName, 'ng-new', angularOption);
            let ngScNewOptions = { ...ngNewOption, skipInstall: true, skipGit: true, version: ngVersion };

           await this.runSchematic({
               collectionName, schematicName: 'ng-new', schematicOptions: ngScNewOptions,
               executionOptions: { dryRun, defaults, force, interactive, workflowRoot: baseDir }
           });

            //-- delete files...
            ['.vscode', '.gitignore', 'README.md'].forEach(p => paths.del(p, projectDir));

            //-- create [app | lib] project
            let schematicName = isCreateApp ? 'application' : 'library';
            let appLibOptions = await this.copyTargetOption(collectionName, schematicName, angularOption);

            await this.runSchematic({
                collectionName, schematicName, schematicOptions: appLibOptions,
                executionOptions: { dryRun, defaults, force, interactive, workflowRoot: projectDir }
            });

            // delete file .spec if
            if(!isCreateApp && !!angularOption.skipTests) {
                let ngJsonPath = paths.join(projectDir, 'angular.json');
                let sourceRoot = JSONFile.read(ngJsonPath).get(['projects', projectName, 'root']);
                let files: string[] = paths.files(paths.join(projectDir, sourceRoot)).filter(f => f.includes('.spec.'));
                logger.info(colors.blue('[DELETE]') + files.join(' | '))
                files.forEach(file => paths.del(file, paths.join(projectDir, sourceRoot)));
            }


            // add project to config
            await this.context.workspace.add({
                name: projectName,
                framework: 'angular', projectType: <any>command,
                root:path.relative(root, projectDir).replaceAll('\\', '/')
            });

        }
        catch (err) {
            paths.del(projectDir);
            throw err;
        }

    }

    @memoize
    protected async getCollectionFromConfig(schematicName: string, defaultCollection?: string): Promise<string> {
        return super.getCollectionFromConfig(schematicName, defaultCollection ?? ECollection.Angular);
    }

    @memoize
    protected async getSchematicCollections(): Promise<Set<string>> {
        return new Set([ECollection.Angular]);
    }

    @memoize
    private async getSchematicOptionsByName(collectionName: string, schematicName: string): Promise<{ [name: string]: Option }> {
        const workflow = this.getOrCreateWorkflowForBuilder(collectionName);
        const collection = workflow.engine.createCollection(collectionName);
        const options = await this.getSchematicOptions(collection, schematicName, workflow);
        return options.reduce((json, option) => ({ ...json, [option.name]: option }), {});
    }

    @memoize
    private async copyTargetOption(collectionName: string, schematicName: string, targetOption: any, callback?: (options?: any) => any) {
        let optionNames: string[] = Object.keys(await this.getSchematicOptionsByName(collectionName, schematicName));
        let options = optionNames.filter(name => name in targetOption).reduce((json, name) => ({...json, [name]: targetOption[name]}), {});
        return callback ? callback(options) : options;
    }

    private async applyYargsBuilderForAppLib(localYargs: Argv, schematicName: string): Promise<Argv> {
        const collectionName: string = <any>this.context.args.options['collection'] ?? (await this.getCollectionFromConfig('ng-new'));


        const ngNewOption = await this.getSchematicOptionsByName(collectionName, 'ng-new');
        const ngAppOption = await this.getSchematicOptionsByName(collectionName, schematicName);

        const names = Object.keys(ngAppOption).filter(name => !(name in ngNewOption));
        names.forEach(name => ngNewOption[name] = ngAppOption[name]);

        const deleteOptions = ['packageManager', 'directory', 'createApplication'];
        const includeKeys = Object.keys(ngNewOption).filter(name => !deleteOptions.includes(name));

        const options = includeKeys.map(name => ngNewOption[name]);
        options.push(
            {
                name: 'prime', type: 'boolean', default: true,
                description: 'Use primeng framework'
            },
            {
                name: 'tailwind', type: 'boolean', default: true,
                description: 'Use Tailwind CSS'
            });

        localYargs = localYargs.positional('name', { type: 'string' });
        localYargs = localYargs.option('collection', {
            alias: 'c', type: 'string',
            description: 'A collection of schematics to use'
        });

        return this.addSchemaOptionsToCommand(localYargs, options);
    }

    private async applyBuilderYargsForGenerate(localYargs: LocalArgv): Promise<LocalArgv<any>> {
        localYargs = localYargs.positional('schematic', {
            describe: 'The [collection:schematic] to run.',
            type: 'string',
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
                    'x-deprecated': xDeprecated,
                    description = schematicDescription,
                    hidden = schematicHidden
                } = schemaJson;
                let options: Option[] = await this.getSchematicOptions(collection, schematicName, workflow);

                localYargs = localYargs.command({
                    command: (await this.generateCommandString(collectionName, schematicName, options)), //
                    describe: hidden === true ? false : typeof description === 'string' ? description : '',
                    deprecated: xDeprecated === true || typeof xDeprecated === 'string' ? xDeprecated : false,
                    aliases: Array.isArray(schematicAliases) ? await this.generateCommandAliasesStrings(collectionName, schematicAliases) : undefined,
                    builder: (localYargs) => this.addSchemaOptionsToCommand(localYargs, options).strict(),
                    handler: (options) => this.handler({
                        ...options,
                        schematic: `${collectionName}:${schematicName}`
                    } as any)
                });
            }

        }
        return localYargs;
    }

    private async applyBuilderYargsForAngular(localYargs: LocalArgv) {

        // generate command default of @angular/cli
        const { RootCommands } = loadCmdConfigByAngularSc(process.cwd());// require("D:\\Angular_CLI\\Tool\\node_modules\\@angular\\cli\\src\\commands\\command-config");
        const ALL_COMMAND: { [key: string]: { factory: any, aliases: string[] } } = RootCommands;

        for (const [command, { factory, aliases = [] }] of Object.entries(ALL_COMMAND)) {
            if (['generate', 'new'].includes(command)) continue;

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


}