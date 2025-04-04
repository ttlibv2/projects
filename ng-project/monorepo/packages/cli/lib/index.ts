
import { writeErrorToLogFile } from '../src/utilities/log-file';
import {runCommand} from '../src/commands/runner.cmd';
import {Logger} from '@ngdev/devkit-core';
import { CommandModuleError } from '../src/commands/abstract.cmd';
import { ERROR_PREFIX } from '../src/utilities/environment';

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
 // console.log('writeLog')




  if (error instanceof CommandModuleError) {
   logger.info(`${ERROR_PREFIX} ${error.message}`);
  } //
  else if (error instanceof Error) {
    try {
      const logPath = writeErrorToLogFile(error);
      const msgLines: string[] = [];

      logger.error(`Message: ${error.message}\nLog Path: ${logPath}\nStack: ${error.stack || error}`
      );
    } //
    catch (e) {  }

    return 127;
  } //
  else if (typeof error === 'string') {
    logger.fatal(error);
  } //
  else if (typeof error === 'number') {
    // Log nothing.
  } //
  else {
    logger.fatal(`An unexpected error occurred: ${error}`);
  }

  return 1;
}

const logger = Logger.create('lib-main-logger');

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