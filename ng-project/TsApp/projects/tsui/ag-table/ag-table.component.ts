import { Component, EventEmitter, Inject, Input, OnChanges, OnInit, Output, SimpleChanges, ViewChild, ViewEncapsulation } from '@angular/core';
import { ExcelCreator, ExportXslOption, ITableNode, TableApi, TableColumn, TableOption, TableReadyEvent, TableRowClick, XlsColumn } from './ag-table.common';
import { ColDef, Column, GetCellValueParams, GetRowIdFunc, GridOptions, GridReadyEvent, IRowNode, ManagedGridOptionKey } from '@ag-grid-community/core';
import { AgGridAngular } from '@ag-grid-community/angular';
import { AG_CONFIG_TOKEN, AgTableConfig } from './ag-table.config';
import { AgI18N } from './ag-table.i18n';
import { Asserts, Consumer, JsonAny, Objects } from 'ts-ui/helper';
import { Workbook, Worksheet } from 'exceljs';
import { Observable, Observer } from 'rxjs';

const { isEmpty, notNull, mergeDeep, isTrue } = Objects;


export interface ExportData {
  fileName: string;
  blob: Blob;
  wb: Workbook;
  ws: Worksheet;
}

@Component({
  selector: 'ts-ag-table',
  templateUrl: './ag-table.component.html',
  styles: ` :host { display: block; }  `,
  encapsulation: ViewEncapsulation.None,
})
export class AgTable<E = any> implements OnInit, OnChanges {
  @Output() tableReady = new EventEmitter<TableReadyEvent>();
  @Output() rowClicked = new EventEmitter<TableRowClick<E>>();

  @Input() rows: any[] = [];
  @Input() columns: TableColumn[] = [];
  @Input() themeClass: string;
  @Input() components: JsonAny;

  @Input() set option(option: TableOption) {
    this.gridOption = mergeDeep(this.gridOption, option);
  }

  @Input() set getRowId(fnc: GetRowIdFunc) {
    this.gridOption.getRowId = fnc;
  }

  @Input() set tableHeight(height: string) {
    this.gridOption.height = height;
  };


  @ViewChild('agGridComp', { static: true })
  view: AgGridAngular;

  /////////////////////////////////

  // private _field: PrivateField = {};
  gridOption: TableOption;

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

  get tableHeight(): string {
    return this.gridOption.height;
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

  set pagination(view: boolean) {
    this.tableApi?.setGridOption('pagination', view);
  }

  autoSizeAllColumns(): void {
    this.tableApi.autoSizeAllColumns();
  }

  deleteAll(): void {
    this.tableApi.setGridOption('rowData', []);
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

  getColumnHeader(column: Column | string) {
    return this.tableApi.getColumnDef(column)?.headerName;
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

  addRow(data: E): IRowNode<E> {
    return this.tableApi.applyTransaction({ add: [data] }).add[0];
  }

  addRows(data: E[]): ITableNode<E>[] {
    return this.tableApi.applyTransaction({ add: data }).add;
  }

  removeRows(...data: E[]): ITableNode<E>[] {
    return this.tableApi.applyTransaction({ remove: data }).remove;
  }

  updateRows(...data: E[]): ITableNode<E>[] {
    const saveData = this.tableApi.applyTransaction({ update: data });
    //this.tableApi.refreshCells({force: true, rowNodes: saveData.update});
    return saveData.update;
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

  /**
   * Updates a single gridOption to the new value provided. (Cannot be used on `Initial` properties.)
   * If updating multiple options, it is recommended to instead use `api.updateGridOptions()` which batches update logic.
   */
  setGridOption<Key extends ManagedGridOptionKey>(key: Key, value: GridOptions<E>[Key]): void {
    this.tableApi?.setGridOption(key, value);
  }

  /** Sets the state back to match the originally provided column definitions. */
  resetColumnState(): void {
    this.tableApi.resetColumnState();
  }

  onRowClicked(event: TableRowClick<E>) {
    event.table = this;
    this.rowClicked.emit(event);
  }

  getAllDisplayedColumns(): Column[] {
    return this.tableApi.getAllDisplayedColumns();
  }

  getSelectedRows(): E[] {
    return this.tableApi?.getSelectedRows() || [];
  }

  getSelectedNodes(): IRowNode<E>[] {
    return this.tableApi?.getSelectedNodes() || [];
  }

  getCellValue<C>(params: GetCellValueParams<C>) {
    return this.tableApi.getCellValue(params);
  }

  getRowValue(rowNode: IRowNode<E>, columns: XlsColumn[]): JsonAny {
    Asserts.notEmpty(columns, "@XlsColumn[]");
    return Objects.arrayToJson(columns, col => {
      const value = this.getCellValue({ rowNode, colKey: col.colId });
      return [col.colId, value];
    });
  }

  /** Returns the column with the given `colKey`, which can either be the `colId` (a string) or the `colDef` (an object). */
  getColumn<TValue = any>(key: string | ColDef<E, TValue> | Column<TValue>): Column<TValue> | null {
    return this.tableApi.getColumn(key);
  }

  /** Similar to `forEachNodeAfterFilter`, except the callbacks are called in the order the rows are displayed in the grid. */
  forEachNodeAfterFilterAndSort(callback: (rowNode: IRowNode<E>, index: number) => void): void {
    this.tableApi?.forEachNodeAfterFilterAndSort(callback);
  }

  /**
     * Iterates through each node (row) in the grid and calls the callback for each node.
     * This works similar to the `forEach` method on a JavaScript array.
     * This is called for every node, ignoring any filtering or sorting applied within the grid.
     * If using the Infinite Row Model, then this gets called for each page loaded in the page cache.
     */

  forEachNode(callback: (rowNode: IRowNode<E>, index: number) => void, includeFooterNodes?: boolean): void {
    this.tableApi?.forEachNode(callback, includeFooterNodes);
  }

  /** Similar to `forEachNode`, except skips any filtered out data. */
  forEachNodeAfterFilter(callback: (rowNode: IRowNode<E>, index: number) => void): void {
    this.tableApi?.forEachNodeAfterFilter(callback);
  }




  exportXsl(options: Partial<ExportXslOption>): Observable<ExportData> {
    options = Objects.mergeDeep({ includeColId: true, sheetName: 'data' }, options);

    return new Observable((observer: Observer<ExportData>) => {
      const columns: XlsColumn[] = this.extractExportColumns(options);
      const sheetName = options.sheetName || 'data';

      const excel = new Workbook();

      //
      if (notNull(options.customWb)) {
        options.customWb(excel);
      }

      const frozenRow = 1 + (isTrue(options.includeColId) ? 1 : 0);
      const ws = excel.addWorksheet(sheetName, { views: [{ state: 'frozen', ySplit: frozenRow }] });

      // column header
      ws.columns = columns.map((col: XlsColumn) => ({
        header: col.label,
        key: col.colId,
        width: col.width,
        alignment: col.alignment
      }));

      // include column id
      if (isTrue(options.includeColId)) {
        ws.addRow(Objects.arrayToJson(columns, col => [col.colId, col.colId])).hidden = true;
      }

      // row data
      this.forEachNodeAfterFilterAndSort(node => {
        ws.addRow(this.getRowValue(node, columns));
      });

      // format
      ws.getRow(0).font = { bold: true }
      ws.getRow(1).font = { bold: true };

      ws.columns.forEach((col) => {
        if (col.width) return col.width;
        else {
          const largestValueLength = col.values.reduce((maxWidth, value: any) => value && value.length > maxWidth ? value.length : maxWidth, 0);
          col.width = Math.max(col.header.length, largestValueLength) + 10;
          return col;
        }
      });


      // export
      const fileName = options?.fileName || 'excel_xsl.xlsx';

      excel.xlsx.writeBuffer({ useStyles: true, filename: fileName })
        .then(buffer => {
          const type = 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet';
          const blob = new Blob([buffer], { type });
          observer.next({ fileName, blob, wb: excel, ws: ws });
          observer.complete();
        })
        .catch(reason => observer.error(reason));

    });

  }

  private createDefaultGridOption(): TableOption {
    return {
      height: '350px',
      domLayout: 'normal',
      animateRows: true,
      rowSelection: 'multiple',
      scrollbarWidth: 20,
      pagination: false,
      enableRangeSelection: true,
      overlayLoadingTemplate: '<i class="fal fa-sync fa-spin"></i>',
      overlayNoRowsTemplate: this.i18n.overlayNoRowsTemplate,
      suppressPropertyNamesCheck: true,
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


  private extractExportColumns(options: Partial<ExportXslOption>): XlsColumn[] {
    const colJson = (col: any) => ({
      label: col.getColDef().headerName,
      colId: col.getColDef().field ?? col.getColId(),
      alignment: { wrapText: true }
    } as XlsColumn);


    if (isEmpty(options.columns)) return this.getAllDisplayedColumns().map(col => colJson(col));
    else return options.columns.map(col => Objects.mergeDeep(colJson(this.getColumn(col.colId)), col));
  }

}
