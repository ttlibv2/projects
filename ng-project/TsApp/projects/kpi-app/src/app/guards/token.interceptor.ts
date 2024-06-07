import { HttpInterceptorFn } from '@angular/common/http';
import {AuthService} from "../services/auth.service";
import {inject} from "@angular/core";
import {ConfigService} from "../services/app-config.service";

export const tokenInterceptor: HttpInterceptorFn = (req, next) => {
  const auth = inject(AuthService), config = inject(ConfigService);
  if(auth.isLogin()) {
    const token = config.get_user().token;
    req = req.clone({
      setHeaders: {
        'Authorization': `${token.token_type} ${token.access_token}`
      }
    });

  }
  return next(req);
};
