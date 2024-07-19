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
import {PasswordModule} from "primeng/password";
import { InputGroupModule } from 'primeng/inputgroup';
import { InputGroupAddonModule } from 'primeng/inputgroupaddon';
import { DynamicDialogRef } from 'primeng/dynamicdialog';


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
        FormsModule,
        PasswordModule,
        InputGroupModule,
        InputGroupAddonModule
    ],
    providers: [DynamicDialogRef]
})
export class ApiInfoModule { }