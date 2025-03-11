import { AssignObject } from "ts-ui/helper";
import { BaseModel } from "./base-model";

export class GroupHelp extends BaseModel {
  id: number;
  code: string;
  title: string;
  value: string;
  
  static from(data: AssignObject<GroupHelp>): GroupHelp {
    return BaseModel.fromJson(GroupHelp, data);
  }

}
