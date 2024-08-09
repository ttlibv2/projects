import {Column as EColumn} from 'exceljs';
import {Sheet} from "./sheet";

export class Column {
    private static from(sheet: Sheet, col: EColumn) {
        return new Column(sheet, col);
    }
    private constructor(private sheet: Sheet, private col: EColumn) {
    }
}