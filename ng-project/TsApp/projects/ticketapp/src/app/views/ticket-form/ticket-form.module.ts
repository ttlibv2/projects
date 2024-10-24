import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { TicketFormRoutingModule } from './ticket-form-routing.module';
import { TicketFormComponent } from './ticket-form.component';
import {CardModule} from "primeng/card";
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
import { InputGroupModule } from 'primeng/inputgroup';
import { InputGroupAddonModule } from 'primeng/inputgroupaddon';
import { RippleModule } from 'primeng/ripple';
import { TsIconModule } from 'ts-ui/icon';
import { TsLoggerModule } from 'ts-ui/logger';
import { FormsModule } from 'ts-ui/forms';
import { IconFieldModule } from 'primeng/iconfield';


@NgModule({
  declarations: [
    TicketFormComponent
  ],
  exports: [TicketFormComponent],
    imports: [
    CommonModule,
    FormsModule,
    TicketFormRoutingModule,
    CardModule,
    MultiSelectModule,
    DropdownModule,
    InputTextModule,
    InputTextareaModule,
    IconFieldModule,
    EditorModule,
    DividerModule,
    CheckboxModule,
    ButtonModule,
    SplitButtonModule,
    ChipModule,
    ChipsModule,
    FieldsetModule,
    DialogModule,
    ToolbarModule,
    CatalogModule,
    NgSelectModule,
    TranslateModule,
    InputGroupModule,
    InputGroupAddonModule,
    TsLoggerModule.forChild(),
    RippleModule,
    TagModule,
    TsIconModule
], 
  providers: [
    DialogService,
    DynamicDialogRef
  ]
})
export class TicketFormModule { }