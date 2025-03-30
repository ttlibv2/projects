import { Argv } from 'yargs';
import { CommandContext, CommandModule, CommandModuleImplementation } from '../abstract.cmd';
export declare const demandCommandFailureMessage = "You need to specify a command before moving on. Use '--help' to view the available commands.";
export type CommandModuleConstructor = Partial<CommandModuleImplementation> & {
    new (context: CommandContext): Partial<CommandModuleImplementation> & CommandModule;
};
export declare function addCommandModuleToYargs<T extends object, U extends CommandModuleConstructor>(localYargs: Argv<T>, commandModule: U, context: CommandContext): Argv<T>;
