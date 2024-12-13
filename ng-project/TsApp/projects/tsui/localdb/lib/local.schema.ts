import {Asserts, AssignObject, Callback, Objects, TsMap} from "ts-ui/helper";
import {LocalTable} from "./local.table";
import {Type} from "@angular/core";
import {IndexSpec, Table} from "dexie";
import {LocalDb} from "./local.db";
import {EntityModel, TableId} from "./local.config";

export class DbSchema extends TsMap<string, TbSchema> {}

export class TbSchema<E=any, Id extends TableId = any, T extends LocalTable=any> {
    name: string;
    keyPath?: string;
    entityClass?: Type<E>
    tableClass?: Type<T>;

    primKey?: IndexSpec;
    indexes?: IndexSpec[];
    dexieTb?: Table;
    jsonToModel?: Callback<any, E>;

    get newTableFunc() {
        return (db: LocalDb) => new this.tableClass(db, this);
    }

    get defaultModelFunc(): Callback<any, E> {
        const entity: any = Asserts.notNull(this.entityClass);
        if(typeof entity['from'] === 'function') {
            return json => entity['from'](json);
        }
        else {
            return json => new entity(json);
        }
    }

    static from(data: AssignObject<TbSchema>): TbSchema {
        const sc: TbSchema = Objects.assign(new TbSchema(), data);
        sc.entityClass = sc.entityClass ?? EntityModel;
        sc.tableClass = sc.tableClass ?? LocalTable;
        sc.jsonToModel = sc.jsonToModel ?? sc.defaultModelFunc;
        return sc;
    }



}