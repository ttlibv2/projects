import {HttpInterceptorFn, HttpRequest} from '@angular/common/http';
import {inject} from "@angular/core";
import {ConfigService} from "../services/config.service";
import { AuthToken } from '../models/user';
import { Objects } from '../utils/objects';

export const tokenInterceptor: HttpInterceptorFn = (request, next) => {
  const token = inject(ConfigService).get_authToken();
  const newRequest =  Objects.isEmpty(token) ? request : cloneReq(request, token);
 return next(newRequest);
};

function cloneReq(request: HttpRequest<any>, token: AuthToken): HttpRequest<any> {
  return request.clone({
    setHeaders: {
      'Authorization': `${token.token_type} ${token.access_token}`
    }
  });
}
