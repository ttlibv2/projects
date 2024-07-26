import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { TableInfoRoutingModule } from './table-info-routing.module';
import { TableInfoComponent } from './table-info.component';
import { AgTableModule } from 'ts-ui/ag-table';


@NgModule({
  declarations: [
    TableInfoComponent
  ],
  imports: [
    CommonModule,
    TableInfoRoutingModule,
    AgTableModule
  ]
})
export class TableInfoModule { }
