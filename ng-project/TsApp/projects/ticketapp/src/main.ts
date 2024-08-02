import { ViewEncapsulation } from '@angular/core';
import { platformBrowserDynamic } from '@angular/platform-browser-dynamic';
import { AppModule } from './app/app.module';
import { GridCoreModule, ModuleRegistry } from '@ag-grid-community/core';
import { InfiniteRowModelModule} from '@ag-grid-community/infinite-row-model';
import { ClientSideRowModelModule } from '@ag-grid-community/client-side-row-model';
import { LicenseManager } from "@ag-grid-enterprise/core";
import { MenuModule } from "@ag-grid-enterprise/menu";
import { ExcelExportModule } from "@ag-grid-enterprise/excel-export";
import { RowGroupingModule } from '@ag-grid-enterprise/row-grouping';
import { RangeSelectionModule } from '@ag-grid-enterprise/range-selection';
import { SideBarModule } from '@ag-grid-enterprise/side-bar';
import { ColumnsToolPanelModule } from '@ag-grid-enterprise/column-tool-panel';
import {ClipboardModule} from "@ag-grid-enterprise/clipboard";
import {GridChartsModule} from "@ag-grid-enterprise/charts";
import {SparklinesModule} from "@ag-grid-enterprise/sparklines";
import {FiltersToolPanelModule} from "@ag-grid-enterprise/filter-tool-panel";
import {MasterDetailModule} from "@ag-grid-enterprise/master-detail";
import {RichSelectModule} from "@ag-grid-enterprise/rich-select"; 
import {ServerSideRowModelModule} from "@ag-grid-enterprise/server-side-row-model";
import {SetFilterModule} from "@ag-grid-enterprise/set-filter";
import {MultiFilterModule} from "@ag-grid-enterprise/multi-filter";
import {AdvancedFilterModule} from "@ag-grid-enterprise/advanced-filter";
import {StatusBarModule} from "@ag-grid-enterprise/status-bar";
import {ViewportRowModelModule} from "@ag-grid-enterprise/viewport-row-model";

LicenseManager.setLicenseKey('_MjU1NTk0NjAwMDAwMA==3d4e805d480235845d58b32b2dae76d0');
ModuleRegistry.registerModules([
  GridCoreModule,
  ClientSideRowModelModule,
  MenuModule,
  ExcelExportModule,
  RowGroupingModule,
  RangeSelectionModule,
  SideBarModule,
  ColumnsToolPanelModule,
  ClipboardModule,
  //GridChartsModule,
  //SparklinesModule,
  FiltersToolPanelModule,
  //MasterDetailModule,
  //RichSelectModule,
  //ServerSideRowModelModule,
  SetFilterModule,
  MultiFilterModule,
  AdvancedFilterModule,
  //StatusBarModule,
  //ViewportRowModelModule,
  //InfiniteRowModelModule


]);

platformBrowserDynamic().bootstrapModule(AppModule, {
  ngZoneEventCoalescing: true,
  defaultEncapsulation: ViewEncapsulation.None
})
  .catch(err => console.error(err));

