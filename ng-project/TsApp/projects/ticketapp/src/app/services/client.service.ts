import {HttpClient, HttpErrorResponse} from '@angular/common/http';
import { Injectable, } from '@angular/core';
import {ClientParams, ErrorResponse, Page } from '../models/common';
import {catchError, map, Observable, of, switchMap, tap, throwError} from 'rxjs';
import {Objects} from "ts-ui/helper";
import {InjectService} from "./inject.service";
import {Router} from "@angular/router";
import { StorageService } from './storage.service';
import { ApiInfoComponent } from '../views/api-info/api-info.component';
import {ToastMessage} from "ts-ui/toast";
import {DBService} from "ts-ui/local-db";
import {LoggerService} from "ts-ui/logger";

@Injectable({ providedIn: 'root' })
export class ClientService {

  constructor(protected inject: InjectService) { }

  protected get http() : HttpClient {
    return this.inject.http;
  }

  protected get storage(): StorageService {
    return this.inject.storage;
  }

  protected get router(): Router {
    return this.inject.get(Router);
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
    url = Objects.isUrl(url) ? url : this.storage.baseUrl + (url.startsWith('/') ? '' : '/') + url;

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
    let alertType: 'toast' | 'modal' = 'toast';
    let errorCode: string = 'undefined';

    if(err instanceof HttpErrorResponse) {
      const error: ErrorResponse = err.error ?? {};
      const baseUrl = (text: string) => `<a href="${new URL(url).host}" target="_blank" rel="noopener noreferrer">${text}</a>`;

      errorCode = error?.code;
      object.message = error?.summary;
      object.title = 'Thông báo !!';

      if(err.status === 0) {
        errorCode = 'disconnect';
        object.message = 'Lỗi không kết nối được tới máy chủ';
        object.detail = `Vui lòng kiểm tra kết nối: ${baseUrl('Kiểm tra')}`;
        object.timeOut = 10 * 1000;
      }

      else if(err.status === 500) {
        errorCode = error.code === 'e_500' ? 'e_server' : error.code;
        object.message = `Đã xảy ra lỗi từ ${baseUrl('máy chủ')} <br>-> (${object.message})`;
        object.timeOut = 10 * 1000;
       // this.inject.toast.error(object);
       // showError = false;
      }

      else if(err.status === 401 && errorCode === 'ts_api') {
        const {ts_api, ts_code} = error.details ?? {};
        this.showUpdateApiToken(object.message, ts_api);
        showError = false;
      }

      else if(err.status === 403 && error?.summary === 'Access Denied') {
        object.message = `Bạn không có quyền vào chức năng này ${errorCode} `;
      }

    }

    // view alert error
    if(showError === true){
      this.showError(object, true, alertType);
    }

    // logout if session expired
    if(errorCode.startsWith('jwt.')) {
      this.storage.set_loginToken(null).pipe(
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
      const object: Partial<ToastMessage> = {message: response.error_desc, detail: 'Code: '+response.error_code};
      this.showError(object, showError, 'toast');
      return throwError(() => object)
    }

    else if(Objects.notBlank(response['alert_msg'])) {
     this.inject.toast.info( response['alert_msg']);
    }

    return of(response);
  }

  private showError(object: Partial<ToastMessage>,visible: boolean, alertType: 'toast' | 'modal' = 'toast') {
    //console.log('showError', object);
    if(visible) this.inject.toast.error(object);
  }

  private showUpdateApiToken(summary: string, apiCode: string) {
    this.inject.alert.danger({        
      title: 'Cảnh báo !!',
      okLabel: 'Kiểm tra',
      cancelLabel: 'Không kiểm tra',
      summary: summary,
      okClick: evt => {
        evt.dynamicRef.close();
        ApiInfoComponent.showDialog(this.inject.modal, apiCode);
      }
    })
  }

}