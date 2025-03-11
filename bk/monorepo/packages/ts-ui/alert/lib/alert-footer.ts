import { CommonModule } from "@angular/common";
import { Component, OnInit, ViewEncapsulation } from "@angular/core";
import { ButtonModule } from "primeng/button";
import { DialogService, DynamicDialogModule, DynamicDialogRef } from "primeng/dynamicdialog";
import { RippleModule } from "primeng/ripple";
import { AlertButtonOption, AlertOption } from "./alert.interface";

@Component({
    standalone: true,
    selector: '[ts-alert-footer]',
    imports: [CommonModule, DynamicDialogModule, ButtonModule, RippleModule],
    encapsulation: ViewEncapsulation.None,
    template: `
        <div class="flex flex-row flex-wrap gap-2 div-alert-footer">
           
            @if(actions?.length) {
                <ng-template ngFor let-action [ngForOf]="actions" >
                    <ng-container [ngTemplateOutlet]="applyButton" 
                    [ngTemplateOutletContext]="{value: action}"></ng-container>
                </ng-template>
            }

            @if(okButton){
                <ng-container [ngTemplateOutlet]="applyButton" 
                    [ngTemplateOutletContext]="{value: okButton}"></ng-container>
            }

            @if(cancelButton){
                <ng-container [ngTemplateOutlet]="applyButton" 
                    [ngTemplateOutletContext]="{value: cancelButton}"></ng-container>
            }



        </div>

        <ng-template let-option="value" #applyButton>
            <p-button [buttonProps]="option" [label]="option.label" 
             [disabled]="option.disabled === true"
                (onClick)="onButtonClick($event, option)"></p-button>
        </ng-template>
    `
})
export class AlertFooter implements OnInit {
    info: AlertOption;

    get okButton() {
        return this.info?.okButton;
    }

    get cancelButton() {
        return this.info?.cancelButton;
    }

    get actions() {
        return this.info?.actions;
    }

    constructor(private dialog: DialogService,
        private ref: DynamicDialogRef) { }

    ngOnInit(): void {
        const instance = this.dialog.dialogComponentRefMap.get(this.ref)?.instance;
        if (instance && instance.data) this.info = instance?.data;
    }

    onButtonClick(event: MouseEvent, option: AlertButtonOption) {
        console.log(`onButtonClick`)
        if (option.onClick) {
            option.onClick({ event, dynamicRef: this.ref, option });
        }
    }



}