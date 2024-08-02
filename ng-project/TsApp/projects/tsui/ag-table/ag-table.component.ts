import { ChangeDetectionStrategy, Component, EventEmitter, Inject, Input, OnChanges, OnInit, Output, SimpleChanges, ViewChild, ViewEncapsulation } from '@angular/core';
import { ExcelCreator, ITableNode, TableApi, TableColumn, TableOption, TableReadyEvent, TableRowClick } from './ag-table.common';
import { ColDef, Column, GetRowIdFunc, GridOptions, GridReadyEvent } from '@ag-grid-community/core';
import { AgGridAngular } from '@ag-grid-community/angular';
import { AG_CONFIG_TOKEN, AgTableConfig } from './ag-table.config';
import { AgI18N } from './ag-table.i18n';
import { Asserts, Consumer, JsonAny, Objects } from 'ts-ui/helper';

const { notNull, mergeDeep } = Objects;



@Component({
  selector: 'ts-ag-table',
  templateUrl: './ag-table.component.html',
  styles: ` :host { display: block; }  `,
  encapsulation: ViewEncapsulation.None,
  //changeDetection: ChangeDetectionStrategy.OnPush
})
export class AgTableComponent<E = any> implements OnInit, OnChanges {
  @Output()tableReady = new EventEmitter<TableReadyEvent>();
  @Output() rowClicked = new EventEmitter<TableRowClick<E>>();

  @Input() rows: any[] = [];
  @Input() columns: TableColumn[] = [];  
  @Input() tableHeight: string = '250px';
  @Input() themeClass: string;
  @Input() components: JsonAny;

  @Input() set option(option: TableOption) {
    this.gridOption = mergeDeep(this.gridOption, option);
  }

  @Input() set getRowId(fnc: GetRowIdFunc) {
    this.gridOption.getRowId = fnc;
  }

  @ViewChild('agGridComp', { static: true })
  view: AgGridAngular;

  /////////////////////////////////

  // private _field: PrivateField = {};
  gridOption: GridOptions;

  constructor(@Inject(AG_CONFIG_TOKEN) private config: AgTableConfig) {
    this.gridOption = this.createDefaultGridOption();
  }

  ngOnInit(): void {
  }

  ngOnChanges(changes: SimpleChanges): void { }

  /** Ready ag-grid */
  gridReadyAg(evt: GridReadyEvent) {
    this.tableReady.emit({ view: this.view, ...evt });
  }

  get i18n(): AgI18N {
    return this.config?.i18n;
  }

  get tableApi(): TableApi<E> {
    return this.view.api as TableApi<E>;
  }

  get excelCreator(): ExcelCreator {
    return this.tableApi?.excelCreator;
  }

  get optionService(): any {
    return this.tableApi?.gos;
  }

  get getRowIdFunc(): any {
    return this.optionService.getCallback('getRowId');
  }


  getRowNode(data: E): ITableNode {
    const getRowIdFunc = this.getRowIdFunc;
    Asserts.notNull(getRowIdFunc, "@gridOption.getRowId");
    return this.tableApi.getRowNode(getRowIdFunc({ data, level: 0 }));
  }

  editColumn(colId: string, consumer: Consumer<TableColumn>): void {
    const columnIndex = this.tableApi.getColumnDefs().findIndex((col: any) => col.colId === colId);
    if (columnIndex !== -1 && notNull(consumer)) {
      const columns = [...this.tableApi.getColumnDefs()];
      consumer(columns[columnIndex]);
      this.tableApi.updateGridOptions({ columnDefs: columns });
    }
  }

  setColumns(columns: ColDef[]) {
    this.tableApi.setGridOption('columnDefs', columns);
  }

  setRows(...data: E[]) {
    this.tableApi.setGridOption('rowData', data);
  }

  saveRows(...data: E[]): void {
    data.reduce((json: any, value) => {
      json['update'] = json['update'] ?? [];
      json['add'] = json['add'] ?? [];

      if (notNull(this.getRowNode(value))) json['update'].push(value);
      else json['add'].push(value);

      return json;
    }, {});

  }

  addRows(...data: E[]): ITableNode<E>[] {
    return this.tableApi.applyTransaction({ add: data }).add;
  }

  removeRows(...data: E[]): ITableNode<E>[] {
    return this.tableApi.applyTransaction({ remove: data }).remove;
  }

  updateRows(...data: E[]): ITableNode<E>[] {
    return this.tableApi.applyTransaction({ update: data }).update;
  }

  hideAllColumn(): void {
    this.setColumnsVisible(this.tableApi.getColumns(), false);
  }

  setColumnsVisible(columns: (string | Column)[], visible: boolean) {
    // this.tableApi.setColumnsVisible(columns, visible);

    this.tableApi.applyColumnState({
      applyOrder: true,
      state: columns.map(col => ({
        colId: typeof col === 'string' ? col : col.getColId(),
        hide: !visible
      }))
    })
  }




  /** Sets the state back to match the originally provided column definitions. */
  resetColumnState(): void {
    this.tableApi.resetColumnState();
  }

  onRowClicked(event: TableRowClick<E>) {
    event.table = this;
    this.rowClicked.emit(event);
  }

  getSelectedRows(): E[] {
    return this.tableApi.getSelectedRows();
  }

  private createDefaultGridOption(): GridOptions {
    return {
      domLayout: 'normal',
      animateRows: true,
      rowSelection: 'multiple',
      scrollbarWidth: 20,
      enableRangeSelection: true,
      overlayLoadingTemplate: '<i class="fal fa-sync fa-spin"></i>',
      overlayNoRowsTemplate: this.i18n.overlayNoRowsTemplate,
      maintainColumnOrder: true,
      rowModelType: 'clientSide',
      sideBar: { toolPanels: ['columns'] },
      pivotPanelShow: 'always',
      defaultColDef: {
        editable: false,
        enableValue: true,
        enableRowGroup: true,
        enablePivot: true,
        sortable: false,
        resizable: true,
        filter: true,
        wrapHeaderText: true,
        suppressHeaderMenuButton: true,
        suppressHeaderFilterButton: true
      },
    
      getRowClass: (p: any) => {
        let n = p.node.rowIndex % 2;
        return `ag-grid-row-style-${n === 0 ? 0 : 1}`;
      }
    }
  }
}