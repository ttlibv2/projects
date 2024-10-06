import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';

import {MvcComponent} from './mvc.component';
import {AgTableModule} from "ts-ui/ag-table";
import {FormsModule} from "ts-ui/forms";
import {ButtonModule} from "primeng/button";
import {InputTextModule} from "primeng/inputtext";
import {CheckboxModule} from "primeng/checkbox";
import {DividerModule} from "primeng/divider";
import {CardModule} from "primeng/card";


@NgModule({
    declarations: [
        MvcComponent
    ],
    exports: [
        MvcComponent
    ],
    imports: [
        CommonModule,
        AgTableModule,
        FormsModule,
        ButtonModule,
        InputTextModule,
        CheckboxModule,
        DividerModule,
        CardModule
    ]
})
export class MvcModule {
}
