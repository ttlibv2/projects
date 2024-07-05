import { Component, Inject, OnDestroy, Renderer2, ViewChild } from '@angular/core';
import { Subscription, filter } from 'rxjs';
import { Objects } from 'ts-helper';
import { LayoutService } from '../services/layout.service';
import { MenuService } from '../services/menu.service';
import { AppSidebarComponent } from '../app-sidebar/app-sidebar.component';
import { AppTopbarComponent } from '../app-topbar/app-topbar.component';
import { NavigationEnd, Router } from '@angular/router';
import { DOCUMENT } from '@angular/common';

const { notBlank, isNull, notNull } = Objects;

@Component({
  selector: 'ts-app-layout',
  templateUrl: './layout.component.html'
})
export class AppLayoutComponent implements OnDestroy {

  get visibleTopBar(): boolean {
    return this.layoutService.config().visibleTopBar;
  }

  get visibleSideBar(): boolean {
    return this.layoutService.config().visibleSideBar;
  }

  get visibleFooter(): boolean {
    return this.layoutService.config().visibleFooter;
  }

  get visibleConfig(): boolean {
    return this.layoutService.config().visibleConfig;
  }

  get visibleBreadcrumb(): boolean {
    return this.layoutService.config().visibleBreadcrumb;
  }

  get layoutStaticInActive(): boolean {
    const { menuMode, visibleSideBar } = this.layoutService.config();
    const staticMenuDesktopInactive = this.layoutService.state.staticMenuDesktopInactive;
    return (staticMenuDesktopInactive && "static" === menuMode) || visibleSideBar !== true;
  }

  get containerClass(): any {
    const { menuMode, topbarTheme, menuTheme, menuProfilePosition, colorScheme } = this.layoutService.config();

    return {
      "p-input-filled": "filled" === this.layoutService.config().inputStyle,
      "p-ripple-disabled": !this.layoutService.config().ripple,
      "layout-static-inactive": this.layoutStaticInActive,

      "layout-overlay-active": this.layoutService.state.overlayMenuActive,
      "layout-mobile-active": this.layoutService.state.staticMenuMobileActive,
      "layout-topbar-menu-active": this.layoutService.state.topbarMenuActive,
      "layout-menu-profile-active": this.layoutService.state.menuProfileActive,
      "layout-topbar-inactive": this.layoutService.config().visibleTopBar === false,

      "layout-sidebar-active": this.layoutService.state.sidebarActive,
      "layout-sidebar-anchored": this.layoutService.state.anchored,

      'layout-light': 'light' === colorScheme,
      'layout-dim': 'dim' === colorScheme,
      'layout-dark': 'dark' === colorScheme,

      'layout-colorscheme-menu': 'colorScheme' === menuTheme,
      'layout-primarycolor-menu': 'primaryColor' === menuTheme,
      'layout-transparent-menu': 'transparent' === menuTheme,

      [`layout-${menuMode}`]: notBlank(menuMode),
      [`layout-topbar-${topbarTheme}`]: notBlank(topbarTheme),
      [`layout-menu-${menuTheme}`]: notBlank(menuTheme),
      [`layout-menu-profile-${menuProfilePosition}`]: notBlank(menuProfilePosition)
    };
  }

  overlayMenuOpenSubscription: Subscription;
  menuOutsideClickListener: any;
  profileMenuOutsideClickListener: any;
  menuScrollListener: any;

  @ViewChild(AppSidebarComponent)
  appSidebar!: AppSidebarComponent;

  @ViewChild(AppTopbarComponent)
  appTopbar!: AppTopbarComponent;


  constructor(
    @Inject(DOCUMENT)
    public document: Document,
    public layoutService: LayoutService,
    public menuService: MenuService,
    public renderer: Renderer2,
    public router: Router) {
    this.initialize();
  }

  initialize() {

    //overlayMenuOpenSubscription
    this.overlayMenuOpenSubscription = this.layoutService.overlayOpen$.subscribe({
      next: res => {

        //menuOutsideClickListener
        if (isNull(this.menuOutsideClickListener)) {
          this.menuOutsideClickListener = this.renderer.listen('document', 'click', event => {
            const element = this.appSidebar.el.nativeElement;
            const topbarEl = this.appTopbar.menuButton?.el?.nativeElement;
            const isOutsideClicked = !(element.isSameNode(event.target) || element.contains(event.target) || topbarEl?.isSameNode(event.target) || topbarEl?.contains(event.target));

            if (isOutsideClicked) {
              this.hideMenu();
            }
          });
        }

          //profileMenuOutsideClickListener
          if (isNull(this.profileMenuOutsideClickListener) && notNull(this.appTopbar.topbarMenuButton)) {
            const menuEl = this.appTopbar.menu.nativeElement;
            const menuButtonEl = this.appTopbar.topbarMenuButton.nativeElement;
            this.profileMenuOutsideClickListener = this.renderer.listen('document', 'click', event => {
              const isOutsideClicked = !(menuEl.isSameNode(event.target) || menuEl.contains(event.target) || menuButtonEl.isSameNode(event.target) || menuButtonEl.contains(event.target));
              if (isOutsideClicked) this.hideProfileMenu();
            });
          }

        //menuScrollListener
        if (this.layoutService.isSlimOrHorizontal() && isNull(this.menuScrollListener)) {
          const sidebarEl = this.appSidebar.menuContainer.nativeElement;
          this.menuScrollListener = this.renderer.listen(sidebarEl, 'scroll', event => {
            if (this.layoutService.isDesktop()) this.hideMenu();
          })
        }

        //staticMenuMobileActive
        if (this.layoutService.state.staticMenuMobileActive) {
          this.blockBodyScroll();
        }
      }
    });

    this.router.events.pipe(filter(event => event instanceof NavigationEnd)).subscribe(() => {
      this.hideMenu();
      this.hideProfileMenu();
      this.unblockBodyScroll();
    });


  }


  hideMenu() {
    this.layoutService.state.overlayMenuActive = false;
    this.layoutService.state.staticMenuMobileActive = false;
    this.layoutService.state.menuHoverActive = false;
    this.menuService.reset();

    if (notNull(this.menuOutsideClickListener)) {
      this.menuOutsideClickListener();
      this.menuOutsideClickListener = null;
    }

    if (notNull(this.menuScrollListener)) {
      this.menuScrollListener();
      this.menuScrollListener = null;
    }

    this.unblockBodyScroll();
  }

  hideProfileMenu() {
    this.layoutService.state.profileSidebarVisible = false;
    if (notNull(this.profileMenuOutsideClickListener)) {
      this.profileMenuOutsideClickListener();
      this.profileMenuOutsideClickListener = null;
    }
  }

  blockBodyScroll() {
    if (this.document.body.classList) {
      this.document.body.classList.add('blocked-scroll');
    }
    else {
      this.document.body.className += ' blocked-scroll';
    }
  }

  unblockBodyScroll(): void {
    if (this.document.body.classList) {
      this.document.body.classList.remove('blocked-scroll');
    }
    else {
      this.document.body.className = this.document.body.className.replace(new RegExp('(^|\\b)' +
        'blocked-scroll'.split(' ').join('|') + '(\\b|$)', 'gi'), ' ');
    }
  }

  ngOnDestroy() {
    if(notNull(this.overlayMenuOpenSubscription)) {
      this.overlayMenuOpenSubscription.unsubscribe();
    }

    if(notNull(this.menuOutsideClickListener())) {
      this.menuOutsideClickListener();
    }
  
    if(notNull(this.profileMenuOutsideClickListener)) {
      this.profileMenuOutsideClickListener();
    }
  }
}
