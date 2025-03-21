
import { writeErrorToLogFile } from '../utilities/log-file';
import {runCommand} from '../command-builder/command.runner';
import {Logger} from '../utilities/logger';

function changeCmdTitle() {
  // Provide a title to the process in `ps`.
  // Due to an obscure Mac bug, do not start this title with any symbol.
  try {
    process.title = 'ngdev ' + Array.from(process.argv).slice(2).join(' ');
  } catch (_) {
    // If an error happened above, use the most basic title.
    process.title = 'ngdev';
  }
}

function writeLog(logger: Logger, err: any) {
  //if (err instanceof CommandModuleError) {
  //  logger.fatal(`Error: ${err.message}`);
  //} //
  //else 
  if (err instanceof Error) {
    try {
      const logPath = writeErrorToLogFile(err);
      logger.fatal(
        `An unhandled exception occurred: ${err.message}\n` +
        `See "${logPath}" for further details.`,
      );
    } //
    catch (e) {

      logger.fatal(
        `An unhandled exception occurred: ${err.message}\n` +
        `Fatal error writing debug log file: ${e}`,
      );

      if (err.stack) {
        logger.fatal(err.stack);
      }
    }

    return 127;
  } else if (typeof err === 'string') {
    logger.fatal(err);
  } else if (typeof err === 'number') {
    // Log nothing.
  } else {
    logger.fatal(`An unexpected error occurred: ${err}`);
  }

  return 1;
}

const logger = Logger.create('cli-main-logger');

export default async function (option: { cliArgs: string[] }) {
  try {

    // change title process
    changeCmdTitle();

    // execute commands
    return await runCommand(option.cliArgs, logger);
  }

  // logger error
  catch (err) {
    writeLog(logger, err);
  }

  // fisnish
  finally {
    logger.close();
  }

}

