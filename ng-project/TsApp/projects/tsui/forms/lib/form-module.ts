import { CommonModule } from "@angular/common";
import { NgModule } from "@angular/core";
import { FormsModule as NgxFormsModule, ReactiveFormsModule } from "@angular/forms";
import { FormsBuilder } from "./form-builder";

@NgModule({
    imports: [
        CommonModule,
        NgxFormsModule,
        ReactiveFormsModule
    ],
    exports: [
        NgxFormsModule,
        ReactiveFormsModule
    ],
    providers: [
        FormsBuilder
    ]
})
export class FormsModule {

}