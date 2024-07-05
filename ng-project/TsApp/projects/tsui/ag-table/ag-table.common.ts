import { AgGridAngular } from "ag-grid-angular";
import { ColDef, ColGroupDef, GridOptions, GridReadyEvent, RowClickedEvent } from "ag-grid-community";
import { AgTableComponent } from "./ag-table.component";


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
