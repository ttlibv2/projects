import {NgModule} from '@angular/core';
import {SoftwareRoutingModule} from './software-routing.module';
import {SoftwareComponent} from './software.component';
import {MvcModule} from "../shared/mvc/mvc.module";
import {PrimeTemplate} from "primeng/api";
import {InputTextModule} from "primeng/inputtext";


@NgModule({
    declarations: [
        SoftwareComponent
    ],
    imports: [
        SoftwareRoutingModule,
        MvcModule,
        PrimeTemplate,
        InputTextModule

    ]
})
export class SoftwareModule {
}
