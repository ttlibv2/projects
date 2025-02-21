import { bootstrapApplication } from '@angular/platform-browser';
import { ModuleRegistry } from 'ag-grid-community'; 
import { appConfig } from './app/app.config';
import { AppComponent } from './app/app.component';
import { AllEnterpriseModule, LicenseManager } from "ag-grid-enterprise";

LicenseManager.setLicenseKey('_MjU1NTk0NjAwMDAwMA==3d4e805d480235845d58b32b2dae76d0');
ModuleRegistry.registerModules([AllEnterpriseModule]);

bootstrapApplication(AppComponent, appConfig)
  .catch((err) => console.error(err));
