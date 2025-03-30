"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
exports.Logger = void 0;
exports.createLogger = createLogger;
const core_1 = require("@angular-devkit/core");
const color_1 = require("./color");
const node_util_1 = require("node:util");
class Logger {
    scLogger;
    fisnish;
    static create(name) {
        const l = createLogger(name);
        return new Logger(l.logger, l.finished);
    }
    constructor(scLogger, fisnish) {
        this.scLogger = scLogger;
        this.fisnish = fisnish;
    }
    async close() {
        this.scLogger.complete();
        await this.fisnish;
    }
    log(level, message, metadata) {
        this.scLogger.log(level, message, metadata);
    }
    debug(message, metadata) {
        this.scLogger.debug(message, metadata);
    }
    info(message, metadata) {
        this.scLogger.info(message, metadata);
    }
    warn(message, metadata) {
        this.scLogger.warn(message, metadata);
    }
    error(message, metadata) {
        this.scLogger.error(message, metadata);
    }
    fatal(message, metadata) {
        this.scLogger.fatal(message, metadata);
    }
    logConsole(logPath, error) {
        this.scLogger.error(`Message: ${error.message}\nLog Path: ${logPath}\nStack: ${error.stack || error}`);
    }
}
exports.Logger = Logger;
function createLogger(name) {
    const colorLevels = {
        info: (s) => s,
        debug: (s) => s,
        warn: (s) => color_1.colors.bold.yellow(s),
        error: (s) => color_1.colors.bold.red(s),
        fatal: (s) => color_1.colors.bold.red(s),
    };
    const logger = new core_1.logging.IndentLogger(name);
    const logInfo = console.log;
    const logError = console.error;
    const useColor = (0, color_1.supportColor)();
    const finished = logger.forEach((entry) => {
        const color = useColor ? colorLevels[entry.level] : node_util_1.stripVTControlCharacters;
        const message = color(entry.message);
        switch (entry.level) {
            case 'warn':
            case 'fatal':
            case 'error':
                logError(message);
                break;
            default:
                logInfo(message);
                break;
        }
    });
    // Redirect console to logger
    console.info = console.log = function (...args) {
        logger.info((0, node_util_1.format)(...args));
    };
    console.warn = function (...args) {
        logger.warn((0, node_util_1.format)(...args));
    };
    console.error = function (...args) {
        logger.error((0, node_util_1.format)(...args));
    };
    return {
        logger, finished
    };
}
//# sourceMappingURL=logger.js.map