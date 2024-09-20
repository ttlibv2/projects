package vn.conyeu.google.xsldb;

import vn.conyeu.commons.utils.Asserts;
import vn.conyeu.google.core.GoogleException;
import vn.conyeu.google.drives.DriveApp;
import vn.conyeu.google.drives.GFolder;
import vn.conyeu.google.drives.GMime;
import vn.conyeu.google.sheet.XslApp;
import vn.conyeu.google.sheet.XslBook;
import vn.conyeu.google.sheet.XslSheet;
import vn.conyeu.google.sheet.builder.ConsumerReturn;
import vn.conyeu.google.sheet.builder.GridBuilder;
import vn.conyeu.google.sheet.builder.SheetBuilder;
import vn.conyeu.google.sheet.builder.XlRange;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SheetDb {
    protected final GFolder folder;
    protected final DriveApp drives;
    protected final XslApp sheets;

    // Map<File_Name, File_Id>
    private final Map<String, String> files;

    // Map<BookId, XslBook>
    private final Map<String, SheetTb> tables;

    SheetDb(DbApp dbApp, GFolder folder) {
        this.drives = dbApp.driveApp;
        this.sheets = dbApp.xslApp;
        this.folder = Asserts.notNull(folder);
        this.tables = new HashMap<>();
        this.files = new HashMap<>();
        this.validateFolder(folder);
    }

    private void validateFolder(GFolder folder) {
        String isDb = folder.getProperty("isDb");
        if (!"true".equals(isDb)) {
            String msg = "The folder %s not database";
            throw new GoogleException(msg, folder.getUrl());
        }
    }


    public String getId() {
        return folder.getId();
    }

    public String getUrl() {
        return folder.getUrl();
    }

    public String getOwner() {
        return folder.getOwner().getEmailAddress();
    }


    /**
     * Create table
     *
     * @param name the name of table
     */
    public SheetTb createTable(String name) {
        return createTable(name, new ArrayList<>());
    }

    /**
     * Create table
     *
     * @param name the name of table
     */
    public SheetTb createTable(String name, List<Column> columns) {
        validateTableExists(name);
        SheetTb sheetTb = createXsl(name, sheet -> applySheet(sheet, columns));
        sheetTb.putColumns(columns);
        return sheetTb;
    }

    public <E> SheetTb<E> openTable(String name) {
        if (files.isEmpty()) loadSchema();
        if (!files.containsKey(name)) throw new DbException("The table %s not exist", name);
        if (!tables.containsKey(name)) {
            String fileId = files.get(name);
            XslBook xslBook = sheets.openById(fileId, name + "!2:2");
            return putSchema(xslBook, name).parseColumnFromGrid();
        }
        return tables.get(name);
    }

    /**
     * Load schema
     */
    protected SheetDb loadSchema() {
        files.clear();
        folder.search(q -> q.notFolder()
                        .and(q.properties.has("istable"), q.isMime(GMime.G_SHEET)))
                .mapTo(file -> this.files.put(file.getName(), file.getId()));

        return this;
    }

    private void validateTableExists(String name) {
        if (files.containsKey(name)) {
            throw new DbException("The table '%s' exist", name);
        }
    }

    private SheetBuilder applySheet(SheetBuilder sheet, List<Column> columns) {
        int frozenRow = 2, countDataRow = 1;
        int colSize = Math.max(2, columns.size());

        sheet.sheetId(0).rowCount(frozenRow + countDataRow)
                .frozenRowCount(frozenRow).columnCount(colSize)
                .defaultFormat(c -> c.fontFamily("Consolas").fontSize(12));

        GridBuilder grid = sheet.getGrid(0);

        // set value column
        for (int c = 0; c < columns.size(); c++) {
            Column col = columns.get(c);
            ColumnType colType = col.getType();

            // set name column
            XlRange range = grid.getRange(0, c, frozenRow);
            range.setValues(0, col.getName(), col.toString());

            // set type column
            range = grid.getRange(frozenRow, c, countDataRow).applyDefaultCell();
            range.setNumberFormat(colType.formatType, colType.pattern);

        }

        grid.getRow(0).editCells(c -> c.bold(true));
        for (int r = 1; r < frozenRow; r++) grid.hideRow(r);

        // permision row id
        String email = getOwner();
        grid.protectRow(0, 2, List.of(email), "Only Owner "+email+" edit");

        return sheet;
    }

    private <E> SheetTb<E> createXsl(String name, ConsumerReturn<SheetBuilder> consumer) {
        XslBook xslBook = sheets.create(name, xsl -> xsl.addSheet(s -> consumer.accept(s).title(name)).title(name));
        drives.move(getId(), xslBook.getId(), f -> f.property("istable", "true"));
        putSchema(xslBook, name);
        return tables.get(name);
    }

    private SheetTb putSchema(XslBook xslBook, String sheetName) {
        XslSheet xslSheet = xslBook.getSheetByName(sheetName);
        SheetTb sheetTb = new SheetTb(this, xslSheet);
        files.put(xslSheet.getName(), xslSheet.getBookId());
        tables.put(xslSheet.getName(), sheetTb);
        return sheetTb;
    }


}