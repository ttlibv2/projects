import { Component, OnInit, ViewChild } from '@angular/core';
import { PrimeNGConfig } from "primeng/api";
import { TranslateService } from "@ngx-translate/core";
import { LayoutService } from 'ts-ui/app-layout';

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

  constructor(
    private primengConfig: PrimeNGConfig,
    private layoutService: LayoutService,
    private translateService: TranslateService) {
  }

  ngOnInit() {
    this.layoutService.tryAddTheme();
    this.primengConfig.ripple = true;
    this.translateService.get('primeng').subscribe(s => this.primengConfig.setTranslation(s));
  }
}