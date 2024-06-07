import {Component, OnInit, ViewEncapsulation} from '@angular/core';
import {FormBuilder, FormGroup} from "@angular/forms";
import {DialogService, DynamicDialogComponent, DynamicDialogRef} from "primeng/dynamicdialog";
import {Objects} from "../../utils/objects";
import {ColDef, GridOptions} from "ag-grid-community";

const {isBlank, notBlank} = Objects;

@Component({
  selector: 'ts-find-partner',
  templateUrl: './find-partner.component.html',
  styleUrl: './find-partner.component.scss',
  encapsulation: ViewEncapsulation.None
})
export class FindPartnerComponent implements OnInit {
  instance: DynamicDialogComponent | undefined;

  data: any[] = [];
  formGroup: FormGroup;

  searchWhere: any[] = [
    {label: 'MST', value: 'vat', checked: true},
    {label: 'E-mail', value: 'email', checked: true},
    {label: 'Cá nhân', value: 'is_person', checked: true}
  ];

  columns: ColDef[] = [
    {headerName: 'Mã số thuế', field: 'tax_code', headerCheckboxSelection: true},
    {headerName: 'Tên hiển thị', field: 'display_name'},
    {headerName: 'Tên công ty', field: 'company_name'},
    {headerName: 'Người liên hệ', field: 'person_name'},
    {headerName: 'Số đện thoại', field: 'phone'},
    {headerName: 'E-mail', field: 'email'},
  ];

  agOptions: GridOptions<any> = {
    rowSelection: 'single'
  };


  constructor(
    private fb: FormBuilder,
    private ref: DynamicDialogRef,
    private dialogSrv: DialogService) {
  }

  get isVisibleComp(): boolean {
    const {company_id} = this.getRaw();
    return isBlank(company_id);
  }

  get isVisiblePerson(): boolean {
    const {person_id, company_id} = this.getRaw();
    return notBlank(company_id) && isBlank(person_id);
  }

  ngOnInit() {

    if (this.instance && this.instance.data) {
      // this.ticket = this.instance.data['ticket'];
    }

    this.formGroup = this.fb.group({
      tax_code: [null],
      company_name: [null],
      person_name: [null],
      company_id: [null],
      person_id: [null],
      address: [null],
      phone: [null],
      email: [null],
      search_type: ['eq']
    });


  }

  onSearch() {
  }

  onResetForm() {
  }

  onCreateContact(person: string) {

  }

  onClickUp(checked: boolean) {

  }

  closeDialog(): void {
    this.ref.close();
  }


  getRaw() {
    return this.formGroup.getRawValue();
  }


}
