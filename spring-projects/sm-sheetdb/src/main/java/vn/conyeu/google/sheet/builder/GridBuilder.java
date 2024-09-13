package vn.conyeu.google.sheet.builder;

import com.google.api.services.sheets.v4.model.GridData;
import com.google.api.services.sheets.v4.model.RowData;
import vn.conyeu.commons.utils.Asserts;
import vn.conyeu.google.core.GoogleException;
import vn.conyeu.google.core.Utils;
import vn.conyeu.google.xsldb.ColumnType;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class GridBuilder implements XmlBuilder<GridData>, Iterable<RowBuilder> {
    private final SheetBuilder sheet;
    private final GridData grid;
    private final List<RowBuilder> rows;
    private List<ColumnBuilder> columns;

    public GridBuilder(final SheetBuilder sheet, GridData data) {
        this.sheet = Asserts.notNull(sheet, "SheetBuilder");
        this.grid = Utils.getIfNull(data, GridData::new);
        this.rows = new ArrayList<>();
        this.columns = new ArrayList<>();
        this.initialize();
    }

    public void initialize() {
        List<RowData> rowData = Utils.setIfNull(grid::getRowData, ArrayList::new, grid::setRowData);
        for (RowData row : rowData) addRow(row);
    }

    /**
     * Returns the sheet
     */
    public final SheetBuilder getSheet() {
        return sheet;
    }

    public GridData build() {
        grid.getRowData().clear();

        // apply column
        int cellIndex = 0;
        for(ColumnBuilder columnBuilder:columns) {
            for(RowBuilder rowBuilder:rows) {
                ColumnType ct = columnBuilder.getType();
                rowBuilder.editCell(cellIndex++, c -> c.applyColumn(ct));
                grid.getRowData().add(rowBuilder.build());
            }
        }

        return grid;
    }

    public GridBuilder copy() {
        return new GridBuilder(sheet, grid.clone());
    }

    public int getRowSize() {
        return rows.size();
    }

    public boolean isRowEmpty() {
        return rows.isEmpty();
    }

    /**
     * Returns size cell
     */
    public int getColumnSize() {
        return columns.size();
    }

    public GridBuilder clear() {
        rows.clear();
        grid.getRowData().clear();
        return this;
    }

    /**
     * Delete row at index
     * @param index the index row to delete
     * */
    public GridBuilder removeRow(int index) {
        validateIndex(index);
        rows.remove(index);
        return this;
    }

    /**
     * Add row to grid
     *
     * @return RowBuilder
     */
    public RowBuilder addRow() {
        return addRow(new RowData());
    }

    /**
     * Add row starting at the given {@link #getRowSize()}.
     * @param howMany the number rows to add
     * @see #addRows(int, int)
     */
    public GridBuilder addRows(int howMany) {
        return addRows(getRowSize(), howMany);
    }

    /**
     * Add row starting at the given index.
     *
     * @param index   the index to add
     * @param howMany the number rows to add
     */
    public GridBuilder addRows(int index, int howMany) {
        int countRow = getRowSize(), count = howMany <= 0 ? 1 : howMany;
        if (index < 0 || index > countRow) index = countRow;
        for (int pos = index; pos < index + count; pos++) addRow();
        return this;
    }

    /**
     * Format row at index
     *
     * @param rowIndex    the row index
     * @param consumer the consumer custom format for all cell of row
     */
    public GridBuilder formatCells(int rowIndex, ConsumerReturn<CellFormatBuilder> consumer) {
        return editCells(rowIndex, cell -> cell.format(consumer));
    }

    /**
     * edit row at index
     *
     * @param rowIndex    the row index
     * @param consumer the custom edit for all cell of row
     */
    public GridBuilder editCells(int rowIndex, ConsumerReturn<CellBuilder> consumer) {
        RowBuilder rowBuilder = getRow(rowIndex, true);
        rowBuilder.fixCountCell(getColumnSize());
        rowBuilder.forEach(consumer::accept);
        return this;
    }

    /**
     * Returns row at index
     * @param index The index get row
     * @throws IndexOutOfBoundsException index invalid
     * */
    public RowBuilder findRow(int index) {
        validateIndex(index);
        return rows.get(index);
    }

    /**
     * Find or create new row
     * @param index The index get row
     * */
    public RowBuilder getRow(int index) {
        return getRow(index, true);
    }

    @Override
    public Iterator<RowBuilder> iterator() {
        return rows.iterator();
    }

    private RowBuilder getRow(int index, boolean hasCreate) {
        int countRow = getRowSize();
        if (index < 0 || index >= countRow) {
            if (hasCreate) addRows(index, index < 0 ? 1 : index - countRow + 1);
            else throw new GoogleException("The index invalid ( index < 0 || index >= %s)", countRow);
        }

        return rows.get(index);
    }

    public GridBuilder editRow(int index, ConsumerReturn<RowBuilder> consumer) {
        consumer.accept(getRow(index, true));
        return this;
    }

    private RowBuilder addRow(RowData row) {
        RowBuilder builder = new RowBuilder(this, row);
        builder.fixCountCell(getColumnSize());
        rows.add(builder);
        return builder;
    }

    private void validateIndex(int index) {
        Asserts.validateIndex(index, 0, getRowSize());
    }

    public List<RowBuilder> getRows() {
        return List.copyOf(rows);
    }

    public GridBuilder addColumn(String name, ConsumerReturn<ColumnBuilder> consumer) {
        return addColumn(c -> consumer.accept(c).name(name));
    }

    public GridBuilder addColumn(String name, ColumnType type, ConsumerReturn<ColumnBuilder> consumer) {
        return addColumn(c -> consumer.accept(c).name(name).type(type));
    }

    public GridBuilder addColumn(ConsumerReturn<ColumnBuilder> consumer) {
        ColumnBuilder builder = consumer.accept(new ColumnBuilder());
        builder.index(columns.size());

        ColumnType colType = builder.getType();

        // add column to row
        for(RowBuilder rowBuilder:rows) {
            rowBuilder.addCell();
        }

        columns.add(builder);
        return this;
    }

    /**
     * Returns the range with the top left cell at the given coordinates.
     * @param row The row index of the cell to return; row indexing starts with 1.
     * @param column The column index of the cell to return; column indexing starts with 1.
     * */
    public XlRange getRange(Integer row, Integer column) {
        return new XlRange(this, row, column, row, column);
    }

    /**
     * Returns the range with the top left cell at the given coordinates, and with the given number of rows.
     * @param row The row index of the cell to return; row indexing starts with 1.
     * @param column The column index of the cell to return; column indexing starts with 1.
     * @param numRows 	The number of rows to return.
     * */
    public XlRange getRange(Integer row, Integer column, Integer numRows) {
        return new XlRange(this, row, column, row + numRows - 1, column);
    }

    /**
     * Returns the range with the top left cell at the given coordinates with the given number of rows and columns.
     * @param row The row index of the cell to return; row indexing starts with 1.
     * @param column The column index of the cell to return; column indexing starts with 1.
     * @param numRows 	The number of rows to return.
     * @param numColumns 	The number of columns to return.
     * */
    public XlRange getRange(Integer row, Integer column, Integer numRows, Integer numColumns) {
        return new XlRange(this, row, column, row + numRows - 1, column + numColumns - 1);
    }

    /**
     * Returns the range as specified in A1 notation or R1C1 notation.
     * @param a1Notation The range to return, as specified in A1 notation or R1C1 notation.
     * */
    public XlRange getRange(String a1Notation) {
        //return new XlRange(this, row, column, numRows);
        throw new UnsupportedOperationException();
    }

}