import { Component } from '@angular/core';
import { Validators } from '@angular/forms';
import { FormsBuilder } from 'ts-ui/forms';
import { Field, MvcOption } from '../shared/mvc/mvc.component';
import { Objects } from 'ts-ui/helper';
import { TableColumn } from 'ts-ui/ag-table';
import { QuestionService } from '../../services/question.service';
import { StorageService } from '../../services/storage.service';
import { Question } from '../../models/question';


@Component({
  selector: 'ts-question',
  templateUrl: './question.component.html',
  styleUrl: './question.component.scss'
})
export class QuestionComponent {
  constructor(private fb: FormsBuilder,
    private config: StorageService,
    private questionSrv: QuestionService) { }

  formGroup = this.fb.group({
    id: [null],
    title: [null, Validators.required],
    reply: [null, Validators.required],
    soft_type: [null, Validators.required],
    shared: [false, Validators.required],
  });


  mvcOption: MvcOption<any> = {
    visibleExport: true,
    visibleImport: true,
    autoLoadData: true,
    rowNameId: 'id',
    formFields: [
      { fieldId: 'title', label: 'Tiêu đề', type: 'input', class: 'sm:col-4' },
      { fieldId: 'soft_type', label: 'Lĩnh vực', type: 'input', class: 'lg:col-4' },
      { fieldId: 'reply', label: 'Nội dung', type: 'area', class: 'lg:col-8' },
      { fieldId: 'shared', label: 'Chia sẻ', type: 'checked', class: 'lg:col-12' }
    ],
    xsl_options: {
      fileName: 'question.xlsx'
    },

    resetDataFunc: () => { },
    loadDataFunc: () => this.questionSrv.findByUserLogin({ size: 500 }),
    editDataFunc: data => this.questionSrv.updateById(data.id, data),
    deleteDataFunc: data => this.questionSrv.deleteById(data.id),
    newDataFunc: (data: Question) => {
      data.user_id = this.config.loginUser.account_id;
      return this.questionSrv.createNew(data)
    },

  }
}
