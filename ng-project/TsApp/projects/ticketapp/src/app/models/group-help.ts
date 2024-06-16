import { BaseModel } from "./base-model";
import { JsonObject } from "./common";

export class GroupHelp extends BaseModel{
  static fromList(value: any[] | GroupHelp[]): GroupHelp[] {
      throw new Error("Method not implemented.");
  }

  static from(data: JsonObject): GroupHelp {
    return new GroupHelp().update(data);
  }

}
