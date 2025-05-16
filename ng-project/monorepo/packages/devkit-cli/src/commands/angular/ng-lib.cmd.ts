import { RootCommands } from '../command.list';
import { NgNewArg, NgNewCommand } from './ng-new.cmd';

export default class NgNewLibCommand extends NgNewCommand<NgNewArg> {
    readonly command = "ng lib <name>";
    readonly aliases = RootCommands['ng-lib'].aliases;
    readonly describe = 'Creates a new angular library project';

}