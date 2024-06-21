import { AssignObject} from "./common";
import {Objects} from "ts-helper";
import { Type } from "@angular/core";



export abstract class BaseModel {
  [field: string]: any;


  update(object: AssignObject<this>): this {
    if(Objects.notEmpty(object)) {
      Objects.assign(this, object);
    }

    return this;
  }

  protected static fromJson<E extends BaseModel>(modelType: Type<E>, data: AssignObject<E>): E {
    return data instanceof modelType ? data : new modelType().update(data);

  }

}