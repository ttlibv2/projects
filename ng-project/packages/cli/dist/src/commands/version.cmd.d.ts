import { Argv } from 'yargs';
import { CommandModule, CommandModuleImplementation } from './abstract.cmd';
export default class VersionCommandModule extends CommandModule implements CommandModuleImplementation {
    command: string;
    aliases: string[] | undefined;
    describe: string;
    builder(localYargs: Argv): Argv;
    run(): Promise<void>;
    private getVersion;
}
