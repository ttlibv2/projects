package vn.conyeu.google.sheet.builder;

import com.google.api.services.sheets.v4.model.GridRange;
import vn.conyeu.google.core.Utils;

public class GridRangeBuilder implements XmlBuilder<GridRange> {
    private final GridRange range;

    public GridRangeBuilder(GridRange range) {
        this.range = Utils.getIfNull(range, GridRange::new);
    }

    @Override
    public GridRange build() {
        return range;
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
     * The end column (exclusive) of the range, or not set if unbounded.
     *
     * @param endColumnIndex endColumnIndex or {@code null} for none
     */
    public GridRangeBuilder endColumn(Integer endColumnIndex) {
        range.setEndColumnIndex(endColumnIndex);
        return this;
    }

    /**
     * The end row (exclusive) of the range, or not set if unbounded.
     *
     * @param endRowIndex endRowIndex or {@code null} for none
     */
    public GridRangeBuilder endRow(Integer endRowIndex) {
        range.setEndRowIndex(endRowIndex);
        return this;
    }


    /**
     * The start column (inclusive) of the range, or not set if unbounded.
     *
     * @param startColumnIndex startColumnIndex or {@code null} for none
     */
    public GridRangeBuilder beginColumn(Integer startColumnIndex) {
        range.setStartColumnIndex(startColumnIndex);
        return this;
    }

    /**
     * The start row (inclusive) of the range, or not set if unbounded.
     *
     * @param startRowIndex startRowIndex or {@code null} for none
     */
    public GridRangeBuilder beginRow(Integer startRowIndex) {
        range.setStartRowIndex(startRowIndex);
        return this;
    }
}