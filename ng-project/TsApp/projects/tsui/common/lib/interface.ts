/** 
 * use type [INgClass]
 * @deprecated 
 * */
export type PropCls = string | string[] | Set<string> | {[klass: string]: any; };

/**
 * use type [INgStyle]
 * @deprecated 
 * */
export type StyleCls = {[name: string]: any};


export type INgClass = string | string[] | Set<string> | { [klass: string]: any; };
export type INgStyle = { [name: string]: any };


export type Severity = "success" | "info" | "warning" | "primary" | "help" | "danger" | "secondary" | "contrast";
export type TooltipPos = 'right' | 'left' | 'top' | 'bottom';

export type JustifyContent = 'start' | 'end' | 'center' | 'space-around' | 'space-between' | 'space-evenly';
export type AlignItem = 'top' | 'middle' | 'bottom';