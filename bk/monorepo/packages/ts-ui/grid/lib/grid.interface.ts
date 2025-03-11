import { InjectionToken } from "@angular/core";

export enum BreakpointEnum {
    x3l = 'x3l',
    x2l = 'x2l',
    xl = 'xl',
    lg = 'lg',
    md = 'md',
    sm = 'sm',
    xs = 'xs'
}

export type BreakpointMap = { [key in BreakpointEnum]: string };
export type BreakpointNumber = { [key in BreakpointEnum]: number };
export type BreakpointBoolMap = { [key in BreakpointEnum]: boolean };
export type BreakpointKey = keyof typeof BreakpointEnum;

export interface GridConfig {
    gridNumber?: BreakpointMap,
    responsiveMap?: BreakpointMap;
}

export const DEFAULT_GRID_NUM: BreakpointNumber = {
    xs: 0, sm: 576, md: 768, lg: 1200, xl: 1400, x2l: 1600, x3l: 1900
};

export const GRID_RESPONSIVE = new InjectionToken<BreakpointMap>('GRID_RESPONSIVE');
export const GRID_CONFIG = new InjectionToken<GridConfig>('GRID_CONFIG');