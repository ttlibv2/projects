import { Injectable } from '@angular/core';
import { ClientService } from "./client.service";
import { Observable, forkJoin, map, of, switchMap, tap } from "rxjs";
import { Catalog } from "../models/catalog";
import { JsonObject } from '../models/common';
import { ClsAssign } from '../models/od-cls';
import { DbTable, LocalDbService } from './local-db.service';
import { LoggerService } from 'ts-logger';


export const CATALOG_MAP: { [key: string]: (db: LocalDbService) => DbTable } = {
  ls_chanel: db => db.chanel,
  ls_software: db => db.software,
  ls_group_help: db => db.groupHelp,
  ls_question: db => db.question,
  ls_helpdesk_team: db => db.clsTeam,
  ls_assign: db => db.clsAssign,
  ls_subject_type: db => db.clsSubjectType,
  ls_repiled_status: db => db.clsReplied,
  ls_category: db => db.clsCate,
  ls_category_sub: db => db.clsCateSub,
  ls_ticket_tag: db => db.clsTag,
  ls_priority: db => db.clsPriority,
  ls_ticket_type: db => db.clsTicketType,
  ls_topic: db => db.clsTopic,
  ls_stage: db => db.clsStage,
  ls_product: db => db.clsProduct,
  ls_teamplate: db => db.template
};

function jsonToCatalog(db: LocalDbService, data: JsonObject, logger?: LoggerService): Catalog {
  logger.warn('catalog response => ', data)
  const names = Object.keys(data);
  const array = names.map(name => {
    if (name in CATALOG_MAP) {
      const fncTb: DbTable = CATALOG_MAP[name](db);
      const newData = data[name].flatMap((item: any) => fncTb.jsonToModel(item))
      return [name, newData];
    }
    else if('ls_teamplate' === name) {
      const ls: JsonObject = data['ls_teamplate'];
      return [name, new Map(Object.keys(ls).map(k => [k, ls[k]]))];
    }
    else {
      if (logger) logger.warn(`The catalog without key not defined [${name}]`);
      return [name, data[name]];
    }
  });

  return Catalog.from(Object.fromEntries(array));
}


@Injectable({ providedIn: 'root' })
export class CatalogService extends ClientService {

  read_db(lsName: string = 'all'): Observable<Catalog> {
    const allKey = lsName === 'all' ? Object.keys(CATALOG_MAP) : lsName.split(',');
    const fncs = allKey.map(name => [name, CATALOG_MAP[name](this.db).getAll()]);
    return forkJoin(Object.fromEntries(fncs)).pipe(switchMap(res => {
      return of(Catalog.from(jsonToCatalog(this.db, res, this.logger)));
    }));
  }


  getAll(include: string = 'all'): Observable<Catalog> {
    const url = '/ts-api/catalog/get-all';
    return this.get(url, { include }).pipe(map(res => {
      const newData = jsonToCatalog(this.db, res, this.logger);
      this.saveCatalogToDb(newData);
      return newData;
    }));
  }


  searchAssign(keyword: string): Observable<ClsAssign[]> {
    const url = '/od-api/user/search';
    return this.get(url, { keyword }).pipe(map((res: any[]) => ClsAssign.fromList(res)));
  }

  searchAssignByIds(ids: number[]): Observable<ClsAssign[]> {
    const url = '/od-api/user/search';
    return this.get(url, { ids }).pipe(map((res: any[]) => ClsAssign.fromList(res)));
  }



  private saveCatalogToDb(data: Catalog, hasSave: boolean = false): void {
    if (hasSave === true) {
      for (const key of Object.keys(data)) {
        if (!(key in CATALOG_MAP)) continue;
        else {
          CATALOG_MAP[key](this.db).save(data[key]).subscribe({
            next: res => this.logger.info(`save ${key}....`)
          });
        }
      }
    }
  }



}