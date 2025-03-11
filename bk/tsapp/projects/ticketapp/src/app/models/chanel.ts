import { AssignObject } from "ts-ui/helper";
import { BaseModel } from "./base-model";

export class Chanel extends BaseModel {
  id?: number;
  code?: string;
  extend_code?: string;
  has_image?: boolean;
  support?: string;
  value2?: string;

  override update(object: any): this {
    if('value' in object) {
      object['value2'] = object['value'];
      delete object['value'];
    }
    super.update(object);
    return this;
  }
  

  get display_name(): string {
    return this.value2;
  }

  static from(data: AssignObject<Chanel>): Chanel {
    return BaseModel.fromJson(Chanel, data);
  }

  static fromList(data: AssignObject<Chanel>[]): Chanel[] {
    return data.flatMap(item => Chanel.from(item));
  }

}

