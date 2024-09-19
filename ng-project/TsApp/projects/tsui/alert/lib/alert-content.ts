import { CommonModule } from "@angular/common";
import { Component, OnInit, ViewEncapsulation } from "@angular/core";
import { ButtonModule } from "primeng/button";
import { DialogService, DynamicDialogModule, DynamicDialogRef } from "primeng/dynamicdialog";
import { RippleModule } from "primeng/ripple";
import { AlertOption } from "./alert.interface";

@Component({
    standalone: true,
    selector: '[ts-alert-body]',
    imports: [CommonModule, DynamicDialogModule, ButtonModule, RippleModule],
    encapsulation: ViewEncapsulation.None,
    template: `
        <div class="p-alert-content-body-icon">
            <span [class]="info?.icon" class="p-icon"></span>
        </div>
        <div class="p-alert-content-body-summary">
            <span [innerHTML]="info?.summary" class="summary"></span>
        </div>
    `
})
export class AlertContent implements OnInit {
    info: AlertOption;

    constructor(private dialog: DialogService,
        private ref: DynamicDialogRef) { }

    ngOnInit(): void {
        const instance = this.dialog.dialogComponentRefMap.get(this.ref)?.instance;
        if (instance && instance.data) this.info = instance?.data;
    }

}