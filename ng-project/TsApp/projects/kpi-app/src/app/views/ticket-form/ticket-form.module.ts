import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { TicketFormRoutingModule } from './ticket-form-routing.module';
import { TicketFormComponent } from './ticket-form.component';
import {CardModule} from "primeng/card";
import {FormsModule, ReactiveFormsModule} from "@angular/forms";
import {MultiSelectModule} from "primeng/multiselect";
import {DropdownModule} from "primeng/dropdown";
import {InputTextModule} from "primeng/inputtext";
import {InputTextareaModule} from "primeng/inputtextarea";
import {EditorModule} from "primeng/editor";
import {DividerModule} from "primeng/divider";
import {CheckboxModule} from "primeng/checkbox";
import {ButtonModule} from "primeng/button";
import {SplitButtonModule} from "primeng/splitbutton";
import {ChipModule} from "primeng/chip";
import {ChipsModule} from "primeng/chips";
import {FieldsetModule} from "primeng/fieldset";
import {TemplateFormModule} from "../template-form/template-form.module";
import {DialogModule} from "primeng/dialog";
import {DialogService} from "primeng/dynamicdialog";


@NgModule({
  declarations: [
    TicketFormComponent
  ],
  imports: [
    CommonModule,
    TicketFormRoutingModule,
    CardModule,
    ReactiveFormsModule,
    MultiSelectModule,
    DropdownModule,
    InputTextModule,
    InputTextareaModule,
    EditorModule,
    DividerModule,
    CheckboxModule,
    ButtonModule,
    SplitButtonModule,
    ChipModule,
    ChipsModule,
    FieldsetModule,
    FormsModule,
    DialogModule,
    TemplateFormModule
  ],
  providers: [
    DialogService
  ]
})
export class TicketFormModule { }
