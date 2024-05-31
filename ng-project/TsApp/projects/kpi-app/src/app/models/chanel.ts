import { BaseModel } from "./base-model";
import { JsonObject } from "./common";

export class Chanel  extends BaseModel{
    chanel_id?: number;
    code?: string;
    extend_code?: string;
    has_image?: boolean;
    support?: string;
    value?: string;

  static from(data: JsonObject): Chanel {
    return new Chanel().update(data);
  }

  static fromList(data: JsonObject[]): Chanel[] {
    return data.flatMap(item => Chanel.from(item));
  }

}

