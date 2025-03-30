"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
exports.runCommand = runCommand;
// const { terminalWidth, Argv } = require('yargs');
const yargs = require("yargs");
const helpers_1 = require("yargs/helpers");
const color_1 = require("../utilities/color");
const config_1 = require("../utilities/config");
const error_1 = require("../utilities/error");
const package_manager_1 = require("../utilities/package-manager");
const abstract_cmd_1 = require("./abstract.cmd");
const add_cmd_to_args_1 = require("./helper/add-cmd-to-args");
const json_help_usage_1 = require("./helper/json-help-usage");
const normalize_middleware_1 = require("./helper/normalize-middleware");
const command_list_1 = require("./command.list");
const { bgYellow, red } = color_1.colors;
const pkg = Promise.resolve().then(() => require('../../package.json'));
const yargsParser = helpers_1.Parser;
const helpDesc = `See ${red('--help')} for a list of available commands`;
const invalidCmd = `Invalid command: ${bgYellow(red(` %s `))}\n${helpDesc}`;
const invalidArg = `Invalid argument: ${bgYellow(red(` %s `))}\n${helpDesc}`;
async function runCommand(args, logger) {
    const abc = yargsParser(args, {
        boolean: ['help', 'json-help', 'get-yargs-completions'],
        alias: { 'collection': 'c' },
    });
    const { $0, _, help = false, jsonHelp = false, getYargsCompletions = false, ...rest } = abc;
    // When `getYargsCompletions` is true the scriptName 'ngdev' at index 0 is not removed.
    const positional = getYargsCompletions ? _.slice(1) : _;
    let workspace;
    let globalConfiguration;
    try {
        [workspace, globalConfiguration] = await Promise.all([
            (0, config_1.getWorkspace)('local'),
            (0, config_1.getWorkspace)('global'),
        ]);
    }
    catch (e) {
        (0, error_1.assertIsError)(e);
        logger.fatal(e.message);
        return 1;
    }
    const root = workspace?.basePath ?? process.cwd();
    const context = {
        globalConfiguration, workspace, logger, root, currentDirectory: process.cwd(),
        packageManager: new package_manager_1.PackageManagerUtils({ globalConfiguration, workspace, root }),
        args: {
            positional: positional.map((v) => v.toString()),
            options: { help, jsonHelp, getYargsCompletions, ...rest },
        },
    };
    if (!args.length || (jsonHelp && !help)) {
        args.push('--help');
    }
    let localYargs = yargs(args);
    const usage = localYargs.getInternalMethods().getUsageInstance();
    for (const CommandModule of await getCommandsToRegister(positional[0])) {
        localYargs = (0, add_cmd_to_args_1.addCommandModuleToYargs)(localYargs, CommandModule, context);
    }
    if (jsonHelp) {
        usage.help = () => (0, json_help_usage_1.jsonHelpUsage)(localYargs);
    }
    // Add default command to support version option when no subcommand is specified
    //localYargs.command('*', false, b => logger.warn('wa'));
    await localYargs
        .scriptName('ngdev')
        .usage("Usage: $0 <command> [options]")
        //#customizing-yargs-parser
        .parserConfiguration({
        'populate--': true,
        'unknown-options-as-args': true,
        'dot-notation': false,
        'boolean-negation': true,
        'strip-aliased': true,
        'strip-dashed': true,
        'camel-case-expansion': false,
    })
        .option('json-help', {
        describe: 'Output usage JSON format.',
        implies: ['help'],
        hidden: true,
        type: 'boolean',
    })
        // A complete list of strings can be found: https://github.com/yargs/yargs/blob/main/locales/en.json
        .updateStrings({
        'Commands:': color_1.colors.cyan('Commands:'),
        'Options:': color_1.colors.cyan('Options:'),
        'Positionals:': color_1.colors.cyan('Arguments:'),
        'deprecated': color_1.colors.yellow('deprecated'),
        'deprecated: %s': color_1.colors.yellow('deprecated:') + ' %s',
        'Did you mean %s?': `Invalid command. Did you mean %s?`,
        'Show help': "Output usage information",
        'Show version': 'Output the current version',
        'Unknown command: %s': {
            "one": invalidCmd,
            "other": invalidCmd
        },
        'Unknown argument: %s': {
            "one": invalidArg,
            "other": invalidArg
        },
    })
        .epilogue('For more information, see https://ngdev.github.io/cli/.\n')
        .version((await pkg).version).alias("v", ["version"])
        .demandCommand(0, helpDesc) //.recommendCommands()
        .middleware(args => (0, normalize_middleware_1.normalizeMiddleware)(args, logger))
        .showHelpOnFail(false).help('help')
        .strict().strictOptions(true)
        .wrap(null)
        .fail((msg, err) => {
        throw msg ? new abstract_cmd_1.CommandModuleError(msg) : err;
    })
        .parseAsync();
    return +(process.exitCode ?? 0);
}
/**
 * Get the commands that need to be registered.
 * @returns One or more command factories that needs to be registered.
 */
async function getCommandsToRegister(commandName) {
    const commands = [];
    if (commandName in command_list_1.RootCommands) {
        commands.push(command_list_1.RootCommands[commandName]);
    } //
    else if (commandName in command_list_1.RootCommandsAliases) {
        commands.push(command_list_1.RootCommandsAliases[commandName]);
    }
    else {
        // Unknown command, register every possible command.
        Object.values(command_list_1.RootCommands).forEach((c) => commands.push(c));
    }
    return Promise.all(commands.map((command) => command.factory().then((m) => m.default)));
}
//# sourceMappingURL=command.runner.js.map