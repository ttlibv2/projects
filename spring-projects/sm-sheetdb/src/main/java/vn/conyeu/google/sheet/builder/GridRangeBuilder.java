package vn.conyeu.google.sheet.builder;

import com.google.api.services.sheets.v4.model.GridRange;

public class GridRangeBuilder implements XmlBuilder<GridRange> {
    private final GridRange range = new GridRange();

    @Override
    public GridRange build() {
        return range;
    }

    /**
     * The end column (exclusive) of the range, or not set if unbounded.
     *
     * @param endColumnIndex endColumnIndex or {@code null} for none
     */
    public GridRangeBuilder endColumnIndex(Integer endColumnIndex) {
        range.setEndColumnIndex(endColumnIndex);
        return this;
    }

    /**
     * The end row (exclusive) of the range, or not set if unbounded.
     *
     * @param endRowIndex endRowIndex or {@code null} for none
     */
    public GridRangeBuilder endRowIndex(Integer endRowIndex) {
        range.setEndRowIndex(endRowIndex);
        return this;
    }

    /**
     * The sheet this range is on.
     *
     * @param sheetId sheetId or {@code null} for none
     */
    public GridRangeBuilder sheetId(Integer sheetId) {
        range.setSheetId(sheetId);
        return this;
    }

    /**
     * The start column (inclusive) of the range, or not set if unbounded.
     *
     * @param startColumnIndex startColumnIndex or {@code null} for none
     */
    public GridRangeBuilder startColumnIndex(Integer startColumnIndex) {
        range.setStartColumnIndex(startColumnIndex);
        return this;
    }

    /**
     * The start row (inclusive) of the range, or not set if unbounded.
     *
     * @param startRowIndex startRowIndex or {@code null} for none
     */
    public GridRangeBuilder startRowIndex(Integer startRowIndex) {
        range.setStartRowIndex(startRowIndex);
        return this;
    }
}