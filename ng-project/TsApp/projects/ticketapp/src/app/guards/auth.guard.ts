import {CanActivateFn, Router} from '@angular/router';
import {inject} from "@angular/core";
import {StorageService} from "../services/storage.service";
import {routerUrl} from '../constant';

export const userGuard: CanActivateFn = (route, state) => {
 const isLogin = inject(StorageService).isLogin;
 console.log('isLogin: ', isLogin);
 return isLogin ? true : inject(Router).navigate([routerUrl.signinUrl]);
};