import { Logger } from '../utilities/logger';
export declare function createRunnerLogger(nameOrLogger?: string | Logger): Logger;
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
export declare abstract class AbstractRunner {
    protected binary: string;
    protected args: string[];
    protected logger?: (string | Logger) | undefined;
    constructor(binary: string, args?: string[], logger?: (string | Logger) | undefined);
    run(command: string, collect?: boolean, cwd?: string): Promise<string | null>;
    /**
    * @param command
    * @returns The entire command that will be ran when calling `run(command)`.
    */
    rawFullCommand(command: string): string;
    protected buildCommandLine({ command, inputs, flags }: CmdOptions): string;
    protected buildInputs(inputs?: string[]): string;
    protected buildFlags(flags?: CmdInput[]): string;
}
