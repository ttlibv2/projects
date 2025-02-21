import {ApplicationConfig, provideZoneChangeDetection, importProvidersFrom} from '@angular/core';
import {provideAnimationsAsync} from '@angular/platform-browser/animations/async';
import {provideRouter, withComponentInputBinding, withInMemoryScrolling} from '@angular/router';
import {routes} from './app.routes';
import {provideHttpClient, withFetch} from '@angular/common/http';
import {providePrimeNG} from "primeng/config";
import Aura from "@primeng/themes/aura";

export const appConfig: ApplicationConfig = {
  providers: [
    provideAnimationsAsync('animations'),
    provideHttpClient(withFetch()),
    provideZoneChangeDetection({eventCoalescing: true}),
    provideRouter(routes,
      withComponentInputBinding(),
      withInMemoryScrolling({
        anchorScrolling: 'enabled',
        scrollPositionRestoration: 'enabled'
      })
    ),
    providePrimeNG({
      ripple: true,
      theme: {preset: Aura}
    })
    
  ]
};