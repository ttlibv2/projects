package vn.conyeu.google.sheet.builder;

import com.google.api.services.sheets.v4.model.*;
import vn.conyeu.google.core.Utils;

import java.util.ArrayList;
import java.util.List;

public class SheetBuilder implements XmlBuilder<Sheet> {
    private final Sheet sheet = new Sheet();

    public SheetBuilder() {
        initialize();
    }

    public void initialize() {
        sheet.setData(new ArrayList<>());
        sheet.getData().add(new GridData());
    }

    @Override
    public Sheet build() {
        return sheet;
    }

    public RowDataBuilder getRow(int rowPos) {
        RowData rData = getRowDataPos(0, rowPos);
        return new RowDataBuilder(rData);
    }

    /**
     * The ID of the sheet. Must be non-negative. This field cannot be changed once set.
     *
     * @return value or {@code null} for none
     */
    public Integer getSheetId() {
        return getSheetProp().getSheetId();
    }

    public SheetBuilder bandedRanges(List<BandedRange> bandedRanges) {
        sheet.setBandedRanges(bandedRanges);
        return this;
    }

    public SheetBuilder basicFilter(ConsumerReturn<BasicFilter> basicFilter) {
        BasicFilter filter = basicFilter.accept(initBasicFilter());
        sheet.setBasicFilter(filter);
        return this;
    }

    private BasicFilter initBasicFilter() {
        BasicFilter filter = sheet.getBasicFilter();
        return filter == null ? new BasicFilter():filter;
    }

    public SheetBuilder charts(List<EmbeddedChart> charts) {
        sheet.setCharts(charts);
        return this;
    }

    public SheetBuilder columnGroups(List<DimensionGroup> columnGroups) {
        sheet.setColumnGroups(columnGroups);
        return this;
    }

    public SheetBuilder conditionalFormats(List<ConditionalFormatRule> conditionalFormats) {
        sheet.setConditionalFormats(conditionalFormats);
        return this;
    }

    public SheetBuilder data(List<GridData> data) {
        sheet.setData(data);
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
     * The index of the sheet within the spreadsheet. When adding or updating sheet properties, if
     * this field is excluded then the sheet is added or moved to the end of the sheet list. When
     * updating sheet indices or inserting sheets, movement is considered in "before the move"
     * indexes. For example, if there were 3 sheets (S1, S2, S3) in order to move S1 ahead of S2 the
     * index would have to be set to 2. A sheet index update request is ignored if the requested index
     * is identical to the sheets current index or if the requested new index is equal to the current
     * sheet index + 1.
     *
     * @param index index or {@code null} for none
     */
    public SheetBuilder index(Integer index) {
        getSheetProp().setIndex(index);
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
        getSheetProp().setSheetType(sheetType.name());
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
     * @param tabColor tabColor or {@code null} for none
     */
    public SheetBuilder tabColor(ConsumerReturn<Color> tabColor) {
        Color color = tabColor.accept(initTabColor());
        getSheetProp().setTabColor(color);
        return this;
    }

    private Color initTabColor() {
        Color color = getSheetProp().getTabColor();
        return color == null ? new Color() : color;
    }

    /**
     * The color of the tab in the UI. If tab_color is also set, this field takes precedence.
     *
     * @param tabColorStyle tabColorStyle or {@code null} for none
     */
    public SheetBuilder tabColorStyle(ConsumerReturn<ColorStyle> tabColorStyle) {
        ColorStyle style = tabColorStyle.accept(initTabColorStyle());
        getSheetProp().setTabColorStyle(style);
        return this;
    }

    private ColorStyle initTabColorStyle() {
        ColorStyle style = getSheetProp().getTabColorStyle();
        return style == null ? new ColorStyle():style;
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

    public SheetBuilder protectedRange(ConsumerReturn<ProtectedRangeBuilder> consumer) {
        ProtectedRangeBuilder builder = consumer.accept(new ProtectedRangeBuilder());
        initProtectedRanges().add(builder.build());
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
        GridProperties gprop = sheetProp.getGridProperties();
        if (gprop != null) return gprop;
        else {
            gprop = new GridProperties();
            sheetProp.setGridProperties(gprop);
            return gprop;
        }
    }

    private RowData getRowDataPos(int gridPos, int rowPos) {
        List<GridData> lisGrid = Utils.setIfNull(sheet::getData, ArrayList::new, sheet::setData);
        if(gridPos >= lisGrid.size()) {
            GridData gData = new GridData();
            gData.setRowData(new ArrayList<>());
            lisGrid.add(gData);

            RowData rData = new RowData();
            rData.setValues(new ArrayList<>());
            gData.getRowData().add(rData);
            return rData;
        }
        else {
            GridData gData = lisGrid.get(gridPos);
            if(rowPos >= gData.size()) {
                RowData rData = new RowData();
                rData.setValues(new ArrayList<>());
                gData.getRowData().add(rData);
                return rData;
            }
            else return gData.getRowData().get(rowPos);
        }
    }

    public RowDataBuilder addRow() {
        RowDataBuilder builder = new RowDataBuilder(null);
        GridData gridData = sheet.getData().get(0);
        gridData.getRowData().add(builder.build());
        return builder;
    }

}