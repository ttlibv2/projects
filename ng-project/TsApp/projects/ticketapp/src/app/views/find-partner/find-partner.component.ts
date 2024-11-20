import {
  ChangeDetectionStrategy,
  ChangeDetectorRef,
  Component,
  Input,
  OnDestroy,
  OnInit,
  ViewChild,
  ViewEncapsulation
} from '@angular/core';
import { DynamicDialogRef } from "primeng/dynamicdialog";
import { Objects, Page } from 'ts-ui/helper';
import { ToastService } from 'ts-ui/toast';
import { OdPartnerService } from '../../services/od-partner.service';
import { ClsPartner, ClsSearch } from '../../models/od-cls';
import { LoggerService } from 'ts-ui/logger';
import { AgTable, TableColumn, TableOption } from 'ts-ui/ag-table';
import { StorageService } from "../../services/storage.service";
import { ModalService } from "ts-ui/modal";
import { FormGroup, FormsBuilder } from 'ts-ui/forms';
import { delay, Observable, Observer, of, tap } from 'rxjs';
import { DEFAULT_DATA, InputOption, SearchData, SearchItem, SearchOpt, State, asBool, searchOption } from './partner.interface';
import { MenuItem } from 'primeng/api';
import { SearchContact } from '../../models/cls-partner';


const { isBlank, notBlank, notNull, isNull, isEmpty, isTrue, anyBlank } = Objects;


export function openDialog(modal: ModalService, data: SearchData, options?: InputOption): DynamicDialogRef<FindPartnerComponent> {
  options = Objects.mergeDeep({ maximizable: true, header: "Tìm kiếm khách hàng" }, options);
  return modal.open(FindPartnerComponent, { ...options, data: { data, options } });
}

@Component({
  selector: 'ts-find-partner',
  templateUrl: './find-partner.component.html',
  styleUrl: './find-partner.component.scss',
  encapsulation: ViewEncapsulation.None,
})
export class FindPartnerComponent implements OnInit, OnDestroy {
  @Input({ transform: (v: any) => asBool(v, true) }) set isvat(b: boolean) { this.data.options.isvat = b; }
  @Input({ transform: (v: any) => asBool(v, true) }) set isemail(b: boolean) { this.data.options.isemail = b; }
  @Input({ transform: (v: any) => asBool(v, true) }) set ismobile(b: boolean) { this.data.options.ismobile = b; }
  @Input({ transform: (v: any) => asBool(v, true) }) set isperson(b: boolean) { this.data.options.isperson = b; }
  @Input() set iscompany(b: boolean) { this.data.options.iscompany = b; }
  @Input() set vat(s: string) { this.data.vat = s; }
  @Input() set email(s: string) { this.data.email = s; }
  @Input() set mobile(s: string) { this.data.mobile = s; }

  get tableHeight(): string {
    return this.options?.tableHeight || this.tableOption?.height;
  }

  form: FormGroup;
  options: InputOption = { timeDelay: 1000, showToatResult: true, max_width: '60%' };
  data: SearchData = { ...DEFAULT_DATA };
  dataSelect: ClsPartner;
  page: Page<ClsPartner> = new Page();
  state: State = {};

  tableOption: TableOption = {
    height: '350px',
    onRowDoubleClicked: evt => this.selectAndClose(evt.data)
  };

  columns: TableColumn[] = [
    {
      headerName: 'Mã số thuế', field: 'vat', colId: 'vat', width: 150,
      cellRenderer: (p: any) => `<a href="${this.link(p.data.id)}" target="_blank" class="open_link">${p?.value}</i></a>`,
      cellStyle: { 'color': 'red', 'font-weight': 'bold' }
    },
    { headerName: 'Tên hiển thị', field: 'display_name', colId: 'display_name', width: 400, wrapText: true },
    { headerName: 'E-mail', field: 'email', colId: 'email', width: 230 },
    { headerName: 'Số di động', field: 'mobile', colId: 'mobile', width: 129 },
    { headerName: 'Người liên hệ', field: 'customer_name', colId: 'customer_name', width: 168 },
  ];

  searchMenu: MenuItem[] = [
    { label: 'Tìm theo công ty (MST)', icon: 'pi pi-code', command: evt => this.clickSearchMST(true) },
    { label: 'Tìm theo cá nhân (MST + Email)', icon: 'pi pi-code', command: evt => this.clickSearchMST(true) },
  ];

  newMenuItems: MenuItem[] = [
    { label: 'Tạo công ty', icon: 'pi pi-users', command: _ => this.clickNewCompany() },
    { label: 'Tạo cá nhân', icon: 'pi pi-user', command: _ => this.clickNewPerson() },
    { label: 'Tạo theo e-mail', icon: 'pi pi-envelope', command: _ => this.clickNewPersonEmail() },
  ];

  private newMenu = {
    company: this.newMenuItems[0],
    person: this.newMenuItems[1],
    email: this.newMenuItems[2],
  }

  get hasCompanyUID(): boolean {
    return notBlank(this.form?.get_value('company_id'));
  }

  get hasPersonUID(): boolean {
    return notBlank(this.form?.get_value('customer_id'));
  }

  get visibleAllowNew(): boolean {
    return Objects.anyTrue(this.state.visibleComp,
      this.state.visiblePerson, this.state.visibleNewEmail);
  }

  get formOption(): SearchItem[] {
    return Object.values(searchOption);
  }

  get rowData(): ClsPartner[] {
    return this.page?.data || [];
  }


  @ViewChild('agTable', { static: true })
  private agTable: AgTable;

  constructor(
    private logger: LoggerService,
    private config: StorageService,
    private cdRef: ChangeDetectorRef,
    private alert: ToastService,
    private fb: FormsBuilder,
    private dialogRef: DynamicDialogRef,
    private toast: ToastService,
    private modal: ModalService,
    private odSrv: OdPartnerService) {
  }

  ngOnInit(): void {
    const instance = this.modal.getInstance(this.dialogRef);

    this.createFormGroup();
    this.state.hasDialog = notNull(instance);
    if (instance && instance.data) {
      const { data, options } = instance.data;
      this.options = Objects.mergeDeep(this.options, options);
      this.data = Objects.mergeDeep(this.data, data);
      this.form.reset(this.data);
      this.clickSearch({ showWarning: false });
    }
    else {
      this.form.reset(this.data);
      this.clickSearch({ showWarning: false });
    }


  }

  clickSearchMST(showWarning: boolean = true): void {
    //this.form.path_value('options.isemail', false);
    //this.clickSearch({ showWarning, trialMST: true, hint: '- Tìm theo MST' });

  }


  /**
   * Search customer
   * @param opt
   */
  clickSearch(opt?: SearchOpt) {
    this.asyncSearch(opt).subscribe({
      error: ({ msg }) => {
        this.logger.warn(`clickSearch: `, msg);
        if (notBlank(msg)) this.alert.warning(msg);
      },
      next: ({ option, page }) => {
        if (isTrue(this.options.showToatResult)) {
          let msg = `Tìm kiếm khách hàng: <br>${option.hint ?? '- Tìm tất cà'}<br>`;
          this.alert.success(`${msg}=> Tìm được <span class="text-danger">${page?.total ?? 0}</span> dòng.`);
        }

        if (isTrue(this.options.autoSelect)) {
          const sizeRow = page.data.length;

          if (sizeRow === 1) {
            const obser = of(1).pipe(delay(this.options.timeDelay));
            obser.subscribe(_ => this.selectAndClose(page.data[0]));
          }

          // 0_row  -> search without vat
          else if (sizeRow === 0 && isTrue(option.trialMST)) {
            this.clickSearchMST(false);
          }
        }
      }
    });
  }

  clickResetForm(): void {
    this.form.reset(this.data);
  }

  clickNewPerson(): void {
    this.createContact('person');
  }

  clickNewCompany(): void {
    this.createContact('company');
  }

  clickNewPersonEmail(): void {
    const { vat, email, company_id, mobile } = this.form.getRawValue();

    const clickNewFunc = () => {
      this.clickRow(this.rowData[0], 'company');
      this.createContact('person', {
        email, mobile, customer_name: email,
        company_id: this.form.get_value('company_id')
      });
    };

    // validate email + vat
    if (anyBlank(vat, email)) {

      let obj = [
        isBlank(vat) ? { name: 'vat', suffix: '<b>[Mã số thuế]</b>' } : null,
        isBlank(email) ? { name: 'email', suffix: '<b>[Email]</b>' } : null
      ]
        .filter(v => Objects.notNull(v));

      this.form.set_error('email', {}, { emitEvent: true });


      let msg = `Vui lòng nhập thông tin`;
      obj.forEach(i => this.form.set_error(i.name, { empty: `${msg} ${i.suffix}` }));
      this.alert.warning(`${msg} ${obj.map(i => i.suffix).join(' và ')}`);

      return;
    }

    // click firstRow if not click
    if (notBlank(company_id) || (isBlank(company_id) && this.rowData.length > 0)) {
      clickNewFunc();
    }

    // click search without mst
    else if (isBlank(company_id) && this.rowData.length === 0) {
      this.form.reset_value('options', { isvat: true, iscompany: true });
      this.asyncSearch({ showWarning: false }).subscribe({
        next: ({ page }) => {

          if (page.isEmpty()) {
            let msg = `Không tìm thấy dữ liệu mã số thuế <b>${vat}</b> trên hệ thống.`;
            this.toast.warning(msg);
            return;
          }

          // click firstRow to set company
          clickNewFunc();
        }
      })
    }



  }

  closeDialog(): void {
    this.dialogRef.close();
  }

  selectAndClose(data?: ClsPartner): void {
    this.dialogRef.close(data || this.dataSelect);
  }


  clickRow(cls: ClsPartner, upType?: 'person' | 'company') {
    const { operator, options, pageSize } = <SearchData>this.form.getRawValue();
    const data: Partial<ClsPartner> = isNull(upType) ? cls

      // upType === 'person'
      : upType === 'company' ? {
        vat: cls.vat, street: cls.street,
        company_id: cls.company_id,
        company_name: cls.company_name,
      }

        : // upType === 'person'
        {
          email: cls.email,
          mobile: cls.mobile, phone: cls.phone,
          customer_id: cls.customer_id,
          customer_name: cls.customer_name
        }

    this.form.reset({ ...data, operator, options, pageSize }, { onlySelf: false, emitEvent: true });
    this.dataSelect = cls;
  }

  clearUID(field: 'customer_id' | 'company_id'): void {
    this.form.get(field).reset(undefined);
  }


  ngOnDestroy(): void {
    this.dialogRef?.destroy();
  }


  private createFormGroup() {
    this.form = this.fb.group({
      vat: [null],
      mobile: [null],
      email: [null],
      street: [null],
      company_name: [null],
      customer_name: [null],
      company_id: [{ value: null, disabled: true }],
      customer_id: [{ value: null, disabled: true }],
      display_name: [{ value: null, disabled: true }],
      pageSize: [20],
      operator: [null],

      // form_option
      options: this.fb.group({
        isvat: [true],
        isemail: [true],
        ismobile: [true],
        isperson: [true],
        iscompany: [false]
      })
    });

    this.form.formChange(data => {
      const { customer_id, company_id, vat, email } = data;
      this.state.visibleComp = isBlank(company_id);
      this.state.visiblePerson = notBlank(company_id) && isBlank(customer_id);
      this.state.visibleNewEmail = true;//!Objects.anyNull(company_id, vat, email);

      this.newMenu['company'].visible = this.state.visibleComp;
      this.newMenu['person'].visible = this.state.visiblePerson;
      this.newMenu['email'].visible = this.state.visibleNewEmail;

    });

  }

  private formToObject(json: Partial<SearchData>, keys: string[]): any {
    const newKeys = keys.map(k => searchOption[k].field).filter(n => notBlank(json[n]));
    return Objects.arrayToJson(newKeys, k => [k, json[k]]);
  }

  private link(id: number): string {
    const link = this.config.loginUser?.ts_links['od_partner'];
    return link?.replace(`{id}`, `${id}`);
  }


  private createContact(type: 'person' | 'company', data?: Partial<ClsPartner>) {
    console.log(`click createContact(${type})`)

    //-- only support
    if (!['person', 'company'].includes(type)) {
      return;
    }


    const value: SearchData = data || this.form.getRawValue();
    const phone = value.phone ?? value.mobile;

    let hasNext: boolean = true;
    let cls: ClsPartner = ClsPartner.from({
      company_type: type,
      phone: phone,
      mobile: phone,
      email: value.email
    });

    if (type === 'person') {

      //-- báo lỗi > chưa chọn công ty
      if (isNull(value?.company_id)) {
        this.alert.warning(`<b>Vui lòng chọn công ty.</b>`);
        this.state.asyncSave = false;
        hasNext = false;
        return;
      }

      cls.update({
        name: value?.customer_name ?? value?.email,
        parent_id: value?.company_id

      });

    }//

    else if (type === 'company') {
      const hasOk = Objects.anyNotBlank(value.company_name, value.email);
      if (hasOk) cls.update({
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
    if (hasNext === true) {
      let name = type == 'company' ? 'Công ty' : 'Cá nhân';
      this.state.asyncSave = true;
      this.odSrv.createPartner(cls).subscribe({
        next: (data: ClsPartner) => {
          this.state.asyncSave = false;
          this.alert.success(`[${data.id}] Tạo <b>${name}</b> thành công`);
          this.page.addFirst(data);
          //this.agTable.addRow(data);
          this.agTable.setRows([data]);
          this.clickRow(data);
        },
        error: err => {
          this.state.asyncSave = false;
          this.alert.error(`Xảy ra lỗi tạo <b>${name}</b> > ${err.message} !`);
        }
      });
    }

  }




  private asyncSearch(opt?: SearchOpt): Observable<{ option: SearchOpt, page: Page<ClsPartner> }> {
    return new Observable((observer: Observer<any>) => {
      const json = <SearchData>this.form.getRawValue();
      const keyCheck = Object.keys(json.options).filter(k => isTrue(json.options[k]));
      const option = Object.assign({ showWarning: true, trialMST: false }, opt);

      const searchData: ClsPartner = this.formToObject(json, keyCheck);
      if (isEmpty(searchData)) {
        if (option.showWarning) this.alert.warning(this.config.i18n.searchInvalid);
        observer.error({ option, msg: '' });
      }//
      else {
        // search
        const filter = keyCheck.map(k => searchOption[k]?.filter).filter(o => notNull(o));
        const clsSearch: ClsSearch = ClsSearch.from({
          data: searchData, filter: filter,
          limit: json.pageSize ?? 20,
          operator: json.operator
        });

        option.hint = this.buildHint(searchData, filter, json.operator);
        this.state.asyncLoading = true;
        this.dataSelect = undefined;

        // clear focus row table
        this.agTable.tableApi?.clearFocusedCell();

        const ref = this.alert.loading(this.config.i18n.searchAwait);

        let resultObser = this.odSrv.searchPartner(clsSearch);
        let funcAfter = this.options.afterSearch;
        if (notNull(funcAfter)) {
          resultObser = resultObser.pipe(tap(page => funcAfter(this.dialogRef, page)));
        }

        resultObser.subscribe({
          next: (page: Page) => {
            this.page = page;//[...page?.data] ;
            this.state.asyncLoading = false;
            this.alert.close(ref);

            observer.next({ page, option })

          },
          error: err => {
            this.logger.error('search partner error: ', err);
            this.state.asyncLoading = false;
            this.alert.close(ref);
            this.page = undefined;
            observer.error({ option, msg: ` Xảy ra lỗi tìm khách hàng => ${err}` })
          },

          complete: () => observer.complete()
        });
      }

    });

  }


  buildHint(searchData: ClsPartner, filter: any[][], operator: string) {
    // build help
    let hint = [], opt2 = [], keys;
    if (notBlank(searchData.vat)) hint.push('Mã số thuế');
    if (notBlank(searchData.email)) hint.push('E-mail');
    if (notBlank(searchData.mobile)) hint.push('Số điện thoại');

    const lines = [];
    lines.push(`- Tìm theo: ${hint.join(' | ')}`);
    lines.push(`- Tùy chỉnh: ${operator === 'like' ? 'Tìm gần đúng' : 'Tìm chính xác'}`);

    const la = filter.map(it => {
      let n = it[0], l = it[2];
      if (n === 'email') return 'Email <> NULL';
      else if (n === 'vat') return 'MST <> NULL';
      else if (n === 'mobile') return 'SDT <> NULL';
      else if (n === 'is_company') {
        if (l === true) return 'Cá nhân <> NULL';
        else return 'Công ty <> NULL';
      }
      else return null;
    }).filter(it => notNull(it));


    lines.push(`- Tìm khác: ${la.join(' | ')}`);
    return lines.join('<br>');
  }


}