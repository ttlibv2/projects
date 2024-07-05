import { BaseModel } from "./base-model";
import { JsonObject } from "./common";

export class Chanel  extends BaseModel{
    id?: number;
    code?: string;
    extend_code?: string;
    has_image?: boolean;
    support?: string;
    value?: string;

    get display_name(): string {
      return this.value;
    }

  static from(data:  Chanel | JsonObject): Chanel {
    return data instanceof Chanel ? data : new Chanel().update(data);
  }

  static fromList(data: JsonObject[]): Chanel[] {
    return data.flatMap(item => Chanel.from(item));
  }

}

