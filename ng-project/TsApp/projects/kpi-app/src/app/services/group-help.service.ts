import { Injectable } from '@angular/core';
import {ModelApi} from "./model-api.service";
import {ResponseToModel} from "../models/common";
import {GroupHelp} from "../models/group-help";

@Injectable({
  providedIn: 'root'
})
export class GroupHelpService extends ModelApi<GroupHelp> {
  override basePath(): string {
    return `/ts-api/group-help`;
  }
  override resToModel(): ResponseToModel<any> {
    return json => GroupHelp.from(json);
  }


}
