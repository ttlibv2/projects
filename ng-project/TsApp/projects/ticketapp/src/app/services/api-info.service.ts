import { Injectable } from '@angular/core';
import { ModelApi } from "./model-api.service";
import { ApiInfo, UserApi } from "../models/api-info";
import { JsonObject, ResponseToModel } from "../models/common";
import { Observable } from "rxjs";
import { Pageable, Page, Objects } from 'ts-ui/helper';
import { ClsUser } from '../models/od-cls';


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
   * @param data the object filter
   * @param page  the page data
   * */  
  override search(query?: {suid?: string, sname?: string}, page?: Pageable): Observable<Page<ApiInfo>> {
    query = Objects.extractValueNotNull(query);
    return super.search(query, page);
  }

  /**
   * Find api without service_name
   * @param name the service name to find
   * */
  findByServiceName(name: string): Observable<ApiInfo> {
    return this.getOne(`find-by-name/${name}`);
  }

  /**
   * Find api without service_uid
   * @param uid the service uid to find
   * */
  findByServiceUID(uid: string): Observable<ApiInfo[]> {
    return this.getArray(`find-by-uid/${uid}`);
  }

  /** 
   * Load user api by service_name 
   * @param sname the service name
   * */
  loadUserBySName(sname: string): Observable<UserApi> {
    const url = this.callBasePath(`user/find-by-name/${sname}`);
    return this.getOne(url, {}, item => UserApi.from(item));
  }

  /**
   * Check user api login by service_name
   * @param sname the service name
   * */
  checkLoginApiBySName(sname: string): Observable<UserApi> {
    const url = this.callBasePath(`user/check-api-login/${sname}`);
    return this.sendPost(url, {}, item => UserApi.from(item));
  }

  /** 
   * Update menu link for api 
   * @param sname the service name
   * */
  getMenuLink(sname: string): Observable<any> {
    const url = this.callBasePath(`user/get-menu-links/${sname}`);
    return this.sendPost(url, {}, item => item);
  }

  saveAll(info: SaveDto): Observable<ApiInfo> {
    const url = this.callBasePath('user/save-info')
    return this.savePost(url, info);
  }


}

export interface SaveDto {
  api_id?: number;
  api_info: Partial<ApiInfo>;
  user_api: Partial<UserApi>;
}
