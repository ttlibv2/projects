import { ChangeDetectionStrategy, Component, NgZone, OnInit, ViewEncapsulation } from '@angular/core';
import { DynamicDialogRef } from 'primeng/dynamicdialog';
import { TableColumn } from 'ts-ui/ag-table';
import { FormGroup, FormsBuilder } from 'ts-ui/forms';
import { ModalService } from 'ts-ui/modal';
import { ToastService } from 'ts-ui/toast';
import { MvcOption } from '../shared/mvc/mvc.component';
import { ColDef } from '@ag-grid-community/core';

export interface IAgField extends ColDef { }

@Component({
  selector: 'ts-ag-field-view',
  encapsulation: ViewEncapsulation.None,
  changeDetection: ChangeDetectionStrategy.OnPush,
  templateUrl: './field.component.html',
  styleUrl: './field.component.scss'
})
export class AgFieldComponent implements OnInit {
  agColumns: TableColumn[] = [
    { field: 'field' },
    { field: 'headerName' },
    { field: 'width' },
    { field: 'hide' },
    { field: 'position' },
    { field: 'table_id' },
    { field: 'cellClass' },
    { field: 'flex' },
    { field: 'pivot' },
    { field: 'pinned' },
    { field: 'sortable' },
    { field: 'checkbox' },
    { field: 'editable' },
    { field: 'cellEditor' },
    { field: 'enablePivot' },
    { field: 'cellRenderer' },
    { field: 'wrapText' },
    { field: 'resizable' }
  ];

  form: FormGroup;

  mvcOptions: MvcOption<IAgField> = {

    formFields: [
      { fieldId: 'field', label: 'aaaa', type: 'text' },
      { fieldId: 'headerName', label: 'aaaaa', type: 'text' },
      { fieldId: 'width' , label: 'aaa', type: 'text'},
      { fieldId: 'hide' , label: '', type: 'text'},
      { fieldId: 'position' , label: '', type: 'text'},
      { fieldId: 'table_id' , label: '', type: 'text'},
      { fieldId: 'cellClass' , label: '', type: 'text'},
      { fieldId: 'flex' , label: '', type: 'text'},
      { fieldId: 'pivot' , label: '', type: 'text'},
      { fieldId: 'pinned' , label: '', type: 'text'},
      { fieldId: 'sortable' , label: '', type: 'text'},
      { fieldId: 'checkbox' , label: '', type: 'text'},
      { fieldId: 'editable' , label: '', type: 'text'},
      { fieldId: 'cellEditor' , label: '', type: 'text'},
      { fieldId: 'enablePivot' , label: '', type: 'text'},
      { fieldId: 'cellRenderer' , label: '', type: 'text'},
      { fieldId: 'wrapText' , label: '', type: 'text'},
      { fieldId: 'resizable' , label: '', type: 'text'}
    ],



  
  };

  constructor(private fb: FormsBuilder,
    private modal: ModalService,
    private toast: ToastService,
    private ngZone: NgZone,
    private dialogRef: DynamicDialogRef) {
    this.maximized();
  }

  ngOnInit(): void {
  }

  private maximized(): void {
    const instance = this.modal.getInstance(this.dialogRef);
    if (instance) instance.maximized = true;
  }
}
