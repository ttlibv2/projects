import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { AgTableComponent } from './ag-table.component';
import { AgGridModule } from 'ag-grid-angular';



@NgModule({
  declarations: [
    AgTableComponent
  ],
  imports: [
    CommonModule,
    AgGridModule
  ],
  exports: [AgTableComponent]
})
export class AgTableModule { }
