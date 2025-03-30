"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
exports.CommandModuleError = exports.CommandModule = exports.CommandScope = void 0;
const core_1 = require("@angular-devkit/core");
const node_fs_1 = require("node:fs");
const path = require("node:path");
const helpers_1 = require("yargs/helpers");
const completion_1 = require("../utilities/completion");
const json_schema_1 = require("./helper/json-schema");
var CommandScope;
(function (CommandScope) {
    /** Command can only run inside an Angular workspace. */
    CommandScope[CommandScope["In"] = 0] = "In";
    /** Command can only run outside an Angular workspace. */
    CommandScope[CommandScope["Out"] = 1] = "Out";
    /** Command can run inside and outside an Angular workspace. */
    CommandScope[CommandScope["Both"] = 2] = "Both";
})(CommandScope || (exports.CommandScope = CommandScope = {}));
class CommandModule {
    context;
    scope = CommandScope.Both;
    shouldReportAnalytics = true;
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
        return this.describe === false ? false : {
            describe: this.describe,
            ...this.getDesFile(this.longDescriptionPath)
        };
    }
    getDesFile(desPath) {
        if (desPath === void 0)
            return {};
        else {
            const from = path.join(__dirname, '../../../..');
            const desRelPath = path.relative(from, desPath).replace(/\\/g, path.posix.sep);
            let desText = '';
            try {
                desText = (0, node_fs_1.readFileSync)(this.longDescriptionPath, 'utf8').replace(/\r\n/g, '\n');
            } //
            catch (err) {
                this.context.logger.warn(`Read document: [${this.command}] ==> Message: ${err.message}`);
                desText = '';
            }
            return { longDescriptionRelativePath: desRelPath, longDescription: desText };
        }
    }
    get commandName() {
        return this.command.split(' ', 1)[0];
    }
    get longDescriptionPath() {
        return path.join(__dirname, 'docs', `${this.commandName}.doc.md`);
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
//# sourceMappingURL=abstract.cmd.js.map