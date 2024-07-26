import {CanActivateFn, Router} from '@angular/router';
import {inject} from "@angular/core";
import {StorageService} from "../services/storage.service";
import { authUri } from '../constant';

export const userGuard: CanActivateFn = (route, state) => {
 const isLogin = inject(StorageService).isLogin;
 const result = isLogin ? true : inject(Router).navigate([authUri]);
 console.log('userGuard: ', result);
 return result;
};