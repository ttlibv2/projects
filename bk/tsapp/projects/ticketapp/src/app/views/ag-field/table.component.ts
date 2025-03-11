import { Component, ViewEncapsulation } from '@angular/core';
import { TableColumn } from 'ts-ui/agtable';
import { FormGroup, FormsBuilder } from 'ts-ui/forms';
import { ModalService } from 'ts-ui/modal';
import { ToastService } from 'ts-ui/toast';
import { AgFieldComponent } from './field.component';

@Component({
  selector: 'ts-ag-table-view',
  encapsulation: ViewEncapsulation.None,
  templateUrl: './table.component.html',
  styleUrl: './table.component.scss'
})
export class AgTbComponent {
  agColumns: TableColumn[] = [
    { field: 'table_id' },
    { field: 'code' },
    { field: 'title' },
    { field: 'svg_icon' },
    { field: 'position' },
    { field: 'summary' },
    { field: 'config' }
  ];

  form: FormGroup;

  constructor(private fb: FormsBuilder,
    private modal: ModalService,
    private toast: ToastService) { }


  viewDsField(): void { 
    let ref = this.modal.open(AgFieldComponent, {
      maximizable: true,
      styleClass: 'ag-table-field-dialog'
    });


  }
}
