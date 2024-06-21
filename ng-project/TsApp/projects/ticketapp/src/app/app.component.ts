import {Component, OnInit, ViewChild} from '@angular/core';
import {PrimeNGConfig} from "primeng/api";
import {TranslateService} from "@ngx-translate/core";
import {ToastContainerDirective, ToastrService} from "ngx-toastr";

@Component({
  selector: 'ts-root',
  template: `
    <router-outlet></router-outlet>
   <div aria-live="polite" class="aaaaaa" toastContainer 
    [style]="{position: 'absolute', 'z-index': 9999}"></div>
  `,
  styles: []
})
export class AppComponent implements OnInit {

  // @ViewChild(ToastContainerDirective, { static: true })
  // toastContainer: ToastContainerDirective;

  constructor(private primengConfig: PrimeNGConfig,
              private toastService: ToastrService,
              private translateService: TranslateService) {

  }

  ngOnInit() {
    console.log(`log`);
    // this.toastService.overlayContainer = this.toastContainer;
    this.primengConfig.ripple = true;
    this.translateService.get('primeng').subscribe(s => this.primengConfig.setTranslation(s));
  }
}