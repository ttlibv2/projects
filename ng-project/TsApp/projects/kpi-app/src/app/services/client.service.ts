import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import {ClientParams, JsonObject} from '../models/common';
import {catchError, Observable, switchMap, throwError} from 'rxjs';
import {Toast} from "primeng/toast";

@Injectable({ providedIn: 'root' })
export class ClientService {

  constructor(protected http: HttpClient, private toast: Toast) { }

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
    return this.http.request(method, url, options).pipe(
      catchError((error, caught) => this.handlerError(error, caught)),
      switchMap(response => this.handlerResponse(response))
    );
  }

  /**
   * Handler error client
   * */
  protected handlerError(err: any, caught: Observable<any>): Observable<any> {
    return throwError(() => {});
  }

  /**
   * Handle response data
   * */
  protected handlerResponse(response: any): Observable<any> {
    return null;
  }

}
