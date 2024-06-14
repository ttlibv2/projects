import {Component, OnInit, ViewEncapsulation} from '@angular/core';
import {PrimeNGConfig} from "primeng/api";
import {TranslateService} from "@ngx-translate/core";
import {LocalDbService} from "./services/local-db.service";

@Component({
  selector: 'ts-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss'],
  encapsulation: ViewEncapsulation.None
})
export class AppComponent implements OnInit {

  constructor(private primengConfig: PrimeNGConfig,
              private db: LocalDbService,
              private translateService: TranslateService) {
  }

  ngOnInit() {
    console.log(`log`);
    this.primengConfig.ripple = true;
   // this.translateService.get('tsapp').subscribe(s => this.appCfg.set_translation(s));
    this.translateService.get('primeng').subscribe(s => this.primengConfig.setTranslation(s));
  }

}
