import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';

import {MvcComponent} from './mvc.component';
import {AgTableModule} from "ts-ui/ag-table";
import {FormsBuilder, FormsModule} from "ts-ui/forms";
import {ButtonModule} from "primeng/button";
import {InputTextModule} from "primeng/inputtext";
import {CheckboxModule} from "primeng/checkbox";
import {DividerModule} from "primeng/divider";
import {CardModule} from "primeng/card";
import {ReactiveFormsModule} from "@angular/forms";
import {TranslateModule} from "@ngx-translate/core";
import {InputTextareaModule} from "primeng/inputtextarea";


@NgModule({
    declarations: [
        MvcComponent
    ],
    exports: [
        MvcComponent,
        // FormsModule,
        // ReactiveFormsModule,
    ],
    imports: [
        CommonModule,
        AgTableModule,
        FormsModule,
        ButtonModule,
        InputTextModule,
        CheckboxModule,
        DividerModule,
        CardModule,
        TranslateModule,
        InputTextareaModule
    ]
})
export class MvcModule {
}
