import {Objects, AssignObject} from "ts-ui/helper";
import { Type } from "@angular/core";
const {isNull, isEmpty} = Objects;

export abstract class BaseModel<E extends BaseModel = any> {
  [field: string]: any;

  protected get modelType(): Type<E> {
    return null;
  }

  update(object: AssignObject<E>): this {
    if(Objects.notEmpty(object)) {
      Objects.assign(this, object);
    }

    return this;
  }

  delete(...fields: string[]): this {
    const self: any = this;
    fields.forEach(field => delete self[field]);
    return self;
  }

   clone(): E {
    if(Objects.isNull(this.modelType)) throw new Error(`method clone not support`);
    else return new this.modelType().update(this);
  }

  protected static fromJson<E extends BaseModel>(modelType: Type<E>, data: AssignObject<E>): E {
    return isNull(data) ? undefined : data instanceof modelType ? data : new modelType().update(data);

  }

}