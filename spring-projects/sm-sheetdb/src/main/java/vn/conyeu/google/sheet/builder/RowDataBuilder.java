package vn.conyeu.google.sheet.builder;

import com.google.api.services.sheets.v4.model.CellData;
import com.google.api.services.sheets.v4.model.RowData;
import com.google.api.services.sheets.v4.model.TextFormat;
import vn.conyeu.commons.utils.Asserts;
import vn.conyeu.google.core.Utils;

import java.util.ArrayList;
import java.util.List;

public class RowDataBuilder implements XmlBuilder<RowData> {
    private final RowData row;
    private final SheetBuilder sheetBuilder;
    private final List<CellDataBuilder> cells;
    private Integer rowIndex;

    public RowDataBuilder(SheetBuilder sheetBuilder) {
        this(sheetBuilder, null);
    }

    public RowDataBuilder(SheetBuilder sheetBuilder, RowData model) {
        this.row = Utils.getIfNull(model, RowData::new);
        this.sheetBuilder = Asserts.notNull(sheetBuilder);
        this.cells = new ArrayList<>();
        this.initCellsBuilder();
    }

    public RowDataBuilder rowIndex(Integer rowIndex) {
        this.rowIndex = rowIndex;
        return this;
    }

    public int size() {
        return cells.size();
    }

    public boolean isEmpty() {
        return cells.isEmpty();
    }

    /**
     * Returns cell at index
     * @param cellIndex the index cell for get
     * */
    public CellDataBuilder getCell(int cellIndex) {
        tryAddCell(cellIndex);
        return cells.get(cellIndex);
    }

    /**
     * Update cell at index
     * @param cellIndex the index cell for update
     * @param consumer the custom cell
     * */
    public RowDataBuilder editCell(int cellIndex, ConsumerReturn<CellDataBuilder> consumer) {
        consumer.accept(getCell(cellIndex));
        return this;
    }

    /**
     * Update all cell
     * @param consumer the custom cell
     * */
    public RowDataBuilder editCell(ConsumerReturn<CellDataBuilder> consumer) {
        tryAddCell(sheetBuilder.getMaxColumn());
        for(CellDataBuilder cell:cells)consumer.accept(cell);
        return this;
    }

    public RowDataBuilder protect(ConsumerReturn<ProtectedRangeBuilder> consumer) {
        Integer sheetId = sheetBuilder.getSheetId();
        Asserts.notNull(sheetId, "SheetBuilder not set `sheetId`");
        Asserts.notNull(rowIndex, "RowDataBuilder not set `rowIndex`");
        sheetBuilder.protectedRange(r -> consumer.accept(r).forRow(sheetId, rowIndex));
        return this;
    }











    public RowData build() {
        row.getValues().clear();

        for(CellDataBuilder cell:cells){
            row.getValues().add(cell.build());
        }

        return row;
    }

    private void initCellsBuilder() {
        List<CellData> values = Utils.setIfNull(row::getValues, ArrayList::new, row::setValues);
        for(CellData cellData:values)cells.add(new CellDataBuilder(cellData));
    }

    private CellDataBuilder addCell() {
        CellDataBuilder builder = new CellDataBuilder(null);
        cells.add(builder);
        return builder;
    }

    private void tryAddCell(int rightPos) {
        Asserts.isTrue(rightPos >= 0, "@index");
        if(rightPos >= cells.size()) {
            for (int pos = cells.size(); pos <= rightPos; pos++) addCell();
        }
    }
}