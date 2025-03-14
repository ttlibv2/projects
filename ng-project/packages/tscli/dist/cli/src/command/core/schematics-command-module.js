"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
exports.SchematicsCommandModule = exports.DEFAULT_SCHEMATICS_COLLECTION = void 0;
const tslib_1 = require("tslib");
const core_1 = require("@angular-devkit/core");
const schematics_1 = require("@angular-devkit/schematics");
const tools_1 = require("@angular-devkit/schematics/tools");
const node_path_1 = require("node:path");
// import { isPackageNameSafeForAnalytics } from '../analytics/analytics';
// import { EventCustomDimension } from '../analytics/analytics-parameters';
const config_1 = require("../../utilities/config");
const error_1 = require("../../utilities/error");
const memoize_1 = require("../../utilities/memoize");
const tty_1 = require("../../utilities/tty");
const command_module_1 = require("./command-module");
const json_schema_1 = require("../../utilities/json-schema");
const sc_engine_host_1 = require("../../utilities/sc-engine-host");
const sc_workflow_1 = require("../../utilities/sc-workflow");
exports.DEFAULT_SCHEMATICS_COLLECTION = '@schematics/angular';
class SchematicsCommandModule extends command_module_1.CommandModule {
    scope = command_module_1.CommandScope.In;
    allowPrivateSchematics = false;
    async builder(argv) {
        return argv
            .option('interactive', {
            describe: 'Enable interactive input prompts.',
            type: 'boolean',
            default: true,
        })
            .option('dry-run', {
            describe: 'Run through and reports activity without writing out results.',
            type: 'boolean',
            alias: ['d'],
            default: false,
        })
            .option('defaults', {
            describe: 'Disable interactive input prompts for options with a default.',
            type: 'boolean',
            default: false,
        })
            .option('force', {
            describe: 'Force overwriting of existing files.',
            type: 'boolean',
            default: false,
        })
            .strict();
    }
    /** Get schematic schema options.*/
    async getSchematicOptions(collection, schematicName, workflow) {
        const schematic = collection.createSchematic(schematicName, true);
        const { schemaJson } = schematic.description;
        if (!schemaJson) {
            return [];
        }
        return (0, json_schema_1.parseJsonSchemaToOptions)(workflow.registry, schemaJson);
    }
    // @memoize
    getOrCreateWorkflowForBuilder(collectionName) {
        return new tools_1.NodeWorkflow(this.context.root, {
            resolvePaths: this.getResolvePaths(collectionName),
            engineHostCreator: (options) => new sc_engine_host_1.SchematicEngineHost(options.resolvePaths),
        });
    }
    // @memoize
    async getOrCreateWorkflowForExecution(collectionName, options) {
        const { logger, root, packageManager } = this.context;
        const { force, dryRun, packageRegistry } = options;
        const workflow = new tools_1.NodeWorkflow(root, {
            force,
            dryRun,
            packageManager: packageManager.name,
            // A schema registry is required to allow customizing addUndefinedDefaults
            registry: new core_1.schema.CoreSchemaRegistry(schematics_1.formats.standardFormats),
            packageRegistry,
            resolvePaths: this.getResolvePaths(collectionName),
            schemaValidation: true,
            optionTransforms: [
                // Add configuration file defaults
                async (schematic, current) => {
                    const projectName = typeof current?.project === 'string' ? current.project : this.getProjectName();
                    return {
                        ...(await (0, config_1.getSchematicDefaults)(schematic.collection.name, schematic.name, projectName)),
                        ...current,
                    };
                },
            ],
            engineHostCreator: (options) => new sc_engine_host_1.SchematicEngineHost(options.resolvePaths),
        });
        workflow.registry.addPostTransform(core_1.schema.transforms.addUndefinedDefaults);
        workflow.registry.useXDeprecatedProvider((msg) => logger.warn(msg));
        workflow.registry.addSmartDefaultProvider('projectName', () => this.getProjectName());
        const workingDir = (0, core_1.normalize)((0, node_path_1.relative)(this.context.root, process.cwd()));
        workflow.registry.addSmartDefaultProvider('workingDirectory', () => workingDir === '' ? undefined : workingDir);
        workflow.engineHost.registerOptionsTransform(async (schematic, options) => {
            const { collection: { name: collectionName }, name: schematicName, } = schematic;
            // const analytics = isPackageNameSafeForAnalytics(collectionName)
            //   ? await this.getAnalytics()
            //   : undefined;
            // analytics?.reportSchematicRunEvent({
            //   [EventCustomDimension.SchematicCollectionName]: collectionName,
            //   [EventCustomDimension.SchematicName]: schematicName,
            //   ...this.getAnalyticsParameters(options as unknown as {}),
            // });
            return options;
        });
        if (options.interactive !== false && (0, tty_1.isTTY)()) {
            workflow.registry.usePromptProvider(async (definitions) => {
                let prompts;
                const answers = {};
                for (const definition of definitions) {
                    if (options.defaults && definition.default !== undefined) {
                        continue;
                    }
                    // Only load prompt package if needed
                    prompts ??= await Promise.resolve().then(() => tslib_1.__importStar(require('@inquirer/prompts')));
                    switch (definition.type) {
                        case 'confirmation':
                            answers[definition.id] = await prompts.confirm({
                                message: definition.message,
                                default: definition.default,
                            });
                            break;
                        case 'list':
                            if (!definition.items?.length) {
                                continue;
                            }
                            answers[definition.id] = await (definition.multiselect ? prompts.checkbox : prompts.select)({
                                message: definition.message,
                                validate: (values) => {
                                    if (!definition.validator) {
                                        return true;
                                    }
                                    return definition.validator(Object.values(values).map(({ value }) => value));
                                },
                                default: definition.multiselect ? undefined : definition.default,
                                choices: definition.items?.map((item) => typeof item == 'string'
                                    ? {
                                        name: item,
                                        value: item,
                                    }
                                    : {
                                        ...item,
                                        name: item.label,
                                        value: item.value,
                                    }),
                            });
                            break;
                        case 'input': {
                            let finalValue;
                            answers[definition.id] = await prompts.input({
                                message: definition.message,
                                default: definition.default,
                                async validate(value) {
                                    if (definition.validator === undefined) {
                                        return true;
                                    }
                                    let lastValidation = false;
                                    for (const type of definition.propertyTypes) {
                                        let potential;
                                        switch (type) {
                                            case 'string':
                                                potential = String(value);
                                                break;
                                            case 'integer':
                                            case 'number':
                                                potential = Number(value);
                                                break;
                                            default:
                                                potential = value;
                                                break;
                                        }
                                        lastValidation = await definition.validator(potential);
                                        // Can be a string if validation fails
                                        if (lastValidation === true) {
                                            finalValue = potential;
                                            return true;
                                        }
                                    }
                                    return lastValidation;
                                },
                            });
                            // Use validated value if present.
                            // This ensures the correct type is inserted into the final schema options.
                            if (finalValue !== undefined) {
                                answers[definition.id] = finalValue;
                            }
                            break;
                        }
                    }
                }
                return answers;
            });
        }
        return workflow;
    }
    async getSchematicCollections() {
        // const getSchematicCollections = (
        //   configSection: Record<string, unknown> | undefined,
        // ): Set<string> | undefined => {
        //   if (!configSection) {
        //     return undefined;
        //   }
        //   const { schematicCollections } = configSection;
        //   if (Array.isArray(schematicCollections)) {
        //     return new Set(schematicCollections);
        //   }
        //   return undefined;
        // };
        // const { workspace, globalConfiguration } = this.context;
        // if (workspace) {
        //   const project = getProjectByCwd(workspace);
        //   if (project) {
        //     const value = getSchematicCollections(workspace.getProjectCli(project));
        //     if (value) {
        //       return value;
        //     }
        //   }
        // }
        // const value =
        //   getSchematicCollections(workspace?.getCli()) ??
        //   getSchematicCollections(globalConfiguration.getCli());
        // if (value) {
        //   return value;
        // }
        return new Set([exports.DEFAULT_SCHEMATICS_COLLECTION]);
    }
    parseSchematicInfo(schematic) {
        if (schematic?.includes(':')) {
            const [collectionName, schematicName] = schematic.split(':', 2);
            return [collectionName, schematicName];
        }
        return [undefined, schematic];
    }
    async runSchematic(options) {
        const { logger } = this.context;
        const { schematicOptions, executionOptions, collectionName, schematicName } = options;
        const workflow = await this.getOrCreateWorkflowForExecution(collectionName, executionOptions);
        if (!schematicName) {
            throw new Error('schematicName cannot be undefined.');
        }
        const { unsubscribe, files } = (0, sc_workflow_1.subscribeToWorkflow)(workflow, logger);
        try {
            await workflow
                .execute({
                collection: collectionName,
                schematic: schematicName,
                options: schematicOptions,
                logger,
                allowPrivate: this.allowPrivateSchematics,
            })
                .toPromise();
            if (!files.size) {
                logger.info('Nothing to be done.');
            }
            if (executionOptions['dryRun']) {
                logger.warn(`\nNOTE: The "--dry-run" option means no changes were made.`);
            }
        }
        catch (err) {
            // In case the workflow was not successful, show an appropriate error message.
            if (err instanceof schematics_1.UnsuccessfulWorkflowExecution) {
                // "See above" because we already printed the error.
                logger.fatal('The Schematic workflow failed. See above.');
            }
            else {
                (0, error_1.assertIsError)(err);
                logger.fatal(err.message);
            }
            return 1;
        }
        finally {
            unsubscribe();
        }
        return 0;
    }
    getProjectName() {
        const { workspace } = this.context;
        if (!workspace) {
            return undefined;
        }
        const projectName = (0, config_1.getProjectByCwd)(workspace);
        if (projectName) {
            return projectName;
        }
        return undefined;
    }
    getResolvePaths(collectionName) {
        const { workspace, root } = this.context;
        if (collectionName[0] === '.') {
            // Resolve relative collections from the location of `angular.json`
            return [root];
        }
        return workspace
            ? // Workspace
                collectionName === exports.DEFAULT_SCHEMATICS_COLLECTION
                    ? // Favor __dirname for @schematics/angular to use the build-in version
                        [__dirname, process.cwd(), root]
                    : [process.cwd(), root, __dirname]
            : // Global
                [__dirname, process.cwd()];
    }
}
exports.SchematicsCommandModule = SchematicsCommandModule;
tslib_1.__decorate([
    memoize_1.memoize
], SchematicsCommandModule.prototype, "getSchematicCollections", null);
