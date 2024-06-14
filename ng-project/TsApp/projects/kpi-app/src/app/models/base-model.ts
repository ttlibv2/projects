import { JsonObject} from "./common";
import {Objects} from "../utils/objects";
import { Type } from "@angular/core";

export abstract class BaseModel {
  [field: string]: any;


  update(object: JsonObject, include404: boolean = true): this {
    if(Objects.notEmpty(object)) {
      Objects.assign(this, object);
    }

    return this;
  }

  protected static fromJson<E extends BaseModel>(modelType: Type<E>, data: E | JsonObject): E {
    return data instanceof modelType ? data : new modelType().update(data);

  }

}
