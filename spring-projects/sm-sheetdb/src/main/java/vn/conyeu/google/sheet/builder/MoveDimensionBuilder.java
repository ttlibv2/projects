package vn.conyeu.google.sheet.builder;

import com.google.api.services.sheets.v4.model.DimensionRange;
import com.google.api.services.sheets.v4.model.MoveDimensionRequest;

public class MoveDimensionBuilder implements XmlBuilder<MoveDimensionRequest> {
    private final MoveDimensionRequest request = new MoveDimensionRequest();
    private final DimensionRange dimensionRange = new DimensionRange();

    @Override
    public MoveDimensionRequest build() {
        request.setSource(dimensionRange);
        return request;
    }

    /**
     * The zero-based start index of where to move the source data to, based on the coordinates
     * *before* the source data is removed from the grid.  Existing data will be shifted down or right
     * (depending on the dimension) to make room for the moved dimensions. The source dimensions are
     * removed from the grid, so the the data may end up in a different index than specified.
     * <p>
     * For example, given `A1..A5` of `0, 1, 2, 3, 4` and wanting to move `"1"` and `"2"` to between
     * `"3"` and `"4"`, the source would be `ROWS [1..3)`,and the destination index would be `"4"`
     * (the zero-based index of row 5). The end result would be `A1..A5` of `0, 3, 1, 2, 4`.
     *
     * @param destinationIndex destinationIndex or {@code null} for none
     */
    public MoveDimensionBuilder destinationIndex(Integer destinationIndex) { request.setDestinationIndex(destinationIndex); return this;
    }

    /**
     * The dimension of the span.
     *
     * @param dimension dimension or {@code null} for none
     */
    public MoveDimensionBuilder dimension(Dimension dimension) { dimensionRange.setDimension(dimension.name()); return this;
    }

    /**
     * The end (exclusive) of the span, or not set if unbounded.
     *
     * @param endIndex endIndex or {@code null} for none
     */
    public MoveDimensionBuilder endIndex(Integer endIndex) { dimensionRange.setEndIndex(endIndex); return this;
    }

    /**
     * The sheet this span is on.
     *
     * @param sheetId sheetId or {@code null} for none
     */
    public MoveDimensionBuilder sheetId(Integer sheetId) { dimensionRange.setSheetId(sheetId); return this;
    }

    /**
     * The start (inclusive) of the span, or not set if unbounded.
     *
     * @param startIndex startIndex or {@code null} for none
     */
    public MoveDimensionBuilder startIndex(Integer startIndex) { dimensionRange.setStartIndex(startIndex); return this;
    }
}