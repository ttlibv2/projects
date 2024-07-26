import {Injectable, signal} from "@angular/core";
import {Observable, tap} from "rxjs";
import {Objects} from "ts-ui/helper";
import {LocalDbService, TB_NAMES} from "./local-db.service";
import {AuthToken, ChkUser, RememberUser, User} from "../models/user";
import {Chanel} from "../models/chanel";
import {Software} from "../models/software";
import {GroupHelp} from "../models/group-help";
import {Question} from "../models/question";
import * as cls from "../models/od-cls";
import {Translation} from "../models/translation";
import {AppConfig} from "../models/app-config";
import { Catalog } from "../models/catalog";
import { Template } from "../models/template";

const {isNull} = Objects;


export interface StorageData {
    lsChanel?: Chanel[];
    lsSoftware?: Software[];
    lsGroupHelp?: GroupHelp[];
    lsQuestion?: Question[];
    lsTeam?: cls.ClsTeam[];
    lsUser?: cls.ClsUser[];
    lsCategory?: cls.ClsCategory[];
    lsCategorySub?: cls.ClsCategorySub[];
    lsPriority?: cls.ClsPriority[];
    lsStage?: cls.ClsStage[];
    lsTag?: cls.ClsTag[];
    lsSubjectType?: cls.ClsSubjectType[];
    lsTicketType?: cls.ClsTicketType[];
    lsProduct?: cls.ClsProduct[];
    lsReplied?: cls.ClsRepiled[];
    allCatalog?: Catalog;
    agTicketTemplate?: Template;
}

const defaultConfig = AppConfig.from({
    baseUrl: 'http://localhost:8888',
    rememberUser: new RememberUser(),
    currentLang: 'vi',
    currentI18N: new Translation(),
})

@Injectable({providedIn: 'root'})
export class StorageService {
    config = signal<AppConfig>(defaultConfig);
    data = signal<StorageData>({});

    constructor(private db: LocalDbService) {
    }

    private updateConfig(field: string, data: any): Observable<any> {
        const local = () => this.config.update(cls => cls.update({[field]: data}));
        return this.db.appCfg.set(field, data).pipe(tap(_ => local()));
    }

    clearCache(): void {
        this.data.update(json => ({...json, 
            allCatalog: undefined
        }));
    }
    

    set_loginToken(token: AuthToken): Observable<AuthToken> {
        return this.updateConfig('loginToken', token);
    }

    set_baseUrl(baseUrl: string): Observable<string> {
        return this.updateConfig('baseUrl', baseUrl);
    }

    set_rememberUser(user: RememberUser | ChkUser): Observable<RememberUser> {
        const ur = user instanceof RememberUser ? user : this.rememberUser.set(user);
        return this.updateConfig('rememberUser', ur);
    }

    set_loginUser(user: User): Observable<User> {
        return this.updateConfig('loginUser', user);
    }

    set_i18n(lang: string, data: Translation): Observable<Translation> {
        return this.updateConfig(`i18n_${lang}`, data);
    }

    set_currentTemplate(entityCode: string, templateCode: string) {
        const map = this.currentTemplate;
        if(isNull(templateCode)) delete map[entityCode];
        else map[entityCode]= templateCode;
        return this.updateConfig(`currentTemplate`, map);
    }

    get asyncConfig(): Observable<AppConfig> {
        return this.db.appCfg.getSet(this.config()).pipe(tap(cls => this.config.set(cls)));
    }

    //@formatter:off
    set_lsChanel(data: Chanel[]): Observable<Chanel[]> { return this.db.get_tb(TB_NAMES.chanel).save(data); }
    set_lsSoftware(data: Software[]): Observable<Software[]> { return this.db.get_tb(TB_NAMES.software).save(data); }
	set_lsGroupHelp(data: GroupHelp[]): Observable<GroupHelp[]> { return this.db.get_tb(TB_NAMES.groupHelp).save(data); }
	set_lsQuestion(data: Question[]): Observable<Question[]> { return this.db.get_tb(TB_NAMES.question).save(data); }
	set_lsTeam(data: cls.ClsTeam[]): Observable<cls.ClsTeam[]> { return this.db.get_tb(TB_NAMES.clsTeam).save(data); }
	set_lsUser(data: cls.ClsUser[]): Observable<cls.ClsUser[]> { return this.db.get_tb(TB_NAMES.user).save(data); }
	set_lsCategory(data: cls.ClsCategory[]): Observable<cls.ClsCategory[]> { return this.db.get_tb(TB_NAMES.clsCate).save(data); }
	set_lsCategorySub(data: cls.ClsCategorySub[]): Observable<cls.ClsCategorySub[]> { return this.db.get_tb(TB_NAMES.clsCateSub).save(data); }
	set_lsPriority(data: cls.ClsPriority[]): Observable<cls.ClsPriority[]> { return this.db.get_tb(TB_NAMES.clsPriority).save(data); }
	set_lsStage(data: cls.ClsStage[]): Observable<cls.ClsStage[]> { return this.db.get_tb(TB_NAMES.clsStage).save(data); }
	set_lsTag(data: cls.ClsTag[]): Observable<cls.ClsTag[]> { return this.db.get_tb(TB_NAMES.clsTag).save(data); }
	set_lsSubjectType(data: cls.ClsSubjectType[]): Observable<cls.ClsSubjectType[]> { return this.db.get_tb(TB_NAMES.clsSubjectType).save(data); }
	set_lsTicketType(data: cls.ClsTicketType[]): Observable<cls.ClsTicketType[]> { return this.db.get_tb(TB_NAMES.clsTicketType).save(data); }
	set_lsProduct(data: cls.ClsProduct[]): Observable<cls.ClsProduct[]> { return this.db.get_tb(TB_NAMES.clsProduct).save(data); }
	set_lsReplied(data: cls.ClsRepiled[]): Observable<cls.ClsRepiled[]> { return this.db.get_tb(TB_NAMES.clsReplied).save(data); }


	get loginToken(): AuthToken {return this.config().loginToken;}
	get baseUrl(): string {return this.config().baseUrl;}
	get rememberUser(): RememberUser {
        return this.config().rememberUser ?? new RememberUser();
    }
	get loginUser(): User {return this.config().loginUser;}
    get i18n(): Translation { return this.config().currentI18N; }
    get currentTemplate() { return this.config().currentTemplate ?? {}; }



	get lsChanel(): Chanel[] {return this.data().lsChanel;}
	get lsSoftware(): Software[] { return this.data().lsSoftware;}
	get lsGroupHelp(): GroupHelp[] { return this.data().lsGroupHelp;}
	get lsQuestion(): Question[] { return this.data().lsQuestion;}
	get lsTeam(): cls.ClsTeam[] { return this.data().lsTeam;}
	get lsUser(): cls.ClsUser[] { return this.data().lsUser;}
	get lsCategory(): cls.ClsCategory[] { return this.data().lsCategory;}
	get lsCategorySub(): cls.ClsCategorySub[] { return this.data().lsCategorySub;}
	get lsPriority(): cls.ClsPriority[] { return this.data().lsPriority;}
	get lsStage(): cls.ClsStage[] { return this.data().lsStage;}
	get lsTag(): cls.ClsTag[] { return this.data().lsTag;}
	get lsSubjectType(): cls.ClsSubjectType[] { return this.data().lsSubjectType;}
	get lsTicketType(): cls.ClsTicketType[] { return this.data().lsTicketType;}
	get lsProduct(): cls.ClsProduct[] { return this.data().lsProduct;}
	get lsReplied(): cls.ClsRepiled[] { return this.data().lsReplied;}
    get allCatalog(): Catalog { return this.data().allCatalog;}
    get isLogin(): boolean { return Objects.notNull(this.loginToken);}
    



    //@formatter:on


}