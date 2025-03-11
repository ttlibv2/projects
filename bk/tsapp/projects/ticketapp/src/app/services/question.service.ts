import { Injectable } from '@angular/core';
import {ModelApi} from "./model-api.service";
import {ResponseToModel} from "../models/common";
import {Question} from "../models/question";
import { Observable } from 'rxjs';
import { Page, Pageable } from 'ts-ui/helper';

@Injectable({
  providedIn: 'root'
})
export class QuestionService extends ModelApi<Question> {

  override basePath(): string {
    return `/ts-api/question`;
  }

  override resToModel(): ResponseToModel<any> {
    return json => Question.from(json);
  }

  findByUserLogin(page?: Pageable): Observable<Page<Question>> {
    const url = this.callBasePath('get-by-user-login');
    const newPage = {size: 500, offset: 0, ...page}
    return this.getPage(url, {}, newPage);
  }


}
