package vn.conyeu.google.sheet.builder;

import vn.conyeu.google.core.GoogleException;

import java.util.List;

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
     * Sets the value of the range. The value can be numeric, string, boolean or date. If it begins with '=' it is interpreted as a formula.
     *
     * @param value The value for the range.
     */
    public XlRange setValue(Object value) {
        for (int r = beginRow - 1; r < endRow; r++) {
            for (int c = beginCol - 1; c < endCol; c++) {
                CellBuilder cell = grid.getRow(r).getCell(c);
                cell.setValue(value);
            }
        }
        return this;
    }

    /**
     * Sets a rectangular grid of values (must match dimensions of this range). If a value begins with =, it's interpreted as a formula.
     *
     * @param value The value for the range.
     */
    public XlRange setValues(List<List<Object>> value) {
        int numRows = endRow - beginRow + 1;
        int numCols = endCol - beginCol + 1;

        // validate numRows + value.size()
        if (numRows != value.size()) {
            String msg = "The number of rows in the data does not match the number of rows in the range. The data has %s but the range has %s";
            throw new GoogleException(msg, value.size(), numRows);
        }

        for (int r = 0; r < value.size(); r++) {
            List<Object> rowValue = value.get(r);

        }


        for (int r = beginRow - 1; r < endRow; r++) {


            for (int c = beginCol - 1; c < endCol; c++) {


                CellBuilder cell = grid.getRow(r).getCell(c);

                cell.setValue(value);
            }
        }
        return this;
    }


}