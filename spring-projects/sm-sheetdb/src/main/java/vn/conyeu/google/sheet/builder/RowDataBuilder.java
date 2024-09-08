package vn.conyeu.google.sheet.builder;

import com.google.api.services.sheets.v4.model.CellData;
import com.google.api.services.sheets.v4.model.RowData;
import vn.conyeu.commons.utils.Asserts;
import vn.conyeu.google.core.Utils;

import java.util.ArrayList;
import java.util.List;

public class RowDataBuilder implements XmlBuilder<RowData> {
    private final RowData row;
    private final List<CellDataBuilder> cells;

    public RowDataBuilder(RowData row) {
        this.row = Utils.getIfNull(row, RowData::new);
        this.cells = new ArrayList<>();
        this.initCellsBuilder();
    }

    public int size() {
        return cells.size();
    }

    public boolean isEmpty() {
        return cells.isEmpty();
    }

    public CellDataBuilder getCell(int index) {
        tryAddCell(index);
        return cells.get(index);
    }

    public RowDataBuilder editCell(int index, ConsumerReturn<CellDataBuilder> consumer) {
        consumer.accept(getCell(index));
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

    public CellDataBuilder addCell() {
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