package vn.conyeu.google.sheet.builder;

import com.google.api.services.sheets.v4.model.CellData;
import com.google.api.services.sheets.v4.model.RowData;
import vn.conyeu.commons.utils.Asserts;
import vn.conyeu.google.core.FindList;
import vn.conyeu.google.core.GoogleException;
import vn.conyeu.google.core.Utils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;

public class RowBuilder implements XmlBuilder<RowData>, Iterable<CellBuilder> {
    private final FindList<CellBuilder> cells;
    private final RowData row;
    private ProtectedRangeBuilder protectBuilder;
    private ConsumerReturn<CellBuilder> editCellConsumer;
    private GridBuilder grid;

    public RowBuilder(RowData model) {
        this(null, model);
    }

    public RowBuilder(GridBuilder grid, RowData model) {
        this.grid = grid;//Asserts.notNull(grid, "GridBuilder");
        this.row = Utils.getIfNull(model, RowData::new);
        this.cells = new FindList<>(i -> new CellBuilder());
        this.initialize();
    }

    @Override
    public void initialize() {
        List<CellData> values = Utils.setIfNull(row::getValues, ArrayList::new, row::setValues);
        for (CellData cellData : values) cells.add(new CellBuilder(cellData));
    }

    @Override
    public RowData build() {
        row.getValues().clear();

        for (CellBuilder cell : cells) {
            row.getValues().add(cell.build());
        }

        return row;
    }

    @Override
    public RowBuilder copy() {
        return new RowBuilder(grid, row.clone());
    }

    /**
     * Set the grid
     *
     * @param grid the value
     */
    public RowBuilder setGrid(GridBuilder grid) {
        Asserts.isNull(this.grid, "The gridBuilder has exist.");
        this.grid = grid;
        return this;
    }

    /**
     * Returns the grid
     */
    private GridBuilder getGrid() {
        return Asserts.notNull(grid, "@GridBuilder not set");
    }

    /**
     * Returns the sheet
     */
    public SheetBuilder getSheet() {
        return getGrid().getSheet();
    }



    /**
     * Returns count cell
     *
     * @see #cells#size()
     */
    public int size() {
        return cells.size();
    }

    /**
     * Returns true if cell size() = 0
     *
     * @see #cells#isEmpty()
     */
    public boolean isEmpty() {
        return cells.isEmpty();
    }

    /**
     * Add cell to row
     *
     * @return [CellBuilder] new cell
     */
    public CellBuilder addCell() {
        return createCell(size());
    }

    /**
     * Add cell starting at the given index.
     *
     * @param howMany The number of cells to add.
     */
    public void addCells(int howMany) {
        addCells(cells.size(), howMany);
    }

    /**
     * Add cell starting at the given index.
     *
     * @param index   The starting index of the cell to add.
     * @param howMany The number of cells to add.
     */
    public void addCells(int index, int howMany) {
        if (index < 0 || index > size()) index = size();
        for (int pos = index; pos < index + howMany; pos++) {
            createCell(pos++);
        }
    }

    /**
     * Remove cell at index
     *
     * @param cellIndex the cell index to remove
     */
    public void removeCell(int cellIndex) {
        if (cellIndex >= 0 && cellIndex < cells.size()) {
            cells.remove(cellIndex);
        }
    }

    /**
     * Remove cell from index
     *
     * @param beginCell The begin index of the rows to remove.
     * @see #removeCells(int, int)
     */
    public void removeCells(int beginCell) {
        removeCells(beginCell, cells.size() - beginCell);
    }

    /**
     * Remove cell starting at the given index.
     *
     * @param beginIndex The begin index of the rows to remove.
     * @param howMany    The number of cells to remove.
     * @see #removeCells(int, int)
     */
    public void removeCells(int beginIndex, int howMany) {
        if (beginIndex >= 0 && beginIndex < cells.size()) {
            int maxCell = Math.min(cells.size(), beginIndex + howMany);
            for (int index = beginIndex; index < maxCell; index++) {
                cells.remove(index);
            }
        }
    }

    /**
     * Returns cell at index
     *
     * @param index the index cell for get
     * @throws IndexOutOfBoundsException if the index is out of range({@code index < 0 || index >= size()})
     */
    public CellBuilder findCell(int index) {
        validateCellIndex(index);
        return cells.find(index);
    }

    /**
     * Find or create cell
     *
     * @param index the index cell
     */
    public CellBuilder getCell(int index) {
        return cells.get(index);
    }

    /**
     * Returns the immutable cells
     */
    public List<CellBuilder> getCells() {
        return List.copyOf(cells);
    }

    /**
     * Update cell at index
     *
     * @param cellIndex the index cell for update
     * @param consumer  the custom cell
     */
    public RowBuilder editCell(int cellIndex, Consumer<CellBuilder> consumer) {
        consumer.accept(getCell(cellIndex));
        return this;
    }

    public RowBuilder editCells(ConsumerReturn<CellBuilder> consumer) {

        // apply for cell exists
        loopCells(consumer::accept);

        // save to use after
        if (this.editCellConsumer == null) this.editCellConsumer = consumer;
        else this.editCellConsumer = this.editCellConsumer.andThen(consumer);

        return this;
    }

    private void loopCells(Consumer<CellBuilder> consumer) {
        int cellSize = cells.isEmpty() ? getSheet().getColumnCount() : cells.size();
        for (int c = 0; c < cellSize; c++) {
            consumer.accept(getCell(c));
        }
    }

    /**
     * Protected row
     *
     * @param consumer the custom permission protected
     */
    public RowBuilder protect(ConsumerReturn<ProtectedPermission> consumer) {
        Integer rowIndex = getGrid().getRows().indexOf(this);
        getSheet().protect(rowIndex, consumer);
        return this;
    }

    /**
     * Returns an iterator over elements of type {@code CellBuilder}.
     */
    public Iterator<CellBuilder> iterator() {
        return cells.iterator();
    }

    private void validateCellIndex(int index) {
        if (index < 0 || index >= size()) {
            throw new GoogleException("Index: %s, Size: %s", index, size());
        }
    }

    private CellBuilder createCell(int index) {
        CellBuilder builder = new CellBuilder(null);

        // apply default format
        ConsumerReturn<CellFormatBuilder> formatConsumer = getSheet().getDefaultFormat();
        if(formatConsumer != null) builder.format(formatConsumer);

        // apply custom cell
        if (editCellConsumer != null) editCellConsumer.accept(builder);

        // add & return
        cells.add(index, builder);
        return builder;
    }

    private ProtectedRangeBuilder getProtectBuilder() {
        if (protectBuilder == null) {
            protectBuilder = new ProtectedRangeBuilder(null);
        }
        return protectBuilder;
    }

    protected void fixCountCell(int countCell) {
        int cells = size();
        if (countCell > cells) {
            int howMany = countCell - cells;
            addCells(cells, howMany);
        }
    }

}