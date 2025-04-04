import yargs = require('yargs');
import { Parser } from 'yargs/helpers';
import { colors } from '@ngdev/devkit-core';
import { assertIsError } from '../utilities/error';
import { CommandContext, CommandModuleError } from './abstract.cmd';
import { addCommandModuleToYargs, CommandModuleConstructor } from './helper/add-cmd-to-args';
import { jsonHelpUsage } from './helper/json-help-usage';
import { normalizeMiddleware } from './helper/normalize-middleware';
import { Logger,PkgManagerFactory, EnumPkg } from '@ngdev/devkit-core';
import { CommandConfig, RootCommands, RootCommandsAliases } from "./command.list";
import {WorkspaceDev } from "../workspace";
import {Arguments, DetailedArguments, Dictionary} from "../typings/yargs";
import {yargsUnknownCommands} from "./helper/yargs-func";

const {bgYellow, red} = colors;

const pkg = import('../../package.json');

const yargsParser = Parser as unknown as typeof Parser;
const helpDesc = `See ${red('--help')} for a list of available commands`;
const invalidCmd = `Invalid command: ${bgYellow(red(` %s `))}\n${helpDesc}`;
const invalidArg = `Invalid argument: ${bgYellow(red(` %s `))}\n${helpDesc}`;

export async function runCommand(args: string[], logger: Logger): Promise<number> {
  const abc = yargsParser(args, {
    boolean: ['help', 'json-utilities', 'get-yargs-completions'],
    alias: { 'collection': 'c' },
  });

  const { $0, _, help = false, jsonHelp = false, getYargsCompletions = false, ...rest } = abc;

  // When `getYargsCompletions` is true the scriptName 'ngdev' at index 0 is not removed.
  const positional = getYargsCompletions ? _.slice(1) : _;

  let workspace: WorkspaceDev | undefined;
  let globalConfiguration: WorkspaceDev;

  try {
    [workspace, globalConfiguration] = await Promise.all([
      WorkspaceDev.project(),
      WorkspaceDev.global(),
    ]);
  } //
  catch (e) {
    assertIsError(e);
    logger.fatal(e.message);
    return 1;
  }

  const root = workspace?.basePath ?? process.cwd();

  //
  const pkgmanager = PkgManagerFactory.create(EnumPkg.PNPM);
  pkgmanager.logger = logger;

  const context: CommandContext = {
    globalConfiguration, workspace, logger, root,
    currentDirectory: process.cwd(),
    packageManager: pkgmanager,
    args: {
      positional: positional.map((v: any) => v.toString()),
      options: { help, jsonHelp, getYargsCompletions, ...rest },
    },
  };

  if (!args.length || (jsonHelp && !help)) {
    args.push('--help');
  }

  let localYargs: yargs.Argv = yargs(args);


  //const validator = localYargs.getInternalMethods().getValidationInstance();
  //validator.unknownCommands = (argv: yargs.Arguments): boolean => yargsUnknownCommands(argv);

  const usage = localYargs.getInternalMethods().getUsageInstance();

  for (const CommandModule of await getCommandsToRegister(positional[0])) {
    localYargs = addCommandModuleToYargs(localYargs, CommandModule, context);
  }

  if (jsonHelp) {
    usage.help = () => jsonHelpUsage(localYargs);
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
    .version((await pkg).version)//.alias("v", ["version"])
    .demandCommand(0, helpDesc)//.recommendCommands()
    .middleware(args => normalizeMiddleware(args, logger))
    .showHelpOnFail(false).help('help')
    .strict().strictOptions(true).strictCommands(true)
    .wrap(null).fail((msg, err) => {
      throw msg ? new CommandModuleError(msg) : err
    })
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