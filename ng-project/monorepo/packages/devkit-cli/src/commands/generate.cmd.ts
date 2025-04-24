import { strings } from '@angular-devkit/core';
import { Collection } from '@angular-devkit/schematics';
import { FileSystemCollectionDescription, FileSystemSchematicDescription } from '@angular-devkit/schematics/tools';
import { CommandModuleError, RunOptions, OtherOptions, LocalArgv } from "./abstract.cmd";
import { SchematicsCommandArgs, SchematicsCommandModule } from './schematics.cmd';
import { demandCommandFailureMessage } from './helper/add-cmd-to-args';
import { Option } from './helper/json-schema';
import { RootCommands } from './command.list';

interface GenerateCommandArgs extends SchematicsCommandArgs {
    schematic?: string;
}

export default class GenerateCommandModule extends SchematicsCommandModule<GenerateCommandArgs> {
    command = 'generate <schematic>';
    aliases = RootCommands['generate'].aliases;
    describe = 'Generates and/or modifies files based on a schematic.';

    override async builder(argv: LocalArgv): Promise<LocalArgv<GenerateCommandArgs>> {
        let localYargs = (await super.builder(argv)).command({
            command: '$0 <schematic>',
            describe: 'Run the provided schematic.',
            handler: (options) => this.handler(options as any),
            builder: (localYargs) => localYargs
                    .positional('schematic', {
                        describe: 'The [collection:schematic] to run.',
                        type: 'string', demandOption: true
                    })
                    .strict()
        });

        for (const [
            schematicName,
            collectionName,
        ] of await this.getSchematicsToRegister()) {
            const workflow = this.getOrCreateWorkflowForBuilder(collectionName);
            const collection = workflow.engine.createCollection(collectionName);

            const {
                description: {
                    schemaJson,
                    aliases: schematicAliases,
                    hidden: schematicHidden,
                    description: schematicDescription
                }
            } = collection.createSchematic(schematicName, true);

            if (!schemaJson) {
                continue;
            }

            const {
                'x-deprecated': xDeprecated,
                description = schematicDescription,
                hidden = schematicHidden
            } = schemaJson;

            const options = await this.getSchematicOptions(collection, schematicName, workflow);

            localYargs = localYargs.command({
                command: await this.generateCommandString(collectionName, schematicName, options),
                describe: hidden === true ? false : typeof description === 'string' ? description : '',
                deprecated: xDeprecated === true || typeof xDeprecated === 'string' ? xDeprecated : false,
                aliases: Array.isArray(schematicAliases) ? await this.generateCommandAliasesStrings(collectionName, schematicAliases) : undefined,
                builder: (localYargs) => this.addSchemaOptionsToCommand(localYargs, options).strict(),
                handler: (options) => this.handler({ ...options, schematic: `${collectionName}:${schematicName}` })
            });
        }

        return localYargs.demandCommand(1, demandCommandFailureMessage);
    }

    async run(options: RunOptions<GenerateCommandArgs> & OtherOptions): Promise<number | void> {
        const { dryRun, schematic, defaults, force, interactive, framework, ...schematicOptions } = options;
        const [collectionName, schematicName] = this.parseSchematicInfo(schematic);

        if (!collectionName || !schematicName) {
            throw new CommandModuleError('A collection and schematic is required during execution.');
        }

        return this.runSchematic({
            collectionName, schematicName, schematicOptions,
            executionOptions: { dryRun, defaults, force, interactive }
        });


    }


}