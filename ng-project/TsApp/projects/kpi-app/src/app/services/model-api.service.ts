import {inject, Injectable} from '@angular/core';
import { ClientService } from './client.service';
import { BaseModel } from '../models/base-model';
import {ClientParams, JsonObject, Page, Pageable, ResponseToModel} from '../models/common';
import { Observable, of, switchMap, tap } from 'rxjs';
import {InjectService} from "./inject.service";

@Injectable({ providedIn: 'root' })
export abstract class ModelApi<E extends BaseModel> extends ClientService {
 // static readonly rootUrl: string = 'http://localhost:8888';

  constructor(inject: InjectService) {
    super(inject);
  }

  abstract basePath(): string ;

  abstract resToModel(): ResponseToModel<any> ;

  protected callBasePath(path: string): string {
    if(!path.startsWith('/')) path = '/' + path;
    return this.basePath() + path;
  }

  protected convertListModel(response: any): Observable<Page<E>> {
    const converter = this.resToModel();
    const data = response['data'].flatMap((item:any) => converter(item));
    return of({...response, data});
  }

  protected getOne(path: string, params?: ClientParams) {
    const convert = this.resToModel(), url = this.callBasePath(path);
    return this.get(url, params).pipe(switchMap((item:any )=> of(convert(item))));
  }

  search(data: JsonObject, page: Pageable): Observable<Page<E>> {
    const url = this.callBasePath('search');
    return this.get(url, {...data, ...page})
      .pipe(switchMap((items:any[] )=> this.convertListModel(items)));
  }

  getAll(page?: Pageable): Observable<Page<E>> {
    const url = this.callBasePath('get-all');
    return this.get(url, page)
      .pipe(switchMap((items:any[] )=> this.convertListModel(items)));
  }

  getById(modelId: number): Observable<E> {
    return this.getOne(`get-by-id/${modelId}`);
  }

  updateById(modelId: number, data: any): Observable<number> {
    const converterNew = this.resToModel();
    const url = this.callBasePath(`update-by-id/${modelId}`);
    return this.put(url, data).pipe(switchMap(data => of(data.result)));
  }

  deleteById(modelId: number): Observable<boolean> {
    const converterNew = this.resToModel();
    const url = this.callBasePath(`delete-by-id/${modelId}`);
    return this.delete(url).pipe(switchMap(data => of(data.result)));
  }

  createNew(data: any): Observable<E> {
    const converterNew = this.resToModel();
    const url = this.callBasePath(`create-new`);
    return this.post(url, data).pipe(switchMap(data => of(converterNew(data))));
  }





}
