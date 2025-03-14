
import { SpawnOptions } from 'child_process';
import { spawnAsync } from '../utils';
import { CmdInput } from '../commands';
import { CmdOptions } from './cli.interface';

export abstract class AbstractCli {

    protected constructor(
        protected binary: string,
        protected args: string[] = []) { }

    async run(command: string, collect?: boolean, cwd?: string): Promise<any> {
        collect = collect ?? false;
        cwd = cwd ?? process.cwd();

        const options: SpawnOptions = {
            cwd, shell: true,
            stdio: collect ? 'pipe' : 'inherit'
        };

        const newArgs = [...this.args, command];
        return spawnAsync(this.binary, newArgs, options, collect);
    }

    /** Returns [command inputs flags] */
    protected buildCmd({ flags, inputs, command }: CmdOptions): string {
        return `${command} ${this.buildInputs(inputs)} ${this.buildFlags(flags)}`;
    }

    protected buildInputs(inputs: string[] = []): string {
        return inputs.join(' ');
    }

    protected buildFlags(flags: CmdInput[] = []): string {
        return flags.map(({ name, value }) =>`--${name}${value !== undefined ? '=' + value : ''}`).join(' ') ;
    }
}