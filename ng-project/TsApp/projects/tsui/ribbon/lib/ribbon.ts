import {
    AfterContentInit,
    AfterViewInit,
    booleanAttribute,
    Component, ContentChildren,
    ElementRef,
    Input, numberAttribute, OnChanges, OnDestroy, OnInit, QueryList, Renderer2, SimpleChanges,
    ViewChild,
    ViewEncapsulation
} from "@angular/core";
import {CommonModule} from "@angular/common";
import {AnyTemplateOutlet, INgClass, INgStyle, QueryPTemplate, Severity, StringTemplate} from "ts-ui/common";
import {PrimeTemplate} from "primeng/api";
import {Objects} from "ts-ui/helper";

const  {isTrue, isFalse, isBlank, isNull} = Objects;

@Component({
    standalone: true,
    selector: 'ts-ribbon',
    encapsulation:ViewEncapsulation.None,
    imports: [CommonModule, AnyTemplateOutlet],
    styles: ':host { display: block; }',
    host: { 'class': 'ribbon-wrapper' },
    template: `
        <div class="ribbon" [ngClass]="ribbonCls()" [ngStyle]="ribbonStyle" #ribbonRef>
            <ng-container *anyTemplate="textTemplate">
                @if(text) {
                    <span [innerText]="text"></span>
                }
            </ng-container>
        </div>
        <ng-content></ng-content>
    `
})
export class Ribbon implements AfterContentInit, OnChanges, OnDestroy, AfterViewInit {
    @Input() position: 'left-top' | 'left-bottom' | 'right-top' | 'right-bottom';
    @Input() orientation: 'horizontal' | 'vertical';
    @Input() severity: Severity | [string, string];
    @Input() text: StringTemplate;
    @Input({transform: booleanAttribute}) clip: boolean;
    @Input({transform: booleanAttribute}) bookmark: boolean;
    @Input() ribbonWidth: string;
    @Input() ribbonHeight: string;
    @Input() ribbonStyle: INgStyle;
    @Input() ribbonClass: string;

    get textTemplate(): any {
        return this.text || this.pTemplates?.text;
    }

    @ViewChild('ribbonRef', {static: true})
    private ribbonRef: ElementRef<HTMLElement>;

    @ContentChildren(PrimeTemplate)
    private templates!:QueryList<PrimeTemplate>;
    private templatesQuery: QueryPTemplate;
    private pTemplates: any;

    constructor(private renderer: Renderer2) {
    }

    ngOnInit() {
    }

    ngOnChanges(changes: SimpleChanges) {
        const {ribbonHeight, ribbonWidth} = changes;
        if(ribbonWidth) this.setRibbonStyle('--ribbon-width', this.ribbonWidth);
        if(ribbonHeight) this.setRibbonStyle('--ribbon-height', this.ribbonHeight);
    }


    ribbonCls(): any {
        return {
            [`ribbon`]: true,
            [`ribbon-horizontal`]: isBlank(this.orientation) || this.orientation === 'horizontal',
            [`ribbon-vertical`]: this.orientation === 'vertical',
            [`ribbon-${this.severity}`]: !!this.severity,

            [`ribbon-left-top`]: isBlank(this.position) || this.position === 'left-top',
            [`ribbon-left-bottom`]: this.position === 'left-bottom',
            [`ribbon-right-top`]: this.position === 'right-top',
            [`ribbon-right-bottom`]: this.position === 'right-bottom',

            [`ribbon-clip`]: !!this.clip,
            [`ribbon-clip-left`]: !!this.clip && this.isLeft(),
            [`ribbon-clip-right`]: !!this.clip && this.isRight(),

            [`ribbon-bookmark`]: !!this.bookmark,
            [`ribbon-bookmark-left`]: !!this.bookmark && this.isLeft(),
            [`ribbon-bookmark-right`]: !!this.bookmark && this.isRight(),

            [this.ribbonClass]: !!this.ribbonClass,
        }
    }

    ngAfterContentInit() {
        this.templatesQuery = QueryPTemplate.register({
            templates: this.templates,
            onClear: () => this.pTemplates = undefined,
            onUpdate: pt => this.pTemplates = pt
        });
    }

    ngAfterViewInit() {
        if(isBlank(this.position)) {
            this.addRemoveRibbonCls(undefined, 'left-top');
        }

        if(isBlank(this.orientation)) {
            this.addRemoveRibbonCls(undefined, 'horizontal');
        }
    }

    ngOnDestroy() {
        this.templatesQuery?.destroy();
    }

    private isLeft(): boolean {
        return this.position === 'left-top' || this.position === 'left-bottom' || isBlank(this.position);
    }

    private isRight(): boolean {
        return this.position === 'right-top' || this.position === 'right-bottom';
    }

    private addRemoveRibbonCls(prevCls: string | boolean, newCls: string, prefix: string = 'ribbon-'): void {
        if(this.ribbonRef) {
            const element = this.ribbonRef.nativeElement;
            if (typeof prevCls === 'boolean') {
                const flag = prevCls;
                prevCls = Objects.isFalse(flag) ? undefined : newCls;
                newCls = Objects.isTrue(flag) ? newCls : undefined;
            }

            if (prevCls) this.renderer.removeClass(element, `${prefix}${prevCls}`);
            if (newCls) this.renderer.addClass(element, `${prefix}${newCls}`);
        }
    }

    private setRibbonStyle(property: string, value: any):void {
        if(this.ribbonRef) {
            const element = this.ribbonRef.nativeElement;
            element.style.setProperty(property, value);
        }
    }
}