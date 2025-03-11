import { ModuleWithProviders, NgModule } from "@angular/core";
import { ToastrModule} from "ngx-toastr";
import { CommonModule } from "@angular/common";
import { ToastService } from "./toast.service";
import {Objects} from "ts-ui/helper";
import {DEFAULT_CONFIG, TOAST_CONFIG, ToastConfig} from "./toast.common";

@NgModule({
    imports: [
        CommonModule,
        ToastrModule
    ]
})
export class ToastModule {

    static forRoot(config: Partial<ToastConfig> = {}): ModuleWithProviders<ToastrModule> {
        const newConfig = Objects.mergeDeep({...DEFAULT_CONFIG}, config);
        const newModule = ToastrModule.forRoot(newConfig);

        return {
            ngModule: ToastrModule,
            providers: [
                ...newModule.providers,
                { provide: TOAST_CONFIG, useValue: newConfig},
                ToastService
            ]
        }
    }
}