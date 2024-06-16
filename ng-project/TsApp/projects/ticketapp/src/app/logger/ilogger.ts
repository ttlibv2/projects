import { INGXLoggerConfig, INGXLoggerLogPosition, INGXLoggerMetadata, NGXLogger } from "ngx-logger";

export const DEFAULT_COLOR_SCHEMA_OBJ: ColorSchemaObj = {info: '#0ea5e9', log: '#f1f5f9', 
    warn: '#f97316', error: '#ef4444', 
    fatal: '#a855f7', debug: '#22c55e'}

export interface ColorSchemaObj {
    info: string, log:string, 
    warn:string, error: string, 
    fatal: string, debug: string
}

export interface LoggerConfig extends INGXLoggerConfig {
    colorSchemaObj: ColorSchemaObj;
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