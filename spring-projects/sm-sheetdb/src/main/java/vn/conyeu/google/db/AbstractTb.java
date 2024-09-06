package vn.conyeu.google.db;

import vn.conyeu.google.drives.DriveApp;
import vn.conyeu.google.sheet.SheetApp;
import vn.conyeu.google.sheet.XslSheet;

public abstract class AbstractTb {
    private final DriveApp drives;
    private final SheetApp sheets;
    private final XslSheet sheet;

    AbstractTb(DriveApp drives, SheetApp sheets, XslSheet sheet) {
        this.drives = drives;
        this.sheets = sheets;
        this.sheet = sheet;
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