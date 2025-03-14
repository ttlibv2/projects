"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
exports.runCommand = runCommand;
const tslib_1 = require("tslib");
const yargs_1 = tslib_1.__importDefault(require("yargs"));
const helpers_1 = require("yargs/helpers");
const command_config_1 = require("../command-config");
const color_1 = require("../../utilities/color");
const config_1 = require("../../utilities/config");
const error_1 = require("../../utilities/error");
const pkg_manager_1 = require("../../utilities/pkg-manager");
const version_1 = require("../../utilities/version");
const command_module_1 = require("./command-module");
const command_1 = require("./command");
const json_help_1 = require("../../utilities/json-help");
const middleware_1 = require("../../utilities/middleware");
const yargsParser = helpers_1.Parser; // as unknown as typeof Parser['default'];
async function runCommand(args, logger) {
    const parseObject = yargsParser(args, {
        boolean: ['help', 'json-help', 'get-yargs-completions'],
        alias: { 'collection': 'c' },
    });
    const { $0, _, help = false, jsonHelp = false, getYargsCompletions = false, ...rest } = parseObject;
    // When `getYargsCompletions` is true the scriptName 'ng' at index 0 is not removed.
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
        globalConfiguration,
        workspace,
        logger,
        currentDirectory: process.cwd(),
        root,
        packageManager: new pkg_manager_1.PackageManagerUtils({ globalConfiguration, workspace, root }),
        args: {
            positional: positional.map((v) => v.toString()),
            options: {
                help,
                jsonHelp,
                getYargsCompletions,
                ...rest,
            },
        },
    };
    let localYargs = (0, yargs_1.default)(args);
    for (const CommandModule of await getCommandsToRegister(positional[0])) {
        localYargs = (0, command_1.addCommandModuleToYargs)(localYargs, CommandModule, context);
    }
    if (jsonHelp) {
        // eslint-disable-next-line @typescript-eslint/no-explicit-any
        const usageInstance = localYargs.getInternalMethods().getUsageInstance();
        usageInstance.help = () => (0, json_help_1.jsonHelpUsage)();
    }
    // Add default command to support version option when no subcommand is specified
    localYargs.command('*', false, (builder) => builder.version('version', 'Show Angular CLI version.', version_1.VERSION.full));
    await localYargs
        .scriptName('ng')
        // https://github.com/yargs/yargs/blob/main/docs/advanced.md#customizing-yargs-parser
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
        // .demandCommand(1, demandCommandFailureMessage)
        .recommendCommands()
        .middleware(middleware_1.normalizeOptionsMiddleware)
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
        //.wrap(terminalWidth())
        .parseAsync();
    return +(process.exitCode ?? 0);
}
/**
 * Get the commands that need to be registered.
 * @returns One or more command factories that needs to be registered.
 */
async function getCommandsToRegister(commandName) {
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
}
