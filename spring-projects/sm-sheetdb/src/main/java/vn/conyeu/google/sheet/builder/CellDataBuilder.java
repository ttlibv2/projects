package vn.conyeu.google.sheet.builder;

import com.google.api.services.sheets.v4.model.*;
import vn.conyeu.google.core.Utils;

import java.util.List;

public class CellDataBuilder implements XmlBuilder<CellData> {
    private final CellData cell;
    private ValueBuilder valueBuilder;
    private CellFormatBuilder formatBuilder;
    private DataValidationRuleBuilder ruleBuilder;

    public CellDataBuilder(CellData cell) {
        this.cell = Utils.getIfNull(cell, CellData::new);
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
     * @param consumer dataValidation or {@code null} for none
     */
    public CellDataBuilder dataValidation(ConsumerReturn<DataValidationRuleBuilder> consumer) {
        consumer.accept(getRuleBuilder());
        return this;
    }

    /**
     * The formatted value of the cell. This is the value as it's shown to the user. This field is
     * read-only.
     *
     * @param formattedValue formattedValue or {@code null} for none
     */
    public CellDataBuilder formattedValue(String formattedValue) {
        cell.setFormattedValue(formattedValue);
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
     * Represents a string resetAll(). Leading single quotes are not included. For example, if the user
     * typed `'123` into the UI, this would be represented as a `stringValue` of `"123"`.
     *
     * @param stringValue stringValue or {@code null} for none
     */
    public CellDataBuilder stringValue(String stringValue) {
        initValue().stringValue(stringValue);
        return this;
    }

    /**
     * Represents a double resetAll(). Note: Dates, Times and DateTimes are represented as doubles in
     * "serial number" format.
     *
     * @param numberValue numberValue or {@code null} for none
     */
    public CellDataBuilder numberValue(Double numberValue) {
        initValue().numberValue(numberValue);
        return this;
    }

    /**
     * Represents a formula.
     *
     * @param formulaValue formulaValue or {@code null} for none
     */
    public CellDataBuilder formulaValue(String formulaValue) {
        initValue().formulaValue(formulaValue);
        return this;
    }

    /**
     * Represents an error. This field is read-only.
     *
     * @param errorValue errorValue or {@code null} for none
     */
    public CellDataBuilder errorValue(ErrorValue errorValue) {
        initValue().errorValue(errorValue);
        return this;
    }

    /**
     * Represents a boolean resetAll().
     *
     * @param boolValue boolValue or {@code null} for none
     */
    public CellDataBuilder boolValue(Boolean boolValue) {
        initValue().boolValue(boolValue);
        return this;
    }

    /**
     * The wrap strategy for the value in the cell.
     *
     * @param wrapStrategy wrapStrategy or {@code null} for none
     */
    public CellDataBuilder wrapStrategy(WrapStrategy wrapStrategy) {
        initFormat().wrapStrategy(wrapStrategy);
        return this;
    }

    /**
     * The vertical alignment of the value in the cell.
     *
     * @param verticalAlignment verticalAlignment or {@code null} for none
     */
    public CellDataBuilder verticalAlignment(VerticalAlign verticalAlignment) {
        initFormat().verticalAlignment(verticalAlignment);
        return this;
    }

    /**
     * The left border of the cell.
     *
     * @param left left or {@code null} for none
     */
    public CellDataBuilder leftBorder(ConsumerReturn<BorderBuilder> left) {
        initFormat().leftBorder(left);
        return this;
    }

    /**
     * How a hyperlink, if it exists, should be displayed in the cell.
     *
     * @param hyperlinkDisplayType hyperlinkDisplayType or {@code null} for none
     */
    public CellDataBuilder hyperlinkDisplayType(HyperlinkDisplayType hyperlinkDisplayType) {
        initFormat().hyperlinkDisplayType(hyperlinkDisplayType);
        return this;
    }

    /**
     * The rotation applied to text in a cell
     *
     * @param angle    The angle between the standard orientation and the desired orientation. Measured in degrees. Valid values are between -90 and 90. Positive angles are angled upwards, negative are angled downwards. Note: For LTR text direction positive angles are in the counterclockwise direction, whereas for RTL they are in the clockwise direction
     * @param vertical If true, text reads top to bottom, but the orientation of individual characters is unchanged. For example: | V | | e | | r | | t | | i | | c | | a | | l |
     */
    public CellDataBuilder textRotation(Integer angle, Boolean vertical) {
        initFormat().textRotation(angle, vertical);
        return this;
    }

    /**
     * True if the text is bold.
     *
     * @param bold bold or {@code null} for none
     */
    public CellDataBuilder bold(Boolean bold) {
        initFormat().bold(bold);
        return this;
    }

    /**
     * True if the text is underlined.
     *
     * @param underline underline or {@code null} for none
     */
    public CellDataBuilder underline(Boolean underline) {
        initFormat().underline(underline);
        return this;
    }

    /**
     * The background color of the cell
     *
     * @param rgbColor RGB color
     */
    public CellDataBuilder backgroundColorStyle(Color rgbColor) {
        initFormat().backgroundColorStyle(rgbColor);
        return this;
    }

    /**
     * A format describing how number values should be represented to the user.
     *
     * @param pattern Pattern string used for formatting. If not set, a default pattern based on the user's locale will be used if necessary for the given type. See the [Date and Number Formats guide](/ sheets/ api/ guides/ formats) for more information about the supported patterns.
     * @param type    The type of the number format. When writing, this field must be set.
     */
    public CellDataBuilder numberFormat(String pattern, String type) {
        initFormat().numberFormat(pattern, type);
        return this;
    }

    /**
     * The horizontal alignment of the value in the cell.
     *
     * @param horizontalAlignment horizontalAlignment or {@code null} for none
     */
    public CellDataBuilder horizontalAlignment(HorizontalAlign horizontalAlignment) {
        initFormat().horizontalAlignment(horizontalAlignment);
        return this;
    }

    /**
     * The direction of the text in the cell.
     *
     * @param textDirection textDirection or {@code null} for none
     */
    public CellDataBuilder textDirection(TextDirection textDirection) {
        initFormat().textDirection(textDirection);
        return this;
    }

    /**
     * The background color of the cell
     *
     * @param themeColor Theme color
     */
    public CellDataBuilder backgroundColorStyle(ThemeColorType themeColor) {
        initFormat().backgroundColorStyle(themeColor);
        return this;
    }

    /**
     * The bottom border of the cell.
     *
     * @param bottom bottom or {@code null} for none
     */
    public CellDataBuilder bottomBorder(ConsumerReturn<BorderBuilder> bottom) {
        initFormat().bottomBorder(bottom);
        return this;
    }

    /**
     * The padding of the cell.
     * @param topBottom The top+bottom padding of the cell.
     * @param leftRight The left+right padding of the cell.
     */
    public CellDataBuilder padding(Integer topBottom, Integer leftRight) {
        initFormat().padding(topBottom, leftRight);
        return this;
    }

    /**
     * True if the text has a strikethrough.
     *
     * @param strikethrough strikethrough or {@code null} for none
     */
    public CellDataBuilder strikethrough(Boolean strikethrough) {
        initFormat().strikethrough(strikethrough);
        return this;
    }

    /**
     * True if the text is italicized.
     *
     * @param italic italic or {@code null} for none
     */
    public CellDataBuilder italic(Boolean italic) {
        initFormat().italic(italic);
        return this;
    }

    /**
     * The foreground color of the text.
     *
     * @param rgbColor RGB color
     */
    public CellDataBuilder foregroundColorStyle(Color rgbColor) {
        initFormat().foregroundColorStyle(rgbColor);
        return this;
    }

    /**
     * The foreground color of the text.
     *
     * @param themeColor Theme color
     */
    public CellDataBuilder foregroundColorStyle(ThemeColorType themeColor) {
        initFormat().foregroundColorStyle(themeColor);
        return this;
    }

    /**
     * The size of the font.
     *
     * @param fontSize fontSize or {@code null} for none
     */
    public CellDataBuilder fontSize(Integer fontSize) {
        initFormat().fontSize(fontSize);
        return this;
    }

    /**
     * The padding of the cell.
     * @param top The top padding of the cell.
     * @param right The right padding of the cell.
     * @param bottom The bottom padding of the cell.
     * @param left The left padding of the cell.
     */
    public CellDataBuilder padding(Integer top, Integer right, Integer bottom, Integer left) {
        initFormat().padding(top, right, bottom, left);
        return this;
    }

    /**
     * The top border of the cell.
     *
     * @param top top or {@code null} for none
     */
    public CellDataBuilder topBorder(ConsumerReturn<BorderBuilder> top) {
        initFormat().topBorder(top);
        return this;
    }

    /**
     * The font family.
     *
     * @param fontFamily fontFamily or {@code null} for none
     */
    public CellDataBuilder fontFamily(String fontFamily) {
        initFormat().fontFamily(fontFamily);
        return this;
    }

    /**
     * The right border of the cell.
     *
     * @param right right or {@code null} for none
     */
    public CellDataBuilder rightBorder(ConsumerReturn<BorderBuilder> right) {
        initFormat().rightBorder(right);
        return this;
    }

    private CellFormatBuilder initFormat() {
        if (formatBuilder == null) {
            formatBuilder = new CellFormatBuilder(Utils.setIfNull(cell::getUserEnteredFormat, CellFormat::new, cell::setUserEnteredFormat));
        }
        return formatBuilder;
    }

    private ValueBuilder initValue() {
        if (valueBuilder == null) {
            valueBuilder = new ValueBuilder(Utils.setIfNull(cell::getUserEnteredValue, ExtendedValue::new, cell::setUserEnteredValue));
        }
        return valueBuilder;
    }

    private DataValidationRuleBuilder getRuleBuilder() {
        if(ruleBuilder == null) {
            ruleBuilder = new DataValidationRuleBuilder(Utils.setIfNull(cell::getDataValidation,
                    DataValidationRule::new, cell::setDataValidation));
        }
        return ruleBuilder;
    }
}
