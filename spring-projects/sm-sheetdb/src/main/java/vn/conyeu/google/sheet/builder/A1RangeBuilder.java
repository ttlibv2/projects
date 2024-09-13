package vn.conyeu.google.sheet.builder;

import vn.conyeu.commons.utils.Objects;

public final class A1RangeBuilder implements XmlBuilder<String> {
    private String sheetName;
    private int firstRow;
    private int firstCol;
    private int lastRow;
    private int lastCol;
    private boolean useAbsolute;


    public static A1RangeBuilder name(String name) {
        return new A1RangeBuilder().sheetName(name);
    }

    /**
     * Set the sheetName
     *
     * @param sheetName the value
     */
    public A1RangeBuilder sheetName(String sheetName) {
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
     * Set the useAbsolute
     *
     * @param useAbsolute the value
     */
    public A1RangeBuilder useAbsolute(boolean useAbsolute) {
        this.useAbsolute = useAbsolute;
        return this;
    }

    @Override
    public String build() {
        StringBuilder sb = new StringBuilder();

        if (Objects.notBlank(sheetName)) {
            sb.append(sheetName).append("!");
        }

        appendFormatCell(sb, firstCol, firstRow);
        appendFormatCell(sb, lastCol, lastRow);
        return sb.toString();
    }

    private void appendFormatCell(StringBuilder sb, int colPos, int rowPos) {
        if(colPos >=0) {
            if(useAbsolute)sb.append("$");
            sb.append(SheetUtil.numberToLetter(colPos));
        }

        if(rowPos >=0) {
            if(useAbsolute)sb.append("$");
            sb.append(rowPos + 1);
        }
    }
}