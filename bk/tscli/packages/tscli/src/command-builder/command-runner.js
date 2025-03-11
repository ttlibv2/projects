"use strict";
/**
 * @license
 * Copyright Google LLC All Rights Reserved.
 *
 * Use of this source code is governed by an MIT-style license that can be
 * found in the LICENSE file at https://angular.dev/license
 */
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
exports.runCommand = runCommand;
const yargs_1 = require("yargs");
const yargs_2 = require("yargs");
const helpers_1 = require("yargs/helpers");
const command_config_1 = require("../commands/command-config");
const color_1 = require("../utilities/color");
const config_1 = require("../utilities/config");
const error_1 = require("../utilities/error");
const package_manager_1 = require("../utilities/package-manager");
const command_module_1 = require("./command-module");
const command_1 = require("./utilities/command");
const json_help_1 = require("./utilities/json-help");
const normalize_options_middleware_1 = require("./utilities/normalize-options-middleware");
const yargsParser = helpers_1.Parser;
function runCommand(args, logger) {
    return __awaiter(this, void 0, void 0, function* () {
        var _a, _b;
        const sparse = yargsParser(args, {
            boolean: ['help', 'json-help', 'get-yargs-completions'],
            alias: { 'collection': 'c' },
        });
        const { $0, _, help = false, jsonHelp = false, getYargsCompletions = false } = sparse, rest = __rest(sparse, ["$0", "_", "help", "jsonHelp", "getYargsCompletions"]);
        // When `getYargsCompletions` is true the scriptName 'ng' at index 0 is not removed.
        const positional = getYargsCompletions ? _.slice(1) : _;
        let workspace;
        let globalConfiguration;
        try {
            [workspace, globalConfiguration] = yield Promise.all([(0, config_1.getWorkspace)('local'), (0, config_1.getWorkspace)('global'),]);
        }
        catch (e) {
            (0, error_1.assertIsError)(e);
            logger.fatal(e.message);
            return 1;
        }
        const root = (_a = workspace === null || workspace === void 0 ? void 0 : workspace.basePath) !== null && _a !== void 0 ? _a : process.cwd();
        const context = {
            globalConfiguration,
            workspace,
            logger,
            currentDirectory: process.cwd(),
            root,
            packageManager: new package_manager_1.PackageManagerUtils({ globalConfiguration, workspace, root }),
            args: {
                positional: positional.map((v) => v.toString()),
                options: Object.assign({ help,
                    jsonHelp,
                    getYargsCompletions }, rest),
            },
        };
        let localYargs = (0, yargs_1.default)(args);
        for (const CommandModule of yield getCommandsToRegister(positional[0])) {
            localYargs = (0, command_1.addCommandModuleToYargs)(localYargs, CommandModule, context);
        }
        if (jsonHelp) {
            // eslint-disable-next-line @typescript-eslint/no-explicit-any
            const usageInstance = localYargs.getInternalMethods().getUsageInstance();
            usageInstance.help = () => (0, json_help_1.jsonHelpUsage)();
        }
        // // Add default command to support version option when no subcommand is specified
        // localYargs.command('*', false, (builder) =>
        //   builder.version('version', 'Show Angular CLI version.', VERSION.full),
        // );
        yield localYargs
            .scriptName('ngx')
            .parserConfiguration({
            'populate--': true,
            'unknown-options-as-args': false,
            'dot-notation': false,
            'boolean-negation': true,
            'strip-aliased': true,
            'strip-dashed': true,
            'camel-case-expansion': false,
        })
            .option('json-help', {
            describe: 'Show help in JSON format.',
            implies: ['help'],
            hidden: true,
            type: 'boolean',
        })
            .help('help', 'Shows a help message for this command in the console.')
            // A complete list of strings can be found: https://github.com/yargs/yargs/blob/main/locales/en.json
            .updateStrings({
            'Commands:': color_1.colors.cyan('Commands:'),
            'Options:': color_1.colors.cyan('Options:'),
            'Positionals:': color_1.colors.cyan('Arguments:'),
            'deprecated': color_1.colors.yellow('deprecated'),
            'deprecated: %s': color_1.colors.yellow('deprecated:') + ' %s',
            'Did you mean %s?': 'Unknown command. Did you mean %s?',
        })
            .epilogue('For more information, see https://angular.dev/cli/.\n')
            .demandCommand(1, command_1.demandCommandFailureMessage)
            .recommendCommands()
            .middleware(normalize_options_middleware_1.normalizeOptionsMiddleware)
            .version(false)
            .showHelpOnFail(false)
            .strict()
            .fail((msg, err) => {
            throw msg
                ? // Validation failed example: `Unknown argument:`
                    new command_module_1.CommandModuleError(msg)
                : // Unknown exception, re-throw.
                    err;
        })
            .wrap((0, yargs_2.terminalWidth)())
            .parseAsync();
        return +((_b = process.exitCode) !== null && _b !== void 0 ? _b : 0);
    });
}
/**
 * Get the commands that need to be registered.
 * @returns One or more command factories that needs to be registered.
 */
function getCommandsToRegister(commandName) {
    return __awaiter(this, void 0, void 0, function* () {
        const commands = [];
        if (commandName in command_config_1.RootCommands) {
            commands.push(command_config_1.RootCommands[commandName]);
        }
        else if (commandName in command_config_1.RootCommandsAliases) {
            commands.push(command_config_1.RootCommandsAliases[commandName]);
        }
        else {
            // Unknown command, register every possible command.
            Object.values(command_config_1.RootCommands).forEach((c) => commands.push(c));
        }
        return Promise.all(commands.map((command) => command.factory().then((m) => m.default)));
    });
}
//# sourceMappingURL=command-runner.js.map