import { AssignObject, JsonAny, Objects } from "ts-ui/helper";
import { BaseModel } from "./base-model";
import { ClsUser } from "./od-cls";
import { Type } from "@angular/core";

export const EMPTY_PWD: string = "*".repeat(8);
const {isNull, isEmpty} = Objects;


export class ApiInfo extends BaseModel<ApiInfo> {
  api_id?: number;
  title?: string;
  summary?: string;
  base_url?: string;
  login_path?: string;
  service_name?: string;
  service_uid?: string;
  headers?: JsonAny;
  queries?: JsonAny;
  links?: JsonAny;
  user_api?: UserApi;
  is_system?: boolean;
  allow_copy?: boolean;

  set_user_api(info: AssignObject<UserApi>) {
    this.user_api = UserApi.from(info);
  }
  
  get displayName(): string {
    return `[${this.service_name}] ${this.title}`;
  }

  protected override get modelType(): Type<ApiInfo> {
    return ApiInfo;
  }

  static from(json: AssignObject<ApiInfo>): ApiInfo {
    return BaseModel.fromJson(ApiInfo, json);
  }

}


export class UserApi extends BaseModel {
  service_name?: string = null;
  user_name?: string = null;
  password?: string = null;
  ua_id?: number = null;
  user_id?: number = null;
  csrf_token?: string = null;
  cookie_value?: string = null;
  auto_login?: boolean = null;
  allow_edit?: boolean = null;
  user_info?: ClsUser = null;
  menu_links?: MenuLink = null;
  api_item?: ApiInfo = null;

  set_user_info(info: AssignObject<ClsUser>) {
    this.user_info = ClsUser.from(info);
  }

  set_api_item(info: AssignObject<ApiInfo>) {
    this.api_item = ApiInfo.from(info);
  }

  static from(object: AssignObject<UserApi>): any {
    if(object instanceof UserApi) return object;
    else if(isEmpty(object)) return new UserApi();
    else {
      object.password = EMPTY_PWD;
      return BaseModel.fromJson(UserApi, object);
    }
  }

}

export interface MenuLink {
  od_partner?: string;
  od_ticket?: string;
}

