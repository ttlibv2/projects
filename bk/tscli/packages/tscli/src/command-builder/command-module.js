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
var __rest = (this && this.__rest) || function (s, e) {
    var t = {};
    for (var p in s) if (Object.prototype.hasOwnProperty.call(s, p) && e.indexOf(p) < 0)
        t[p] = s[p];
    if (s != null && typeof Object.getOwnPropertySymbols === "function")
        for (var i = 0, p = Object.getOwnPropertySymbols(s); i < p.length; i++) {
            if (e.indexOf(p[i]) < 0 && Object.prototype.propertyIsEnumerable.call(s, p[i]))
                t[p[i]] = s[p[i]];
        }
    return t;
};
Object.defineProperty(exports, "__esModule", { value: true });
exports.CommandModuleError = exports.CommandModule = exports.CommandScope = void 0;
const core_1 = require("@angular-devkit/core");
const node_fs_1 = require("node:fs");
const path = require("node:path");
const helpers_1 = require("yargs/helpers");
// import { getAnalyticsUserId } from '../analytics/analytics';
// import { AnalyticsCollector } from '../analytics/analytics-collector';
// import { EventCustomDimension, EventCustomMetric } from '../analytics/analytics-parameters';
const completion_1 = require("../utilities/completion");
const json_schema_1 = require("../utilities/json-schema");
const memoize_1 = require("../utilities/memoize");
var CommandScope;
(function (CommandScope) {
    /** Command can only run inside an Angular workspace. */
    CommandScope[CommandScope["In"] = 0] = "In";
    /** Command can only run outside an Angular workspace. */
    CommandScope[CommandScope["Out"] = 1] = "Out";
    /** Command can run inside and outside an Angular workspace. */
    CommandScope[CommandScope["Both"] = 2] = "Both";
})(CommandScope || (exports.CommandScope = CommandScope = {}));
let CommandModule = (() => {
    var _a;
    let _instanceExtraInitializers = [];
    let _getAnalytics_decorators;
    return _a = class CommandModule {
            constructor(context) {
                this.context = (__runInitializers(this, _instanceExtraInitializers), context);
                this.shouldReportAnalytics = true;
                this.scope = CommandScope.Both;
                this.optionsWithAnalytics = new Map();
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
                    : Object.assign({ describe: this.describe }, (this.longDescriptionPath
                        ? {
                            longDescriptionRelativePath: path
                                .relative(path.join(__dirname, '../../../../'), this.longDescriptionPath)
                                .replace(/\\/g, path.posix.sep),
                            longDescription: (0, node_fs_1.readFileSync)(this.longDescriptionPath, 'utf8').replace(/\r\n/g, '\n'),
                        }
                        : {}));
            }
            get commandName() {
                return this.command.split(' ', 1)[0];
            }
            handler(args) {
                return __awaiter(this, void 0, void 0, function* () {
                    const { _, $0 } = args, options = __rest(args, ["_", "$0"]);
                    // Camelize options as yargs will return the object in kebab-case when camel casing is disabled.
                    const camelCasedOptions = {};
                    for (const [key, value] of Object.entries(options)) {
                        camelCasedOptions[helpers_1.Parser.camelCase(key)] = value;
                    }
                    // Set up autocompletion if appropriate.
                    const autocompletionExitCode = yield (0, completion_1.considerSettingUpAutocompletion)(this.commandName, this.context.logger);
                    if (autocompletionExitCode !== undefined) {
                        process.exitCode = autocompletionExitCode;
                        return;
                    }
                    // Gather and report analytics.
                    const analytics = yield this.getAnalytics();
                    const stopPeriodicFlushes = analytics && analytics.periodFlush();
                    let exitCode;
                    try {
                        //   if (analytics) {
                        //     this.reportCommandRunAnalytics(analytics);
                        //     this.reportWorkspaceInfoAnalytics(analytics);
                        //   }
                        exitCode = yield this.run(camelCasedOptions);
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
                        yield (stopPeriodicFlushes === null || stopPeriodicFlushes === void 0 ? void 0 : stopPeriodicFlushes());
                        if (typeof exitCode === 'number' && exitCode > 0) {
                            process.exitCode = exitCode;
                        }
                    }
                });
            }
            getAnalytics() {
                return __awaiter(this, void 0, void 0, function* () {
                    // if (!this.shouldReportAnalytics) {
                    //   return undefined;
                    // }
                    // const userId = await getAnalyticsUserId(
                    //   this.context,
                    //   // Don't prompt on `ng update`, 'ng version' or `ng analytics`.
                    //   ['version', 'update', 'analytics'].includes(this.commandName),
                    // );
                    // return userId ? new AnalyticsCollector(this.context, userId) : undefined;
                    return undefined;
                });
            }
            /**
             * Adds schema options to a command also this keeps track of options that are required for analytics.
             * **Note:** This method should be called from the command bundler method.
             */
            addSchemaOptionsToCommand(localYargs, options) {
                const optionsWithAnalytics = (0, json_schema_1.addSchemaOptionsToCommand)(localYargs, options, this.context.args.options.help);
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
            /**
             * Flush on an interval (if the event loop is waiting).
             *
             * @returns a method that when called will terminate the periodic
             * flush and call flush one last time.
             */
            //   protected getAnalyticsParameters(
            //     options: (Options<T> & OtherOptions) | OtherOptions,
            //   ): Partial<Record<EventCustomDimension | EventCustomMetric, string | boolean | number>> {
            getAnalyticsParameters(options) {
                const parameters = {};
                // const validEventCustomDimensionAndMetrics = new Set([
                //   ...Object.values(EventCustomDimension),
                //   ...Object.values(EventCustomMetric),
                // ]);
                // for (const [name, ua] of this.optionsWithAnalytics) {
                //   const value = options[name];
                //   if (
                //     (typeof value === 'string' || typeof value === 'number' || typeof value === 'boolean') &&
                //     validEventCustomDimensionAndMetrics.has(ua as EventCustomDimension | EventCustomMetric)
                //   ) {
                //     parameters[ua as EventCustomDimension | EventCustomMetric] = value;
                //   }
                // }
                return parameters;
            }
        },
        (() => {
            const _metadata = typeof Symbol === "function" && Symbol.metadata ? Object.create(null) : void 0;
            _getAnalytics_decorators = [memoize_1.memoize];
            __esDecorate(_a, null, _getAnalytics_decorators, { kind: "method", name: "getAnalytics", static: false, private: false, access: { has: obj => "getAnalytics" in obj, get: obj => obj.getAnalytics }, metadata: _metadata }, null, _instanceExtraInitializers);
            if (_metadata) Object.defineProperty(_a, Symbol.metadata, { enumerable: true, configurable: true, writable: true, value: _metadata });
        })(),
        _a;
})();
exports.CommandModule = CommandModule;
/**
 * Creates an known command module error.
 * This is used so during executation we can filter between known validation error and real non handled errors.
 */
class CommandModuleError extends Error {
}
exports.CommandModuleError = CommandModuleError;
//# sourceMappingURL=command-module.js.map