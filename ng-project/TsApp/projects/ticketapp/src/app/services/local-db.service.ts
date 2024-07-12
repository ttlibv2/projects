import {Injectable, Type} from '@angular/core';
import {Chanel} from "../models/chanel";
import {Dexie, IndexableType, Table, UpdateSpec} from "dexie";
import {AuthToken, User} from "../models/user";
import {Software} from '../models/software';
import {GroupHelp} from '../models/group-help';
import {Ticket} from '../models/ticket';
import {AgTable} from '../models/ag-table';
import {ApiInfo} from '../models/api-info';
import {Objects} from 'ts-helper';
import {Question} from '../models/question';
import {Template} from '../models/template';
import * as od from "../models/od-cls";
import {EMPTY, from, map, Observable, switchMap} from "rxjs";
import {JsonObject, ResponseToModel} from "../models/common";
import {AppConfig} from "../models/app-config";
import { kMaxLength } from 'buffer';

const  {isArray, isNull, isClass, valueToMap} = Objects;

Dexie.debug = true;


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
  //  agColumn: "agColumn",
    apiInfo: "apiInfo",
    user: "user",
    appConfig: "appConfig",
};

function observable<E>(promise: Promise<E>): Observable<E> {
    return from(promise);
}

interface TableName {
    tbName: string;
    keyOrIndex?: string;
    entityType: Type<any>;
    tableType?: () => Type<any>;
}

const tables: TableName[] = [
    {tbName: TB_NAMES.template, keyOrIndex: "id", entityType: Template, tableType: () => TemplateTable},
    {tbName: TB_NAMES.chanel, keyOrIndex: "id", entityType: Chanel, tableType: () => DbTable},
    {tbName: TB_NAMES.software, keyOrIndex: 'id', entityType: Software, tableType: () => DbTable},
    {tbName: TB_NAMES.groupHelp, keyOrIndex: 'id', entityType: GroupHelp, tableType: () => DbTable},
    {tbName: TB_NAMES.question, keyOrIndex: 'id', entityType: Question, tableType: () => DbTable},
    {tbName: TB_NAMES.ticket, keyOrIndex: 'id', entityType: Ticket, tableType: () => DbTable},
    {tbName: TB_NAMES.clsTeam, keyOrIndex: 'id', entityType: od.ClsTeam, tableType: () => DbTable},
    {tbName: TB_NAMES.clsAssign, keyOrIndex: 'id', entityType: od.ClsAssign, tableType: () => DbTable},
    {tbName: TB_NAMES.clsSubjectType, keyOrIndex: 'id', entityType: od.ClsSubjectType, tableType: () => DbTable},
    {tbName: TB_NAMES.clsReplied, keyOrIndex: 'id', entityType: od.ClsRepiled, tableType: () => DbTable},
    {tbName: TB_NAMES.clsCate, keyOrIndex: 'id', entityType: od.ClsCategory, tableType: () => DbTable},
    {tbName: TB_NAMES.clsCateSub, keyOrIndex: 'id', entityType: od.ClsCategorySub, tableType: () => DbTable},
    {tbName: TB_NAMES.clsTicketType, keyOrIndex: 'id', entityType: od.ClsTicketType, tableType: () => DbTable},
    {tbName: TB_NAMES.clsPriority, keyOrIndex: 'id', entityType: od.ClsPriority, tableType: () => DbTable},
    {tbName: TB_NAMES.clsProduct, keyOrIndex: 'id', entityType: od.ClsProduct, tableType: () => DbTable},
    {tbName: TB_NAMES.clsStage, keyOrIndex: 'id', entityType: od.ClsStage, tableType: () => DbTable},
    {tbName: TB_NAMES.clsTag, keyOrIndex: 'id', entityType: od.ClsTag, tableType: () => DbTable},
    {tbName: TB_NAMES.clsTopic, keyOrIndex: 'id', entityType: od.ClsTopic, tableType: () => DbTable},
    {tbName: TB_NAMES.agTable, keyOrIndex: 'code', entityType: AgTable, tableType: () => DbAgTable},
   // {tbName: TB_NAMES.agColumn, keyOrIndex: '[field_name+ag_code]', entityType: AgColumn, tableType: () => DbTable},
    {tbName: TB_NAMES.apiInfo, keyOrIndex: 'api_id', entityType: ApiInfo, tableType: () => DbTable},
    {tbName: TB_NAMES.user, keyOrIndex: 'user_id', entityType: User, tableType: () => DbUserTable},
    {tbName: TB_NAMES.appConfig, keyOrIndex: '', entityType: AppConfig, tableType: () => ConfigTable},
];

@Injectable({providedIn: 'root'})
export class LocalDbService extends Dexie {
    private lsTable: Map<string, any> = new Map();

    constructor() {
        super("ts_web", {autoOpen: true, allowEmptyDB: true});
        this.initializeTable();
        this.open()
            .then(s => console.log(`opening database success `, s._dbSchema))
            .catch(err => console.error(`opening database error -> `, err));
    }

    private initializeTable() {
        const schema = Object.fromEntries(tables.map(tb => [tb.tbName, tb.keyOrIndex]));
        this.version(100).stores(schema);
        this.lsTable = valueToMap(tables, tb => [tb.tbName, this.create_tb(tb)]);
    }

    private create_tb<T>(tb: TableName): any {
        const modelTable = isNull(tb.tableType) ? DbTable : tb.tableType();
        return new modelTable(this.table(tb.tbName), tb.entityType);
    }


    get_tb<E extends DbTable>(tbName: string): E {
        if(!this.lsTable.has(tbName)) throw new Error(`The table ${tbName} not exists`);
        else return this.lsTable.get(tbName);
    }

    get template(): TemplateTable {
        return this.get_tb(TB_NAMES.template);
    }

    get chanel(): DbTable<Chanel, number> {
        return this.get_tb(TB_NAMES.chanel);
    }

    get software(): DbTable<Software, number> {
        return this.get_tb(TB_NAMES.software);
    }

    get groupHelp(): DbTable<GroupHelp, number> {
        return this.get_tb(TB_NAMES.groupHelp);
    }

    get question(): DbTable<Question, number> {
        return this.get_tb(TB_NAMES.question);
    }

    get clsTeam(): DbTable<od.ClsTeam, number> {
        return this.get_tb(TB_NAMES.clsTeam);
    }

    get clsAssign(): DbTable<od.ClsAssign, number> {
        return this.get_tb(TB_NAMES.clsAssign);
    }

    get clsSubjectType(): DbTable<od.ClsSubjectType, number> {
        return this.get_tb(TB_NAMES.clsSubjectType);
    }

    get clsReplied(): DbTable<od.ClsRepiled, number> {
        return this.get_tb(TB_NAMES.clsReplied);
    }

    get clsCate(): DbTable<od.ClsCategory, number> {
        return this.get_tb(TB_NAMES.clsCate);
    }

    get clsCateSub(): DbTable<od.ClsCategorySub, number> {
        return this.get_tb(TB_NAMES.clsCateSub);
    }

    get clsTicketType(): DbTable<od.ClsTicketType, number> {
        return this.get_tb(TB_NAMES.clsTicketType);
    }

    get clsPriority(): DbTable<od.ClsPriority, number> {
        return this.get_tb(TB_NAMES.clsPriority);
    }

    get clsTag(): DbTable<od.ClsTag, number> {
        return this.get_tb(TB_NAMES.clsTag);
    }

    get clsTopic(): DbTable<od.ClsTopic, number> {
        return this.get_tb(TB_NAMES.clsTopic);
    }

    get clsProduct(): DbTable<od.ClsProduct, number> {
        return this.get_tb(TB_NAMES.clsProduct);
    }

    get clsStage(): DbTable<od.ClsStage, number> {
        return this.get_tb(TB_NAMES.clsStage);
    }

    get user(): DbUserTable {
        return this.get_tb(TB_NAMES.user);
    }

    get apiInfo(): DbTable<ApiInfo, string> {
        return this.get_tb(TB_NAMES.apiInfo);
    }

    get agTable(): DbAgTable {
        return this.get_tb(TB_NAMES.agTable);
    }

    get appCfg(): ConfigTable {
        return this.get_tb(TB_NAMES.appConfig);
    }

}


export class DbTable<T = any, Id extends IndexableType = any> {
    public readonly jsonToModel: ResponseToModel<T>;
    protected readonly table: Table<T, Id, any>;
    protected readonly tableName: string;
    protected readonly modelType: Type<T>;

    constructor(table: Table<T, Id, any>, type: Type<T>) {
        this.jsonToModel = (<any>type)['from'];
        this.table = table;
        this.modelType = type;
    }

    isTableEmpty(): Observable<boolean> {
        return observable(this.table.toArray().then(s => s.length == 0));
    }

    get_first(): Observable<T> {
        return observable(this.table.toCollection()
            .first(json => this.jsonToModel(json)));
    }

    getById(id: Id): Observable<T> {
        return observable(this.table.get(id));
    }

    getAll(): Observable<T[]> {
        return observable(this.table.toArray(this.listToModel));
    }

    delById(id: Id): Observable<any> {
        return observable(this.table.where(':id').equals(id).delete())
    }

    delAll(): Observable<any> {
        return observable(this.table.clear());
    }

    existsById(key: Id): Observable<boolean> {
        const keyName = this.table.schema.primKey.keyPath;
        return observable(this.table.where(keyName).equals(key).count(c => c > 0));
    }

    save(data: T | T[] | JsonObject | JsonObject[], key?: Id): Observable<any> {
        if (isNull(data)) return EMPTY;
        else if (!isArray(data)) {
            const dataSave = isClass(data) ? data : this.jsonToModel(data);
            return observable(this.table.put(dataSave, key).then(_ => dataSave));
        } //
        else {
            const dataSave = (<any[]>data).flatMap(item => isClass(item) ? item : this.jsonToModel(item));
            return observable(this.table.bulkPut(dataSave).then(_ => dataSave));
        }

    }

    deleteById(key: Id): Observable<void> {
        return observable(this.table.delete(key));
    }


    update(key: Id, changes: UpdateSpec<T>): Observable<number> {
        return observable(this.table.update(key, changes));
    }


    private listToModel(data: any[]): T[] {
        return data.flatMap(item => this.jsonToModel(item));
    }


}

export class DbAgTable extends DbTable<AgTable, string> {

}

export class ConfigTable extends DbTable<any, string> {
    

    set(code: string, value: any): Observable<any> {
        if (isNull(value)) return this.deleteById(code);
        else return observable(this.table.put(value, code).then(_ => value));
    }

    setAll(cfg: AppConfig):Observable<AppConfig> {
        return observable(this.table.clear().then(async () => {
            const keys = Object.keys(cfg);
            const data = keys.map(k => cfg[k]);
            this.table.bulkAdd(data, keys);
            return cfg;
        }));
    }

    read(): Observable<AppConfig> {
        const json: JsonObject = {};
        return observable(this.table.toCollection().each((item, cursor) => {
            let field: any = cursor.key;
            json[field] = item;//?.value;
        }).then(() => AppConfig.from(json)));
    }

    getSet(cfg: AppConfig): Observable<AppConfig> {
        return this.isTableEmpty()
            .pipe(switchMap(empty => empty ? this.setAll(cfg) : this.read()));
    }




}

export class TemplateTable extends DbTable<Template, number> {

    override save(data: Template | Template[] | JsonObject | JsonObject[] | Map<string, Template>, key?: number): Observable<any> {
        if (data instanceof Map) return super.save([...data.values()]);
        else return super.save(data);
    }

}

export class DbUserTable extends DbTable<User, number> {

    read(): Observable<User> {
        return observable(this.table.toCollection().first());
    }

    saveToken(user_id: number, token: AuthToken): Observable<any> {
        return this.existsById(user_id).pipe(switchMap(flag => {
            if (!flag) return this.save(new User().update({token, user_id}));
            else return this.update(user_id, {token});
        }));
    }

}