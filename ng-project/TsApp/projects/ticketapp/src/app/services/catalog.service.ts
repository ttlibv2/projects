import { Injectable } from '@angular/core';
import { ClientService } from "./client.service";
import { Observable, forkJoin, map, of, switchMap, tap } from "rxjs";
import { Catalog } from "../models/catalog";
import { JsonObject } from '../models/common';
import { ClsAssign } from '../models/od-cls';
import {LocalTable} from "ts-ui/local-db";
import {StorageService} from "./storage.service";

export const CATALOG_MAP: { [key: string]: (db: StorageService) => LocalTable } = {
  ls_chanel:db => db.chanelTb,
  ls_software: db => db.softwareTb,
  ls_group_help: db => db.groupHelpTb,
  ls_question: db => db.questionTb,
  ls_helpdesk_team: db => db.clsTeamTb,
  ls_assign: db => db.clsAssignTb,
  ls_subject_type: db => db.clsSubjectTypeTb,
  ls_replied_status: db => db.clsRepliedTb,
  ls_category: db => db.clsCateTb,
  ls_category_sub: db => db.clsCateSubTb,
  ls_ticket_tag: db => db.clsTagTb,
  ls_priority: db => db.clsPriorityTb,
  ls_ticket_type: db => db.clsTicketTypeTb,
  ls_topic: db => db.clsTopicTb,
  ls_stage: db => db.clsStageTb,
  ls_product: db => db.clsProductTb,
  ls_template: db => db.templateTb
};

@Injectable({ providedIn: 'root' })
export class CatalogService extends ClientService {

  read_db(lsName: string = 'all'): Observable<Catalog> {
    const allKey = lsName === 'all' ? Object.keys(CATALOG_MAP) : lsName.split(',');
    const fncs = allKey.map(name => [name, CATALOG_MAP[name](this.storage).getAll()]);
    return forkJoin(Object.fromEntries(fncs)).pipe(switchMap(res => {
      return of(Catalog.from(res));
    }));
  }


  getAll(params: JsonObject): Observable<Catalog> {
    const url = '/ts-api/catalog/get-all';
    return this.get(url, params).pipe(map(res => {
      const newData = Catalog.from(res);
      this.saveCatalogToDb(newData, true);
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



  private saveCatalogToDb(catalog: any, hasSave: boolean = false): void {
    if (hasSave === true) {
      this.storage.set_catalog(catalog);

      for (const key of Object.keys(catalog)) {
       // this.storage.set_catalogWithKey(key, catalog[key]).subscribe();

        if (!(key in CATALOG_MAP)) continue;
        else {
          CATALOG_MAP[key](this.storage).bulkPut(catalog[key]).subscribe({
            next: (res:any) => this.logger.info(`save ${key}....`)
          });
        }
      }
    }
  }

  clearCache(): Observable<any> {
    const url = '/ts-api/catalog/clear-cache';
    return this.get(url);
  }


}