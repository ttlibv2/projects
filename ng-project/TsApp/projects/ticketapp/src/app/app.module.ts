import { APP_INITIALIZER, NgModule } from '@angular/core';
import {
  BrowserModule,
  provideClientHydration} from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import {RouterOutlet} from "@angular/router";
import {BrowserAnimationsModule} from "@angular/platform-browser/animations";
import {AppConfigModule} from "./views/app-config/app-config.module";
import { CommonModule, DatePipe, registerLocaleData } from '@angular/common';
import { provideAnimationsAsync } from '@angular/platform-browser/animations/async';
import {
  HttpClient,
  provideHttpClient,
  withFetch,
  withInterceptors} from '@angular/common/http';
import {TranslateLoader, TranslateModule, TranslateModuleConfig} from "@ngx-translate/core";
import vi from '@angular/common/locales/vi';
import {TranslateHttpLoader} from "@ngx-translate/http-loader";
import {ToastModule} from "primeng/toast";
import {MessageService} from "primeng/api";
import {tokenInterceptor} from "./guards/token.interceptor";
import { ConfigService } from './services/config.service';
import { LoggerModule, NGXLogger, NgxLoggerLevel, TOKEN_LOGGER_MAPPER_SERVICE, TOKEN_LOGGER_METADATA_SERVICE, TOKEN_LOGGER_WRITER_SERVICE } from 'ngx-logger';
import { LoggerMapperService } from './logger/mapper.service';
import { LoggerMetadataService } from './logger/metadata.service';
import { LoggerWriterService } from './logger/writer.service';
import { LoggerService } from './logger/logger.service';
import { MessagesModule } from 'primeng/messages';
import { InputIconModule } from 'primeng/inputicon';
import {ToastrModule} from "ngx-toastr";
import { trigger, state, style, transition, animate } from '@angular/animations';

registerLocaleData(vi);

function LOAD_CFG(config: ConfigService, logger: LoggerService) {
  return () => config.read().subscribe({
    error: err => logger.error('initialize app error --> ', err),
    next: res => logger.info('initialize app success --> ', res)
  });
}

function createTranslateLoader(http: HttpClient) {
  return new TranslateHttpLoader(http, './assets/i18n/', '.json');
}

const translateConfig: TranslateModuleConfig = {
    defaultLanguage: 'vi',
    loader: {
        provide: TranslateLoader,
        useFactory: (createTranslateLoader),
        deps: [HttpClient]
    }
}

@NgModule({
  declarations: [
    AppComponent,
  ],
  imports: [
    BrowserModule,

    BrowserAnimationsModule,
    CommonModule,
    RouterOutlet,
    AppRoutingModule,
    AppConfigModule,
    ToastModule,
    DatePipe,
      ToastrModule.forRoot({
        autoDismiss: true,
        closeButton: true,
        enableHtml: true,
        newestOnTop: true,
        tapToDismiss: true
      }),
    MessagesModule,
    InputIconModule,
    TranslateModule.forRoot(translateConfig),
    LoggerModule.forRoot(<any>{
      level: NgxLoggerLevel.TRACE,
      timestampFormat: 'yyyy-MM-dd HH:mm:ss',
      colorScheme: ['purple', 'teal', 'gray', 'gray', 'red', 'red', 'red'],
      disableMethodName: true,
      disableClassName: true,
      colorSchemeObj: {
        info: '#0ea5e9', log: '#f1f5f9', 
        warn: '#f97316', error: '#ef4444', 
        fatal: '#a855f7', debug: '#22c55e'
      },

    }, {
      mapperProvider: {provide: TOKEN_LOGGER_MAPPER_SERVICE, useClass: LoggerMapperService},
      metadataProvider: {provide: TOKEN_LOGGER_METADATA_SERVICE, useClass: LoggerMetadataService},
      writerProvider: {provide: TOKEN_LOGGER_WRITER_SERVICE, useClass: LoggerWriterService}
    }),
  ],
  providers: [
    provideAnimationsAsync(),
    provideHttpClient(withFetch(), withInterceptors([tokenInterceptor])),
   // provideClientHydration(),
   {provide: APP_INITIALIZER, useFactory: LOAD_CFG, deps: [ConfigService, LoggerService], multi: true},
    DatePipe,  MessageService,
  ],
  bootstrap: [AppComponent],
  exports: [
  ]
})
export class AppModule { }
