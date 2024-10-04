import { Injectable } from '@angular/core';
import { ModelApi } from "./model-api.service";
import { ApiInfo, UserApi } from "../models/api-info";
import { ResponseToModel } from "../models/common";
import { Observable } from "rxjs";
import { Pageable, Page, Objects } from 'ts-ui/helper';
import {App_UID} from "../models/cls-var";

export type SaveAction = 'copy_api' | 'edit_api' | 'edit_user'; 

export interface SaveDto {
  action: SaveAction;
  source_id: number;
  api_info?: Partial<ApiInfo>;
  user_api?: Partial<UserApi>;
}

@Injectable({ providedIn: 'root' })
export class ApiInfoService extends ModelApi<ApiInfo> {
  

  basePath(): string {
    return "/ts-api/api-info";
  }

  resToModel(): ResponseToModel<any> {
    return json => ApiInfo.from(json);
  }

  /** 
   * find all model without page
   * @param query the object filter
   * @param page  the page data
   * */  
  override search(query?: {uid?: string, name?: string, api_id?: number}, page?: Pageable): Observable<Page<ApiInfo>> {
    const url = this.callBasePath('get-in-one');
    query = Objects.extractValueNotNull(query);
    return this.getPage(url, query, page);
  }

  /**
   * Find api without app_name
   * @param name the app name to find
   * @param credential if true then load user credential
   * */
  findByAppName(name: string, credential: boolean = false): Observable<ApiInfo> {
    return this.getOne(`find-by-name/${name}`, {credential});
  }

  /**
   * Find api without app_uid
   * @param uid the app uid to find
   * @param credential if true then load user credential
   * */
  findByAppUID(uid: App_UID, credential: boolean = false): Observable<ApiInfo[]> {
    return this.getArray(`find-by-uid/${uid}`, {credential});
  }

  /** 
   * Load user api by app_name
   * @param appName the app name
   * */
  loadUserByAppName(appName: string): Observable<UserApi> {
    const url = this.callBasePath(`user/find-by-name/${appName}`);
    return this.getOne(url, {}, item => UserApi.from(item));
  }

  /**
   * Check user api login by app_name
   * @param appName the app name
   * */
  checkLoginByAppName(appName: string): Observable<UserApi> {
    const url = this.callBasePath(`user/check-api-login/${appName}`);
    return this.sendPost(url, {}, item => UserApi.from(item));
  }

  /** 
   * Update menu link for api 
   * @param app_name the app name
   * */
  updateMenuLink(app_name: string): Observable<any> {
    const url = this.callBasePath(`user/get-menu-links/${app_name}`);
    return this.sendPost(url, {}, item => item);
  }

  saveAll(info: SaveDto): Observable<ApiInfo> {
    const url = this.callBasePath('user/save-info')
    return this.savePost(url, info);
  }


}