import { BaseModel } from "./base-model";
import { JsonObject } from "./common";

export class Question extends BaseModel {
    question_id?: number;
    reply?: string;
    title?: string;
    soft_type?: string;
    shared?: boolean;
    user_id?: number;

  static from(data: JsonObject): Question {
    return new Question().update(data);
  }
}
