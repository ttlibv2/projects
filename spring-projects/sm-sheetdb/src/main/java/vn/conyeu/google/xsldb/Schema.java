package vn.conyeu.google.xsldb;

import vn.conyeu.google.drives.DriveApp;
import vn.conyeu.google.sheet.XslApp;
import vn.conyeu.google.sheet.XslBook;
import vn.conyeu.google.sheet.XslSheet;

public class Schema {
    private final DbApp dbApp;
    private final XslBook xslBook;

    Schema(SheetDb sheetDb, XslBook xslBook) {
        this.dbApp = sheetDb.dbApp();
        this.xslBook = xslBook;
    }

    public String getId() {
        return xslBook.getId();
    }

    public void loadData() {

    }

    public abstract class SchemaTb extends AbstractTb<Column> {

        SchemaTb(DriveApp drives, XslApp sheets, SheetDb sheetDb, XslSheet sheet) {
            super(drives, sheets, sheetDb, sheet);
        }
    }

    public abstract class SchemaCol extends AbstractTb<Column> {

        SchemaCol(DriveApp drives, XslApp sheets, SheetDb sheetDb, XslSheet sheet) {
            super(drives, sheets, sheetDb, sheet);
        }
    }

}