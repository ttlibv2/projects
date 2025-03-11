import { CommonModule } from "@angular/common";
import { ModuleWithProviders, NgModule } from "@angular/core";
import { Breakpoint } from "./breakpoint";
import { GridConfig } from "./grid.interface";

@NgModule({
    providers: [CommonModule]
})
export class GridModule {

    static forRoot(config: GridConfig): ModuleWithProviders<GridModule> {

        return {

            ngModule: GridModule,
            providers: [
                Breakpoint
            ]
        }
    }
}