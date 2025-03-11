import { AssignObject } from "ts-ui/helper";
import { BaseModel } from "./base-model";

export class Question extends BaseModel {

  question_id?: number;
  reply?: string;
  title?: string;
  soft_type?: string;
  shared?: boolean;
  user_id?: number;

  static from(data: AssignObject<Question>): Question {
    return BaseModel.fromJson(Question, data);
  }

  static fromList(data: AssignObject<Question>[]): Question[] {
    return data.flatMap(v => Question.from(v));
  }
}
