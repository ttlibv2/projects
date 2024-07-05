import {CanActivateFn, Router} from '@angular/router';
import {inject} from "@angular/core";
import {StorageService} from "../services/storage.service";

export const userGuard: CanActivateFn = (route, state) => {
 const isLogin = inject(StorageService).isLogin;
 return isLogin ? true : inject(Router).navigate(['/auth/signin']);
//  return true;
};