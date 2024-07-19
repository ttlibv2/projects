import { AfterViewInit, ChangeDetectionStrategy, ChangeDetectorRef, Component, OnInit, ViewChild, ViewEncapsulation} from '@angular/core';
import { FormBuilder, FormGroup } from "@angular/forms";
import { DialogService, DynamicDialogComponent, DynamicDialogRef } from "primeng/dynamicdialog";
import { Objects } from 'ts-helper';
import { ColDef } from "ag-grid-community";
import { ToastService } from '../../services/toast.service';
import { OdTicketService } from '../../services/od.service';
import { ClsPartner, ClsSearch } from '../../models/od-cls';
import { LoggerService } from 'ts-logger';
import { Page } from '../../models/common';
import { AgTableComponent, TableOption } from 'ts-ui/ag-table';
import {StorageService} from "../../services/storage.service";

const { isBlank, notBlank, notNull } = Objects;

export interface SearchItem {
  label: string;
  name: string;
  checked: boolean;

}

export interface SearchData {
  vat?: string;
  email?: string;
  mobile?: string;
  company_name?: string;
  person_name?: string;
  company_id?: string;
  person_id?: string;
  address?: string;
}


@Component({
  selector: 'ts-find-partner',
  templateUrl: './find-partner.component.html',
  styleUrl: './find-partner.component.scss',
  encapsulation: ViewEncapsulation.None,
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class FindPartnerComponent implements OnInit, AfterViewInit {

  searchWhere: SearchItem[] = [
    { label: 'MST', name: 'vat', checked: true },
    { label: 'E-mail', name: 'email', checked: true },
    { label: 'Cá nhân', name: 'is_person', checked: true }
  ];

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
  };


  instance: DynamicDialogComponent | undefined;
  formGroup: FormGroup;
  searchData: SearchData;
  asyncLoading: boolean = false;
  asyncSave: boolean = false;
  rowData: ClsPartner[] = [];
  dataSelected: ClsPartner = undefined;
  tableHeight: string = '250px';

  visiblePerson: boolean = false;
  visibleComp: boolean = false;
  allowNew: boolean = false;
  showToolbar: boolean = false;

  @ViewChild(AgTableComponent, { static: true }) 
  agTable: AgTableComponent<ClsPartner>;

 // gridApi: GridApi = undefined;

  constructor(
    private logger: LoggerService,
    private config: StorageService,
    private cdRef: ChangeDetectorRef,
    private alert: ToastService,
    private fb: FormBuilder,
    private ref: DynamicDialogRef,
    private dialogSrv: DialogService,
    private odSrv: OdTicketService) {
    
  }

  get formValue() {
    return this.formGroup.getRawValue();
  }

  ngOnInit() {
    this.createFormGroup();
    this.initializeToolBar();
  }

  private initializeToolBar() {
    const refView = this.dialogSrv.dialogComponentRefMap.get(this.ref);
    this.instance = refView && refView.instance;
    this.showToolbar = notNull(this.instance);

    if( this.showToolbar === false) {
      this.tableHeight = undefined;
      this.tableOption.domLayout = 'autoHeight';
    }

    if (this.instance && this.instance.data) {
      this.searchData = this.instance.data;
      this.formGroup.patchValue(this.searchData);
      this.clickSearch(false);
    }
  }

  private createFormGroup() {
    this.formGroup = this.fb.group({
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

    this.formGroup.valueChanges.subscribe({
      next: data => {
          const { customer_id, company_id } = data;
          this.visibleComp = isBlank(company_id);
          this.visiblePerson = notBlank(company_id) && isBlank(customer_id);
      }
    })
  }

  ngAfterViewInit(): void {
  }

  clickSearch(showWarning: boolean = true) {
    const newKeys = this.searchWhere.filter(o => o.checked).map(k => k.name);
    const formValue = this.formValue, searchData = this.formToObj(newKeys, formValue);

    if (Objects.isEmpty(searchData)) {
      if(showWarning) this.alert.warning({ summary: this.config.i18n.searchInvalid });
      return;
    }

    
    if (newKeys.includes('is_person')) {
      searchData['is_company'] = false;
    }


    const ref = this.alert.loading({ summary: this.config.i18n.searchAwait });

    const clsSearch: ClsSearch = ClsSearch.from({
      data: searchData,
      limit: searchData['pageSize'] ?? 20,
      operator: formValue['operator']
    });

    this.asyncLoading = true;
    this.dataSelected = undefined;
   

    this.odSrv.searchPartner(clsSearch).subscribe({
      next: (page: Page) => {
        this.rowData = [...page?.data] ;
        this.asyncLoading = false;
        this.alert.closeToast(ref);
        this.cdRef.detectChanges();
        this.alert.success({ summary: `Đã tìm được ${page?.total ?? 0} dòng.` });
        
      },
      error: err => {
        this.logger.error('search partner error: ', err);
        this.asyncLoading = false;
        this.alert.closeToast(ref);
        this.rowData = [];
        this.cdRef.detectChanges();
      }
    });


  }


  private formToObj(keys: string[], formData?: any) {
    if (this.formGroup.invalid) return undefined;
    else {
      let obj = formData ?? this.formGroup.getRawValue();
      const newKeys = (keys ?? Object.keys(obj)).filter(k => notBlank(obj[k]));
      return Object.fromEntries(newKeys.map(k => [k, obj[k].trim()]));
    }
  }



  /** Lấy dữ liệu mặc định */
  resetForm() {
    this.formGroup.patchValue(this.searchData);
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
        this.alert.warning({summary: `<b>Vui lòng chọn công ty.</b>`});
        this.asyncSave = false;
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
        this.asyncSave = false;
        this.agTable.setRows(data);
        this.cdRef.detectChanges();
        this.alert.success({summary: `[${data.id}] Tạo <b>${name}</b> thành công`});
      },
      error: err => {
        this.asyncSave = false;
        this.agTable.setRows(...[]);
        this.cdRef.detectChanges();
        this.alert.error({summary: `Xảy ra lỗi tạo <b>${name}</b> > ${err.message} !`});
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
    this.formGroup.patchValue(data);
    this.dataSelected = data;
  }

}