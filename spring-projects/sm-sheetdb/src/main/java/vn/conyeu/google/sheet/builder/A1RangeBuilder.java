package vn.conyeu.google.sheet.builder;

import static vn.conyeu.google.sheet.builder.SheetUtil.*;

public final class A1RangeBuilder implements XmlBuilder<String> {
    private String sheetName;
    private int firstRow;
    private int firstCol;
    private int lastRow;
    private int lastCol;
    private boolean useAbs;

    public static A1RangeBuilder sheetName(String name) {
        return new A1RangeBuilder().name(name);
    }

    /**
     * Creates a CellRangeAddress from a cell range reference string.
     *
     * @param ref usually a standard area ref (e.g. "B1:D8").  May be a single
     *            cell ref (e.g. "B5") in which case the result is a 1 x 1 cell
     *            range. May also be a whole row range (e.g. "3:5"), or a whole
     *            column range (e.g. "C:F")
     */
    public static A1RangeBuilder valueOf(String ref) {
        int sep = ref.indexOf(':');
        CellReference a;
        CellReference b;
        if (sep == -1) {
            a = new CellReference(ref);
            b = a;
        } else {
            a = new CellReference(ref.substring(0, sep));
            b = new CellReference(ref.substring(sep + 1));
        }

        return A1RangeBuilder.sheetName(a.getSheetName())
                .firstRow(a.getRow()).firstCol(a.getCol())
                .lastRow(b.getRow()).lastCol(b.getCol());

    }

    @Override
    public A1RangeBuilder copy() {
        return A1RangeBuilder.sheetName(sheetName)
                .firstRow(firstRow).firstCol(firstCol)
                .lastRow(lastRow).lastCol(lastCol)
                .useAbsolute(useAbs);
    }

    /**
     * Set the sheetName
     *
     * @param sheetName the value
     */
    public A1RangeBuilder name(String sheetName) {
        this.sheetName = sheetName;
        return this;
    }

    /**
     * Set the firstRow
     *
     * @param firstRow the value
     */
    public A1RangeBuilder firstRow(int firstRow) {
        this.firstRow = firstRow;
        return this;
    }

    /**
     * Set the firstCol
     *
     * @param firstCol the value
     */
    public A1RangeBuilder firstCol(int firstCol) {
        this.firstCol = firstCol;
        return this;
    }

    /**
     * Set the lastRow
     *
     * @param lastRow the value
     */
    public A1RangeBuilder lastRow(int lastRow) {
        this.lastRow = lastRow;
        return this;
    }

    /**
     * Set the lastCol
     *
     * @param lastCol the value
     */
    public A1RangeBuilder lastCol(int lastCol) {
        this.lastCol = lastCol;
        return this;
    }

    /**
     * Set the useAbs
     *
     * @param useAbs the value
     */
    public A1RangeBuilder useAbsolute(boolean useAbs) {
        this.useAbs = useAbs;
        return this;
    }

    /**
     * @return the text format of this range using specified sheet name.
     */
    public String formatAsString() {

        if (lastRow < firstRow || lastCol < firstCol) {
            throw new IllegalArgumentException("Invalid cell range, having lastRow < firstRow || lastCol < firstCol, " +
                    "had rows " + lastRow + " >= " + firstRow + " or cells " + lastCol + " >= " + firstCol);
        }

        StringBuilder sb = new StringBuilder();

        if (sheetName != null) {
            sb.append(SheetNameFormatter.format(sheetName));
            sb.append(SHEET_NAME_DELIMITER);
        }

        CellReference cellRefFrom = new CellReference(firstRow, firstCol, useAbs, useAbs);
        sb.append(cellRefFrom.formatAsString());

        //for a single-cell reference return A1 instead of A1:A1
        //for full-column ranges or full-row ranges return A:A instead of A,
        //and 1:1 instead of 1
        CellReference cellRefTo = new CellReference(lastRow, lastCol, useAbs, useAbs);
        if(!cellRefFrom.equals(cellRefTo)){
            sb.append(':');
            sb.append(cellRefTo.formatAsString());
        }

        return sb.toString();
    }















    @Override
    public String build() {
        return formatAsString();
    }



}