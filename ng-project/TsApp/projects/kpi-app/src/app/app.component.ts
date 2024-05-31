import {Component, OnInit, ViewEncapsulation} from '@angular/core';
import {PrimeNGConfig} from "primeng/api";
import {TranslateService} from "@ngx-translate/core";

@Component({
  selector: 'ts-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss'],
  encapsulation: ViewEncapsulation.None
})
export class AppComponent implements OnInit  {

  constructor(private primengConfig: PrimeNGConfig,
              private translateService: TranslateService) {
  }

  ngOnInit() {
    this.primengConfig.ripple = true;
    this.translateService.get('primeng')
      .subscribe(s => this.primengConfig.setTranslation(s));
  }

}
