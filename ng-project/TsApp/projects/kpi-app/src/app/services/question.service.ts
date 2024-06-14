import { Injectable } from '@angular/core';
import {ModelApi} from "./model-api.service";
import {ResponseToModel} from "../models/common";
import {Question} from "../models/question";

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


}
