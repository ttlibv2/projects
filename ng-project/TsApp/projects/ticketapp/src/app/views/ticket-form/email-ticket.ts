import { Component, Input, OnInit } from "@angular/core";
import { ButtonModule } from "primeng/button";
import { DropdownModule } from "primeng/dropdown";
import { DynamicDialogRef } from "primeng/dynamicdialog";
import { EditorModule } from "primeng/editor";
import { Template } from "../../models/template";
import { ModalService } from "../../services/ui/model.service";
import { Forms } from "ts-ui/helper";
import { FormBuilder, ReactiveFormsModule, Validators } from "@angular/forms";

@Component({
    standalone: true,
    selector: '[ts-email-ticket]',
    imports: [ReactiveFormsModule, EditorModule, ButtonModule, DropdownModule],
    template: `
    <form [formGroup]="forms.formGroup" (ngSubmit)="save()">
        <div class="grid">
            <div class="p-fluid col-12">
                <label>Danh sách E-mail mẫu</label>
                <p-dropdown class="flex-1" [options]="list" optionLabel="title" [filter]="true" dataKey="id" [showClear]="true"></p-dropdown>
            </div>
            <div class="p-fluid col-12">
                <label>Nội dung E-mail</label>
                <p-editor id="content_email" [style]="{height: '350px'}" formControlName="html"> 
                </p-editor>
            </div>
            <div class="p-fluid col-12 flex gap-2"> 
                <div class="left flex flex-1 justify-content-start gap-2">
                <p-button label="Lưu lại" severity="primary" type="submit"></p-button>
                <p-button label="Xem trước" severity="help" (onClick)="preview()"></p-button>
                </div>               
                <div class="right flex justify-content-end">
                <p-button label="Đóng"severity="danger" (onClick)="closeDialog()"></p-button>
                </div>               
               
        </div>
        
        </div>
</form>
         
    
    `
})
export class EmailTicketView implements OnInit {
    @Input() list: Template[] = [];
    @Input() select: Template;
    @Input() html: string;

    readonly forms: Forms<any>;

    constructor(
        private fb: FormBuilder,
        private dialogRef: DynamicDialogRef,
        private modal: ModalService) {
        this.forms = Forms.builder(fb, {
            select: [null, Validators.required],
            html: [null, Validators.required]
        });
    }

    ngOnInit(): void {

        const instanceRef = this.modal.getInstance(this.dialogRef);
        if (instanceRef && instanceRef.data) {
            const { list, select, html } = instanceRef.data;
            this.list = list ?? [];
            this.select = select ?? this.list[0];
            this.html = html || this.select?.data.html;
            this.forms.pathValue({select, html: {html: this.html}});
        }
    }

    save(): void { 
        const {select, html} = this.forms.formRawValue;
        this.dialogRef.close({select, html: html.html});
    }

    preview(): void { }

    closeDialog(): void {
        this.dialogRef.close();
    }
}