import { CommonModule } from "@angular/common";
import { AfterContentInit, booleanAttribute, Component, ContentChildren, Input, OnDestroy, QueryList, TemplateRef, ViewEncapsulation } from "@angular/core";
import { CardAction } from "./card.interface";
import {AnyTemplateOutlet, INgClass, INgStyle, QueryPTemplate, StringTemplate} from "ts-ui/common";
import { ButtonModule } from "primeng/button";
import { PrimeTemplate } from "primeng/api";
import { Objects } from "ts-ui/helper";

interface PTemplate {
     header?: TemplateRef<any>;
     headerIcon?: TemplateRef<any>;
     headerLeft?: TemplateRef<any>;
     headerRight?: TemplateRef<any>;
     title?: TemplateRef<any>;
     subtitle?: TemplateRef<any>;
     content?: TemplateRef<any>;
     footer?: TemplateRef<any>;
     action?: TemplateRef<any>;
}

@Component({
    standalone: true,
    selector: 'ts-card',
    encapsulation: ViewEncapsulation.None,
    imports: [CommonModule, AnyTemplateOutlet, ButtonModule],
    templateUrl: 'card-view.html',
})
export class Card implements AfterContentInit, OnDestroy {

    /**
     * Defined ngClass 
     * @group Props 
     * */    
    @Input() styleClass: INgClass;

    /**
     * Defined ngStyle
     * @group Props 
     * */
    @Input() style: INgStyle;

    /**
     * Defined title header
     * @group Props 
     * */    
    @Input() header: StringTemplate;

    /**
     * Defined icon header
     * @group Props 
     * */
    @Input() headerIcon: StringTemplate;

    /**
     * Defined area header right
     * @group Props 
     * */    
    @Input() headerRight: TemplateRef<any>;

    /**
     * Defined area header left
     * @group Props 
     * */    
    @Input() headerLeft: TemplateRef<any>;

    /**
     * Defined card title
     * @group Props 
     * */
    @Input() title: StringTemplate;

    /**
     * Defined card subtitle
     * @group Props 
     * */
    @Input() subtitle: StringTemplate;

    /**
     * Defined card content
     * @group Props 
     * */
    @Input() content: TemplateRef<any>;    

    /**
     * Defined card footer
     * @group Props 
     * */
    @Input() footer: TemplateRef<any>;   
    
    @Input() action: TemplateRef<any>;

    /**
     * footer actions
     * @group Props 
     * */
    @Input() actions: CardAction[] = [];

    /**
     * visible header
     * @group Props 
     * */
    @Input({transform: booleanAttribute})
    visibleHeader: boolean = true;

    @Input() contentClass: INgClass;
    @Input() contentStyle: INgStyle;

    @Input() footerClass: INgClass;
    @Input() footerStyle: INgStyle;

    @Input() footerAlign: 'start' | 'end' | 'center' = 'start';

    get actionTemplate() {
        return this.action || this.pTemplate?.action;
    }

    //-------------
    @ContentChildren(PrimeTemplate)
    private templates!:QueryList<PrimeTemplate>;
    private query$: QueryPTemplate;

    pTemplate: PTemplate = {};

    hasHeader(): boolean {
        return Objects.anyNotNull(this.header, this.headerIcon, this.headerLeft, this.headerRight,
                this.pTemplate?.headerIcon, this.pTemplate?.header, this.pTemplate?.headerLeft, this.pTemplate?.headerRight)
    }

    ngAfterContentInit(): void {
        this.query$ = QueryPTemplate.register({
            templates: this.templates,
            onClear: () => this.pTemplate = {},
            onUpdate: v => this.pTemplate = v
        })

    }
    
    ngOnDestroy(): void {
        this.query$?.destroy();
    }

    ngFooterStyle(): any {
        return {
            ...this.footerStyle
        };
    }

    ngFooterClass(): any {
        return {
            [`justify-content-${this.footerAlign}`]: !!this.footerAlign,
            ...Objects.ngClassToJson(this.footerClass)
        };
    }

}