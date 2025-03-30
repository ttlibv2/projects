import { JsonObject, logging } from "@angular-devkit/core";
export type LogLevel = 'debug' | 'info' | 'warn' | 'error' | 'fatal';
export declare class Logger {
    readonly scLogger: logging.Logger;
    private fisnish;
    static create(name: string): Logger;
    constructor(scLogger: logging.Logger, fisnish: Promise<void>);
    close(): Promise<void>;
    log(level: LogLevel, message: string, metadata?: JsonObject): void;
    debug(message: string, metadata?: JsonObject): void;
    info(message: string, metadata?: JsonObject): void;
    warn(message: string, metadata?: JsonObject): void;
    error(message: string, metadata?: JsonObject): void;
    fatal(message: string, metadata?: JsonObject): void;
    logConsole(logPath: string, error: Error): void;
}
export declare function createLogger(name: string): {
    logger: logging.IndentLogger;
    finished: Promise<void>;
};
