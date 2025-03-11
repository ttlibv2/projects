import { ActivatedRoute, IsActiveMatchOptions, Params, QueryParamsHandling, RouterLink, RouterLinkActive, RouterModule } from "@angular/router";
import { ChangeDetectionStrategy, Component, ElementRef, inject, Input, numberAttribute, OnChanges, Renderer2, SimpleChanges, ViewEncapsulation } from "@angular/core";
import { CommonModule } from "@angular/common";
import { TooltipOptions } from "primeng/api";
import { DomHandler, INgStyle, Severity, TooltipPos } from "ts-ui/common";
import { JsonAny, Objects } from "ts-ui/helper";
import { BadgeModule } from "primeng/badge";
import { LinkTarget, SideItem, SideItemCommand } from "./side.interface";
import { TooltipModule } from "primeng/tooltip";
import { RippleModule } from "primeng/ripple";
const { isNull, isBlank } = Objects;

const matchOptions: IsActiveMatchOptions = {
  paths: 'exact',
  queryParams: 'ignored',
  matrixParams: 'ignored',
  fragment: 'ignored'
};

@Component({
  standalone: true,
  encapsulation: ViewEncapsulation.None,
  changeDetection: ChangeDetectionStrategy.OnPush,
  imports: [CommonModule, RouterModule, BadgeModule, TooltipModule, RippleModule],
  selector: '[ts-side-item]',
  templateUrl: './view/side-item.html',
  host: {
    'class': 'side--item',
    '[class.main]': `item.main`,
    '[class.use-bullet]': `level > 0 && !!bullet`,
    '[class.side--item-main]': `item.main`,  
    '[class.side--item-pined]': 'pinned',

  },
})
export class SideItemView implements OnChanges {
  @Input() item: SideItem;
  @Input({transform: numberAttribute}) index: number;
  @Input({transform: numberAttribute}) level: number = 0;
  @Input() bullet: string;

  private _elementRef = inject(ElementRef);
  private _renderer = inject(Renderer2);

  ngOnChanges(changes: SimpleChanges): void {
    Object.keys(changes).filter(k => !['item', 'index'].includes(k))
      .forEach(key => this.item[key] = changes[key].currentValue);

      if(changes['level'] && this.level > 0) {
        DomHandler.addClass(this._elementRef.nativeElement, 'level-'+this.level);
      }
  }

  /**
   * Identifier of the element. 
   * @group Props
   * */
  get id(): string { return this.item.id; }

  /**
   * Text of the item.
   * @group Props
   * */
  get title(): string { return this.item.title; }

  /**
   *  External link to navigate when item is clicked.
   * @group Props
   * */
  get url(): string { return this.item.url; }

  /**
   * Specifies tab order of the item.
   * @group Props
   * */
  get tabindex(): string { return this.item.tabindex; }

  /**
   * When set as true, disables the menuitem.
   * @group Props
   * */
  get disabled(): boolean { return this.item.disabled; }

  /**
   * Whether the dom element of menuitem is created or not.
   * @group Props
   * */
  get visible(): boolean { return this.item.visible; }

  /**
   * Specifies where to open the linked document.
   * @group Props
   * */
  get target(): LinkTarget { return this.item.target; }

  /**
   *  Visibility of submenu.
   * @group Props
   * */
  get expanded(): boolean { return this.item.expanded; }

  /**
   *  Inline style of the menuitem.
   * @group Props
   * */
  get style(): INgStyle { return this.item.style; }

  /**
   *  Style class of the menuitem.
   * @group Props
   * */
  get styleClass(): string { return this.item.styleClass; }

  /**
   *  Callback to execute when item is clicked.
   * @group Props
   * */
  get command(): SideItemCommand { return this.item.command; };

  /**
   *  An array of children menuitems.
   * @group Props
   * */
  get items(): SideItem[] { return this.item.items; }

  //+++++++++++++++++++++++++++++
  //  ICON
  //+++++++++++++++++++++++++++++

  /**
   *  Icon of the item.
   * @group Props
   * */
  get icon(): string { return this.item.icon; }

  /**
   *  Style icon of the item.
   * @group Props
   * */
  get iconStyle(): INgStyle { return this.item.iconStyle; }

  /**
   *  Style class icon of the item.
   * @group Props
   * */
  get iconClass() {
    return {
      [this.icon]: !isBlank(this.icon),
      [this.item.iconClass]: !!this.item.iconClass
    }
  }

  //+++++++++++++++++++++++++++++
  //  PINNED
  //+++++++++++++++++++++++++++++

  get pinned(): boolean { return this.item.pinned; }

  /**
   * Style pinned of the item.
   * @group Props
   * */
  get pinnedStyle(): INgStyle { return this.item.pinnedStyle; }

  /**
   * Style class pinned of the item.
   * @group Props
   * */
  get pinnedClass(): any {
    return this.item.pinnedClass ?? 'pi pi-thumbtack';
  }

  get visiblePinned(): boolean { return isNull(this.item.visiblePinned) || this.item.visiblePinned; }

  //+++++++++++++++++++++++++++++
  //  TOOLTIP
  //+++++++++++++++++++++++++++++

  /**
   * Tooltip of the item.
   * @group Props
   * */
  get tooltip(): string { 
    return this.item.tooltip; 
  }

  /**
   * Position of the tooltip item.
   * @group Props
   * */
  get tooltipPosition(): TooltipPos { 
    return this.item.tooltipPosition ?? this.tooltipProp?.tooltipPosition ?? 'right'; 
  }

  /**
   * Options of the item's tooltip.
   * @see {TooltipOptions}
   */
  get tooltipProp(): TooltipOptions { return this.item.tooltipProp; }

  //+++++++++++++++++++++++++++++
  //  BADGE
  //+++++++++++++++++++++++++++++

  /**
   *  Value of the badge.
   * @group Props
   * */
  get badge(): string | number { return this.item.badge ?? this.item.badgeProp?.value; }

  /**
   *  Style class of the badge.
   * @group Props
   * */
  get badgeClass() { return this.item.badgeClass || this.item.badgeProp?.badgeStyleClass; }

  /**
   *  Style class of the badge.
   * @group Props
   * */
  get badgeStyle() { return this.item.badgeProp?.badgeStyle; }

  /**
   *  size of the badge.
   * @group Props
   * */
  get badgeSize() { return this.item.badgeProp?.badgeSize; }

  /**
   *  disabled of the badge.
   * @group Props
   * */
  get badgeDisabled() { return this.item.badgeProp?.disabled; }

  /**
   *  Severity type of the badge.
   * @group Props
   * */
  get badgeSeverity(): Severity { return this.item.badgeSeverity || this.item.badgeProp?.severity; }

  //+++++++++++++++++++++++++++++
  //  ROUTER
  //+++++++++++++++++++++++++++++

  /**
   *  RouterLink definition for internal navigation.
   * @group Props
   * */
  get routerLink(): any { return this.item.routerLink; }

  /**
    * Query parameters for internal navigation via routerLink.
    * @see {@link RouterLink#queryParams}
    * @group Props
    * */
  get queryParams(): Params { return this.item.queryParams; }

  /**
   * Sets the hash fragment for the URL.
   * @see {@link RouterLink#fragment}
   * @group Props
   * */
  get fragment(): string { return this.item.fragment; }

  /**
   * How to handle query parameters in the router link for the next navigation. 
   * @see {@link RouterLink#queryParamsHandling}
   * @group Props
   */
  get queryParamsHandling(): QueryParamsHandling { return this.item.queryParamsHandling; }

  /**
   * Specify a value here when you do not want to use the default value
   * for `routerLink`, which is the current activated route.
   * @see {@link RouterLink#relativeTo}
   * @group Props
   */
  get relativeTo(): ActivatedRoute { return this.item.relativeTo; }

  /**
   *  When true, preserves the URL fragment for the next navigation.
   * @see {@link RouterLink#preserveFragment}
   * @group Props
   * */
  get preserveFragment(): boolean { return this.item.preserveFragment; }

  /**
   *  When true, navigates without pushing a new state into history.
   * @see {@link RouterLink#skipLocationChange}
   * @group Props
   * */
  get skipLocationChange(): boolean { return this.item.skipLocationChange; }

  /**
   *  When true, navigates while replacing the current state in history.
   * @see {@link RouterLink#replaceUrl}
   * @group Props
   * */
  get replaceUrl(): boolean { return this.item.replaceUrl; }

  /**
   * Developer-defined state that can be passed to any navigation.
   * @see {@link RouterLink#state}
   */
  get state(): JsonAny { return this.item.state; }

  /**
   *  Configuration for active router link.
   * @see {@link RouterLinkActive#routerLinkActiveOptions}
   * @group Props
   * */
  get routerLinkActiveOptions(): { exact: boolean; } | IsActiveMatchOptions { return this.item.routerLinkActiveOptions ?? matchOptions; }

  /**
   *  Defines the item as a separator.
   * @group Props
   * */
  get separator(): boolean { return this.item.separator; }

  /**
   *  Defines the item as a main.
   * @group Props
   * */
  get main(): boolean { return this.item.main; }

  get isVisible(): boolean {
    return isNull(this.item.visible) || !!this.item.visible;
  }

  get hasChildren(): boolean {
    return this.item.items && this.item.items.length > 0
  }

  get hasPinned(): boolean {
    return isNull(this.item.visiblePinned) || !!this.item.visiblePinned;
  }

}