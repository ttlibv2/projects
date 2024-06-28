import {HttpClient, HttpErrorResponse} from '@angular/common/http';
import { Injectable, } from '@angular/core';
import {ClientParams, ErrorResponse, Page } from '../models/common';
import {catchError, map, Observable, of, switchMap, tap, throwError} from 'rxjs';
import {Objects} from "ts-helper";
import {InjectService} from "./inject.service";
import {Router} from "@angular/router";
import {LocalDbService} from "./local-db.service";
import {ToastMessage} from "./toast.service";
import { LoggerService } from 'ts-logger';
import { StorageService } from './storage.service';

@Injectable({ providedIn: 'root' })
export class ClientService {

  constructor(protected inject: InjectService) { }

  protected get http() : HttpClient {
    return this.inject.http;
  }

  protected get config(): StorageService {
    return this.inject.config;
  }

  protected get router(): Router {
    return this.inject.get(Router);
  }

  protected get db(): LocalDbService {
    return this.inject.get(LocalDbService);
  }

  protected get logger(): LoggerService {
    return this.inject.get(LoggerService);
  }

  protected searchGet(url: string, params?: ClientParams): Observable<Page<any>> {
    return this.send('get', url, {params}).pipe(map(s => new Page().update(s)));
  }

  protected get(url: string, params?: ClientParams): Observable<any> {
    return this.send('get', url, {params});
  }

  protected post(url: string, body: any, params?: ClientParams): Observable<any> {
    return this.send('post', url, {body, params});
  }

  protected delete(url: string, params?: ClientParams): Observable<any> {
    return this.send('delete', url, { params});
  }

  protected put(url: string, body: any, params?: ClientParams): Observable<any> {
    return this.send('put', url, {body, params});
  }

  protected send(method: string, url: string, options?: any): Observable<any> {
    url = Objects.isUrl(url) ? url : this.config.baseUrl + (url.startsWith('/') ? '' : '/') + url;

    // get
    const showError: boolean = options['showError'] ?? true;
    delete options['showError'];

    return this.http.request(method, url, options).pipe(
      catchError((error, caught) => this.handlerError(error, url, showError)),
      switchMap(response => this.handlerResponse(response, url, showError))
    );
  }

  /**
   * Handler error client
   * */
  protected handlerError(err: any, url: string, showError: boolean = true): Observable<any> {
    let object: ToastMessage = {};

    if(err instanceof HttpErrorResponse) {
      const error: ErrorResponse = err.error ?? {};
      const baseUrl = (text: string) => `<a href="${new URL(url).host}" target="_blank" rel="noopener noreferrer">${text}</a>`;

      object = {
        details: error,
        summary: error?.summary,
        code: error?.code
      };


      if(err.status === 0) {
        object.code = 'disconnect';
        object.timeOut = 20000;
        object.title = 'Lỗi không kết nối được tới máy chủ';
        object.summary = `Vui lòng kiểm tra kết nối: ${baseUrl('Kiểm tra')}`;
      }

      else if(err.status === 500) {
        object.code = error.code === 'e_500' ? 'e_server' : error.code;
        object.summary = `Đã xảy ra lỗi từ ${baseUrl('máy chủ')} <br>-> (${object.summary})`;
      }

    }

    if(showError) {
      this.inject.toast.error(object);
    }

    if(object.code.startsWith('jwt.')) {
      this.config.set_loginToken(null).pipe(
        tap(_ => this.router.navigate(['/']))
      ).subscribe();
    }

    return throwError(() => object);
  }

  /**
   * Handle response data
   * */
  protected handlerResponse(response: any, url: string,showError: boolean): Observable<any> {
    if(Objects.notBlank(response?.error)) {
      const object = {summary: response.error_desc, code: response.error_code};
      if(showError) this.inject.toast.error(object);
      return throwError(() => object)
    }

    else if(Objects.notBlank(response['alert_msg'])) {
     this.inject.toast.info({summary: response['alert_msg']});
    }

    return of(response);
  }

}