import {Worksheet as EWS,Column as EColumn} from "exceljs";
import {AutoFilter, HeaderFooter, PageSetup, WorksheetProperties, WorksheetState, WorksheetView, WorksheetViewSplit} from "exceljs";
import {WorksheetViewCommon, WorksheetViewNormal, WorksheetViewFrozen} from "exceljs";
import {Workbook} from "./workbook";
import {Column} from "./column";
import {TsMap} from "ts-ui/helper";

export type SheetViewFrozen = WorksheetViewCommon & WorksheetViewFrozen
export type SheetViewNormal = WorksheetViewCommon & WorksheetViewNormal;
export type SheetViewSplit = WorksheetViewCommon & WorksheetViewSplit;

export class Sheet {

    private static from(wb: Workbook, ws: EWS): Sheet {
        return new Sheet(wb, ws);
    }

    private columns = new TsMap<string, Column>();

    private constructor(private wb: Workbook,
                        private ws: EWS) {
        this.initialize();
    }

    private initialize() {

        // initialize columnKeys
        const columnKeys =(<any>this.ws)['_key'];
        Object.keys(columnKeys).forEach(colKey => this.setColumn(colKey));

        
    }

    get workbook(): Workbook {
        return this.wb;
    }

    get sheetId(): number {return this.ws.id;}

    get sheetName(): string { return this.ws.name; }

    /**	Returns the number of frozen columns.*/
    get frozenColumns(): number {
        return this.frozenView?.xSplit;
    }

    /**	Returns the number of frozen rows.*/
    get frozenRows(): number {
        return this.frozenView?.ySplit;
    }

    /**	Returns the position of the last row that has content.*/
    get lastRow(): number {
        return this.ws.actualRowCount;
    }

    /**	Returns the position of the last column that has content.*/
    get lastColumn(): number {
        return this.ws.actualColumnCount;
    }

    /**Returns the current number of rows in the sheet, regardless of content.*/
    get maxRow(): number {
        return this.ws.rowCount;
    }

    /**Returns the current number of columns in the sheet, regardless of content.*/
    get maxColumn(): number {
        return this.ws.columnCount;
    }

    /** get the pageSetup */
    get pageSetup(): Partial<PageSetup> { return this.ws.pageSetup;}

    /** Set the pageSetup */
    set pageSetup(value: Partial<PageSetup>) { this.ws.pageSetup=value;}

    /** get the headerFooter */
    get headerFooter(): Partial<HeaderFooter> { return this.ws.headerFooter;}

    /** Set the headerFooter */
    set headerFooter(value: Partial<HeaderFooter>) { this.ws.headerFooter=value;}

    /** get the state */
    get state(): WorksheetState { return this.ws.state;}

    /** Set the state */
    set state(value: WorksheetState) { this.ws.state=value;}

    /** get the properties */
    get properties(): WorksheetProperties { return this.ws.properties;}

    /** Set the properties */
    set properties(value: WorksheetProperties) { this.ws.properties=value;}

    /** get the views */
    get views(): Array<Partial<WorksheetView>> { return this.ws.views;}

    /**Controls the view state*/
    get normalView(): Partial<SheetViewNormal> {
        return <any>this.views.find(v => v.state === 'normal');
    }

    /**Where a number of rows and columns to the top and left are frozen in place. Only the bottom left section will scroll*/
    get frozenView(): Partial<SheetViewFrozen> {
        return <any>this.views.find(v => v.state === 'frozen');
    }

    /**Where the view is split into 4 sections, each semi-independently scrollable*/
    get splitView(): Partial<SheetViewSplit> {
        return <any>this.views.find(v => v.state === 'split');
    }

    /** Set the views */
    set views(value: Array<Partial<WorksheetView>>) { this.ws.views=value;}

    /** get the autoFilter? */
    get autoFilter(): AutoFilter { return this.ws.autoFilter;}

    /** Set the autoFilter? */
    set autoFilter(value: AutoFilter) { this.ws.autoFilter=value;}

    getColumnKey(key: string): Column {
        return this.newColumn(this.ws.getColumnKey(key));
    }

    setColumnKey(key: string, value: Column): void {}

    deleteColumnKey(key: string): void {}

    eachColumnKey(callback: (col: Column, index: number) => void): void {}






    private newColumn(col: EColumn) {
        return Column['from'](this, col);
    }

    private setColumn(col: EColumn | string) {
        const ecol = typeof col === 'string' ? this.ws.getColumnKey(col) : col;
        this.columns.set(ecol.key, this.newColumn(ecol));
    }
}