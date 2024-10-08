import { Component } from '@angular/core';
import { Validators } from '@angular/forms';
import { FormsBuilder } from 'ts-ui/forms';
import { Field, MvcOption } from '../shared/mvc/mvc.component';
import { Objects } from 'ts-ui/helper';
import { TableColumn } from 'ts-ui/ag-table';
import { QuestionService } from '../../services/question.service';
import { StorageService } from '../../services/storage.service';
import { Question } from '../../models/question';
import { GroupHelpService } from '../../services/group-help.service';

@Component({
  selector: 'ts-group-help',
  templateUrl: './group-help.component.html',
  styleUrl: './group-help.component.scss'
})
export class GroupHelpComponent {
  constructor(private fb: FormsBuilder,
    private config: StorageService,
    private ghelpSrv: GroupHelpService) { }

  formGroup = this.fb.group({
    id: [null],
    code: [null, Validators.required],
    title: [null, Validators.required],
    value: [null, Validators.required],
  });


  mvcOption: MvcOption<any> = {
    autoLoadData: true,
    rowNameId: 'id',

    formFields: [
      { fieldId: 'code', label: 'Mã nhóm', type: 'input', class: 'sm:col-4', tb_width: 120 },
      { fieldId: 'title', label: 'Tên nhóm', type: 'input', class: 'lg:col-4', tb_width: 130 },
      { fieldId: 'value', label: 'Ghi chú', type: 'input', class: 'lg:col-8', tb_width: 500 },
    ],

    resetDataFunc: () => { },
    loadDataFunc: () => this.ghelpSrv.findAll(),
    editDataFunc: data => this.ghelpSrv.updateById(data.id, data),
    deleteDataFunc: data => this.ghelpSrv.deleteById(data.id),
    newDataFunc: (data: Question) => {
      data.user_id = this.config.loginUser.account_id;
      return this.ghelpSrv.createNew(data)
    },

  }
}
