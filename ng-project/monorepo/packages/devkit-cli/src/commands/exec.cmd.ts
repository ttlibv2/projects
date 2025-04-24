import { Argv } from 'yargs/index.mjs';
import { CommandModule, CommandScope, RunOptions, OtherOptions } from './abstract.cmd';
import { RootCommands } from './command.list';
import { RunnerFactory } from '@ngdev/devkit-core/runners';
import { cmdOptionToString } from '@ngdev/devkit-core/utilities';
import { join } from 'path';

export default class ExecuteCmd extends CommandModule {
    command = 'exec <collection> [schematic]';
    aliases = RootCommands['exec'].aliases;
    describe = 'Execute any collection no matter the workspace';
    scope = CommandScope.Both;

    async builder(yargs: Argv): Promise<Argv> {
        return yargs
            .positional('collection', {
                describe: 'The collection name',
                default: '@schematics/angular',
                alias: ['c']
            })
            .positional('schematic',{
                describe: 'The schematic name',
                alias: ['sc']
            })
            .strictCommands(false)
            .strictOptions(false)
            .strict(false);
    }

    async run(options: RunOptions<any> & OtherOptions): Promise<any> {
        throw new Error(`Method not implements`);
    }

    async run2(options: RunOptions<any> & OtherOptions, other?: Record<string, any>): Promise<any> {
        const {collection, schematic} = options;
        const {unknownOptions, originArgs} = other;

        console.log(
            `ngCli: `, options,
            `\nunknownOptions: `, unknownOptions,
            `\noriginArgs: `, originArgs,
            `\nabc: `, this.context.yargsInstance.getInternalMethods().getCommandInstance().getCommands()
        );

        const argFirst: string[] = originArgs._.slice(1);
        let pos = argFirst.findIndex(s => s.startsWith('--') || s.startsWith('-'));
        let fist_cmd = pos > 0 ? argFirst.slice(0, pos).join(' ') : '';

        if(collection == 'ngcli') {

            if(unknownOptions && schematic == 'new') {
                unknownOptions['package-manager'] = 'pnpm';
            }

            const cmd = Object.entries(unknownOptions).map(e => cmdOptionToString(e[0], e[1])).join(' ');
            const cwd = join(process.cwd(), 'apps');
            //await RunnerFactory.angular().run(`${schematic} ${fist_cmd} ${cmd}`, false, cwd);

        }



    }
}