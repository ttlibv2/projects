import { schema } from "@angular-devkit/core";
import { readFileSync } from "node:fs";
import * as path from "node:path";
import { ArgumentsCamelCase, Argv, CamelCaseKey, CommandModule as YargsCommandModule} from "yargs";
import { Option, addSchemaOptionsToCommand } from "../helper/json-schema";
import { Logger } from "@ngdev/devkit-core/utilities";
import { AbstractPkgManager } from "@ngdev/devkit-core/pkgmanager";
import { DevWorkspace } from "../../workspace";
import { Parser as yargsParser } from "yargs/helpers";
import * as yargs from '../../typings/yargs';

export type ArgOption<T> = { [key in keyof T as CamelCaseKey<key>]: T[key] } & OtherOptions;

export type LocalArgv<T = {}> = Argv<T>;

export enum CommandScope {
  /** Command can only run inside an Angular workspace. */
  In,

  /** Command can only run outside an Angular workspace. */
  Out,

  /** Command can run inside and outside an Angular workspace. */
  Both,
}

export interface CommandContext {
  currentDirectory: string;
  root: string;
  workspace?: DevWorkspace;
  logger: Logger;
  globalConfiguration: DevWorkspace;
  packageManager: AbstractPkgManager;
  originArgs: string[];
  yargsInstance: yargs.Argv;

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

export type OtherOptions = Record<string, any>;

export interface ICommandModule<T extends {} = {}> {

  /** Scope in which the command can be executed in. */
  scope: CommandScope;

  /** Path used to load the long description for the command in JSON utilities text. */
  longDescriptionPath?: string;

  /** array of strings (or a single string) representing aliases of `exports.command`, positional args defined in an alias are ignored */
  aliases?: readonly string[] | string | undefined;

  /** Object declaring the options the command accepts, or a function accepting and returning a yargs instance */
  builder(argv: LocalArgv): Promise<LocalArgv<T>> | LocalArgv<T>;

  /** string (or array of strings) that executes this command when given on the command line, first string may contain positional args */
  command?: readonly string[] | string | undefined;

  /** boolean (or string) to show deprecation notice */
  deprecated?: boolean | string | undefined;

  /** a function which will be passed the parsed argv. */
  run(options: ArgOption<T>): Promise<number | void>;

}

export interface FullDescribe {
  describe?: string;
  longDescription?: string;
  longDescriptionRelativePath?: string;
}

export abstract class CommandModule<T extends {} = {}> implements ICommandModule<T>
{
  abstract readonly command: string;
  abstract readonly describe: string | false;
  readonly scope: CommandScope = CommandScope.Both;

  //readonly shouldReportAnalytics: boolean = true;
  //private readonly optionsWithAnalytics = new Map<string, string>();

  constructor(protected readonly context: CommandContext) {}

  /**
   * Description object which contains the long command descroption.
   * This is used to generate JSON utilities wich is used in AIO.
   *
   * `false` will result in a hidden command.
   */
  get fullDescribe(): FullDescribe | false {
    return this.describe === false
      ? false
      : {
          describe: this.describe,
          ...this.getDesFile(this.longDescriptionPath),
        };
  }

  get longDescriptionPath(): string {
    const fileName = `${this.commandName}.doc.md`;
    return path.join(__dirname, "docs", fileName);
  }

  private getDesFile(desPath?: string) {
    if (desPath === void 0) return {};
    else {
      const from = path.join(__dirname, "../../../..");
      const desRelPath = path
        .relative(from, desPath)
        .replace(/\\/g, path.posix.sep);
      let desText = "";
      try {
        desText = readFileSync(this.longDescriptionPath, "utf8").replace(
          /\r\n/g,
          "\n",
        );
      } catch (err) {
        //
        this.context.logger.warn(
          `Read document: [${this.command}] ==> Message: ${err.message}`,
        );
        desText = "";
      }

      return {
        longDescriptionRelativePath: desRelPath,
        longDescription: desText,
      };
    }
  }

  protected get commandName(): string {
    return this.command.split(" ", 1)[0];
  }

  abstract builder(argv: LocalArgv): Promise<LocalArgv<T>> | LocalArgv<T>;
  abstract run(options: ArgOption<T>): Promise<number | void>;

  async handler(options: ArgumentsCamelCase<T> & OtherOptions): Promise<void> {

    const { _, $0, ...argOptions } = options;

    // Camelize options as yargs will return the object in kebab-case when camel casing is disabled.
    const camelCasedOptions: Record<string, unknown> = {};
    for (const [key, value] of Object.entries(argOptions)) {
      camelCasedOptions[yargsParser.camelCase(key)] = value;
    }

    //origin options
    //const contextOptions: Record<string, any> = {};
    const unknownOptions: Record<string, unknown> = {};
    const {help, jsonHelp, getYargsCompletions, ...unknownArgs} = this.context.args.options;
    for (const [key, value] of Object.entries(unknownArgs)) {
      unknownOptions[yargsParser.decamelize(key)] = value;
    }


    let exitCode: number | void | undefined;
    try {

      exitCode = await this.run(camelCasedOptions as any);
    } //
    catch (e) {
      if (e instanceof schema.SchemaValidationException) {
        this.context.logger.fatal(`Error: ${e.message}`);
        exitCode = 1;
      } else {
        throw e;
      }
    } finally {
      // await stopPeriodicFlushes?.();

      if (typeof exitCode === "number" && exitCode > 0) {
        process.exitCode = exitCode;
      }
    }
  }

  /**
   * Adds schema options to a command also this keeps track of options that are required for analytics.
   * **Note:** This method should be called from the command bundler method.
   */
  protected addSchemaOptionsToCommand<T>(localYargs: Argv<T>, options: Option[]): Argv<T> {

    addSchemaOptionsToCommand(localYargs, options,
      this.context.args.options.help,
    );

    return localYargs;
  }

  protected getWorkspaceOrThrow(): DevWorkspace {
    const { workspace } = this.context;
    if (!workspace) throw new CommandModuleError("A workspace is required for this command.");
    return workspace;
  }

}

/**
 * Creates an known command module error.
 * This is used so during executation we can filter between known validation error and real non handled errors.
 */
export class CommandModuleError extends Error {}