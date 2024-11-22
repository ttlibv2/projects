import { AfterViewInit, Directive, ElementRef, Input, OnChanges, OnDestroy, OnInit, Renderer2, SimpleChanges } from "@angular/core";
import { Subject, takeUntil } from "rxjs";
import { Direction, Directionality } from "@angular/cdk/bidi";
import { Objects } from "ts-ui/helper";
import { DomHandler } from "primeng/dom";
import { PropCls } from "./interface";
const { notNull, isObject, isNumber, isString, parseFlex } = Objects;

export interface Breakpoint {

    /***>=576px */
    sm?: number;

    /**>=768px */
    md?: number;

    /**	992px */
    lg?: number;

    /**	>=1200px */
    xl?: number;

    /**	>=1600px */
    xxl?: number;

}

export interface EmbeddedProperty {
    span?: number;
    pull?: number;
    push?: number;
    offset?: number;
    order?: number;
    hide?: boolean;
}


const sizeNames: string[] = ['xs', 'sm', 'md', 'lg', 'xl', 'xxl'];
const namePrefixes: string[] = ['span', 'pull', 'push', 'offset', 'order'];
const isNumStr = (obj: any) => isString(obj) || isNumber(obj);



@Directive({
    standalone: true,
    selector: '[ts-col],ts-form-field',
    exportAs: 'tsCol',
    host: {
        '[style.flex]': 'hostFlexStyle',
        '[class.col-rtl]': 'dir === "rtl"',
    }
})
export class ColDirective implements OnInit, OnChanges, AfterViewInit, OnDestroy {

    /**	
     * flex layout style 
     * @group Props
     * */
    @Input() flex: string | number;

    /**	
     * the number of cells to offset Col from the left
     * @group Props
     * */
    @Input() offset: string | number;

    /**	
     * raster order
     * @group Props
     * */
    @Input() order: string | number;

    /**	
     * the number of cells that raster is moved to the left
     * @group Props
     * */
    @Input() pull: string | number;

    /**	
     * the number of cells that raster is moved to the right
     * @group Props
     * */
    @Input() push: string | number;

    /**	
     * raster number of cells to occupy, 0 corresponds to display: none
     * @group Props
     * */
    @Input() span: string | number;

    /**
     * <576px and also default setting, could be a span value or an object containing above props 
     * @group Props
     * */
    @Input() xs: string | number | EmbeddedProperty;

    /**
     * ≥576px, could be a span value or an object containing above props
     * @group Props
     * */
    @Input() sm: string | number | EmbeddedProperty;

    /**
     * ≥768px, could be a span value or an object containing above props
     * @group Props
     * */
    @Input() md: string | number | EmbeddedProperty;

    /**
     * ≥992px, could be a span value or an object containing above props
     * @group Props
     * */
    @Input() lg: string | number | EmbeddedProperty;

    /**
     * ≥1200px, could be a span value or an object containing above props
     * @group Props
     * */
    @Input() xl: string | number | EmbeddedProperty;

    /**
     * ≥1600px, could be a span value or an object containing above props
     * @group Props
     * */
    @Input() xxl: string | number | EmbeddedProperty;

    private classMap: { [key: string]: boolean } = {};
    private destroy$ = new Subject<boolean>();
    hostFlexStyle: string | null = null;
    dir: Direction = 'ltr';

    constructor(
        private elementRef: ElementRef,
        private renderer: Renderer2,
        private directionality: Directionality
    ) { }

    ngOnInit(): void {
        this.dir = this.directionality.value;
        this.hostFlexStyle = parseFlex(this.flex);
        this.setHostClassMap();
        this.directionality.change
            .pipe(takeUntil(this.destroy$))
            .subscribe(dir => this.dir = dir);
    }

    ngOnChanges(changes: SimpleChanges): void {
        this.setHostClassMap();

        const { flex } = changes;
        if (flex) this.hostFlexStyle = parseFlex(this.flex);
    }

    ngAfterViewInit(): void {
    }


    ngOnDestroy(): void {
        this.destroy$.next(true);
        this.destroy$.complete();
    }


    private setHostClassMap(): void {

        const hostClassMap = {
            [`ts-col`]: true,
            [`col-${this.span}`]: !!this.span,
            [`col-order-${this.order}`]: !!this.order,
            [`col-offset-${this.offset}`]: !!this.offset,
            [`col-pull-${this.pull}`]: !!this.pull,
            [`col-push-${this.push}`]: !!this.push,
        };


        sizeNames.filter(n => notNull(this[n])).forEach(name => {
            const sizeVal: string | number | EmbeddedProperty = this[name];
            if (isNumStr(sizeVal)) hostClassMap[`${name}:col-${sizeVal}`] = true;
            else namePrefixes.forEach(prefix => {
                const sizeVal2 = sizeVal[prefix];
                const cls = prefix === 'span' ? '-' : `-${prefix}-`;
               
                // col-hide
                hostClassMap[`${name}:col${cls}${sizeVal2}`] = notNull(sizeVal[prefix]);
            })
        });


        const element = this.elementRef.nativeElement;
        for (const i in this.classMap) {
            if (this.classMap.hasOwnProperty(i)) {
                this.renderer.removeClass(element, i);
            }
        }

        this.classMap = { ...hostClassMap };
        for (const i in this.classMap) {
            if (this.classMap.hasOwnProperty(i) && this.classMap[i]) {
                this.renderer.addClass(element, i);
            }
        }
    }

    private renderGutter = (name: string, gutter: number | null): void => {
        const nativeElement = this.elementRef.nativeElement;
        if (notNull(gutter)) { this.renderer.setStyle(nativeElement, name, `${gutter / 2}px`); }

    };

}