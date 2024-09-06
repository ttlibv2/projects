package vn.conyeu.google.sheet.builder;

import com.google.api.services.sheets.v4.model.*;
import vn.conyeu.google.core.Utils;

import java.util.List;

public class CellDataBuilder implements XmlBuilder<CellData> {
    private final CellData cell;
    private ExtendedValue extendedValue;

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
        format.accept(getCellFormat());
        return this;
    }

    public CellDataBuilder verticalAlignment(VerticalAlign alignment) {
        getCellFormat().verticalAlignment(alignment);
        return this;
    }

    public CellDataBuilder wrapStrategy(WrapStrategy strategy) {
        getCellFormat().wrapStrategy(strategy);
        return this;
    }

    public CellDataBuilder backgroundColorStyle(ConsumerReturn<ColorStyle> backgroundColorStyle) {
        getCellFormat().backgroundColorStyle(backgroundColorStyle);
        return this;
    }

    public CellDataBuilder borders(ConsumerReturn<BordersBuilder> borders) {
        getCellFormat().borders(borders);
        return this;
    }

    /**
     * The top border of the cell.
     *
     * @param top top or {@code null} for none
     */
    public CellDataBuilder borderTop(ConsumerReturn<BorderBuilder> top) {
        getCellFormat().borderTop(top);
        return this;
    }

    /**
     * The bottom border of the cell.
     *
     * @param bottom bottom or {@code null} for none
     */
    public CellDataBuilder borderBottom(ConsumerReturn<BorderBuilder> bottom) {
        getCellFormat().borderBottom(bottom);
        return this;
    }

    /**
     * The left border of the cell.
     *
     * @param left left or {@code null} for none
     */
    public CellDataBuilder borderLeft(ConsumerReturn<BorderBuilder> left) {
        getCellFormat().borderLeft(left);
        return this;
    }

    /**
     * The right border of the cell.
     *
     * @param right right or {@code null} for none
     */
    public CellDataBuilder borderRight(ConsumerReturn<BorderBuilder> right) {
        getCellFormat().borderRight(right);
        return this;
    }

    /**
     * The background color of the cell.
     *
     * @param backgroundColor backgroundColor or {@code null} for none
     */
    public CellDataBuilder backgroundColor(Color backgroundColor) {
        getCellFormat().backgroundColor(backgroundColor);
        return this;
    }

    /**
     * The background color of the cell. If background_color is also set, this field takes precedence.
     *
     * @param backgroundColorStyle backgroundColorStyle or {@code null} for none
     */
    public CellDataBuilder backgroundColorStyle(ColorStyle backgroundColorStyle) {
        getCellFormat().backgroundColorStyle(backgroundColorStyle);
        return this;
    }

    /**
     * The borders of the cell.
     *
     * @param borders borders or {@code null} for none
     */
    public CellDataBuilder borders(Borders borders) {
        getCellFormat().borders(borders);
        return this;
    }

    /**
     * The horizontal alignment of the value in the cell.
     *
     * @param horizontalAlignment horizontalAlignment or {@code null} for none
     */
    public CellDataBuilder horizontalAlignment(String horizontalAlignment) {
        getCellFormat().horizontalAlignment(horizontalAlignment);
        return this;
    }

    /**
     * How a hyperlink, if it exists, should be displayed in the cell.
     *
     * @param hyperlinkDisplayType hyperlinkDisplayType or {@code null} for none
     */
    public CellDataBuilder hyperlinkDisplayType(String hyperlinkDisplayType) {
        getCellFormat().hyperlinkDisplayType(hyperlinkDisplayType);
        return this;
    }

    /**
     * A format describing how number values should be represented to the user.
     *
     * @param numberFormat numberFormat or {@code null} for none
     */
    public CellDataBuilder numberFormat(NumberFormat numberFormat) {
        getCellFormat().numberFormat(numberFormat);
        return this;
    }

    /**
     * The padding of the cell.
     *
     * @param padding padding or {@code null} for none
     */
    public CellDataBuilder padding(Padding padding) {
        getCellFormat().padding(padding);
        return this;
    }

    /**
     * The direction of the text in the cell.
     *
     * @param textDirection textDirection or {@code null} for none
     */
    public CellDataBuilder textDirection(String textDirection) {
        getCellFormat().textDirection(textDirection);
        return this;
    }

    public CellDataBuilder bold(boolean bold) {
       textFormat(f -> f.bold(bold));
        return this;
    }

    public CellDataBuilder family(String family) {
        textFormat(f -> f.fontFamily(family));
        return this;
    }

    /**
     * The format of the text in the cell (unless overridden by a format run).
     *
     * @param consumer textFormat or {@code null} for none
     */
    public CellDataBuilder textFormat(ConsumerReturn<TextFormatBuilder> consumer) {
        getCellFormat().textFormat(consumer);
        return this;
    }

    /**
     * The rotation applied to text in a cell
     *
     * @param textRotation textRotation or {@code null} for none
     */
    public CellDataBuilder textRotation(TextRotation textRotation) {
        getCellFormat().textRotation(textRotation);
        return this;
    }

    /**
     * The vertical alignment of the value in the cell.
     *
     * @param verticalAlignment verticalAlignment or {@code null} for none
     */
    public CellDataBuilder verticalAlignment(String verticalAlignment) {
        getCellFormat().verticalAlignment(verticalAlignment);
        return this;
    }

    /**
     * The wrap strategy for the value in the cell.
     *
     * @param wrapStrategy wrapStrategy or {@code null} for none
     */
    public CellDataBuilder wrapStrategy(String wrapStrategy) {
        getCellFormat().wrapStrategy(wrapStrategy);
        return this;
    }

    /**
     * A format describing how number values should be represented to the user.
     * @param format numberFormat or {@code null} for none
     */
    public CellDataBuilder numberFormat(ConsumerReturn<NumberFormatBuilder> format) {
        getCellFormat().numberFormat(format);
        return this;
    }


    /**
     * The padding of the cell.
     * @param padding padding or {@code null} for none
     */
    public CellDataBuilder padding(ConsumerReturn<Padding> padding) {
        getCellFormat().padding(padding);
        return this;
    }
    /**
     * The rotation applied to text in a cell
     * @param textRotation textRotation or {@code null} for none
     */
    public CellDataBuilder textRotation(ConsumerReturn<TextRotation> textRotation) {
        getCellFormat().textRotation(textRotation);
        return this;
    }

    /**
     * The value the user entered in the cell. e.g, `1234`, `'Hello'`, or `=NOW()` Note: Dates, Times
     * and DateTimes are represented as doubles in serial number format.
     *
     * @param consumer userEnteredValue or {@code null} for none
     */
    private CellDataBuilder setValue(ConsumerReturn<ExtendedValue> consumer) {
        ExtendedValue value = consumer.accept(new ExtendedValue());
        cell.setUserEnteredValue(value);
        return this;
    }

    /**
     * Represents a string value. Leading single quotes are not included. For example, if the user
     * typed `'123` into the UI, this would be represented as a `stringValue` of `"123"`.
     * @param value stringValue or {@code null} for none
     */
    public CellDataBuilder value(String value) {
        return setValue(ext -> {
            if (value == null) ext.setStringValue(null);
            else if (value.startsWith("=")) ext.setFormulaValue(value);
            else ext.setStringValue(value);
            return ext;
        });
    }
    /**
     * Represents a boolean value.
     * @param boolValue boolValue or {@code null} for none
     */
    public CellDataBuilder value(Boolean boolValue) {
        return setValue(ext -> ext.setBoolValue(boolValue));
    }

    /**
     * Represents an error. This field is read-only.
     * @param errorValue errorValue or {@code null} for none
     */
    public CellDataBuilder value(ErrorValue errorValue) {
        return setValue(ext -> ext.setErrorValue(errorValue));
    }

    /**
     * Represents a double value. Note: Dates, Times and DateTimes are represented as doubles in
     * "serial number" format.
     * @param numberValue numberValue or {@code null} for none
     */
    public CellDataBuilder value(Double numberValue) {
        return setValue(ext -> ext.setNumberValue(numberValue));
    }

    private CellFormatBuilder cellFormatBuilder;


    private CellFormatBuilder getCellFormat() {
        if (cellFormatBuilder == null) {
            cellFormatBuilder = new CellFormatBuilder(Utils.setIfNull(
                    cell::getUserEnteredFormat, CellFormat::new,
                    cell::setUserEnteredFormat));
        }
        return cellFormatBuilder;
    }
}