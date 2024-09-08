package vn.conyeu.google.xsldb;

import com.google.api.services.sheets.v4.model.Spreadsheet;
import vn.conyeu.commons.utils.Asserts;
import vn.conyeu.google.drives.DriveService;
import vn.conyeu.google.drives.GFolder;
import vn.conyeu.google.sheet.XslApp;
import vn.conyeu.google.sheet.XslService;
import vn.conyeu.google.sheet.XslBook;
import vn.conyeu.google.sheet.builder.ColumnDataBuilder;
import vn.conyeu.google.sheet.builder.SheetBuilder;
import vn.conyeu.google.xsldb.builder.DbUtil;

import java.util.List;

public class SheetDb {
    private final DbApp dbApp;
    private final GFolder folder;
    private final Schema schema;

    SheetDb(DbApp dbApp, GFolder folder, XslBook schema) {
        this.dbApp = Asserts.notNull(dbApp);
        this.folder = Asserts.notNull(folder);
        this.schema = new Schema(this, schema);
    }

    protected DbApp dbApp() {
        return dbApp;
    }

    public String getDbId() {
        return folder.getId();
    }

    public String getDbUrl() {
        return folder.getUrl();
    }

    public String getOwnerEmail() {
        return folder.getOwner().getEmailAddress();
    }

    public List<Column> getColumnForTable(String name) {
        throw new UnsupportedOperationException();
    }

    public <E> SheetTb<E> createTable(String name, List<Column> columns) {
        SheetBuilder sheetBuilder = DbUtil.buildSheet(name, getOwnerEmail());
        sheetBuilder.columnCount(columns.size());

        for(int pos=0;pos<columns.size();pos++) {
            Column column = columns.get(pos);
            ColumnDataBuilder colBuilder = sheetBuilder.addColumn();
            colBuilder.getCell(0).stringValue(column.getColumnName());
            colBuilder.getCell(1).numberFormat();
        }

        Spreadsheet ss = dbApp.create().sheets.createXsl(name, xsl -> xsl.addSheet(sheetBuilder));




    }


}