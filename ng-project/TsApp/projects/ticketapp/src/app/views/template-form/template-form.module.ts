import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { TemplateFormComponent } from './template-form.component';
import {CardModule} from "primeng/card";
import {DropdownModule} from "primeng/dropdown";
import {DialogModule} from "primeng/dialog";
import {DialogService} from "primeng/dynamicdialog";
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
import {ReactiveFormsModule} from "@angular/forms";



@NgModule({
  declarations: [
    TemplateFormComponent
  ],
  imports: [
    CommonModule,
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
    TranslateModule,
    ColorPipe,
    ReactiveFormsModule
  ],
  providers: [
    DialogService
  ]
})
export class TemplateFormModule { }
