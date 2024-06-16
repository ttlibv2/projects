import { Injectable } from '@angular/core';
import {ModelApi} from "./model-api.service";
import {AgTable} from "../models/ag-table";
import {ResponseToModel} from "../models/common";
import {Observable, switchMap} from "rxjs";

@Injectable({providedIn: 'root'})
export class AgTableService extends ModelApi<AgTable> {

  override basePath(): string {
    return "/ts-api/ag-table";
  }

  override resToModel(): ResponseToModel<AgTable> {
    return response => AgTable.from(response);
  }

  getByCode(tableCode: string, includeCol: boolean = true): Observable<AgTable> {
    const url = (`get-by-code/${tableCode}`);
    return this.getOne(url, {includeCol});
  }


}
