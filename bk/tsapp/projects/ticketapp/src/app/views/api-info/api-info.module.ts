import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { ApiInfoRoutingModule } from './api-info-routing.module';
import { ApiInfoComponent } from './api-info.component';
import { MultiSelectModule } from "primeng/multiselect";
import { DropdownModule } from "primeng/dropdown";
import { ChipsModule } from "primeng/chips";
import { ButtonModule } from "primeng/button";
import { TranslateModule } from "@ngx-translate/core";
import { CheckboxModule } from "primeng/checkbox";
import { PasswordModule } from "primeng/password";
import { InputGroupModule } from 'primeng/inputgroup';
import { InputGroupAddonModule } from 'primeng/inputgroupaddon';
import { DynamicDialogRef } from 'primeng/dynamicdialog';
import { Divider } from 'ts-ui/divider';
import { FormField, FormsModule } from 'ts-ui/forms';
import { InputTextareaModule } from 'primeng/inputtextarea';
import { Card, CardModule } from 'ts-ui/card';
import { TooltipModule } from 'primeng/tooltip';

@NgModule({
    declarations: [
        ApiInfoComponent
    ],
    exports: [
        ApiInfoComponent
    ],
    imports: [
        CommonModule,
        FormsModule,
        ApiInfoRoutingModule,
        CardModule,
        FormField,
        MultiSelectModule,
        DropdownModule,
        ChipsModule,
        ButtonModule,
        TranslateModule,
        CheckboxModule,
        PasswordModule,
        InputGroupModule,
        InputTextareaModule,
        InputGroupAddonModule,
        TooltipModule,
        Divider
    ],
    providers: [DynamicDialogRef]
})
export class ApiInfoModule { }