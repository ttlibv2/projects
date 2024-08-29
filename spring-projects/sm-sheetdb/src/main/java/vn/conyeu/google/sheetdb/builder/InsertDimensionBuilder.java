package vn.conyeu.google.sheetdb.builder;

import com.google.api.services.sheets.v4.model.DimensionRange;
import com.google.api.services.sheets.v4.model.InsertDimensionRequest;

public class InsertDimensionBuilder implements XmlBuilder<InsertDimensionRequest> {
    private final InsertDimensionRequest request = new InsertDimensionRequest();
    private DimensionRange dimensionRange = new DimensionRange();


    @Override
    public InsertDimensionRequest build() {
        request.setRange(dimensionRange);
        return request;
    }

    /**
     * Whether dimension properties should be extended from the dimensions before or after the newly
     * inserted dimensions. True to inherit from the dimensions before (in which case the start index
     * must be greater than 0), and false to inherit from the dimensions after.
     * <p>
     * For example, if row index 0 has red background and row index 1 has a green background, then
     * inserting 2 rows at index 1 can inherit either the green or red background.  If
     * `inheritFromBefore` is true, the two new rows will be red (because the row before the insertion
     * point was red), whereas if `inheritFromBefore` is false, the two new rows will be green
     * (because the row after the insertion point was green).
     *
     * @param inheritFromBefore inheritFromBefore or {@code null} for none
     */
    public InsertDimensionBuilder inheritFromBefore(Boolean inheritFromBefore) {
        request.setInheritFromBefore(inheritFromBefore);
        return this;
    }

    /**
     * The dimension of the span.
     *
     * @param dimension dimension or {@code null} for none
     */
    public InsertDimensionBuilder dimension(Dimension dimension) {
        dimensionRange.setDimension(dimension.name());
        return this;
    }

    /**
     * The end (exclusive) of the span, or not set if unbounded.
     *
     * @param endIndex endIndex or {@code null} for none
     */
    public InsertDimensionBuilder endIndex(Integer endIndex) {
        dimensionRange.setEndIndex(endIndex);
        return this;
    }

    /**
     * The sheet this span is on.
     *
     * @param sheetId sheetId or {@code null} for none
     */
    public InsertDimensionBuilder sheetId(Integer sheetId) {
        dimensionRange.setSheetId(sheetId);
        return this;
    }

    /**
     * The start (inclusive) of the span, or not set if unbounded.
     *
     * @param startIndex startIndex or {@code null} for none
     */
    public InsertDimensionBuilder startIndex(Integer startIndex) {
        dimensionRange.setStartIndex(startIndex);
        return this;
    }
}