import { Argv } from 'yargs';
import { CommandModule, CommandModuleImplementation, Options, OtherOptions } from '../../command-builder/command-module';
import { RootCommands } from '../all';

export default class InfoCommand extends CommandModule {
  command = 'version';
  aliases = RootCommands['version'].aliases;
  describe = 'Outputs CLI version.';
  longDescriptionPath?: string | undefined;

  builder(localYargs: Argv): Argv {
    return localYargs;
  }

  run(options: Options<{}> & OtherOptions): Promise<number | void> | number | void {
    
  }
}