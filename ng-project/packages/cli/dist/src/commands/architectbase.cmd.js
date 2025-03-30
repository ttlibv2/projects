"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
exports.ArchitectBaseCommandModule = void 0;
const architect_1 = require("@angular-devkit/architect");
const node_1 = require("@angular-devkit/architect/node");
const core_1 = require("@angular-devkit/core");
const node_fs_1 = require("node:fs");
const node_path_1 = require("node:path");
// import { isPackageNameSafeForAnalytics } from '../analytics/analytics';
// import { EventCustomDimension, EventCustomMetric } from '../analytics/analytics-parameters';
const error_1 = require("../utilities/error");
const prompt_1 = require("../utilities/prompt");
const tty_1 = require("../utilities/tty");
const abstract_cmd_1 = require("./abstract.cmd");
const json_schema_1 = require("./helper/json-schema");
class ArchitectBaseCommandModule extends abstract_cmd_1.CommandModule {
    scope = abstract_cmd_1.CommandScope.In;
    missingTargetChoices;
    async runSingleTarget(target, options) {
        const architectHost = this.getArchitectHost();
        let builderName;
        try {
            builderName = await architectHost.getBuilderNameForTarget(target);
        }
        catch (e) {
            (0, error_1.assertIsError)(e);
            return this.onMissingTarget(e.message);
        }
        const { logger } = this.context;
        const run = await this.getArchitect().scheduleTarget(target, options, { logger: logger.scLogger });
        // const analytics = isPackageNameSafeForAnalytics(builderName)
        //   ? await this.getAnalytics() : undefined;
        let outputSubscription;
        // if (analytics) {
        //   analytics.reportArchitectRunEvent({
        //     [EventCustomDimension.BuilderTarget]: builderName,
        //   });
        //
        //   let firstRun = true;
        //   outputSubscription = run.output.subscribe(({ stats }) => {
        //     const parameters = this.builderStatsToAnalyticsParameters(stats, builderName);
        //     if (!parameters) {
        //       return;
        //     }
        //
        //     if (firstRun) {
        //       firstRun = false;
        //       analytics.reportBuildRunEvent(parameters);
        //     } else {
        //       analytics.reportRebuildRunEvent(parameters);
        //     }
        //   });
        // }
        try {
            const { error, success } = await run.lastOutput;
            if (error) {
                logger.error(error);
            }
            return success ? 0 : 1;
        }
        finally {
            await run.stop();
            //outputSubscription?.unsubscribe();
        }
    }
    // private builderStatsToAnalyticsParameters(
    //   stats: json.JsonValue,
    //   builderName: string,
    // ): Partial<
    //   | Record<EventCustomDimension & EventCustomMetric, string | number | undefined | boolean>
    //   | undefined
    // > {
    //   if (!stats || typeof stats !== 'object' || !('durationInMs' in stats)) {
    //     return undefined;
    //   }
    //
    //   const {
    //     optimization,
    //     allChunksCount,
    //     aot,
    //     lazyChunksCount,
    //     initialChunksCount,
    //     durationInMs,
    //     changedChunksCount,
    //     cssSizeInBytes,
    //     jsSizeInBytes,
    //     ngComponentCount,
    //   } = stats;
    //
    //   return {
    //     [EventCustomDimension.BuilderTarget]: builderName,
    //     [EventCustomDimension.Aot]: aot,
    //     [EventCustomDimension.Optimization]: optimization,
    //     [EventCustomMetric.AllChunksCount]: allChunksCount,
    //     [EventCustomMetric.LazyChunksCount]: lazyChunksCount,
    //     [EventCustomMetric.InitialChunksCount]: initialChunksCount,
    //     [EventCustomMetric.ChangedChunksCount]: changedChunksCount,
    //     [EventCustomMetric.DurationInMs]: durationInMs,
    //     [EventCustomMetric.JsSizeInBytes]: jsSizeInBytes,
    //     [EventCustomMetric.CssSizeInBytes]: cssSizeInBytes,
    //     [EventCustomMetric.NgComponentCount]: ngComponentCount,
    //   };
    // }
    _architectHost;
    getArchitectHost() {
        if (this._architectHost) {
            return this._architectHost;
        }
        const workspace = this.getWorkspaceOrThrow();
        return this._architectHost = new node_1.WorkspaceNodeModulesArchitectHost(workspace, workspace.basePath);
    }
    _architect;
    getArchitect() {
        if (this._architect) {
            return this._architect;
        }
        const registry = new core_1.json.schema.CoreSchemaRegistry();
        registry.addPostTransform(core_1.json.schema.transforms.addUndefinedDefaults);
        registry.useXDeprecatedProvider((msg) => this.context.logger.warn(msg));
        const architectHost = this.getArchitectHost();
        return (this._architect = new architect_1.Architect(architectHost, registry));
    }
    async getArchitectTargetOptions(target) {
        const architectHost = this.getArchitectHost();
        let builderConf;
        try {
            builderConf = await architectHost.getBuilderNameForTarget(target);
        }
        catch {
            return [];
        }
        let builderDesc;
        try {
            builderDesc = await architectHost.resolveBuilder(builderConf);
        }
        catch (e) {
            (0, error_1.assertIsError)(e);
            if (e.code === 'MODULE_NOT_FOUND') {
                this.warnOnMissingNodeModules();
                throw new abstract_cmd_1.CommandModuleError(`Could not find the '${builderConf}' builder's node package.`);
            }
            throw e;
        }
        return (0, json_schema_1.parseJsonSchemaToOptions)(new core_1.json.schema.CoreSchemaRegistry(), builderDesc.optionSchema, true);
    }
    warnOnMissingNodeModules() {
        const basePath = this.context.workspace?.basePath;
        if (!basePath) {
            return;
        }
        // Check if yarn PnP is used. https://yarnpkg.com/advanced/pnpapi#processversionspnp
        if (process.versions.pnp) {
            return;
        }
        // Check for a `node_modules` directory (npm, yarn non-PnP, etc.)
        if ((0, node_fs_1.existsSync)((0, node_path_1.resolve)(basePath, 'node_modules'))) {
            return;
        }
        this.context.logger.warn(`Node packages may not be installed. Try installing with '${this.context.packageManager.name} install'.`);
    }
    getArchitectTarget() {
        return this.commandName;
    }
    async onMissingTarget(defaultMessage) {
        const { logger } = this.context;
        const choices = this.missingTargetChoices;
        if (!choices?.length) {
            logger.error(defaultMessage);
            return 1;
        }
        const missingTargetMessage = `Cannot find "${this.getArchitectTarget()}" target for the specified project.\n` +
            `You can add a package that implements these capabilities.\n\n` +
            `For example:\n` +
            choices.map(({ name, value }) => `  ${name}: ng add ${value}`).join('\n') +
            '\n';
        if ((0, tty_1.isTTY)()) {
            // Use prompts to ask the user if they'd like to install a package.
            logger.warn(missingTargetMessage);
            const packageToInstall = await this.getMissingTargetPackageToInstall(choices);
            if (packageToInstall) {
                throw new Error(`AddCommandModule`);
                // Example run: `ng add angular-eslint`.
                // const AddCommandModule = (await import('../commands/add/lib')).default;
                // await new AddCommandModule(this.context).run({
                //   interactive: true,
                //   force: false,
                //   dryRun: false,
                //   defaults: false,
                //   collection: packageToInstall,
                // });
            }
        }
        else {
            // Non TTY display error message.
            logger.error(missingTargetMessage);
        }
        return 1;
    }
    async getMissingTargetPackageToInstall(choices) {
        if (choices.length === 1) {
            // Single choice
            const { name, value } = choices[0];
            if (await (0, prompt_1.askConfirmation)(`Would you like to add ${name} now?`, true, false)) {
                return value;
            }
            return null;
        }
        // Multiple choice
        return (0, prompt_1.askQuestion)(`Would you like to add a package with "${this.getArchitectTarget()}" capabilities now?`, [
            {
                name: 'No',
                value: null,
            },
            ...choices,
        ], 0, null);
    }
}
exports.ArchitectBaseCommandModule = ArchitectBaseCommandModule;
//# sourceMappingURL=architectbase.cmd.js.map