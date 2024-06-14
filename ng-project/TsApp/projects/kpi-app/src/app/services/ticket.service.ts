import { Injectable } from '@angular/core';
import {ModelApi} from "./model-api.service";
import {Ticket} from "../models/ticket";
import { ResponseToModel } from '../models/common';

@Injectable({providedIn: 'root'})
export class TicketService extends ModelApi<Ticket> {

    override basePath(): string {
        return '/ts-api/ticket';
    }

    override resToModel(): ResponseToModel<any> {
        return json => Ticket.from(json);
    }

}
