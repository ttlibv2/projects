import { AssignObject } from "ts-ui/helper";
import {BaseModel} from "./base-model";
import {JsonObject} from "./common";

export class UserCfg extends BaseModel{

  static from(json: AssignObject<UserCfg>):UserCfg {
    return BaseModel.fromJson(UserCfg, json);
  }
}
