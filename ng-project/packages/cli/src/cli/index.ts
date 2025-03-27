
import { writeErrorToLogFile } from '../utilities/log-file';
import {runCommand} from '../commands/command.runner';
import {Logger} from '../utilities/logger';
import { CommandModuleError } from '../commands/abstract.cmd';
import { ERROR_PREFIX } from '../utilities/environment';

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

function writeLog(logger: Logger, error: any) {
  console.log('writeLog')




  if (error instanceof CommandModuleError) {
   logger.info(`${ERROR_PREFIX} ${error.message}`);
  } //
  else 
  if (error instanceof Error) {
    try {
      const logPath = writeErrorToLogFile(error);
      logger.fatal(`AAA: ${error.stack || error}`);

      // logger.fatal(
      //   `An unhandled exception occurred: ${error.message}\n` +
      //   `See "${logPath}" for further details.`,
      // );
    } //
    catch (e) {

      logger.fatal(
        `An unhandled exception occurred: ${error.message}\n` +
        `Fatal error writing debug log file: ${e}`,
      );

      if (error.stack) {
        logger.fatal(error.stack);
      }
    }

    return 127;
  } else if (typeof error === 'string') {
    logger.fatal(error);
  } else if (typeof error === 'number') {
    // Log nothing.
  } else {
    logger.fatal(`An unexpected error occurred: ${error}`);
  }

  return 1;
}

const logger = Logger.create('cli-main-logger');

export default async function (option: { cliArgs: string[] }) {
  //logger.warn(JSON.stringify(option));
  
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