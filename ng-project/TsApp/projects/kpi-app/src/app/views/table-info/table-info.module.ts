import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { TableInfoRoutingModule } from './table-info-routing.module';
import { TableInfoComponent } from './table-info.component';


@NgModule({
  declarations: [
    TableInfoComponent
  ],
  imports: [
    CommonModule,
    TableInfoRoutingModule
  ]
})
export class TableInfoModule { }
