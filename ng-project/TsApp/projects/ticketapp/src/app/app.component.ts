import { Component, inject, OnInit, ViewChild } from '@angular/core';
import { PrimeNGConfig } from "primeng/api";
import { TranslateService } from "@ngx-translate/core";
import { LayoutService } from 'ts-ui/app-layout';
import { Platform } from '@angular/cdk/platform';
import { ToastService } from 'ts-ui/toast';
import { toastConfig } from './constant';
import { DOCUMENT } from '@angular/common';
import { DomHandler } from 'primeng/dom';

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

  constructor(
    private platform: Platform,
    private toast: ToastService,
    private primengConfig: PrimeNGConfig,
    private layoutService: LayoutService,
    private translateService: TranslateService) {
  }

  ngOnInit() {
    const pl = !!this.platform.ANDROID ? 'is-mobile' : this.platform.IOS ? 'is-mobile' : 'pc';
    DomHandler.addClass(this.document.documentElement, pl);

    if (!!this.platform.ANDROID) {
      this.toast.globalConfig.position = 'bottom-right';
    }

    this.layoutService.tryAddTheme();
    this.primengConfig.ripple = true;
    this.translateService.get('primeng').subscribe(s => this.primengConfig.setTranslation(s));
  }
}