import { IsActiveMatchOptions, QueryParamsHandling } from "@angular/router";
import { MenuItem, TooltipOptions } from "primeng/api";
import { INgClass, INgStyle, TooltipPos } from "ts-ui/common";

/**
 * Custom command event
 * @see {@link SideItem.click}
 * @group Events
 */
export interface SideItemEvent {
    /**
     * Browser event.
     */
    originalEvent?: Event;
    /**
     * Selected menu item.
     */
    item?: SideItem;
    /**
     * Index of the selected item.
     */
    index?: number;
}

export interface SideItem  {
    [key: string]: any;

    /**
     * Identifier of the element.
     */
    id?: string;

    /**
     * Inline style of the menuitem.
     */
    style?: INgStyle;

    /**
     * Style class of the menuitem.
     */
    styleClass?: INgClass;

    /**
     * Text of the item.
     */
    label?: string;
  
    /**
     * Icon of the item.
     */
    icon?: string;
   
    /**
     * An array of children menuitems.
     */
    children?: MenuItem[];
   
    /**
     * Visibility of submenu.
     */
    expanded?: boolean;

    expandedIcon?: string;
    collapseIcon?: string;
  
    /**
     * When set as true, disables the menuitem.
     */
    disabled?: boolean;
   
    /**
     * Whether the dom element of menuitem is created or not.
     */
    visible?: boolean;
   
    /**
     * Specifies where to open the linked document.
     */
    target?: string;
   
    /**
     * Whether to escape the label or not. Set to false to display html content.
     */
    escape?: boolean;
   
    /**
     * Defines the item as a separator.
     */
    separator?: boolean;

    /**
     * Value of HTML data-* attribute.
     */
    automationId?: any;

    /**
     * Specifies tab order of the item.
     */
    tabindex?: string;    

    /**
     * Developer-defined state that can be passed to any navigation.
     * @see {MenuItemState}
     */
    state?: {
        [k: string]: any;
    };

    hasMain?: boolean;

    //==================================
    //  PINNED
    //==================================

    pinned?: boolean;

    pinnedClass?: INgClass;

    pinnedStyle?: INgStyle;

    //==================================
    //  ICON
    //==================================

    /**
     * Inline style of the item's icon.
     */
    iconStyle?: INgStyle;
   
    /**
     * Class of the item's icon.
     */
    iconClass?: INgClass;

    /**
     * defined color icon
     */    
    iconColor?: string;

    //==================================
    //  BADGE
    //==================================

    /**
     * Value of the badge.
     */
    badge?: string;

    /**
     * Style class of the badge.
     */
    badgeClass?: INgClass;    

    //==================================
    //  TOOLTIP
    //==================================

    /**
     * Tooltip of the item.
     */
    tooltip?: string;

    /**
     * Position of the tooltip item.
     */
    tooltipPosition?: TooltipPos;

    /**
     * Options of the item's tooltip.
     * @see {TooltipOptions}
     */
    tooltipOptions?: TooltipOptions;

    //==================================
    //  ROUTER_LINK
    //==================================

    /**
     * RouterLink definition for internal navigation.
     */
    routerLink?: any;

    /**
     * Query parameters for internal navigation via routerLink.
     */
    queryParams?: {
        [k: string]: any;
    };

    /**
     * Sets the hash fragment for the URL.
     */
    fragment?: string;

    /**
     *  How to handle query parameters in the router link for the next navigation. One of:
        merge : Merge new with current parameters.
        preserve : Preserve current parameters.k.
     */
    queryParamsHandling?: QueryParamsHandling;

    /**
     * When true, preserves the URL fragment for the next navigation.
     */
    preserveFragment?: boolean;

    /**
     * When true, navigates without pushing a new state into history.
     */
    skipLocationChange?: boolean;

    /**
     * When true, navigates while replacing the current state in history.
     */
    replaceUrl?: boolean;

    /**
     * External link to navigate when item is clicked.
     */
    url?: string;

    /**
     * Configuration for active router link.
     */
    routerLinkActiveOptions?: { exact: boolean } | IsActiveMatchOptions;

    /**
     * Callback to execute when item is clicked.
     */
    click?(event: SideItemEvent): void;    
}