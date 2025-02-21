import { Component, ViewEncapsulation, Input, TemplateRef, AfterContentInit, ContentChildren, QueryList, OnDestroy } from "@angular/core";
import { AnyTemplateOutlet, INgClass, INgStyle, QueryPTemplate, StringTemplate } from 'ts-ui/common';
import { PrimeTemplate } from "primeng/api";
import { CommonModule } from "@angular/common";

@Component({
    standalone: true,
    encapsulation: ViewEncapsulation.None,
    imports: [CommonModule, AnyTemplateOutlet],
    selector: 'ts-card-section',
    host: { 'class': 'ts-card-section'},
    template: `
        @if(header || pHeader) {
            <div class="ts-card-section-header" [ngClass]="headerClass" [ngStyle]="headerStyle">
                <ng-container *anyTemplate="header || pHeader;useI18n:true">
                    <span [innerHTML]="header"></span>
                </ng-container>
            </div>
        }

        <div class="ts-card-section-content" [ngClass]="contentClass" [ngStyle]="contentStyle">
            <ng-container *anyTemplate="content || pContent">
                <ng-content></ng-content>
            </ng-container>
        </div>
    `
})
export class CardSection implements AfterContentInit, OnDestroy {

    /**
     * defined title section
     * @group Props
     * */
    @Input() header: StringTemplate;

    /**
     * defined content section
     * @group Props
     * */    
    @Input() content: TemplateRef<any>;

    /**
     * defined header style class
     * @group Props
     * */
    @Input() headerClass: INgClass;

    /**
     * defined header style
     * @group Props
     * */
    @Input() headerStyle: INgStyle;

    /**
     * defined content style class
     * @group Props
     * */
    @Input() contentClass: INgClass;

    /**
     * defined content style
     * @group Props
     * */
    @Input() contentStyle: INgStyle;

    @ContentChildren(PrimeTemplate)
    private templates!:QueryList<PrimeTemplate>;

    pHeader: TemplateRef<any>;
    pContent: TemplateRef<any>;

    private clsQuery: QueryPTemplate;

    ngAfterContentInit(): void {
        this.clsQuery = QueryPTemplate.register({

            templates: this.templates,

            onClear: () => {
                this.pHeader = undefined;
                this.pContent = undefined;
            },

            onUpdate: items => {
                this.pHeader = items['title'];
                this.pContent = items['content'];
            }
        });
    }

    ngOnDestroy(): void {
        this.clsQuery?.destroy();
    }

}