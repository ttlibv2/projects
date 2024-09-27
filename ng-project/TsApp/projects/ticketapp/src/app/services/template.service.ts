import { Injectable } from '@angular/core';
import { ModelApi } from "./model-api.service";
import { ResponseToModel } from "../models/common";
import { Observable, map } from 'rxjs';
import { Template } from '../models/template';
import { Page } from 'ts-ui/helper';

@Injectable({ providedIn: 'root' })
export class TemplateService extends ModelApi<Template> {

  override basePath(): string {
    return `/ts-api/template`;
  }

  override resToModel(): ResponseToModel<any> {
    return json => Template.fromAll(json);
  }


  getAllByUser(...threads: string[]): Observable<Page<Template>> {
    const url = this.callBasePath('/user/get-all');
    const models = threads.length == 0 ? {} : {threads: threads.join(',')};
    return this.getPage(url, models);
  }


  override create(data: any): Observable<Template> {
    const converterNew = this.resToModel();
    const url = this.callBasePath(`save`);
    return this.post(url, data).pipe(map(data => converterNew(data)));
  }


}
