import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { ApiInfoRoutingModule } from './api-info-routing.module';
import { ApiInfoComponent } from './api-info.component';
import {CardModule} from "primeng/card";
import {FormsModule, ReactiveFormsModule} from "@angular/forms";
import {MultiSelectModule} from "primeng/multiselect";
import {DropdownModule} from "primeng/dropdown";
import {ChipsModule} from "primeng/chips";
import {ButtonModule} from "primeng/button";
import {TranslateModule} from "@ngx-translate/core";
import {CheckboxModule} from "primeng/checkbox";


@NgModule({
    declarations: [
        ApiInfoComponent
    ],
    exports: [
        ApiInfoComponent
    ],
  imports: [
    CommonModule,
    ApiInfoRoutingModule,
    CardModule,
    ReactiveFormsModule,
    MultiSelectModule,
    DropdownModule,
    ChipsModule,
    ButtonModule,
    TranslateModule,
    CheckboxModule,
    FormsModule
  ]
})
export class ApiInfoModule { }
