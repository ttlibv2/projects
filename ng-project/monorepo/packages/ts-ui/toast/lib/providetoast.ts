import { EnvironmentProviders, makeEnvironmentProviders } from "@angular/core";
import { provideToastr } from "ngx-toastr";
import { ToastService } from "./toast.service";
import { Objects } from "ts-ui/helper";
import { DEFAULT_CONFIG, TOAST_CONFIG, ToastConfig } from "./toast.common";

export function providePrimeNG(config: Partial<ToastConfig> = {}): EnvironmentProviders {
    const newConfig = Objects.mergeDeep({ ...DEFAULT_CONFIG }, config);
    return makeEnvironmentProviders([
        provideToastr(newConfig),
        { provide: TOAST_CONFIG, useValue: newConfig },
        ToastService
    ]);
}