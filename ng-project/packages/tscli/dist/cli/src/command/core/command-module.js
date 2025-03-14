"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
exports.CommandModuleError = exports.CommandModule = exports.demandCommandFailureMessage = exports.CommandScope = void 0;
exports.addCommandModuleToYargs = addCommandModuleToYargs;
const tslib_1 = require("tslib");
const core_1 = require("@angular-devkit/core");
const node_fs_1 = require("node:fs");
const path = tslib_1.__importStar(require("node:path"));
const helpers_1 = require("yargs/helpers");
// import { getAnalyticsUserId } from '../analytics/analytics';
// import { AnalyticsCollector } from '../analytics/analytics-collector';
// import { EventCustomDimension, EventCustomMetric } from '../analytics/analytics-parameters';
const completion_1 = require("../../utilities/completion");
const json_schema_1 = require("../../utilities/json-schema");
var CommandScope;
(function (CommandScope) {
    /** Command can only run inside an Angular workspace. */
    CommandScope[CommandScope["In"] = 0] = "In";
    /** Command can only run outside an Angular workspace. */
    CommandScope[CommandScope["Out"] = 1] = "Out";
    /** Command can run inside and outside an Angular workspace. */
    CommandScope[CommandScope["Both"] = 2] = "Both";
})(CommandScope || (exports.CommandScope = CommandScope = {}));
exports.demandCommandFailureMessage = `You need to specify a command before moving on. Use '--help' to view the available commands.`;
function addCommandModuleToYargs(localYargs, commandModule, context) {
    const cmd = new commandModule(context);
    const { args: { options: { jsonHelp }, }, workspace, } = context;
    const describe = jsonHelp ? cmd.fullDescribe : cmd.describe;
    return localYargs.command({
        command: cmd.command,
        aliases: cmd.aliases,
        describe: 
        // We cannot add custom fields in help, such as long command description which is used in AIO.
        // Therefore, we get around this by adding a complex object as a string which we later parse when generating the help files.
        typeof describe === 'object' ? JSON.stringify(describe) : describe,
        deprecated: cmd.deprecated,
        builder: (argv) => {
            // Skip scope validation when running with '--json-help' since it's easier to generate the output for all commands this way.
            const isInvalidScope = !jsonHelp &&
                ((cmd.scope === CommandScope.In && !workspace) ||
                    (cmd.scope === CommandScope.Out && workspace));
            if (isInvalidScope) {
                throw new CommandModuleError(`This command is not available when running the Angular CLI ${workspace ? 'inside' : 'outside'} a workspace.`);
            }
            return cmd.builder(argv);
        },
        handler: (args) => cmd.handler(args),
    });
}
class CommandModule {
    context;
    shouldReportAnalytics = true;
    scope = CommandScope.Both;
    optionsWithAnalytics = new Map();
    constructor(context) {
        this.context = context;
    }
    /**
     * Description object which contains the long command descroption.
     * This is used to generate JSON help wich is used in AIO.
     *
     * `false` will result in a hidden command.
     */
    get fullDescribe() {
        return this.describe === false
            ? false
            : {
                describe: this.describe,
                ...(this.longDescriptionPath
                    ? {
                        longDescriptionRelativePath: path
                            .relative(path.join(__dirname, '../../../../'), this.longDescriptionPath)
                            .replace(/\\/g, path.posix.sep),
                        longDescription: (0, node_fs_1.readFileSync)(this.longDescriptionPath, 'utf8').replace(/\r\n/g, '\n'),
                    }
                    : {}),
            };
    }
    get commandName() {
        return this.command.split(' ', 1)[0];
    }
    async handler(args) {
        const { _, $0, ...options } = args;
        // Camelize options as yargs will return the object in kebab-case when camel casing is disabled.
        const camelCasedOptions = {};
        for (const [key, value] of Object.entries(options)) {
            camelCasedOptions[helpers_1.Parser.camelCase(key)] = value;
        }
        // Set up autocompletion if appropriate.
        const autocompletionExitCode = await (0, completion_1.considerSettingUpAutocompletion)(this.commandName, this.context.logger);
        if (autocompletionExitCode !== undefined) {
            process.exitCode = autocompletionExitCode;
            return;
        }
        // Gather and report analytics.
        // const analytics = await this.getAnalytics();
        // const stopPeriodicFlushes = analytics && analytics.periodFlush();
        let exitCode;
        try {
            // if (analytics) {
            //   this.reportCommandRunAnalytics(analytics);
            //   this.reportWorkspaceInfoAnalytics(analytics);
            // }
            exitCode = await this.run(camelCasedOptions);
        }
        catch (e) {
            if (e instanceof core_1.schema.SchemaValidationException) {
                this.context.logger.fatal(`Error: ${e.message}`);
                exitCode = 1;
            }
            else {
                throw e;
            }
        }
        finally {
            // await stopPeriodicFlushes?.();
            if (typeof exitCode === 'number' && exitCode > 0) {
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
    addSchemaOptionsToCommand(localYargs, options) {
        const optionsWithAnalytics = (0, json_schema_1.addSchemaOptionsToCommand)(localYargs, options, 
        // This should only be done when `--help` is used otherwise default will override options set in angular.json.
        /* includeDefaultValues= */ this.context.args.options.help);
        // Record option of analytics.
        for (const [name, userAnalytics] of optionsWithAnalytics) {
            this.optionsWithAnalytics.set(name, userAnalytics);
        }
        return localYargs;
    }
    getWorkspaceOrThrow() {
        const { workspace } = this.context;
        if (!workspace) {
            throw new CommandModuleError('A workspace is required for this command.');
        }
        return workspace;
    }
}
exports.CommandModule = CommandModule;
/**
 * Creates an known command module error.
 * This is used so during executation we can filter between known validation error and real non handled errors.
 */
class CommandModuleError extends Error {
}
exports.CommandModuleError = CommandModuleError;
