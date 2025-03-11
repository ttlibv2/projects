import { NgModule } from '@angular/core';
import { CommonModule,  } from '@angular/common';
import { TemplateFormComponent } from './template-form.component';
import {DropdownModule} from "primeng/dropdown";
import {DialogModule} from "primeng/dialog";
import {DialogService, DynamicDialogRef} from "primeng/dynamicdialog";
import {ButtonModule} from "primeng/button";
import {ChipsModule} from "primeng/chips";
import {ColorPickerModule} from "primeng/colorpicker";
import {Divider} from "ts-ui/divider";
import {InputGroupModule} from "ts-ui/inputgroup";
import {InputIconModule} from "primeng/inputicon";
import {IconFieldModule} from "primeng/iconfield";
import {TranslateModule} from "@ngx-translate/core";
import {ColorPipe} from "../../pipes/color.pipe";
import { InputTextareaModule } from 'primeng/inputtextarea';
import { CheckboxModule } from 'primeng/checkbox';
import { TemplateRouting } from './template-routing.module';
import { AgTableModule } from 'ts-ui/agtable';
import { ChipModule } from 'primeng/chip';
import { RippleModule } from 'primeng/ripple';
import { AgCellColor } from './renderer';
import { JsonPipe } from '../../pipes/json.pipe';
import { FormsModule } from 'ts-ui/forms';
import { EditorModule } from 'primeng/editor';
import { InputColor } from 'ts-ui/colorpicker';
import { Card } from 'ts-ui/card';

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
    FormsModule,
    ChipModule,
    AgTableModule,
    Card,
    DropdownModule,
    DialogModule,
    ButtonModule,
    ChipsModule,
    ColorPickerModule,
    Divider,
    InputGroupModule,
    InputIconModule,
    IconFieldModule,
    InputTextareaModule,
    TranslateModule,
    ColorPipe,
    InputColor,
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