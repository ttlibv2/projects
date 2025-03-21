import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { TicketFormRouting } from './ticket-form.routing';
import { TicketFormComponent } from './ticket-form.component';
import { MultiSelectModule } from "primeng/multiselect";
import { DropdownModule } from "primeng/dropdown";
import { InputTextModule } from "primeng/inputtext";
import { InputTextareaModule } from "primeng/inputtextarea";
import { EditorModule } from "primeng/editor";
import { DividerModule } from "primeng/divider";
import { CheckboxModule } from "primeng/checkbox";
import { ButtonModule } from "primeng/button";
import { SplitButtonModule } from "ts-ui/splitbutton";
import { ChipModule } from "primeng/chip";
import { ChipsModule } from "primeng/chips";
import { FieldsetModule } from "primeng/fieldset";
import { DialogModule } from "primeng/dialog";
import { DialogService, DynamicDialogRef } from "primeng/dynamicdialog";
import { ToolbarModule } from "primeng/toolbar";
import { CatalogModule } from '../catalog/catalog.module';
import { NgSelectModule } from '@ng-select/ng-select';
import { TranslateModule } from "@ngx-translate/core";
import { TagModule } from 'ts-ui/tag';
import { InputGroupModule } from 'ts-ui/inputgroup';
import { InputGroupAddonModule } from 'primeng/inputgroupaddon';
import { RippleModule } from 'primeng/ripple';
import { TsLoggerModule } from 'ts-ui/logger';
import { FormsModule, FormField, FormLabel } from 'ts-ui/forms';
import { IconFieldModule } from 'primeng/iconfield';
import { ToolBar } from 'ts-ui/toolbar';
import { ColDirective, GridDirective } from 'ts-ui/common';
import { CardModule } from 'ts-ui/card';


@NgModule({
  declarations: [
    TicketFormComponent
  ],
  exports: [TicketFormComponent],
  imports: [
    CommonModule,
    FormsModule,
    //TicketFormRouting,
    CardModule,
    MultiSelectModule,
    ToolBar,
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
    FormField,
    FormLabel,
    ColDirective,
    GridDirective,
    
],
  providers: [
    DialogService,
    DynamicDialogRef
  ]
})
export class TicketFormModule { }