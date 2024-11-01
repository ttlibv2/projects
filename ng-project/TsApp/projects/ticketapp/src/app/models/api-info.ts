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
  app_name?: string;
  app_uid?: string;
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
    return `[${this.app_name}] ${this.title}`;
  }

  protected override get modelType(): Type<ApiInfo> {
    return ApiInfo;
  }

  static from(json: AssignObject<ApiInfo>): ApiInfo {
    return BaseModel.fromJson(ApiInfo, json);
  }

}


export class UserApi extends BaseModel {
  app_name?: string ;
  user_name?: string;
  password?: string;
  ts_name?: string;
  ts_email?: string;
  ua_id?: number ;
  user_id?: number ;
  csrf_token?: string ;
  cookie_value?: string ;
  auto_login?: boolean;
  allow_edit?: boolean;
  save_log?: boolean;
  user_info?: ClsUser;
  menu_links?: MenuLink;
  api_item?: ApiInfo ;

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

