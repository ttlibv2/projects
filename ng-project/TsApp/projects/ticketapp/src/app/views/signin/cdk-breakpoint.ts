import { MediaMatcher } from "@angular/cdk/layout";
import { inject, Injectable, OnDestroy } from "@angular/core";
import { ResizeObserver } from "./cdk-resize";
import { distinctUntilChanged, map, Observable, startWith, Subject, takeUntil } from "rxjs";
import { Objects } from "ts-ui/helper";
import { B } from "@angular/cdk/keycodes";
const { isNumber, isString, isObject } = Objects;

// 576 | 576px | {mql: (min-width: 576px)}
export type BPValue = number | string | { mql: string };

export interface Breakpoint {
    xs?: BPValue;
    sm?: BPValue;
    md?: BPValue;
    lg?: BPValue;
    xl?: BPValue;
    x2l?: BPValue;
    x3l?: BPValue;
    x4l?: BPValue;
}

export interface BreakpointState {

}

@Injectable({ providedIn: 'root' })
export class BreakpointService implements OnDestroy {
    private _resize = inject(ResizeObserver);
    private _mediaMatcher = inject(MediaMatcher);
    private _destroy$ = new Subject<void>();

    constructor() {
        this._resize.observe()
            .pipe(takeUntil(this._destroy$))
            .subscribe(() => { });
    }

    observe(breakpoints: Breakpoint): Observable<any> {
        const get = () => this.matchMedia(breakpoints);
        return this._resize.observe().pipe(
            map(get), startWith(get()),
            distinctUntilChanged( (x: [string, {}],  y: [string, {}]) => x[0] === y[0]),
            map(x => x[1])
        );
    }

    ngOnDestroy(): void {
        this._destroy$.next();
        this._destroy$.complete();
    }

    private matchMedia(breakpoints: Breakpoint) {
        let bp: string = 'md';
        const breakpointState = {};
        Object.keys(breakpoints).forEach(breakpoint => {
            const query = this.getQuery(breakpoint, breakpoints[breakpoint]);
            const matches = this._mediaMatcher.matchMedia(query).matches;
            breakpointState[breakpoint] = matches;
            if (matches) bp = breakpoint;
        });
        return [bp, breakpointState];
    }

    private getQuery(bpName: string, value: BPValue): string {
        if (isNumber(value)) return `(min-width: ${value}px)`;
        else if (isString(value)) return value.includes('(') ? value : `(min-width: ${value})`;
        else if (isObject(value)) return value?.mql;
        else throw new Error(`The breakpoint ${bpName} not defined`);
    }



}