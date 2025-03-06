import { CommonModule } from "@angular/common";
import { NgModule } from "@angular/core";
import { FormsModule as NgxFormsModule, ReactiveFormsModule } from "@angular/forms";
import { FormsBuilder } from "./form-builder";
import { FormField } from "../view/form-field";
import { FormLabel } from "../view/form-label";

@NgModule({
    imports: [
        CommonModule,
        NgxFormsModule,
        ReactiveFormsModule,
        FormField,
        FormLabel
    ],
    exports: [
        NgxFormsModule,
        ReactiveFormsModule,
        FormField,
        FormLabel
    ],
    providers: [
        FormsBuilder
    ]
})
export class FormsModule {

}