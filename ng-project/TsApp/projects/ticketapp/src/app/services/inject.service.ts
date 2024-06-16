import {Injectable, InjectOptions, Injector, ProviderToken} from "@angular/core";
import {HttpClient} from "@angular/common/http";
import {DialogService} from "primeng/dynamicdialog";
import {ToastService} from "./toast.service";
import { ConfigService } from "./config.service";

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
