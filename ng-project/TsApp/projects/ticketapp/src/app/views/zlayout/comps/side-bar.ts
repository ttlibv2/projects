import { ActivatedRoute, IsActiveMatchOptions, QueryParamsHandling, Router, RouterLink, UrlTree } from "@angular/router";
import { MenuItem } from "primeng/api";
import { BadgeProps, INgStyle, Severity, TooltipPos } from "ts-ui/common";

interface LinkProps {

    /**
     * Represents the `target` attribute on a host element. 
     * This is only used when the host element is an `<a>` tag.
     * @group Props
     * */
    target?: string;

    /**
     * Query parameters for internal navigation via routerLink.
     * Passed to {@link Router#createUrlTree} as part of the
     * `UrlCreationOptions`.
     * @see {@link UrlCreationOptions#queryParams}
     * @see {@link Router#createUrlTree}
     * @group Props
     */
    queryParams?: { [k: string]: any; };

    /**
     * Sets the hash fragment for the URL.
     * @group Props
     * */
    fragment?: string;

    /**
     * How to handle query parameters in a router link. One of:
     * <li>"merge" : Merge new parameters with current parameters</li>
     * <li>"preserve" : Preserve current parameters.</li>
     * <li>"replace" : Replace current parameters with new parameters. This is the default behavior.</li>
     * <li>"" : For legacy reasons, the same as 'replace'.</li>
     * @group Props
     */
    queryParamsHandling?: QueryParamsHandling;

    /**
     * Developer-defined state that can be passed to any navigation.
     * @group Props
     * */
    state?: { [k: string]: any; }

    /**
     * Specify a value here when you do not want to use the default value
     * for `routerLink`, which is the current activated route.
     * Note that a value of `undefined` here will use the `routerLink` default.
     * @see {@link RouterLink#relativeTo}
     */
    relativeTo?: ActivatedRoute;

    /**
     * When true, preserves the URL fragment for the next navigation.
     * @see {@link RouterLink#preserveFragment}
     */
    preserveFragment?: boolean;

    /**
     * When true, navigates without pushing a new state into history.
     * @see {@link RouterLink#skipLocationChange}
     */
    skipLocationChange?: boolean;

    /**
     * When true, navigates while replacing the current state in history.
     * @see {@link RouterLink#replaceUrl}
     */
    replaceUrl?: boolean;

    /**
     * Configuration for active router link.
     */
    routerLinkActiveOptions?: IsActiveMatchOptions | { exact: boolean };


}

export interface SideItem {

    /**
     * Identifier of the element.
     */
    id?: string;

    /**
     * Defined label
     * @group Props
     * */
    label?: string;

    /**
     * Defined type
     * @group Props
     * */
    type?: 'header' | 'seperator' | 'link' | 'item';

    /**
     * Inline style of the menuitem.
     */
    style?: INgStyle;

    /**
     * Style class of the menuitem.
     */
    styleClass?: string;

    /**
     * Visibility of submenu.
     * @group Props
     */
    expanded?: boolean;

    /**
     * Define expand icon
     * @group Props
     */
    expandedIcon?: string;

    /**
     * Define collapse icon
     * @group Props
     */
    collapseIcon?: string;

    /**
     * Define visible pinned.
     * @group Props
     */
    pinned?: boolean;

    /**
     * When set as true, disables the menuitem.
     * @group Props
     */
    disabled?: boolean;

    /**
     * Whether the dom element of menuitem is created or not.
     * @group Props
     */
    visible?: boolean;

    /**
     * Value of HTML data-* attribute.
     */
    automationId?: any;

    /**
     * Specifies tab order of the item.
     */
    tabindex?: string;


    //=============================
    //  ICON
    //=============================

    /**
     * Defined icon
     * @group Props
     * */
    icon?: string;

    /**
     * Defined Style class of icon
     * @group Props
     * */
    iconClass?: string;

    /**
     * Defined Style of icon
     * @group Props
     * */
    iconStyle?: INgStyle;

    //=============================
    //  BADGE
    //=============================

    /**
     * Value of the badge.
     */
    badge?: string | number;

    /**
     * Style class of the badge.
     */
    badgeClass?: string;

    /**
     * Style of the badge.
     */
    badgeStyle?: INgStyle;

    /**
     * Severity type of the badge.
     * @group Props
     */
    badgeSeverity?: Severity;

    //=============================
    //  TOOLTIP
    //=============================

    /**
     * Tooltip of the item.
     */
    tooltip?: string;

    /**
     * Position of the tooltip item.
     */
    tooltipPos?: TooltipPos;

    //=============================
    //  ROUTER LINK
    //=============================

    /**
     * Defined external link
     * @group Props
     * */
    href?: string;

    /**
     * RouterLink definition for internal navigation.
     */
    routerLink?: string | any[] | UrlTree | ((router: Router) => UrlTree);

    /**
     * Define routerlink option */
    routerLinkProps?: LinkProps;


}
