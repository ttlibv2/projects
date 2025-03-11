import { AssignObject } from "ts-ui/helper";
import { BaseModel } from "./base-model";

export class Software extends BaseModel {
  id?: number;
  code?: string;
  soft_names?: string[];
  value?: string;

  static from(data: AssignObject<Software>): Software {
    return new Software().update(data);
  }

  static fromList(data: AssignObject<Software>[]): Software[] {
    return data.flatMap(item => Software.from(item));
  }
  
}
