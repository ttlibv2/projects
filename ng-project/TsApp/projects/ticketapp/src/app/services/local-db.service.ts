import { Injectable, Type } from '@angular/core';
import { Chanel } from "../models/chanel";
import { catchError, EMPTY, forkJoin, from, map, Observable, Observer, of, switchMap, throwError } from "rxjs";
import { Dexie, IndexableType, Table, UpdateSpec } from "dexie";
import { AuthToken, RememberUser, User } from "../models/user";
import { Software } from '../models/software';
import { GroupHelp } from '../models/group-help';
import { Ticket } from '../models/ticket';
import { AgTable } from '../models/ag-table';
import { ApiInfo } from '../models/api-info';
import { Objects } from '../utils/objects';
import {
  ClsAssign,
  ClsCategory,
  ClsCategorySub, ClsPriority,
  ClsProduct,
  ClsRepiled,
  ClsStage,
  ClsSubjectType, ClsTag,
  ClsTeam,
  ClsTicketType,
  ClsTopic
} from "../models/od-cls";
import { JsonObject, ResponseToModel } from '../models/common';
import { Question } from '../models/question';
import { AppConfig } from '../models/app-config';
import { BaseModel } from '../models/base-model';

const { isNull, isArray, isClass } = Objects;

Dexie.debug = true;

function observable<E>(promise: Promise<E>, logging: boolean = true): Observable<E> {
  return from(promise.catch(err => {
    console.log(err);
    return err;
  }));
}

@Injectable({ providedIn: 'root' })
export class LocalDbService extends Dexie {
  private readonly lsTable: Map<string, DbTable<any, any>> = new Map();

  constructor() {
    super("ts_web", { autoOpen: true, allowEmptyDB: true });

    this.version(50).stores({
      chanel: "chanel_id",
      software: 'software_id',
      groupHelp: 'help_id',
      question: 'question_id',
      ticket: 'ticket_id',
      clsTeam: 'id',
      clsAssign: 'id',
      clsSubjectType: 'id',
      clsReplied: 'id',
      clsCate: 'id',
      clsCateSub: 'id',
      clsTicketType: 'id',
      clsPriority: 'id',
      clsProduct: 'id',
      clsStage: 'id',
      clsTag: 'id',
      agTable: 'code',
      agColumn: '[field_name+ag_code]',
      apiInfo: 'api_id',
      user: 'user_id',
      appConfig: 'code',

    })

    this.open()
        .then(s => console.log(`opening database success `, s._dbSchema))
        .catch(err => console.error(`opening database error -> `, err));
  }


  private get_set_tb<T>(tableName: string, modelType: Type<T>, customTable?: Type<any>): any {
    if (this.lsTable.has(tableName)) return this.lsTable.get(tableName);
    else {
      const modelTable = Objects.isNull(customTable) ? DbTable : customTable;
      const newTable = new modelTable(this.table(tableName), modelType);
      this.lsTable.set(tableName, newTable);
      return newTable;
    }
  }








  get chanel(): DbTable<Chanel, number> {
    return this.get_set_tb('chanel', Chanel);
  }

  get software(): DbTable<Software, number> {
    return this.get_set_tb('software', Software);
  }

  get groupHelp(): DbTable<GroupHelp, number> {
    return this.get_set_tb('groupHelp', GroupHelp);
  }

  get question(): DbTable<Question, number> {
    return this.get_set_tb('question', Question);
  }

  get clsTeam(): DbTable<ClsTeam, number> {
    return this.get_set_tb('clsTeam', ClsTeam);
  }

  get clsAssign(): DbTable<ClsAssign, number> {
    return this.get_set_tb('clsAssign', ClsAssign);
  }

  get clsSubjectType(): DbTable<ClsSubjectType, number> {
    return this.get_set_tb('clsSubjectType', ClsSubjectType);
  }

  get clsReplied(): DbTable<ClsRepiled, number> {
    return this.get_set_tb('clsReplied', ClsRepiled);
  }

  get clsCate(): DbTable<ClsCategory, number> {
    return this.get_set_tb('clsCate', ClsCategory);
  }

  get clsCateSub(): DbTable<ClsCategorySub, number> {
    return this.get_set_tb('clsCateSub', ClsCategorySub);
  }

  get clsTicketType(): DbTable<ClsTicketType, number> {
    return this.get_set_tb('clsTicketType', ClsTicketType);
  }

  get clsPriority(): DbTable<ClsPriority, number> {
    return this.get_set_tb('clsPriority', ClsPriority);
  }

  get clsTag(): DbTable<ClsTag, number> {
    return this.get_set_tb('clsTag', ClsTag);
  }

  get clsTopic(): DbTable<ClsTopic, number> {
    return this.get_set_tb('clsTopic', ClsTopic);
  }

  get clsProduct(): DbTable<ClsProduct, number> {
    return this.get_set_tb('clsProduct', ClsProduct);
  }

  get clsStage(): DbTable<ClsStage, number> {
    return this.get_set_tb('clsStage', ClsStage);
  }

  get user(): DbUserTable {
    return this.get_set_tb('user', User, DbUserTable);
  }

  get ticketTemplate(): DbTable<Ticket, number> {
    return this.get_set_tb('ticketTemplate', Ticket);
  }

  get apiInfo(): DbTable<ApiInfo, string> {
    return this.get_set_tb('apiInfo', ApiInfo);
  }

  get agTable(): DbAgTable {
    return this.get_set_tb('agTable', AgTable, DbAgTable);
  }

  get tbAppCfg(): ConfigTable {
    return this.get_set_tb('appConfig', AppConfig, ConfigTable);
  }

}

export class DbTable<T, Id extends IndexableType> {
  public readonly jsonToModel: ResponseToModel<T>;
  protected readonly table: Table<T, Id, any>;
  protected readonly tableName: string;
  protected readonly modelType: Type<T>;

  constructor(table: Table<T, Id, any>, type: Type<T>) {
    this.jsonToModel = (<any>type)['from'];
    this.table = table;
    this.modelType = type;
  }

  get_first(): Observable<T> {
    return observable(this.table.toCollection()
      .first(json => this.jsonToModel(json)));
  }

  getById(id: Id): Observable<T> {
    return observable(this.table.get(id));//.then(s => isNull(s) ? null : this.jsonToModel(s)));
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
      return observable(this.table.put(dataSave, key).then(s => dataSave));
    }
    else {
      const dataSave = (<any[]>data).flatMap(item => isClass(item) ? item : this.jsonToModel(item));
      return observable(this.table.bulkPut(dataSave).then(s => dataSave));
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


export class DbUserTable extends DbTable<User, number> {

  read(): Observable<User> {
    return observable(this.table.toCollection().first());
  }

  saveToken(user_id: number, token: AuthToken): Observable<any> {
    return this.existsById(user_id).pipe(switchMap(flag => {
      if (!flag) return this.save(new User().update({ token, user_id }));
      else return this.update(user_id, { token });
    }));
  }

}

export class DbAgTable extends DbTable<AgTable, string> {

}

export class ConfigTable extends DbTable<any, string> {


  set(code: string, value: any): Observable<any> {
    if(Objects.isNull(value)) return this.deleteById(code);
    else return observable(this.table.put({value, code}, code));//.pipe(map(() => value));
  }

  read(): Observable<AppConfig> {
    const json: JsonObject = {};
    return observable(this.table.toCollection().each((item, cursor) => {
      let field: any = cursor.key;
      json[field] = item?.value;
    }).then(() => AppConfig.from(json)));
  }


}
