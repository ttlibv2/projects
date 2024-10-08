import { Injectable } from '@angular/core';
import { ModelApi } from "./model-api.service";
import { Ticket } from "../models/ticket";
import { ResponseToModel } from '../models/common';
import { Observable } from 'rxjs';
import { JsonAny,Page, Pageable } from 'ts-ui/helper';

export interface SearchOption {
    created_min?: string;
    created_max?: string;
    updated_min?: string;
    updated_max?: string;
    user_id?: number;
    is_send?: boolean;
    is_note?: boolean;
    is_attach?: boolean;
    is_close?: boolean;
    is_report?: boolean;
}

@Injectable({ providedIn: 'root' })
export class TicketService extends ModelApi<Ticket> {

    override basePath(): string {
        return '/ts-api/ticket';
    }

    override resToModel(): ResponseToModel<any> {
        return json => Ticket.from(json);
    }

    override search(data: SearchOption, page?: Pageable): Observable<Page<Ticket>> {
        return super.search(data, { page: 0, size: 1000, ...page });
    }

    save(data: any): Observable<Ticket> {
        return this.savePost('save', data);
    }

    sendOd(action: string, ticket_id: number, options: JsonAny): Observable<Ticket> {
        const url = '/od-api/ticket/send';
        return this.post(url, {...options, action, ticket_id});
    }

}
