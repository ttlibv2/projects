import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { CatalogComponent } from './catalog.component';
import { CheckboxModule } from 'primeng/checkbox';
import { ButtonModule } from 'primeng/button';
import { AgTableModule } from '../api/ag-table/ag-table.module';
import { TranslateModule } from '@ngx-translate/core';



@NgModule({
  declarations: [
    CatalogComponent
  ],
  imports: [
    CommonModule,
    CheckboxModule,
    ButtonModule,
    AgTableModule,
    TranslateModule
  ],
  exports: [
    CatalogComponent
  ]
})
export class CatalogModule { }
