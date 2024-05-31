import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {DialogService, DynamicDialogComponent, DynamicDialogRef} from "primeng/dynamicdialog";
import {AgColumn} from "../../models/ag-table";
import {Objects} from "../../utils/objects";

@Component({
  selector: 'ts-find-partner',
  templateUrl: './find-partner.component.html',
  styleUrl: './find-partner.component.scss'
})
export class FindPartnerComponent implements OnInit {
  instance: DynamicDialogComponent | undefined;
  data: any[] = [];
  formGroup: FormGroup;

  searchWhere: any[] = [
    { label: 'MST', value: 'vat', checked: true },
    { label: 'E-mail', value: 'email', checked: true },
    { label: 'Cá nhân', value: 'is_person', checked: true }
  ];

  columns: AgColumn[] = [
    {header_name: 'Mã số thuế'},
    {header_name: 'Tên hiển thị'},
    {header_name: 'Số điện thoại'},
    {header_name: 'Người liên hệ'},
    {header_name: 'Email'},
    {header_name: 'Tên công ty'},
  ];

  constructor(
   private ref: DynamicDialogRef,
   private dialogSrv: DialogService,
    private fb: FormBuilder) {}

  ngOnInit() {

    if(this.instance && this.instance.data) {
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

  onSearch() {}

  onResetForm() {}

  onCreateContact(person: string) {

  }

  onClickUp(checked: boolean) {

  }


  closeDialog(): void {
    this.ref.close();
  }

  isVisibleComp(): boolean {
    const company_id: number = this.getRaw().company_id;
    return Objects.isNull(company_id);
  }

  isVisiblePerson() {
    const person_id: number = this.getRaw().person_id;
    return Objects.isNull(person_id);
  }

  getRaw() {
    return this.formGroup.getRawValue();
  }


}
