import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {ReactiveFormsModule} from "@angular/forms";
import {FormLabel} from "./form-label";
import {FormField} from "./form-field";
import {FormsBuilder} from "./form-builder";

@NgModule({
    declarations: [FormLabel, FormField],
    providers: [FormsBuilder],
    imports: [CommonModule, ReactiveFormsModule],
    exports: [ReactiveFormsModule, FormLabel, FormField]
})
export class FormsModule {
}