import { logging } from '@angular-devkit/core';
import { ArgumentsCamelCase, Argv, CommandModule as YargsCommandModule } from 'yargs';
import { AngularWorkspace } from '../../utilities/config';
import { PackageManagerUtils } from '../../utilities/pkg-manager';
import { Option } from '../../utilities/json-schema';
/** Convert literal string types like 'foo-bar' to 'FooBar' */
export type PascalCase<S extends string> = string extends S ? string : S extends `${infer T}-${infer U}` ? `${Capitalize<T>}${PascalCase<U>}` : Capitalize<S>;
/** Convert literal string types like 'foo-bar' to 'fooBar' */
export type CamelCase<S extends string> = string extends S ? string : S extends `${infer T}-${infer U}` ? `${T}${PascalCase<U>}` : S;
export type CamelCaseKey<K extends PropertyKey> = K extends string ? Exclude<CamelCase<K>, ""> : K;
export type Options<T> = {
    [key in keyof T]: T[key];
};
export declare enum CommandScope {
    /** Command can only run inside an Angular workspace. */
    In = 0,
    /** Command can only run outside an Angular workspace. */
    Out = 1,
    /** Command can run inside and outside an Angular workspace. */
    Both = 2
}
export interface CommandContext {
    currentDirectory: string;
    root: string;
    workspace?: AngularWorkspace;
    globalConfiguration: AngularWorkspace;
    logger: logging.Logger;
    packageManager: PackageManagerUtils;
    /** Arguments parsed in free-from without parser configuration. */
    args: {
        positional: string[];
        options: {
            help: boolean;
            jsonHelp: boolean;
            getYargsCompletions: boolean;
        } & Record<string, unknown>;
    };
}
export type OtherOptions = Record<string, unknown>;
export interface CommandModuleImplementation<T extends {} = {}> extends Omit<YargsCommandModule<{}, T>, 'builder' | 'handler'> {
    /** Scope in which the command can be executed in. */
    scope: CommandScope;
    /** Path used to load the long description for the command in JSON help text. */
    longDescriptionPath?: string;
    /** Object declaring the options the command accepts, or a function accepting and returning a yargs instance. */
    builder(argv: Argv): Promise<Argv<T>> | Argv<T>;
    /** A function which will be passed the parsed argv. */
    run(options: Options<T> & OtherOptions): Promise<number | void> | number | void;
}
export interface FullDescribe {
    describe?: string;
    longDescription?: string;
    longDescriptionRelativePath?: string;
}
export declare const demandCommandFailureMessage = "You need to specify a command before moving on. Use '--help' to view the available commands.";
export type CommandModuleConstructor = Partial<CommandModuleImplementation> & {
    new (context: CommandContext): Partial<CommandModuleImplementation> & CommandModule;
};
export declare function addCommandModuleToYargs<T extends object, U extends CommandModuleConstructor>(localYargs: Argv<T>, commandModule: U, context: CommandContext): Argv<T>;
export declare abstract class CommandModule<T extends {} = {}> implements CommandModuleImplementation<T> {
    protected readonly context: CommandContext;
    abstract readonly command: string;
    abstract readonly describe: string | false;
    abstract readonly longDescriptionPath?: string;
    protected readonly shouldReportAnalytics: boolean;
    readonly scope: CommandScope;
    private readonly optionsWithAnalytics;
    constructor(context: CommandContext);
    /**
     * Description object which contains the long command descroption.
     * This is used to generate JSON help wich is used in AIO.
     *
     * `false` will result in a hidden command.
     */
    get fullDescribe(): FullDescribe | false;
    protected get commandName(): string;
    abstract builder(argv: Argv): Promise<Argv<T>> | Argv<T>;
    abstract run(options: Options<T> & OtherOptions): Promise<number | void> | number | void;
    handler(args: ArgumentsCamelCase<T> & OtherOptions): Promise<void>;
    /**
     * Adds schema options to a command also this keeps track of options that are required for analytics.
     * **Note:** This method should be called from the command bundler method.
     */
    protected addSchemaOptionsToCommand<T>(localYargs: Argv<T>, options: Option[]): Argv<T>;
    protected getWorkspaceOrThrow(): AngularWorkspace;
}
/**
 * Creates an known command module error.
 * This is used so during executation we can filter between known validation error and real non handled errors.
 */
export declare class CommandModuleError extends Error {
}
//# sourceMappingURL=command-module.d.ts.map