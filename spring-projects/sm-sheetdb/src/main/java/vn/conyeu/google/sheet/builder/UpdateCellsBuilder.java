package vn.conyeu.google.sheet.builder;

import com.google.api.services.sheets.v4.model.GridRange;
import com.google.api.services.sheets.v4.model.UpdateCellsRequest;
import vn.conyeu.google.core.FindList;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class UpdateCellsBuilder implements XmlBuilder<UpdateCellsRequest> {
    private final UpdateCellsRequest request;
    private final GridRange gridRange;
    private final FindList<RowBuilder> rows;

    public UpdateCellsBuilder() {
        request = new UpdateCellsRequest();
        gridRange = new GridRange();
        rows = new FindList<>(i -> new RowBuilder(null));
    }

    @Override
    public UpdateCellsRequest build() {
        request.setRows(new ArrayList<>());
        request.setRange(gridRange);

        for(RowBuilder rowBuilder:rows) {
            request.getRows().add(rowBuilder.build());
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
     * The end column (exclusive) of the range, or not set if unbounded.
     *
     * @param endColumnIndex endColumnIndex or {@code null} for none
     */
    public UpdateCellsBuilder endColumn(Integer endColumnIndex) {
        gridRange.setEndColumnIndex(endColumnIndex);
        return this;
    }

    /**
     * The end row (exclusive) of the range, or not set if unbounded.
     *
     * @param endRowIndex endRowIndex or {@code null} for none
     */
    public UpdateCellsBuilder endRow(Integer endRowIndex) {
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
    public UpdateCellsBuilder beginCol(Integer startColumnIndex) {
        gridRange.setStartColumnIndex(startColumnIndex);
        return this;
    }

    /**
     * The start row (inclusive) of the range, or not set if unbounded.
     *
     * @param startRowIndex startRowIndex or {@code null} for none
     */
    public UpdateCellsBuilder beginRow(Integer startRowIndex) {
        gridRange.setStartRowIndex(startRowIndex);
        return this;
    }

    /**
     * find or create new row at index
     * @param rowIndex 0-index
     * */
    public RowBuilder getRow(int rowIndex) {
        return rows.get(rowIndex);
    }

    /**
     * find or create new cell at index
     * @param rowIndex 0-index
     * @param columnIndex 0-index
     * */
    public CellBuilder getCell(int rowIndex, int columnIndex) {
        return getRow(rowIndex).getCell(columnIndex);
    }

    /**
     * find or create new cell at index
     * @param rowIndex 0-index
     * @param columnIndex 0-index
     * */
    public UpdateCellsBuilder editCell(int rowIndex, int columnIndex, Consumer<CellBuilder> consumerReturn) {
        consumerReturn.accept(getRow(rowIndex).getCell(columnIndex));
        return this;
    }



    public UpdateCellsBuilder addRow(Consumer<List<CellBuilder>> cells) {
        RowBuilder rowBuilder = getRow(rows.size());
        cells.accept(rowBuilder.getCells());
        return this;
    }

//    private UpdateCellsBuilder addRow(int countCell, Consumer<CellBuilder> consumerCell) {
//        RowData rowData = new RowData();
//        rowData.setValues(new ArrayList<>());
//        for(int pos=0;pos<countCell;pos++) {
//            CellBuilder builder = new CellBuilder(null);
//            consumerCell.accept(builder);
//            rowData.getValues().add(builder.build());
//        }
//
//        rows.add(rowData);
//        return this;
//    }
}