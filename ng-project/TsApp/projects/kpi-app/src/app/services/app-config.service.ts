import { Injectable } from '@angular/core';
import {StorageService} from "./storage.service";
import {Observable} from "rxjs";
import {AppConfig, RememberUser} from "../models/app-config";

@Injectable({providedIn: 'root'})
export class AppConfigService {

  constructor(private storage: StorageService) {
  }

  read(): Observable<AppConfig> {
    return new Observable<AppConfig>(obs => {
      let object = this.storage.get('RememberUser');

      const cfg = new AppConfig();
      cfg.remember = RememberUser.from(object);

      obs.next(cfg);
      obs.complete();
    });
  }


}
