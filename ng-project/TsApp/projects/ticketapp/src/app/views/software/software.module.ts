import {NgModule} from '@angular/core';
import {SoftwareRoutingModule} from './software-routing.module';
import {SoftwareComponent} from './software.component';
import {MvcModule} from "../shared/mvc/mvc.module";
import {PrimeTemplate} from "primeng/api";
import {InputTextModule} from "primeng/inputtext";
import {CommonModule} from "@angular/common";
import {ReactiveFormsModule} from "@angular/forms";


@NgModule({
    declarations: [
        SoftwareComponent
    ],
    imports: [
        CommonModule,
        ReactiveFormsModule,
        SoftwareRoutingModule,
        MvcModule,
        PrimeTemplate,
        InputTextModule

    ]
})
export class SoftwareModule {
}
