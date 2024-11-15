import {HttpClient, HttpErrorResponse} from '@angular/common/http';
import { ErrorHandler, inject, Injectable, } from '@angular/core';
import {ClientParams, ErrorResponse } from '../models/common';
import {catchError, map, Observable, of, switchMap, tap, throwError} from 'rxjs';
import {Callback, Objects, Pageable, Page} from "ts-ui/helper";
import {InjectService} from "./inject.service";
import {Router} from "@angular/router";
import { StorageService } from './storage.service';
import { ApiInfoComponent } from '../views/api-info/api-info.component';
import {ToastMessage} from "ts-ui/toast";
import {LoggerService} from "ts-ui/logger";
import { UserService } from './user.service';
import { Alert } from 'ts-ui/alert';

export const defaultPage: Pageable = {};

@Injectable({ providedIn: 'root' })
export class ClientService {
  private errorHandler = inject(ErrorHandler);

  constructor(protected inject2: InjectService) { }

  protected get http() : HttpClient {
    return this.inject2.http;
  }

  protected get storage(): StorageService {
    return this.inject2.storage;
  }

  protected get router(): Router {
    return this.inject2.get(Router);
  }

  protected get logger(): LoggerService {
    return this.inject2.get(LoggerService);
  }

  protected joinUrls(rootPath: string, ...paths: string[]): string {
    let lastPath = paths.map(p => p.endsWith('/') ? p.substring(0, p.length-1) : p).join('/');
    return lastPath.startsWith(rootPath) ? lastPath : rootPath + '/' + lastPath;
  }

  protected sendGetPage<E>(url: string, options?: {params?: ClientParams, page?: Pageable, cbItem: Callback<any, E>}): Observable<Page<E>> {
    const newPage:Pageable = {...defaultPage, ...options?.page};
    const query = {...options?.params, ...newPage};
    const newCbItem = (item:any) => options?.cbItem(item) || item;
    const convert = (res:any) => Page.from(res, item => newCbItem(item));
    return this.get(url, query).pipe(map(res => convert(res)));
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

  protected clientPost(url: string, body: any, options?: any): Observable<any> {
    return this.send('post', url, {...options, body});
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
      this.errorHandler.handleError(`[handlerError] ${url} ${JSON.stringify(err.error)}`);

      const error: ErrorResponse = err.error ?? {};
      const baseUrl = (text: string) => `<a href="${new URL(url).host}" target="_blank" rel="noopener noreferrer">${text}</a>`;

      errorCode = error?.code;
      object.message = error?.summary;
      object.title = 'Thông báo !!';

      if(err.status === 0) {
        errorCode = 'e_server';//'disconnect';
        object.message = 'Lỗi không kết nối được tới máy chủ';
        object.detail = `Vui lòng kiểm tra kết nối: ${baseUrl(' Tại đây ')}`;
        object.timeOut = 10 * 1000;
        showError = true;
      }

      else if(err.status === 500) {
        errorCode = error.code === 'e_500' ? 'e_server' : error.code;
        object.message = `Đã xảy ra lỗi từ ${baseUrl('máy chủ')}`;
        object.timeOut = 10 * 1000;   
        showError = true;     
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

     // logout if session expired
     if(errorCode.startsWith('jwt.')) {
      this.inject2.get(Alert).warning({
        title: 'Thông báo đăng xuất',
        summary: 'Phiên làm việc của bạn đã hết hạn. Vui lòng đăng nhập lại !!',
        okClick: _ => this.storage.set_loginToken(null).pipe(
          tap(_ => this.router.navigate(['/']))
        ).subscribe()
      })

      // this.inject.get(UserService).signout().pipe(
      //   tap(_ => this.router.navigate(['/']))
      // ).subscribe();

      // this.storage.set_loginToken(null).pipe(
      //   tap(_ => this.router.navigate(['/']))
      // ).subscribe();
      
    }

    // view alert error
    else if(showError === true){
      this.showError(object, true, alertType);
    }

   
    object.code = object.code ?? errorCode;
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
     this.inject2.toast.info( response['alert_msg']);
    }

    return of(response);
  }

  private showError(object: Partial<ToastMessage>,visible: boolean, alertType: 'toast' | 'modal' = 'toast') {
    if(visible) {
      this.inject2.toast.error(object);
    }
  }

  private showUpdateApiToken(summary: string, app_name: string) {
    this.inject2.alert.danger({        
      title: 'Cảnh báo !!',
      okLabel: 'Kiểm tra',
      cancelLabel: 'Không kiểm tra',
      summary: summary,
      okClick: evt => {
        evt.dynamicRef.close();
        ApiInfoComponent.showDialog(this.inject2.modal, {name: app_name});
      }
    })
  }

}