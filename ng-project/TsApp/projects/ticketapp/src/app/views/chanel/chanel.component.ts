import { Component } from '@angular/core';
import { Validators } from '@angular/forms';
import { FormsBuilder } from 'ts-ui/forms';
import { Field, MvcOption } from '../shared/mvc/mvc.component';
import { Objects } from 'ts-ui/helper';
import { ChanelService } from '../../services/chanel.service';

@Component({
  selector: 'ts-chanel',
  templateUrl: './chanel.component.html',
  styleUrl: './chanel.component.scss'
})
export class ChanelComponent {
  constructor(private fb: FormsBuilder,
    private chanelSrv: ChanelService) { }

  formGroup = this.fb.group({
    id: [null],
    code: [null, Validators.required],
    value: [null, Validators.required],
    extend_code: [null, Validators.required]
  });

  mvcOption: MvcOption<any> = {
    autoLoadData: true,
    formFields: [
      { fieldId: 'code', label: 'Mã Kênh', type: 'input', class: 'lg:col-4' },
      { fieldId: 'extend_code', label: 'Mã Kênh 2', type: 'input', class: 'lg:col-4' },
      { fieldId: 'value', label: 'Tên kênh', type: 'input', class: 'lg:col-4' },
    ],
    tbColumns: [
      { field: 'id', headerName: 'UID', width: 100, headerCheckboxSelection: true, checkboxSelection: true },
      { field: 'code', headerName: 'Mã Kênh', width: 100 },
      { field: 'extend_code', headerName: 'Mã Kênh 2', width: 100 },
      { field: 'value', headerName: 'Tên kênh', width: 300 },
    ],
    resetDataFunc: () => { },
    loadDataFunc: () => this.chanelSrv.findAll(),
    editDataFunc: data => this.chanelSrv.updateById(data.id, data),
    deleteDataFunc: data => this.chanelSrv.deleteById(data.id),
    newDataFunc: data => {
      const names: string = <any>data.soft_names;
      if (Objects.notBlank(names)) data.soft_names = names.split(',');
      return this.chanelSrv.createNew(data)
    },

  }
}
