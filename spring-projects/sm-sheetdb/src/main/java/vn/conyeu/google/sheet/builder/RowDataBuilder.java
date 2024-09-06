package vn.conyeu.google.sheet.builder;

import com.google.api.services.sheets.v4.model.CellData;
import com.google.api.services.sheets.v4.model.RowData;

import java.util.ArrayList;
import java.util.List;

public class RowDataBuilder implements XmlBuilder<RowData> {
    private final RowData row;

    public RowDataBuilder(RowData row) {
        this.row = initRow(row);
    }

    @Override
    public RowData build() {
        return row;
    }

    public CellDataBuilder getCell(int cellPos) {
        CellData cellData;

        if(cellPos >= cellSize()) {
            cellData = new CellData();
            row.getValues().add(cellData);
        }
        else cellData = row.getValues().get(cellPos);
        return new CellDataBuilder(cellData);
    }

    public RowDataBuilder addCell(ConsumerReturn<CellDataBuilder> cell) {
        CellDataBuilder builder = cell.accept(new CellDataBuilder(null));
        row.getValues().add(builder.build());
        return this;
    }

    public boolean isEmptyCell() {
        return row.getValues().isEmpty();
    }

    public int cellSize() {
        return row.getValues().size();
    }

    public void formatCells(int countCell, ConsumerReturn<CellFormatBuilder> format) {
        for(int beginPos=0;beginPos<countCell;beginPos++) {
            getCell(beginPos).cellFormat(format);
        }
    }

    public void setData(List<String> data) {
        for(int pos=0;pos<data.size();pos++) {
            getCell(pos).value(data.get(pos));
        }
    }

    private static RowData initRow(RowData row) {
        if(row == null)row = new RowData();
        if(row.getValues() == null) row.setValues(new ArrayList<>());
        return row;
    }

}