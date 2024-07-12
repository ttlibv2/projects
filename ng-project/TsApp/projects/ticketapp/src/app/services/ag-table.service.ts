import { Injectable } from '@angular/core';
import {ModelApi} from "./model-api.service";
import {AgTable} from "../models/ag-table";
import {ResponseToModel} from "../models/common";
import {Observable} from "rxjs";

@Injectable({providedIn: 'root'})
export class AgTableService extends ModelApi<AgTable> {

  override basePath(): string {
    return "/ts-api/ag-table";
  }

  override resToModel(): ResponseToModel<AgTable> {
    return json => AgTable.from(json);
  }

  getByCode(tableCode: string, includeCol: boolean = true): Observable<AgTable> {
    return this.getOne(`get-by-code`, {code: tableCode, includeCol});
  }

  


}
