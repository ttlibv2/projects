import { Argv } from 'yargs';
import { CommandModuleImplementation, CommandScope, Options, OtherOptions } from '../core/command-module';
import { SchematicsCommandArgs, SchematicsCommandModule } from '../core/schematics-command-module';
interface NewCommandArgs extends SchematicsCommandArgs {
    collection?: string;
}
export default class NewCommandModule extends SchematicsCommandModule implements CommandModuleImplementation<NewCommandArgs> {
    private readonly schematicName;
    scope: CommandScope;
    protected allowPrivateSchematics: boolean;
    command: string;
    aliases: string[];
    describe: string;
    longDescriptionPath: string;
    builder(argv: Argv): Promise<Argv<NewCommandArgs>>;
    run(options: Options<NewCommandArgs> & OtherOptions): Promise<number | void>;
    /** Find a collection from config that has an `ng-new` schematic. */
    private getCollectionFromConfig;
}
export {};
//# sourceMappingURL=cli.d.ts.map