package vn.conyeu.google.xsldb;

import vn.conyeu.google.drives.DriveApp;
import vn.conyeu.google.sheet.XslApp;
import vn.conyeu.google.sheet.XslSheet;

import java.util.List;
import java.util.Set;

public abstract class AbstractTb<E> {
    private final DriveApp drives;
    private final XslApp sheets;
    private final XslSheet sheet;
    private final SheetDb sheetDb;

    AbstractTb(DriveApp drives, XslApp sheets, SheetDb sheetDb, XslSheet sheet) {
        this.drives = drives;
        this.sheets = sheets;
        this.sheet = sheet;
        this.sheetDb = sheetDb;
    }

    /**
     * The ID of the table.
     */
    public Integer getId() {
        return sheet.getSheetId();
    }

    /**
     * The name of the table.
     */
    public String getName() {
        return sheet.getTitle();
    }

    public List<Column> getColumns() {
        return sheetDb.getColumnForTable(getName());
    }




}