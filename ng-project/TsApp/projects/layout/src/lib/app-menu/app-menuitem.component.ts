import {
  animate,
  state,
  style,
  transition,
  trigger,
} from "@angular/animations";
import {
  ChangeDetectorRef,
  Component,
  ElementRef,
  HostBinding,
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

@Component({
  selector: "[ts-app-menuitem]",
  templateUrl: "./app-menuitem.component.html",
  animations: [
    trigger("children", [
      state("collapsed",style({height: "0"})),
      state("expanded",style({height: "*"})),
      state("hidden", style({display: "none"})),
      state("visible", style({display: "block"})),
      transition(
        "collapsed <=> expanded",
        animate("400ms cubic-bezier(0.86, 0, 0.07, 1)")
      ),
    ]),
  ],
})
export class AppMenuItemComponent implements OnInit, OnDestroy  {
    @Input() item: any;
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



    constructor(public layoutService: LayoutService, private cd: ChangeDetectorRef, public router: Router, private menuService: MenuService) {
        this.menuSourceSubscription = this.menuService.menuSource$.subscribe(value => {
            Promise.resolve(null).then(() => {
                if (value.routeEvent) {
                    this.active = (value.key === this.key || value.key.startsWith(this.key + '-')) ? true : false;
                }
                else {
                    if (value.key !== this.key && !value.key.startsWith(this.key + '-')) {
                        this.active = false;
                    }
                }
            });
        });

        this.menuResetSubscription = this.menuService.resetSource$.subscribe(() => {
            this.active = false;
        });

        this.router.events.pipe(filter(event => event instanceof NavigationEnd))
            .subscribe(params => {
                if (this.item.routerLink) {
                    this.updateActiveStateFromRoute();
                }
            });
    }

    ngOnInit() {
        this.key = this.parentKey ? this.parentKey + '-' + this.index : String(this.index);

        if (this.item.routerLink) {
            this.updateActiveStateFromRoute();
        }
    }

    updateActiveStateFromRoute() {
        let activeRoute = this.router.isActive(this.item.routerLink[0], this.matchOptions);

        if (activeRoute) {
            this.menuService.onMenuStateChange({ key: this.key, routeEvent: true });
        }
    }

    itemClick(event: Event) {
        // avoid processing disabled items
        if (this.item.disabled) {
            event.preventDefault();
            return;
        }

        // execute command
        if (this.item.command) {
            this.item.command({ originalEvent: event, item: this.item });
        }

        // toggle active state
        if (this.item.items) {
            this.active = !this.active;
        }

        this.menuService.onMenuStateChange({ key: this.key });
    }

    get submenuAnimation() {
        //return this.root ? 'expanded' : (this.active ? 'expanded' : 'collapsed');
        return this.isDesktop && this.isSlimOrHorizontal ? (this.active   ? "visible"  : "hidden")
              : (this.root || this.active ? "expanded" : "collapsed");
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

    ngOnDestroy() {
        if (this.menuSourceSubscription) {
            this.menuSourceSubscription.unsubscribe();
        }

        if (this.menuResetSubscription) {
            this.menuResetSubscription.unsubscribe();
        }
    }
}

// class AppMenuItemComponent2
// {
//   @Input() item: MenuItem;
//   @Input({ transform: numberAttribute }) index!: number;
//   @Input() parentKey!: string;
//   @Input() @HostBinding("class.layout-root-menuitem") root!: boolean;

//   @ViewChild("submenu")
//   submenu: ElementRef;

//   active = false;
//   menuSourceSubscription: Subscription;
//   menuResetSubscription: Subscription;
//   key: string = "";

//   constructor(
//     public router: Router,
//     public layoutService: LayoutService,
//     private cd: ChangeDetectorRef,
//     private menuService: MenuService
//   ) {
//     this.initialize();
//   }

//   initialize() {
//     this.menuSourceSubscription = this.menuService.menuSource$.subscribe(
//       (value) => {
//         Promise.resolve(null).then(() => {
//           if (value.routeEvent) {
//             this.active =
//               value.key === this.key || value.key.startsWith(this.key + "-")
//                 ? true
//                 : false;
//           } else {
//             if (
//               value.key !== this.key &&
//               !value.key.startsWith(this.key + "-")
//             ) {
//               this.active = false;
//             }
//           }
//         });
//       }
//     );

//     this.menuResetSubscription = this.menuService.resetSource$.subscribe(() => {
//       this.active = false;
//     });

//     this.router.events
//       .pipe(filter((event) => event instanceof NavigationEnd))
//       .subscribe((params) => {
//         if (this.isCheck) this.active = false;
//         else if (this.item.routerLink) this.updateActiveStateFromRoute();
//       });
//   }

//   ngOnInit(): void {
//     this.key = this.parentKey
//       ? this.parentKey + "-" + this.index
//       : String(this.index);
//     if (this.item.routerLink && !this.isCheck) {
//       this.updateActiveStateFromRoute();
//     }
//   }

//   ngAfterViewChecked() {
//     if (this.root && this.active && this.isDesktop && this.isCheck) {
//       this.calculatePosition(
//         this.submenu?.nativeElement,
//         this.submenu?.nativeElement.parentElement
//       );
//     }
//   }

//   ngOnDestroy() {
//     if (this.menuSourceSubscription) {
//       this.menuSourceSubscription.unsubscribe();
//     }

//     if (this.menuResetSubscription) {
//       this.menuResetSubscription.unsubscribe();
//     }
//   }

//   updateActiveStateFromRoute() {
//     let activeRoute = this.router.isActive(this.item.routerLink[0], {
//       paths: "exact",
//       queryParams: "ignored",
//       matrixParams: "ignored",
//       fragment: "ignored",
//     });
//     if (activeRoute)
//       this.menuService.onMenuStateChange({ key: this.key, routeEvent: true });
//   }

//   onSubmenuAnimated(u: any) {
//     if ("visible" === u.toState && this.isDesktop && this.isCheck) {
//       const C = u.element;
//       this.calculatePosition(C, C.parentElement);
//     }
//   }

//   calculatePosition(u: any, C: any) {
//     // if (u) {
//     //   const { left: S, top: B } = C.getBoundingClientRect(),
//     //     [ie, X] = [window.innerWidth, window.innerHeight],
//     //     [be, xe] = [u.offsetWidth, u.offsetHeight],
//     //     ot = Q.p.calculateScrollbarWidth(),
//     //     Nt = document.querySelector(".layout-topbar")?.offsetHeight || 0;
//     //   if (
//     //     ((u.style.top = ""),
//     //     (u.style.left = ""),
//     //     this.layoutService.isHorizontal())
//     //   ) {
//     //     const Pt = S + be + ot;
//     //     u.style.left = ie < Pt ? S - (Pt - ie) + "px" : `${S}px`;
//     //   } else if (
//     //     this.layoutService.isSlim() ||
//     //     this.layoutService.isSlimPlus()
//     //   ) {
//     //     const Pt = B - Nt,
//     //       wt = Pt + xe + Nt;
//     //     u.style.top = X < wt ? Pt - (wt - X) + "px" : `${Pt}px`;
//     //   }
//     // }
//     throw new Error('app-menuitem.component.ts - calculatePosition');
//   }

//   itemClick(event: Event) {
//     // avoid processing disabled items
//     if (this.item.disabled) {
//       event.preventDefault();
//       return;
//     }

//     if ((this.root && this.isSlim) || this.isHorizontal || this.isSlimPlus) {
//       this.layoutService.state.menuHoverActive =
//         !this.layoutService.state.menuHoverActive;
//     }

//       // execute command
//       if (this.item.command) {
//         this.item.command({ originalEvent: event, item: this.item });
//       }

//       // toggle active state
//       if (this.item.items) {
//         this.active = !this.active;
//         if(this.root && this.active && this.isCheck) {
//             this.layoutService.onOverlaySubmenuOpen();
//         }
//       }
//       //
//       else if(this.isMobile){
//         this.layoutService.state.staticMenuMobileActive =  false;
//       }

//       if(this.isCheck) {
//         this.menuService.reset();
//         this.layoutService.state.menuHoverActive = false;
//       }

//       this.menuService.onMenuStateChange({
//         key: this.key,
//       });
//   }
//   onMouseEnter() {
//     if(this.root && this.isCheck && this.isDesktop && this.layoutService.state.menuHoverActive) {
//         this.active = true;
//         this.menuService.onMenuStateChange({
//             key: this.key,
//           });
//     }

  
//   }

//   get isCheck(): boolean {
//     return this.isHorizontal || this.isSlim || this.isSlimPlus;
//   }

//   get submenuAnimation() {
//     return this.isDesktop && this.isCheck
//       ? this.active
//         ? "visible"
//         : "hidden"
//       : this.root || this.active
//       ? "expanded"
//       : "collapsed";
//   }

//   get isHorizontal() {
//     return this.layoutService.isHorizontal();
//   }

//   get isSlim() {
//     return this.layoutService.isSlim();
//   }

//   get isSlimPlus() {
//     return this.layoutService.isSlimPlus();
//   }

//   get isDesktop() {
//     return this.layoutService.isDesktop();
//   }

  
//   get isMobile() {
//     return this.layoutService.isMobile();
//   }

//   @HostBinding("class.active-menuitem")
//   get activeClass() {
//     return this.active && !this.root;
//   }
// }
