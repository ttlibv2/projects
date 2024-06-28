import {HttpInterceptorFn, HttpRequest} from '@angular/common/http';
import {inject} from "@angular/core";
import {Objects} from 'ts-helper';
import {StorageService} from "../services/storage.service";
import {AuthToken} from '../models/user';

export const tokenInterceptor: HttpInterceptorFn = (request, next) => {
    const token = inject(StorageService).loginToken;
    const newRequest = Objects.isEmpty(token) ? request : cloneReq(request, token);
    return next(newRequest);
};

function cloneReq(request: HttpRequest<any>, token: AuthToken): HttpRequest<any> {
    return request.clone({
        setHeaders: {
            'Authorization': `${token.token_type} ${token.access_token}`
        }
    });
}