import {BaseModel} from "./base-model";
import {JsonObject} from "./common";

export class UserCfg extends BaseModel{

  static from(json: JsonObject):UserCfg {
    return new UserCfg().update(json);
  }
}
