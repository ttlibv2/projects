package vn.conyeu.google.xsldb;

import com.google.api.services.sheets.v4.model.Sheet;
import vn.conyeu.google.drives.DriveApp;
import vn.conyeu.google.sheet.XslApp;
import vn.conyeu.google.sheet.XslSheet;
import vn.conyeu.google.sheet.builder.GridBuilder;
import vn.conyeu.google.sheet.builder.XlRange;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class AbstractTb<E> {
    protected final DriveApp drives;
    protected final XslApp sheets;
    protected final XslSheet sheet;
    protected final SheetDb sheetDb;
    private List<String> colNames = new ArrayList<>();
    protected Map<String, Column> columns;

    AbstractTb(SheetDb sheetDb, XslSheet sheet) {
        this.drives = sheetDb.drives;
        this.sheets = sheetDb.sheets;
        this.sheet = sheet;
        this.sheetDb = sheetDb;
        this.columns = new HashMap<>();

        this.initialize();
    }

    private void initialize() {
        GridBuilder grid = sheet.getDataBuilder();
        for(int c=0;c<grid.getColumnSize();c++) {
            XlRange range = grid.findColumn(c);
            Column column = parseColumn(range);
            colNames.add(column.getColumnName());
            columns.put(column.getColumnName(), column);
        }
    }

    private Column parseColumn(XlRange range) {
        Column col = new Column();
        col.position(range.getFirstColumn());
        col.columnName(range.findValueAt(0));

        String colType = range.findValueAt(1);
        String[] segs = colType.split("::");
        col.columnType(ColumnType.valueOf(segs[0]));


        return col;
    }

    /**
     * Returns the columns
     */
    public List<Column> getColumns() {
        return List.copyOf(columns.values());
    }

    /**
     * Returns the sheet
     */
    public Sheet getSheet() {
        return sheet.getModel();
    }

    /**
     * The ID of the table.
     */
    public Integer getId() {
        return sheet.getSheetId();
    }

    public String getUrl() {
        return sheet.getUrl();
    }

    /**
     * The name of the table.
     */
    public String getName() {
        return sheet.getTitle();
    }

    public void addRow(E item) {

    }





}