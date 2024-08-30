package vn.conyeu.google.sheet.builder;

import com.google.api.services.sheets.v4.model.GridRange;
import com.google.api.services.sheets.v4.model.RowData;
import com.google.api.services.sheets.v4.model.UpdateCellsRequest;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;

public class UpdateCellsBuilder implements XmlBuilder<UpdateCellsRequest> {
    private final UpdateCellsRequest request = new UpdateCellsRequest();
    private GridRange gridRange = new GridRange();
    private final Set<String> fields = new HashSet<>();
    private List<RowData> rows = new ArrayList<>();

    @Override
    public UpdateCellsRequest build() {
        request.setRange(gridRange);

        if (!rows.isEmpty()) {
            request.setRows(rows);
        }

        if (!fields.isEmpty()) {
            request.setFields(String.join(",", fields));
        }

        return request;
    }

    /**
     * The fields of CellData that should be updated. At least one field must be specified. The root
     * is the CellData; 'row.values.' should not be specified. A single `"*"` can be used as short-
     * hand for listing every field.
     *
     * @param fields fields or {@code null} for none
     */
    public UpdateCellsBuilder fields(String fields) {
        request.setFields(fields);
        return this;
    }

    /**
     * The range to write data to.
     * <p>
     * If the data in rows does not cover the entire requested range, the fields matching those set in
     * fields will be cleared.
     *
     * @param range range or {@code null} for none
     */
    public UpdateCellsBuilder range(GridRange range) {
        gridRange = range == null ? new GridRange(): range;
        return this;
    }

    /**
     * The end column (exclusive) of the range, or not set if unbounded.
     *
     * @param endColumnIndex endColumnIndex or {@code null} for none
     */
    public UpdateCellsBuilder endColumnIndex(Integer endColumnIndex) {
        gridRange.setEndColumnIndex(endColumnIndex);
        return this;
    }

    /**
     * The end row (exclusive) of the range, or not set if unbounded.
     *
     * @param endRowIndex endRowIndex or {@code null} for none
     */
    public UpdateCellsBuilder endRowIndex(Integer endRowIndex) {
        gridRange.setEndRowIndex(endRowIndex);
        return this;
    }

    /**
     * The sheet this range is on.
     *
     * @param sheetId sheetId or {@code null} for none
     */
    public UpdateCellsBuilder sheetId(Integer sheetId) {
        gridRange.setSheetId(sheetId);
        return this;
    }

    /**
     * The start column (inclusive) of the range, or not set if unbounded.
     *
     * @param startColumnIndex startColumnIndex or {@code null} for none
     */
    public UpdateCellsBuilder startColumnIndex(Integer startColumnIndex) {
        gridRange.setStartColumnIndex(startColumnIndex);
        return this;
    }

    /**
     * The start row (inclusive) of the range, or not set if unbounded.
     *
     * @param startRowIndex startRowIndex or {@code null} for none
     */
    public UpdateCellsBuilder startRowIndex(Integer startRowIndex) {
        gridRange.setStartRowIndex(startRowIndex);
        return this;
    }

    public UpdateCellsBuilder addRow(int countCell, Consumer<CellDataBuilder> consumerCell) {
        RowData rowData = new RowData();
        rowData.setValues(new ArrayList<>());
        for(int pos=0;pos<countCell;pos++) {
            CellDataBuilder builder = new CellDataBuilder(null);
            consumerCell.accept(builder);
            rowData.getValues().add(builder.build());
        }

        return this;
    }
}