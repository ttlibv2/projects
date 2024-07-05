import { MenuItem } from "primeng/api";

export interface MenuChangeEvent {
  key: string;
  routeEvent?: boolean;
}

export interface ThemeInfo {
  label: string;
  name: string;
  url?: string;
  colorSchemes?: IColorScheme[];
}


export interface LayoutState {
  staticMenuDesktopInactive: boolean;
  staticMenuMobileActive: boolean;
  overlayMenuActive: boolean;
  profileSidebarVisible: boolean;
  configSidebarVisible: boolean;
  menuHoverActive: boolean;
  rightMenuActive: boolean;
  topbarMenuActive: boolean;
  menuProfileActive: boolean;
  sidebarActive: boolean;
  anchored: boolean;
}

export interface MenuModeLabel {
  label: string;
  mode: MenuMode;
}

export type MenuMode =
  | "drawer"
  | "horizontal"
  | "reveal"
  | "slim"
  | "slim-plus"
  | "static"
  | "overlay";
export type MenuPosition = "end" | "start";
export type InputStyle = "outlined" | "filled";

export interface AppConfig {
  listMenu?: MenuItem[];
  inputStyle: InputStyle;
 
  componentTheme: string;
  menuTheme: string;
  topbarTheme: string;
 
  ripple: boolean;
  menuMode: MenuMode;
  scale: number;
  menuProfilePosition: MenuPosition;
  
  elThemeId: string | "theme-css" | "theme-link";
  themeUrlPrefix: string;
  colorScheme: string;
  theme: string;
  visibleSideBar?: boolean;
  visibleFooter?: boolean;
  visibleConfig?: boolean;
  visibleBreadcrumb?: boolean;
  visibleTopBar?: boolean;
  
  appLogo?: string;
  appName?: string;

  listTheme?: Record<string, ThemeInfo>;
}

export interface ComponentThemeLabel {
  name: string;
  color: string;
}

export interface MenuThemeLabel {
  name: string;
  color: string;
  styleClass?: string;
  logoColor?: string;
}

export interface TopbarThemeLabel {
  name: string;
  color: string;
}

export interface IColorScheme {
  name: string;
  color: string;
  url: string;
}


export const defaultLayoutState: LayoutState = {
  profileSidebarVisible: false,
  staticMenuDesktopInactive: false,
  overlayMenuActive: false,
  configSidebarVisible: false,
  staticMenuMobileActive: false,
  menuHoverActive: false,
  rightMenuActive: false,
  topbarMenuActive: false,
  menuProfileActive: false,
  sidebarActive: false,
  anchored: false,
};

export const allMenuModeLabel: MenuModeLabel[] = [
  { label: "Static", mode: "static" },
  { label: "Overlay", mode: "overlay" },
  { label: "Slim", mode: "slim" },
  { label: "Slim+", mode: "slim-plus" },
  { label: "Reveal", mode: "reveal" },
  { label: "Drawer", mode: "drawer" },
  { label: "Horizontal", mode: "horizontal" },
];

export const allMenuThemeLabel: MenuThemeLabel[] = [
  {
    name: "light",
    color: "#FDFEFF",
  },
  {
    name: "dark",
    color: "#434B54",
    styleClass: "text-white",
  },
  {
    name: "indigo",
    color: "#1A237E",
    styleClass: "text-white",
  },
  {
    name: "bluegrey",
    color: "#37474F",
    styleClass: "text-white",
  },
  {
    name: "brown",
    color: "#4E342E",
    styleClass: "text-white",
  },
  {
    name: "cyan",
    color: "#006064",
    styleClass: "text-white",
  },
  {
    name: "green",
    color: "#2E7D32",
    styleClass: "text-white",
  },
  {
    name: "deeppurple",
    color: "#4527A0",
    styleClass: "text-white",
  },
  {
    name: "deeporange",
    color: "#BF360C",
    styleClass: "text-white",
  },
  {
    name: "pink",
    color: "#880E4F",
    styleClass: "text-white",
  },
  {
    name: "purple",
    color: "#6A1B9A",
    styleClass: "text-white",
  },
  {
    name: "teal",
    color: "#00695C",
    styleClass: "text-white",
  },
];

export const allTopbarThemeLabel: TopbarThemeLabel[] = [
  {
    name: "white",
    color: "#FDFEFF",
  },
  {
    name: "dark",
    color: "#363636",
  },
  {
    name: "lightblue",
    color: "#2E88FF",
  },
  {
    name: "blue",
    color: "#1565C0",
  },
  {
    name: "deeppurple",
    color: "#4527A0",
  },
  {
    name: "purple",
    color: "#6A1B9A",
  },
  {
    name: "pink",
    color: "#AD1457",
  },
  {
    name: "cyan",
    color: "#0097A7",
  },
  {
    name: "teal",
    color: "#00796B",
  },
  {
    name: "green",
    color: "#43A047",
  },
  {
    name: "lightgreen",
    color: "#689F38",
  },
  {
    name: "lime",
    color: "#AFB42B",
  },
  {
    name: "yellow",
    color: "#FBC02D",
  },
  {
    name: "amber",
    color: "#FFA000",
  },
  {
    name: "orange",
    color: "#FB8C00",
  },
  {
    name: "deeporange",
    color: "#D84315",
  },
  {
    name: "brown",
    color: "#5D4037",
  },
  {
    name: "grey",
    color: "#616161",
  },
  {
    name: "bluegrey",
    color: "#546E7A",
  },
  {
    name: "indigo",
    color: "#3F51B5",
  },
];

export const allTheme: Record<string, ThemeInfo> = {
  "theme-freya-light": {
    label: "Theme Light",
    name: 'theme-freya-light',
  },
  "theme-freya-dark": {
    name: "theme-freya-dark",
    label: "Theme Dark",
  },
};
