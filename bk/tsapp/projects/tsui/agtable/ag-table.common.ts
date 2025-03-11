import { ColDef, ColGroupDef, ExcelExportParams, GridApi, GridOptions, GridOptionsService, GridReadyEvent, IRowNode, RowClickedEvent } from "@ag-grid-community/core";
import { AgGridAngular } from "@ag-grid-community/angular";
import { BaseCreator } from "@ag-grid-community/csv-export";
import { AgTable } from "./ag-table.component";
import { Consumer } from "ts-ui/helper";
import { Workbook } from "exceljs";


export type TableColumn<TData = any, TValue = any> = ColDef<TData, TValue> | ColGroupDef<TData>;

export interface TableOption<TData = any> extends GridOptions<TData> { 
  height?: string;
}

export interface TableReadyEvent<TData = any> extends GridReadyEvent<TData, any> {
  view: AgGridAngular<TData, any>;
}

export interface TableRowClick<E = any> extends RowClickedEvent<E, any> {
  table?: AgTable;
}

export interface TableApi<TData = any> extends GridApi<TData> {
  excelCreator: ExcelCreator;
  gos: GridOptionsService;
}

export interface ITableNode<TData = any> extends IRowNode<TData> {

}





export interface PrivateField {
  themeClass?: string;// = 'ag-themes-quartz';
  option?: TableOption;
}



export interface ExcelCreator extends BaseCreator<any, any, any> {
  excelXmlFactory: any;
  xlsxFactory: any;
  columnController: any;
  valueService: any;
  gridOptions: any;
  stylingService: any;
  downloader: any;
  gridSerializer: any;
  gridOptionsWrapper: any;//GridOptionsWrapper;
  zipContainer: any;//ZipContainer;
  exportMode: any;
  postConstruct(): void;
  exportDataAsExcel(params?: ExcelExportParams): string;
  getDataAsExcelXml(params?: ExcelExportParams): string;
  getMimeType(): string;
  getDefaultFileName(): string;
  getDefaultFileExtension(): string;
  createSerializingSession(params: ExcelExportParams): any;//SerializingSession;
  styleLinker: any;
  isExportSuppressed(): boolean;
  setExportMode: any;
  getExportMode: any;
  packageFile(data: string): Blob;
}

export interface XlsColumn {
  label?: string;
  colId: string;
  width?: number;
  alignment?: {
    horizontal: 'left' | 'center' | 'right' | 'fill' | 'justify' | 'centerContinuous' | 'distributed';
    vertical: 'top' | 'middle' | 'bottom' | 'distributed' | 'justify';
    wrapText: boolean;
    shrinkToFit: boolean;
    indent: number;
    readingOrder: 'rtl' | 'ltr';
    textRotation: number | 'vertical';
  }
}

export interface ExportXslOption {
  fileName: string;
  sheetName: string;
  includeColId: boolean;
  customWb?: Consumer<Workbook>;
  columns?: XlsColumn[];
}