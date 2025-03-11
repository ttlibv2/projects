import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { UserInfoRoutingModule } from './user-info-routing.module';
import { UserInfoComponent } from './user-info.component';
import {ReactiveFormsModule} from "@angular/forms";
// import {CardModule} from "primeng/card";
import {InputTextModule} from "primeng/inputtext";
import {TranslateModule} from "@ngx-translate/core";
import {ButtonModule} from "primeng/button";
import {ApiInfoModule} from "../api-info/api-info.module";
import { DividerModule } from 'primeng/divider';
import {DropdownModule} from "primeng/dropdown";
import {Card} from 'ts-ui/card';


@NgModule({
  declarations: [
    UserInfoComponent
  ],
  imports: [
    CommonModule,
    UserInfoRoutingModule,
    ReactiveFormsModule,
    DividerModule,
    // CardModule,
    Card,
    InputTextModule,
    TranslateModule,
    ButtonModule,
    ApiInfoModule,
    DropdownModule
  ]
})
export class UserInfoModule { }