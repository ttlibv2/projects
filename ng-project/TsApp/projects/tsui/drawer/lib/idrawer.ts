import { StringTemplate } from "ts-ui/common";

export type DrawerPos = 'left' | 'right' | 'top' | 'bottom' | 'full';
export type DrawerMode = 'side' | 'modal';

export interface HeaderContext {
    closable?: boolean;
    headerTitle?: StringTemplate;
    headerExtra?: StringTemplate;
    closeIcon?: StringTemplate;
}