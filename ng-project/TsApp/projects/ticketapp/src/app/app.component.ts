import { Component, computed, HostListener, inject, OnInit, Signal, signal, ViewChild } from '@angular/core';
import { OverlayOptions, PrimeNGConfig } from "primeng/api";
import { TranslateService } from "@ngx-translate/core";
import { LayoutService } from 'ts-ui/app-layout';
import { Platform } from '@angular/cdk/platform';
import { ToastService } from 'ts-ui/toast';
import { toastConfig } from './constant';
import { DOCUMENT } from '@angular/common';
import { DomHandler } from 'ts-ui/core';

@Component({
  selector: 'ts-root',
  template: `
    <router-outlet></router-outlet>
   <div aria-live="polite" toastContainer 
    [style]="{position: 'absolute', 'z-index': 9999}"></div>
  `,
  styles: []
})
export class AppComponent implements OnInit {
  document = inject(DOCUMENT);
  windowWidth = signal<number>(0);

  constructor(
    private platform: Platform,
    private toast: ToastService,
    private primengConfig: PrimeNGConfig,
    private layoutService: LayoutService,
    private translateService: TranslateService) {
  }

  @HostListener('window:resize', ['$event'])
  private onWindowResize(event: any) {
    this.windowWidth.set(window.innerWidth);
  }

  ngOnInit() {
    const pl = !!this.platform.ANDROID ? 'is-mobile' : this.platform.IOS ? 'is-mobile' : 'pc';
    DomHandler.addClass(this.document.documentElement, pl);

    if (!!this.platform.ANDROID) {
      this.toast.globalConfig.position = 'bottom-right';
    }

    this.layoutService.tryAddTheme();

    //primengConfig
    this.primengConfig.ripple = true;
    this.primengConfig.overlayOptions = this.computedOverlayOption();



    this.translateService.get('primeng').subscribe(s => this.primengConfig.setTranslation(s));
  }

  private get isMobile(): boolean {
    return this.windowWidth() <= 575.98;
  }

  private get computedOverlayOption(): Signal<OverlayOptions> {
    return computed(() => {
      const appendTo = this.isMobile ? 'body' : undefined;
      const options: OverlayOptions = {
        appendTo,
        onShow: evt => DomHandler.alignOverlay(evt.overlay, evt.target, appendTo)
      };
      return options;
    });
  }
}