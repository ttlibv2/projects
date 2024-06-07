import { Injectable } from '@angular/core';
import {TranslateService} from "@ngx-translate/core";
import {Observable} from "rxjs";

@Injectable({providedIn: 'root'})
export class I18nService {

  constructor(private translate: TranslateService) { }

  get(key: string | Array<string>, interpolateParams?: Object): Observable<string | any> {
    return this.translate.get(key, interpolateParams);
  }



}
