import { Component, ViewEncapsulation, Input, TemplateRef, AfterContentInit, ContentChildren, QueryList, OnDestroy } from "@angular/core";
import { AnyTemplateOutlet, QueryPTemplate, StringTemplate } from 'ts-ui/common';
import { PrimeTemplate } from "primeng/api";

@Component({
    standalone: true,
    encapsulation: ViewEncapsulation.None,
    imports: [AnyTemplateOutlet],
    selector: 'ts-card-section',
    host: { 'class': 'ts-card-section'},
    template: `
        @if(header || pHeader) {
            <div class="ts-card-section-header">
                <ng-container *anyTemplate="header || pHeader;useI18n:true">
                    <span [innerHTML]="header"></span>
                </ng-container>
            </div>
        }

        <div class="ts-card-section-content">
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