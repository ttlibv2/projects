package vn.conyeu.google.sheet.builder;

import com.google.api.services.sheets.v4.model.*;
import vn.conyeu.google.core.GoogleException;
import vn.conyeu.google.core.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.OptionalInt;

public class SheetBuilder implements XmlBuilder<Sheet> {
    private final Sheet sheet;
    private final List<RowDataBuilder> rowBuilders;
    private Integer rowCount, columnCount;
    private Integer frozen_row;

    public SheetBuilder(Sheet model) {
        sheet = Utils.getIfNull(model, Sheet::new);
        rowBuilders = new ArrayList<>();
        initialize();
    }

    @Override
    public void initialize() {
        rowCount = getGridProp().getRowCount();
        columnCount = getGridProp().getColumnCount();
        frozen_row = getGridProp().getFrozenRowCount();


        List<GridData> gridDataList = Utils.setIfNull(sheet::getData, ArrayList::new, sheet::setData);
        if(gridDataList.isEmpty()) gridDataList.add(new GridData());

        //--
        GridData gridData = gridDataList.getFirst();
        List<RowData> rows = Utils.setIfNull(gridData::getRowData, ArrayList::new, gridData::setRowData);
        for(RowData row:rows) rowBuilders.add(new RowDataBuilder(row));

    }

    @Override
    public Sheet build() {
        sheet.getData().clear();

        GridData gridData = sheet.getData().getFirst();
        for(RowDataBuilder rowBuilder:rowBuilders) {
            RowData rowData = rowBuilder.build();
            gridData.getRowData().add(rowData);
        }

        // row_count
        Integer row_count = rowCount;
        if(rowCount == null || rowCount < getRowSize()) {
            row_count = getRowSize();
        }

        // column_count
        Integer col_count = columnCount;
        int currColCount = getColumnSize();
        if(col_count == null || col_count < currColCount) {
            col_count = currColCount;
        }

        //frozen_row
        if(frozen_row == null || frozen_row < 1) {
            frozen_row = 1;
        }

        getGridProp().setFrozenRowCount(frozen_row);
        getGridProp().setRowCount(row_count);
        getGridProp().setColumnCount(col_count);

        return sheet;
    }

    public int getRowSize() {
        return rowBuilders.size();
    }

    public int getColumnSize() {
        return getOptionalColumnSize().orElse(0);
    }

    private OptionalInt getOptionalColumnSize() {
        return rowBuilders.stream().mapToInt(RowDataBuilder::size).max();
    }

    /**
     * The ID of the sheet. Must be non-negative. This field cannot be changed once set.
     *
     * @return value or {@code null} for none
     */
    public Integer getSheetId() {
        return getSheetProp().getSheetId();
    }

    /**
     * The index of the sheet within the spreadsheet.
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
        this.columnCount = columnCount;
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
        this.frozen_row = frozenRowCount;
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
        this.rowCount = rowCount;
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

    /*The protected ranges in this sheet.*/
    public SheetBuilder protectedRange(ConsumerReturn<ProtectedRangeBuilder> consumer) {
        ProtectedRangeBuilder builder = consumer.accept(new ProtectedRangeBuilder());
        initProtectedRanges().add(builder.build());
        return this;
    }

    /**A banded (alternating colors) range in a sheet.*/
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

    public RowDataBuilder getRow(int index) {
        if(index < 0) throw new GoogleException("The index < 0");
        else if(index < getRowSize()) return rowBuilders.get(index);
        else {
            for (int pos = getRowSize(); pos <= index; pos++) addRow();
            return rowBuilders.get(index);
        }
    }

    public ColumnDataBuilder getColumn(int index) {
        ColumnDataBuilder columnBuilder = new ColumnDataBuilder();
        columnBuilder.columnIndex(index);

        for(RowDataBuilder rowBuilder:rowBuilders) {
            CellDataBuilder cellBuilder = rowBuilder.getCell(index);
            columnBuilder.addCell(cellBuilder);
        }

        return columnBuilder;
    }

    private SheetProperties getSheetProp() {
        if (sheet.getProperties() == null) {
            sheet.setProperties(new SheetProperties());
        }
        return sheet.getProperties();
    }

    private GridProperties getGridProp() {
        SheetProperties sheetProp = getSheetProp();
        GridProperties gprop = sheetProp.getGridProperties();
        if (gprop != null) return gprop;
        else {
            gprop = new GridProperties();
            sheetProp.setGridProperties(gprop);
            return gprop;
        }
    }

    public RowDataBuilder addRow() {
        RowDataBuilder builder = new RowDataBuilder(null);
        rowBuilders.add(builder);
        return builder;
    }

    public ColumnDataBuilder addColumn() {
        int countColumn = getColumnSize();
        ColumnDataBuilder columnBuilder = new ColumnDataBuilder();
        columnBuilder.columnIndex(countColumn+1);

        for(RowDataBuilder rowBuilder:rowBuilders) {
            CellDataBuilder cellBuilder = rowBuilder.addCell();
            columnBuilder.addCell(cellBuilder);
        }

        return columnBuilder;
    }

}