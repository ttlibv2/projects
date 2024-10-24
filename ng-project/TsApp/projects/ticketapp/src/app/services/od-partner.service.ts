import { Injectable } from "@angular/core";
import { ClientService } from "./client.service";
import { Observable, map } from "rxjs";
import { ClsPartner, ClsSearch } from "../models/od-cls";
import { Page } from "ts-ui/helper";

// 'isvat': { label: 'Mã số thuế', name: 'isvat', field: 'vat', checked: true },
// 'isemail': { label: 'E-mail', name: 'isemail', field: 'email', checked: true, filter: ['email', '!=', false] },
// 'ismobile': { label: 'Điện thoại', name: 'ismobile', field: 'phone', checked: true, filter: ['mobile', '!=', false] },
// 'isperson': { label: 'Cá nhân', name: 'isperson', checked: true, filter: ['is_company', '=', false] },
// 'iscompany': { label: 'Công ty', name: 'iscompany', checked: false, filter: ['is_company', '=', true] },


export interface SearchUserOption {
    vat?: string;
    email?: string;
    mobile?: string;
    phone?: string;

    isvat?: boolean;
    isemail?: boolean;
    ismobile?: boolean;
    isperson?: boolean;
    iscompany?: boolean;
}



@Injectable({  providedIn: 'root' })
export class OdPartnerService extends ClientService {

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