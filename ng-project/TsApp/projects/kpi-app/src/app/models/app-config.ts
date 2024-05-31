import {BaseModel} from "./base-model";

export class AppConfig {
  remember: RememberUser;

}

export class RememberUser extends BaseModel {
  currentUser: string;
  users: {
    [email: string]: {
      username: string;
      password: string;
      url_dev: string;
      remember: boolean;
    }
  }

  static from(object: any): RememberUser {
    return new RememberUser().update(object);
  }
}
