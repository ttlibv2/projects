import {ModuleWithProviders, NgModule} from "@angular/core";
import {DB_CONFIG_TOKEN, DBConfig, DEFAULT_DB_CONFIG} from "./local.config";
import {Objects} from "ts-ui/helper";
import {DBService} from "./local.service";

@NgModule({

})
export class LocalDbModule {
    static forRoot(config?:Partial<DBConfig>):ModuleWithProviders<LocalDbModule> {
        const newConfig = Objects.mergeDeep({...DEFAULT_DB_CONFIG}, config);
        return {
            ngModule: LocalDbModule,
            providers: [
                {provide: DB_CONFIG_TOKEN, useValue: newConfig},
                DBService
            ]
        }
    }
}