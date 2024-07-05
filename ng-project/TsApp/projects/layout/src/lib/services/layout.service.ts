import { Inject, Injectable, effect, signal } from "@angular/core";
import { AppConfig, LayoutState, defaultLayoutState } from "../common";
import { Subject } from "rxjs";
import { DOCUMENT } from "@angular/common";
import { Objects } from "ts-helper";
import { StyleManager } from "./theme.service";

const { isNull, notNull } = Objects;

export const defaultAppConfig: AppConfig = {
  ripple: true,
  inputStyle: "outlined",
  menuMode: "static",

  componentTheme: "light",
  scale: 14,
  menuTheme: "light",
  topbarTheme: "white",
  menuProfilePosition: "end",

  theme: "aura-lime",
  colorScheme: "light",

  elThemeId: "theme-link",
  themeUrlPrefix: "/assets/themes",
};

@Injectable()
export class LayoutService {
 
  private _config: AppConfig = { ...defaultAppConfig };
  private _configUpdate = new Subject<AppConfig>();
  private _overlayOpen = new Subject<any>();
  private _topbarMenuOpen = new Subject<any>();
  private _menuProfileOpen = new Subject<any>();

  config = signal<AppConfig>(this._config);
  state: LayoutState = { ...defaultLayoutState };

  configUpdate$ = this._configUpdate.asObservable();
  overlayOpen$ = this._overlayOpen.asObservable();
  topbarMenuOpen$ = this._topbarMenuOpen.asObservable();
  menuProfileOpen4 = this._menuProfileOpen.asObservable();

  constructor(
    private style: StyleManager,

    @Inject(DOCUMENT) 
    private document: Document) {
    effect(() => {
      const config = this.config();
      if (this.updateStyle(config)) {
        this.changeTheme();
      }
      this.changeScale(config.scale);
      this.onConfigUpdate();
    });
  }

  updateStyle(config: AppConfig) {
    return (
      config.theme !== this._config.theme ||
      config.colorScheme !== this._config.colorScheme ||
      config.componentTheme !== this._config.componentTheme
    );
  }

  tryAddTheme(): void {
    const themeLinkEl = this.document.getElementById(this.config().elThemeId);
    if(isNull(themeLinkEl))   this.changeTheme()
  }

  changeScale(value: number): void {
    this.document.documentElement.style.fontSize = `${value}px`;
  }

  changeTheme() {
    const { theme, colorScheme, elThemeId, themeUrlPrefix } = this.config();
    this.document.getElementById(elThemeId)?.remove();
    
    if(notNull(theme) && notNull(colorScheme)) {
      const themeUrl = `${themeUrlPrefix}${theme}-${colorScheme}.css`;
      this.style.setStyle(elThemeId, themeUrl);

      //const themeUrl = this._config.themeUrlPrefix + '/' + theme + '/' + colorScheme + '/theme.css';
      //const linkEl = Objects.createElement(this.document, 'link', 'id', elThemeId,   'rel', 'stylesheet',  'type', 'text/css',  'href', themeUrl);
      //this.document.getElementsByTagName('body').item(0).appendChild(linkEl);
    }
  }

  onMenuToggle() {
    if (this.isOverlay()) {
      this.state.overlayMenuActive = !this.state.overlayMenuActive;
      if (this.state.overlayMenuActive) {
        this._overlayOpen.next(null);
      }
    }

    if (this.isDesktop()) {
      this.state.staticMenuDesktopInactive =
        !this.state.staticMenuDesktopInactive;
    } else {
      this.state.staticMenuMobileActive = !this.state.staticMenuMobileActive;

      if (this.state.staticMenuMobileActive) {
        this._overlayOpen.next(null);
      }
    }
  }

  onTopbarMenuToggle() {
    this.state.topbarMenuActive = !this.state.topbarMenuActive;
    if (this.state.topbarMenuActive) {
      this._topbarMenuOpen.next(null);
    }
  }

  onOverlaySubmenuOpen() {
    this._overlayOpen.next(null);
  }

  openRightSidebar() {
    this.state.rightMenuActive = true;
  }

  onMenuProfileToggle() {
    this.state.menuProfileActive = !this.state.menuProfileActive;
    if (
      this.state.menuProfileActive &&
      this.isHorizontal() &&
      this.isDesktop()
    ) {
      this._menuProfileOpen.next(null);
    }
  }

  onConfigUpdate() {
    this._config = { ...this.config() };
    this._configUpdate.next(this.config());
  }

  layoutConfig(config: Partial<AppConfig>): void {
    this.config.update(json => ({...json, ...config}));
  }

  showConfigSidebar() {
    this.state.configSidebarVisible = true;
  }

  toggleMenuHoverActive(): void {
    this.state.menuHoverActive = !this.state.menuHoverActive;
  }
  
  toggleAnchor() {
    this.state.anchored = !this.state.anchored
  }

  isOverlay() {
    return "overlay" === this.config().menuMode;
  }

  isDesktop() {
    return window.innerWidth > 991;
  }

  isSlim() {
    return "slim" === this.config().menuMode;
  }

  isSlimPlus() {
    return "slim-plus" === this.config().menuMode;
  }

  isHorizontal() {
    return "horizontal" === this.config().menuMode;
  }

  isSlimOrHorizontal(): boolean {
    return this.isSlim() || this.isSlimPlus() || this.isHorizontal();
  }

  isMobile() {
    return !this.isDesktop();
  }

  isRightMenuActive() {
    return this.state.rightMenuActive;
  }

  isVisibleToggleMenuButton(): boolean {
    const {visibleSideBar, menuMode} = this.config();
    return visibleSideBar && (menuMode === 'static' || menuMode === 'overlay');
  }

  isMenuDrawer(): boolean {
    return this.config().menuMode === 'drawer';
}
}
