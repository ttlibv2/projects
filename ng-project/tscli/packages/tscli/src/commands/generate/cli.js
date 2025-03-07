"use strict";
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
var __await = (this && this.__await) || function (v) { return this instanceof __await ? (this.v = v, this) : new __await(v); }
var __asyncGenerator = (this && this.__asyncGenerator) || function (thisArg, _arguments, generator) {
    if (!Symbol.asyncIterator) throw new TypeError("Symbol.asyncIterator is not defined.");
    var g = generator.apply(thisArg, _arguments || []), i, q = [];
    return i = Object.create((typeof AsyncIterator === "function" ? AsyncIterator : Object).prototype), verb("next"), verb("throw"), verb("return", awaitReturn), i[Symbol.asyncIterator] = function () { return this; }, i;
    function awaitReturn(f) { return function (v) { return Promise.resolve(v).then(f, reject); }; }
    function verb(n, f) { if (g[n]) { i[n] = function (v) { return new Promise(function (a, b) { q.push([n, v, a, b]) > 1 || resume(n, v); }); }; if (f) i[n] = f(i[n]); } }
    function resume(n, v) { try { step(g[n](v)); } catch (e) { settle(q[0][3], e); } }
    function step(r) { r.value instanceof __await ? Promise.resolve(r.value.v).then(fulfill, reject) : settle(q[0][2], r); }
    function fulfill(value) { resume("next", value); }
    function reject(value) { resume("throw", value); }
    function settle(f, v) { if (f(v), q.shift(), q.length) resume(q[0][0], q[0][1]); }
};
var __asyncValues = (this && this.__asyncValues) || function (o) {
    if (!Symbol.asyncIterator) throw new TypeError("Symbol.asyncIterator is not defined.");
    var m = o[Symbol.asyncIterator], i;
    return m ? m.call(o) : (o = typeof __values === "function" ? __values(o) : o[Symbol.iterator](), i = {}, verb("next"), verb("throw"), verb("return"), i[Symbol.asyncIterator] = function () { return this; }, i);
    function verb(n) { i[n] = o[n] && function (v) { return new Promise(function (resolve, reject) { v = o[n](v), settle(resolve, reject, v.done, v.value); }); }; }
    function settle(resolve, reject, d, v) { Promise.resolve(v).then(function(v) { resolve({ value: v, done: d }); }, reject); }
};
Object.defineProperty(exports, "__esModule", { value: true });
const core_1 = require("@angular-devkit/core");
const command_module_1 = require("../../command-builder/command-module");
const command_1 = require("../../command-builder/utilities/command");
const command_config_1 = require("../command-config");
const schematics_command_module_1 = require("../../command-builder/schematics-command-module");
class GenerateCommandModule extends schematics_command_module_1.SchematicsCommandModule {
    constructor() {
        super(...arguments);
        this.command = 'generate';
        this.aliases = command_config_1.RootCommands['generate'].aliases;
        this.describe = 'Generates and/or modifies files based on a schematic.';
    }
    builder(argv) {
        const _super = Object.create(null, {
            builder: { get: () => super.builder }
        });
        return __awaiter(this, void 0, void 0, function* () {
            let localYargs = (yield _super.builder.call(this, argv)).command({
                command: '$0 <schematic>',
                describe: 'Run the provided schematic.',
                builder: (localYargs) => localYargs
                    .positional('schematic', {
                    describe: 'The [collection:schematic] to run.',
                    type: 'string',
                    demandOption: true,
                }).strict(),
                handler: (options) => this.handler(options),
            });
            for (const [schematicName, collectionName] of yield this.getSchematicsToRegister()) {
                const workflow = this.getOrCreateWorkflowForBuilder(collectionName);
                const collection = workflow.engine.createCollection(collectionName);
                const { description: { schemaJson, aliases: schematicAliases, hidden: schematicHidden, description: schematicDescription, }, } = collection.createSchematic(schematicName, true);
                if (!schemaJson) {
                    continue;
                }
                const { 'x-deprecated': xDeprecated, description = schematicDescription, hidden = schematicHidden, } = schemaJson;
                const options = yield this.getSchematicOptions(collection, schematicName, workflow);
                localYargs = localYargs.command({
                    command: yield this.generateCommandString(collectionName, schematicName, options),
                    // When 'describe' is set to false, it results in a hidden command.
                    describe: hidden === true ? false : typeof description === 'string' ? description : '',
                    deprecated: xDeprecated === true || typeof xDeprecated === 'string' ? xDeprecated : false,
                    aliases: Array.isArray(schematicAliases)
                        ? yield this.generateCommandAliasesStrings(collectionName, schematicAliases)
                        : undefined,
                    builder: (localYargs) => this.addSchemaOptionsToCommand(localYargs, options).strict(),
                    handler: (options) => this.handler(Object.assign(Object.assign({}, options), { schematic: `${collectionName}:${schematicName}` })),
                });
            }
            return localYargs.demandCommand(1, command_1.demandCommandFailureMessage);
        });
    }
    run(options) {
        return __awaiter(this, void 0, void 0, function* () {
            const { dryRun, schematic, defaults, force, interactive } = options, schematicOptions = __rest(options, ["dryRun", "schematic", "defaults", "force", "interactive"]);
            const [collectionName, schematicName] = this.parseSchematicInfo(schematic);
            if (!collectionName || !schematicName) {
                throw new command_module_1.CommandModuleError('A collection and schematic is required during execution.');
            }
            return this.runSchematic({
                collectionName,
                schematicName,
                schematicOptions,
                executionOptions: {
                    dryRun,
                    defaults,
                    force,
                    interactive,
                },
            });
        });
    }
    getCollectionNames() {
        return __awaiter(this, void 0, void 0, function* () {
            const [collectionName] = this.parseSchematicInfo(
            // positional = [generate, component] or [generate]
            this.context.args.positional[1]);
            return collectionName ? [collectionName] : [...(yield this.getSchematicCollections())];
        });
    }
    shouldAddCollectionNameAsPartOfCommand() {
        return __awaiter(this, void 0, void 0, function* () {
            const [collectionNameFromArgs] = this.parseSchematicInfo(
            // positional = [generate, component] or [generate]
            this.context.args.positional[1]);
            const schematicCollectionsFromConfig = yield this.getSchematicCollections();
            const collectionNames = yield this.getCollectionNames();
            // Only add the collection name as part of the command when it's not a known
            // schematics collection or when it has been provided via the CLI.
            // Ex:`ng generate @schematics/angular:c`
            return (!!collectionNameFromArgs ||
                !collectionNames.some((c) => schematicCollectionsFromConfig.has(c)));
        });
    }
    /**
     * Generate an aliases string array to be passed to the command builder.
     *
     * @example `[component]` or `[@schematics/angular:component]`.
     */
    generateCommandAliasesStrings(collectionName, schematicAliases) {
        return __awaiter(this, void 0, void 0, function* () {
            // Only add the collection name as part of the command when it's not a known
            // schematics collection or when it has been provided via the CLI.
            // Ex:`ng generate @schematics/angular:c`
            return (yield this.shouldAddCollectionNameAsPartOfCommand())
                ? schematicAliases.map((alias) => `${collectionName}:${alias}`)
                : schematicAliases;
        });
    }
    /**
     * Generate a command string to be passed to the command builder.
     *
     * @example `component [name]` or `@schematics/angular:component [name]`.
     */
    generateCommandString(collectionName, schematicName, options) {
        return __awaiter(this, void 0, void 0, function* () {
            const dasherizedSchematicName = core_1.strings.dasherize(schematicName);
            // Only add the collection name as part of the command when it's not a known
            // schematics collection or when it has been provided via the CLI.
            // Ex:`ng generate @schematics/angular:component`
            const commandName = (yield this.shouldAddCollectionNameAsPartOfCommand())
                ? collectionName + ':' + dasherizedSchematicName
                : dasherizedSchematicName;
            const positionalArgs = options
                .filter((o) => o.positional !== undefined)
                .map((o) => {
                const label = `${core_1.strings.dasherize(o.name)}${o.type === 'array' ? ' ..' : ''}`;
                return o.required ? `<${label}>` : `[${label}]`;
            })
                .join(' ');
            return `${commandName}${positionalArgs ? ' ' + positionalArgs : ''}`;
        });
    }
    /**
     * Get schematics that can to be registered as subcommands.
     */
    getSchematics() {
        return __asyncGenerator(this, arguments, function* getSchematics_1() {
            const seenNames = new Set();
            for (const collectionName of yield __await(this.getCollectionNames())) {
                const workflow = this.getOrCreateWorkflowForBuilder(collectionName);
                const collection = workflow.engine.createCollection(collectionName);
                for (const schematicName of collection.listSchematicNames(true /** includeHidden */)) {
                    // If a schematic with this same name is already registered skip.
                    if (!seenNames.has(schematicName)) {
                        seenNames.add(schematicName);
                        yield yield __await({
                            schematicName,
                            collectionName,
                            schematicAliases: this.listSchematicAliases(collection, schematicName),
                        });
                    }
                }
            }
        });
    }
    listSchematicAliases(collection, schematicName) {
        const description = collection.description.schematics[schematicName];
        if (description) {
            return description.aliases && new Set(description.aliases);
        }
        // Extended collections
        if (collection.baseDescriptions) {
            for (const base of collection.baseDescriptions) {
                const description = base.schematics[schematicName];
                if (description) {
                    return description.aliases && new Set(description.aliases);
                }
            }
        }
        return undefined;
    }
    /**
     * Get schematics that should to be registered as subcommands.
     *
     * @returns a sorted list of schematic that needs to be registered as subcommands.
     */
    getSchematicsToRegister() {
        return __awaiter(this, void 0, void 0, function* () {
            var _a, e_1, _b, _c;
            const schematicsToRegister = [];
            const [, schematicNameFromArgs] = this.parseSchematicInfo(
            // positional = [generate, component] or [generate]
            this.context.args.positional[1]);
            try {
                for (var _d = true, _e = __asyncValues(this.getSchematics()), _f; _f = yield _e.next(), _a = _f.done, !_a; _d = true) {
                    _c = _f.value;
                    _d = false;
                    const { schematicName, collectionName, schematicAliases } = _c;
                    if (schematicNameFromArgs &&
                        (schematicName === schematicNameFromArgs || (schematicAliases === null || schematicAliases === void 0 ? void 0 : schematicAliases.has(schematicNameFromArgs)))) {
                        return [[schematicName, collectionName]];
                    }
                    schematicsToRegister.push([schematicName, collectionName]);
                }
            }
            catch (e_1_1) { e_1 = { error: e_1_1 }; }
            finally {
                try {
                    if (!_d && !_a && (_b = _e.return)) yield _b.call(_e);
                }
                finally { if (e_1) throw e_1.error; }
            }
            // Didn't find the schematic or no schematic name was provided Ex: `ng generate --help`.
            return schematicsToRegister.sort(([nameA], [nameB]) => nameA.localeCompare(nameB, undefined, { sensitivity: 'accent' }));
        });
    }
}
exports.default = GenerateCommandModule;
//# sourceMappingURL=cli.js.map