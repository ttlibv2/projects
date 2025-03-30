"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
exports.default = default_1;
const log_file_1 = require("../src/utilities/log-file");
const command_runner_1 = require("../src/commands/command.runner");
const logger_1 = require("../src/utilities/logger");
const abstract_cmd_1 = require("../src/commands/abstract.cmd");
const environment_1 = require("../src/utilities/environment");
function changeCmdTitle() {
    // Provide a title to the process in `ps`.
    // Due to an obscure Mac bug, do not start this title with any symbol.
    try {
        process.title = 'ngdev ' + Array.from(process.argv).slice(2).join(' ');
    }
    catch (_) {
        // If an error happened above, use the most basic title.
        process.title = 'ngdev';
    }
}
function writeLog(logger, error) {
    // console.log('writeLog')
    if (error instanceof abstract_cmd_1.CommandModuleError) {
        logger.info(`${environment_1.ERROR_PREFIX} ${error.message}`);
    } //
    else if (error instanceof Error) {
        try {
            const logPath = (0, log_file_1.writeErrorToLogFile)(error);
            const msgLines = [];
            logger.error(`Message: ${error.message}\nLog Path: ${logPath}\nStack: ${error.stack || error}`);
        } //
        catch (e) { }
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
const logger = logger_1.Logger.create('lib-main-logger');
async function default_1(option) {
    //logger.warn(JSON.stringify(option));
    try {
        // change title process
        changeCmdTitle();
        // execute commands
        return await (0, command_runner_1.runCommand)(option.cliArgs, logger);
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
//# sourceMappingURL=index.js.map