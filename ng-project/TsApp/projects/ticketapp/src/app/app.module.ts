import { APP_INITIALIZER, ErrorHandler, NgModule } from '@angular/core';
import { BrowserModule, } from '@angular/platform-browser';
import { AppRouting } from './app.routing';
import { AppComponent } from './app.component';
import { RouterOutlet } from "@angular/router";
import { PlatformModule } from "@angular/cdk/platform";
import { BrowserAnimationsModule } from "@angular/platform-browser/animations";
import { CommonModule, DatePipe, registerLocaleData } from '@angular/common';
import { provideAnimationsAsync } from '@angular/platform-browser/animations/async';
import {
    HTTP_INTERCEPTORS,
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
import { AppLayoutModule } from 'ts-ui/applayout';
import { layoutConfig, toastConfig, translateConfig} from "./constant";
import {databaseConfig, StorageService} from "./services/storage.service";
import { AgTableModule } from 'ts-ui/agtable';
import { ToastModule } from 'ts-ui/toast';
import {DBService, LocalDbModule} from "ts-ui/localdb";
import { FormsModule } from 'ts-ui/forms';
import { ErrorInterceptor } from './services/error-interceptor';
import { AppLayoutModule2 } from 'ts-ui/layout';
import { LayoutModule } from "./views/zlayout/zlayout.module";
import { FormlyPrimeNGModule } from '@ngx-formly/primeng';
import { FormlyModule } from '@ngx-formly/core';

class ErrorHandlerBasic implements ErrorHandler {

    handleError(error: any): void {
        console.log(error);
    }


}
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
        PlatformModule,
        FormsModule,
        CommonModule,
        RouterOutlet,
        AppRouting,
        DatePipe,
        InputIconModule,
        DialogModule,
        AppLayoutModule2,
        FormlyPrimeNGModule, 
        FormlyModule.forRoot(),
        AgTableModule.forRoot(),
        TsLoggerModule.forRoot(),
        AppLayoutModule.forRoot(layoutConfig),
        ToastModule.forRoot(toastConfig),
        TranslateModule.forRoot(translateConfig),
        LocalDbModule.forRoot(databaseConfig),
        LayoutModule

    ],
    providers: [
        provideAnimationsAsync(),
        provideHttpClient(withFetch(), withInterceptors([tokenInterceptor])),
        { provide: APP_INITIALIZER, useFactory: LOAD_DB, deps: [DBService], multi: true },
        { provide: APP_INITIALIZER, useFactory: LOAD_CFG, deps: [StorageService, LoggerService], multi: true },
        { provide: ErrorHandler, useClass: ErrorHandlerBasic },
        { provide: HTTP_INTERCEPTORS, useClass: ErrorInterceptor, multi: true},
        DatePipe, MessageService, DialogService,

    ],
    bootstrap: [AppComponent],
    exports: []
})
export class AppModule {
}