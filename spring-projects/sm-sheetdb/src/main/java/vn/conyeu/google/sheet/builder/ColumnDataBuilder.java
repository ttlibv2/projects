package vn.conyeu.google.sheet.builder;

import lombok.Getter;
import vn.conyeu.google.core.GoogleException;

import java.util.ArrayList;
import java.util.List;

@Getter
public class ColumnDataBuilder {
    private List<CellDataBuilder> cells;
    private Integer columnIndex;

    public ColumnDataBuilder() {
        cells = new ArrayList<>();
    }

    public int getSize() {
        return cells.size();
    }

    public List<CellDataBuilder> getCells() {
        return List.copyOf(cells);
    }

    public ColumnDataBuilder columnIndex(Integer columnIndex) {
        this.columnIndex = columnIndex;
        return this;
    }

    public void addCell(CellDataBuilder cellBuilder) {
        cells.add(cellBuilder);
    }

    public CellDataBuilder getCell(int rowIndex) {
        if(rowIndex < 0 || rowIndex >= cells.size()) {
            throw new GoogleException("The index invalid");
        }

        return cells.get(rowIndex);
    }
}
