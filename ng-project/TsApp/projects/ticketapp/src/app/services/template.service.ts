import { Injectable } from '@angular/core';
import { ModelApi } from "./model-api.service";
import { Page, ResponseToModel } from "../models/common";
import { Template } from '../models/template';
import { Observable, map, tap } from 'rxjs';

@Injectable({ providedIn: 'root' })
export class TemplateService extends ModelApi<Template> {

  override basePath(): string {
    return `/ts-api/template`;
  }

  override resToModel(): ResponseToModel<any> {
    return json => Template.from(json);
  }

  getByUserAndCode(code: string): Observable<Page<Template>> {
    const url = this.callBasePath('/user/get-by-code');
    return this.getPage(url, {code});
  }

  getAllByUser(...entities: string[]): Observable<Page<Template>> {
    const url = this.callBasePath('/user/get-all');
    return this.getPage(url, {entities});
  }

  
  override create(data: any): Observable<Template> {
    const converterNew = this.resToModel();
    const url = this.callBasePath(`save`);
    return this.post(url, data).pipe(map(data => converterNew(data)));
  }


}
