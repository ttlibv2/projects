package vn.conyeu.google.sheetdb;

import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.model.*;
import vn.conyeu.commons.utils.Asserts;
import vn.conyeu.google.core.GoogleException;
import vn.conyeu.google.core.Utils;
import vn.conyeu.google.sheetdb.builder.Dimension;
import vn.conyeu.google.sheetdb.builder.SheetPropertiesBuilder;

public class XslSheet {
    private final Sheets.Spreadsheets service;
    private final XslBatchUpdate batchUpdate;
    private final XslBook workbook;
    private final Sheet sheet;

    private SheetProperties properties;
    private GridProperties gridProperties;
    private SheetPropertiesBuilder builder;

    XslSheet(Sheets.Spreadsheets service, XslBatchUpdate batchUpdate, XslBook workbook, Sheet sheet) {
        this.workbook = Asserts.notNull(workbook);
        this.sheet = Asserts.notNull(sheet);
        this.batchUpdate = batchUpdate;
        this.service = service;
        this.properties = Utils.getIfNull(sheet.getProperties(), SheetProperties::new);
        this.gridProperties = Utils.setIfNull(properties::getGridProperties, GridProperties::new, properties::setGridProperties);
    }

    /**
     * The number of columns in the grid.
     *
     * @return value or {@code null} for none
     */
    public Integer getColumnCount() {
        return gridProperties.getColumnCount();
    }

    /**
     * True if the column grouping control toggle is shown after the group.
     *
     * @return value or {@code null} for none
     */
    public Boolean getColumnGroupControlAfter() {
        return gridProperties.getColumnGroupControlAfter();
    }

    /**
     * The number of columns that are frozen in the grid.
     *
     * @return value or {@code null} for none
     */
    public Integer getFrozenColumnCount() {
        return gridProperties.getFrozenColumnCount();
    }

    /**
     * The number of rows that are frozen in the grid.
     *
     * @return value or {@code null} for none
     */
    public Integer getFrozenRowCount() {
        return gridProperties.getFrozenRowCount();
    }

    /**
     * True if the grid isn't showing gridlines in the UI.
     *
     * @return value or {@code null} for none
     */
    public Boolean getHideGridlines() {
        return gridProperties.getHideGridlines();
    }

    /**
     * The number of rows in the grid.
     *
     * @return value or {@code null} for none
     */
    public Integer getRowCount() {
        return gridProperties.getRowCount();
    }

    /**
     * True if the row grouping control toggle is shown after the group.
     *
     * @return value or {@code null} for none
     */
    public Boolean getRowGroupControlAfter() {
        return gridProperties.getRowGroupControlAfter();
    }

    /**
     * True if the sheet is hidden in the UI, false if it's visible.
     *
     * @return value or {@code null} for none
     */
    public Boolean getHidden() {
        return properties.getHidden();
    }

    /**
     * The index of the sheet within the spreadsheet.
     *
     * @return value or {@code null} for none
     */
    public Integer getIndex() {
        return properties.getIndex();
    }

    /**
     * True if the sheet is an RTL sheet instead of an LTR sheet.
     *
     * @return value or {@code null} for none
     */
    public Boolean getRightToLeft() {
        return properties.getRightToLeft();
    }

    /**
     * The ID of the sheet. Must be non-negative. This field cannot be changed once set.
     *
     * @return value or {@code null} for none
     */
    public Integer getSheetId() {
        return properties.getSheetId();
    }

    /**
     * The type of sheet. Defaults to GRID. This field cannot be changed once set.
     *
     * @return value or {@code null} for none
     */
    public String getSheetType() {
        return properties.getSheetType();
    }

    /**
     * The color of the tab in the UI.
     *
     * @return value or {@code null} for none
     */
    public Color getTabColor() {
        return properties.getTabColor();
    }

    /**
     * The color of the tab in the UI. If tab_color is also set, this field takes precedence.
     *
     * @return value or {@code null} for none
     */
    public ColorStyle getTabColorStyle() {
        return properties.getTabColorStyle();
    }

    /**
     * The name of the sheet.
     *
     * @return value or {@code null} for none
     */
    public String getTitle() {
        return properties.getTitle();
    }

    /**
     * True if the sheet is hidden in the UI, false if it's visible.
     *
     * @param hidden hidden or {@code null} for none
     */
    public XslSheet setHidden(Boolean hidden) {
        properties.setHidden(hidden);
        getBuilder().hidden(hidden);
        return this;
    }

    /**
     * The index of the sheet within the spreadsheet.
     *
     * @param index index or {@code null} for none
     */
    public XslSheet setIndex(Integer index) {
        properties.setIndex(index);
        getBuilder().index(index); return this;
    }

    /**
     * True if the sheet is an RTL sheet instead of an LTR sheet.
     *
     * @param rightToLeft rightToLeft or {@code null} for none
     */
    public XslSheet setRightToLeft(Boolean rightToLeft) {
        properties.setRightToLeft(rightToLeft);
        getBuilder().rightToLeft(rightToLeft); return this;
    }

    /**
     * The color of the tab in the UI.
     *
     * @param tabColor tabColor or {@code null} for none
     */
    public XslSheet setTabColor(Color tabColor) {
        properties.setTabColor(tabColor);
        getBuilder().tabColor(tabColor); return this;
    }

    /**
     * The color of the tab in the UI. If tab_color is also set, this field takes precedence.
     *
     * @param tabColorStyle tabColorStyle or {@code null} for none
     */
    public XslSheet setTabColorStyle(ColorStyle tabColorStyle) {
        properties.setTabColorStyle(tabColorStyle);
        getBuilder().tabColorStyle(tabColorStyle); return this;
    }

    /**
     * The name of the sheet.
     *
     * @param title title or {@code null} for none
     */
    public XslSheet setTitle(String title) {
        properties.setTitle(title);
        getBuilder().title(title); return this;
    }

    public void remove() {
        this.workbook.deleteSheet(this);
    }

    public XslSheet save() {
        if (builder != null) {
            builder.sheetId(getSheetId());
            batchUpdate.updateSheet(builder);
            builder = null;
        }
        return this;
    }

    /**
     * Hides a single column at the given index. Use 0-index for this method.
     * @param columnIndex The starting index of the columns to hide.
     * */
    public void hideColumn(int columnIndex) {
        batchUpdate.hideColumn(getSheetId(), columnIndex, 1);
    }

    /**
     * Hides one or more consecutive columns starting at the given index. Use 0-index for this method.
     * @param columnIndex The starting index of the columns to hide.
     * @param numColumns The number of columns to hide.
     * */
    public void hideColumn(int columnIndex, int numColumns) {
        batchUpdate.hideColumn(getSheetId(), columnIndex, numColumns);
    }

    /**
     * Hides the row at the given index.
     * @param rowIndex 	The index of the row to hide.
     * */
    public void hideRow(int rowIndex) {
        hideRows(rowIndex, 1);
    }

    /**
     * Hides one or more consecutive rows starting at the given index.
     * @param rowIndex 	The index of the row to hide.
     * @param numRows The number of rows to hide.
     * */
    public void hideRows(int rowIndex, int numRows) {
        batchUpdate.hideRow(getSheetId(), rowIndex, numRows);
    }

    /**
     * Unhides the row at the given index.
     * @param rowIndex 	The index of the row to unhide.
     * */
    public void showRow(int rowIndex) {
        showRows(rowIndex, 1);
    }

    /**
     * Unhides one or more consecutive rows starting at the given index.
     * @param rowIndex 	The index of the row to unhide.
     * @param numRows The number of rows to unhide.
     * */
    public void showRows(int rowIndex, int numRows) {
        batchUpdate.showRow(getSheetId(), rowIndex, numRows);
    }

    /**
     * Unhides a single column at the given index. Use 0-index for this method.
     * @param columnIndex The starting index of the columns to unhide.
     * */
    public void showColumn(int columnIndex) {
        showColumns(columnIndex, 1);
    }

    /**
     * Unhides one or more consecutive columns starting at the given index. Use 0-index for this method.
     * @param columnIndex The starting index of the columns to unhide.
     * @param numColumns The number of columns to unhide.
     * */
    public void showColumns(int columnIndex, int numColumns) {
        batchUpdate.showColumn(getSheetId(), columnIndex, numColumns);
    }

    /**
     * Hides this sheet. Has no effect if the sheet is already hidden.
     * If this method is called on the only visible sheet, it throws an exception.
     * */
    public void hideSheet() {
        setHidden(true).save();
    }

    /**Makes the sheet visible. Has no effect if the sheet is already visible.*/
    public void showSheet() {
        setHidden(false).save();
    }

    /**
     * Inserts a blank column in a sheet at the specified location.
     * @param columnIndex 	The index indicating where to insert a column.
     * */
    public void insertColumn(int columnIndex) {
        insertColumns(columnIndex, 1);

    }

    /**
     * Inserts a blank column in a sheet at the specified location.
     * @param columnIndex 	The index indicating where to insert a column.
     * @param numColumns 	The number of columns to insert.
     * */
    public void insertColumns(int columnIndex, int numColumns) {
        batchUpdate.insertColumns(getSheetId(), columnIndex, numColumns);
    }

    /**
     * Inserts a column after the given column position.
     * @param afterPosition 	The column after which the new column should be added.
     * */
    public void insertColumnAfter(int afterPosition) {
        insertColumnAfter(afterPosition, 1);
    }

    /**
     * Inserts a given number of columns after the given column position.
     * @param afterPosition 	The column after which the new column should be added.
     * @param howMany 	The number of columns to insert.
     * */
    public void insertColumnAfter(int afterPosition, int howMany) {
        batchUpdate.insertColumns(getSheetId(), afterPosition+1, howMany);
    }

    /**
     * Inserts a blank row in a sheet at the specified location.
     * @param rowIndex 	The index indicating where to insert a row.
     * */
    public void insertRow(int rowIndex) {
        insertRows(rowIndex, 1);

    }

    /**
     * Inserts a blank row in a sheet at the specified location.
     * @param rowIndex 	The index indicating where to insert a row.
     * @param numRows 	The number of rows to insert.
     * */
    public void insertRows(int rowIndex, int numRows) {
        batchUpdate.insertRows(getSheetId(), rowIndex, numRows);
    }

    /**
     * Inserts a row after the given row position.
     * @param afterPosition 	The row after which the new row should be added.
     * */
    public void insertRowsAfter(int afterPosition) {
        insertRowsAfter(afterPosition, 1);
    }

    /**
     * Inserts a given number of columns after the given row position.
     * @param afterPosition 	The row after which the new row should be added.
     * @param howMany 	The number of rows to insert.
     * */
    public void insertRowsAfter(int afterPosition, int howMany) {
        batchUpdate.insertRows(getSheetId(), afterPosition+1, howMany);
    }

    /**
     * Moves the columns selected by the given range to the position indicated by the destinationIndex.
     * @param columnIndex the column index
     * @param destinationIndex The index that the columns should be moved to.
     */
    public void moveColumns(int columnIndex, int endColumn, int destinationIndex) {
        batchUpdate.moveDimension(getSheetId(), Dimension.COLUMNS, columnIndex, endColumn, destinationIndex);
    }

    /**
     * Moves the rows selected by the given range to the position indicated by the destinationIndex.
     * @param rowIndex the row index
     * @param destinationIndex The index that the columns should be moved to.
     */
    public void moveRows(int rowIndex, int endRow, int destinationIndex) {
        batchUpdate.moveDimension(getSheetId(), Dimension.ROWS, rowIndex, endRow, destinationIndex);
    }

    /**
     * Deletes the row at the given row position.
     * @param rowPosition The position of the row, starting at 0 for the first row.
     * */
    public void deleteRow(int rowPosition) {
        deleteRows(rowPosition, 1);
    }

    /**
     * Deletes a number of rows starting at the given row position.
     * @param rowPosition 	The position of the first row to delete.
     * @param howMany 	The number of rows to delete.
     * */
    public void deleteRows(int rowPosition, int howMany) {
        batchUpdate.deleteDimension(getSheetId(), Dimension.ROWS, rowPosition, rowPosition+howMany);
    }

    /**
     * Deletes the column at the given column position.
     * @param columnPosition The position of the column, starting at 0 for the first column.
     * */
    public void deleteColumn(int columnPosition) {
        deleteRows(columnPosition, 1);
    }

    /**
     * Deletes a number of columns starting at the given column position.
     * @param columnPosition 	The position of the first column to delete.
     * @param howMany 	The number of columns to delete.
     * */
    public void deleteColumns(int columnPosition, int howMany) {
        batchUpdate.deleteDimension(getSheetId(), Dimension.COLUMNS, columnPosition, columnPosition+howMany);
    }

    /**
     * Clears the sheet of text formatting.
     * Formatting refers to how data is formatted as allowed by choices under the "Format" menu (ex: bold, italics, conditional formatting)
     * */
    public void clearTextFormat(int beginRow, int beginCol) {
        batchUpdate.clearTextFormat(getSheetId(), beginRow, beginCol);
    }

    /**
     * Clears the sheet of formatting, while preserving contents.
     * Formatting refers to how data is formatted as allowed by choices under the "Format" menu (ex: bold, italics, conditional formatting) and not width or height of cells.
     * */
    public void clearFormat(int beginRow, int beginCol) {
        batchUpdate.clearFormat(getSheetId(), beginRow, beginCol);
    }

    /**
     * Clears the sheet of contents, while preserving formatting information.
     * */
    public void clearContent(int beginRow, int beginCol) {
        batchUpdate.clearContent(getSheetId(), beginRow, beginCol);
    }

    /**
     * Clears the sheet of content and formatting information.
     * */
    public void clearAll(int beginRow, int beginCol) {
        batchUpdate.clearAll(getSheetId(), beginRow, beginCol);
    }

    public void clearAll() {
        clearAll(0, 0);
    }






    private SheetPropertiesBuilder getBuilder() {
        if (builder == null) builder = new SheetPropertiesBuilder(null);
        return builder;
    }
}