import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { CatalogComponent } from './catalog.component';
import { CheckboxModule } from 'primeng/checkbox';
import { ButtonModule } from 'primeng/button';
import { AgTableModule } from 'ts-ui/ag-table';
import { TranslateModule } from '@ngx-translate/core';
import { ToastModule } from 'ts-ui/toast';

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