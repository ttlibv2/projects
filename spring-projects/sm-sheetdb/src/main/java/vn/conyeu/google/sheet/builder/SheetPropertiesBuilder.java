package vn.conyeu.google.sheet.builder;

import com.google.api.services.sheets.v4.model.*;
import vn.conyeu.google.core.Utils;

import java.util.HashSet;
import java.util.Set;

public class SheetPropertiesBuilder implements XmlBuilder<SheetProperties> {
    private final SheetProperties props;
    private GridPropBuilder gridBuilder;
    private final Set<String> fields = new HashSet<>();
    private final Set<String> fieldsGrid = new HashSet<>();

    public SheetPropertiesBuilder(SheetProperties props) {
        this.props = Utils.getIfNull(props, SheetProperties::new);
    }

    @Override
    public SheetProperties build() {
        return props;
    }

    /**
     * The number of columns in the grid.
     *
     * @param columnCount columnCount or {@code null} for none
     */
    public GridPropBuilder columnCount(Integer columnCount) {
        fieldsGrid.add("columnCount");
        return initGridBuilder().columnCount(columnCount);
    }

    /**
     * True if the column grouping control toggle is shown after the group.
     *
     * @param columnGroupControlAfter columnGroupControlAfter or {@code null} for none
     */
    public GridPropBuilder columnGroupControlAfter(Boolean columnGroupControlAfter) {
        fieldsGrid.add("columnGroupControlAfter");
        return initGridBuilder().columnGroupControlAfter(columnGroupControlAfter);
    }

    /**
     * The number of columns that are frozen in the grid.
     *
     * @param frozenColumnCount frozenColumnCount or {@code null} for none
     */
    public GridPropBuilder frozenColumnCount(Integer frozenColumnCount) {
        fieldsGrid.add("frozenColumnCount");
        return initGridBuilder().frozenColumnCount(frozenColumnCount);
    }

    /**
     * The number of rows that are frozen in the grid.
     *
     * @param frozenRowCount frozenRowCount or {@code null} for none
     */
    public GridPropBuilder frozenRowCount(Integer frozenRowCount) {
        fieldsGrid.add("frozenRowCount");
        return initGridBuilder().frozenRowCount(frozenRowCount);
    }

    /**
     * True if the grid isn't showing gridlines in the UI.
     *
     * @param hideGridlines hideGridlines or {@code null} for none
     */
    public GridPropBuilder hideGridlines(Boolean hideGridlines) {
        fieldsGrid.add("hideGridlines");
        return initGridBuilder().hideGridlines(hideGridlines);
    }

    /**
     * The number of rows in the grid.
     *
     * @param rowCount rowCount or {@code null} for none
     */
    public GridPropBuilder rowCount(Integer rowCount) {
        fieldsGrid.add("rowCount");
        return initGridBuilder().rowCount(rowCount);
    }

    /**
     * True if the row grouping control toggle is shown after the group.
     *
     * @param rowGroupControlAfter rowGroupControlAfter or {@code null} for none
     */
    public GridPropBuilder rowGroupControlAfter(Boolean rowGroupControlAfter) {
        fieldsGrid.add("rowGroupControlAfter");
        return initGridBuilder().rowGroupControlAfter(rowGroupControlAfter);
    }

    /**
     * True if the sheet is hidden in the UI, false if it's visible.
     *
     * @param hidden hidden or {@code null} for none
     */
    public SheetPropertiesBuilder hidden(Boolean hidden) {
        props.setHidden(hidden);
        fields.add("hidden");
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
    public SheetPropertiesBuilder index(Integer index) {
        fields.add("index");
        props.setIndex(index);
        return this;
    }

    /**
     * True if the sheet is an RTL sheet instead of an LTR sheet.
     *
     * @param rightToLeft rightToLeft or {@code null} for none
     */
    public SheetPropertiesBuilder rightToLeft(Boolean rightToLeft) {
        props.setRightToLeft(rightToLeft);
        fields.add("rightToLeft");
        return this;
    }

    /**
     * The ID of the sheet. Must be non-negative. This field cannot be changed once set.
     *
     * @param sheetId sheetId or {@code null} for none
     */
    public SheetPropertiesBuilder sheetId(Integer sheetId) {
        props.setSheetId(sheetId);
        return this;
    }

    /**
     * The type of sheet. Defaults to GRID. This field cannot be changed once set.
     *
     * @param sheetType sheetType or {@code null} for none
     */
    public SheetPropertiesBuilder sheetType(String sheetType) {
        props.setSheetType(sheetType);
        return this;
    }

    /**
     * The color of the tab in the UI.
     *
     * @param tabColor tabColor or {@code null} for none
     */
    public SheetPropertiesBuilder tabColor(Color tabColor) {
        props.setTabColor(tabColor);
        fields.add("tabColor");
        return this;
    }

    /**
     * The color of the tab in the UI. If tab_color is also set, this field takes precedence.
     *
     * @param tabColorStyle tabColorStyle or {@code null} for none
     */
    public SheetPropertiesBuilder tabColorStyle(ColorStyle tabColorStyle) {
        props.setTabColorStyle(tabColorStyle);
        fields.add("tabColorStyle");
        return this;
    }

    /**
     * The name of the sheet.
     *
     * @param title title or {@code null} for none
     */
    public SheetPropertiesBuilder title(String title) {
        props.setTitle(title);
        fields.add("title");
        return this;
    }


    private GridPropBuilder initGridBuilder() {
        if (gridBuilder == null) {
            gridBuilder = new GridPropBuilder(Utils.setIfNull(props::getGridProperties,
                    GridProperties::new, props::setGridProperties));
        }
        return gridBuilder;
    }

    public UpdateSheetPropertiesRequest buildUpdate() {
        UpdateSheetPropertiesRequest request = new UpdateSheetPropertiesRequest();
        StringBuilder fieldBuilder = new StringBuilder();

        if(!fields.isEmpty()) {
            fieldBuilder.append(String.join(",", fields));
            fieldBuilder.append(",");
        }

        if(!fieldsGrid.isEmpty()) {
            fieldBuilder.append("gridProperties(");
            fieldBuilder.append(String.join(",", fieldsGrid));
            fieldBuilder.append(")");
        }

        return request.setProperties(props)
                .setFields(fieldBuilder.toString());
    }
}