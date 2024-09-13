package vn.conyeu.google.xsldb;

import vn.conyeu.google.drives.DriveApp;
import vn.conyeu.google.sheet.XslApp;
import vn.conyeu.google.sheet.XslSheet;
import vn.conyeu.google.xsldb.Column;
import vn.conyeu.google.xsldb.SheetDb;

import java.util.List;
import java.util.Set;

public abstract class AbstractTb<E> {
    private final DriveApp drives;
    private final XslApp sheets;
    private final XslSheet sheet;
    private final SheetDb sheetDb;

    AbstractTb(SheetDb sheetDb, XslSheet sheet) {
        this.drives = sheetDb.dbApp().driveApp;
        this.sheets = sheetDb.dbApp().xslApp;
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





}