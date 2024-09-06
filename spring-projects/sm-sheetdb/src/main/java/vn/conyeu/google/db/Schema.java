package vn.conyeu.google.db;

import vn.conyeu.google.drives.DriveApp;
import vn.conyeu.google.sheet.SheetApp;
import vn.conyeu.google.sheet.XslBook;
import vn.conyeu.google.sheet.XslSheet;

public class Schema {
    private final DriveApp drives;
    private final SheetApp sheets;
    private final XslBook xslBook;

    Schema(DriveApp drives, SheetApp sheets, XslBook xslBook) {
        this.drives = drives;
        this.sheets = sheets;
        this.xslBook = xslBook;
    }

    public String getId() {
        return xslBook.getId();
    }

    public void loadData() {

    }

    public class SchemaTb extends AbstractTb {

        SchemaTb(DriveApp drives, SheetApp sheets, XslSheet sheet) {
            super(drives, sheets, sheet);
        }

    }

    public class SchemaCol extends AbstractTb {

        SchemaCol(DriveApp drives, SheetApp sheets, XslSheet sheet) {
            super(drives, sheets, sheet);
        }
    }

}