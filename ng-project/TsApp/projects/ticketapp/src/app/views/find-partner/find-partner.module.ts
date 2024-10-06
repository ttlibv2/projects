import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { FindPartnerRoutingModule } from './find-partner-routing.module';
import { FindPartnerComponent } from './find-partner.component';
import {FormsModule, ReactiveFormsModule} from "@angular/forms";
import {ChipsModule} from "primeng/chips";
import {InputGroupModule} from "primeng/inputgroup";
import {CheckboxModule} from "primeng/checkbox";
import {RadioButtonModule} from "primeng/radiobutton";
import {ButtonModule} from "primeng/button";
import {CardModule} from "primeng/card";
import {FieldsetModule} from "primeng/fieldset";
import {DialogModule} from "primeng/dialog";
import {DialogService, DynamicDialogRef} from "primeng/dynamicdialog";
import { ToolbarModule } from 'primeng/toolbar';
import { AgTableModule } from 'ts-ui/ag-table';
import { InputNumberModule } from 'primeng/inputnumber';
import { DividerModule } from 'primeng/divider';
import {InputGroupAddonModule} from "primeng/inputgroupaddon";


@NgModule({
  declarations: [
    FindPartnerComponent
  ],
  imports: [
    CommonModule,
    FindPartnerRoutingModule,
    FormsModule,
    ReactiveFormsModule,
    DialogModule,
    ChipsModule,
    InputGroupModule,
    CheckboxModule,
    RadioButtonModule,
    ButtonModule,
    CardModule,
    FieldsetModule,
    AgTableModule,
    ToolbarModule,
    AgTableModule,
    InputNumberModule,
    DividerModule,
    InputGroupAddonModule
  ],
  providers: [
    DialogService,
    DynamicDialogRef
  ]
})
export class FindPartnerModule { }
