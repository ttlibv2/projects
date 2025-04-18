import { schema } from "@angular-devkit/core";
import { readFileSync } from "node:fs";
import * as path from "node:path";
import {
  ArgumentsCamelCase,
  Argv,
  CamelCaseKey,
  CommandModule as YargsCommandModule,
} from "yargs";
import { Option, addSchemaOptionsToCommand } from "./helper/json-schema";
import { Logger } from "@ngdev/devkit-core/utilities";
import { AbstractPkgManager } from "@ngdev/devkit-core/pkgmanager";
import { DevWorkspace } from "../workspace";
import { Parser as yargsParser } from "yargs/helpers";
import a from 'ansis';
import * as yargs from '../typings/yargs';

export type Options<T> = { [key in keyof T as CamelCaseKey<key>]: T[key] };

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

export type OtherOptions = Record<string, unknown>;

export interface CommandModuleImplementation<T extends {} = {}>
  extends Omit<YargsCommandModule<{}, T>, "builder" | "handler"> {
  /** Scope in which the command can be executed in. */
  scope: CommandScope;

  /** Path used to load the long description for the command in JSON utilities text. */
  longDescriptionPath?: string;

  /** Object declaring the options the command accepts, or a function accepting and returning a yargs instance. */
  builder(argv: Argv): Promise<Argv<T>> | Argv<T>;

  /** A function which will be passed the parsed argv. */
  run(
    options: Options<T> & OtherOptions,
  ): Promise<number | void> | number | void;
}

export interface FullDescribe {
  describe?: string;
  longDescription?: string;
  longDescriptionRelativePath?: string;
}

export abstract class CommandModule<T extends {} = {}>
  implements CommandModuleImplementation<T>
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
  public get fullDescribe(): FullDescribe | false {
    return this.describe === false
      ? false
      : {
          describe: this.describe,
          ...this.getDesFile(this.longDescriptionPath),
        };
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

  public get longDescriptionPath(): string {
    return path.join(__dirname, "docs", `${this.commandName}.doc.md`);
  }

  abstract builder(localYargs: Argv): Promise<Argv<T>> | Argv<T>;
  abstract run(
    options: Options<T> & OtherOptions,
    unknownOptions?: Record<string, unknown>
  ): Promise<number | void> | number | void;

  async handler(args: ArgumentsCamelCase<T> & OtherOptions): Promise<void> {
    const { _, $0, ...options } = args;

    // Camelize options as yargs will return the object in kebab-case when camel casing is disabled.
    const camelCasedOptions: Record<string, unknown> = {};
    for (const [key, value] of Object.entries(options)) {
      camelCasedOptions[yargsParser.camelCase(key)] = value;
    }

    //origin options
    //const contextOptions: Record<string, any> = {};
    const unknownOptions: Record<string, unknown> = {};
    const {help, jsonHelp, getYargsCompletions, ...unknownArgs} = this.context.args.options;
    for (const [key, value] of Object.entries(unknownArgs)) {
      unknownOptions[yargsParser.decamelize(key)] = value;
    }

    // Set up autocompletion if appropriate.
    // const autocompletionExitCode = await considerSettingUpAutocompletion(
    //   this.commandName, this.context.logger);
    //
    // if (autocompletionExitCode !== undefined) {
    //   process.exitCode = autocompletionExitCode;
    //
    //   return;
    // }

    // Gather and report analytics.
    // const analytics = await this.getAnalytics();
    // const stopPeriodicFlushes = analytics && analytics.periodFlush();

    let exitCode: number | void | undefined;
    try {
      // if (analytics) {
      //   this.reportCommandRunAnalytics(analytics);
      //   this.reportWorkspaceInfoAnalytics(analytics);
      // }

      exitCode = await this.run(camelCasedOptions as Options<T> & OtherOptions, { unknownOptions, originArgs: args });
    } catch (e) {
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

  // @memoize
  // protected async getAnalytics(): Promise<AnalyticsCollector | undefined> {
  //   if (!this.shouldReportAnalytics) {
  //     return undefined;
  //   }

  //   const userId = await getAnalyticsUserId(
  //     this.context,
  //     // Don't prompt on `ng update`, 'ng version' or `ng analytics`.
  //     ['version', 'update', 'analytics'].includes(this.commandName),
  //   );

  //   return userId ? new AnalyticsCollector(this.context, userId) : undefined;
  // }

  /**
   * Adds schema options to a command also this keeps track of options that are required for analytics.
   * **Note:** This method should be called from the command bundler method.
   */
  protected addSchemaOptionsToCommand<T>(
    localYargs: Argv<T>,
    options: Option[],
  ): Argv<T> {
    const optionsWithAnalytics = addSchemaOptionsToCommand(
      localYargs,
      options,
      this.context.args.options.help,
    );

    // Record option of analytics.
    //for (const [name, userAnalytics] of optionsWithAnalytics) {
    //this.optionsWithAnalytics.set(name, userAnalytics);
    //}

    return localYargs;
  }

  protected getWorkspaceOrThrow(): DevWorkspace {
    const { workspace } = this.context;
    if (!workspace)
      throw new CommandModuleError("A workspace is required for this command.");
    return workspace;
  }

  // /**
  //  * Flush on an interval (if the event loop is waiting).
  //  *
  //  * @returns a method that when called will terminate the periodic
  //  * flush and call flush one last time.
  //  */
  // protected getAnalyticsParameters(
  //   options: (Options<T> & OtherOptions) | OtherOptions,
  // ): Partial<Record<EventCustomDimension | EventCustomMetric, string | boolean | number>> {
  //   const parameters: Partial<
  //     Record<EventCustomDimension | EventCustomMetric, string | boolean | number>
  //   > = {};

  //   const validEventCustomDimensionAndMetrics = new Set([
  //     ...Object.values(EventCustomDimension),
  //     ...Object.values(EventCustomMetric),
  //   ]);

  //   for (const [name, ua] of this.optionsWithAnalytics) {
  //     const value = options[name];
  //     if (
  //       (typeof value === 'string' || typeof value === 'number' || typeof value === 'boolean') &&
  //       validEventCustomDimensionAndMetrics.has(ua as EventCustomDimension | EventCustomMetric)
  //     ) {
  //       parameters[ua as EventCustomDimension | EventCustomMetric] = value;
  //     }
  //   }

  //   return parameters;
  // }

  // private reportCommandRunAnalytics(analytics: AnalyticsCollector): void {
  //   // eslint-disable-next-line @typescript-eslint/no-explicit-any
  //   const internalMethods = (yargs as any).getInternalMethods();
  //   // $0 generate component [name] -> generate_component
  //   // $0 add <collection> -> add
  //   const fullCommand = (internalMethods.getUsageInstance().getUsage()[0][0] as string)
  //     .split(' ')
  //     .filter((x) => {
  //       const code = x.charCodeAt(0);

  //       return code >= 97 && code <= 122;
  //     })
  //     .join('_');

  //   analytics.reportCommandRunEvent(fullCommand);
  // }

  // private reportWorkspaceInfoAnalytics(analytics: AnalyticsCollector): void {
  //   const { workspace } = this.context;
  //   if (!workspace) {
  //     return;
  //   }

  //   let applicationProjectsCount = 0;
  //   let librariesProjectsCount = 0;
  //   for (const project of workspace.projects.values()) {
  //     switch (project.extensions['projectType']) {
  //       case 'app':
  //         applicationProjectsCount++;
  //         break;
  //       case 'library':
  //         librariesProjectsCount++;
  //         break;
  //     }
  //   }

  //   analytics.reportWorkspaceInfoEvent({
  //     [EventCustomMetric.AllProjectsCount]: librariesProjectsCount + applicationProjectsCount,
  //     [EventCustomMetric.ApplicationProjectsCount]: applicationProjectsCount,
  //     [EventCustomMetric.LibraryProjectsCount]: librariesProjectsCount,
  //   });
  // }
}

/**
 * Creates an known command module error.
 * This is used so during executation we can filter between known validation error and real non handled errors.
 */
export class CommandModuleError extends Error {}