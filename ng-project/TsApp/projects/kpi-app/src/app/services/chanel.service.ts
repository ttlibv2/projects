import { Injectable } from '@angular/core';
import {ModelApi} from "./model-api.service";
import { ResponseToModel } from '../models/common';
import {Chanel} from "../models/chanel";

@Injectable({providedIn: 'root'})
export class ChanelService extends ModelApi<Chanel> {

    override basePath(): string {
       return `/ts-api/chanel`;
    }

    override resToModel(): ResponseToModel<any> {
       return json => Chanel.from(json);
    }

   


}
