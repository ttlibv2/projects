import { Injectable } from '@angular/core';
import {ModelApi} from "./model-api.service";
import {Software} from "../models/software";
import { ResponseToModel } from '../models/common';

@Injectable({providedIn: 'root'})
export class SoftwareService extends ModelApi<Software> {

    override basePath(): string {
        return '/ts-api/software';
    }

    override resToModel(): ResponseToModel<any> {
        return json => Software.from(json);
    }
}
