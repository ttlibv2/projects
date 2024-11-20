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
import { DynamicDialogRef } from "primeng/dynamicdialog";
import { ToolBar } from "ts-ui/toolbar";

@NgModule({
    declarations: [EmailTemplateView],
    exports: [EmailTemplateView],
    providers: [DynamicDialogRef],
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
        DividerModule,
        ToolBar
        
    ]
})
export class EmailTemplateModule {}