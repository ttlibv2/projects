import {BaseModel} from "./base-model";
import {AuthToken, RememberUser, User} from "./user";
import {Translation} from "./translation";
import {Objects, TsMap, AssignObject} from "ts-ui/helper";
import { Catalog } from "./catalog";
const {isNull} = Objects;

export class AppConfig extends BaseModel {
    currentLang: string;
    baseUrl: string;
    rememberUser: RememberUser;
    loginToken: AuthToken;
    loginUser: User;
    currentI18N: Translation;
    currentTemplate: TsMap<string, string>;
    tsAppUID: string;
    catalog:Catalog;

    setNull(field: string): this {
        this.delete(field);
        return this;
    }

    set_rememberUser(user: AssignObject<RememberUser>): this {
        this.rememberUser = RememberUser.from(user);
        return this;
    }

    set_loginToken(token: AuthToken) {
        this.loginToken = token;
    }

    set_catalog(catalog:AssignObject<Catalog>): void  {
        if(isNull(this.catalog))this.catalog = Catalog.from(catalog);
        else this.catalog.update(catalog);
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
    currentTemplate: new TsMap(),
    tsAppUID: 'TSApp'
}