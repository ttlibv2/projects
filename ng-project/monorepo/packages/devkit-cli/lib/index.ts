import { writeErrorToLogFile } from "../src/utilities/log-file";
import { runCommand } from "../src/commands/core/runner.cmd";
import { Logger, colors } from "@ngdev/devkit-core/utilities";
import { CommandModuleError } from "../src/commands/core/abstract.cmd";
import { ERROR_PREFIX } from "../src/utilities/environment";
import { DevWorkspace } from '../src/workspace';

function changeCmdTitle() {
  // Provide a title to the process in `ps`.
  // Due to an obscure Mac bug, do not start this title with any symbol.
  try {
    process.title = "ngdev " + Array.from(process.argv).slice(2).join(" ");
  } catch (_) {
    // If an error happened above, use the most basic title.
    process.title = "ngdev";
  }
}

async function writeLog(logger: Logger, error: any) {
  // console.log('writeLog')

  if (error instanceof CommandModuleError) {
    logger.info(`${ERROR_PREFIX} ${error.message}`);
  } //
  else if (error instanceof Error) {
    try {

      const workspace = await DevWorkspace.project();
      const [logPath, message] = writeErrorToLogFile(error);

      let consoleMsg = error.message;

      if(!!workspace?.cli?.debug) {
        consoleMsg = message;
      }

      const lines: string[] = [
        `${colors.bold('Message:')} ${colors.cyan(consoleMsg)}`,
        `${colors.bold('See detail:')} ${colors.underline.magenta(logPath)}`
      ];

      logger.error(lines.join('\n'));

    } catch (e) {
      //
    }

    return 127;
  } //
  else if (typeof error === "string") {
    logger.fatal(error);
  } //
  else if (typeof error === "number") {
    // Log nothing.
  } //
  else {
    logger.fatal(`An unexpected error occurred: ${error}`);
  }

  return 1;
}

const logger = Logger.create("lib-main-logger");

export default async function (option: { cliArgs: string[] }) {
  //logger.warn(JSON.stringify(option));

  try {
    // change title process
    changeCmdTitle();

    // execute commands
    return await runCommand(option.cliArgs, logger);
  } catch (err) {
    // logger error
    await writeLog(logger, err);
  } finally {
    // fisnish
    logger.close();
  }
}