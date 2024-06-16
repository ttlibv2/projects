import {Component, Input, ViewEncapsulation} from '@angular/core';
import {MenuService} from "../../services/menu.service";
import {LayoutService} from "../../services/layout.service";

export type MenuMode = 'static' | 'overlay' | 'slim' | 'slim2' | 'drawer' | 'reveal' | 'horizontal';

export interface MenuType {
  label: string;
  mode: MenuMode;
}

@Component({
  selector: 'app-config',
  templateUrl: './app-config.component.html',
  styleUrl: './app-config.component.scss',
  encapsulation: ViewEncapsulation.None
})
export class AppConfigComponent {
  @Input() minimal: boolean = false;

  scales: number[] = [12, 13, 14, 15, 16];
  menuTypes: MenuType[] = [
    { label: 'Static', mode: 'static'},
    { label: 'Overlay',mode: 'overlay'},
    { label: 'Slim', mode: 'slim'},
    { label: 'Slim +', mode: 'slim2'},
    { label: 'Reveal', mode: 'reveal'},
    { label: 'Drawer', mode: 'drawer'},
    { label: 'Horizontal', mode: 'horizontal'},
  ];

  constructor(
    public layoutService: LayoutService,
    public menuService: MenuService) {}

  get visible(): boolean {
    return this.layoutService.state.configSidebarVisible;
  }

  set visible(_val: boolean) {
    this.layoutService.state.configSidebarVisible = _val;
  }

  get scale(): number {
    return this.layoutService.config().scale;
  }
  set scale(_val: number) {
    this.layoutService.config.update((config) => ({
      ...config,
      scale: _val,
    }));
  }

  get menuMode(): string {
    return this.layoutService.config().menuMode;
  }

  set menuMode(_val: string) {
    this.layoutService.config.update((config) => ({
      ...config,
      menuMode: _val,
    }));
  }

  get inputStyle(): string {
    return this.layoutService.config().inputStyle;
  }
  set inputStyle(_val: string) {
    this.layoutService.config().inputStyle = _val;
  }

  get ripple(): boolean {
    return this.layoutService.config().ripple;
  }
  set ripple(_val: boolean) {
    this.layoutService.config.update((config) => ({
      ...config,
      ripple: _val,
    }));
  }

  set theme(val: string) {
    this.layoutService.config.update((config) => ({
      ...config,
      theme: val,
    }));
  }
  get theme(): string {
    return this.layoutService.config().theme;
  }

  set colorScheme(val: string) {
    this.layoutService.config.update((config) => ({
      ...config,
      colorScheme: val,
    }));
  }
  get colorScheme(): string {
    return this.layoutService.config().colorScheme;
  }

  onConfigButtonClick() {
    this.layoutService.showConfigSidebar();
  }

  changeTheme(theme: string, colorScheme: string) {
    this.theme = theme;
    this.colorScheme = colorScheme;
  }

  decrementScale() {
    this.scale--;
  }

  incrementScale() {
    this.scale++;
  }
}
