import { Injectable } from '@angular/core';
import {ModelApi} from "./model-api.service";
import {UserCfg} from "../models/user-cfg";
import { ResponseToModel } from '../models/common';

@Injectable({providedIn: 'root'})
export class UserCfgService extends ModelApi<UserCfg> {

    override basePath(): string {
        return `/ts-api/users/cfg`;
    }
    override resToModel(): ResponseToModel<any> {
        return json => UserCfg.from(json);
    }

}
