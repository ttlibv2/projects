import { InjectionToken } from "@angular/core";
import { AgI18N, DEFAULT_I18N } from "./ag-table.i18n";

export const AG_CONFIG_TOKEN = new InjectionToken<AgTableConfig>('AG_CONFIG_TOKEN');
// export const AG_I18N_TOKEN = new InjectionToken<AgI18N>('AG_I18N_TOKEN');

export interface AgTableConfig {
    i18n?: AgI18N;
}

export const defaultConfig: AgTableConfig = {
    i18n: DEFAULT_I18N
}

