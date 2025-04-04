import { Argv } from "yargs";
import { CommandModule, Options, OtherOptions } from "./abstract.cmd";
import { RootCommands } from "./command.list";

export default class ExecuteCmd extends CommandModule {
  command = 'execute <collection> <schematic>';
  aliases = RootCommands['execute'].aliases;
  describe = 'Execute any collection no matter the workspace';

  async builder(yargs: Argv): Promise<Argv> {
    yargs.positional('collection', {
      type: 'string',
      alias: ['c'],
      description: 'Select collection',
      demandOption: true
    });

    yargs.positional('schematic', {
      type: 'string',
      alias: ['c'],
      description: 'Select schematic',
      demandOption: true
    });

    return yargs;
  }

  run(options: Options<{}> & OtherOptions): Promise<any> {
    throw new Error("Method not implemented.");
  }


}