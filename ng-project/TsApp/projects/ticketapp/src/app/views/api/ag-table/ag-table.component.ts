import { Component, EventEmitter, Input, OnChanges, Output, Renderer2, SimpleChanges, ViewChild, ViewEncapsulation } from '@angular/core';
import { AgGridAngular } from 'ag-grid-angular';
import { ColDef, ColGroupDef, GridApi, GridOptions, GridReadyEvent, IRowNode, RowClickedEvent } from 'ag-grid-community';
import { Callback, Consumer, Objects } from 'ts-helper';
import { LoggerService } from 'ts-logger';


const { notBlank, notNull } = Objects;

export type TableColumn<TData = any, TValue = any> = ColDef<TData, TValue> | ColGroupDef<TData>;

export interface TableOption<TData = any> extends GridOptions<TData> { }

export interface TableReadyEvent<TData = any> extends GridReadyEvent<TData, any> {
  view: AgGridAngular<TData, any>;
}

export interface TableRowClick<E=any> extends RowClickedEvent<E, any> {
  table?: AgTableComponent;
}


export interface PrivateField {
  themeClass?: string;// = 'ag-themes-quartz';
  option?: TableOption;

}


export const defaultOption: TableOption = {
  domLayout: 'normal',
  animateRows: true,
  rowSelection: 'single',
  scrollbarWidth: 20,
  enableRangeSelection: true,
  overlayLoadingTemplate: '<i class="fal fa-sync fa-spin"></i>',
  overlayNoRowsTemplate: 'Không có dòng nào',
  maintainColumnOrder: true,
  defaultColDef: {
    editable: true,
    enableValue: true,
    enableRowGroup: true,
    enablePivot: true,
    sortable: false,
    resizable: true,
    filter: true
  },

  getRowClass: (p: any) => {
    let n = p.node.rowIndex % 2;
    return `ag-grid-row-style-${n === 0 ? 0 : 1}`;
  }
};

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
    this.field.option = { ...defaultOption, ...option };
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


  constructor(
    private logger: LoggerService,
    //private render: Renderer2
  ) { }



  /** Ready ag-grid */
  gridReadyAg(evt: GridReadyEvent) {
    this.tableReady.emit({ view: this.view, ...evt });
  }

  editColumn(colId: string, consumer: Consumer<ColDef>): void {
    const columnIndex = this.tableApi.getColumnDefs().findIndex((col: any) => col.colId === colId);
    if(columnIndex !== -1 && notNull(consumer))  {
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