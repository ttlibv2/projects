import {
    INGXLoggerConfig,
    INGXLoggerLogPosition,
    INGXLoggerMetadata,
    NgxLoggerLevel,
    TOKEN_LOGGER_MAPPER_SERVICE, TOKEN_LOGGER_METADATA_SERVICE, TOKEN_LOGGER_WRITER_SERVICE
} from "ngx-logger";
import {ClassProvider, ConstructorProvider, ExistingProvider, FactoryProvider, ValueProvider} from "@angular/core";
import {NGXLoggerColorScheme} from "ngx-logger";
import {LoggerMapperService} from "./mapper.service";
import {LoggerMetadataService} from "./metadata.service";
import {LoggerWriterService} from "./writer.service";

export const DEFAULT_COLOR_SCHEMA_OBJ: ColorSchemaObj = {
    info: '#0ea5e9', log: '#f1f5f9',
    warn: '#f97316', error: '#ef4444',
    fatal: '#a855f7', debug: '#22c55e'
}

export const defaultConfig: LoggerConfig = {
    //[TRACE = 0,DEBUG = 1,INFO = 2,LOG = 3,WARN = 4,ERROR = 5,FATAL = 6, OFF = 7]
    level: NgxLoggerLevel.TRACE,
    timestampFormat: 'yyyy-MM-dd HH:mm:ss',
    colorSchema: DEFAULT_COLOR_SCHEMA_OBJ,
    disableClassName: false,
    disableMethodName: false,
};

export const defaultProvider: LoggerProvider = {
    mapperProvider: {provide: TOKEN_LOGGER_MAPPER_SERVICE, useClass: LoggerMapperService},
    metadataProvider: {provide: TOKEN_LOGGER_METADATA_SERVICE, useClass: LoggerMetadataService},
    writerProvider: {provide: TOKEN_LOGGER_WRITER_SERVICE, useClass: LoggerWriterService}
};


export interface ColorSchemaObj {
    info: string,
    log: string,
    warn: string,
    error: string,
    fatal: string,
    debug: string
}

export interface LoggerConfig extends INGXLoggerConfig {

    // [TRACE = 0,DEBUG = 1,INFO = 2,LOG = 3,WARN = 4,ERROR = 5,FATAL = 6, OFF = 7]
    colorSchema: NGXLoggerColorScheme | ColorSchemaObj;
    disableClassName: boolean;
    disableMethodName: boolean;
}

export interface LoggerMetadata extends INGXLoggerMetadata {
    methodName?: string;
    className?: string;
}

export interface LoggerPosition extends INGXLoggerLogPosition {
    methodName?: string;
    className?: string;
}

export interface LoggerProvider {
    configProvider?: ValueProvider | ClassProvider | ConstructorProvider | ExistingProvider | FactoryProvider;
    configEngineFactoryProvider?: ValueProvider | ClassProvider | ConstructorProvider | ExistingProvider | FactoryProvider;
    metadataProvider?: ValueProvider | ClassProvider | ConstructorProvider | ExistingProvider | FactoryProvider;
    ruleProvider?: ValueProvider | ClassProvider | ConstructorProvider | ExistingProvider | FactoryProvider;
    mapperProvider?: ValueProvider | ClassProvider | ConstructorProvider | ExistingProvider | FactoryProvider;
    writerProvider?: ValueProvider | ClassProvider | ConstructorProvider | ExistingProvider | FactoryProvider;
    serverProvider?: ValueProvider | ClassProvider | ConstructorProvider | ExistingProvider | FactoryProvider;
}