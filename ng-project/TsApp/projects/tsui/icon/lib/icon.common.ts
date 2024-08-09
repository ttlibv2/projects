import { InjectionToken } from "@angular/core";
import { Severity } from "ts-ui/common";

export const ICON_CONFIG_TOKEN = new InjectionToken<IconConfig>('ICON_CONFIG_TOKEN');

export const DEFAULT_CONFIG: Partial<IconConfig> = {
    size: '1rem',
    color: '#000000',
    spin: false,
    severity: 'primary'

};
export interface IconConfig {
    size: string;
    color: string;
    spin: boolean;
    severity: Severity;
}
