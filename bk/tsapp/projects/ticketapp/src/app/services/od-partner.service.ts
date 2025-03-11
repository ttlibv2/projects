import { Injectable } from "@angular/core";
import { ClientService } from "./client.service";
import { EMPTY, Observable, map, of, switchMap } from "rxjs";
import { ClsPartner, ClsSearch } from "../models/od-cls";
import { Objects, Page, Pageable } from "ts-ui/helper";
import { SearchContact } from "../models/cls-partner";

// const { isTrue } = Objects;

// export interface SearchData {
//   [key: string]: any;

//   vat?: string;
//   email?: string;
//   mobile?: string;
//   phone?: string;

//   // create_new
//   street?: string;
//   company_name?: string;
//   customer_name?: string;

//   //form_option
//   options?: {
//     [key: string]: boolean;
//     isperson?: boolean;
//     iscompany?: boolean;
//     isvat?: boolean;
//     isemail?: boolean;
//     ismobile?: boolean;
//   },

//   page?: {
//     pageSize?: number;
//     operator?: 'like' | 'equal';

//   }


// }


@Injectable({ providedIn: 'root' })
export class OdPartnerService extends ClientService {

  search(object: SearchContact): Observable<Page<ClsPartner>> {

    const data = Objects.extractValueNotNull({ 
      vat: object.vat, mobile: object.mobile, email: object.email 
    });
    
    const cls = ClsSearch.from<ClsPartner>({
      limit: object.limit || 80,
      offset: object.offset || 0,
      operator: object.operator,
      data: ClsPartner.from(data),
      filter: [
        object.isvat ? ['vat', '!=', 'false'] : null,
        object.ismobile ? ['mobile', '!=', 'false'] : null,
        object.isemail ? ['email', '!=', 'false'] : null,
        object.isperson ? ['is_company', '!=', 'false'] : null,
        object.iscompany ? ['is_company', '!=', 'true'] : null,
      ].filter(v => Objects.notNull(v))
    });

    cls.data = ClsPartner.from(data);
    cls.filter = [
      object.isvat ? ['vat', '!=', 'false'] : null,
      object.ismobile ? ['mobile', '!=', 'false'] : null,
      object.isemail ? ['email', '!=', 'false'] : null,
      object.isperson ? ['is_company', '!=', 'false'] : null,
      object.iscompany ? ['is_company', '!=', 'true'] : null,
    ].filter(v => Objects.notNull(v));

    return this.searchPartner(cls);
  }

  searchPartner(search: ClsSearch<ClsPartner>): Observable<Page<ClsPartner>> {
    const url = `/od-api/partner/search`;
    return this.post(url, search).pipe(map((res: any) => {
      return Page.from(res, i => ClsPartner.from(i));
    }));
  }

  createPartner(cls: ClsPartner): Observable<ClsPartner> {
    const url = '/od-api/partner/create';
    return this.post(url, cls, { companyId: null }).pipe(map(s => ClsPartner.from(s)));
  }


}