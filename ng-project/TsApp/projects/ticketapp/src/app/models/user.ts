import { UserApi } from "./api-info";
import {BaseModel} from "./base-model";
import {JsonObject} from "./common";
import {AssignObject, Objects} from 'ts-ui/helper';

export type ApiCode = 'od.ticket';

export interface AuthToken {
  access_token?: string;
  token_type?: string;
  user_id?: number;
}

export interface SignUpDto {
  email?: string;
  phone?: string;
  first_name?: string;
  last_name?: string;
  dob?: string;
  bio?: string;
  gender?: string;
  password?: string;
  re_password?: string;
  allow_term?: boolean;
}

export class User extends BaseModel {
  user_id?: number;
  full_name?: string;
  user_code?: string;
  room_code?: string;
  required_update?: boolean;
  config?: JsonObject;
  ts24id?: number;
  services: {[webName: string]: UserApi}



  set_config(config: any): this {
   this.config = config;
   return this;
 }


  static from(data: AssignObject<User>): User {
    return BaseModel.fromJson(User, data);
  }

}


export class RememberUser extends BaseModel {
  currentUser?: string;
  users: { [name: string]: ChkUser; };

  constructor() {
    super();
    this.users = {};
  }

  set(user: ChkUser): this {
    if (!user.remember) {
      delete this.currentUser;
      delete this.users[user.username];
      return this;
    } else {
      this.currentUser = user.username;
      this.users[user.username] = user;
      return this;
    }


  }

  static from(data: RememberUser | JsonObject): RememberUser {
    return Objects.assign(RememberUser, data);
  }
}


export interface ChkUser {
  username: string;
  password: string;
  url_dev?: string;
  remember: boolean;
}