import { NgModule } from '@angular/core';
import { CommonModule, NgIf } from '@angular/common';

import { PartnerRouting } from './find-partner-routing.module';
import { FindPartnerComponent } from './find-partner.component';
import { ChipsModule } from "primeng/chips";
import { CheckboxModule } from "primeng/checkbox";
import { RadioButtonModule } from "primeng/radiobutton";
import { ButtonModule } from "primeng/button";
import { CardModule } from "ts-ui/card";
import { FieldsetModule } from "primeng/fieldset";
import { DialogModule } from "primeng/dialog";
import { DialogService, DynamicDialogRef } from "primeng/dynamicdialog";
import { ToolbarModule } from 'primeng/toolbar';
import { AgTableModule } from 'ts-ui/agtable';
import { InputNumberModule } from 'primeng/inputnumber';
import { DividerModule } from 'primeng/divider';
import { InputGroupAddonModule } from "primeng/inputgroupaddon";
import { TranslateModule } from '@ngx-translate/core';
import { FormField, FormLabel, FormsModule } from 'ts-ui/forms';
import { SplitButtonModule } from 'ts-ui/splitbutton';
import { InputGroupModule } from 'ts-ui/inputgroup';
import { IconModule } from 'ts-ui/icon';
import { SharedModule } from 'primeng/api';
import { Divider } from 'ts-ui/divider';


@NgModule({
  declarations: [
    FindPartnerComponent
  ],
  imports: [
    CommonModule,
    PartnerRouting,
    SharedModule,
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
    FormLabel,
    FormField,
    Divider,
    NgIf
  ],
  providers: [
    DialogService,
    DynamicDialogRef
  ]
})
export class FindPartnerModule { }
