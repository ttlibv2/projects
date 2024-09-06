import { AssignObject} from "./common";
import {Objects} from "ts-ui/helper";
import { Type } from "@angular/core";

export abstract class BaseModel {
  [field: string]: any;

  update(object: AssignObject<this>): this {
    if(Objects.notEmpty(object)) {
      Objects.assign(this, object);
    }

    return this;
  }

  delete(...fields: string[]): this {
    const self = this;
    fields.forEach(field => delete self[field]);
    return self;
  }

  protected static fromJson<E extends BaseModel>(modelType: Type<E>, data: AssignObject<E>): E {
    return data instanceof modelType ? data : new modelType().update(data);

  }

}