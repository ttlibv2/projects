export interface MenuChangeEvent {
  key: string;
  routeEvent?: boolean;
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
  inputStyle: InputStyle;
  colorScheme: string;
  componentTheme: string;
  menuTheme: string;
  topbarTheme: string;
  theme: string;
  ripple: boolean;
  menuMode: MenuMode;
  scale: number;
  menuProfilePosition: MenuPosition;
  elThemeId: string | "theme-css" | "theme-link";
  themeUrlPrefix: string;

  visibleSideBar?: boolean;
  visibleFooter?: boolean;
  visibleConfig?: boolean;
  visibleBreadcrumb?: boolean;

  appLogo?: string;
  appName?: string;
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
}

export interface ITheme {
  label: string;
  id: string;
  path: string;
  resolveUrl?: (theme: string, color: string) => string;
  colors?: IColorScheme[];
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

export const allComponentTheme: any[] = [
  {
    name: "indigo",
    color: "#3F51B5",
  },
  //   {
  //     name: "pink",
  //     color: "#E91E63",
  //   },
  {
    name: "purple",
    color: "#9C27B0",
  },
  //   {
  //     name: "deeppurple",
  //     color: "#673AB7",
  //   },
  {
    name: "blue",
    color: "#2196F3",
  },
  //   {
  //     name: "lightblue",
  //     color: "#03A9F4",
  //   },
  //   {
  //     name: "cyan",
  //     color: "#00BCD4",
  //   },
  {
    name: "teal",
    color: "#009688",
  },
  {
    name: "green",
    color: "#4CAF50",
  },
  //   {
  //     name: "lightgreen",
  //     color: "#8BC34A",
  //   },
  //   {
  //     name: "lime",
  //     color: "#CDDC39",
  //   },
  {
    name: "yellow",
    color: "#FFEB3B",
  },
  //   {
  //     name: "amber",
  //     color: "#FFC107",
  //   },
  {
    name: "orange",
    color: "#FF9800",
  },
  //   {
  //     name: "deeporange",
  //     color: "#FF5722",
  //   },
  //   {
  //     name: "brown",
  //     color: "#795548",
  //   },
  //   {
  //     name: "bluegrey",
  //     color: "#607D8B",
  //   },
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

export const allTheme: Record<string, ITheme> = {
  "theme-freya-light": {
    label: "Theme Light",
    id: "theme-freya-light",
    path: "theme-freya-light",
    colors: allComponentTheme,
  },
  "theme-freya-dark": {
    id: "theme-freya-dark",
    label: "Theme Dark",
    path: "theme-freya-dark",
    colors: allComponentTheme,
  },
};
