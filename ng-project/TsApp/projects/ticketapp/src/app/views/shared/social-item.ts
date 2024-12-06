import { CommonModule } from "@angular/common";
import { Component, Input, ViewEncapsulation } from "@angular/core";
import { ButtonModule } from "primeng/button";
import { SocialLink } from "../../models/common";
import { RouterLink } from "@angular/router";

@Component({
    standalone: true,
    selector: 'ts-btn-social',
    encapsulation: ViewEncapsulation.None,
    imports: [CommonModule, ButtonModule, RouterLink], 
    template: `
         <p-button [label]="item.label" [icon]="item.icon" 
            [routerLink]="item.link" styleClass="w-full" size="large" (onClick)="click($event)">
                <ng-template pTemplate="icon" let-cls="class">
                    <span class="social-icon" [ngClass]="cls" [style.color]="item.iconColor"></span>
                </ng-template> 
        </p-button>
    `
})
export class SocialItem {
    @Input() item: SocialLink;

    click(event: any): void {
        if(this.item){
            this.item.onClick(event);
        }
    }
}