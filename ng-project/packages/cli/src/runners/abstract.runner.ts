import { red } from 'ansis';
import { ChildProcess, spawn, SpawnOptions } from 'child_process';
import { MESSAGES } from '../logui/messages';
import { Logger } from '../help/logger';

export function createRunnerLogger(nameOrLogger: string | Logger = 'runner') {
  if (nameOrLogger instanceof Logger) return nameOrLogger;
  else {
    const newName = nameOrLogger == 'runner' || nameOrLogger.endsWith('-runner') ? nameOrLogger : `${nameOrLogger}-runner`;
    return Logger.create(newName);
  }
}

export interface CmdOptions {
  command?: string;
  inputs: string[];
  flags: CmdInput[];
}

export interface CmdInput {
  name: string;
  value?: boolean | string;
  options?: any;
}

export abstract class AbstractRunner {

  constructor(
    protected binary: string,
    protected args: string[] = [],
    protected logger?: string | Logger) {
    this.logger = createRunnerLogger(logger);
  }

  async run(command: string, collect?: boolean, cwd?: string): Promise<string | null> {
    collect = collect ?? false;
    cwd = cwd ?? process.cwd();

    const options: SpawnOptions = {
      cwd, shell: true,
      stdio: collect ? 'pipe' : 'inherit'
    };


    //return await spawnAsync(`node`, [this.binary, command], options, collect);
    return await spawnAsync(this.binary, [...this.args, command], options, collect,);
  }

  /**
  * @param command
  * @returns The entire command that will be ran when calling `run(command)`.
  */
  rawFullCommand(command: string): string {
    const commandArgs: string[] = [...this.args, command];
    return `${this.binary} ${commandArgs.join(' ')}`;
  }

  protected buildCommandLine({command,inputs,flags}: CmdOptions): string {
    return `${command} ${this.buildInputs(inputs)} ${this.buildFlags(flags)}`;
  }

  protected buildInputs(inputs: string[] = []): string {
    return inputs.join(' ');
  }

  protected buildFlags(flags: CmdInput[] = []): string {
    return flags.map(({ name, value }) =>`--${name}${value !== undefined ? '=' + value : ''}`).join(' ');
  }

}

function spawnAsync(command: string, args: string[], options: SpawnOptions, collect: boolean = false): Promise<string | null> {
  return new Promise<string | null>((resolve, reject) => {
    const child: ChildProcess = spawn(command, args, options);

    if (collect) {
      child.stdout!.on('data', (data) =>
        resolve(data.toString().replace(/\r\n|\n/, '')),
      );
    }

    child.on('close', (code) => {
      if (code === 0) { resolve(null); }
      else {
        const msg = MESSAGES.RUNNER_EXECUTION_ERROR(`${command}`);
        //writeErrorToLogFile(msg);
        console.error(red(msg));
        reject();
      }
    });

    child.on('error', (error: any) => {
      console.error(`Spawn error: ${error.message}`);
      process.exit(1);
    });


  });
}
