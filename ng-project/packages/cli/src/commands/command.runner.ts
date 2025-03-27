// const { terminalWidth, Argv } = require('yargs');
import yargs = require('yargs');
import { Parser } from 'yargs/helpers';
import { colors } from '../utilities/color';
import { AngularWorkspace, getWorkspace } from '../utilities/config';
import { assertIsError } from '../utilities/error';
import { PackageManagerUtils } from '../utilities/package-manager';
import { CommandContext, CommandModuleError } from './abstract.cmd';
import { addCommandModuleToYargs, CommandModuleConstructor } from './helper/add-cmd-to-args';
import { jsonHelpUsage } from './helper/json-help-usage';
import { normalizeMiddleware } from './helper/normalize-middleware';
import { Logger } from '../utilities/logger';
import { CommandConfig, RootCommands, RootCommandsAliases } from "./command.list";

const {bgYellow, red} = colors;

const pkg = import('../../package.json');

const yargsParser = Parser as unknown as typeof Parser;
const helpDesc = `See ${red('--help')} for a list of available commands`;
const invalidCmd = `Invalid command: ${bgYellow(red(` %s `))}\n${helpDesc}`;
const invalidArg = `Invalid argument: ${bgYellow(red(` %s `))}\n${helpDesc}`;

export async function runCommand(args: string[], logger: Logger): Promise<number> {
  const abc = yargsParser(args, {
    boolean: ['help', 'json-help', 'get-yargs-completions'],
    alias: { 'collection': 'c' },
  });

  const { $0, _, help = false, jsonHelp = false, getYargsCompletions = false, ...rest } = abc;

  // When `getYargsCompletions` is true the scriptName 'ngdev' at index 0 is not removed.
  const positional = getYargsCompletions ? _.slice(1) : _;

  if (!args.length) {

  }

  let workspace: AngularWorkspace | undefined;
  let globalConfiguration: AngularWorkspace;
  try {
    [workspace, globalConfiguration] = await Promise.all([
      getWorkspace('local'),
      getWorkspace('global'),
    ]);
  } catch (e) {
    assertIsError(e);
    logger.fatal(e.message);

    return 1;
  }

  const root = workspace?.basePath ?? process.cwd();
  const context: CommandContext = {
    globalConfiguration, workspace, logger, root, currentDirectory: process.cwd(),
    packageManager: new PackageManagerUtils({ globalConfiguration, workspace, root }),
    args: {
      positional: positional.map((v: any) => v.toString()),
      options: { help, jsonHelp, getYargsCompletions, ...rest },
    },
  };

  if (!args.length || (jsonHelp && !help)) {
    args.push('--help');
  }

  let localYargs: yargs.Argv = yargs(args);
  const usage = (localYargs as any).getInternalMethods().getUsageInstance();

  for (const CommandModule of await getCommandsToRegister(positional[0])) {
    localYargs = addCommandModuleToYargs(localYargs, CommandModule, context);
  }

  if (jsonHelp) {
    usage.help = () => jsonHelpUsage(localYargs);
  }

  // Add default command to support version option when no subcommand is specified
  //localYargs.command('*', false, b => logger.warn('wa'));

  localYargs
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
      'Commands:': colors.cyan('Commands:'),
      'Options:': colors.cyan('Options:'),
      'Positionals:': colors.cyan('Arguments:'),
      'deprecated': colors.yellow('deprecated'),
      'deprecated: %s': colors.yellow('deprecated:') + ' %s',
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
    .demandCommand(0, helpDesc)//.recommendCommands()
    .middleware(args => normalizeMiddleware(args, logger))
    .showHelpOnFail(false).help('help')
    .strict().strictOptions(true)
    .wrap(null)
    .fail((msg, err) => { throw msg ? new CommandModuleError(msg) : err })
    .parseAsync();


  return +(process.exitCode ?? 0);
}

/**
 * Get the commands that need to be registered.
 * @returns One or more command factories that needs to be registered.
 */
async function getCommandsToRegister(commandName: string | number): Promise<CommandModuleConstructor[]> {
  const commands: CommandConfig[] = [];
  if (commandName in RootCommands) {
    commands.push(RootCommands[commandName]);
  } //
  else if (commandName in RootCommandsAliases) {
    commands.push(RootCommandsAliases[commandName]);
  } else {
    // Unknown command, register every possible command.
    Object.values(RootCommands).forEach((c) => commands.push(c));
  }

  return Promise.all(commands.map((command) => command.factory().then((m) => m.default)));
}