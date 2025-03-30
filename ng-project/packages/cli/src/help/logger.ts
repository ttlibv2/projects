import { JsonObject, logging } from "@angular-devkit/core";
import { colors, supportColor } from "./color";
import { stripVTControlCharacters, format } from "node:util";

export type LogLevel = 'debug' | 'info' | 'warn' | 'error' | 'fatal';

export class Logger {

  static create(name: string): Logger {
    const l = createLogger(name);
    return new Logger(l.logger, l.finished);
  }


  constructor(public readonly scLogger: logging.Logger,
    private fisnish: Promise<void>) { }

  async close() {
    this.scLogger.complete();
    await this.fisnish;
  }

  log(level: LogLevel, message: string, metadata?: JsonObject): void {
    this.scLogger.log(level, message, metadata);
  }
  debug(message: string, metadata?: JsonObject): void {
    this.scLogger.debug(message, metadata);
  }
  info(message: string, metadata?: JsonObject): void {
    this.scLogger.info(message, metadata);
  }
  warn(message: string, metadata?: JsonObject): void {
    this.scLogger.warn(message, metadata);
  }
  error(message: string, metadata?: JsonObject): void {
    this.scLogger.error(message, metadata);
  }
  fatal(message: string, metadata?: JsonObject): void {
    this.scLogger.fatal(message, metadata);
  }

  logConsole(logPath: string, error: Error): void {
    this.scLogger.error(`Message: ${error.message}\nLog Path: ${logPath}\nStack: ${error.stack || error}`);
  }
}

export function createLogger(name: string) {
  const colorLevels: Record<string, (message: string) => string> = {
    info: (s) => s,
    debug: (s) => s,
    warn: (s) => colors.bold.yellow(s),
    error: (s) => colors.bold.red(s),
    fatal: (s) => colors.bold.red(s),
  };

  const logger = new logging.IndentLogger(name);
  const logInfo = console.log;
  const logError = console.error;
  const useColor = supportColor();

  const finished = logger.forEach((entry) => {

    const color = useColor ? colorLevels[entry.level] : stripVTControlCharacters;
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
    logger.info(format(...args));
  };
  console.warn = function (...args) {
    logger.warn(format(...args));
  };
  console.error = function (...args) {
    logger.error(format(...args));
  };

  return {
    logger, finished
  }
}
