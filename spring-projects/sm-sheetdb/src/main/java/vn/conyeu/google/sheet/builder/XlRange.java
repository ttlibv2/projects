package vn.conyeu.google.sheet.builder;

import vn.conyeu.google.core.GoogleException;

import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class XlRange {
    private final GridBuilder grid;
    private Integer beginRow, endRow;
    private Integer beginCol, endCol;

    XlRange(GridBuilder grid, Integer rowIndex, Integer beginCol, Integer endRow, Integer endCol) {
        this.grid = grid;
        this.beginRow = rowIndex;
        this.beginCol = beginCol;
        this.endRow = endRow;
        this.endCol = endCol;
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

    /**
     * Sets the number or date format to the given formatting string.
     * The accepted format patterns are described in the Sheets API documentation.
     *
     * @param numberFormat A number format string.
     */
    public XlRange setNumberFormats(String numberFormat) {
        return setCellCb(cell -> cell.numberFormat(numberFormat));
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
     * @param size 	A font size in point size.
     * */
    public XlRange setFontSize(Integer size) {
        return setCellCb(cell -> cell.fontSize(size));
    }

    /**
     * Sets a rectangular grid of font sizes (must match dimensions of this range). The sizes are in points.
     * @param sizes	A font size in point size.
     * */
    public XlRange setFontSize(List<List<Integer>> sizes	) {
        return setCellCb(sizes	, CellBuilder::fontSize);
    }

    public XlRange setItalic(boolean italic) {
        return setCellCb(cell -> cell.italic(italic));
    }

    public XlRange setItalic(List<List<Boolean>> italics	) {
        return setCellCb(italics, CellBuilder::italic);
    }

    public XlRange bold(boolean bold) {
        return setCellCb(cell -> cell.bold(bold));
    }

    public XlRange bolds(List<List<Boolean>> bolds	) {
        return setCellCb(bolds, CellBuilder::bold);
    }










    private <E> XlRange setCellCb(Consumer<CellBuilder> consumer) {
        for (int r = beginRow - 1; r < endRow; r++) {
            for (int c = beginCol - 1; c < endCol; c++) {
                consumer.accept(grid.getRow(r).getCell(c));
            }
        }
        return this;
    }

    private <E> XlRange setCellCb(List<List<E>> data, BiConsumer<CellBuilder, E> consumer) {
        int numRows = endRow - beginRow + 1;
        int numCols = endCol - beginCol + 1;

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
}