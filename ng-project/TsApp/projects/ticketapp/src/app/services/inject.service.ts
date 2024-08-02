import {Injectable, InjectOptions, Injector, ProviderToken} from "@angular/core";
import {HttpClient} from "@angular/common/http";
import {DialogService} from "primeng/dynamicdialog";
import { StorageService } from "./storage.service";
import { Alert } from "./ui/alert/alert.service";
import  { ToastService } from 'ts-ui/toast';
import {ModalService} from "./ui/model.service";

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

  get modal(): ModalService {
    return this.inject.get(ModalService);
  }

  get alert(): Alert {
    return this.inject.get(Alert);
  }

  get storage(): StorageService {
    return this.inject.get(StorageService);
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