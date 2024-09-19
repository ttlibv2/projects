package vn.conyeu.google.sheet.builder;

import com.google.api.services.sheets.v4.model.*;
import vn.conyeu.commons.utils.Asserts;
import vn.conyeu.google.core.Utils;

import java.util.ArrayList;
import java.util.List;

public class SheetBuilder implements XmlBuilder<Sheet> {
    private final Sheet sheet;
    private final List<GridBuilder> grids;
    private ConsumerReturn<CellFormatBuilder> defaultFormat;
    private GridBuilder grid;

    public SheetBuilder(Sheet model) {
        sheet = Utils.getIfNull(model, Sheet::new);
        grids = new ArrayList<>();
        initialize();
    }

    public void initialize() {

        // initialize grid_builder
        Utils.setIfNull(sheet::getData, ArrayList::new, sheet::setData);
        for (GridData data : sheet.getData()) {
            grids.add(new GridBuilder(this, data));
        }

        grid = getGrid(0);
    }

    @Override
    public SheetBuilder copy() {
        return new SheetBuilder(sheet.clone());
    }

    public Sheet build() {
        Sheet sheet = this.sheet.clone();
        sheet.getData().clear();

        //update grid_data
        for (GridBuilder grid : grids) {
            sheet.getData().add(grid.build());
        }

        //frozen_row
        Integer frozen_row = getGridProp().getFrozenRowCount();
        if (frozen_row == null || frozen_row < 1) {
            frozen_row = 1;
        }

        getGridProp().setFrozenRowCount(frozen_row);

        Integer actuRow = getRowSize(), maxRow = getGridProp().getRowCount();
        Integer countRow = maxRow == null || maxRow < actuRow ? actuRow : maxRow;
        getGridProp().setRowCount(countRow);

        Integer actuCol = getColumnSize(), maxColumn = getGridProp().getColumnCount();
        Integer countCol = maxColumn == null || maxColumn < actuCol ? actuCol : maxColumn;
        getGridProp().setColumnCount(countCol);

        return sheet;
    }

    public int gridSize() {
        return grids.size();
    }

    /**
     * The number of columns in the grid.
     *
     * @return value or {@code null} for none
     */
    public Integer getColumnCount() {
        return getGridProp().getColumnCount();
    }

    /**
     * The number of rows in the grid.
     *
     * @return value or {@code null} for none
     */
    public Integer getRowCount() {
        return getGridProp().getRowCount();
    }

    public Integer getFrozenRow() {
        return getGridProp().getFrozenRowCount();
    }

    /**
     * The ID of the sheet. Must be non-negative. This field cannot be changed once set.
     *
     * @return value or {@code null} for none
     */
    public Integer getSheetId() {
        Integer sheetId = getSheetProp().getSheetId();
        return Asserts.notNull(sheetId, "The sheetId not set");
    }

    /**
     * The index of the sheet within the spreadsheet.
     *
     * @param index index or {@code null} for none
     */
    public SheetBuilder index(Integer index) {
        getSheetProp().setIndex(index);
        return this;
    }

    /**
     * True if the sheet is hidden in the UI, false if it's visible.
     *
     * @param hidden hidden or {@code null} for none
     */
    public SheetBuilder hidden(Boolean hidden) {
        getSheetProp().setHidden(hidden);
        return this;
    }

    /**
     * True if the sheet is an RTL sheet instead of an LTR sheet.
     *
     * @param rightToLeft rightToLeft or {@code null} for none
     */
    public SheetBuilder rightToLeft(Boolean rightToLeft) {
        getSheetProp().setRightToLeft(rightToLeft);
        return this;
    }

    /**
     * The type of sheet. Defaults to GRID. This field cannot be changed once set.
     *
     * @param sheetType sheetType or {@code null} for none
     */
    public SheetBuilder sheetType(SheetType sheetType) {
        getSheetProp().setSheetType(Utils.enumName(sheetType));
        return this;
    }

    /**
     * The ID of the sheet. Must be non-negative. This field cannot be changed once set.
     *
     * @param sheetId sheetId or {@code null} for none
     */
    public SheetBuilder sheetId(Integer sheetId) {
        getSheetProp().setSheetId(sheetId);
        return this;
    }

    /**
     * The color of the tab in the UI.
     *
     * @param rgbColor rgb color
     */
    public SheetBuilder tabColorStyle(Color rgbColor) {
        getSheetProp().setTabColorStyle(new ColorStyle().setRgbColor(rgbColor));
        return this;
    }

    /**
     * The color of the tab in the UI. If tab_color is also set, this field takes precedence.
     *
     * @param themeColor theme color
     */
    public SheetBuilder tabColorStyle(ThemeColorType themeColor) {
        getSheetProp().setTabColorStyle(new ColorStyle().setThemeColor(Utils.enumName(themeColor)));
        return this;
    }

    /**
     * The name of the sheet.
     *
     * @param title title or {@code null} for none
     */
    public SheetBuilder title(String title) {
        getSheetProp().setTitle(title);
        return this;
    }

    /**
     * The number of columns in the grid.
     *
     * @param columnCount columnCount or {@code null} for none
     */
    public SheetBuilder columnCount(Integer columnCount) {
        getGridProp().setColumnCount(columnCount);
        return this;
    }

    /**
     * True if the column grouping control toggle is shown after the group.
     *
     * @param columnGroupControlAfter columnGroupControlAfter or {@code null} for none
     */
    public SheetBuilder columnGroupControlAfter(Boolean columnGroupControlAfter) {
        getGridProp().setColumnGroupControlAfter(columnGroupControlAfter);
        return this;
    }

    /**
     * The number of columns that are frozen in the grid.
     *
     * @param frozenColumnCount frozenColumnCount or {@code null} for none
     */
    public SheetBuilder frozenColumnCount(Integer frozenColumnCount) {
        getGridProp().setFrozenColumnCount(frozenColumnCount);
        return this;
    }

    /**
     * The number of rows that are frozen in the grid.
     *
     * @param frozenRowCount frozenRowCount or {@code null} for none
     */
    public SheetBuilder frozenRowCount(Integer frozenRowCount) {
        getGridProp().setFrozenRowCount(frozenRowCount);
        return this;
    }

    /**
     * True if the grid isn't showing gridlines in the UI.
     *
     * @param hideGridlines hideGridlines or {@code null} for none
     */
    public SheetBuilder hideGridlines(Boolean hideGridlines) {
        getGridProp().setHideGridlines(hideGridlines);
        return this;
    }

    /**
     * The number of rows in the grid.
     *
     * @param rowCount rowCount or {@code null} for none
     */
    public SheetBuilder rowCount(Integer rowCount) {
        getGridProp().setRowCount(rowCount);
        return this;
    }

    /**
     * True if the row grouping control toggle is shown after the group.
     *
     * @param rowGroupControlAfter rowGroupControlAfter or {@code null} for none
     */
    public SheetBuilder rowGroupControlAfter(Boolean rowGroupControlAfter) {
        getGridProp().setRowGroupControlAfter(rowGroupControlAfter);
        return this;
    }

    public GridBuilder findGrid(int index) {
        Asserts.validateIndex(index, 0, gridSize());
        return grids.get(index);
    }

    public GridBuilder getGrid(int index) {
        if (index < 0 || index >= gridSize()) {
            addGrids(gridSize(), index < 0 ? 1 : index - gridSize() + 1);
        }
        return grids.get(index);
    }

    /**
     * edit row at index
     *
     * @param rowIndex the row index
     * @param consumer the custom edit for all cell of row
     */
    public SheetBuilder editRow(int rowIndex, ConsumerReturn<RowBuilder> consumer) {
        consumer.accept(getGrid(0).getRow(rowIndex));
        return this;
    }

    /*The protected ranges in this sheet.*/
    protected SheetBuilder protect(Integer rowIndex, ConsumerReturn<ProtectedPermission> consumer) {
        if (rowIndex == null || rowIndex < 0) throw new IndexOutOfBoundsException("The index invalid -- " + rowIndex);
        ProtectedRangeBuilder builder = new ProtectedRangeBuilder(null);
        builder.forRow(getSheetId(), rowIndex);
        consumer.accept(builder.getPermission());
        initProtectedRanges().add(builder.build());
        return this;
    }

    /*The protected ranges in this sheet.*/
    protected SheetBuilder protectAll(ConsumerReturn<ProtectedRangeBuilder> consumer) {
        ProtectedRangeBuilder builder = consumer.accept(new ProtectedRangeBuilder(null));
        builder.range(r -> r.sheetId(getSheetId()));
        initProtectedRanges().add(builder.build());
        return this;
    }

    /*The protected ranges in this sheet.*/
    protected SheetBuilder protect(ConsumerReturn<ProtectedPermission> consumer) {
        ProtectedRangeBuilder builder = new ProtectedRangeBuilder(null)
                .range(r -> r.sheetId(getSheetId()));
        consumer.accept(builder.getPermission());
        initProtectedRanges().add(builder.build());
        return this;
    }

    /**
     * A banded (alternating colors) range in a sheet.
     */
    public SheetBuilder bandedRange(ConsumerReturn<BandedRangeBuilder> consumer) {
        BandedRangeBuilder builder = consumer.accept(new BandedRangeBuilder());
        getBandedRanges().add(builder.build());
        return this;
    }

    private List<BandedRange> getBandedRanges() {
        return Utils.setIfNull(sheet::getBandedRanges, ArrayList::new, sheet::setBandedRanges);
    }

    /*The filter on this sheet, if any.*/
    public SheetBuilder basicFilter(ConsumerReturn<BasicFilter> consumer) {
        consumer.accept(initBasicFilter());
        return this;
    }

    private BasicFilter initBasicFilter() {
        return Utils.setIfNull(sheet::getBasicFilter, BasicFilter::new, sheet::setBasicFilter);
    }

    /*The specifications of every chart on this sheet.*/
    public SheetBuilder charts(ConsumerReturn<EmbeddedChart> consumer) {
        getCharts().add(consumer.accept(new EmbeddedChart()));
        return this;
    }

    private List<EmbeddedChart> getCharts() {
        return Utils.setIfNull(sheet::getCharts, ArrayList::new, sheet::setCharts);
    }

    /*All column groups on this sheet, ordered by increasing range start index, then by group depth.*/
    public SheetBuilder columnGroup(ConsumerReturn<DimensionGroup> consumer) {
        getColumnGroups().add(consumer.accept(new DimensionGroup()));
        return this;
    }

    private List<DimensionGroup> getColumnGroups() {
        return Utils.setIfNull(sheet::getColumnGroups, ArrayList::new, sheet::setColumnGroups);
    }

    public SheetBuilder conditionalFormats(List<ConditionalFormatRule> conditionalFormats) {
        sheet.setConditionalFormats(conditionalFormats);
        return this;
    }

    public SheetBuilder developerMetadata(List<DeveloperMetadata> developerMetadata) {
        sheet.setDeveloperMetadata(developerMetadata);
        return this;
    }

    public SheetBuilder filterViews(List<FilterView> filterViews) {
        sheet.setFilterViews(filterViews);
        return this;
    }

    public SheetBuilder merges(List<GridRange> merges) {
        sheet.setMerges(merges);
        return this;
    }

    private List<ProtectedRange> initProtectedRanges() {
        return Utils.setIfNull(sheet::getProtectedRanges, ArrayList::new, sheet::setProtectedRanges);
    }

    public SheetBuilder rowGroups(List<DimensionGroup> rowGroups) {
        sheet.setRowGroups(rowGroups);
        return this;
    }

    public SheetBuilder slicers(List<Slicer> slicers) {
        sheet.setSlicers(slicers);
        return this;
    }

    private SheetProperties getSheetProp() {
        if (sheet.getProperties() == null) {
            sheet.setProperties(new SheetProperties());
        }
        return sheet.getProperties();
    }

    private GridProperties getGridProp() {
        SheetProperties sheetProp = getSheetProp();
        GridProperties gridProperties = sheetProp.getGridProperties();
        if (gridProperties == null) {
            gridProperties = new GridProperties();
            sheetProp.setGridProperties(gridProperties);
        }
        return gridProperties;
    }

    private Integer getRowSize() {
        return grids.stream().mapToInt(GridBuilder::getRowSize).max().orElse(0);
    }

    private Integer getColumnSize() {
        return grids.stream().mapToInt(GridBuilder::getColumnSize).max().orElse(0);
//        throw new UnsupportedOperationException();
    }

    private void addGrids(int index, int howMany) {
        for (int pos = index; pos < index + howMany; pos++) {
            GridBuilder gb = new GridBuilder(this, null);
            grids.add(gb);
        }
    }

    public SheetBuilder defaultFormat(ConsumerReturn<CellFormatBuilder> formatBuilder) {
        if(defaultFormat == null) defaultFormat = formatBuilder;
        else defaultFormat = defaultFormat.andThen(formatBuilder);
        return this;
    }

    /**
     * Returns the defaultFormat
     */
    public ConsumerReturn<CellFormatBuilder> getDefaultFormat() {
        return defaultFormat;
    }
}