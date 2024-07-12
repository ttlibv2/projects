import { Injectable } from '@angular/core';
import { ClientService } from './client.service';
import { BaseModel } from '../models/base-model';
import { ClientParams, JsonObject, Page, Pageable, ResponseToModel } from '../models/common';
import { map, Observable } from 'rxjs';
import { InjectService } from "./inject.service";

export const defaultPage: Pageable = {};

@Injectable({ providedIn: 'root' })
export abstract class ModelApi<E extends BaseModel> extends ClientService {

  constructor(inject: InjectService) {
    super(inject);
  }

  abstract basePath(): string;

  abstract resToModel(): ResponseToModel<any>;

  protected callBasePath(path: string): string {
    if (!path.startsWith('/')) path = '/' + path;
    return this.basePath() + path;
  }

  protected responseToPage(response: any): Page<E> {
    const converter = this.resToModel();
    return Page.from(response, item => converter(item));
  }

  protected getOne(path: string, params?: ClientParams): Observable<E> {
    const convert = this.resToModel(), url = this.callBasePath(path);
    return this.get(url, params).pipe(map((item: any) => convert(item)));
  }

  protected getPage(url: string, params?: ClientParams, page?: Pageable): Observable<Page<E>> {
    const newPage:Pageable = {...defaultPage, ...page};
      return this.get(url, {...params, ...newPage}).pipe(map(res => this.responseToPage(res)));
  }

   /** 
   * find all model without page
   * @param data the object filter
   * @param page  the page data
   * */
  search(data: JsonObject, page: Pageable): Observable<Page<E>> {
    const url = this.callBasePath('search');
    return this.get(url, { ...data, ...page }).pipe(map(res => this.responseToPage(res)));
  }

  /** 
   * find all model without page
   * @param page  the page data
   * */
  findAll(page?: Pageable): Observable<Page<E>> {
    const url = this.callBasePath('get-all');
    return this.get(url, page).pipe(map(res => this.responseToPage(res)));
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
  deleteById(modelId: number): Observable<boolean> {
    const url = this.callBasePath(`delete-by-id/${modelId}`);
    return this.delete(url).pipe(map(data => data.result));
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

  save(data: any): Observable<E> {
    const converterNew = this.resToModel();
    const url = this.callBasePath(`create`);
    return this.post(url, data).pipe(map(data => converterNew(data)));
  }




}
