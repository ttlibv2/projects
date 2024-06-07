import {HttpClient, HttpErrorResponse} from '@angular/common/http';
import { Injectable, } from '@angular/core';
import {ClientParams, MessageObj, } from '../models/common';
import {catchError, Observable, of, switchMap, throwError} from 'rxjs';
import {ConfigService} from "./app-config.service";
import {Objects} from "../utils/objects";
import {InjectService} from "./inject.service";

@Injectable({ providedIn: 'root' })
export class ClientService {

  constructor(protected inject: InjectService) { }

  protected get http() : HttpClient {
    return this.inject.http;
  }

  protected get config(): ConfigService {
    return this.inject.config;
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
    url = Objects.isUrl(url) ? url : this.config.get_baseUrl() + (url.startsWith('/') ? '' : '/') + url;

    // get
    const showError: boolean = options['showError'] ?? true;
    delete options['showError'];

    return this.http.request(method, url, options).pipe(
      catchError((error, caught) => this.handlerError(error, showError)),
      switchMap(response => this.handlerResponse(response, showError))
    );
  }

  /**
   * Handler error client
   * */
  protected handlerError(err: any, showError: boolean): Observable<any> {
    const object: MessageObj = {code: 'unknown', details: []};
    const url = this.config.get_baseUrl();

    if(err instanceof HttpErrorResponse) {
      object.code = err?.error?.code;
      object.summary = err?.error?.message;

      if(err.status === 0) {
        object.code = 'disconnect';
        object.summary = 'Lỗi không kết nối được tới máy chủ';
        object.details = [
          `Vui lòng kiểm tra kết nối: <a href="${url}" target="_blank" rel="noopener noreferrer">Kiểm tra</a>`,
        ];
      }
    }

    if(showError) {
      this.inject.toast.error(object);
    }

    return throwError(() => object);
  }

  /**
   * Handle response data
   * */
  protected handlerResponse(response: any, showError: boolean): Observable<any> {
    if(Objects.notBlank(response?.error)) {
      const object: MessageObj = {summary: response.error_desc, code: response.error_code};
      if(showError) this.inject.toast.error(object);
      return throwError(() => object)
    }

    return of(response);
  }

}
