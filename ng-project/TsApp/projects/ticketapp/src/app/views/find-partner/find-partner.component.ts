import {
  AfterViewInit,
  ChangeDetectionStrategy,
  ChangeDetectorRef,
  Component,
  Input,
  OnInit,
  ViewChild,
  ViewEncapsulation
} from '@angular/core';
import { Validators } from "@angular/forms";
import { DialogService, DynamicDialogComponent, DynamicDialogRef } from "primeng/dynamicdialog";
import { Objects, Page, booleanAttribute } from 'ts-ui/helper';
import { ColDef } from "@ag-grid-community/core";
import { ToastService } from 'ts-ui/toast';
import { OdTicketService } from '../../services/od.service';
import { ClsPartner, ClsSearch } from '../../models/od-cls';
import { LoggerService } from 'ts-ui/logger';
import { AgTable, TableColumn, TableOption } from 'ts-ui/ag-table';
import { StorageService } from "../../services/storage.service";
import { ModalService } from "ts-ui/modal";
import { FormGroup, FormsBuilder } from 'ts-ui/forms';
import { delay, of } from 'rxjs';


const { isBlank, notBlank, notNull, isNull, isEmpty, isTrue } = Objects;

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
  operator?: string;

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

interface State {
  asyncLoading?: boolean;
  asyncSave?: boolean;
  visiblePerson?: boolean;
  visibleComp?: boolean;
  allowNew?: boolean;
  showToolbar?: boolean;
}

interface InputOption {
  autoSelect?: boolean;
  timeDelay?: number;
}

const searchOption: { [name: string]: SearchItem } = {
  'isvat': { label: 'Mã số thuế', name: 'isvat', field: 'vat', checked: true },
  'isemail': { label: 'E-mail', name: 'isemail', field: 'email', checked: true, filter: ['email', '!=', false] },
  'ismobile': { label: 'Điện thoại', name: 'ismobile', field: 'mobile', checked: true, filter: ['mobile', '!=', false] },
  'isperson': { label: 'Cá nhân', name: 'isperson', checked: true, filter: ['is_company', '=', false] },
  'iscompany': { label: 'Công ty', name: 'iscompany', checked: false, filter: ['is_company', '=', true] },
};

const defaultInputSearch: SearchData = {
  operator: 'like',
  pageSize: 20,
  options: {
    isvat: true,
    isemail: true,
    ismobile: true,
    isperson: true,
    iscompany: false
  }
};

function asBool(v: any, val: boolean): boolean {
  if(isBlank(v)) return val;
  else return booleanAttribute(v);
}

@Component({
  selector: 'ts-find-partner',
  templateUrl: './find-partner.component.html',
  styleUrl: './find-partner.component.scss',
  encapsulation: ViewEncapsulation.None,
})
export class FindPartnerComponent implements OnInit {

  @ViewChild('agTable', { static: true })
  private agTable: AgTable;

  @Input({transform: (v:any) => asBool(v, true)}) 
  set isvat(b: boolean) { this.inputSearch.options.isvat = b; }
  
  @Input({transform: (v:any) => asBool(v, true)}) 
  set isemail(b: boolean) { this.inputSearch.options.isemail = b; }

  @Input({transform: (v:any) => asBool(v, true)}) 
  set ismobile(b: boolean) { this.inputSearch.options.ismobile = b; }

  @Input({transform: (v:any) => asBool(v, true)}) 
  set isperson(b: boolean) { this.inputSearch.options.isperson = b; }

  @Input() 
  set iscompany(b: boolean) { this.inputSearch.options.iscompany = b; }

  @Input() 
  set vat(s: string) { this.inputSearch.vat = s; }

  @Input() 
  set email(s: string) { this.inputSearch.email = s; }

  @Input() 
  set mobile(s: string) { this.inputSearch.mobile = s; }


  form: FormGroup;
  inputOption: InputOption = {timeDelay: 1000};
  page: Page<ClsPartner>;
  inputSearch: SearchData = { ...defaultInputSearch };
  dataSelect: ClsPartner;
  state: State = {
    allowNew: false,
    showToolbar: true
  };

  tableOption: TableOption = {
    height: '350px'
  };

  columns: TableColumn[] = [
    {
      headerName: 'Mã số thuế', field: 'vat', colId: 'vat', width: 150,
      cellRenderer: (p: any) => `<a href="${this.link(p.data.id)}" target="_blank" class="open_link">${p?.value}</i></a>`,
      cellStyle: { 'color': 'red', 'font-weight': 'bold' }
    },
    { headerName: 'Tên hiển thị', field: 'display_name', colId: 'display_name', width: 400 },
    { headerName: 'E-mail', field: 'email', colId: 'email', width: 230 },
    { headerName: 'Số điện thoại', field: 'mobile', colId: 'mobile', width: 129 },
    { headerName: 'Người liên hệ', field: 'customer_name', colId: 'customer_name', width: 168 },
    { headerName: 'Tên công ty', field: 'company_name', colId: 'company_name', width: 260 }
  ];

  get visiblePersonBtn(): boolean {
    return this.state.visiblePerson && this.state.allowNew;
  }

  get visibleCompBtn(): boolean {
    return this.state.visibleComp && this.state.allowNew;
  }

  get formOption(): SearchItem[] {
    return Object.values(searchOption);
  }

  get rowData(): ClsPartner[] {
    return this.page?.data || [];
  }

  constructor(
    private logger: LoggerService,
    private config: StorageService,
    private cdRef: ChangeDetectorRef,
    private alert: ToastService,
    private fb: FormsBuilder,
    private ref: DynamicDialogRef,
    private toast: ToastService,
    private modal: ModalService,
    private odSrv: OdTicketService) {
  }

  ngOnInit(): void {
    this.createFormGroup();


    const instance = this.modal.getInstance(this.ref);
    if(instance && instance.data) {
      const {data, option} = instance.data;
      this.inputSearch = Objects.mergeDeep(this.inputSearch, data);
      this.inputOption = Objects.mergeDeep({}, option);
      this.form.reset(this.inputSearch);
      this.clickSearch(false);
    }
    else {
      this.form.reset(this.inputSearch);
      this.clickSearch(false);
    }

   
  }

  clickSearch(showWarning: boolean = true) {
    const json = <SearchData>this.form.getRawValue();
    const keyCheck = Object.keys(json.options).filter(k => isTrue(json.options[k]));

    const searchData = this.formToObject(json, keyCheck);
    if (isEmpty(searchData)) {
      if (showWarning) this.alert.warning(this.config.i18n.searchInvalid);
      return;
    }

    const filter = keyCheck.map(k => searchOption[k]?.filter).filter(o => notNull(o));

    const clsSearch: ClsSearch = ClsSearch.from({
      data: searchData, filter: filter,
      limit: json.pageSize ?? 20,
      operator: json.operator
    });

    this.state.asyncLoading = true;
    this.dataSelect = undefined;

    const ref = this.alert.loading(this.config.i18n.searchAwait);
    this.odSrv.searchPartner(clsSearch).subscribe({
      next: (page: Page) => {
        this.page = page;//[...page?.data] ;
        this.state.asyncLoading = false;
        this.alert.close(ref);
        this.alert.success(`Đã tìm được ${page?.total ?? 0} dòng.`);

        if(this.inputOption?.autoSelect && page.data.length > 0) {
          of(111).pipe(delay(this.inputOption.timeDelay)).subscribe(_ => {
            this.selectAndClose(page.data[0]);
          });
        }

      },
      error: err => {
        this.logger.error('search partner error: ', err);
        this.state.asyncLoading = false;
        this.alert.close(ref);
        this.page = undefined;
      }
    });
  }


  clickResetForm(): void {
    this.form.reset(this.inputSearch);
  }

  clickNewPerson(): void {
    this.createContact('person');
  }

  clickNewCompany(): void {
    this.createContact('company');
  }

  closeDialog(): void {
    this.ref.close();
  }

  selectAndClose(data?: ClsPartner): void {
    this.ref.close(data || this.dataSelect);
  }


  clickRow(data: ClsPartner) {
    const { operator, options, pageSize } = <SearchData>this.form.getRawValue();
    this.form.reset({ ...data, operator, options, pageSize });
    this.dataSelect = data;
  }



  private createFormGroup() {
    this.form = this.fb.group({
      vat: [null], phone: [null],
      email: [null], street: [null],
      company_name: [null], customer_name: [null],
      display_name: [{ value: null, disabled: true }],
      company_id: [null], customer_id: [null],
      pageSize: [20], operator: ['like'],

      // form_option
      options: this.fb.group({
        isvat: [true], isemail: [true],
        ismobile: [true], isperson: [true],
        iscompany: [false]
      })
    });

    this.form.valueChanges.subscribe({
      next: data => {
        const { customer_id, company_id, pageSize } = data;
        this.state.visibleComp = isBlank(company_id);
        this.state.visiblePerson = notBlank(company_id) && isBlank(customer_id);
      }
    });

  }

  private formToObject(json: Partial<SearchData>, keys: string[]): any {
    const newKeys = keys.map(k => searchOption[k].field).filter(n => notBlank(json[n]));
    return Objects.arrayToJson(newKeys, k => [k, json[k]]);
  }

  private link(id: number): string {
    const link = this.config.loginUser.ts_links['od_partner'];
    return link.replace(`{id}`, `${id}`);
  }


  private createContact(type: 'person' | 'company') {

    //-- only support
    if (!['person', 'company'].includes(type)) {
      return;
    }

    const value: SearchData = this.form.getRawValue();
    const phone = value['phone'] ?? value['mobile'];

    let hasNext: boolean = true;
    let cls: ClsPartner = ClsPartner.from({
      company_type: type,
      phone: phone, mobile: phone,
      email: value.email
    });

    if (type === 'person') {

      //-- báo lỗi > chưa chọn công ty
      if (isNull(value['company_id'])) {
        this.alert.warning(`<b>Vui lòng chọn công ty.</b>`);
        this.state.asyncSave = false;
        hasNext = false;
        return;
      }

      cls.update({
        name: value['customer_name'] ?? value['email'],
        parent_id: value['company_id']

      });

    }//

    else if(type === 'company'){
      const hasOk = Objects.anyNotBlank(value.company_name, value.email);
      if(hasOk) cls.update({
        name: value['company_name'] ?? value['email'],
        vat: value['vat'],
        street: value['street'],
        parent_id: false
      });

      else {
        hasNext = false;
        this.form.get('email').setErrors({});
        this.form.get('company_name').setErrors({});
        this.toast.warning(`Vui lòng nhập thông tin <b>[Tên công ty]</b> hoặc <b>[Email]</b>`);
      }

    }

    // create
    if(hasNext === true){
    let name = type == 'company' ? 'Công ty' : 'Cá nhân';
    this.state.asyncSave = true;
    this.odSrv.createPartner(cls).subscribe({
      next: (data: ClsPartner) => {
        this.state.asyncSave = false;
        this.alert.success(`[${data.id}] Tạo <b>${name}</b> thành công`);
        this.agTable.addRow(data);
        this.clickRow(data);
      },
      error: err => {
        this.state.asyncSave = false;
        this.alert.error(`Xảy ra lỗi tạo <b>${name}</b> > ${err.message} !`);
      }
    });
}

  }
}