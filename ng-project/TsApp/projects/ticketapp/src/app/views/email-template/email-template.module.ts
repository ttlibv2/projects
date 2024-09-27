import { CommonModule } from "@angular/common";
import { NgModule } from "@angular/core";
import { EditorModule } from "primeng/editor";
import { EmailTemplateView } from "./email-template.view";
import { ButtonModule } from "primeng/button";
import { FormsModule } from "ts-ui/forms";
import { InputTextModule } from "primeng/inputtext";
import { CheckboxModule } from "primeng/checkbox";
import { DropdownModule } from "primeng/dropdown";
import { CardModule } from "primeng/card";
import { FieldsetModule } from "primeng/fieldset";
import { DividerModule } from "primeng/divider";
import { AgTableModule } from "ts-ui/ag-table";

@NgModule({
    declarations: [EmailTemplateView],
    exports: [EmailTemplateView],
    imports: [
        CommonModule, 
        FormsModule,
        AgTableModule,
        EditorModule, 
        ButtonModule, 
        InputTextModule,
        CheckboxModule,
        DropdownModule,
        CardModule,
        FieldsetModule,
        DividerModule
        
    ]
})
export class EmailTemplateModule {}