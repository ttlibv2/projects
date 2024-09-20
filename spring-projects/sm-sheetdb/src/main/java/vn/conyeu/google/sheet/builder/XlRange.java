package vn.conyeu.google.sheet.builder;

import vn.conyeu.commons.utils.Asserts;
import vn.conyeu.google.core.GoogleException;

import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class XlRange {
    private final GridBuilder grid;
    private final Integer beginRow, endRow;
    private final Integer beginCol, endCol;

    XlRange(GridBuilder grid, Integer beginRow, Integer beginCol, Integer endRow, Integer endCol) {
        this.grid = grid;
        this.beginRow = beginRow;
        this.beginCol = beginCol;
        this.endRow = endRow;
        this.endCol = endCol;
    }

    public XlRange applyDefaultCell() {
        ConsumerReturn<CellFormatBuilder> consumer = grid.getSheet().getDefaultFormat();
        if (consumer != null) setCellCb(cb -> cb.format(consumer));
        return this;
    }

    /**
     * Returns the end row position.
     */
    public Integer getLastRow() {
        return endRow;
    }

    /**
     * Returns the end column position.
     */
    public Integer getLastColumn() {
        return endCol;
    }

    /**
     * Returns the beginCol
     */
    public Integer getFirstColumn() {
        return beginCol;
    }

    /**
     * Returns the beginRow
     */
    public Integer getFirstRow() {
        return beginRow;
    }

    /**
     * Sets the value of the range. The value can be numeric, string, boolean or date.
     * If it begins with '=' it is interpreted as a formula.
     *
     * @param value The value for the range.
     */
    public XlRange setValue(Object value) {
        return setCellCb(cell -> cell.setValue(value));
    }

    /**
     * Sets a rectangular grid of values (must match dimensions of this range).
     * If a value begins with =, it's interpreted as a formula.
     *
     * @param data The value for the range.
     */
    public XlRange setValues(List<List<Object>> data) {
        return setCellCb(data, CellBuilder::setValue);
    }

    public XlRange setValues(int columnIndex, Object... data) {

        int numCols = endCol - beginCol;
        int numRows = endRow - beginRow;

        // validate numRows + value.size()
        validateRowSize(numRows, data.length);

        // validate numCols + columnIndex
        Asserts.validateIndex(columnIndex, 0, numCols);

        // loop set value
        for (int r = 0; r < data.length; r++) {
            int cellIndex = columnIndex + beginCol;
            grid.getRow(r).getCell(cellIndex)
                    .setValue(data[r]);
        }

        return this;
    }

    /**
     * Sets the number or date format to the given formatting string.
     * The accepted format patterns are described in the Sheets API documentation.
     *
     * @param type A number format string.
     */
    public XlRange setNumberFormat(NumberFormatType type, String pattern) {
        return setCellCb(cell -> cell.numberFormat(type, pattern));
    }

    /**
     * Sets a rectangular grid of number or date formats (must match dimensions of this range).
     * The values are format pattern strings as described in the Sheets API documentation.
     *
     * @param numberFormats list of number formats.
     */
    public XlRange setNumberFormats(List<List<String>> numberFormats) {
        return setCellCb(numberFormats, CellBuilder::numberFormat);
    }

    /**
     * Sets the font size, with the size being the point size to use.
     *
     * @param size A font size in point size.
     */
    public XlRange setFontSize(Integer size) {
        return setCellCb(cell -> cell.fontSize(size));
    }

    /**
     * Sets a rectangular grid of font sizes (must match dimensions of this range). The sizes are in points.
     *
     * @param sizes A font size in point size.
     */
    public XlRange setFontSize(List<List<Integer>> sizes) {
        return setCellCb(sizes, CellBuilder::fontSize);
    }

    public XlRange setItalic(boolean italic) {
        return setCellCb(cell -> cell.italic(italic));
    }

    public XlRange setItalic(List<List<Boolean>> italics) {
        return setCellCb(italics, CellBuilder::italic);
    }

    public XlRange setBold(boolean bold) {
        return setCellCb(cell -> cell.bold(bold));
    }

    public XlRange setBolds(List<List<Boolean>> bolds) {
        return setCellCb(bolds, CellBuilder::bold);
    }


    //----------------------------

    private <E> XlRange setCellCb(Consumer<CellBuilder> consumer) {
        for (int r = beginRow; r < endRow; r++) {
            for (int c = beginCol; c < endCol; c++) {
                consumer.accept(grid.getRow(r).getCell(c));
            }
        }
        return this;
    }

    private <E> XlRange setCellCb(List<List<E>> data, BiConsumer<CellBuilder, E> consumer) {
        int numRows = endRow - beginRow;
        int numCols = endCol - beginCol;

        // validate numRows + value.size()
        validateRowSize(numRows, data.size());

        for (int r = 0; r < data.size(); r++) {
            List<E> rowValue = data.get(r);
            validateColumnSize(numCols, rowValue.size());

            // loop columns
            int rows = beginRow + r;
            for (int c = 0; c < rowValue.size(); c++) {
                int columns = beginCol + c;
                CellBuilder cell = grid.getRow(rows).getCell(columns);
                consumer.accept(cell, rowValue.get(columns));
            }

        }

        return this;
    }

    private void validateColumnSize(int numCols, int size) {
        if (numCols != size) {
            String msg = "The number of columns in the data does not match the number of columns in the range. The data has %s but the range has %s";
            throw new GoogleException(msg, size, numCols);
        }
    }

    private void validateRowSize(int numRows, int size) {
        if (numRows != size) {
            String msg = "The number of rows in the data does not match the number of rows in the range. The data has %s but the range has %s";
            throw new GoogleException(msg, size, numRows);
        }
    }

    public void protect(String editorUser, String description) {
        grid.getSheet().protectAll(p -> p.users(editorUser).description(description)
                .range(r -> r.beginRow(beginRow).endRow(endRow)
                        .beginColumn(beginCol).endColumn(endCol)));
    }

    public String findValueAt(int rowIndex) {
        int rowPos = beginRow + rowIndex;
        return grid.findRow(rowPos).findCell(beginCol).getValue();
    }
}