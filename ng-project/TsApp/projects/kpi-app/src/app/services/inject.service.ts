import {Injectable, InjectFlags, InjectOptions, Injector, ProviderToken} from "@angular/core";
import {HttpClient} from "@angular/common/http";
import {Toast} from "primeng/toast";
import {ConfigService} from "./app-config.service";
import {MessageService} from "primeng/api";
import {DialogService} from "primeng/dynamicdialog";
import {ToastService} from "./toast.message";
import {UserService} from "./user.service";

@Injectable({providedIn: 'root'})
export class InjectService {

  constructor(protected inject: Injector) {
  }

  get http(): HttpClient {
    return this.inject.get(HttpClient);
  }

  get toast(): ToastService {
    return this.inject.get(ToastService);
  }

  get config(): ConfigService {
    return this.inject.get(ConfigService);
  }

  get dialog(): DialogService {
    return this.inject.get(DialogService);
  }

  // get user(): UserService {
  //   return this.inject.get(UserService);
  // }

  get<T>(token: ProviderToken<T>, notFoundValue?: T, options?: InjectOptions): T {
    return this.inject.get(token, notFoundValue, options);
  }
}
