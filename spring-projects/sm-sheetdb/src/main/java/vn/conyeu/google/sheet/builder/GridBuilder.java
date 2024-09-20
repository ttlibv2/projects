package vn.conyeu.google.sheet.builder;

import com.google.api.services.sheets.v4.model.DimensionProperties;
import com.google.api.services.sheets.v4.model.GridData;
import com.google.api.services.sheets.v4.model.RowData;
import vn.conyeu.commons.utils.Asserts;
import vn.conyeu.google.core.GoogleException;
import vn.conyeu.google.core.Utils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class GridBuilder implements XmlBuilder<GridData>, Iterable<RowBuilder> {
    private final SheetBuilder sheet;
    private final GridData grid;
    private final List<RowBuilder> rows;
    private List<DimensionProperties> columnMetadata;
    private List<DimensionProperties> rowMetadata;

    public GridBuilder(final SheetBuilder sheet, GridData data) {
        this.sheet = Asserts.notNull(sheet, "SheetBuilder");
        this.grid = Utils.getIfNull(data, GridData::new);
        this.rows = new ArrayList<>();
        this.initialize();
    }

    public void initialize() {
        List<RowData> rowData = Utils.setIfNull(grid::getRowData, ArrayList::new, grid::setRowData);
        for (RowData row : rowData) addRow(row);

        // columnMetadata
        this.columnMetadata = Utils.setIfNull(grid::getColumnMetadata, ArrayList::new, grid::setColumnMetadata);
        this.rowMetadata = Utils.setIfNull(grid::getRowMetadata, ArrayList::new, grid::setRowMetadata);
    }

    /**
     * Returns the sheet
     */
    public final SheetBuilder getSheet() {
        return sheet;
    }

    public GridData build() {
        grid.getRowData().clear();

        for(RowBuilder rowBuilder:rows) {
            grid.getRowData().add(rowBuilder.build());
        }

        return grid;
    }

    public GridBuilder copy() {
        return new GridBuilder(sheet, grid.clone());
    }

    public int getColumnSize() {
        return rows.stream().mapToInt(RowBuilder::size).max().orElse(columnMetadata.size());
    }

    public int getRowSize() {
        return rows.size();
    }

    public Integer getSheetId() {
        return getSheet().getSheetId();
    }


    public boolean isRowEmpty() {
        return rows.isEmpty();
    }

    public GridBuilder clear() {
        rows.clear();
        grid.getRowData().clear();
        return this;
    }

    public DimensionProperties getRowMetadata(int index) {
        int size = rowMetadata.size();
        if(index < 0 || index >= size) {
            int numRows = index < 0 ? 1 : index  - size + 1;
            addRowMetadatas(size, numRows);
        }
        return rowMetadata.get(index);
    }

    public GridBuilder hideRow(int index) {
        getRowMetadata(index).setHiddenByUser(true);
        return this;
    }

    /**
     * Delete row at index
     * @param index the index row to delete
     * */
    public GridBuilder removeRow(int index) {
        validateRowIndex(index);
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
        rowBuilder.editCells(consumer);
        return this;
    }

    /**
     * Returns row at index
     * @param index The index get row
     * @throws IndexOutOfBoundsException index invalid
     * */
    public RowBuilder findRow(int index) {
        validateRowIndex(index);
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
        //builder.fixCountCell(getColumnSize());
        rows.add(builder);
        return builder;
    }

    private void validateRowIndex(int index) {
        Asserts.validateIndex(index, 0, getRowSize());
    }

    public List<RowBuilder> getRows() {
        return List.copyOf(rows);
    }

    /**
     * Returns the range with the top left cell at the given coordinates.
     * @param row The row index of the cell to return; row indexing starts with 0.
     * @param column The column index of the cell to return; column indexing starts with 0.
     * */
    public XlRange getRange(int row, int column) {
        return getRange(row, column, 1, 1);
    }

    /**
     * Returns the range with the top left cell at the given coordinates, and with the given number of rows.
     * @param row The row index of the cell to return; row indexing starts with 0.
     * @param column The column index of the cell to return; column indexing starts with 0.
     * @param numRows     The number of rows to return.
     * */
    public XlRange getRange(int row, int column, int numRows) {
        return getRange(row, column, numRows, 1);
    }

    /**
     * Returns the range with the top left cell at the given coordinates with the given number of rows and columns.
     * @param row The row index of the cell to return; row indexing starts with 0.
     * @param column The column index of the cell to return; column indexing starts with 0.
     * @param numRows     The number of rows to return.
     * @param numColumns     The number of columns to return.
     * */
    public XlRange getRange(int row, int column, int numRows, int numColumns) {
        return new XlRange(this, row, column, row + numRows, column + numColumns);
    }

    /**
     * Returns the range as specified in A1 notation or R1C1 notation.
     * @param a1Notation The range to return, as specified in A1 notation or R1C1 notation.
     * */
    public XlRange getRange(String a1Notation) {
        //return new XlRange(this, row, column, numRows);
        throw new UnsupportedOperationException();
    }

    private void addRowMetadatas(int index, int numRows) {
        if(index < 0 || index > rowMetadata.size()) {
            throw new GoogleException("The index < 0 || > %s", rowMetadata.size());
        }

        for(int r=0;r<numRows;r++) {
            int pos = index + r;
            rowMetadata.add(pos, new DimensionProperties());
        }

    }

    public XlRange getColumn(int columnIndex, int numRows) {
        return getRange(0, columnIndex, numRows);
    }

    public XlRange findColumn(int columnIndex) {
        Asserts.validateIndex(columnIndex, 0, getColumnSize());
        return getRange(0, columnIndex, getRowSize());
    }

    public GridBuilder protectRow(int beginRow, int numRows, List<String> editorUser, String description) {
        sheet.protectAll(b -> b.forRow(getSheetId(), beginRow, beginRow + numRows).description(description).users(editorUser));
        return this;
    }
}