import { AssignObject } from "ts-ui/helper";
import { BaseModel } from "./base-model";

export class Chanel extends BaseModel {
  id?: number;
  code?: string;
  extend_code?: string;
  has_image?: boolean;
  support?: string;
  value?: string;

  get display_name(): string {
    return this.value;
  }

  static from(data: AssignObject<Chanel>): Chanel {
    return BaseModel.fromJson(Chanel, data);
  }

  static fromList(data: AssignObject<Chanel>[]): Chanel[] {
    return data.flatMap(item => Chanel.from(item));
  }

}

