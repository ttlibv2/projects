import { Argv } from 'src/typings/yargs';
import { CommandModuleImplementation, CommandScope, Options, OtherOptions } from './abstract.cmd';
import { SchematicsCommandArgs, SchematicsCommandModule } from './schematics.cmd';
interface NewCommandArgs extends SchematicsCommandArgs {
    collection?: string;
}
export default class NewCommandModule extends SchematicsCommandModule implements CommandModuleImplementation<NewCommandArgs> {
    private readonly schematicName;
    scope: CommandScope;
    protected allowPrivateSchematics: boolean;
    command: string;
    aliases: string[] | undefined;
    describe: string;
    builder(argv: Argv): Promise<Argv<NewCommandArgs>>;
    run(options: Options<NewCommandArgs> & OtherOptions): Promise<number | void>;
    /** Find a collection from config that has an `ng-new` schematic. */
    private getCollectionFromConfig;
}
export {};
