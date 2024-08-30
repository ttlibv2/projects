package vn.conyeu.google.sheet.builder;

import com.google.api.services.sheets.v4.model.GridProperties;

public class GridPropBuilder implements XmlBuilder<GridProperties> {
    private final GridProperties prop;

    public GridPropBuilder(final GridProperties props) {
        this.prop = XmlBuilder.ifNull(props, GridProperties::new);
    }

    @Override
    public GridProperties build() {
        return prop;
    }

    /**
     * The number of columns in the grid.
     *
     * @param columnCount columnCount or {@code null} for none
     */
    public GridPropBuilder columnCount(Integer columnCount) {
        prop.setColumnCount(columnCount);
        return this;
    }

    /**
     * True if the column grouping control toggle is shown after the group.
     *
     * @param columnGroupControlAfter columnGroupControlAfter or {@code null} for none
     */
    public GridPropBuilder columnGroupControlAfter(Boolean columnGroupControlAfter) {
        prop.setColumnGroupControlAfter(columnGroupControlAfter);
        return this;
    }

    /**
     * The number of columns that are frozen in the grid.
     *
     * @param frozenColumnCount frozenColumnCount or {@code null} for none
     */
    public GridPropBuilder frozenColumnCount(Integer frozenColumnCount) {
        prop.setFrozenColumnCount(frozenColumnCount);
        return this;
    }

    /**
     * The number of rows that are frozen in the grid.
     *
     * @param frozenRowCount frozenRowCount or {@code null} for none
     */
    public GridPropBuilder frozenRowCount(Integer frozenRowCount) {
        prop.setFrozenRowCount(frozenRowCount);
        return this;
    }

    /**
     * True if the grid isn't showing gridlines in the UI.
     *
     * @param hideGridlines hideGridlines or {@code null} for none
     */
    public GridPropBuilder hideGridlines(Boolean hideGridlines) {
        prop.setHideGridlines(hideGridlines);
        return this;
    }

    /**
     * The number of rows in the grid.
     *
     * @param rowCount rowCount or {@code null} for none
     */
    public GridPropBuilder rowCount(Integer rowCount) {
        prop.setRowCount(rowCount);
        return this;
    }

    /**
     * True if the row grouping control toggle is shown after the group.
     *
     * @param rowGroupControlAfter rowGroupControlAfter or {@code null} for none
     */
    public GridPropBuilder rowGroupControlAfter(Boolean rowGroupControlAfter) {
        prop.setRowGroupControlAfter(rowGroupControlAfter);
        return this;
    }
}