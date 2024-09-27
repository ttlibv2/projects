import { Component, OnInit } from "@angular/core";
import { DynamicDialogRef } from "primeng/dynamicdialog";
import { ModalService } from "../../services/ui/model.service";

@Component({
    standalone: true,
    selector: '[ts-email-ticket]',
    template: `<div [innerHTML]="html"></div>`
})
export class ViewHtml implements OnInit {
    html: string;

    constructor(
        private dialogRef: DynamicDialogRef,
        private modal: ModalService) {
    }

    ngOnInit(): void {

        const instanceData: any = this.modal.getData(this.dialogRef);
        if (instanceData && instanceData.html) {
            this.html = instanceData.html;
        }
    }

    closeDialog(): void {
        this.dialogRef.close();
    }
}