import { Injectable } from '@angular/core';
import {ModelApi} from "./model-api.service";
import {ApiInfo, EMPTY_PWD, UserApiInfo} from "../models/api-info";
import {ResponseToModel} from "../models/common";
import {Observable, of, switchMap, tap} from "rxjs";



@Injectable({providedIn: 'root'})
export class ApiInfoService extends ModelApi<ApiInfo> {

  basePath(): string {
    return "/ts-api/api-info";
  }

  resToModel(): ResponseToModel<any> {
    return json => ApiInfo.from(json);
  }

  getByCode(code: string, userid?: number): Observable<ApiInfo> {
    const url = (`get-by-code/${code}`);
    return this.getOne(url, {userid});//.pipe(tap(res => of(ApiInfo.from(res))));
  }

  saveUserApi(apiCode: string, info: UserApiInfo): Observable<UserApiInfo> {
    if(info.password === EMPTY_PWD) info.password = undefined;
    const url = (`save-user/${apiCode}`);
    const data =  {api_code: apiCode, ...info};
    return this.savePost<UserApiInfo>(url, data, res => UserApiInfo.from(res));
  }

  checkLogin(): Observable<any> {
    const url = `/od-api/user/login`;
    return this.post(url, {}).pipe(switchMap(res => of(res['result'])));
  }
}
