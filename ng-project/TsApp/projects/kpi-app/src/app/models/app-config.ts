import { BaseModel } from "./base-model";
import { JsonObject } from "./common";
import { AuthToken, RememberUser } from "./user";

export interface IAppConfig {
    baseUrl?: string;
    rememberUser?: RememberUser;
    authToken?: AuthToken;
}

export class AppConfig extends BaseModel implements IAppConfig {
    baseUrl?: string;
    rememberUser?: RememberUser;
    authToken?: AuthToken;

    static from(json: AppConfig | JsonObject): AppConfig {
        return this.fromJson(AppConfig, json);
    }

    updateDefault(object: JsonObject): this {
        for (const key of Object.keys(object)) {
            if (!(key in this)) this[key] = object[key];
        }
        return this;
    }

    set_rememberUser(info: any): this {
        this.rememberUser = RememberUser.from(info);
        return this;
    }
}