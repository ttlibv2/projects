import { ViewEncapsulation } from '@angular/core';
import { platformBrowserDynamic } from '@angular/platform-browser-dynamic';
import {LicenseManager} from 'ag-grid-enterprise';

import { AppModule } from './app/app.module';


LicenseManager.setLicenseKey('_MjU1NTk0NjAwMDAwMA==3d4e805d480235845d58b32b2dae76d0');

platformBrowserDynamic().bootstrapModule(AppModule, {
  ngZoneEventCoalescing: true,
  defaultEncapsulation: ViewEncapsulation.None
})
  .catch(err => console.error(err));
