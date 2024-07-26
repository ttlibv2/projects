import {ModuleWithProviders, NgModule} from "@angular/core";
import {RouterModule} from "@angular/router";
import {CommonModule} from "@angular/common";
import {Objects} from 'ts-ui/helper';
import {AppLayoutComponent} from "./layout.component";
import {AppBreadcrumbModule} from "./app-breadcrumb/app-breadcrumb.module";
import {AppFooterModule} from "./app-footer/app-footer.module";
import {AppConfigModule} from "./app-config/app-config.module";
import {AppMenuModule} from "./app-menu/app-menu.module";
import {AppTopbarModule} from "./app-topbar/app-topbar.module";
import {AppSidebarModule} from "./app-sidebar/app-sidebar.module";
import {AppRightmenuModule} from "./app-rightmenu/app-rightmenu.module";
import {defaultLayoutConfig, LayoutService} from "./services/layout.service";
import {MenuService} from "./services/menu.service";
import {StyleManager} from "./services/theme.service";
import {LAYOUT_CONFIG, LayoutConfig} from "./layout.common";

@NgModule({
    declarations: [AppLayoutComponent],
    exports: [AppLayoutComponent],
    imports: [
        CommonModule,
        RouterModule,
        AppBreadcrumbModule,
        AppFooterModule,
        AppConfigModule,
        AppMenuModule,
        AppTopbarModule,
        AppSidebarModule,
        AppRightmenuModule,
    ]
})
export class AppLayoutModule {

    static forRoot(config: LayoutConfig = {}): ModuleWithProviders<AppLayoutModule> {
        const newConfig = Objects.mergeDeep({...defaultLayoutConfig}, config);
        return {
            ngModule: AppLayoutModule,
            providers: [
                {provide: LAYOUT_CONFIG, useValue: newConfig},
                LayoutService, MenuService, StyleManager
            ],
        };
    }
}