import {BaseModel} from "./base-model";
import {AssignObject, JsonObject} from "./common";
import {AuthToken, RememberUser, User} from "./user";
import {Translation} from "./translation";

export class AppConfig extends BaseModel {
    currentLang?: string;
    baseUrl?: string;
    rememberUser?: RememberUser = new RememberUser();
    loginToken?: AuthToken;
    loginUser?: User;
    currentI18N?: Translation;
    currentTemplate?: Record<string, string>;

    static from(json: AssignObject<AppConfig>): AppConfig {
        return BaseModel.fromJson(AppConfig, json);
    }

}