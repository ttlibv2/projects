import { NgModule } from '@angular/core';
import { CommonModule, NgIf } from '@angular/common';

import { FindPartnerRoutingModule } from './find-partner-routing.module';
import { FindPartnerComponent } from './find-partner.component';
import { ChipsModule } from "primeng/chips";
import { CheckboxModule } from "primeng/checkbox";
import { RadioButtonModule } from "primeng/radiobutton";
import { ButtonModule } from "primeng/button";
import { CardModule } from "primeng/card";
import { FieldsetModule } from "primeng/fieldset";
import { DialogModule } from "primeng/dialog";
import { DialogService, DynamicDialogRef } from "primeng/dynamicdialog";
import { ToolbarModule } from 'primeng/toolbar';
import { AgTableModule } from 'ts-ui/ag-table';
import { InputNumberModule } from 'primeng/inputnumber';
import { DividerModule } from 'primeng/divider';
import { InputGroupAddonModule } from "primeng/inputgroupaddon";
import { TranslateModule } from '@ngx-translate/core';
import { FormsModule } from 'ts-ui/forms';
import { SplitButtonModule } from 'ts-ui/split-button';
import { InputGroupModule } from 'ts-ui/input-group';
import { IconModule } from 'ts-ui/icon';


@NgModule({
  declarations: [
    FindPartnerComponent
  ],
  imports: [
    CommonModule,
    FindPartnerRoutingModule,
    FormsModule,
    DialogModule,
    ChipsModule,
    IconModule,
    InputGroupModule,
    CheckboxModule,
    RadioButtonModule,
    ButtonModule,
    SplitButtonModule,
    CardModule,
    FieldsetModule,
    AgTableModule,
    ToolbarModule,
    AgTableModule,
    InputNumberModule,
    DividerModule,
    InputGroupAddonModule,
    TranslateModule,
    NgIf
  ],
  providers: [
    DialogService,
    DynamicDialogRef
  ]
})
export class FindPartnerModule { }
