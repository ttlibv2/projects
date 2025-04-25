import { red } from 'ansis';
import { ChildProcess, spawn, SpawnOptions } from 'child_process';
import { MESSAGES } from '../logui/messages';
import { Logger } from '../utilities/logger';
import { colors } from '../utilities/color';
import * as process from 'node:process';
import { cmdOptionToString } from "../utilities";

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
  flags: Record<string, string | boolean | number | unknown>;
}

// export interface CmdInput {
//   name: string;
//   value?: boolean | string;
//   options?: any;
// }

export type FlagCmd = Record<string, string | boolean | number | unknown>;
export type RunCmd = string | Record<string, string | boolean | number | unknown>;

export interface RunOptions {
  collect?: boolean; cwd?: string;showLog?: boolean;
}

export abstract class AbstractRunner {

  constructor(
    protected binary: string,
    protected args: string[] = [],
    protected logger?: string | Logger) {
    this.logger = createRunnerLogger(logger);
  }

  async run(command: string, options?: RunOptions): Promise<string | null> {
    options = Object.assign({collect: false, cwd: process.cwd()}, options);

    const spawnOptions: SpawnOptions = {
      cwd: options.cwd, shell: true,
      stdio: options.collect ? 'pipe' : 'inherit'
    };

    return await spawnAsync(this.binary, [...this.args, command], spawnOptions, options);
  }

  /**
  * @param command
  * @returns The entire command that will be ran when calling `run(command)`.
  */
  rawFullCommand(command: string): string {
    const commandArgs: string[] = [...this.args, command];
    return `${this.binary} ${commandArgs.join(' ')}`;
  }

  getVersion(cwd?: string): Promise<string | null> {
    return this.run('--version', { collect: true, cwd });
  }

  buildCommandLine({command,inputs,flags}: CmdOptions): string {
    const segments = [command, this.buildInputs(inputs), this.buildFlags(flags)];
    return segments.filter(s => s != '').join(' ');
  }

  protected buildInputs(inputs: string[] = []): string {
    return inputs.join(' ');
  }

  protected buildFlags(flags: FlagCmd): string {
    return Object.entries(flags).map(op => cmdOptionToString(op[0], op[1])).join(' ');
  }

}

function spawnAsync(command: string, args: string[], spawnOptions: SpawnOptions, other?: RunOptions): Promise<string | null> {
  return new Promise<string | null>((resolve, reject) => {
    spawnOptions = Object.assign({collect: false, cwd: process.cwd()}, spawnOptions);

    if(!!other?.showLog) {
      console.debug(`[DEBUG] ${command} ${args.join(' ')}`);
    }

    const child: ChildProcess = spawn(command, args, spawnOptions);

    if (!!other?.collect) {
      child.stdout!.on('data', (data) =>
        resolve(data.toString().replace(/\r\n|\n/, '')),
      );
    }

    child.on('close', (code) => {
      if (code === 0) { resolve(null); }
      else {
        const msg = MESSAGES.RUNNER_EXECUTION_ERROR(`${command}`);
        //writeErrorToLogFile(msg);
        console.error(`close: `, red(msg));
        reject();
      }
    });

    child.on('error', (error: any) => {
      console.error(`error: `, `Spawn error: ${error.message}`);
      if(error instanceof Error) console.log(error.stack, error.cause);
      process.exit(1);
    });


  });
}