import {animate, state, style, transition, trigger} from '@angular/animations';
import {ChangeDetectionStrategy, Component, NgZone, OnInit} from '@angular/core';
import {CommonModule, NgIf} from '@angular/common';
import {Toast, ToastPackage, ToastrService} from "ngx-toastr";
import {ToastConfig} from "../../services/toast.service";
import {InputIconModule} from "primeng/inputicon";
import {Ripple} from "primeng/ripple";
import {TimesIcon} from "primeng/icons/times";
import {ButtonModule} from "primeng/button";
import {Objects} from "ts-helper";
import {ProgressBarModule} from "primeng/progressbar";
import {ToolbarModule} from "primeng/toolbar";
import {IconFieldModule} from "primeng/iconfield";

const  { isBlank } = Objects;

@Component({
    standalone: true,
    selector: '[toast-component]',
    animations: [
        trigger('flyInOut', [
            state('inactive', style({ opacity: 0 })),
            state('active', style({ opacity: 1 })),
            state('removed', style({ opacity: 0 })),
            transition('inactive => active', animate('{{ easeTime }}ms {{ easing }}')),
            transition('active => removed', animate('{{ easeTime }}ms {{ easing }}')),
        ]),
    ],
    preserveWhitespaces: false,
    imports: [NgIf, InputIconModule, Ripple, TimesIcon, ButtonModule, ProgressBarModule, ToolbarModule, IconFieldModule, CommonModule],
    changeDetection: ChangeDetectionStrategy.OnPush,
    template: `
       <div class="p-toast-message-content" [ngClass]="config.messageClass">
           <div class="column-left" *ngIf="config.messageIcon">
               <p-button [icon]="config.messageIcon" [rounded]="true" [text]="true" [severity]="severity" size="large" />
           </div>
           <div class="column-center" *ngIf="title || message">
               <div *ngIf="title" class="p-toast-message-title">
                   <label class="label">{{title}}</label>
                   <label class="count" *ngIf="duplicatesCount">[{{duplicatesCount + 1}}]</label>
               </div>
               <div *ngIf="message" class="p-toast-message-summary">
                   <div *ngIf="config.enableHtml" [innerHTML]="message"></div>
                   <div *ngIf="!config.enableHtml">{{message}}</div>
               </div>
           </div>
           <div class="column-right">
               <p-button *ngIf="config.closeButton" (click)="remove()"
                      [icon]="closeIcon" [class]="prefix + '-icon-close'" class="p-link" ariaLabel="Close"
                         [rounded]="true" [severity]="severity" [text]="true" size="small"/>
           </div>
       </div>
        
    `
})
export class ToastItem<ConfigPayload = any> extends Toast<ConfigPayload> implements OnInit{
    config: ToastConfig;
    prefix: string;

    // "success" | "info" | "warning" | "danger" | "help" | "primary" | "secondary" | "contrast" | null | undefined
    severity: any;

    constructor(service: ToastrService, pkg: ToastPackage, ngZone: NgZone) {
        super(service, pkg, ngZone);
    }

    ngOnInit() {
        this.config = this.toastPackage.config
        this.prefix = this.config.toastClassPrefix ?? '';
        this.severity = this.determineSeverity();
    }

    private determineSeverity(): string {
        const toastType = this.toastPackage.toastType.trim();
        const dotIndex = toastType.lastIndexOf('-');
        let severity = dotIndex === -1 ? toastType : toastType.substring(dotIndex+1);
        if(severity === 'error') severity = 'danger';
        return severity;
    }

    get closeIcon(): string {
        return this.config.closeIcon ?? 'pi pi-times';
    }

    get progressValue(): number {
        return Math.floor( this.width() );
    }

}