package vn.conyeu.google.db;

import com.google.api.services.sheets.v4.model.Sheet;
import vn.conyeu.google.sheet.builder.ConsumerReturn;
import vn.conyeu.google.sheet.builder.RowDataBuilder;
import vn.conyeu.google.sheet.builder.SheetBuilder;
import vn.conyeu.google.sheet.builder.XmlBuilder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TableBuilder implements XmlBuilder<Sheet> {
    private final SheetBuilder sheet = new SheetBuilder();
    private final List<String> columnNames = new ArrayList<>();
    private final Map<String, ColumnBuilder> columns = new HashMap<>();

    private String fontFamily = "Consolas";


    @Override
    public Sheet build() {
        sheet.rowCount(2).frozenRowCount(1);
        sheet.columnCount(columns.isEmpty() ? 2 : columns.size());

        RowDataBuilder rowId = sheet.getRow(0);
        RowDataBuilder rowVal = sheet.getRow(1);

        // build columns
        for(String colName:columnNames) {
            Column cb = columns.get(colName).build();
            rowId.addCell(c -> c.value(colName).bold(true).family(fontFamily));
            rowVal.addCell(c -> c.family(fontFamily));
        }

        return sheet.build();
    }

    /**
     * The ID of the table.
     * @param tableId sheetId
     */
    public TableBuilder tableId(Integer tableId) {
        sheet.sheetId(tableId);
        return this;
    }

    /**
     * Set the fontFamily
     *
     * @param fontFamily the value
     */
    public TableBuilder fontFamily(String fontFamily) {
        this.fontFamily = fontFamily;
        return this;
    }

    /**
     * The name of the table.
     * @param name name
     */
    public TableBuilder name(String name) {
        sheet.title(name);
        return this;
    }

    public TableBuilder addColumn(String name, ConsumerReturn<ColumnBuilder> consumer) {
        return addColumn(consumer.accept(new ColumnBuilder()).columnName(name));
    }

    private TableBuilder addColumn(ColumnBuilder builder) {
        String name = builder.getColumnName();
        if(!columns.containsKey(name)) {
            columns.put(name, builder);
            columnNames.add(name);
            return this;
        }
        else {
            throw new DbException("The column '%s' exist.", name);
        }
    }
}