import {inject, Injectable} from '@angular/core';
import {RestApiService} from "./rest-api.service";
import {User} from "../models/user";
import {ResponseToModel} from "../models/common";
import {EMPTY, Observable, tap} from "rxjs";
import {InjectService} from "./inject.service";
import {ClientService} from "./client.service";

@Injectable({providedIn: 'root'})
export class UserService extends RestApiService<User> {


  override basePath(): string {
    return "ts.user";
  }

  override resToModel(): ResponseToModel<User> {
    return json => User.from(json);
  }

  getConfig(): Observable<any> {
    const url = this.callBasePath('get-config');
    return this.get(url).pipe(tap(res => this.config.set_userCfg(res)));
  }

  getProfile(): Observable<User> {
    const url = this.callBasePath('get-profile');
    return this.get(url).pipe(tap(res => this.config.set_userInfo(res)));
  }

  override updateById(modelId: number, data: User): Observable<number> {
    return super.updateById(modelId, data)
      .pipe(tap(res => this.config.set_userInfo(data)));
  }

}
