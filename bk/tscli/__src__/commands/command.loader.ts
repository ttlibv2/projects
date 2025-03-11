import { Command } from "commander";
import { red } from 'ansis';

export class CommandLoader {
    public static async load(program: Command): Promise<void> {
    //   new NewCommand(new NewAction()).load(program);
    //   new BuildCommand(new BuildAction()).load(program);
    //   new StartCommand(new StartAction()).load(program);
    //   new InfoCommand(new InfoAction()).load(program);
    //   new AddCommand(new AddAction()).load(program); 
    //   await new GenerateCommand(new GenerateAction()).load(program);
  
      this.handleInvalidCommand(program);
    }
  
    private static handleInvalidCommand(program: Command) {
      program.on('command:*', () => {
        console.error(
          `\n${Error} Invalid command: ${red`%s`}`,
          program.args.join(' '),
        );
        console.log(
          `See ${red`--help`} for a list of available commands.\n`,
        );
        process.exit(1);
      });
    }
  }