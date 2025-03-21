import {
    Directive,
    ElementRef,
    Input,
    OnChanges,
    Renderer2,
    AfterViewInit,
    SimpleChanges,
    OnDestroy,
    OnInit,
} from "@angular/core";
import {Objects} from "ts-ui/helper";
import {Subject, takeUntil} from "rxjs";
import {Direction, Directionality} from "@angular/cdk/bidi";
import {DomHandler} from "./domhandler";

const {notNull, isNumber, isString, parseFlex} = Objects;
const {addClass, removeClass} = DomHandler;

export interface Breakpoint {

    /***>=576px */
    sm?: number;

    /**>=768px */
    md?: number;

    /**    992px */
    lg?: number;

    /**    >=1200px */
    xl?: number;

    /**    >=1600px */
    x2l?: number;

    x3l?: number;

    x4l?: number;
}

export interface EmbeddedProperty {
    span?: number;
    pull?: number;
    push?: number;
    offset?: number;
    order?: number;
    hide?: boolean;
}

const sizeNames: string[] = ['xs', 'sm', 'md', 'lg', 'xl', 'xxl', 'x2l', 'x3l', 'x4l'];
const namePrefixes: string[] = ['span', 'pull', 'push', 'offset', 'order'];
const isNumStr = (obj: any) => isString(obj) || isNumber(obj);

export interface ColProps {
    /**
     * flex layout style
     * @group Props
     * */
    flex?: string | number;

    /**
     * the number of cells to offset Col from the left
     * @group Props
     * */
    offset?: string | number;

    /**
     * raster order
     * @group Props
     * */
    order?: string | number;

    /**
     * the number of cells that raster is moved to the left
     * @group Props
     * */
    pull?: string | number;

    /**
     * the number of cells that raster is moved to the right
     * @group Props
     * */
    push?: string | number;

    /**
     * raster number of cells to occupy, 0 corresponds to display?: none
     * @group Props
     * */
    span?: string | number;

    /**
     * <576px and also default setting, could be a span value or an object containing above props
     * @group Props
     * */
    xs?: string | number | EmbeddedProperty;

    /**
     * ≥576px, could be a span value or an object containing above props
     * @group Props
     * */
    sm?: string | number | EmbeddedProperty;

    /**
     * ≥768px, could be a span value or an object containing above props
     * @group Props
     * */
    md?: string | number | EmbeddedProperty;

    /**
     * ≥992px, could be a span value or an object containing above props
     * @group Props
     * */
    lg?: string | number | EmbeddedProperty;

    /**
     * ≥1200px, could be a span value or an object containing above props
     * @group Props
     * */
    xl?: string | number | EmbeddedProperty;

    /**
     * ≥1400px, could be a span value or an object containing above props
     * @group Props
     * */
    xxl?: string | number | EmbeddedProperty;

    /**
     * ≥1400px, could be a span value or an object containing above props
     * @group Props
     * */
    x2l?: string | number | EmbeddedProperty;

    /**
     * ≥1600px, could be a span value or an object containing above props
     * @group Props
     * */
    x3l?: string | number | EmbeddedProperty;

    /**
     * ≥1900px, could be a span value or an object containing above props
     * @group Props
     * */
    x4l?: string | number | EmbeddedProperty;
}

@Directive({
    standalone: true,
    selector: '[tsCol],ts-form-field,ts-card,div',
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
     * ≥1400px, could be a span value or an object containing above props
     * @group Props
     * */
    @Input() xxl: string | number | EmbeddedProperty;

    /**
     * ≥1400px, could be a span value or an object containing above props
     * @group Props
     * */
    @Input() x2l: string | number | EmbeddedProperty;

    /**
     * ≥1600px, could be a span value or an object containing above props
     * @group Props
     * */
    @Input() x3l: string | number | EmbeddedProperty;

    /**
     * ≥1900px, could be a span value or an object containing above props
     * @group Props
     * */
    @Input() x4l: string | number | EmbeddedProperty;

    private classMap: { [key: string]: boolean } = {};
    private destroy$ = new Subject<boolean>();
    hostFlexStyle: string | null = null;
    dir: Direction = 'ltr';

    constructor(
        private elementRef: ElementRef,
        private renderer: Renderer2,
        private directionality: Directionality
    ) {
    }

    update(props: ColProps): void {
        if(Objects.notEmpty(props)) {
            Object.assign(this, props);
            this.setHostClassMap('flex' in props);
        }
    }

    ngOnInit(): void {
        this.dir = this.directionality.value;
        this.hostFlexStyle = parseFlex(this.flex);
        this.setHostClassMap();
        this.directionality.change
            .pipe(takeUntil(this.destroy$))
            .subscribe(dir => this.dir = dir);
    }

    ngOnChanges(changes: SimpleChanges): void {
        const {flex} = changes;
        this.setHostClassMap(!!flex);
    }

    ngAfterViewInit(): void {
    }


    ngOnDestroy(): void {
        this.destroy$.next(true);
        this.destroy$.complete();
    }


    private setHostClassMap(hasFlex: boolean = false): void {

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

        if(!!hasFlex) {
            this.hostFlexStyle = parseFlex(this.flex);
        }

        const element = this.elementRef.nativeElement;
        for (const i in this.classMap) {
            if (this.classMap.hasOwnProperty(i)) {
                this.renderer.removeClass(element, i);
            }
        }

        this.classMap = {...hostClassMap};
        for (const i in this.classMap) {
            if (this.classMap.hasOwnProperty(i) && this.classMap[i]) {
                this.renderer.addClass(element, i);
            }
        }
    }

    private renderGutter = (name: string, gutter: number | null): void => {
        const nativeElement = this.elementRef.nativeElement;
        if (notNull(gutter)) {
            this.renderer.setStyle(nativeElement, name, `${gutter / 2}px`);
        }

    };

}

@Directive({
    standalone: true,
    selector: '[grid],.grid,.row',
    exportAs: 'grid',
    host: {
        '[class.grid]': 'true',
        '[class.row]': 'true',
    }
})
export class GridDirective implements OnChanges {

    /**
     * Define gutter-x for grid
     * */
    @Input() gutterX: string;

    /**
     * Define gutter-y for grid
     * */
    @Input() gutterY: string;

    /**
     * Define container-type for grid
     * */
    @Input() containerType: 'size' | 'inline-size' | undefined;

    constructor(private elementRef: ElementRef) {
    }

    ngOnChanges(changes: SimpleChanges): void {
        const {gutterX, gutterY, containerType} = changes;
        if (gutterX) this.setStyle('--gutter-grx', this.gutterX);
        if (gutterY) this.setStyle('--gutter-gry', this.gutterY);
        if (containerType) {
            this.setStyle('container-type', this.containerType);
            this.addRemoveClass(!!this.containerType, 'container-inline-size');
        }

    }

    setStyle(cssName: string, cssValue: any): void {
        Objects.setStyle(this.elementRef.nativeElement, cssName, cssValue, 'important');
    }

    addRemoveClass(hasAdd: boolean, cls: string) {
        const el = this.elementRef.nativeElement;
        if (!!hasAdd) addClass(el, cls);
        else removeClass(el, cls);
    }
}
