import { Injectable } from "@angular/core";
import { ClientService } from "./client.service";
import { Observable, map } from "rxjs";
import { ClsPartner, ClsSearch } from "../models/od-cls";
import { Page } from "ts-ui/helper";

@Injectable({
    providedIn: 'root'
})
export class OdTicketService extends ClientService {

    searchPartner(search: ClsSearch<ClsPartner>): Observable<Page<ClsPartner>> {
        const url = `/od-api/partner/search`;
        return this.post(url, search).pipe(map((res: any) => {
            return Page.from(res, i => ClsPartner.from(i));
        }));
    }

    createPartner(cls: ClsPartner): Observable<ClsPartner> {
        const url = '/od-api/partner/create';
        return this.post(url, cls, {companyId: null}).pipe(map(s => ClsPartner.from(s)));
    }


}