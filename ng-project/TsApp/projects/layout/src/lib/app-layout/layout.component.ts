import { Component } from '@angular/core';
import { LayoutService } from '../services/layout.service';

@Component({
  selector: 'ts-app-layout',
  templateUrl: './layout.component.html'
})
export class AppLayoutComponent {

  constructor(public layoutService: LayoutService) { }

  get visibleSideBar(): boolean {
    return this.layoutService.config().visibleSideBar;
  }

  get visibleFooter(): boolean {
    return this.layoutService.config().visibleFooter;
  }

  get visibleConfig(): boolean {
    return this.layoutService.config().visibleConfig;
  }

  get visibleBreadcrumb(): boolean {
    return this.layoutService.config().visibleBreadcrumb;
  }

  get layoutStaticInActive(): boolean {
    const { menuMode, visibleSideBar } = this.layoutService.config();
    const staticMenuDesktopInactive = this.layoutService.state.staticMenuDesktopInactive;
    return (staticMenuDesktopInactive && "static" === menuMode) || visibleSideBar !== true;
  }

  get containerClass(): any {

    return {
      "layout-overlay": "overlay" === this.layoutService.config().menuMode,
      "layout-static": "static" === this.layoutService.config().menuMode,
      "layout-slim": "slim" === this.layoutService.config().menuMode,
      "layout-slim-plus": "slim-plus" === this.layoutService.config().menuMode,
      "layout-horizontal": "horizontal" === this.layoutService.config().menuMode,
      "layout-reveal": "reveal" === this.layoutService.config().menuMode,
      "layout-drawer": "drawer" === this.layoutService.config().menuMode,
      "p-input-filled": "filled" === this.layoutService.config().inputStyle,
      "p-ripple-disabled": !this.layoutService.config().ripple,
      "layout-static-inactive": this.layoutStaticInActive,

      "layout-overlay-active": this.layoutService.state.overlayMenuActive,
      "layout-mobile-active": this.layoutService.state.staticMenuMobileActive,
      "layout-topbar-menu-active": this.layoutService.state.topbarMenuActive,
      "layout-menu-profile-active": this.layoutService.state.menuProfileActive,
      "layout-sidebar-active": this.layoutService.state.sidebarActive,
      "layout-sidebar-anchored": this.layoutService.state.anchored,
      ["layout-topbar-" + this.layoutService.config().topbarTheme]: true,
      ["layout-menu-" + this.layoutService.config().menuTheme]: true,
      ["layout-menu-profile-" + this.layoutService.config().menuProfilePosition]: true
    };
  }
}
