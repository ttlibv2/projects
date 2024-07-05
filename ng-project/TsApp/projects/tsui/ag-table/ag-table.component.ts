import { Component, EventEmitter, Input, Output, ViewChild, ViewEncapsulation } from '@angular/core';
import { AgGridAngular } from 'ag-grid-angular';
import { GridApi, GridReadyEvent, IRowNode } from 'ag-grid-community';
import { defaultOption, PrivateField, TableColumn, TableOption, TableReadyEvent, TableRowClick } from './ag-table.common';
import * as helper from 'ts-helper';
import { _Util } from 'ag-grid-enterprise';

   
@Component({ 
  selector: 'ts-ag-table',
  templateUrl: './ag-table.component.html',
  styles: ` :host { display: block; }  `,
  encapsulation: ViewEncapsulation.None
})
export class AgTableComponent<E=any>  {

  
  @Output()
  tableReady = new EventEmitter<TableReadyEvent>();

  @Output()
  rowClicked = new EventEmitter<TableRowClick<E>>();

  @Input() columns: TableColumn[] = [];
  @Input() rows: any[] = [];
  @Input() tableHeight: string = '250px';

  @Input()
  set option(option: TableOption) {
    this.field.option = helper.Objects.mergeDeep({...defaultOption}, option);
  }

  @Input()
  set themeClass(theme: string) {

    //if (notBlank(this.field.themeClass)) {
    //  this.render.removeClass(this.view, this.field.themeClass);
   // }

    //this.field.themeClass = themes ?? 'ag-themes-quartz';
   // this.render.addClass(this.view, themes);

  }

  get themeClass(): string {
    return this.field.themeClass;
  }

  get option(): TableOption {
    return this.field.option;
  }

  get tableApi(): GridApi<E> {
    return this.view.api;
  }

  //==============


  @ViewChild(AgGridAngular, { static: true })
  view: AgGridAngular;
  field: PrivateField = {};

  /** Ready ag-grid */
  gridReadyAg(evt: GridReadyEvent) {
    this.tableReady.emit({ view: this.view, ...evt });
  }

  editColumn(colId: string, consumer: helper.Consumer<TableColumn>): void {
    const columnIndex = this.tableApi.getColumnDefs().findIndex((col: any) => col.colId === colId);
    if(columnIndex !== -1 && helper.Objects.notNull(consumer))  {
      const columns = [...this.tableApi.getColumnDefs()];
      consumer(columns[columnIndex]);
      this.tableApi.updateGridOptions({columnDefs: columns});
    }
  }

  setRows(...data: E[]) {
    this.tableApi.setGridOption('rowData', data);
  }

  addRows(...data: E[]): IRowNode<E>[] {
    return this.tableApi.applyTransaction({add: data}).add;
  }

  removeRows(...data: E[]): IRowNode<E>[] {
    return this.tableApi.applyTransaction({remove: data}).remove;
  }

  updateRows(...data: E[]): IRowNode<E>[] {
    return this.tableApi.applyTransaction({update: data}).update;
  }

  onRowClicked(event: TableRowClick<E>) {
    event.table = this;
    this.rowClicked.emit(event);
  }

  getSelectedRows(): E[] {
    return this.tableApi.getSelectedRows();
  }

}