import yargs, { terminalWidth, Argv } from 'yargs';
import { Parser } from 'yargs/helpers';
import { getCommandsToRegister } from '../commands/all';
import { colors } from '../utilities/color';
import { AngularWorkspace, getWorkspace } from '../utilities/config';
import { assertIsError } from '../utilities/error';
import { PackageManagerUtils } from '../utilities/package-manager';
import { CommandContext, CommandModuleError } from './abstract.command';
import {
  addCommandModuleToYargs,
  demandCommandFailureMessage,
} from './utilities/command';
import { jsonHelpUsage } from './utilities/json-help';
import { normalizeOptionsMiddleware } from './utilities/normalize-options-middleware';
import { Logger } from '../utilities/logger';

const yargsParser = Parser as unknown as typeof Parser;

export async function runCommand(args: string[], logger: Logger): Promise<number> {
  const abc = yargsParser(args, {
    boolean: ['help', 'json-help', 'get-yargs-completions'],
    alias: { 'collection': 'c' },
  });

  const {
    $0,
    _,
    help = false,
    jsonHelp = false,
    getYargsCompletions = false,
    ...rest
  } = abc;

 // logger.warn(JSON.stringify(abc));

  // When `getYargsCompletions` is true the scriptName 'ng' at index 0 is not removed.
  const positional = getYargsCompletions ? _.slice(1) : _;

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
      positional: positional.map((v) => v.toString()),
      options: { help, jsonHelp, getYargsCompletions, ...rest },
    },
  };

  let localYargs: Argv = yargs(args);
  // for (const CommandModule of await getCommandsToRegister(positional[0])) {
  //   localYargs = addCommandModuleToYargs(localYargs, CommandModule, context);
  // }

  // if (jsonHelp) {
  //   // eslint-disable-next-line @typescript-eslint/no-explicit-any
  //   const usageInstance = (localYargs as any).getInternalMethods().getUsageInstance();
  //   usageInstance.help = () => {
  //     console.log('usageInstance');
  //     jsonHelpUsage(localYargs)};
  // }

  // Add default command to support version option when no subcommand is specified
  // localYargs.command('*', false, builder => {
  //   builder.version('version', 'Output the current version', 'v25.03.21').alias('v', 'version');
    //builder.showVersion(msg => logger.warn(`CLI Version: ${msg}\n`));
    // builder.showHelp();
  // });

  // localYargs = localYargs.command('console', 'false, b => console.error(`localYargs`)');

  for (const CommandModule of await getCommandsToRegister(positional[0])) {
    localYargs = addCommandModuleToYargs(localYargs, CommandModule, context);
  }

  if (jsonHelp) {
    // eslint-disable-next-line @typescript-eslint/no-explicit-any
    const usageInstance = (localYargs as any).getInternalMethods().getUsageInstance();
    usageInstance.help = () => jsonHelpUsage(localYargs);
  }

  // Add default command to support version option when no subcommand is specified
  localYargs.command('*', false, (builder) =>
    builder.version('version', 'Show Angular CLI version.', 'VERSION.full'),
  );


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
      'Commands:': colors.cyan('Commands:'),
      'Options:': colors.cyan('Options:'),
      'Positionals:': colors.cyan('Arguments:'),
      'deprecated': colors.yellow('deprecated'),
      'deprecated: %s': colors.yellow('deprecated:') + ' %s',
      'Did you mean %s?': 'Unknown command. Did you mean %s?',
    })
    .epilogue('For more information, see https://angular.dev/cli/.\n')
    .demandCommand()
    //.recommendCommands()
    //.middleware(normalizeOptionsMiddleware)
    .version(false)
    .showHelpOnFail(true)
    .strict()
    // .fail((msg, err) => {
    //   throw msg
    //     ? // Validation failed example: `Unknown argument:`
    //       new CommandModuleError(msg)
    //     : // Unknown exception, re-throw.
    //       err;
    // })
   // .wrap(terminalWidth())
    .parseAsync();


  return +(process.exitCode ?? 0);
}