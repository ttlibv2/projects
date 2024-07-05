import { Component, Input, OnInit } from "@angular/core";
import { LayoutService } from "../services/layout.service";
import { MenuService } from "../services/menu.service";
import {
  AppConfig,
  ComponentThemeLabel,
  IColorScheme,
  InputStyle,
  LayoutState,
  MenuMode,
  MenuModeLabel,
  MenuPosition,
  MenuThemeLabel,
  ThemeInfo,
  TopbarThemeLabel,
  allMenuModeLabel,
  allMenuThemeLabel,
  allTheme,
  allTopbarThemeLabel,
} from "../common";
import { Objects } from "ts-helper";

const {isNull, isEmpty} = Objects;

@Component({
  selector: "ts-app-config",
  templateUrl: "./app-config.component.html",
})
export class AppConfigComponent implements OnInit {
  @Input() minimal: boolean = false;
  @Input() scales: number[] = [12, 13, 14, 15, 16, 17, 18];
  @Input() componentThemes: ComponentThemeLabel[] = [];
  @Input() menuModes: MenuModeLabel[] = allMenuModeLabel;
  @Input() menuThemes: MenuThemeLabel[] = allMenuThemeLabel;
  @Input() topbarThemes: TopbarThemeLabel[] = allTopbarThemeLabel;

  //--
  allTheme: Record<string, ThemeInfo> = this.config.listTheme ?? {};

  private get state(): LayoutState {
    return this.layoutService.state;
  }

  private get config(): AppConfig {
    return this.layoutService.config();
  }

  private updateLayoutConfig(data: any) {
    this.layoutService.config.update((cfg) => ({ ...cfg, ...data }));
  }

  get themes(): ThemeInfo[] {
    return [...Object.values(this.allTheme)];
  }

  get colorSchemes(): IColorScheme[] {
    return  this.allTheme[this.theme]?.colorSchemes || [];
  }

  get visible(): boolean {
    return this.state.configSidebarVisible;
  }

  set visible(visible: boolean) {
    this.state.configSidebarVisible = visible;
  }

  get scale(): number {
    return this.config.scale;
  }

  set scale(scale: number) {
    this.updateLayoutConfig({ scale });
  }

  get menuMode(): MenuMode {
    return this.config.menuMode;
  }

  set menuMode(menuMode: MenuMode) {
    this.updateLayoutConfig({ menuMode });
    if (this.layoutService.isSlimOrHorizontal()) {
      this.menuService.reset();
    }
  }

  get menuProfilePosition(): MenuPosition {
    return this.config.menuProfilePosition;
  }

  set menuProfilePosition(menuProfilePosition: MenuPosition) {
    this.updateLayoutConfig({ menuProfilePosition });
    if (this.layoutService.isSlimOrHorizontal()) {
      this.menuService.reset();
    }
  }

  get colorScheme(): string {
    return this.config.colorScheme;
  }

  set colorScheme(colorScheme: string) {
    this.updateLayoutConfig({ colorScheme });
  }

  get inputStyle(): InputStyle {
    return this.config.inputStyle;
  }

  set inputStyle(inputStyle: InputStyle) {
    this.updateLayoutConfig({ inputStyle });
  }

  get ripple(): boolean {
    return this.config.ripple;
  }

  set ripple(ripple: boolean) {
    this.updateLayoutConfig({ ripple });
  }

  get menuTheme(): string {
    return this.config.menuTheme;
  }

  set menuTheme(menuTheme: string) {
    this.updateLayoutConfig({ menuTheme });
  }

  get topbarTheme(): string {
    return this.config.topbarTheme;
  }

  set topbarTheme(topbarTheme: string) {
    this.updateLayoutConfig({ topbarTheme });
  }

  get componentTheme(): string {
    return this.config.componentTheme;
  }

  set componentTheme(componentTheme: string) {
    this.updateLayoutConfig({ componentTheme });
  }

  get theme(): string {
    return this.config.theme;
  }

  set theme(theme: string) {
    const colorScheme = isEmpty(this.colorSchemes) ? undefined : this.colorSchemes.some(c => c.name === this.colorScheme) ? this.colorScheme : this.colorSchemes[0];
    this.updateLayoutConfig({ theme, colorScheme });

    console.log(this.allTheme[theme], this.colorSchemes)
  }

  get isScaleMin(): boolean {
    return this.scale === this.scales[0];
  }

  get isScaleMax(): boolean {
    return this.scale === this.scales[this.scales.length-1];
  }


  constructor(
    public layoutService: LayoutService,
    public menuService: MenuService
  ) {}

  ngOnInit(): void {
    this.layoutService.tryAddTheme();

    if(isNull(this.theme)) {
      this.theme = this.allTheme[Object.keys(this.allTheme)[0]].name;
    }


  }

  onConfigButtonClick() {
    this.layoutService.showConfigSidebar();
  }

  changeTheme(theme: ComponentThemeLabel) {
    this.componentTheme = theme.name;
  }

  changeColorSchema(schema: IColorScheme): void {
    this.colorScheme = schema.name;
  }

  changeTopbarTheme(theme: TopbarThemeLabel) {
    this.topbarTheme = theme.name;
  }

  changeMenuTheme(theme: MenuThemeLabel) {
    this.menuTheme = theme.name;
  }

  decrementScale() {
    this.scale--;
  }

  incrementScale() {
    this.scale++;
  }

  selectScale(scale: number): void {
    this.scale = scale;
  }
}
