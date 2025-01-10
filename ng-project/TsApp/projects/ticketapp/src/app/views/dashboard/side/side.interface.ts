import { ActivatedRoute, IsActiveMatchOptions, Params, QueryParamsHandling } from "@angular/router";
import { TooltipOptions } from "primeng/api";
import { BadgeProps, INgStyle, Severity, TooltipPos } from "ts-ui/common";
import { JsonAny } from "ts-ui/helper";

export type LinkTarget = '_blank' | '_self' | '_parent' | '_top' | 'framename';
export type SideItemCommand = (event: SideItemEvent) => void;

export interface SideItem {

  /**
   * Identifier of the element. 
   * @group Props
   * */
  id?: string;

  /**
   * Text of the item.
   * @group Props
   * */
  title?: string;

  /**
   *  External link to navigate when item is clicked.
   * @group Props
   * */
  url?: string;

  /**
   *  Specifies tab order of the item.
   * @group Props
   * */
  tabindex?: string;

  /**
   *  When set as true, disables the menuitem.
   * @group Props
   * */
  disabled?: boolean;

  /**
   *  Whether the dom element of menuitem is created or not.
   * @group Props
   * */
  visible?: boolean;

  /**
   *  Specifies where to open the linked document.
   * @group Props
   * */
  target?: LinkTarget;

  /**
   *  Visibility of submenu.
   * @group Props
   * */
  expanded?: boolean;

  /**
   *  Inline style of the menuitem.
   * @group Props
   * */
  style?: INgStyle;

  /**
   *  Style class of the menuitem.
   * @group Props
   * */
  styleClass?: string;

  /**
   *  Callback to execute when item is clicked.
   * @group Props
   * */
  command?: SideItemCommand;

  /**
   *  An array of children menuitems.
   * @group Props
   * */
  items?: SideItem[];

  //+++++++++++++++++++++++++++++
  //  ICON
  //+++++++++++++++++++++++++++++

  /**
   *  Icon of the item.
   * @group Props
   * */
  icon?: string;

  /**
   *  Style icon of the item.
   * @group Props
   * */
  iconStyle?: INgStyle;

  /**
   *  Style class icon of the item.
   * @group Props
   * */
  iconClass?: string;

  //+++++++++++++++++++++++++++++
  //  PINNED
  //+++++++++++++++++++++++++++++

  pinned?: boolean;

  /**
   * Style pinned of the item.
   * @group Props
   * */
  pinnedStyle?: INgStyle;

  /**
   * Style class pinned of the item.
   * @group Props
   * */
  pinnedClass?: string;

  visiblePinned?: boolean;

  //+++++++++++++++++++++++++++++
  //  TOOLTIP
  //+++++++++++++++++++++++++++++

  /**
   * Tooltip of the item.
   * @group Props
   * */
  tooltip?: string;

  /**
   * Position of the tooltip item.
   * @group Props
   * */
  tooltipPosition?: TooltipPos;

  /**
   * Options of the item's tooltip.
   * @see {TooltipOptions}
   */
  tooltipProp?: TooltipOptions;

  //+++++++++++++++++++++++++++++
  //  BADGE
  //+++++++++++++++++++++++++++++

  /**
   *  Value of the badge.
   * @group Props
   * */
  badge?: string;

  /**
   *  Style class of the badge.
   * @group Props
   * */
  badgeClass?: string;

  /**
   *  Severity type of the badge.
   * @group Props
   * */
  badgeSeverity?: Severity;

  /**
   *  Severity type of the badge.
   * @group Props
   * */
  badgeProp?: BadgeProps;

  //+++++++++++++++++++++++++++++
  //  ROUTER
  //+++++++++++++++++++++++++++++

  /**
   *  RouterLink definition for internal navigation.
   * @group Props
   * */
  routerLink?: any;

  /**
    * Query parameters for internal navigation via routerLink.
    * @see {@link RouterLink#queryParams}
    * @group Props
    * */
  queryParams?: Params;

  /**
   * Sets the hash fragment for the URL.
   * @see {@link RouterLink#fragment}
   * @group Props
   * */
  fragment?: string;

  /**
   * How to handle query parameters in the router link for the next navigation. 
   * @see {@link RouterLink#queryParamsHandling}
   * @group Props
   */
  queryParamsHandling?: QueryParamsHandling;

  /**
   * Specify a value here when you do not want to use the default value
   * for `routerLink`, which is the current activated route.
   * @see {@link RouterLink#relativeTo}
   * @group Props
   */
  relativeTo?: ActivatedRoute;

  /**
   *  When true, preserves the URL fragment for the next navigation.
   * @see {@link RouterLink#preserveFragment}
   * @group Props
   * */
  preserveFragment?: boolean;

  /**
   *  When true, navigates without pushing a new state into history.
   * @see {@link RouterLink#skipLocationChange}
   * @group Props
   * */
  skipLocationChange?: boolean;

  /**
   *  When true, navigates while replacing the current state in history.
   * @see {@link RouterLink#replaceUrl}
   * @group Props
   * */
  replaceUrl?: boolean;

  /**
   * Developer-defined state that can be passed to any navigation.
   * @see {@link RouterLink#state}
   */
  state?: JsonAny;

  /**
   *  Configuration for active router link.
   * @see {@link RouterLinkActive#routerLinkActiveOptions}
   * @group Props
   * */
  routerLinkActiveOptions?: { exact: boolean; } | IsActiveMatchOptions;

  /**
   *  Defines the item as a separator.
   * @group Props
   * */
  separator?: boolean;

  /**
   *  Defines the item as a main.
   * @group Props
   * */
  main?: boolean;
}


/**
 * Custom command event
 * @see {@link SideItem.click}
 * @group Events
 */
export interface SideItemEvent {
  /**
   *  Browser event.
   * @group Props
   * */
  originalEvent?: Event;

  /**
   *  Selected menu item.
   * @group Props
   * */
  item?: SideItem;

  /**
   *  Index of the selected item.
   * @group Props
   * */
  index?: number;
}

