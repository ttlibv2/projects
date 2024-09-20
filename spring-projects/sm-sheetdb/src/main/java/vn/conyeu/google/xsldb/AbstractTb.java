package vn.conyeu.google.xsldb;

import com.google.api.services.sheets.v4.model.CellData;
import com.google.api.services.sheets.v4.model.GridData;
import com.google.api.services.sheets.v4.model.RowData;
import com.google.api.services.sheets.v4.model.Sheet;
import vn.conyeu.commons.beans.ObjectMap;
import vn.conyeu.commons.utils.Asserts;
import vn.conyeu.commons.utils.Objects;
import vn.conyeu.google.drives.DriveApp;
import vn.conyeu.google.sheet.XslApp;
import vn.conyeu.google.sheet.XslSheet;
import vn.conyeu.google.sheet.builder.SheetUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class AbstractTb<E> {
    protected final DriveApp drives;
    protected final XslApp sheets;
    protected final XslSheet sheet;
    protected final SheetDb db;
    protected final List<String> columnNames = new ArrayList<>();
    protected final Map<String, Column> columns;

    AbstractTb(SheetDb db, XslSheet sheet) {
        this.db = Asserts.notNull(db, "@SheetDb");
        this.sheet = Asserts.notNull(sheet, "XslSheet");
        this.drives = db.drives;
        this.sheets = db.sheets;
        this.columns = new HashMap<>();
    }

    public AbstractTb loadColumns() {
        clearColumns();
        List<Object> list = Objects.getFirstList(sheet.getRowValues(1, 1));
        if (list != null) {
            for (int index = 0; index < list.size(); index++) {
                putColumnOfString(index, list.get(index));
            }
        }
        return this;
    }

    /**
     * Returns the columns
     */
    public final List<Column> getColumns() {
        return List.copyOf(columns.values());
    }

    /**
     * Returns the sheet
     */
    public final Sheet getSheet() {
        return sheet.getModel();
    }

    /**
     * The ID of the table.
     */
    public final Integer getId() {
        return sheet.getSheetId();
    }

    public final String getUrl() {
        return sheet.getUrl();
    }

    /**
     * The name of the table.
     */
    public final String getName() {
        return sheet.getName();
    }

    public final String getXslId() {
        return sheet.getBookId();
    }

    public final int getFrozenRow() {
        return sheet.getFrozenRowCount();
    }

    public final int getMaxColumn() {
        return sheet.getColumnCount();
    }

    public final int getMaxRow() {
        return sheet.getRowCount();
    }

    public final int getCountColumn() {
        return columns.size();
    }

    public final boolean isExistColumn(String name) {
        return columns.containsKey(name);
    }

    public final boolean isExistColumn(Column column) {
        return columns.containsKey(column.getName());
    }

    public Column getColumn(String columnName) {
        throwColumnNotExist(columnName);
        return columns.get(columnName);
    }


    protected final void putColumn(Column column) {
        throwColumnExist(column);
        columnNames.add(column.getName());
        columns.put(column.getName(), column);
    }

    protected final void putColumns(List<Column> columns) {
        columns.forEach(this::putColumn);
    }

    protected final Column putColumnOfString(int columnIndex, Object json) {
        ObjectMap map = ObjectMap.fromJson(json);
        Column column = map.asObject(Column.class).index(columnIndex);
        putColumn(column);
        return column;
    }

    protected AbstractTb parseColumnFromGrid() {
        clearColumns();
        GridData gridData = sheet.getData();
        List<RowData> rowData = gridData.getRowData();
        if (rowData.size() == 1) {
            List<CellData> cellData = rowData.get(0).getValues();
            boolean prevCellHasEmpty = false;

            for (int c = 0; c < cellData.size(); c++) {
                String cellJson = cellData.get(c).getFormattedValue();
                if (Objects.notBlank(cellJson)) {
                    if(prevCellHasEmpty) throwBlankColumnName(c-1);
                    else putColumnOfString(c, cellJson);
                }
                else {
                    prevCellHasEmpty = true;
                }
            }
        }
        return this;
    }

    protected void removeColumnLocal(String columnName) {
        columnNames.remove(columnName);
        columns.remove(columnName);
    }

    protected void throwBlankColumnName(int columnPos) {
        String letter = SheetUtil.numberToLetter(columnPos);
        throw new DbException("The column at letter `%s` invalid", letter, getName());
    }

    protected void throwColumnExist(Column column) {
        if(isExistColumn(column)) {
            throw new DbException("The column `%s` exist.", column.getName());
        }
    }

    protected void throwColumnNotExist(String columnName) {
        if(!isExistColumn(columnName))
            throw new DbException("The column `%s` not exist", columnName);
    }

    private void clearColumns() {
        columns.clear();
        columnNames.clear();
    }


}