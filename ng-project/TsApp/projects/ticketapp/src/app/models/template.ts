import { BaseModel } from "./base-model";
import { AssignObject, JsonObject } from "./common";

export class Template extends BaseModel {
  template_id?: number;
  entity_code?: string;
  title?: string;
  icon?: string;
  clear?: boolean;
  summary?: string;
  bg_color?: string;
  text_color?: string;
  user_id?: number;
  data: JsonObject;

  static from(json: AssignObject<Template>): Template {
    return BaseModel.fromJson(Template, json);
  }

  static fromList(data: AssignObject<Template>[]): Template[] {
    return data.flatMap((item) => Template.from(item));
  }
}
