import { NgModule } from '@angular/core';
import {
  BrowserModule,
  provideClientHydration,
  withHttpTransferCacheOptions,
  withNoHttpTransferCache
} from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import {RouterOutlet} from "@angular/router";
import {BrowserAnimationsModule} from "@angular/platform-browser/animations";
import {AppConfigModule} from "./views/app-config/app-config.module";
import { registerLocaleData } from '@angular/common';
import { provideAnimationsAsync } from '@angular/platform-browser/animations/async';
import {
  HTTP_INTERCEPTORS,
  HttpClient,
  provideHttpClient,
  withFetch,
  withInterceptors,
  withInterceptorsFromDi
} from '@angular/common/http';
import {TranslateLoader, TranslateModule} from "@ngx-translate/core";
import vi from '@angular/common/locales/vi';
import {TranslateHttpLoader} from "@ngx-translate/http-loader";
import {ToastModule} from "primeng/toast";
import {MessageModule} from "primeng/message";
import {MessageService} from "primeng/api";
import {tokenInterceptor} from "./guards/token.interceptor";

registerLocaleData(vi);

export function createTranslateLoader(http: HttpClient) {
  return new TranslateHttpLoader(http, './assets/i18n/', '.json');
}

@NgModule({
  declarations: [
    AppComponent,
  ],
  imports: [
    BrowserModule,
    TranslateModule.forRoot({
      defaultLanguage: 'vi',
      loader: {
          provide: TranslateLoader,
          useFactory: (createTranslateLoader),
          deps: [HttpClient]
      }

    }),
    RouterOutlet,
    AppRoutingModule,
    AppConfigModule,
    BrowserAnimationsModule,
    ToastModule
  ],
  providers: [
    provideAnimationsAsync(),
    provideHttpClient(withFetch(), withInterceptors([tokenInterceptor])),
    provideClientHydration(),
    MessageService,
    //{provide: HTTP_INTERCEPTORS, useFactory: tokenInterceptor, multi: true},
  ],
  bootstrap: [AppComponent],
  exports: [
  ]
})
export class AppModule { }
