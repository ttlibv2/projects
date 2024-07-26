import {
    AnimationEvent,
    animate,
    state,
    style,
    transition,
    trigger,
} from "@angular/animations";
import {
    AfterViewChecked,
    ChangeDetectorRef,
    Component,
    ElementRef,
    HostBinding,
    Inject,
    Input,
    OnDestroy,
    OnInit,
    ViewChild,
    numberAttribute,
} from "@angular/core";
import { Subscription, filter } from "rxjs";
import { LayoutService } from "../services/layout.service";
import { IsActiveMatchOptions, NavigationEnd, Router } from "@angular/router";
import { MenuService } from "../services/menu.service";
import { DOCUMENT } from "@angular/common";
import { DomHandler } from "primeng/dom";
import { MenuItem } from "primeng/api";
import { Objects } from "ts-ui/helper";

const {notEmpty, notNull} = Objects;

@Component({
    selector: "[ts-app-menuitem]",
    templateUrl: "./app-menuitem.component.html",
    animations: [
        trigger("children", [
            state("collapsed", style({ height: "0" })),
            state("expanded", style({ height: "*" })),
            state("hidden", style({ display: "none" })),
            state("visible", style({ display: "block" })),
            transition(
                "collapsed <=> expanded",
                animate("400ms cubic-bezier(0.86, 0, 0.07, 1)")
            ),
        ]),
    ],
})
export class AppMenuItemComponent implements OnInit, OnDestroy, AfterViewChecked {
    @Input() item: MenuItem;
    @Input({ transform: numberAttribute }) index!: number;
    @Input() @HostBinding('class.layout-root-menuitem') root!: boolean;
    @Input() parentKey!: string;

    @ViewChild("submenu")
    submenu: ElementRef<HTMLUListElement>;

    active = false;
    menuSourceSubscription: Subscription;
    menuResetSubscription: Subscription;
    key: string = "";

    matchOptions: IsActiveMatchOptions = { paths: 'exact', queryParams: 'ignored', matrixParams: 'ignored', fragment: 'ignored' };


    get tooltipDisabled(): boolean {
        return !(this.root && this.layoutService.isSlim() && !this.active);
    }

    @HostBinding('class.active-menuitem')
    get activeClass() {
        return this.active && !this.root;
    }

    get isSlimOrHorizontal(): boolean {
        return this.layoutService.isSlimOrHorizontal();
    }

    get isDesktop(): boolean {
        return this.layoutService.isDesktop();
    }

    get isMobile(): boolean {
        return this.layoutService.isMobile();
    }

    get isMenuHoverActive(): boolean {
        return this.layoutService.state.menuHoverActive;
    }

    get isMenuDrawer(): boolean {
        return this.layoutService.isMenuDrawer();
    }

    constructor(
        @Inject(DOCUMENT)
        private document: Document,
        public layoutService: LayoutService,
        public cd: ChangeDetectorRef,
        public router: Router,
        public menuService: MenuService) {

        this.menuSourceSubscription = this.menuService.menuSource$.subscribe(value => {
            Promise.resolve(null).then(() => {
                const hasNotBegin = !value.key.startsWith(`${this.key}-`);
                if(value.routeEvent) this.active = value.key == this.key || !hasNotBegin;
                else if(value.key !== this.key && hasNotBegin) this.active = false;
            });
        });

        this.menuResetSubscription = this.menuService.resetSource$.subscribe(() => {
            this.active = false;
        });

        this.router.events.pipe(filter(event => event instanceof NavigationEnd)).subscribe(params => {
            if (this.isSlimOrHorizontal) this.active = false;
            else if (this.item.routerLink) this.updateActiveStateFromRoute();
        });

    }

    ngOnInit() {
        this.key = this.parentKey ? `${this.parentKey}-${this.index}` : String(this.index);
        if (this.item.routerLink && !this.isSlimOrHorizontal) {
            this.updateActiveStateFromRoute();
        }
    }

    ngAfterViewChecked() {
        if (this.root && this.active && this.isDesktop && this.isSlimOrHorizontal) {
            const element = this.submenu?.nativeElement;
            const parentElement = this.submenu?.nativeElement.parentElement;
            this.calculatePosition(element, parentElement);
        }
    }

    updateActiveStateFromRoute() {
        let activeRoute = this.router.isActive(this.item.routerLink[0], this.matchOptions);
        if (activeRoute) this.menuService.onMenuStateChange({ key: this.key, routeEvent: true });
    }

    calculatePosition(element: any, parentElement: Element) {
        if (element) {
            const { left: S, top: B } = parentElement.getBoundingClientRect();
            const [innerWidth, innerHeight] = [window.innerWidth, window.innerHeight];
            const [offsetWidth, offsetHeight] = [element.offsetWidth, element.offsetHeight];
            const scrollBarWidth = DomHandler.calculateScrollbarWidth();
            const layoutTopbarOffsetHeight = (<HTMLElement>this.document.querySelector(".layout-topbar"))?.offsetHeight || 0;

            if (element.style.top = "", element.style.left = "", this.layoutService.isHorizontal()) {
                const Pt = S + offsetWidth + scrollBarWidth;
                element.style.left = innerWidth < Pt ? S - (Pt - innerWidth) + "px" : `${S}px`
            } //
            else if (this.layoutService.isSlim() || this.layoutService.isSlimPlus()) {
                const Pt = B - layoutTopbarOffsetHeight, wt = Pt + offsetHeight + layoutTopbarOffsetHeight;
                element.style.top = innerHeight < wt ? Pt - (wt - innerHeight) + "px" : `${Pt}px`
            }
        }
    }

    itemClick(event: Event) {

        // avoid processing disabled items
        if (this.item.disabled) {
            event.preventDefault();
            return;
        }

        if(this.root && this.isSlimOrHorizontal) {
            this.layoutService.toggleMenuHoverActive();
        }

        if(notNull(this.item.command)) {
            this.item.command({
                originalEvent: event, 
                item: this.item
            })
        }

        //
        if(notEmpty(this.item.items)) {
            this.active = !this.active;
            if(this.active && this.root && this.isSlimOrHorizontal) {
                this.layoutService.onOverlaySubmenuOpen();
            }
        }
        //
        else {

            if(this.isMobile)this.layoutService.state.staticMenuMobileActive = false;
            if(this.isSlimOrHorizontal) {
                this.menuService.reset();
                this.layoutService.state.menuHoverActive = false;
            }
        }

        this.menuService.onMenuStateChange({
            key: this.key
        });

    }

    onMouseEnter() {
        if (this.root && this.isSlimOrHorizontal && this.isDesktop && this.isMenuHoverActive) {
            this.active = true;
            this.menuService.onMenuStateChange({ key: this.key });
        }
    }

    get submenuAnimation() {
        return this.isDesktop && this.isSlimOrHorizontal ? (this.active ? "visible" : "hidden")
            : (this.root || this.active ? "expanded" : "collapsed");
    }

    onSubmenuAnimated(event: AnimationEvent): void {
        if ("visible" === event.toState && this.isDesktop && this.isSlimOrHorizontal) {
            const element = event.element;
            this.calculatePosition(element, element.parentElement)
        }
    }

    ngOnDestroy() {
        if (this.menuSourceSubscription) {
            this.menuSourceSubscription.unsubscribe();
        }

        if (this.menuResetSubscription) {
            this.menuResetSubscription.unsubscribe();
        }
    }
}