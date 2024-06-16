import {BaseModel} from "./base-model";
import {JsonObject} from "./common";

export class UserApiInfo  extends BaseModel {

  static from(info: JsonObject): any {
      return new UserApiInfo().update(info);
  }

  csrf_token?: string;
  headers?: JsonObject;
  queries?: JsonObject;
  user_name?: string;
  password?: string;
  user_info?: JsonObject;
  auto_login?: boolean;
  allow_edit?: boolean;
}

export class ApiInfo extends BaseModel {
  api_id?: number;
  code?: string;
  title?: string;
  base_url?: string;
  login_path?: string;
  default_user?: string;
  default_secret?: string;
  default_queries?: JsonObject;
  default_headers?: JsonObject;
  login_info?: JsonObject;
  user_api?: UserApiInfo;

  static from(json: JsonObject): ApiInfo {
    return new ApiInfo().update(json);
  }

  get displayName(): string {
    return `[${this.code}] ${this.title}`;
  }




}
