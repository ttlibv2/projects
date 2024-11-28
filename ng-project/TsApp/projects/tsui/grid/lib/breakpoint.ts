import { MediaMatcher } from '@angular/cdk/layout';
import { inject, Injectable,OnInit, OnDestroy, Inject } from '@angular/core';
import { Observable, Subject } from 'rxjs';
import { distinctUntilChanged, map, startWith, takeUntil } from 'rxjs/operators';
import { ResizeService } from 'ts-ui/common';
import { BreakpointBoolMap, BreakpointEnum, BreakpointMap, GRID_CONFIG, GridConfig } from './grid.interface';

@Injectable({providedIn: 'root'})
export class Breakpoint implements OnInit, OnDestroy {
    private destroy$ = new Subject<void>();
    private resizeService = inject(ResizeService);
    private mediaMatcher = inject(MediaMatcher);
    private gridConfig = inject(GRID_CONFIG);

    private responsiveMap: BreakpointMap;

    ngOnInit(): void {
        this.responsiveMap = this.gridConfig.responsiveMap!;
        this.resizeService.subscribe()
            .pipe(takeUntil(this.destroy$))
            .subscribe(() => { });
    }

    subscribe(breakpointMap: BreakpointMap): Observable<BreakpointEnum>;
    subscribe(breakpointMap: BreakpointMap, fullMap: true): Observable<BreakpointBoolMap>;
    subscribe(breakpointMap: BreakpointMap, fullMap?: true): Observable<BreakpointEnum | BreakpointBoolMap> {
        if (fullMap) {
            // eslint-disable-next-line @typescript-eslint/explicit-function-return-type
            const get = () => this.matchMedia(breakpointMap, true);
            return this.resizeService.subscribe().pipe(
                map(get),startWith(get()),
                distinctUntilChanged( (
                        x: [BreakpointEnum, BreakpointBoolMap], 
                        y: [BreakpointEnum, BreakpointBoolMap]) => x[0] === y[0]
                ),
                map(x => x[1])
            );
        } else {
            // eslint-disable-next-line @typescript-eslint/explicit-function-return-type
            const get = () => this.matchMedia(breakpointMap);
            return this.resizeService.subscribe().pipe(map(get), startWith(get()), distinctUntilChanged());
        }
    }

    private matchMedia(breakpointMap: BreakpointMap): BreakpointEnum;
    private matchMedia(breakpointMap: BreakpointMap, fullMap: true): [BreakpointEnum, BreakpointBoolMap];
    private matchMedia(
        breakpointMap: BreakpointMap,
        fullMap?: true
    ): BreakpointEnum | [BreakpointEnum, BreakpointBoolMap] {
        let bp = BreakpointEnum.md;

        const breakpointBooleanMap: Partial<BreakpointBoolMap> = {};

        Object.keys(breakpointMap).map(breakpoint => {
            const castBP = breakpoint as BreakpointEnum;
            const matched = this.mediaMatcher.matchMedia(this.responsiveMap[castBP]).matches;
            breakpointBooleanMap[breakpoint as BreakpointEnum] = matched;

            if (matched) {
                bp = castBP;
            }
        });

        if (fullMap) {
            return [bp, breakpointBooleanMap as BreakpointBoolMap];
        } else {
            return bp;
        }
    }

    ngOnDestroy(): void {
        this.destroy$.next();    
    }

}