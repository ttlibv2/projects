import { EnvironmentProviders, inject, makeEnvironmentProviders, provideAppInitializer } from "@angular/core";
import { AG_CONFIG_TOKEN, AgTableConfig, defaultConfig } from "./ag-table.config";
import { Objects } from "ts-ui/helper";

export function providePrimeNG(config?: AgTableConfig[]): EnvironmentProviders {
    const newConfig = Objects.mergeDeep(defaultConfig, config);
    const provider =  {provide: AG_CONFIG_TOKEN, useValue: newConfig};
    return makeEnvironmentProviders([provider]);
}