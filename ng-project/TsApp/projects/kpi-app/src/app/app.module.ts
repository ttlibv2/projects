import { NgModule } from '@angular/core';
import { BrowserModule, provideClientHydration } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import {RouterOutlet} from "@angular/router";
import {BrowserAnimationsModule} from "@angular/platform-browser/animations";
import {AppConfigModule} from "./views/app-config/app-config.module";
import { registerLocaleData } from '@angular/common';
import { provideAnimationsAsync } from '@angular/platform-browser/animations/async';
import {HttpClient, provideHttpClient, withFetch} from '@angular/common/http';
import {TranslateLoader, TranslateModule} from "@ngx-translate/core";
import vi from '@angular/common/locales/vi';
import {TranslateHttpLoader} from "@ngx-translate/http-loader";
import {ToastModule} from "primeng/toast";
import { DemoComponent } from './views/demo/demo.component';

registerLocaleData(vi);

export function createTranslateLoader(http: HttpClient) {
  return new TranslateHttpLoader(http, './assets/i18n/', '.json');
}

@NgModule({
  declarations: [
    AppComponent,
    DemoComponent,
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
    provideHttpClient(withFetch()),
    provideClientHydration()
  ],
  bootstrap: [AppComponent],
  exports: [
  ]
})
export class AppModule { }
