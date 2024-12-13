import {
    AfterContentInit, afterRender, AfterRenderRef, AfterViewChecked, booleanAttribute, ChangeDetectionStrategy, Component, ContentChildren, ElementRef, Input, NgZone,
    OnChanges, OnDestroy, Optional, QueryList, Renderer2, SimpleChanges, TemplateRef, ViewChild, ViewEncapsulation
} from "@angular/core";
import { PrimeTemplate } from "primeng/api";
import { Subscription } from "rxjs";
import { JsonAny, Objects } from "ts-ui/helper";
import { IconDesc, IconObject } from "./input.interface";
import { DomHandler } from "primeng/dom";
import { INgClass, INgStyle } from "ts-ui/common";

const { isString, notNull, notBlank, notEmpty, anyTrue, isArray, isObjectNotEmpty, isStringNotBlank, isNull, allNull, isTrue } = Objects;
const cls0 = 'ts-inputgroup';

export type InputType = 'calendar' | 'dropdown';

@Component({
    preserveWhitespaces: true,
    selector: 'ts-inputgroup',
    encapsulation: ViewEncapsulation.None,
    templateUrl: './input-group.html',
    host: {
        'class': 'p-element'
    }
})
export class InputGroup implements AfterContentInit,  OnChanges, OnDestroy {

    /**
     * Defined prefix input
     * @group Props
     * */
    @Input() set prefix(desc: IconDesc) {
        this.templates.prefix.input = desc;
    }

    /**
     * Defined suffix input
     * @group Props
     * */    
    @Input() set suffix(desc: IconDesc) {
        this.templates.suffix.input = desc
    }

    /**
     * Defined addon after input
     * @group Props
     * */    
    @Input() set addonAfter(desc: IconDesc) {
        this.templates.addonAfter.input = desc
    }

    /**
     * Defined addon before input
     * @group Props
     * */
    @Input() set addonBefore(desc: IconDesc) {
        this.templates.addonBefore.input = desc
    }

    /**
     * Defined compact input
     * @group Props
     * */
    @Input({ transform: booleanAttribute }) compact: boolean = true;

    /**
     * Defined size input
     * @group Props
     * */
    @Input() size: 'small' | 'larger';

    /**
     * placeholder of the input element.
     * @group Props
     */
    @Input() placeholder: string = '';

    /**
     * Class of the element.
     * @group Props
     */
    @Input() set styleClass(cls: INgClass) {
        this.styleClass2Json(cls);
    };

    /**
     * Class of the element.
     * @group Props
     */
    @Input() style: INgStyle;

    /**
     * Defined addon after input
     * @group Props
     * */
    @Input() borderStyle: 'dashed' | 'dotted' | 'none' | 'solid' = 'solid';

    @Input() overlayCls: string = undefined;


    @Input() flexAddon: string = '.5rem';
    @Input() flexAffix: string = '.5rem';
    @Input() fieldTag: string = undefined;

    //------
    styleJsonCls: JsonAny;
    fieldTemplate: TemplateRef<any>;
    templates: Templates = new Templates();



    @ContentChildren(PrimeTemplate)
    private lsTemplate: QueryList<PrimeTemplate>;
    private templatesSubscription: Subscription;

    @ViewChild('divInput', { static: false })
    divInput: ElementRef<HTMLDivElement>;

    @ViewChild('containerRef', {static: true})
    containerRef: ElementRef<HTMLDivElement>;

    afterRender$: AfterRenderRef;

    constructor(@Optional() private ngZone: NgZone, private renderer: Renderer2) { 
        this.afterRender$ = afterRender(() => setTimeout(() => this.alignOverlay(), 1));
    }

    ngIconClass(icon: string | IconObject) {
        return isString(icon) ? icon : icon.name;
    }

    iconClick(icon: string | IconObject) {
        if (icon && !isString(icon)) {
            this.ngZone.run(() => icon.onClick());
        }
    }


    ngAfterContentInit(): void {
        this.templatesSubscription = this.lsTemplate?.changes.subscribe(t => this.detectTemplates(t));
        this.detectTemplates(this.lsTemplate);
    }



    ngOnChanges(changes: SimpleChanges): void {
        const {borderStyle} = changes;
        if(borderStyle && this.containerRef){
            Objects.setStyle(this.containerRef, '--input-style', this.borderStyle);
        }
    }

    containerCls(): any {
        return {
            ['ts-inputgroup']: true,
            [`ts-inputgroup-compact`]: this.compact === true,
            [`ts-inputgroup-${this.borderStyle}`]: !!this.borderStyle,
            [`has-addon-before`]: this.templates.hasAddonBefore(),
            [`has-addon-after`]: this.templates.hasAddonAfter(),
            [`has-prefix`]: this.templates.hasPrefix(),
            [`has-suffix`]: this.templates.hasSuffix(),
            ...this.styleJsonCls

        };
    }

    containerStyleCls(): any {
        return {
            ...this.style,
            [`column-gap`]: anyTrue(this.templates.hasAddonAfter(),
                this.templates.hasAddonBefore()) ? this.flexAddon : undefined
        }
    }

    styleAddonCls(): any {
        return {
            [`column-gap`]: this.flexAddon
        }
    }

    styleInputCls(): any {
        return {
            [`column-gap`]: this.flexAffix,
        }
    }

    ngOnDestroy(): void {
        this.templatesSubscription?.unsubscribe();
        this.afterRender$?.destroy();
    }

    ngAfterViewInit(): void {
        this.ngZone.runOutsideAngular(() => {
            setTimeout(() => this.alignOverlay(), 1)
        });
    }


    protected setFieldTemplate(template: TemplateRef<any>) {
        this.fieldTemplate = template;
    }


    private detectTemplates(templates: QueryList<PrimeTemplate>) {
        this.templates?.resetPTemplate();

        if (templates.length > 0 && isNull(this.templates)) {
            this.templates = new Templates();
        }

        templates?.forEach(item => {

            switch (item.getType()) {
                case 'field':
                    this.setFieldTemplate(item.template);
                    break;

                case 'prefix':
                    this.templates.prefix.ptemplate = item.template;
                    break;

                case 'suffix':
                    this.templates.suffix.ptemplate = item.template;
                    break;

                case 'addonBefore':
                    this.templates.addonBefore.ptemplate = item.template;
                    break;

                case 'addonAfter':
                    this.templates.addonAfter.ptemplate = item.template;
                    break;
            }
        });

    }

    private styleClass2Json(cls: INgClass) {
        this.styleJsonCls = Objects.ngClassToJson(cls);
    }

    private alignOverlay(): void {
        const divEl = this.divInput?.nativeElement;
        if (divEl) this.editOverlay(this.overlayCls, '.p-overlay', 
            '.ng-trigger-overlayAnimation');
        
    }

    private editOverlay(...overlayCls: string[]): void {
        const inputEl = this.divInput?.nativeElement;
        const newCls = overlayCls.filter(c => notBlank(c));
        for (let cls of newCls){
            let overlay = DomHandler.findSingle(inputEl, cls);
            if(isNull(overlay)) continue;
            else {
                overlay.style.minWidth = DomHandler.getOuterWidth(inputEl) + 'px';
                DomHandler.relativePosition(overlay, inputEl);
                break;
            }
        }
    }

}

export class Templates {
    prefix = new IconInput();
    suffix = new IconInput();
    addonAfter = new IconInput();
    addonBefore = new IconInput();


    hasPrefix(): boolean {
        return this.prefix.has();
    }

    hasSuffix(): boolean {
        return this.suffix.has();
    }

    hasAddonBefore(): boolean {
        return this.addonBefore.has();
    }

    hasAddonAfter(): boolean {
        return this.addonAfter.has();
    }

    resetPTemplate(): void {
        this.prefix.ptemplate = undefined;
        this.suffix.ptemplate = undefined;
        this.addonBefore.ptemplate = undefined;
        this.addonAfter.ptemplate = undefined;
    }
}

export class IconInput {

    constructor(
        private _input?: IconDesc,
        public ptemplate?: TemplateRef<any>) {
    }

    get inputTemplate(): TemplateRef<any> {
        return this.isInputTemplate ? <any>this._input : undefined;
    }

    get input() { return this._input; }

    set input(input: IconDesc) {
        if (isString(input)) this._input = input.split(';');
        else this._input = input;
    }

    isInputTemplate(): boolean {
        return this._input instanceof TemplateRef;
    }

    isPTemplate(): boolean {
        return notNull(this.ptemplate);
    }


    isString(): boolean {
        return isStringNotBlank(this._input);
    }

    isObject(): boolean {
        return isObjectNotEmpty(this._input);
    }

    isArray(): boolean {
        return isArray(this._input) && this._input.length > 0;
    }

    has(): boolean {
        if (allNull(this._input, this.ptemplate)) return false;
        else if (notNull(this.ptemplate)) return true;
        else if (this.isString()) return true;
        else if (this.isObject()) return true;
        else return this.isArray();
    }


}