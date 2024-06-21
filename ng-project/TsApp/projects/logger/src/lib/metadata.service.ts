import { Injectable } from "@angular/core";
import { INGXLoggerConfig, NGXLoggerMetadataService, NgxLoggerLevel } from "ngx-logger";
import { LoggerMetadata } from "./ilogger";
import {Objects} from "ts-helper";

@Injectable({ providedIn: "root" })
export class LoggerMetadataService extends NGXLoggerMetadataService {

    override getMetadata(level: NgxLoggerLevel, config: INGXLoggerConfig, message?: any, additional?: any[]): LoggerMetadata {
        return {
            level: level,
            additional: additional,
            message: Objects.isFunction(message) ? message() : message,
            timestamp: this.computeTimestamp(config)
        };
    }
}