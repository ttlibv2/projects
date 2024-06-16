import { Injectable } from '@angular/core';
import { ClientService } from "./client.service";
import { Observable, forkJoin, of, switchMap, tap } from "rxjs";
import { Catalog } from "../models/catalog";
// import { DbTable, LocalDbService } from "./local-db.service";
import { JsonObject } from '../models/common';
import { ClsAssign } from '../models/od-cls';


export const CATALOG_MAP: { [key: string]: (db: any) => any} = {//(db: LocalDbService) => DbTable<any, any> } = {
  ls_chanel: db => db.chanel,
  ls_software: db => db.software,
  ls_group_help: db => db.groupHelp,
  ls_question: db => db.question,
  ls_helpdesk_team: db => db.clsTeam,
  ls_assign: db => db.clsAssign,
  ls_subject_type: db => db.clsSubjectType,
  ls_repiled_status: db => db.clsRepiled,
  ls_category: db => db.clsCate,
  ls_category_sub: db => db.clsCateSub,
  ls_ticket_tag: db => db.clsTag,
  ls_priority: db => db.clsPriority,
  ls_ticket_type: db => db.clsTicketType,
  ls_topic: db => db.clsTopic,
  ls_stage: db => db.clsStage,
  ls_product: db => db.clsProduct
};



@Injectable({ providedIn: 'root' })
export class CatalogService extends ClientService {
 
  read_db(lsName: string = 'all'): Observable<Catalog> {
    const allKey = lsName === 'all' ? Object.keys(CATALOG_MAP) : lsName.split(',');
    const fncs = allKey.map(name => [name, CATALOG_MAP[name](this.db).getAll()]);
    return forkJoin(Object.fromEntries(fncs)).pipe(switchMap(res => {
      return of(Catalog.from(this.convertList(res)));
    }));
  }


  getAll(include: string = 'all'): Observable<Catalog> {
    const url = '/ts-api/catalog/get-all';
    return this.get(url, { include }).pipe(switchMap(res => {
      return of(Catalog.from(this.convertList(res))).pipe(
        tap(data => this.saveCatalogToDb(data))
      );
    }));
  }


  private convertList(data: JsonObject) {
    const names = Object.keys(data);
    const array =  names.map(name => {
      const fncTb = CATALOG_MAP[name];
      if(typeof fncTb !== 'function') throw new Error(`The catalog without key not defined [${name}]`);
      else {
        const toModel = fncTb(this.db).jsonToModel;
        const newData = data[name].flatMap((item: any) => toModel(item))
        return [name, newData];
      }
    });

    return Object.fromEntries(array);
  }

  saveCatalogToDb(data: Catalog): void {
    
  }

  searchAssign(keyword: string): Observable<ClsAssign> {
    const url = '/od-api/user/search';
    return this.get(url, {keyword})
      .pipe(switchMap((res:any[]) => res.map(i => ClsAssign.from(i))));
  }


}
