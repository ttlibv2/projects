package vn.conyeu.google.sheet.builder;

import com.google.api.services.sheets.v4.model.*;
import vn.conyeu.google.core.Utils;

import java.util.List;

public class CellDataBuilder implements XmlBuilder<CellData> {
    private final CellData cell;

    public CellDataBuilder(final CellData cell) {
        this.cell = XmlBuilder.ifNull(cell, CellData::new);
    }

    @Override
    public CellData build() {
        return cell;
    }

    /**
     * A data validation rule on the cell, if any.
     * <p>
     * When writing, the new data validation rule will overwrite any prior rule.
     *
     * @param dataValidation dataValidation or {@code null} for none
     */
    public CellDataBuilder dataValidation(DataValidationRule dataValidation) {
        cell.setDataValidation(dataValidation);
        return this;
    }

    /**
     * A hyperlink this cell points to, if any. This field is read-only.  (To set it, use a
     * `=HYPERLINK` formula in the userEnteredValue.formulaValue field.)
     *
     * @param hyperlink hyperlink or {@code null} for none
     */
    public CellDataBuilder hyperlink(String hyperlink) {
        cell.setHyperlink(hyperlink);
        return this;
    }

    /**
     * Any note on the cell.
     *
     * @param note note or {@code null} for none
     */
    public CellDataBuilder note(String note) {
        cell.setNote(note);
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
    public CellDataBuilder pivotTable(PivotTable pivotTable) {
        cell.setPivotTable(pivotTable);
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
    public CellDataBuilder textFormatRuns(List<TextFormatRun> textFormatRuns) {
        cell.setTextFormatRuns(textFormatRuns);
        return this;
    }

    /**
     * The format the user entered for the cell.
     * <p>
     * When writing, the new format will be merged with the existing format.
     *
     * @param format userEnteredFormat or {@code null} for none
     */
    public CellDataBuilder cellFormat(ConsumerReturn<CellFormatBuilder> format) {
        format.accept(initCellFormat());
        return this;
    }

    /**
     * The value the user entered in the cell. e.g, `1234`, `'Hello'`, or `=NOW()` Note: Dates, Times
     * and DateTimes are represented as doubles in serial number format.
     *
     * @param userEnteredValue userEnteredValue or {@code null} for none
     */
    public CellDataBuilder userEnteredValue(ExtendedValue userEnteredValue) {
        cell.setUserEnteredValue(userEnteredValue);
        return this;
    }

    private CellFormatBuilder cellFormatBuilder;
    private CellFormatBuilder initCellFormat() {
        if(cellFormatBuilder == null) {
            cellFormatBuilder = new CellFormatBuilder(Utils.setIfNull(
                    cell::getUserEnteredFormat, CellFormat::new,
                    cell::setUserEnteredFormat));
        }
        return cellFormatBuilder;
    }
}