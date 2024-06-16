import { Component } from '@angular/core';
import {PrimeNGConfig} from "primeng/api";
import {TranslateService} from "@ngx-translate/core";

@Component({
  selector: 'ts-root',
  template: `
    <router-outlet></router-outlet>
    <p-toast key="TOAST_MESSAGE">
      <ng-template let-message pTemplate="message">
        <div class="flex flex-1 gap-2">
          <p-inputIcon styleClass="pi pi-spinner pi-spin" />
          <div class="flex flex-column flex-1">
            <div [innerHTML]="message.summary"></div>
            <div [innerHTML]="message.detail"></div>
          </div>
        </div>
      </ng-template>
    </p-toast>
  `,
  styles: []
})
export class AppComponent {

  constructor(private primengConfig: PrimeNGConfig,
              private translateService: TranslateService) {
  }

  ngOnInit() {
    console.log(`log`);
    this.primengConfig.ripple = true;
    // this.translateService.get('tsapp').subscribe(s => this.appCfg.set_translation(s));
    this.translateService.get('primeng').subscribe(s => this.primengConfig.setTranslation(s));
  }
}
