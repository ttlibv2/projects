import {
    AfterContentInit, afterRender, AfterRenderRef, AfterViewChecked, booleanAttribute, ChangeDetectionStrategy, Component, ContentChildren, ElementRef, Input, NgZone,
    OnChanges, OnDestroy, Optional, QueryList, SimpleChanges, TemplateRef, ViewChild, ViewEncapsulation
} from "@angular/core";
import { PrimeTemplate } from "primeng/api";
import { Subscription } from "rxjs";
import { Objects } from "ts-ui/helper";
import { IconDesc, IconObject } from "./input.interface";
import { DomHandler } from "primeng/dom";

const { isString, notNull, notBlank, anyTrue, isArray, isObjectNotEmpty, isStringNotBlank, isNull, allNull, isTrue } = Objects;
const cls = 'ts-inputgroup';

export type InputType = 'calendar' | 'dropdown';

@Component({
    preserveWhitespaces: true,
    selector: cls,
    encapsulation: ViewEncapsulation.None,
    templateUrl: './input-group.html',
    host: {
        'class': 'p-element'
    }
})
export class InputGroup implements AfterContentInit,  OnChanges, OnDestroy {

    @Input() set prefix(desc: IconDesc) {
        this.templates.prefix.input = desc;
    }

    @Input() suffix(desc: IconDesc) {
        this.templates.suffix.input = desc
    }

    @Input() addonAfter(desc: IconDesc) {
        this.templates.addonAfter.input = desc
    }

    @Input() addonBefore(desc: IconDesc) {
        this.templates.addonBefore.input = desc
    }


    @Input({ transform: booleanAttribute }) compact: boolean = true;
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
    @Input() styleClass: string;

    /**
     * Class of the element.
     * @group Props
     */
    @Input() style: { [key: string]: any };

    @Input() overlayCls: string = undefined;


    @Input() flexAddon: string = '.5rem';
    @Input() flexAffix: string = '.5rem';
    @Input() fieldTag: string = undefined;

    //------
    fieldTemplate: TemplateRef<any>;
    templates: Templates = new Templates();


    @ContentChildren(PrimeTemplate)
    private lsTemplate: QueryList<PrimeTemplate>;
    private templatesSubscription: Subscription;

    @ViewChild('divInput', { static: false })
    divInput: ElementRef<HTMLDivElement>;

    afterRender$: AfterRenderRef;

    constructor(@Optional() private ngZone: NgZone) { 
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
    }

    containerCls(): any {
        return {
            [cls]: true,
            [`${cls}-compact`]: this.compact === true,
            [`has-addon-before`]: this.templates.hasAddonBefore(),
            [`has-addon-after`]: this.templates.hasAddonAfter(),
            [`has-prefix`]: this.templates.hasPrefix(),
            [`has-suffix`]: this.templates.hasSuffix(),
            [this.styleClass]: notBlank(this.styleClass),

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
            [`column-gap`]: this.flexAffix
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