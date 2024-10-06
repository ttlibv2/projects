import {
  AfterViewInit, booleanAttribute,
  ChangeDetectionStrategy,
  ChangeDetectorRef,
  Component,
  Input,
  OnInit,
  ViewChild,
  ViewEncapsulation
} from '@angular/core';
import { FormBuilder, FormGroup } from "@angular/forms";
import { DialogService, DynamicDialogComponent, DynamicDialogRef } from "primeng/dynamicdialog";
import { Objects, Page } from 'ts-ui/helper';
import { ColDef } from "@ag-grid-community/core";
import { ToastService } from 'ts-ui/toast';
import { OdTicketService } from '../../services/od.service';
import { ClsPartner, ClsSearch } from '../../models/od-cls';
import { LoggerService } from 'ts-ui/logger';
import { AgTable, TableOption } from 'ts-ui/ag-table';
import {StorageService} from "../../services/storage.service";
import {ModalService} from "ts-ui/modal";

const { isBlank, notBlank, notNull, booleanValue } = Objects;

export interface SearchItem {
  label: string;
  name: string;
  checked: boolean;
  filter?: any[];
}

export interface SearchData {
  vat?: string;
  email?: string;
  mobile?: string;
  chk?: {
    vat?: string;
    email?: string;
    mobile?: string;
    person?: boolean;
    company?: boolean;
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

@Component({
  selector: 'ts-find-partner',
  templateUrl: './find-partner.component.html',
  styleUrl: './find-partner.component.scss',
  encapsulation: ViewEncapsulation.None,
  // changeDetection: ChangeDetectionStrategy.OnPush
})
export class FindPartnerComponent implements OnInit, AfterViewInit {

  wheres: {[key: string]: SearchItem} = {
    'vat': { label: 'Mã số thuế', name: 'vat', checked: true },
    'email': { label: 'E-mail', name: 'email', checked: true, filter: ['email', '!=', false] },
    'mobile': { label: 'Điện thoại', name: 'mobile', checked: true, filter: ['mobile','!=',false] },
    'person': { label: 'Cá nhân', name: 'person', checked: true, filter: ['is_company','=',false] },
    'company': { label: 'Công ty', name: 'company', checked: false, filter: ['is_company','=',true] },
  };

  columns: ColDef[] = [
    {
      headerName: 'Mã số thuế', field: 'vat', colId: 'vat', width: 150, 
      cellRenderer: (p: any) => `<a href="#" target="_blank" class="open_link">${p?.value}</i></a>`,
      cellStyle: { 'color': 'red', 'font-weight': 'bold' }
    },
    { headerName: 'Tên hiển thị', field: 'display_name', colId: 'display_name', width: 400 },
    { headerName: 'E-mail', field: 'email', colId: 'email', width: 230 },
    { headerName: 'Số điện thoại', field: 'mobile', colId: 'mobile', width: 129 },
    { headerName: 'Người liên hệ', field: 'customer_name', colId: 'customer_name', width: 168 },
    { headerName: 'Tên công ty', field: 'company_name', colId: 'company_name', width: 260 }

  ];

  tableOption: TableOption = {
    rowSelection: 'single',
    domLayout: 'autoHeight'
  };

  form: FormGroup;
  searchData: SearchData;
  //rowData: ClsPartner[] = [];
  page: Page<ClsPartner>;
  dataSelected: ClsPartner = undefined;
  tableHeight: string = undefined;
  state: State = {allowNew: true};


  @ViewChild(AgTable, { static: true })
  agTable: AgTable<ClsPartner>;

  @Input() vat: string;
  @Input() email: string;
  @Input() mobile: string;
  @Input({transform: booleanAttribute}) email_chk: boolean;
  @Input({transform: booleanAttribute}) mobile_chk: boolean;

  get searchWhere(): SearchItem[] {
    return Object.values(this.wheres);
  }

  get rowData(): ClsPartner[] {
    return this.page?.data || [];
  }


  // gridApi: GridApi = undefined;

  constructor(
    private logger: LoggerService,
    private config: StorageService,
    private cdRef: ChangeDetectorRef,
    private alert: ToastService,
    private fb: FormBuilder,
    private ref: DynamicDialogRef,
    private modal: ModalService,
    private odSrv: OdTicketService) {
    
  }

  get formValue() {
    return this.form.getRawValue();
  }

  ngOnInit() {
    this.createFormGroup();
    this.initializeToolBar();
  }

  private initializeToolBar() {
    const instance = this.modal.getInstance(this.ref);
    if(instance && instance.data) {
      this.state.showToolbar = true;
      this.searchData = instance.data;
      this.tableHeight = '250px';
      this.tableOption.domLayout='normal';
      this.form.reset((this.searchData));
      this.clickSearch(false);
    }

    else if(Objects.anyNotNull(this.vat, this.email, this.mobile, this.mobile_chk, this.email_chk)){
      this.wheres['mobile'].checked = this.mobile_chk;
      this.wheres['email'].checked = this.email_chk;
      this.searchData = {vat: this.vat, email: this.email, mobile: this.mobile};
      this.form.reset(this.searchData);
      this.clickSearch(false);
    }
  }

  private createFormGroup() {
    this.form = this.fb.group({
      vat: [null],
      display_name: [{value: null, disabled: true}],
      company_name: [null],
      company_id: [null],
      customer_id: [null],
      customer_name: [null],
      street: [null],
      phone: [null],
      email: [null],
      pageSize: [20],
      operator: ['like']
    });

    this.form.valueChanges.subscribe({
      next: data => {
          const { customer_id, company_id } = data;
          this.state.visibleComp = isBlank(company_id);
          this.state.visiblePerson = notBlank(company_id) && isBlank(customer_id);
      }
    })
  }

  ngAfterViewInit(): void {
  }

  clickSearch(showWarning: boolean = true) {
    const newKeys = this.searchWhere.filter(o => o.checked).map(k => k.name);
    const formValue = this.formValue;
    const searchData = this.formToObj(newKeys, formValue);

    if (Objects.isEmpty(searchData)) {
      if(showWarning) this.alert.warning(this.config.i18n.searchInvalid);
      return;
    }

    const filter = newKeys.map(k => this.wheres[k].filter).filter(v => notNull(v));
    const ref = this.alert.loading(this.config.i18n.searchAwait);

    const clsSearch: ClsSearch = ClsSearch.from({
      data: searchData, filter: filter,
      limit: formValue['pageSize'] ?? 20,
      operator: formValue['operator']
    });

    this.state.asyncLoading = true;
    this.dataSelected = undefined;
   

    this.odSrv.searchPartner(clsSearch).subscribe({
      next: (page: Page) => {
        this.page = page;//[...page?.data] ;
        this.state.asyncLoading = false;
        this.alert.close(ref);
        this.cdRef.detectChanges();
        this.alert.success(`Đã tìm được ${page?.total ?? 0} dòng.`);
        
      },
      error: err => {
        this.logger.error('search partner error: ', err);
        this.state.asyncLoading = false;
        this.alert.close(ref);
        this.page = undefined;
        this.cdRef.detectChanges();
      }
    });


  }


  private formToObj(keys: string[], formData?: any) {
    if (this.form.invalid) return undefined;
    else {
      let obj = formData ?? this.form.getRawValue();
      keys = keys || Object.keys(obj);
      keys = keys.filter(k => notBlank(obj[k]));
      return Object.fromEntries(keys.map(k => [k, obj[k].trim()]));
    }
  }



  /** Lấy dữ liệu mặc định */
  resetForm() {
    this.form.reset(this.searchData);
  }

  createContact(type: 'person' | 'company') {

    //-- only support
    if (!['person', 'company'].includes(type)) {
      return;
    }

    const value = this.formValue;

    const phone =  value['phone'] ?? value['mobile'];
    let cls: ClsPartner = ClsPartner.from({
      company_type: type,
      phone: phone,mobile: phone,
      email: value.email
    });

    if (type === 'person') {

      //-- báo lỗi > chưa chọn công ty
      if (isBlank(value['company_id'])) {
        this.alert.warning(`<b>Vui lòng chọn công ty.</b>`);
        this.state.asyncSave = false;
        return;
      }

      cls.update({
        name: value['customer_name'] ?? value['email'],
        parent_id: value['company_id']
        
      });

    }//

    else {
      cls.update({
        name: value['company_name'] ?? value['email'],
        vat: value['vat'],
        street: value['street'],
        parent_id: false
      });
    }

     // create
     let name = type == 'company' ? 'Công ty' : 'Cá nhân';
     this.odSrv.createPartner(cls).subscribe({
      next: data => {
        this.state.asyncSave = false;
        this.agTable.setRows(data);
        this.cdRef.detectChanges();
        this.alert.success(`[${data.id}] Tạo <b>${name}</b> thành công`);
      },
      error: err => {
        this.state.asyncSave = false;
        this.agTable.setRows(...[]);
        this.cdRef.detectChanges();
        this.alert.error(`Xảy ra lỗi tạo <b>${name}</b> > ${err.message} !`);
      }
     });


  }

  closeDialog(): void {
    this.ref.close();
  }

  selectAndClose(): void {
    this.ref.close(this.dataSelected);
  }


  rowClicked(data: ClsPartner) {
    this.form.patchValue(data);
    this.dataSelected = data;
  }

}