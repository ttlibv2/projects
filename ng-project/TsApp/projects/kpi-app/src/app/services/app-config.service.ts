import {Injectable, Type} from '@angular/core';
import {StorageService} from "./storage.service";
import {Objects} from "../utils/objects";
import {BaseModel} from "../models/base-model";
import {Asserts} from "../utils/asserts";
import {AuthToken, ChkUser, RememberUser, User} from "../models/user";
const { notNull } = Objects;

@Injectable({providedIn: 'root'})
export class ConfigService {
  self: any = this;

  constructor(private storage: StorageService) {}

  get_model<E extends BaseModel>(modelType: Type<E>): E {
    Asserts.notNull(modelType, "The Type<E> is null");
    const self: any = this;
    const field = this.storage.get_nameModel(modelType);
    if(notNull(self[field])) return self[field];
    else {
      const callback = (data: any) => new modelType().update(data);
      return self[field] = this.storage.get_any(field, callback, () => new modelType());
    }
  }

  set_field(field: string, value: any) {
    this.storage.set_any(field, value);
    this.self[field] = value;
  }

  get_basic(field: string): any {
    const self: any = this;
    if(notNull(self[field])) return self[field];
    else return self[field] = this.storage.get_any(field);
  }

  get_baseUrl(): string {
    return this.get_basic('baseUrl');
  }

  set_baseUrl(baseUrl: string) {
    this.set_field('baseUrl', baseUrl);
  }

  get_user(): User {
    return this.get_model(User);
  }

  get_remember(): RememberUser {
    return this.get_model(RememberUser);
  }

  set_remember(user: ChkUser): void {
    this.get_remember().set(user).saveTo(this.storage);
  }

  set_authToken(token: AuthToken): void {
    this.get_user().set_token(token).saveTo(this.storage);
  }

  set_userCfg(config: any): void {
    this.get_user().set_config(config).saveTo(this.storage);
  }

  set_userInfo(user: User): void {
    this.get_user().update(user).saveTo(this.storage);
  }
}
