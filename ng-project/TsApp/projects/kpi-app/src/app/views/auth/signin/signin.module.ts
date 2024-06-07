import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { SigninComponent } from './signin.component';
import {FormsModule, ReactiveFormsModule} from "@angular/forms";
import {InputTextModule} from "primeng/inputtext";
import {InputGroupModule} from "primeng/inputgroup";
import {InputGroupAddonModule} from "primeng/inputgroupaddon";
import {SigninRoutingModule} from "./signin-routing.module";
import {AutoCompleteModule} from "primeng/autocomplete";
import {CheckboxModule} from "primeng/checkbox";
import {ButtonModule} from "primeng/button";
import {CardModule} from "primeng/card";
import {MessagesModule} from "primeng/messages";
import {MessageService} from "primeng/api";


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
        InputGroupAddonModule,
        InputTextModule,
        AutoCompleteModule,
        CheckboxModule,
        ButtonModule,
        CardModule
    ],
  providers: [
    MessageService
  ]
})
export class SigninModule { }
