import {Objects, TsMap, BiConsumer} from "ts-ui/helper";
import * as js from 'exceljs';
import {Workbook} from "./workbook";
import {Column} from "./column";
const {notNull} = Objects;

export type SheetViewFrozen = js.WorksheetViewCommon & js.WorksheetViewFrozen
export type SheetViewNormal = js.WorksheetViewCommon & js.WorksheetViewNormal;
export type SheetViewSplit = js.WorksheetViewCommon & js.WorksheetViewSplit;

export class Sheet {

    private static from(wb: Workbook, ws: js.Worksheet): Sheet {
        return new Sheet(wb, ws);
    }

    private columns = new TsMap<string, Column>();

    private constructor(private wb: Workbook,
                        private ws: js.Worksheet) {
    }

    get workbook(): Workbook {return this.wb;}

    get sheetId(): number {return this.ws.id;}

    get sheetName(): string { return this.ws.name; }

    /**	Returns the number of frozen columns.*/
    get frozenColumns(): number {return this.frozenView?.xSplit;}

    /**	Returns the number of frozen rows.*/
    get frozenRows(): number {return this.frozenView?.ySplit;}

    /**	Returns the position of the last row that has content.*/
    get lastRow(): number {return this.ws.actualRowCount;}

    /**	Returns the position of the last column that has content.*/
    get lastColumn(): number {return this.ws.actualColumnCount;}

    /**Returns the current number of rows in the sheet, regardless of content.*/
    get maxRow(): number {return this.ws.rowCount;}

    /**Returns the current number of columns in the sheet, regardless of content.*/
    get maxColumn(): number {return this.ws.columnCount;}

    /** get the pageSetup */
    get pageSetup(): Partial<js.PageSetup> { return this.ws.pageSetup;}

    /** Set the pageSetup */
    set pageSetup(value: Partial<js.PageSetup>) { this.ws.pageSetup=value;}

    /** get the headerFooter */
    get headerFooter(): Partial<js.HeaderFooter> { return this.ws.headerFooter;}

    /** Set the headerFooter */
    set headerFooter(value: Partial<js.HeaderFooter>) { this.ws.headerFooter=value;}

    /** get the state */
    get state(): js.WorksheetState { return this.ws.state;}

    /** Set the state */
    set state(value: js.WorksheetState) { this.ws.state=value;}

    /** get the properties */
    get properties(): js.WorksheetProperties { return this.ws.properties;}

    /** Set the properties */
    set properties(value: js.WorksheetProperties) { this.ws.properties=value;}

    /** get the views */
    get views(): Array<Partial<js.WorksheetView>> { return this.ws.views;}

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
    set views(value: Array<Partial<js.WorksheetView>>) { this.ws.views=value;}

    /** get the autoFilter? */
    get autoFilter(): js.AutoFilter { return this.ws.autoFilter;}

    /** Set the autoFilter? */
    set autoFilter(value: js.AutoFilter) { this.ws.autoFilter=value;}

    getColumn(key: string): Column {
        const column = this.columns.get(key);
        if(notNull(column)) return column;
        else {
            const ecol = this.ws.getColumnKey(key);
            return this.setEColumn(ecol);
        }
    }

    getColumnByIndex(columnIndex: number): Column {
        const ecol = this.ws.getColumn(columnIndex);
        return this.getColumn(ecol.key);
    }

    getColumnByLetter(letter: string): Column {
        const ecol = this.ws.getColumn(letter);
        return this.getColumn(ecol.key);
    }

    setColumn(key: string, column: Column): void {
        this.columns.set(key, column);
        this.ws.setColumnKey(key, column['col']);
    }

    deleteColumn(key: string): void {
        this.ws.deleteColumnKey(key);
        this.columns.delete(key);
    }

    eachColumnKey(callback: BiConsumer<Column, string>): void {
        this.columns.forEach((column, colKey) => callback(column, colKey));
    }

    hasColumn(columnKey: string): boolean {
        return this.columns.has(columnKey);
    }

    getJsonRow(fieldIndex: number, beginRow: number, endRow: number): any[] {
        const rows: any[] = this.ws.getRows(beginRow, endRow-beginRow+1);
        return rows.map(row => row.values);
    }

    getValueRow(rowIndex: number): any[] {
        const data = this.ws.getRow(rowIndex).values;
        const row: js.CellValue[] = Array.isArray(data) ? data : Object.values(data);
        return row;
    }

    private setEColumn(ecol: js.Column): Column {
        const col = Column['from'](this, ecol);
        this.columns.set(ecol.key, col);
        return col;
    }
}