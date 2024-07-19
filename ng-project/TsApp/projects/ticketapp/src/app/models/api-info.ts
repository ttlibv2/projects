import {BaseModel} from "./base-model";
import {AssignObject, JsonObject} from "./common";

export const EMPTY_PWD: string = "*".repeat(8);

export class UserApiInfo  extends BaseModel {
  csrf_token?: string;
  headers?: JsonObject;
  queries?: JsonObject;
  username?: string;
  password?: string;
  user_info?: JsonObject;
  auto_login?: boolean;
  allow_edit?: boolean;


  static from(data: AssignObject<UserApiInfo>): any {
    data.password = EMPTY_PWD;
    return BaseModel.fromJson(UserApiInfo, data);
  }



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

  set_user_api(data: AssignObject<UserApiInfo>) {
    this.user_api = UserApiInfo.from(data);
  }

  static from(json: AssignObject<ApiInfo>): ApiInfo {
    return BaseModel.fromJson(ApiInfo, json);
  }

  get displayName(): string {
    return `[${this.code}] ${this.title}`;
  }




}
