import { Injectable } from '@angular/core';
import {RestApiService} from "./rest-api.service";
import {ApiInfo} from "../models/api-info";
import {ResponseToModel} from "../models/common";

@Injectable({providedIn: 'root'})
export class ApiInfoService extends RestApiService<ApiInfo> {

  basePath(): string {
    return "ts.api-info";
  }

  resToModel(): ResponseToModel<any> {
    return json => new ApiInfo().update(json);
  }

}
