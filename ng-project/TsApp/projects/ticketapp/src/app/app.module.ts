import { APP_INITIALIZER, NgModule } from '@angular/core';
import { BrowserModule, } from '@angular/platform-browser';
import { AppRouting } from './app.routing';
import { AppComponent } from './app.component';
import { RouterOutlet } from "@angular/router";
import { BrowserAnimationsModule } from "@angular/platform-browser/animations";
import { CommonModule, DatePipe, registerLocaleData } from '@angular/common';
import { provideAnimationsAsync } from '@angular/platform-browser/animations/async';
import {
    provideHttpClient,
    withFetch,
    withInterceptors
} from '@angular/common/http';
import {  TranslateModule } from "@ngx-translate/core";
import vi from '@angular/common/locales/vi';
import { MessageService } from "primeng/api";
import { tokenInterceptor } from "./guards/token.interceptor";
import { InputIconModule } from 'primeng/inputicon';
import { TsLoggerModule, LoggerService } from "ts-ui/logger";
import { DialogModule } from 'primeng/dialog';
import { DialogService } from 'primeng/dynamicdialog';
import { AppLayoutModule } from 'ts-ui/app-layout';
import { layoutConfig, toastConfig, translateConfig} from "./constant";
import {databaseConfig, StorageService} from "./services/storage.service";
import { AgTableModule } from 'ts-ui/ag-table';
import { ToastModule } from 'ts-ui/toast';
import {DBService, LocalDbModule} from "ts-ui/local-db";
import { FormsModule } from 'ts-ui/forms';

registerLocaleData(vi);

function LOAD_CFG(storage: StorageService, logger: LoggerService) {
    return () => storage.asyncConfig;
        //.subscribe({
       // error: err => logger.error('initialize app error --> ', err),
      //  next: res => logger.info('initialize app success --> ', res)
    //});
}

function LOAD_DB(service: DBService) {
    return () => service.initDatabase();
}

@NgModule({
    declarations: [
        AppComponent,
    ],
    imports: [
        BrowserModule,
        BrowserAnimationsModule,
        FormsModule,
        CommonModule,
        RouterOutlet,
        AppRouting,
        DatePipe,
        InputIconModule,
        DialogModule,
        AgTableModule.forRoot(),
        TsLoggerModule.forRoot(),
        AppLayoutModule.forRoot(layoutConfig),
        ToastModule.forRoot(toastConfig),
        TranslateModule.forRoot(translateConfig),
        LocalDbModule.forRoot(databaseConfig)

    ],
    providers: [
        provideAnimationsAsync(),
        provideHttpClient(withFetch(), withInterceptors([tokenInterceptor])),
        { provide: APP_INITIALIZER, useFactory: LOAD_DB, deps: [DBService], multi: true },
        { provide: APP_INITIALIZER, useFactory: LOAD_CFG, deps: [StorageService, LoggerService], multi: true },
        DatePipe, MessageService, DialogService,

    ],
    bootstrap: [AppComponent],
    exports: []
})
export class AppModule {
}