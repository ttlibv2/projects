import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterOutlet, RouterModule } from '@angular/router';

import { SignupRoutingModule } from './signup-routing.module';
import { SignupComponent } from './signup.component';
import { CheckboxModule } from "primeng/checkbox";
import { InputTextModule } from "primeng/inputtext";
import { ReactiveFormsModule } from "@angular/forms";
import { CardModule } from "ts-ui/card";
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
import { InputGroupModule } from 'ts-ui/inputgroup';
import { FormField } from 'ts-ui/forms';
import { Divider } from 'ts-ui/divider';
import { SocialItem } from '../shared/social-item';
import { ColDirective } from 'ts-ui/common';
import { ImageModule } from 'primeng/image';

@NgModule({
  declarations: [
    SignupComponent
  ],
  imports: [
    CommonModule,
    RouterModule,
    TranslateModule,
    SignupRoutingModule,
    CheckboxModule,
    InputTextModule,
    ReactiveFormsModule,
    CardModule,
    FormField,
    Divider,
    ButtonModule,
    InputGroupModule,
    IconFieldModule,
    InputIconModule,
    EditorModule,
    InputTextareaModule,
    CalendarModule,
    TranslateModule,
    BlockUIModule,
    PasswordModule,
    DropdownModule,
    ImageModule,
    SocialItem,
    ColDirective
  ]
})
export class SignupModule { }
