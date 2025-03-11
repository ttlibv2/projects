"use strict";
/**
 * @license
 * Copyright Google LLC All Rights Reserved.
 *
 * Use of this source code is governed by an MIT-style license that can be
 * found in the LICENSE file at https://angular.dev/license
 */
var __runInitializers = (this && this.__runInitializers) || function (thisArg, initializers, value) {
    var useValue = arguments.length > 2;
    for (var i = 0; i < initializers.length; i++) {
        value = useValue ? initializers[i].call(thisArg, value) : initializers[i].call(thisArg);
    }
    return useValue ? value : void 0;
};
var __esDecorate = (this && this.__esDecorate) || function (ctor, descriptorIn, decorators, contextIn, initializers, extraInitializers) {
    function accept(f) { if (f !== void 0 && typeof f !== "function") throw new TypeError("Function expected"); return f; }
    var kind = contextIn.kind, key = kind === "getter" ? "get" : kind === "setter" ? "set" : "value";
    var target = !descriptorIn && ctor ? contextIn["static"] ? ctor : ctor.prototype : null;
    var descriptor = descriptorIn || (target ? Object.getOwnPropertyDescriptor(target, contextIn.name) : {});
    var _, done = false;
    for (var i = decorators.length - 1; i >= 0; i--) {
        var context = {};
        for (var p in contextIn) context[p] = p === "access" ? {} : contextIn[p];
        for (var p in contextIn.access) context.access[p] = contextIn.access[p];
        context.addInitializer = function (f) { if (done) throw new TypeError("Cannot add initializers after decoration has completed"); extraInitializers.push(accept(f || null)); };
        var result = (0, decorators[i])(kind === "accessor" ? { get: descriptor.get, set: descriptor.set } : descriptor[key], context);
        if (kind === "accessor") {
            if (result === void 0) continue;
            if (result === null || typeof result !== "object") throw new TypeError("Object expected");
            if (_ = accept(result.get)) descriptor.get = _;
            if (_ = accept(result.set)) descriptor.set = _;
            if (_ = accept(result.init)) initializers.unshift(_);
        }
        else if (_ = accept(result)) {
            if (kind === "field") initializers.unshift(_);
            else descriptor[key] = _;
        }
    }
    if (target) Object.defineProperty(target, contextIn.name, descriptor);
    done = true;
};
var __awaiter = (this && this.__awaiter) || function (thisArg, _arguments, P, generator) {
    function adopt(value) { return value instanceof P ? value : new P(function (resolve) { resolve(value); }); }
    return new (P || (P = Promise))(function (resolve, reject) {
        function fulfilled(value) { try { step(generator.next(value)); } catch (e) { reject(e); } }
        function rejected(value) { try { step(generator["throw"](value)); } catch (e) { reject(e); } }
        function step(result) { result.done ? resolve(result.value) : adopt(result.value).then(fulfilled, rejected); }
        step((generator = generator.apply(thisArg, _arguments || [])).next());
    });
};
Object.defineProperty(exports, "__esModule", { value: true });
exports.SchematicsCommandModule = void 0;
const core_1 = require("@angular-devkit/core");
const schematics_1 = require("@angular-devkit/schematics");
const tools_1 = require("@angular-devkit/schematics/tools");
const node_path_1 = require("node:path");
// import { isPackageNameSafeForAnalytics } from '../analytics/analytics';
// import { EventCustomDimension } from '../analytics/analytics-parameters';
const config_1 = require("../utilities/config");
const error_1 = require("../utilities/error");
const memoize_1 = require("../utilities/memoize");
const tty_1 = require("../utilities/tty");
const command_module_1 = require("./command-module");
const json_schema_1 = require("../utilities/json-schema");
const schematic_engine_host_1 = require("../utilities/schematic-engine-host");
const schematic_workflow_1 = require("../utilities/schematic-workflow");
const constant_1 = require("../utilities/constant");
let SchematicsCommandModule = (() => {
    var _a;
    let _classSuper = command_module_1.CommandModule;
    let _instanceExtraInitializers = [];
    let _getOrCreateWorkflowForBuilder_decorators;
    let _getOrCreateWorkflowForExecution_decorators;
    let _getSchematicCollections_decorators;
    return _a = class SchematicsCommandModule extends _classSuper {
            constructor() {
                super(...arguments);
                this.scope = (__runInitializers(this, _instanceExtraInitializers), command_module_1.CommandScope.In);
                this.allowPrivateSchematics = false;
            }
            builder(argv) {
                return __awaiter(this, void 0, void 0, function* () {
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
                });
            }
            /** Get schematic schema options.*/
            getSchematicOptions(collection, schematicName, workflow) {
                return __awaiter(this, void 0, void 0, function* () {
                    const schematic = collection.createSchematic(schematicName, true);
                    const { schemaJson } = schematic.description;
                    if (!schemaJson) {
                        return [];
                    }
                    return (0, json_schema_1.parseJsonSchemaToOptions)(workflow.registry, schemaJson);
                });
            }
            getOrCreateWorkflowForBuilder(collectionName) {
                return new tools_1.NodeWorkflow(this.context.root, {
                    resolvePaths: this.getResolvePaths(collectionName),
                    engineHostCreator: (options) => new schematic_engine_host_1.SchematicEngineHost(options.resolvePaths),
                });
            }
            getOrCreateWorkflowForExecution(collectionName, options) {
                return __awaiter(this, void 0, void 0, function* () {
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
                            (schematic, current) => __awaiter(this, void 0, void 0, function* () {
                                const projectName = typeof (current === null || current === void 0 ? void 0 : current.project) === 'string' ? current.project : this.getProjectName();
                                return Object.assign(Object.assign({}, (yield (0, config_1.getSchematicDefaults)(schematic.collection.name, schematic.name, projectName))), current);
                            }),
                        ],
                        engineHostCreator: (options) => new schematic_engine_host_1.SchematicEngineHost(options.resolvePaths),
                    });
                    workflow.registry.addPostTransform(core_1.schema.transforms.addUndefinedDefaults);
                    workflow.registry.useXDeprecatedProvider((msg) => logger.warn(msg));
                    workflow.registry.addSmartDefaultProvider('projectName', () => this.getProjectName());
                    const workingDir = (0, core_1.normalize)((0, node_path_1.relative)(this.context.root, process.cwd()));
                    workflow.registry.addSmartDefaultProvider('workingDirectory', () => workingDir === '' ? undefined : workingDir);
                    let shouldReportAnalytics = true;
                    workflow.engineHost.registerOptionsTransform((schematic, options) => __awaiter(this, void 0, void 0, function* () {
                        // Report analytics
                        if (shouldReportAnalytics) {
                            shouldReportAnalytics = false;
                            // const {
                            //   collection: { name: collectionName },
                            //   name: schematicName,
                            // } = schematic;
                            // const analytics = isPackageNameSafeForAnalytics(collectionName)
                            //   ? await this.getAnalytics()
                            //   : undefined;
                            // analytics?.reportSchematicRunEvent({
                            //   [EventCustomDimension.SchematicCollectionName]: collectionName,
                            //   [EventCustomDimension.SchematicName]: schematicName,
                            //   ...this.getAnalyticsParameters(options as unknown as {}),
                            // });
                        }
                        return options;
                    }));
                    if (options.interactive !== false && (0, tty_1.isTTY)()) {
                        workflow.registry.usePromptProvider((definitions) => __awaiter(this, void 0, void 0, function* () {
                            var _b, _c;
                            let prompts;
                            const answers = {};
                            for (const definition of definitions) {
                                if (options.defaults && definition.default !== undefined) {
                                    continue;
                                }
                                // Only load prompt package if needed
                                prompts !== null && prompts !== void 0 ? prompts : (prompts = yield Promise.resolve().then(() => require('@inquirer/prompts')));
                                switch (definition.type) {
                                    case 'confirmation':
                                        answers[definition.id] = yield prompts.confirm({
                                            message: definition.message,
                                            default: definition.default,
                                        });
                                        break;
                                    case 'list':
                                        if (!((_b = definition.items) === null || _b === void 0 ? void 0 : _b.length)) {
                                            continue;
                                        }
                                        answers[definition.id] = yield (definition.multiselect ? prompts.checkbox : prompts.select)({
                                            message: definition.message,
                                            validate: (values) => {
                                                if (!definition.validator) {
                                                    return true;
                                                }
                                                return definition.validator(Object.values(values).map(({ value }) => value));
                                            },
                                            default: definition.multiselect ? undefined : definition.default,
                                            choices: (_c = definition.items) === null || _c === void 0 ? void 0 : _c.map((item) => typeof item == 'string'
                                                ? {
                                                    name: item,
                                                    value: item,
                                                }
                                                : Object.assign(Object.assign({}, item), { name: item.label, value: item.value })),
                                        });
                                        break;
                                    case 'input': {
                                        let finalValue;
                                        answers[definition.id] = yield prompts.input({
                                            message: definition.message,
                                            default: definition.default,
                                            validate(value) {
                                                return __awaiter(this, void 0, void 0, function* () {
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
                                                        lastValidation = yield definition.validator(potential);
                                                        // Can be a string if validation fails
                                                        if (lastValidation === true) {
                                                            finalValue = potential;
                                                            return true;
                                                        }
                                                    }
                                                    return lastValidation;
                                                });
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
                        }));
                    }
                    return workflow;
                });
            }
            getSchematicCollections() {
                return __awaiter(this, void 0, void 0, function* () {
                    var _b;
                    const getSchematicCollections = (configSection) => {
                        if (!configSection) {
                            return undefined;
                        }
                        const { schematicCollections } = configSection;
                        if (Array.isArray(schematicCollections)) {
                            return new Set(schematicCollections);
                        }
                        return undefined;
                    };
                    const { workspace, globalConfiguration } = this.context;
                    if (workspace) {
                        const project = (0, config_1.getProjectByCwd)(workspace);
                        if (project) {
                            const value = getSchematicCollections(workspace.getProjectCli(project));
                            if (value) {
                                return value;
                            }
                        }
                    }
                    const value = (_b = getSchematicCollections(workspace === null || workspace === void 0 ? void 0 : workspace.getCli())) !== null && _b !== void 0 ? _b : getSchematicCollections(globalConfiguration.getCli());
                    if (value) {
                        return value;
                    }
                    return new Set([constant_1.DEFAULT_SCHEMATICS_COLLECTION]);
                });
            }
            parseSchematicInfo(schematic) {
                if (schematic === null || schematic === void 0 ? void 0 : schematic.includes(':')) {
                    const [collectionName, schematicName] = schematic.split(':', 2);
                    return [collectionName, schematicName];
                }
                return [undefined, schematic];
            }
            runSchematic(options) {
                return __awaiter(this, void 0, void 0, function* () {
                    const { logger } = this.context;
                    const { schematicOptions, executionOptions, collectionName, schematicName } = options;
                    const workflow = yield this.getOrCreateWorkflowForExecution(collectionName, executionOptions);
                    if (!schematicName) {
                        throw new Error('schematicName cannot be undefined.');
                    }
                    const { unsubscribe, files } = (0, schematic_workflow_1.subscribeToWorkflow)(workflow, logger);
                    try {
                        yield workflow
                            .execute({
                            collection: collectionName,
                            schematic: schematicName,
                            options: schematicOptions,
                            logger,
                            allowPrivate: this.allowPrivateSchematics,
                        })
                            .toPromise();
                        // await lastValueFrom(workflow
                        // .execute({
                        //   collection: collectionName,
                        //   schematic: schematicName,
                        //   options: schematicOptions,
                        //   logger,
                        //   allowPrivate: this.allowPrivateSchematics,
                        // }));
                        if (!files.size) {
                            logger.info('Nothing to be done.');
                        }
                        if (executionOptions.dryRun) {
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
                });
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
                        collectionName === constant_1.DEFAULT_SCHEMATICS_COLLECTION
                            ? // Favor __dirname for @schematics/angular to use the build-in version
                                [__dirname, process.cwd(), root]
                            : [process.cwd(), root, __dirname]
                    : // Global
                        [__dirname, process.cwd()];
            }
        },
        (() => {
            var _b;
            const _metadata = typeof Symbol === "function" && Symbol.metadata ? Object.create((_b = _classSuper[Symbol.metadata]) !== null && _b !== void 0 ? _b : null) : void 0;
            _getOrCreateWorkflowForBuilder_decorators = [memoize_1.memoize];
            _getOrCreateWorkflowForExecution_decorators = [memoize_1.memoize];
            _getSchematicCollections_decorators = [memoize_1.memoize];
            __esDecorate(_a, null, _getOrCreateWorkflowForBuilder_decorators, { kind: "method", name: "getOrCreateWorkflowForBuilder", static: false, private: false, access: { has: obj => "getOrCreateWorkflowForBuilder" in obj, get: obj => obj.getOrCreateWorkflowForBuilder }, metadata: _metadata }, null, _instanceExtraInitializers);
            __esDecorate(_a, null, _getOrCreateWorkflowForExecution_decorators, { kind: "method", name: "getOrCreateWorkflowForExecution", static: false, private: false, access: { has: obj => "getOrCreateWorkflowForExecution" in obj, get: obj => obj.getOrCreateWorkflowForExecution }, metadata: _metadata }, null, _instanceExtraInitializers);
            __esDecorate(_a, null, _getSchematicCollections_decorators, { kind: "method", name: "getSchematicCollections", static: false, private: false, access: { has: obj => "getSchematicCollections" in obj, get: obj => obj.getSchematicCollections }, metadata: _metadata }, null, _instanceExtraInitializers);
            if (_metadata) Object.defineProperty(_a, Symbol.metadata, { enumerable: true, configurable: true, writable: true, value: _metadata });
        })(),
        _a;
})();
exports.SchematicsCommandModule = SchematicsCommandModule;
//# sourceMappingURL=schematics-command-module.js.map