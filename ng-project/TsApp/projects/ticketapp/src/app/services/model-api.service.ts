import { Injectable } from '@angular/core';
import { ClientService } from './client.service';
import { BaseModel } from '../models/base-model';
import { ClientParams, JsonObject,  ResponseToModel } from '../models/common';
import { map, Observable } from 'rxjs';
import { InjectService } from "./inject.service";
import { Page, Pageable } from 'ts-ui/helper';

export const defaultPage: Pageable = {};

@Injectable({ providedIn: 'root' })
export abstract class ModelApi<E extends BaseModel> extends ClientService {

  constructor(inject: InjectService) {
    super(inject);
  }

  abstract basePath(): string;

  abstract resToModel(): ResponseToModel<any>;

  protected callBasePath(path: string): string {
    return this.joinUrls(this.basePath(), path);
  }

  //protected responseToPage(response: any): Page<E> {
  //  const converter = this.resToModel();
  //  return Page.from(response, item => converter(item));
 // }


  protected responseToArray(items: any[]): E[] {
    const converter = this.resToModel();
    return items.map(item => converter(item));
  }

  protected getOne(path: string, params?: ClientParams, callback?: ResponseToModel<any>): Observable<any> {
    const convert = callback || this.resToModel(), url = this.callBasePath(path);
    return this.get(url, params).pipe(map((item: any) => convert(item)));
  }


  protected getArray(url: string, params?: ClientParams): Observable<E[]> {
      return this.get(url, {...params}).pipe(map((items: any[]) => this.responseToArray(items)));
  }

  protected getPage(url: string, params?: ClientParams, page?: Pageable): Observable<Page<E>> {    
    //const newPage:Pageable = {...defaultPage, ...page};
    //return this.get(url, {...params, ...newPage}).pipe(map(res => this.responseToPage(res)));
    return this.sendGetPage(url, {params, page, cbItem: this.resToModel()});
  }

   /** 
   * find all model without page
   * @param data the object filter
   * @param page  the page data
   * */
  search(data: JsonObject, page: Pageable): Observable<Page<E>> {
    const url = this.callBasePath('search');
    //return this.get(url, { ...data, ...page }).pipe(map(res => this.responseToPage(res)));
    return this.sendGetPage(url, {page, params: data, cbItem: this.resToModel()});
  }

  /** 
   * find all model without page
   * @param page  the page data
   * */
  findAll(page?: Pageable): Observable<Page<E>> {
    const url = this.callBasePath('get-all');
    //return this.get(url, page).pipe(map(res => this.responseToPage(res)));
    return this.sendGetPage(url, {page, cbItem: this.resToModel()});
  }

  /** 
   * get model without id
   * @param modelId  the id to get
   * */
  getById(modelId: number): Observable<E> {
    return this.getOne(`get-by-id/${modelId}`);
  }


  /** 
   * Update model without id
   * @param modelId  the id to update
   * @param data the data to update
   * */
  updateById(modelId: number, data: any): Observable<number> {
    const url = this.callBasePath(`update-by-id/${modelId}`);
    return this.put(url, data).pipe(map(data => data.result));
  }

  /** 
  * Delete model without id
  * @param modelId  the id to delete
  * */
  deleteById(modelId: number): Observable<{alert_msg: string, model_id: number}> {
    const url = this.callBasePath(`delete-by-id/${modelId}`);
    return this.delete(url);//.pipe(map(data => data.result));
  }

  /** 
   * Create new model 
   * @param data  the data for create
   * */
  createNew(data: any): Observable<E> {
    const converterNew = this.resToModel();
    const url = this.callBasePath(`create-new`);
    return this.post(url, data).pipe(map(data => converterNew(data)));
  }

  create(data: any): Observable<E> {
    const converterNew = this.resToModel();
    const url = this.callBasePath(`create`);
    return this.post(url, data).pipe(map(data => converterNew(data)));
  }

  protected savePost<R>(path: string, data: any, callbackResponse?:ResponseToModel<any>, params?: ClientParams): Observable<R> {
    const url = this.callBasePath(path);
    const converterNew = callbackResponse ?? this.resToModel();
    return this.post(url, data, params).pipe(map(data => converterNew(data)));
  }


 protected sendPost<R>(path: string, data: any, callbackResponse?:ResponseToModel<any>, params?: ClientParams): Observable<R> {
    const url = this.callBasePath(path);
    const converterNew = callbackResponse ?? this.resToModel();
    return this.post(url, data, params).pipe(map(data => converterNew(data)));
  }






}
