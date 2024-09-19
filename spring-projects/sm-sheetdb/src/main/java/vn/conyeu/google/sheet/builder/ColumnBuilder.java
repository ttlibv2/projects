package vn.conyeu.google.sheet.builder;

import lombok.Getter;
import vn.conyeu.commons.utils.Asserts;

import java.util.List;

@Getter
public class ColumnBuilder {
    private final GridBuilder grid;
    private Integer index;

    public ColumnBuilder(GridBuilder grid, Integer index) {
        this.grid = Asserts.notNull(grid, "@GridBuilder");
        this.index = index;
    }

    public ColumnBuilder setValue(List<Object> values) {
        for (int r = 0; r < values.size(); r++) {
            RowBuilder rowBuilder = grid.getRow(r);
            Object value = values.get(r);
            rowBuilder.getCell(index).setValue(value);
        }
        return this;
    }
}