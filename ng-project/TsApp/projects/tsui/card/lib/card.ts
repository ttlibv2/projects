import { CommonModule } from "@angular/common";
import { AfterContentInit, booleanAttribute, Component, ContentChildren, Input, OnChanges, OnDestroy, OnInit, QueryList, SimpleChanges, TemplateRef, ViewEncapsulation } from "@angular/core";
import { CardAction } from "./card.interface";
import { AnyTemplateOutlet, PropCls, StyleCls } from "ts-ui/common";
import { ButtonModule } from "primeng/button";
import { PrimeTemplate } from "primeng/api";
import { Subject, takeUntil } from "rxjs";
import { Objects } from "ts-ui/helper";

@Component({
    standalone: true,
    selector: 'ts-card',
    encapsulation: ViewEncapsulation.None,
    imports: [CommonModule, AnyTemplateOutlet, ButtonModule],
    templateUrl: 'card.html',
    // host: {
    //     '[class.p-card]': 'true',
    //     '[class.p-component]': 'true'
    // }
})
export class Card implements OnInit, OnChanges, AfterContentInit, OnDestroy {

    /**
     * Defined ngClass 
     * @group Props 
     * */    
    @Input() styleClass: string;

    /**
     * Defined ngStyle
     * @group Props 
     * */
    @Input() style: StyleCls;

    /**
     * Defined title header
     * @group Props 
     * */    
    @Input() header: string | TemplateRef<any>;

    /**
     * Defined icon header
     * @group Props 
     * */
    @Input() headerIcon: string | TemplateRef<any>;

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
     * Defined body title
     * @group Props 
     * */
    @Input() title: string | TemplateRef<any>;

    /**
     * Defined body sub-title
     * @group Props 
     * */
    @Input() subtitle: string | TemplateRef<any>;

    /**
     * Defined body sub-title
     * @group Props 
     * */
    @Input() content: TemplateRef<any>;    

    /**
     * Defined body sub-title
     * @group Props 
     * */
    @Input() footer: TemplateRef<any>;   
    
    @Input() actionTemplate: TemplateRef<any>;

    /**
     * footer actions
     * @group Props 
     * */
    @Input() actions: CardAction[] = [];

    /**
     * visible header
     * @group Props 
     * */
    @Input({transform: booleanAttribute}) visibleHeader: boolean = true;

    @Input() contentStyleClass: PropCls;
    @Input() contentStyle: StyleCls;

    @Input() footerClass: PropCls;
    @Input() footerStyle: StyleCls;

    @Input() alignFooter: 'start' | 'end' | 'center' = 'start';

    get footerTemplate(): TemplateRef<any> {
        return this.footer || this._footerTemplate;
    }

    get headerLeftTemplate(): TemplateRef<any> {
        return this.headerLeft || this._headerLeftTemplate;
    }

    get headerRightTemplate(): TemplateRef<any> {
        return this.headerRight || this._headerRightTemplate;
    }

    get contentTemplate(): TemplateRef<any> {
        return this.content || this._contentTemplate;
    }


    //-------------
    @ContentChildren(PrimeTemplate)
    private templates!:QueryList<PrimeTemplate>;

    private destroy$ = new Subject();
    private _headerTemplate: TemplateRef<any>;
    private _headerIconTemplate: TemplateRef<any>;
    private _headerLeftTemplate: TemplateRef<any>;
    private _headerRightTemplate: TemplateRef<any>;
    private _titleTemplate: TemplateRef<any>;
    private _subtitleTemplate: TemplateRef<any>;
    private _contentTemplate: TemplateRef<any>;
    private _footerTemplate: TemplateRef<any>;
    private _actionTemplate: TemplateRef<any>;

    ngOnInit(): void {
    }

    ngOnChanges(changes: SimpleChanges): void {
    }

    ngAfterContentInit(): void {
        this.extractTemplates(this.templates);
        this.templates.changes//.pipe(takeUntil(this.destroy$))
            .subscribe(items => this.extractTemplates(items));
    }
    
    ngOnDestroy(): void {
        this.destroy$.next(null);
        this.destroy$.complete();
    }

    ngFooterStyle(): any {
        return {
            ...this.footerStyle
        };
    }

    ngFooterClass(): any {
        return {
            [`justify-content-${this.alignFooter}`]: !!this.alignFooter,
            ...Objects.ngClassToJson(this.footerClass)
        };
    }

    private extractTemplates(list: QueryList<PrimeTemplate>) {
        console.log(`extractTemplates`, list.length);
        this.clearTemplates();
        list?.forEach(item => {
            switch (item.getType()) {
                case 'header': this._headerTemplate = item.template; break;
                case 'headerIcon': this._headerIconTemplate = item.template; break;
                case 'headerLeft': this._headerLeftTemplate = item.template; break;
                case 'headerRight': this._headerRightTemplate = item.template; break;
                case 'title': this._titleTemplate = item.template; break;
                case 'subtitle': this._subtitleTemplate = item.template; break;
                case 'content': this._contentTemplate = item.template; break;
                case 'footer': this._footerTemplate = item.template; break;
                case 'action': this._actionTemplate = item.template; break;

            }
        });
    }

    private clearTemplates(): void {
        this._headerTemplate = undefined;
        this._headerIconTemplate = undefined;
        this._headerLeftTemplate = undefined;
        this._headerRightTemplate = undefined;
        this._titleTemplate = undefined;
        this._subtitleTemplate = undefined;
        this._contentTemplate = undefined;
        this._footerTemplate = undefined;
        this._actionTemplate = undefined;
    }

}