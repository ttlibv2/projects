import { Inject, Injectable, PLATFORM_ID } from "@angular/core";
import {INGXLoggerMetadata, NGXLoggerWriterService, NgxLoggerLevel } from "ngx-logger";
import { LoggerConfig, LoggerMetadata } from "./ilogger";
import { Objects } from "../utils/objects";

@Injectable({ providedIn: "root" })
export class LoggerWriterService extends NGXLoggerWriterService {

    constructor( @Inject(PLATFORM_ID) platformId: any) {
        super(platformId);
        this.prepareMetaStringFuncs = [
            this.getTimestampToWrite,
            this.getLevelToWrite,
            this.getClassNameToWrite,
            this.getMethodNameToWrite,
            this.getFileDetailsToWrite,
            this.getContextToWrite,
        ];
      }

      protected getClassNameToWrite(metadata: LoggerMetadata, config: LoggerConfig): string {
        return config.disableClassName === true || Objects.isBlank(metadata.className) ? '' : `[${metadata.className}]`;
      }

      protected getMethodNameToWrite(metadata: LoggerMetadata, config: LoggerConfig): string {
        return config.disableMethodName === true || Objects.isBlank(metadata.methodName) ? '' : `[${metadata.methodName}]`;
      }


    protected override getColor(metadata: INGXLoggerMetadata, config: any): string {
        if(Objects.isNull(config['colorSchemeObj'])) return super.getColor(metadata, config);
        else {
            const object = config['colorSchemeObj'];
            switch(metadata.level) {
                case NgxLoggerLevel.WARN: return object['warn'];
                case NgxLoggerLevel.ERROR: return object['error'];
                case NgxLoggerLevel.FATAL: return object['fatal'];
                case NgxLoggerLevel.INFO: return object['info'];
                case NgxLoggerLevel.DEBUG: return object['debug'];
                default: return object['log'];
            }
        }
    }

}