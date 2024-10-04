import {Injectable} from '@angular/core';
import {ModelApi} from "./model-api.service";
import {User} from "../models/user";
import {ResponseToModel} from "../models/common";
import {Observable, map, tap} from "rxjs";

@Injectable({providedIn: 'root'})
export class UserService extends ModelApi<User> {

  protected override basePath(): string {
    return "/ts-api/user";
  }

  protected override resToModel(): ResponseToModel<User> {
    return json => User.from(json);
  }

  getConfig(): Observable<User> {
    const url = this.callBasePath('get-config');
    return this.get(url).pipe(map(res => User.from(res)))
      .pipe(tap(_ => this.storage.set_loginUser(_)));
  }

  override updateById(modelId: number, data: User): Observable<number> {
    return super.updateById(modelId, data);
  }

  getProfile(): Observable<User> {
    const url = this.callBasePath(`get-profile`);
    return this.get(url).pipe(map(res => User.from(res)));
  }

  updateProfile(info: Partial<User>): Observable<User> {
    const url = this.callBasePath(`update-profile`);
    return this.savePost(url, info);
  }

  signout(): Observable<any> {
    const url = this.callBasePath(`signout`);
    return this.savePost(url, {});
  }
  
}