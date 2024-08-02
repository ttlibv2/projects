import {BaseModel} from "./base-model";
import {AssignObject, JsonObject} from "./common";
import {AuthToken, RememberUser, User} from "./user";
import {Translation} from "./translation";
import {Objects, TsMap} from "ts-ui/helper";

export class AppConfig extends BaseModel {
    currentLang: string;
    baseUrl: string;
    rememberUser: RememberUser;
    loginToken: AuthToken;
    loginUser: User;
    currentI18N: Translation;
    currentTemplate: TsMap<string, string>;

    setNull(field: string): this {
        delete this[field];
        return this;
    }

    set_rememberUser(user: AssignObject<RememberUser>): this {
        this.rememberUser = RememberUser.from(user);
        return this;
    }

    set_loginToken(token: AuthToken) {
        this.loginToken = token;
    }

    static from(json: AssignObject<AppConfig>): AppConfig {
        return AppConfig.getDefault().update(json);
    }

    static getDefault(): AppConfig {
        return Objects.assign(AppConfig, DEFAULT_APP_CFG);
    }

}

export const DEFAULT_APP_CFG: Partial<AppConfig> = {
    baseUrl: 'http://localhost:8888',
    currentLang: 'vi',
    rememberUser: new RememberUser(),
    currentI18N: new Translation(),
    currentTemplate: new TsMap()
}