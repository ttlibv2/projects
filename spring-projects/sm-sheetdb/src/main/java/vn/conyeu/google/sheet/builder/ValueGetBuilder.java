package vn.conyeu.google.sheet.builder;

import vn.conyeu.commons.utils.Asserts;

public class ValueGetBuilder {
    private final A1RangeBuilder range = new A1RangeBuilder();
    private ValueRenderOption valueRenderOption = ValueRenderOption.FORMATTED_VALUE;
    private DateTimeRenderOption dateTimeRenderOption = DateTimeRenderOption.FORMATTED_STRING;
    private Dimension majorDimension = Dimension.ROWS;
    private String fields = "*";

    public static ValueGetBuilder withRow() {
        return new ValueGetBuilder().majorDimension(Dimension.ROWS);
    }

    public static ValueGetBuilder withColumn() {
        return new ValueGetBuilder().majorDimension(Dimension.COLUMNS);
    }

    public String buildRange() {
        return Asserts.notBlank(range.build(), "The A1 notation or R1C1 notation of the range to retrieve values from.");
    }

    /**
     * Set the sheetName
     * @param sheetName the value
     */
    public A1RangeBuilder sheetName(String sheetName) {
        return range.sheetName(sheetName);
    }

    /**
     * Set the firstRow
     * @param firstRow the value
     */
    public A1RangeBuilder firstRow(int firstRow) {
        return range.firstRow(firstRow);
    }

    /**
     * Set the firstCol
     * @param firstCol the value
     */
    public A1RangeBuilder firstCol(int firstCol) {
        return range.firstCol(firstCol);
    }

    /**
     * Set the lastRow
     * @param lastRow the value
     */
    public A1RangeBuilder lastRow(int lastRow) {
        return range.lastRow(lastRow);
    }

    /**
     * Set the lastCol
     * @param lastCol the value
     */
    public A1RangeBuilder lastCol(int lastCol) {
        return range.lastCol(lastCol);
    }

    /**
     * Set the useAbsolute
     * @param useAbsolute the value
     */
    public A1RangeBuilder useAbsolute(boolean useAbsolute) {
        return range.useAbsolute(useAbsolute);
    }

    /**
     * Set the majorDimension
     * @param majorDimension the value
     */
    public ValueGetBuilder majorDimension(Dimension majorDimension) {
        this.majorDimension = majorDimension;
        return this;
    }

    /**
     * Set the valueRenderOption
     * @param valueRenderOption the value
     */
    public ValueGetBuilder valueRenderOption(ValueRenderOption valueRenderOption) {
        this.valueRenderOption = valueRenderOption;
        return this;
    }

    /**
     * Set the dateTimeRenderOption
     * @param dateTimeRenderOption the value
     */
    public ValueGetBuilder dateTimeRenderOption(DateTimeRenderOption dateTimeRenderOption) {
        this.dateTimeRenderOption = dateTimeRenderOption;
        return this;
    }

    /**
     * Set the fields
     * @param fields the value
     */
    public ValueGetBuilder fields(String fields) {
        this.fields = fields;
        return this;
    }

    public String getValueOption() {
        return valueRenderOption.name();
    }

    public String getDateTimeOption() {
        return dateTimeRenderOption.name();
    }

    public String getDimension() {
        return majorDimension.name();
    }

    public String getFields() {
        return fields;
    }
}