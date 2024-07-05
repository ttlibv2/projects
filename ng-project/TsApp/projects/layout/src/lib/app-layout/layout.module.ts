import { ModuleWithProviders, NgModule } from "@angular/core";
import { CommonModule } from "@angular/common";
import { AppLayoutComponent } from "./layout.component";
import { AppBreadcrumbModule } from "../app-breadcrumb/app-breadcrumb.module";
import { AppFooterModule } from "../app-footer/app-footer.module";
import { RouterModule } from "@angular/router";
import { AppConfigModule } from "../app-config/app-config.module";
import { AppMenuModule } from "../app-menu/app-menu.module";
import { AppTopbarModule } from "../app-topbar/app-topbar.module";
import { AppSidebarModule } from "../app-sidebar/app-sidebar.module";
import { AppRightmenuModule } from "../app-rightmenu/app-rightmenu.module";
import { LayoutService } from "../services/layout.service";
import { MenuService } from "../services/menu.service";
import { StyleManager  } from "../services/theme.service";

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
  ],
  providers: [
    //LayoutService,
    // MenuService
  ],
})
export class AppLayoutModule {
  static forRoot(): ModuleWithProviders<AppLayoutModule> {
    return {
      ngModule: AppLayoutModule,
      providers: [LayoutService, MenuService, StyleManager ],
    };
  }
}
