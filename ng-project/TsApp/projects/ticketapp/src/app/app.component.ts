import { AfterViewInit, Component, computed, ElementRef, HostListener, inject, NgZone, OnInit, Renderer2, Signal, signal, ViewChild } from '@angular/core';
import { OverlayOptions, PrimeNGConfig } from "primeng/api";
import { TranslateService } from "@ngx-translate/core";
import { LayoutService } from 'ts-ui/applayout';
import { Platform } from '@angular/cdk/platform';
import { ToastService } from 'ts-ui/toast';
import { DOCUMENT } from '@angular/common';
import { DomHandler } from 'ts-ui/common';
import { Router, Event as RouterEvent,
  NavigationStart,
  NavigationEnd,
  NavigationCancel,
  NavigationError,
  RouterLinkActive,
  RouterLink
 } from '@angular/router';
import { AppLoader } from 'ts-ui/layout';
import { TestService } from './services/test.service';

@Component({
  selector: 'ts-root',
  template: `
    <ts-app-loader #appLoader></ts-app-loader>
    <router-outlet></router-outlet>
    <div aria-live="polite" toastContainer 
    [style]="{position: 'absolute', 'z-index': 9999}"></div>
  `,
  styles: []
})
export class AppComponent implements OnInit, AfterViewInit {

  // Instead of holding a boolean value for whether the spinner
  // should show or not, we store a reference to the spinner element,
  // see template snippet below this script
  @ViewChild('appLoader', {static: false})
  appLoader: AppLoader;

  document = inject(DOCUMENT);
  windowWidth = signal<number>(0);

  private layoutService = inject(LayoutService);
  
  constructor(
    private router: Router,
    private toast: ToastService,
    private primengConfig: PrimeNGConfig,
    private translateService: TranslateService) {
    this.setupRouterLoader();
  }

  ngOnInit() {
    //this.setupRouterLoader();
    this.setupConfigApp();
  }

  private setupRouterLoader() {

    this.router.events.subscribe((event: RouterEvent) => {
      if (event instanceof NavigationStart) {
        this.setVisibleLoader(true);
      }
      else if (event instanceof NavigationEnd) {
        this.setVisibleLoader(false);
      }

      // Set loading state to false in both of the below events to hide the spinner in case a request fails
      else if (event instanceof NavigationCancel) {
        this.setVisibleLoader(false);
      }

      else if (event instanceof NavigationError) {
        this.setVisibleLoader(false);
      }
    })

  }

  private setVisibleLoader(visible: boolean): void {
     this.appLoader.visible(visible);
  }

  private setupConfigApp() {
    //const pl = !!this.platform.ANDROID ? 'is-mobile' : this.platform.IOS ? 'is-mobile' : 'pc';
    //DomHandler.addClass(this.document.documentElement, pl);

   // if (!!this.platform.ANDROID) {
      this.toast.globalConfig.position = 'top-center';
   // }

    this.layoutService?.tryAddTheme();

    //primengConfig
    this.primengConfig.ripple = true;
    //this.primengConfig.overlayOptions = this.computedOverlayOption();



    this.translateService.get('primeng').subscribe(s => this.primengConfig.setTranslation(s));
  }

  ngAfterViewInit(): void {
  }

  private get isMobile(): boolean {
    return this.windowWidth() <= 575.98;
  }




}