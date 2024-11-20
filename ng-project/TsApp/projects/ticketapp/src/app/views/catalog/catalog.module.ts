import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { CatalogComponent } from './catalog.component';
import { CheckboxModule } from 'primeng/checkbox';
import { ButtonModule } from 'primeng/button';
import { TranslateModule } from '@ngx-translate/core';
import { CatalogRoutingModule } from './catalog.routing';
import { DynamicDialogRef } from 'primeng/dynamicdialog';
import { CardModule } from 'primeng/card';
import { FieldsetModule } from 'primeng/fieldset';
import { FormsModule } from 'ts-ui/forms';
import { ToolBar } from 'ts-ui/toolbar';

@NgModule({
  declarations: [
    CatalogComponent
  ],
  imports: [
    CommonModule,
    CatalogRoutingModule,
    ToolBar,
    FormsModule,
    CardModule,
    FieldsetModule,
    CheckboxModule,
    ButtonModule,
    TranslateModule
  ],
  exports: [
    CatalogComponent
  ],
  providers: [
    DynamicDialogRef
  ]
})
export class CatalogModule { }