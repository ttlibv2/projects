import {InjectionToken, Type} from "@angular/core";
import Dexie, {ChromeTransactionDurability} from "dexie";
import { DexieTb} from "./dexie.db";

export const DB_CONFIG_TOKEN = new InjectionToken<LocalDbConfig>('DB_CONFIG_TOKEN');
export type DatabaseSchema = { [tableName: string]: TableSchema };

export const DEFAULT_DB_CONFIG: Partial<LocalDbConfig> = {
    option: {
        //autoOpen: true,
        allowEmptyDB: false
    }
}

export interface LocalDbConfig {
    dbName: string;
    dbVersion: number;
    option: LocalDbOption;
    schema:TableSchema[];

}

export interface LocalDbOption  {
    addons?: Array<(db: Dexie) => void>;
    //autoOpen?: boolean;
    indexedDB?: {
        open: Function;
    };
    IDBKeyRange?: {
        bound: Function;
        lowerBound: Function;
        upperBound: Function;
    };
    allowEmptyDB?: boolean;
    modifyChunkSize?: number;
    chromeTransactionDurability?: ChromeTransactionDurability;
    cache?: "immutable" | "cloned" | "disabled";
}


export interface TableSchema<E=any, T extends DexieTb=any> {
    name: string;
    keyPath: string;
    entityClass: Type<E>;
    tableClass?: Type<T>;
    table?: () => T;
}

export class LocalDbError extends Error {

}