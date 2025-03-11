import { DynamicDialogConfig, DynamicDialogRef } from "primeng/dynamicdialog";
import { FindPartnerComponent } from "./find-partner.component";
import { BiConsumer, booleanAttribute, Objects, Page } from "ts-ui/helper";
import { ClsPartner } from "../../models/od-cls";

export const searchOption: { [name: string]: SearchItem } = {
  'isvat': { label: 'Mã số thuế', name: 'isvat', field: 'vat', checked: true },
  'isemail': { label: 'E-mail', name: 'isemail', field: 'email', checked: true, filter: ['email', '!=', false] },
  'ismobile': { label: 'Điện thoại', name: 'ismobile', field: 'mobile', checked: false, filter: ['mobile', '!=', false] },
  'isperson': { label: 'Cá nhân', name: 'isperson', checked: true, filter: ['is_company', '=', false] },
  'iscompany': { label: 'Công ty', name: 'iscompany', checked: false, filter: ['is_company', '=', true] },
};

export const DEFAULT_DATA: SearchData = {
  operator: 'equal',
  pageSize: 20,
  options: {
    isvat: true,
    isemail: true,
    ismobile: false,
    isperson: true,
    iscompany: false
  }
};

export function asBool(v: any, val: boolean): boolean {
  if (Objects.isBlank(v)) return val;
  else return booleanAttribute(v);
}

export type AfterSearchFunc = BiConsumer<DynamicDialogRef<FindPartnerComponent>, Page<ClsPartner>>;

export interface SearchItem {
  label: string;
  name: string;
  checked: boolean;
  field?: string;
  filter?: any[];
}

export interface SearchData {
  [key: string]: any;

  vat?: string;
  email?: string;
  mobile?: string;
  phone?: string;

  // create_new
  street?: string;
  company_name?: string;
  customer_name?: string;
  display_name?: string;
  company_id?: number;
  customer_id?: number;
  pageSize?: number;
  operator?: 'like' | 'equal';

  //form_option
  options?: {
    [key: string]: boolean;
    isperson?: boolean;
    iscompany?: boolean;
    isvat?: boolean;
    isemail?: boolean;
    ismobile?: boolean;
  }


}

export interface State {
  asyncLoading?: boolean;
  asyncSave?: boolean;
  visiblePerson?: boolean;
  visibleComp?: boolean;
  visibleNewEmail?: boolean;
  hasDialog?: boolean;
  isViewAdd?: boolean;
}

export interface InputOption extends DynamicDialogConfig {
  autoSelect?: boolean;
  timeDelay?: number;
  trialMST?: boolean;
  newEmail?: boolean;
  showToatResult?: boolean;
  max_width?: string;
  tableHeight?: 'auto' | string;
  afterSearch?: AfterSearchFunc;
}

export interface SearchOpt {
  showWarning?: boolean;
  trialMST?: boolean;
  newEmail?: boolean;
  hint?: string; 
}

export interface PersonInfo {
  email?: string;
  customer_name?: string;
  customer_id?: number;
  company_id?: number;
  phone?: string;
  mobile?: string;
}