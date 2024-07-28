import {ModuleWithProviders, NgModule} from '@angular/core';
import { CommonModule } from '@angular/common';
import {DB_CONFIG_TOKEN, DEFAULT_DB_CONFIG, LocalDbConfig} from "./localdb.config";
import {Objects} from "ts-ui/helper";
import {LocalDbService} from "./localdb.service";

@NgModule({
  declarations: [],
  imports: [
    CommonModule
  ]
})
export class LocaldbModule {

  static forRoot(config: Partial<LocalDbConfig>):ModuleWithProviders<LocaldbModule> {
    const dbConfig = Objects.mergeDeep(DEFAULT_DB_CONFIG, config);
    return {
      ngModule: LocaldbModule,
      providers: [
        {provide:DB_CONFIG_TOKEN, useValue: dbConfig},
        LocalDbService
      ]
    }
  }
}
