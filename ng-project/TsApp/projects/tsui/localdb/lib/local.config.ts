import {InjectionToken} from "@angular/core";
import {TbSchema} from "./local.schema";

export const DB_CONFIG_TOKEN = new InjectionToken<DBConfig>('DB_CONFIG_TOKEN');

export type TableId = string | number | Date;

export interface DBConfig {
    dbName: string;
    dbVersion: number;
    options: {
        autoOpen?: boolean;
        allowEmptyDB?: boolean;
        modifyChunkSize?: number;
        cache?: "immutable" | "cloned" | "disabled";
    },
    schema: Partial<TbSchema>[];
}

export class EntityModel {
    [key: string]: any;
}

export const DEFAULT_DB_CONFIG: Partial<DBConfig> = {
  dbVersion: 1.0,
  options: {
      autoOpen: true,
      allowEmptyDB: true
  }
};