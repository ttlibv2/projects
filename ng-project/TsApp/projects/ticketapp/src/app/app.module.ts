import {APP_INITIALIZER, NgModule} from '@angular/core';
import {BrowserModule,} from '@angular/platform-browser';
import {AppRoutingModule} from './app-routing.module';
import {AppComponent} from './app.component';
import {RouterOutlet} from "@angular/router";
import {BrowserAnimationsModule} from "@angular/platform-browser/animations";
import {AppConfigModule} from "./views/app-config/app-config.module";
import {CommonModule, DatePipe, registerLocaleData} from '@angular/common';
import {provideAnimationsAsync} from '@angular/platform-browser/animations/async';
import {
    HttpClient,
    provideHttpClient,
    withFetch,
    withInterceptors
} from '@angular/common/http';
import {TranslateLoader, TranslateModule, TranslateModuleConfig} from "@ngx-translate/core";
import vi from '@angular/common/locales/vi';
import {TranslateHttpLoader} from "@ngx-translate/http-loader";
import {MessageService} from "primeng/api";
import {tokenInterceptor} from "./guards/token.interceptor";
import {ConfigService} from './services/config.service';
import {LoggerService} from './logger/logger.service';
import {InputIconModule} from 'primeng/inputicon';
import {ToastContainerDirective, ToastrModule} from "ngx-toastr";
import {TsLoggerModule} from "ts-logger";
import {ToastItem} from "./views/api/toast-item";
import { DialogModule } from 'primeng/dialog';
import { DialogService } from 'primeng/dynamicdialog';

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

const toastConfig: any = {
    toastClassPrefix: 'p-toast',
    closeClass: 'close-icon',
    autoDismiss: true,
    closeButton: true,
    enableHtml: true,
    newestOnTop: true,
    tapToDismiss: true,
    disableTimeOut: false,
    toastComponent: ToastItem,
    progressBar: false,
    //messageIcon: 'pi pi-user',
    position: 'top-right',

};

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
        DatePipe,
        InputIconModule,
        DialogModule,
        ToastrModule.forRoot(toastConfig),
        TranslateModule.forRoot(translateConfig),
        TsLoggerModule.forRoot(),
        ToastContainerDirective
    ],
    providers: [
        provideAnimationsAsync(),
        provideHttpClient(withFetch(), withInterceptors([tokenInterceptor])),
        // provideClientHydration(),
        {provide: APP_INITIALIZER, useFactory: LOAD_CFG, deps: [ConfigService, LoggerService], multi: true},
        DatePipe, MessageService, DialogService
    ],
    bootstrap: [AppComponent],
    exports: []
})
export class AppModule {
}