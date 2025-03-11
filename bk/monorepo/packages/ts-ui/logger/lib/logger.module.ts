import { ModuleWithProviders, NgModule,} from "@angular/core";
import { defaultConfig, defaultProvider, LoggerConfig, LoggerProvider} from "./ilogger";
import {CommonModule, DatePipe} from "@angular/common";

import {LoggerService} from "./logger.service";
import {LoggerModule} from "ngx-logger";



@NgModule({
    imports: [
        CommonModule,
        LoggerModule
    ]
})
export class TsLoggerModule {

    static forRoot(config?: LoggerConfig, customProvider?: LoggerProvider): ModuleWithProviders<TsLoggerModule> {
        const configNew = Object.assign({}, defaultConfig, config);
        const providerNew = Object.assign({}, defaultProvider, customProvider);
        const ngxModule = LoggerModule.forRoot(configNew, providerNew);

        return {
          ngModule: TsLoggerModule,
          providers: [
              ...ngxModule.providers,
              LoggerService,
              DatePipe

          ]
        };
    }

    static forChild(): ModuleWithProviders<TsLoggerModule> {
        return {
            ngModule: TsLoggerModule
        }
    }

}