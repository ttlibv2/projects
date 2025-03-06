import { CommonModule } from "@angular/common";
import { AfterContentInit, booleanAttribute, Component, ContentChildren, DoCheck, ElementRef, inject, Injectable, Input, OnChanges, OnDestroy, QueryList, SimpleChanges, TemplateRef, ViewEncapsulation } from "@angular/core";
import { AnyTemplateOutlet, INgClass, INgStyle, QueryPTemplate, Severity, StringTemplate } from "ts-ui/common";
import { ButtonModule, ButtonProps } from "primeng/button";
import { PrimeTemplate } from "primeng/api";
import { Objects } from "ts-ui/helper";
import { BaseComponent, BaseComponentStyle } from "primeng/basecomponent";

const { isString, isArray } = Objects;

export interface CardAction extends ButtonProps {
}

interface PTemplate {
    header?: TemplateRef<any>;
    subheader?: TemplateRef<any>;
    headerIcon?: TemplateRef<any>;
    headerLeft?: TemplateRef<any>;
    headerRight?: TemplateRef<any>;
    title?: TemplateRef<any>;
    subtitle?: TemplateRef<any>;
    content?: TemplateRef<any>;
    footer?: TemplateRef<any>;
    action?: TemplateRef<any>;
}

@Injectable()
export class CardStyle extends BaseComponentStyle {
    name = 'card';
    classes = {root: 'ts-card'};
    theme: any = ({dt}) => ``;
}

@Component({
    standalone: true,
    selector: 'ts-card',
    encapsulation: ViewEncapsulation.None,
    imports: [CommonModule, ButtonModule, AnyTemplateOutlet],
    templateUrl: 'card-view.html',
    styles: [`ts-card { display: block;}`],
    host: {
        '[class.p-card]': 'true',
        '[class.p-component]': 'true',
    }
})
export class Card extends BaseComponent implements OnChanges, AfterContentInit, OnDestroy, DoCheck {
    _component = inject(CardStyle);
    /**
     * Defined header
     * @group Props 
     * */
    @Input() header: StringTemplate;

    /**
     * Defined sub-header
     * @group Props 
     * */
    @Input() subheader: StringTemplate;

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
     * Defined card severity
     * @group Props 
     * */
    @Input() severity: Severity;

    /**
     * Defined card text + background severity
     * @argument string[bgColor, textColor]
     * @group Props 
     * */
    @Input() textBg: Severity | [string, string];

    /**
     * Defined card text + background severity
     * @argument string[bgColor, textColor]
     * @group Props 
     * */
    @Input() borderColor: Severity | [string, string];

    /**
     * Defined card severity
     * @group Props 
     * */
    @Input({ transform: booleanAttribute }) useBg: boolean = true;

    /**
     * footer actions
     * @group Props 
     * */
    @Input() actions: CardAction[] = [];

    /**
     * visible header
     * @group Props 
     * */
    @Input({ transform: booleanAttribute }) visibleHeader: boolean = true;

    @Input() headerClass: INgClass;
    @Input() headerStyle: INgStyle;

    @Input() bodyClass: INgClass;
    @Input() bodyStyle: INgStyle;

    @Input() contentClass: INgClass;
    @Input() contentStyle: INgStyle;

    @Input() footerClass: INgClass;
    @Input() footerStyle: INgStyle;

    @Input() footerAlign: 'start' | 'end' | 'center' = 'start';

    get actionTemplate() {
        return this.action || this.pTemplate?.action;
    }

    get headerTemplate() {
        return this.header || this.pTemplate?.header;
    }

    get subheaderTemplate() {
        return this.subheader || this.pTemplate?.subheader;
    }

    get headerIconTemplate() {
        return this.headerIcon || this.pTemplate?.headerIcon;
    }

    get headerLeftTemplate() {
        return this.headerLeft || this.pTemplate?.headerLeft;
    }

    get headerRightTemplate() {
        return this.headerRight || this.pTemplate?.headerRight;
    }

    //-------------
    @ContentChildren(PrimeTemplate)
    private templates!: QueryList<PrimeTemplate>;
    private query$: QueryPTemplate;

    pTemplate: PTemplate = {};

    constructor(private elementRef: ElementRef<HTMLDivElement>) {
        super();
     }

    hasHeader(): boolean {
        return Objects.anyNotNull(this.headerTemplate, this.subheaderTemplate, 
                this.headerIconTemplate, this.headerLeftTemplate, this.headerRightTemplate)
    }

    ngOnChanges(changes: SimpleChanges): void {
        const { textBg, severity } = changes;






        if (textBg) {
            let arr = isArray(this.textBg) && this.textBg.length == 2 ? this.textBg : [undefined, undefined];
            this.elementRef.nativeElement.style.setProperty('--card-bg', arr[0]);
            this.elementRef.nativeElement.style.setProperty('--card-text-color', arr[1]);
        }

    }

    ngAfterContentInit(): void {
        this.query$ = QueryPTemplate.register({
            templates: this.templates,
            onClear: () => this.pTemplate = {},
            onUpdate: v => this.pTemplate = v
        })

    }

    ngDoCheck(): void {
        
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

    // containerCls(): any {
    //     return {
    //         [`text-bg-${this.severity}`]: this.textBg && typeof this.textBg === 'string',
    //         ...Objects.ngClassToJson(this.styleClass)
    //     }
    // }

}