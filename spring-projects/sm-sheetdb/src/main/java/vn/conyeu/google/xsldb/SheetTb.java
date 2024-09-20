package vn.conyeu.google.xsldb;

import lombok.extern.slf4j.Slf4j;
import vn.conyeu.commons.utils.Asserts;
import vn.conyeu.google.sheet.XslSheet;
import vn.conyeu.google.sheet.builder.BatchUpdateBuilder;
import vn.conyeu.google.sheet.builder.Dimension;
import vn.conyeu.google.sheet.builder.UpdateCellsBuilder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Slf4j
public class SheetTb<E> extends AbstractTb<E> {

    SheetTb(SheetDb db, XslSheet sheet) {
        super(db, sheet);
    }

    public final SheetTb<E> loadColumns() {
        super.loadColumns();
        return this;
    }

    /**
     * Add column to table
     *
     * @param column the column to add
     */
    public void addColumn(Column column) {
        addColumns(List.of(column));
    }

    /**
     * Add columns to table
     *
     * @param columns the columns to add
     */
    public void addColumns(Column... columns) {
       addColumns(List.of(columns));
    }

    /**
     * Add columns to table
     *
     * @param columns the columns to add
     */
    public void addColumns(List<Column> columns) {
        sheet.batchUpdate(builder -> {
            buildAddColumns(columns, builder);
            builder.includeSpreadsheetInResponse(true);
            builder.fields("updatedSpreadsheet.sheets.properties");
            return builder;
        });

        putColumns(columns);
    }

    /**
     * Delete column by name
     *
     * @param columnName the name column to delete
     */
    public void deleteColumn(String columnName) {
        throwColumnNotExist(columnName);
        Integer colNum = getColumn(columnName).getIndex();
        sheet.deleteColumnByIndex(colNum);
        removeColumnLocal(columnName);
    }

    /**
     * Delete columns by name
     *
     * @param columnNames the name columns to delete
     */
    public void deleteColumns(String... columnNames) {
        List<Integer> indexes = new ArrayList<>();
        for(String columnName:columnNames) {
            throwColumnNotExist(columnName);
            indexes.add(getColumn(columnName).getIndex());
        }
        // action delete
        sheet.deleteColumnByIndex(indexes);

        // update local
        for(String columnName:columnNames) {
            removeColumnLocal(columnName);
        }
    }

    /**
     * Move column to index
     * @param columnName the name column to move
     * @param destinationIndex the index to move
     * */
    public void moveColumn(String columnName, int destinationIndex) {
        moveColumn(getColumn(columnName), destinationIndex);
    }

    /**
     * Move column to index
     * @param destinationIndex the index to move
     * */
    public void moveColumn(Column column, int destinationIndex) {
        Asserts.validateIndex(destinationIndex, 0, getCountColumn());
        int colNum = column.getIndex();
        sheet.moveColumn(colNum, destinationIndex);
        Collections.swap(columnNames, colNum, destinationIndex);
    }

    /**
     * Update info column
     * */
    public void updateColumn(String colName, Column column) {
        throwColumnNotExist(colName);
        throwColumnExist(column);

        Column oldCol = getColumn(colName);
        int oldIndex = oldCol.getIndex();

        ColumnType colTypeNew = column.getType();
        Integer newIndex = column.getIndex();

        BatchUpdateBuilder builder = new BatchUpdateBuilder();

        boolean hasChangeName = !oldCol.getName().equals(column.getName());
        boolean hasChangeType= !colTypeNew.equals(oldCol.getType());
        boolean hasChangeIndex = newIndex != oldIndex;

        // change name + options
        builder.updateCells(cb -> cb.sheetId(getId())
                        .beginRow(0).endRow(getFrozenRow())
                        .beginCol(oldIndex).endColumn(oldIndex+1)
                        .editCell(0, oldIndex, c -> c.stringValue(column.getName()))
                        .editCell(1, oldIndex, c -> c.stringValue(column.toString()))
        );

        // change column type
        if(hasChangeType) {
            builder.repeatCell(cb -> cb.sheetId(getId())
                    .beginRow(getFrozenRow()).endRow(getMaxRow())
                    .beginCol(oldIndex).endCol(oldIndex + 1)
                    .cellFormat(cf -> cf.numberFormat(colTypeNew.getType(), colTypeNew.getPattern()))
            );
        }

        // change column index
        if(hasChangeIndex) {
            builder.moveDimension(Dimension.COLUMNS, getXslId(), getId(), oldIndex, newIndex);
        }

        // execute to server
        sheet.batchUpdate(builder);

        // update local
        if(hasChangeIndex) {
            Collections.swap(columnNames, oldIndex, newIndex);


        }





    }






















    private BatchUpdateBuilder buildAddColumns(List<Column> columns, BatchUpdateBuilder builder) {
        int beginCol = getCountColumn(), endCol = beginCol + columns.size();
        int frozenRow = getFrozenRow(), lastRow = getMaxRow();

        //insert column
        builder.insertDimension(d -> d.inheritFromBefore(true).dimension(Dimension.COLUMNS)
                .sheetId(getId()).startIndex(beginCol).endIndex(beginCol + columns.size())
        );

        //row column [name + option]
        UpdateCellsBuilder updateCellsBuilder = new UpdateCellsBuilder()
                .fields("userEnteredValue").sheetId(getId())
                .beginRow(0).endRow(frozenRow).beginCol(beginCol).endColumn(endCol);

        for (int colIndex = 0; colIndex < columns.size(); colIndex++) {
            Column column = columns.get(colIndex);

            // check column has exist
            if (isExistColumn(column)) {
                String msg = "The column [%s] exist in table [%s]";
                throw new DbException(msg, column.getName(), getName());
            }

            int colNum = beginCol + colIndex;

            // build
            updateCellsBuilder.getRow(0).getCell(colIndex).stringValue(column.getName());
            updateCellsBuilder.getRow(1).getCell(colIndex).stringValue(column.toString());

            ColumnType colType = column.getType();
            builder.repeatCell(cell -> cell.fields("userEnteredFormat.numberFormat").sheetId(getId())
                    .beginRow(frozenRow).endRow(lastRow).beginCol(beginCol).endCol(colNum + 1)
                    .cellFormat(f -> f.numberFormat(colType.getType(), colType.getPattern())));
        }

        // set UpdateCellsBuilder
        builder.updateCells(updateCellsBuilder);

        return builder;
    }

    @Override
    protected final SheetTb<E> parseColumnFromGrid() {
        super.parseColumnFromGrid();
        return this;
    }
}