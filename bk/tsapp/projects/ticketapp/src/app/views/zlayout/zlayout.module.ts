import { NgModule } from "@angular/core";
import { AppLayout } from "./app-layout";
import { AppHeader } from "./app-header";

@NgModule({
    imports: [AppLayout, AppHeader],
    exports: [AppLayout, AppHeader],
})
export class LayoutModule {}