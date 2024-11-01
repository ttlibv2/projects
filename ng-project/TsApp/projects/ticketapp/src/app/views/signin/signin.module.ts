import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { SigninComponent } from './signin.component';
import { FormsModule, ReactiveFormsModule } from "@angular/forms";
import { SigninRoutingModule } from "./signin-routing.module";
import { AutoCompleteModule } from "primeng/autocomplete";
import { CheckboxModule } from "primeng/checkbox";
import { ButtonModule } from "primeng/button";
import { CardModule } from "primeng/card";
import { MessageService } from "primeng/api";
import { PasswordModule } from "primeng/password";
import { InputGroupModule } from 'ts-ui/input-group';


@NgModule({
  declarations: [
    SigninComponent
  ],
  imports: [
    CommonModule,
    FormsModule,
    ReactiveFormsModule,
    SigninRoutingModule,
    InputGroupModule,
    AutoCompleteModule,
    CheckboxModule,
    ButtonModule,
    CardModule,
    PasswordModule
  ],
  providers: [
    MessageService
  ]
})
export class SigninModule { }
