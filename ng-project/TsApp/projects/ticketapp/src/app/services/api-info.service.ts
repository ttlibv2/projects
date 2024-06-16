import { Injectable } from '@angular/core';
import {ModelApi} from "./model-api.service";
import {ApiInfo, UserApiInfo} from "../models/api-info";
import {ResponseToModel} from "../models/common";
import {Observable, of, switchMap, tap} from "rxjs";

@Injectable({providedIn: 'root'})
export class ApiInfoService extends ModelApi<ApiInfo> {

  basePath(): string {
    return "/ts-api/api-info";
  }

  resToModel(): ResponseToModel<any> {
    return json => new ApiInfo().update(json);
  }

  getUserByCode(code: string): Observable<UserApiInfo> {
    const url = this.callBasePath(`/user/get-by-code/${code}`);
    return this.get(url).pipe(tap(res => of(UserApiInfo.from(res))));
  }

  saveUserApi(apiCode: string, info: UserApiInfo): Observable<UserApiInfo> {
    const url = this.callBasePath(`/user/save-info/${apiCode}`);
    return this.post(url, {api_code: apiCode, ...info}).pipe(tap(res => of(UserApiInfo.from(res))));
  }

  checkLogin(): Observable<any> {
    const url = `/od-api/user/login`;
    return this.post(url, {}).pipe(switchMap(res => of(res['result'])));
  }
}
