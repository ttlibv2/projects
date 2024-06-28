import { BaseModel } from "./base-model";
import { JsonObject } from "./common";

export class GroupHelp extends BaseModel {
  id: number;
  code: string;
  title: string;
  value: string;
  
  static from(data: JsonObject): GroupHelp {
    return new GroupHelp().update(data);
  }

}
