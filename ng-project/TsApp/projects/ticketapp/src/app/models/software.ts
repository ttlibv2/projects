import { BaseModel } from "./base-model";
import { JsonObject } from "./common";

export class Software extends BaseModel {
    static fromList(value: any[] | Software[]): Software[] {
        throw new Error("Method not implemented.");
    }
    software_id?: number;
    code?: string;
    soft_names?: string[];
    value?: string;

  static from(data: JsonObject): Software {
    return new Software().update(data);
  }
}
