import { Injectable } from '@angular/core';
import {ModelApi} from "./model-api.service";
import { ResponseToModel } from '../models/common';
import {TicketLog} from "../models/ticket-log";

@Injectable({providedIn: 'root'})
export class TicketLogService extends ModelApi<TicketLog> {

    override basePath(): string {
        return '/ts-api/ticket/log';
    }
    override resToModel(): ResponseToModel<TicketLog> {
        return json => TicketLog.from(json);
    }

}
