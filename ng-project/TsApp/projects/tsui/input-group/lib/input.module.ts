import { NgModule } from "@angular/core";
import { InputGroup } from "./input-group";
import { CommonModule } from "@angular/common";
import { PrimeTemplate } from "primeng/api";
import { ButtonModule } from "primeng/button";
import { InputTextModule } from "primeng/inputtext";

@NgModule({
    declarations: [
        InputGroup,
    ],
    imports: [
        CommonModule,
        PrimeTemplate,
        ButtonModule,
        InputTextModule,
    ],
    exports: [
        InputGroup,
        PrimeTemplate,
        InputTextModule
    ]
})
export class InputGroupModule { }