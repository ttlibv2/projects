import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { SignupRoutingModule } from './signup-routing.module';
import { SignupComponent } from './signup.component';
import { CheckboxModule } from "primeng/checkbox";
import { InputTextModule } from "primeng/inputtext";
import { ReactiveFormsModule } from "@angular/forms";
import { CardModule } from "primeng/card";
import { ButtonModule } from "primeng/button";
import { IconFieldModule } from "primeng/iconfield";
import { InputIconModule } from "primeng/inputicon";
import { DividerModule } from "primeng/divider";
import { EditorModule } from "primeng/editor";
import { InputTextareaModule } from "primeng/inputtextarea";
import { CalendarModule } from "primeng/calendar";
import { TranslateModule } from "@ngx-translate/core";
import { BlockUIModule } from "primeng/blockui";
import { PasswordModule } from "primeng/password";
import { DropdownModule } from "primeng/dropdown";
import { InputGroupModule } from 'ts-ui/input-group';


@NgModule({
  declarations: [
    SignupComponent
  ],
  imports: [
    CommonModule,
    SignupRoutingModule,
    CheckboxModule,
    InputTextModule,
    ReactiveFormsModule,
    CardModule,
    ButtonModule,
    InputGroupModule,
    IconFieldModule,
    InputIconModule,
    DividerModule,
    EditorModule,
    InputTextareaModule,
    CalendarModule,
    TranslateModule,
    BlockUIModule,
    PasswordModule,
    DropdownModule
  ]
})
export class SignupModule { }
