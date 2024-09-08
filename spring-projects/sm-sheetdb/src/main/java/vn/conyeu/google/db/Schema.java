package vn.conyeu.google.db;

import vn.conyeu.google.drives.DriveApp;
import vn.conyeu.google.sheet.XslApp;
import vn.conyeu.google.sheet.XslBook;
import vn.conyeu.google.sheet.XslSheet;

public class Schema {
    private final DriveApp drives;
    private final XslApp sheets;
    private final XslBook xslBook;

    Schema(DriveApp drives, XslApp sheets, XslBook xslBook) {
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

        SchemaTb(DriveApp drives, XslApp sheets, XslSheet sheet) {
            super(drives, sheets, sheet);
        }

    }

    public class SchemaCol extends AbstractTb {

        SchemaCol(DriveApp drives, XslApp sheets, XslSheet sheet) {
            super(drives, sheets, sheet);
        }
    }

}