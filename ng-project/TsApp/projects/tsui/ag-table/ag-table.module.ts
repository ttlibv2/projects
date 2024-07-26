import { ModuleWithProviders, NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { AgTableComponent } from './ag-table.component';
import { AgGridModule } from '@ag-grid-community/angular';
import { AG_CONFIG_TOKEN, AgTableConfig, defaultConfig } from './ag-table.config';
import { Objects } from 'ts-ui/helper';


@NgModule({
  declarations: [ 
    AgTableComponent
  ],
  imports: [ 
    CommonModule,
    AgGridModule,
  ],
  exports: [
    AgTableComponent, 
    AgGridModule
  ]
})
export class AgTableModule {

  static forRoot(config?: AgTableConfig): ModuleWithProviders<AgTableModule> {
    const newConfig = Objects.mergeDeep(defaultConfig, config);

    return {
      ngModule: AgTableModule,
      providers: [
        {provide: AG_CONFIG_TOKEN, useValue: newConfig}
      ]
    };
  }
 }
