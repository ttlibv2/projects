import { CommonModule } from "@angular/common";
import { NgModule } from "@angular/core";
import { FormsModule, ReactiveFormsModule } from "@angular/forms";
import { SelectFileView } from "./select-file.view";
import { FileUploadModule } from "primeng/fileupload";
import { ButtonModule } from "primeng/button";
import { MultiSelectModule } from "primeng/multiselect";
import { DropdownModule } from "primeng/dropdown";
import { InputNumberModule } from "primeng/inputnumber";
import { InputTextModule } from "primeng/inputtext";
import { ToastModule } from "ts-ui/toast";

@NgModule({
    declarations: [SelectFileView],
    exports: [SelectFileView],
    imports: [ 
      CommonModule,
      FormsModule,
      ReactiveFormsModule,
      FileUploadModule,
      ToastModule.forRoot(),
      ButtonModule,
      DropdownModule,
      InputNumberModule,
      InputTextModule
  
    ]
  })
  export class SelectFileModule { }