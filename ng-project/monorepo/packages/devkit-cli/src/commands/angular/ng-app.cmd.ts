import { RootCommands } from '../command.list';
import { NgNewArg, NgNewCommand } from './ng-new.cmd';
import { LocalArgv } from '../core/abstract.cmd';

export default class NgNewAppCommand extends NgNewCommand<NgNewArg> {
    readonly aliases = RootCommands['ng-app'].aliases;
    readonly describe = 'Creates a new angular application project';

    async builder(argv: LocalArgv) {
        argv = await super.builder(argv);
        return argv as any;
    }



}