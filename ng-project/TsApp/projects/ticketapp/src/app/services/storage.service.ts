import {Injectable, signal} from "@angular/core";
import {EMPTY, forkJoin, from, map, Observable, of, switchMap, tap} from "rxjs";
import {AuthToken, ChkUser, RememberUser, User} from "../models/user";
import {DBConfig, DBService, LocalTable} from "ts-ui/local-db";
import {AssignObject, Callback, JsonAny, Objects} from "ts-ui/helper";
import {Chanel} from "../models/chanel";
import {Software} from "../models/software";
import {GroupHelp} from "../models/group-help";
import {Question} from "../models/question";
import {Translation} from "../models/translation";
import {AppConfig, DEFAULT_APP_CFG} from "../models/app-config";
import { Template } from "../models/template";
import {Ticket} from "../models/ticket";
import {AgTable} from "../models/ag-table";
import {ApiInfo} from "../models/api-info";
import * as cls from "../models/od-cls";
import {Catalog} from "../models/catalog";

const {isNull, arrayToJson} = Objects;




export class StorageData {
    config: AppConfig;
    lsChanel: Chanel[];
    lsSoftware: Software[];
    lsGroupHelp: GroupHelp[];
    lsQuestion: Question[];
    lsTeam: cls.ClsTeam[];
    lsUser: cls.ClsUser[];
    lsCategory: cls.ClsCategory[];
    lsCategorySub: cls.ClsCategorySub[];
    lsPriority: cls.ClsPriority[];
    lsStage: cls.ClsStage[];
    lsTag: cls.ClsTag[];
    lsSubjectType: cls.ClsSubjectType[];
    lsTicketType: cls.ClsTicketType[];
    lsProduct: cls.ClsProduct[];
    lsReplied: cls.ClsReplied[];
    agTemplate: Template;
    catalog:Catalog;

    set_config(data:AssignObject<AppConfig>): this{
        //console.log(`set_config`, data);
        this.config = AppConfig.from(data);
        return this;
    }

    up_config(field: any, data: any): this {
       // console.log(`set_${field}`, data);
        this.config.update({[field]: data});
        return this;
    }


    clear() {
    }

    static from(data: AssignObject<StorageData>):StorageData {
        if(data instanceof StorageData) return data;
        else return Objects.assign(new StorageData(), data);
    }

    static getDefault(): StorageData {
        return StorageData.from({
            config: DEFAULT_APP_CFG
        });
    }

}

@Injectable({providedIn: 'root'})
export class StorageService {
    private _cache = StorageData.getDefault();


    constructor(private db: DBService) {}

    private up_config<KEY extends keyof AppConfig, VAL extends AppConfig[KEY]>(field: KEY, data: VAL): Observable<any> {
        const local = () => this._cache.up_config(field, data);
        return this.configTb.set(<string>field, data).pipe(tap(_ => local()));
    }

    set_loginToken(token: AuthToken): Observable<AuthToken> {
        return this.up_config('loginToken', token);
    }

    set_baseUrl(baseUrl: string): Observable<string> {
        return this.up_config('baseUrl', baseUrl);
    }

    set_rememberUser(user: ChkUser): Observable<RememberUser> {
        const config = this.config;
        const ur = config.rememberUser.set(user);
        return this.up_config('rememberUser', ur);
    }

    set_loginUser(user: User): Observable<User> {
        return this.up_config('loginUser', user);
    }

    set_i18n(lang: string, data: Translation): Observable<Translation> {
        return this.up_config(`i18n_${lang}`, data);
    }

    set_currentTemplate(entityCode: string, templateCode: string) {
        const map = this.config.currentTemplate.set(entityCode, templateCode);
        return this.up_config(`currentTemplate`, map);
    }

    get asyncConfig(): Observable<AppConfig> {
       // console.log(`asyncConfig`, this.config);
        return this.configTb.getSet(this.config).pipe(map(cfg => {
          //  console.log('after-cfg', cfg);
            this.cache.set_config(cfg);
            return cfg;
        }))
    }

    get isLogin(): boolean {
        //console.log('loginToken: ', this.loginToken);
        return Objects.notEmpty(this.loginToken);
    }


    clearCache() {
        this.set_loginUser(undefined);
        this.set_loginToken(undefined);
        this._cache.clear();
    }

    //@formatter:off

    set_lsChanel(data: Chanel[]): Observable<Chanel[]> { return this.chanelTb.setAll(data); }
    set_lsSoftware(data: Software[]): Observable<Software[]> { return this.softwareTb.setAll(data); }
    set_lsGroupHelp(data: GroupHelp[]): Observable<GroupHelp[]> { return this.groupHelpTb.setAll(data); }
    set_lsQuestion(data: Question[]): Observable<Question[]> { return this.questionTb.setAll(data); }
    set_lsTeam(data: cls.ClsTeam[]): Observable<cls.ClsTeam[]> { return this.clsTeamTb.setAll(data); }
    set_lsUser(data: cls.ClsUser[]): Observable<cls.ClsUser[]> { return this.userTb.setAll(data); }
    set_lsCategory(data: cls.ClsCategory[]): Observable<cls.ClsCategory[]> { return this.clsCateTb.setAll(data); }
    set_lsCategorySub(data: cls.ClsCategorySub[]): Observable<cls.ClsCategorySub[]> { return this.clsCateSubTb.setAll(data); }
    set_lsPriority(data: cls.ClsPriority[]): Observable<cls.ClsPriority[]> { return this.clsPriorityTb.setAll(data); }
    set_lsStage(data: cls.ClsStage[]): Observable<cls.ClsStage[]> { return this.clsStageTb.setAll(data); }
    set_lsTag(data: cls.ClsTag[]): Observable<cls.ClsTag[]> { return this.clsTagTb.setAll(data); }
    set_lsSubjectType(data: cls.ClsSubjectType[]): Observable<cls.ClsSubjectType[]> { return this.clsSubjectTypeTb.setAll(data); }
    set_lsTicketType(data: cls.ClsTicketType[]): Observable<cls.ClsTicketType[]> { return this.clsTicketTypeTb.setAll(data); }
    set_lsProduct(data: cls.ClsProduct[]): Observable<cls.ClsProduct[]> { return this.clsProductTb.setAll(data); }
    set_lsReplied(data: cls.ClsReplied[]): Observable<cls.ClsReplied[]> { return this.clsRepliedTb.setAll(data); }
    set_config(data:AppConfig): Observable<AppConfig> { return this.configTb.setData(data); }

    get cache(): StorageData { return this._cache; }
    get loginToken(): AuthToken {return this.config.loginToken;}
    get baseUrl(): string {return this.config.baseUrl;}
    get rememberUser(): RememberUser {return this.config.rememberUser;}
    get loginUser(): User {return this.config.loginUser;}
    get i18n(): Translation { return this.config.currentI18N; }
    get currentTemplate() { return this.config.currentTemplate; }
    get lsChanel(): Chanel[] {return this.cache.lsChanel;}
    get lsSoftware(): Software[] { return this.cache.lsSoftware;}
    get lsGroupHelp(): GroupHelp[] { return this.cache.lsGroupHelp;}
    get lsQuestion(): Question[] { return this.cache.lsQuestion;}
    get lsTeam(): cls.ClsTeam[] { return this.cache.lsTeam;}
    get lsUser(): cls.ClsUser[] { return this.cache.lsUser;}
    get lsCategory(): cls.ClsCategory[] { return this.cache.lsCategory;}
    get lsCategorySub(): cls.ClsCategorySub[] { return this.cache.lsCategorySub;}
    get lsPriority(): cls.ClsPriority[] { return this.cache.lsPriority;}
    get lsStage(): cls.ClsStage[] { return this.cache.lsStage;}
    get lsTag(): cls.ClsTag[] { return this.cache.lsTag;}
    get lsSubjectType(): cls.ClsSubjectType[] { return this.cache.lsSubjectType;}
    get lsTicketType(): cls.ClsTicketType[] { return this.cache.lsTicketType;}
    get lsProduct(): cls.ClsProduct[] { return this.cache.lsProduct;}
    get lsReplied(): cls.ClsReplied[] { return this.cache.lsReplied;}
    get catalog(): Catalog { return this.cache.catalog;}
    get config(): AppConfig { return this.cache.config;}

    //-------------table_list
    get chanelTb(): LocalTable<Chanel> {return this.db.getTable(TB_NAMES.chanel);}
    get softwareTb(): LocalTable<Software> {return this.db.getTable(TB_NAMES.software);}
    get templateTb(): LocalTable<Template> { return this.db.getTable(TB_NAMES.template);}
    get groupHelpTb(): LocalTable<GroupHelp> { return this.db.getTable(TB_NAMES.groupHelp);}
    get questionTb(): LocalTable<Question> { return this.db.getTable(TB_NAMES.question);}
    get ticketTb(): LocalTable<Ticket> { return this.db.getTable(TB_NAMES.ticket);}
    get clsTeamTb(): LocalTable<cls.ClsTeam> { return this.db.getTable(TB_NAMES.clsTeam);}
    get clsAssignTb(): LocalTable<cls.ClsAssign> { return this.db.getTable(TB_NAMES.clsAssign);}
    get clsSubjectTypeTb(): LocalTable<cls.ClsSubjectType> { return this.db.getTable(TB_NAMES.clsSubjectType);}
    get clsRepliedTb(): LocalTable<cls.ClsReplied> { return this.db.getTable(TB_NAMES.clsReplied);}
    get clsCateTb(): LocalTable<cls.ClsCategory> { return this.db.getTable(TB_NAMES.clsCate);}
    get clsCateSubTb(): LocalTable<cls.ClsCategorySub> { return this.db.getTable(TB_NAMES.clsCateSub);}
    get clsTicketTypeTb(): LocalTable<cls.ClsTicketType> { return this.db.getTable(TB_NAMES.clsTicketType);}
    get clsPriorityTb(): LocalTable<cls.ClsPriority> { return this.db.getTable(TB_NAMES.clsPriority);}
    get clsProductTb(): LocalTable<cls.ClsProduct> { return this.db.getTable(TB_NAMES.clsProduct);}
    get clsStageTb(): LocalTable<cls.ClsStage> { return this.db.getTable(TB_NAMES.clsStage);}
    get clsTagTb(): LocalTable<cls.ClsTag> { return this.db.getTable(TB_NAMES.clsTag);}
    get clsTopicTb(): LocalTable<cls.ClsTopic> { return this.db.getTable(TB_NAMES.clsTopic);}
    get agTableTb(): LocalTable<AgTable> { return this.db.getTable(TB_NAMES.agTable);}
    get apiInfoTb(): LocalTable<ApiInfo> { return this.db.getTable(TB_NAMES.apiInfo);}
    get userTb(): LocalTable<cls.ClsUser> { return this.db.getTable(TB_NAMES.user);}
    get configTb(): ConfigTable { return this.db.getTable(TB_NAMES.appConfig);}
    //@formatter:on

}

export class ConfigTable extends LocalTable<any, string> {

    protected override get libToModel(): Callback<any, any> {
        return object => object;
    }

    load(): Observable<AppConfig> {
       return this.getAllToJson().pipe(map(json => AppConfig.from(json)));
    }

    set(code: string, value: any): Observable<any> {
        if (isNull(value)) return this.delete(code);
        else return this.put(value, code);
    }

    setData(cfg: AppConfig):Observable<AppConfig> {
        return this.deleteAll().pipe(switchMap(_ => {
            const keys = Object.keys(cfg);
            const data = keys.map(k => cfg[k]);
            return this.bulkPut(data, keys).pipe(map(_ => cfg));
        }));
    }

    getSet(cfg: AppConfig): Observable<AppConfig> {
        return this.count().pipe(switchMap(num => num > 0 ? this.load() : this.setData(cfg)));
    }

    saveDefault(): Observable<AppConfig> {
        return this.getSet(AppConfig.getDefault());
    }

}

export class TemplateTable extends LocalTable<Template, number> {
}

export class DbUserTable extends LocalTable<User, number> {
}

export class DbAgTable extends LocalTable<AgTable, string> {

}


export const TB_NAMES = {
    template: "template",
    chanel: "chanel",
    software: "software",
    groupHelp: "groupHelp",
    question: "question",
    ticket: "ticket",
    clsTeam: "clsTeam",
    clsAssign: "clsAssign",
    clsSubjectType: "clsSubjectType",
    clsReplied: "clsReplied",
    clsCate: "clsCate",
    clsCateSub: "clsCateSub",
    clsTicketType: "clsTicketType",
    clsPriority: "clsPriority",
    clsProduct: "clsProduct",
    clsStage: "clsStage",
    clsTag: "clsTag",
    clsTopic: "clsTopic",
    agTable: "agTable",
    apiInfo: "apiInfo",
    user: "user",
    appConfig: "appConfig",
};

export const databaseConfig: Partial<DBConfig> = {
    dbName: 'ts_web',
    dbVersion: 2.0,
    schema: [
        {name: TB_NAMES.chanel, keyPath: "id", entityClass: Chanel},
        {name: TB_NAMES.software, keyPath: 'id', entityClass: Software},
        {name: TB_NAMES.groupHelp, keyPath: 'id', entityClass: GroupHelp},
        {name: TB_NAMES.question, keyPath: 'id', entityClass: Question},
        {name: TB_NAMES.ticket, keyPath: 'id', entityClass: Ticket},
        {name: TB_NAMES.clsTeam, keyPath: 'id', entityClass: cls.ClsTeam},
        {name: TB_NAMES.clsAssign, keyPath: 'id', entityClass: cls.ClsAssign},
        {name: TB_NAMES.clsSubjectType, keyPath: 'id', entityClass: cls.ClsSubjectType},
        {name: TB_NAMES.clsReplied, keyPath: 'id', entityClass: cls.ClsReplied},
        {name: TB_NAMES.clsCate, keyPath: 'id', entityClass: cls.ClsCategory},
        {name: TB_NAMES.clsCateSub, keyPath: 'id', entityClass: cls.ClsCategorySub},
        {name: TB_NAMES.clsTicketType, keyPath: 'id', entityClass: cls.ClsTicketType},
        {name: TB_NAMES.clsPriority, keyPath: 'id', entityClass: cls.ClsPriority},
        {name: TB_NAMES.clsProduct, keyPath: 'id', entityClass: cls.ClsProduct},
        {name: TB_NAMES.clsStage, keyPath: 'id', entityClass: cls.ClsStage},
        {name: TB_NAMES.clsTag, keyPath: 'id', entityClass: cls.ClsTag},
        {name: TB_NAMES.clsTopic, keyPath: 'id', entityClass: cls.ClsTopic},
        {name: TB_NAMES.apiInfo, keyPath: 'api_id', entityClass: ApiInfo},
        {name: TB_NAMES.agTable, keyPath: 'code', entityClass: AgTable, tableClass: DbAgTable},
        {name: TB_NAMES.template, keyPath: "id", entityClass: Template, tableClass: TemplateTable},
        {name: TB_NAMES.user, keyPath: 'user_id', entityClass: User, tableClass: DbUserTable},
        {name: TB_NAMES.appConfig, keyPath: '', entityClass: AppConfig, tableClass: ConfigTable},
    ]
};