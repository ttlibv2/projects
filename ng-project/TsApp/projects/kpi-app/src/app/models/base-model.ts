import { JsonObject } from "./common";
import {Objects} from "../utils/objects";

export abstract class BaseModel {
  [field: string]: any;

  update(object: JsonObject): this {
    if(Objects.notEmpty(object)) {
      Object.assign(this, object);
    }
    return this;
  }




}
