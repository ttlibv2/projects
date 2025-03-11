import { Directive, ElementRef, Inject, inject, Input, NgZone, OnChanges, OnInit, Renderer2, SimpleChange, SimpleChanges } from "@angular/core";
import { ReplaySubject, Subject, takeUntil } from "rxjs";
import { AlignItem, JustifyContent } from "ts-ui/common";
import { JsonAny } from "ts-ui/helper";
import { BreakpointKey, BreakpointMap, GRID_RESPONSIVE } from "./grid.interface";
import { Direction, Directionality } from "@angular/cdk/bidi";
import { Breakpoint } from "./breakpoint";
import { Platform } from "@angular/cdk/platform";
import { MediaMatcher } from "@angular/cdk/layout";

@Directive({
    standalone: true,
    selector: `[ts-row], ts-row, ts-form-item`,
    exportAs: 'tsRow',
    host: {
        '[class.grid]': 'true',
        '[class.ts-row]': 'true',
        '[class.ts-row-top]': `align === 'top'`,
        '[class.ts-row-middle]': `align === 'middle'`,
        '[class.ts-row-bottom]': `align === 'bottom'`,
        '[class.ts-row-start]': `align === 'start'`,
        '[class.ts-row-center]': `align === 'center'`,
        '[class.ts-row-end]': `align === 'end'`,
        '[class.ts-row-space-around]': `align === 'space-around'`,
        '[class.ts-row-space-between]': `align === 'space-between'`,
        '[class.ts-row-space-evenly]': `align === 'space-evenly'`,
        '[class.ts-row-rtl]': `dir === "rtl"`
        
    }
})
export class GridRow implements OnInit {

    /**
     * spacing between grids, could be a number or a object like { xs: 8, sm: 16, md: 24}. 
     * or you can use array to make horizontal and vertical spacing work at the same time [horizontal, vertical] 
     * @group Props
     * */
    @Input() gutter: string | number | JsonAny | [number, number] | [JsonAny, JsonAny];

    /**
     * The vertical alignment
     * @group Props
     * */
    @Input() align: AlignItem;

    /**
     * The horizontal alignment
     * @group Props
     * */
    @Input() justify: JustifyContent;

    private actualGutter$ = new ReplaySubject<[number | null, number | null]>(1);
    private destroy$ = new Subject<boolean>();
    private dir: Direction = 'ltr';

    constructor(
        public elementRef: ElementRef,
        public renderer: Renderer2,
        public mediaMatcher: MediaMatcher,
        public ngZone: NgZone,
        public platform: Platform,
        private breakpointService: Breakpoint,
        private directionality: Directionality,
        @Inject(GRID_RESPONSIVE)
        private responsiveMap: BreakpointMap
    ) { }

    ngOnInit(): void {
        this.dir = this.directionality.value;
    }


}