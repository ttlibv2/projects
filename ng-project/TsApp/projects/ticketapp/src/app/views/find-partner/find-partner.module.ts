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
import {TableModule} from "primeng/table";
import {DialogModule} from "primeng/dialog";
import {DialogService, DynamicDialogRef} from "primeng/dynamicdialog";
import {AgGridAngular} from "ag-grid-angular";


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
    TableModule,
    AgGridAngular
  ],
  providers: [
    DialogService,
    DynamicDialogRef
  ]
})
export class FindPartnerModule { }
