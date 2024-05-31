import { BaseModel } from "./base-model";
import { JsonObject } from "./common";

export class GroupHelp extends BaseModel{

  static from(data: JsonObject): GroupHelp {
    return new GroupHelp().update(data);
  }

}
