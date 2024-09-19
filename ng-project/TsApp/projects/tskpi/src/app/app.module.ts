import { APP_INITIALIZER, NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { DatePipe } from '@angular/common';
import { MessageService } from 'primeng/api';
import { DialogService } from 'primeng/dynamicdialog';
import { ToastModule } from 'ts-ui/toast';
import { AppLayoutModule, LayoutConfig } from 'ts-ui/app-layout';
import { KpiConfig } from './service/kpi-config';
import { provideAnimationsAsync } from '@angular/platform-browser/animations/async';
import { provideHttpClient, withFetch, withInterceptors } from '@angular/common/http';
import { FileUploadModule } from 'primeng/fileupload';


export const layoutConfig: LayoutConfig = {
  visibleTopBar: false,
  visibleSideBar: false,
  visibleConfig: false,
  appLogo: 'assets/images/logo-dark.svg',
  appName: 'ConYeu.Vn',
  menuTheme: 'light',
  menuMode: 'slim',
  theme: 'aura-light',
  colorScheme: 'indigo',
  themeUrlPrefix: '',
};

export function LOAD_CFIG(cfig: KpiConfig) {
  return () => cfig.load().catch(msg => console.error(msg));
}



@NgModule({
  declarations: [
    AppComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    AppLayoutModule.forRoot(layoutConfig),
    ToastModule.forRoot(),
    FileUploadModule
  ],
  providers: [
    DatePipe, MessageService, DialogService,
    provideAnimationsAsync(),
    provideHttpClient(withFetch()),
     {provide: APP_INITIALIZER, useFactory: LOAD_CFIG, deps: [KpiConfig], multi: true},
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
