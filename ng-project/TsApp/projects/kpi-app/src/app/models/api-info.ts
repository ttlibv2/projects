import {BaseModel} from "./base-model";
import {JsonObject} from "./common";

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
  user_api?: {
    csrf_token?: string;
    headers?: JsonObject;
    queries?: JsonObject;
    user_name?: string;
    secret?: string;
    user_info?: JsonObject;
    auto_login?: boolean;
  }


}
