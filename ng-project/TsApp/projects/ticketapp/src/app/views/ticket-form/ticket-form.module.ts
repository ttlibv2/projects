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
import {SplitButtonModule} from "ts-ui/split-button";
import {ChipModule} from "primeng/chip";
import {ChipsModule} from "primeng/chips";
import {FieldsetModule} from "primeng/fieldset";
import {DialogModule} from "primeng/dialog";
import {DialogService, DynamicDialogRef} from "primeng/dynamicdialog";
import {ToolbarModule} from "primeng/toolbar";
import { CatalogModule } from '../catalog/catalog.module';
import { NgSelectModule } from '@ng-select/ng-select';
import {TranslateModule} from "@ngx-translate/core";
import { TagModule } from 'ts-ui/tag';


@NgModule({
  declarations: [
    TicketFormComponent
  ],
  exports: [TicketFormComponent],
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
        ToolbarModule,
        CatalogModule,
        NgSelectModule,
        TranslateModule,
        TagModule
      
    ],
  providers: [
    DialogService,
    DynamicDialogRef
  ]
})
export class TicketFormModule { }