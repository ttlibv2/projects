import { CommonModule } from "@angular/common";
import {Component, inject, Input, OnChanges, SimpleChanges, ViewEncapsulation} from "@angular/core";
import { ButtonModule } from "primeng/button";
import { SocialLink } from "../../models/common";
import { RouterLink } from "@angular/router";
import {ColDirective} from "ts-ui/common";

@Component({
    standalone: true,
    selector: 'ts-btn-social',
    encapsulation: ViewEncapsulation.None,
    imports: [CommonModule, ButtonModule, RouterLink, ColDirective],
    hostDirectives: [{directive:ColDirective, inputs: ['span','sm','md','lg','xl','x2l','x3l','x4l']}],
    styles: `:host{ display: block; }`,
    template: `
        <p-button [label]="item.label" [icon]="item.icon"
                  [routerLink]="item.link" styleClass="w-full" size="large" (onClick)="click($event)">
            <ng-template pTemplate="icon" let-cls="class">
                <span class="social-icon" [ngClass]="cls" [style.color]="item.iconColor"></span>
            </ng-template>
        </p-button><!---->
    `
})
export class SocialItem implements OnChanges {
    @Input() item: SocialLink;

   private tsCol = inject(ColDirective);

    click(event: any): void {
        if(this.item){
            this.item.onClick(event);
        }
    }

    ngOnChanges(changes: SimpleChanges) {
        const {item} = changes;
        if(item && this.item.responsive) {
            this.tsCol.update(this.item.responsive);
        }
    }
}