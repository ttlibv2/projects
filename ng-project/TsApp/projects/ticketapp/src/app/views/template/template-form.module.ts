import { NgModule } from '@angular/core';
import { CommonModule,  } from '@angular/common';
import { TemplateFormComponent } from './template-form.component';
import {CardModule} from "primeng/card";
import {DropdownModule} from "primeng/dropdown";
import {DialogModule} from "primeng/dialog";
import {DialogService, DynamicDialogRef} from "primeng/dynamicdialog";
import {ButtonModule} from "primeng/button";
import {ChipsModule} from "primeng/chips";
import {ColorPickerModule} from "primeng/colorpicker";
import {DividerModule} from "primeng/divider";
import {InputGroupModule} from "primeng/inputgroup";
import {InputGroupAddonModule} from "primeng/inputgroupaddon";
import {InputIconModule} from "primeng/inputicon";
import {IconFieldModule} from "primeng/iconfield";
import {TranslateModule} from "@ngx-translate/core";
import {ColorPipe} from "../../pipes/color.pipe";
// import {FormsModule, ReactiveFormsModule} from "@angular/forms";
import { InputTextareaModule } from 'primeng/inputtextarea';
import { CheckboxModule } from 'primeng/checkbox';
import { TemplateRoutingModule } from './template-routing.module';
import { AgTableModule } from 'ts-ui/ag-table';
import { ChipModule } from 'primeng/chip';
import { RippleModule } from 'primeng/ripple';
import { AgCellColor } from './renderer';
import { JsonPipe } from '../../pipes/json.pipe';
import { FormsModule } from 'ts-ui/forms';
import { EmailTemplateModule } from '../email-template/email-template.module';
import { EditorModule } from 'primeng/editor';



@NgModule({
  declarations: [
    TemplateFormComponent,
    AgCellColor
  ],
  exports: [
    TemplateFormComponent
  ],
  imports: [
    CommonModule,
    TemplateRoutingModule,
    FormsModule,
    ChipModule,
    AgTableModule,
    CardModule,
    DropdownModule,
    DialogModule,
    ButtonModule,
    ChipsModule,
    ColorPickerModule,
    DividerModule,
    InputGroupModule,
    InputGroupAddonModule,
    InputIconModule,
    IconFieldModule,
    InputTextareaModule,
    TranslateModule,
    ColorPipe,
    CheckboxModule,
    RippleModule,
    EditorModule,
  ],
  providers: [
    DialogService,
    DynamicDialogRef,
    JsonPipe
  ]
})
export class TemplateFormModule { }