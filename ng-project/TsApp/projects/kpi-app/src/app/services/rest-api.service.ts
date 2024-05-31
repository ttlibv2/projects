import { Injectable } from '@angular/core';
import { ClientService } from './client.service';
import { BaseModel } from '../models/base-model';
import { JsonObject, Pageable, ResponseToModel } from '../models/common';
import { Observable, of, switchMap, tap } from 'rxjs';

@Injectable({ providedIn: 'root' })
export abstract class RestApiService<E extends BaseModel> extends ClientService {
  static readonly rootUrl: string = 'http://localhost:8888';

  abstract basePath(): string;
  abstract resToModel(): ResponseToModel<E>;

  protected callBasePath(path: string): string {
    if(path.startsWith('/')) path = path.substring(1);
    return this.basePath() + '/' + path;
  }

  protected convertListModel(items: any[]): Observable<E[]> {
    const converter = this.resToModel();
    const data = items.flatMap(item => converter(item));
    return of(data);
  }

  search(data: JsonObject, page: Pageable): Observable<E[]> {
    const url = this.callBasePath('search');
    return this.get(url, {...data, ...page})
      .pipe(switchMap((items:any[] )=> this.convertListModel(items)));
  }

  getAll(page: Pageable): Observable<E[]> {
    const url = this.callBasePath('get-all');
    return this.get(url, page)
      .pipe(switchMap((items:any[] )=> this.convertListModel(items)));
  }

  getById(modelId: number): Observable<E> {
    const converterNew = this.resToModel();
    const url = this.callBasePath(`get-by-id/${modelId}`);
    return this.get(url).pipe(switchMap(data => of(converterNew(data))));
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
