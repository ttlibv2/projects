package vn.conyeu.google.sheet.builder;

import com.google.api.services.sheets.v4.model.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class RepeatCellBuilder implements XmlBuilder<RepeatCellRequest> {
    private final RepeatCellRequest request = new RepeatCellRequest();
    private final Set<String> fields = new HashSet<>();
    private CellFormat cellFormat;
    private CellData cellData;
    private GridRange gridRange;

    @Override
    public RepeatCellRequest build() {
        if (cellFormat != null) initCell().setUserEnteredFormat(cellFormat);
        if (cellData != null) request.setCell(cellData);
        if (gridRange != null) request.setRange(gridRange);
        if (!fields.isEmpty()) request.setFields(String.join(",", fields));
        return request;
    }

    private CellData initCell() {
        if (cellData == null) {
            cellData = new CellData();
        }
        return cellData;
    }

    private GridRange initRange() {
        if (gridRange == null) {
            gridRange = new GridRange();
        }
        return gridRange;
    }

    private CellFormat initFormat() {
        if (cellFormat == null) {
            cellFormat = new CellFormat();
        }
        return cellFormat;
    }

    /**
     * The fields that should be updated.  At least one field must be specified. The root `cell` is
     * implied and should not be specified. A single `"*"` can be used as short-hand for listing every
     * field.
     *
     * @param fields fields or {@code null} for none
     */
    public RepeatCellBuilder fields(String fields) {
        request.setFields(fields);
        return this;
    }

    /**
     * A data validation rule on the cell, if any.
     * <p>
     * When writing, the new data validation rule will overwrite any prior rule.
     *
     * @param dataValidation dataValidation or {@code null} for none
     */
    public RepeatCellBuilder dataValidation(DataValidationRule dataValidation) {
        initCell().setDataValidation(dataValidation);
        return this;
    }


    /**
     * A hyperlink this cell points to, if any. This field is read-only.  (To set it, use a
     * `=HYPERLINK` formula in the userEnteredValue.formulaValue field.)
     *
     * @param hyperlink hyperlink or {@code null} for none
     */
    public RepeatCellBuilder hyperlink(String hyperlink) {
        initCell().setHyperlink(hyperlink);
        return this;
    }

    /**
     * Any note on the cell.
     *
     * @param note note or {@code null} for none
     */
    public RepeatCellBuilder note(String note) {
        initCell().setNote(note);
        return this;
    }

    /**
     * A pivot table anchored at this cell. The size of pivot table itself is computed dynamically
     * based on its data, grouping, filters, values, etc. Only the top-left cell of the pivot table
     * contains the pivot table definition. The other cells will contain the calculated values of the
     * results of the pivot in their effective_value fields.
     *
     * @param pivotTable pivotTable or {@code null} for none
     */
    public RepeatCellBuilder pivotTable(PivotTable pivotTable) {
        initCell().setPivotTable(pivotTable);
        return this;
    }

    /**
     * Runs of rich text applied to subsections of the cell.  Runs are only valid on user entered
     * strings, not formulas, bools, or numbers. Runs start at specific indexes in the text and
     * continue until the next run. Properties of a run will continue unless explicitly changed in a
     * subsequent run (and properties of the first run will continue the properties of the cell unless
     * explicitly changed).
     * <p>
     * When writing, the new runs will overwrite any prior runs.  When writing a new
     * user_entered_value, previous runs are erased.
     *
     * @param textFormatRuns textFormatRuns or {@code null} for none
     */
    public RepeatCellBuilder textFormatRuns(List<TextFormatRun> textFormatRuns) {
        initCell().setTextFormatRuns(textFormatRuns);
        return this;
    }

    /**
     * The format the user entered for the cell.
     * <p>
     * When writing, the new format will be merged with the existing format.
     *
     * @param builder userEnteredFormat or {@code null} for none
     */
    public RepeatCellBuilder cellFormat(ConsumerReturn<CellFormatBuilder> builder) {
        cellFormat = builder.accept(new CellFormatBuilder(cellFormat)).build();
        return this;
    }

    /**
     * The value the user entered the cell. e.g, `1234`, `'Hello'`, or `=NOW()` Note: Dates, Times
     * and DateTimes are represented as doubles in serial number format.
     *
     * @param userEnteredValue userEnteredValue or {@code null} for none
     */
    public RepeatCellBuilder userEnteredValue(ExtendedValue userEnteredValue) {
        initCell().setUserEnteredValue(userEnteredValue);
        return this;
    }

    /**
     * The end column (exclusive) of the range, or not set if unbounded.
     *
     * @param endColumnIndex endColumnIndex or {@code null} for none
     */
    public RepeatCellBuilder endColumnIndex(Integer endColumnIndex) {
        initRange().setEndColumnIndex(endColumnIndex);
        return this;
    }

    /**
     * The end row (exclusive) of the range, or not set if unbounded.
     *
     * @param endRowIndex endRowIndex or {@code null} for none
     */
    public RepeatCellBuilder endRowIndex(Integer endRowIndex) {
        initRange().setEndRowIndex(endRowIndex);
        return this;
    }

    /**
     * The sheet this range is on.
     *
     * @param sheetId sheetId or {@code null} for none
     */
    public RepeatCellBuilder sheetId(Integer sheetId) {
        initRange().setSheetId(sheetId);
        return this;
    }

    /**
     * The start column (inclusive) of the range, or not set if unbounded.
     *
     * @param startColumnIndex startColumnIndex or {@code null} for none
     */
    public RepeatCellBuilder startColumnIndex(Integer startColumnIndex) {
        initRange().setStartColumnIndex(startColumnIndex);
        return this;
    }

    /**
     * The start row (inclusive) of the range, or not set if unbounded.
     *
     * @param startRowIndex startRowIndex or {@code null} for none
     */
    public RepeatCellBuilder startRowIndex(Integer startRowIndex) {
        initRange().setStartRowIndex(startRowIndex);
        return this;
    }
}