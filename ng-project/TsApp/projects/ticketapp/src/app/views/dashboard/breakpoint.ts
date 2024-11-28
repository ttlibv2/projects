
import { MediaMatcher } from '@angular/cdk/layout';
import { effect, inject, Inject, Injectable, OnDestroy, Renderer2, RendererFactory2 } from '@angular/core';
import { Observable, Subject } from 'rxjs';
import { distinctUntilChanged, map, startWith, takeUntil } from 'rxjs/operators';

import { NzResizeService } from './resize';
import { DOCUMENT } from '@angular/common';
import { Objects } from 'ts-ui/helper';
import { DomHandler } from 'ts-ui/common';

export enum NzBreakpointEnum {
    x4l = 'x4l',
    x3l = 'x3l',
    x2l = 'x2l',
    xl = 'xl',
    lg = 'lg',
    md = 'md',
    sm = 'sm',
    xs = 'xs'
}

export type BreakpointMap = { [key in NzBreakpointEnum]: string };
export type BreakpointBooleanMap = { [key in NzBreakpointEnum]: boolean };
export type NzBreakpointKey = keyof typeof NzBreakpointEnum;

export const gridResponsiveMap: BreakpointMap = {
    xs: '(max-width: 575px)',
    sm: '(min-width: 576px)',
    md: '(min-width: 768px)',
    lg: '(min-width: 992px)',
    xl: '(min-width: 1200px)',
    x2l: '(min-width: 1400px)',
    x3l: '(min-width: 1600px)',
    x4l: '(min-width: 1900px)'
};

export const siderResponsiveMap: BreakpointMap = {
    xs: '(max-width: 479.98px)',
    sm: '(max-width: 575.98px)',
    md: '(max-width: 767.98px)',
    lg: '(max-width: 991.98px)',
    xl: '(max-width: 1199.98px)',
    x2l: '(max-width: 1399.98px)',
    x3l: '(max-width: 1599.98px)',
    x4l: '(max-width: 1899.98px)'
};

@Injectable({
    providedIn: 'root'
})
export class NzBreakpointService implements OnDestroy {
    private destroy$ = new Subject<void>();
    private document = inject(DOCUMENT);
    private renderer: Renderer2;
    private clazz: string = '';

    constructor(
        private rendererFactory: RendererFactory2,
        private resizeService: NzResizeService,
        private mediaMatcher: MediaMatcher) {
            this.renderer = rendererFactory.createRenderer(null, null);
            this.updateGrid();

        this.resizeService
            .subscribe()
            .pipe(takeUntil(this.destroy$))
            .subscribe(() => { });

        this.subscribe(gridResponsiveMap)
            .pipe(takeUntil(this.destroy$))
            .subscribe(bp => {
                let el = this.document.documentElement;
                DomHandler.removeMultipleClasses(el, this.clazz);

                this.clazz = `breakpoint-${bp}`;
                DomHandler.addMultipleClasses(el, this.clazz);
            });
    }

    private updateGrid(): void {
        console.log(Object.keys(gridResponsiveMap))
        
        const lines: string[] = Object.keys(gridResponsiveMap).map(key => {
            let vl: string = gridResponsiveMap[key];
            let cssKey = `--grid-bpw-${key}`;
            let cssValue: any = vl.includes('max-width') ? 0 : vl.substring(vl.indexOf(':')).replace(')', '').trim();
            return  `${cssKey}: ${cssValue};`
        });

        const headTag = this.document.getElementsByTagName('head')[0];
        const styleTag = this.document.createElement("style");
        headTag.appendChild(styleTag);
        styleTag.innerHTML = `:root { ${lines.join(';')}}`;

    }

    ngOnDestroy(): void {
        this.destroy$.next();
    }

    subscribe(breakpointMap: BreakpointMap): Observable<NzBreakpointEnum>;
    subscribe(breakpointMap: BreakpointMap, fullMap: true): Observable<BreakpointBooleanMap>;
    subscribe(breakpointMap: BreakpointMap, fullMap?: true): Observable<NzBreakpointEnum | BreakpointBooleanMap> {
        if (fullMap) {
            // eslint-disable-next-line @typescript-eslint/explicit-function-return-type
            const get = () => this.matchMedia(breakpointMap, true);
            return this.resizeService.subscribe().pipe(
                map(get),
                startWith(get()),
                distinctUntilChanged(
                    (x: [NzBreakpointEnum, BreakpointBooleanMap], y: [NzBreakpointEnum, BreakpointBooleanMap]) => x[0] === y[0]
                ),
                map(x => x[1])
            );
        } else {
            // eslint-disable-next-line @typescript-eslint/explicit-function-return-type
            const get = () => this.matchMedia(breakpointMap);
            return this.resizeService.subscribe().pipe(map(get), startWith(get()), distinctUntilChanged());
        }
    }

    private matchMedia(breakpointMap: BreakpointMap): NzBreakpointEnum;
    private matchMedia(breakpointMap: BreakpointMap, fullMap: true): [NzBreakpointEnum, BreakpointBooleanMap];
    private matchMedia(
        breakpointMap: BreakpointMap,
        fullMap?: true
    ): NzBreakpointEnum | [NzBreakpointEnum, BreakpointBooleanMap] {
        let bp = NzBreakpointEnum.md;

        const breakpointBooleanMap: Partial<BreakpointBooleanMap> = {};

        Object.keys(breakpointMap).map(breakpoint => {
            const castBP = breakpoint as NzBreakpointEnum;
            const matched = this.mediaMatcher.matchMedia(gridResponsiveMap[castBP]).matches;

            breakpointBooleanMap[breakpoint as NzBreakpointEnum] = matched;

            if (matched) {
                bp = castBP;
            }
        });

        if (fullMap) {
            return [bp, breakpointBooleanMap as BreakpointBooleanMap];
        } else {
            return bp;
        }
    }
}