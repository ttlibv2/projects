package vn.conyeu.google.sheet.builder;

import com.google.api.services.sheets.v4.model.*;
import vn.conyeu.google.core.GoogleException;
import vn.conyeu.google.core.Utils;
import vn.conyeu.google.xsldb.ColumnType;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class CellBuilder implements XmlBuilder<CellData> {
    private final CellData cell;
    private ValueBuilder valueBuilder;
    private CellFormatBuilder formatBuilder;
    private DataValidationRuleBuilder ruleBuilder;

    public CellBuilder(CellData cell) {
        this.cell = Utils.getIfNull(cell, CellData::new);
    }

    @Override
    public CellBuilder copy() {
        return new CellBuilder(cell.clone());
    }

    public CellData build() {
        return cell;
    }

    public String getValue() {
        return cell.getFormattedValue();
    }

    /**
     * A data validation rule on the cell, if any.
     * <p>
     * When writing, the new data validation rule will overwrite any prior rule.
     *
     * @param consumer dataValidation or {@code null} for none
     */
    public CellBuilder dataValidation(ConsumerReturn<DataValidationRuleBuilder> consumer) {
        consumer.accept(initRuleBuilder());
        return this;
    }

    /**
     * The formatted value of the cell. This is the value as it's shown to the user. This field is
     * read-only.
     *
     * @param formattedValue formattedValue or {@code null} for none
     */
    public CellBuilder formattedValue(String formattedValue) {
        cell.setFormattedValue(formattedValue);
        return this;
    }

    /**
     * Any note on the cell.
     *
     * @param note note or {@code null} for none
     */
    public CellBuilder note(String note) {
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
    public CellBuilder pivotTable(PivotTable pivotTable) {
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
    public CellBuilder textFormatRuns(List<TextFormatRun> textFormatRuns) {
        cell.setTextFormatRuns(textFormatRuns);
        return this;
    }

    /**
     * Represents a string resetAll(). Leading single quotes are not included. For example, if the user
     * typed `'123` into the UI, this would be represented as a `stringValue` of `"123"`.
     *
     * @param stringValue stringValue or {@code null} for none
     */
    public CellBuilder stringValue(String stringValue) {
        initValue().stringValue(stringValue);
        return this;
    }

    /**
     * Represents a double resetAll(). Note: Dates, Times and DateTimes are represented as doubles in
     * "serial number" format.
     *
     * @param numberValue numberValue or {@code null} for none
     */
    public CellBuilder numberValue(Double numberValue) {
        initValue().numberValue(numberValue);
        return this;
    }

    /**
     * Represents a formula.
     *
     * @param formulaValue formulaValue or {@code null} for none
     */
    public CellBuilder formulaValue(String formulaValue) {
        initValue().formulaValue(formulaValue);
        return this;
    }

    /**
     * Represents a boolean resetAll().
     *
     * @param boolValue boolValue or {@code null} for none
     */
    public CellBuilder boolValue(Boolean boolValue) {
        initValue().boolValue(boolValue);
        return this;
    }

    public CellBuilder dateTimeValue(LocalDateTime dt) {
        return dateTimeValue(dt, SheetUtil.ISO_DATETIME);
    }

    public CellBuilder dateTimeValue(LocalDateTime dateTime, DateTimeFormatter format) {
        return stringValue(dateTime.format(format));
    }

    public CellBuilder dateValue(LocalDate dt) {
        return dateValue(dt, SheetUtil.ISO_DATE);
    }

    public CellBuilder dateValue(LocalDate dt, DateTimeFormatter format) {
        return stringValue(dt.format(format));
    }

    public CellBuilder timeValue(LocalTime dt) {
        return timeValue(dt, SheetUtil.ISO_TIME);
    }

    public CellBuilder timeValue(LocalTime dt, DateTimeFormatter format) {
        return stringValue(dt.format(format));
    }

    public CellBuilder setValue(Object value) {
        if(value == null) stringValue(null);
        else if(value instanceof String str) {
            if(str.startsWith("="))formulaValue(str);
            else stringValue(str);
        }
        else if(value instanceof Boolean bool) boolValue(bool);
        else if(value instanceof Number in) numberValue(in.doubleValue());
        else if(value instanceof LocalDateTime dt) dateTimeValue(dt);
        else if(value instanceof LocalDate lc) dateValue(lc);
        else if(value instanceof LocalTime lt) timeValue(lt);
        else throw new GoogleException("The value type '%s' not support", value.getClass());
        return this;
    }



    /**
     * The wrap strategy for the value in the cell.
     *
     * @param wrapStrategy wrapStrategy or {@code null} for none
     */
    public CellBuilder wrapStrategy(WrapStrategy wrapStrategy) {
        initFormat().wrapStrategy(wrapStrategy);
        return this;
    }

    /**
     * The vertical alignment of the value in the cell.
     *
     * @param verticalAlignment verticalAlignment or {@code null} for none
     */
    public CellBuilder verticalAlignment(VerticalAlign verticalAlignment) {
        initFormat().verticalAlignment(verticalAlignment);
        return this;
    }

    /**
     * The left border of the cell.
     *
     * @param left left or {@code null} for none
     */
    public CellBuilder leftBorder(ConsumerReturn<BorderBuilder> left) {
        initFormat().leftBorder(left);
        return this;
    }

    /**
     * How a hyperlink, if it exists, should be displayed in the cell.
     *
     * @param hyperlinkDisplayType hyperlinkDisplayType or {@code null} for none
     */
    public CellBuilder hyperlinkDisplayType(HyperlinkDisplayType hyperlinkDisplayType) {
        initFormat().hyperlinkDisplayType(hyperlinkDisplayType);
        return this;
    }

    /**
     * The rotation applied to text in a cell
     *
     * @param angle    The angle between the standard orientation and the desired orientation. Measured in degrees. Valid values are between -90 and 90. Positive angles are angled upwards, negative are angled downwards. Note: For LTR text direction positive angles are in the counterclockwise direction, whereas for RTL they are in the clockwise direction
     * @param vertical If true, text reads top to bottom, but the orientation of individual characters is unchanged. For example: | V | | e | | r | | t | | i | | c | | a | | l |
     */
    public CellBuilder textRotation(Integer angle, Boolean vertical) {
        initFormat().textRotation(angle, vertical);
        return this;
    }

    /**
     * True if the text is bold.
     *
     * @param bold bold or {@code null} for none
     */
    public CellBuilder bold(Boolean bold) {
        initFormat().bold(bold);
        return this;
    }

    /**
     * True if the text is underlined.
     *
     * @param underline underline or {@code null} for none
     */
    public CellBuilder underline(Boolean underline) {
        initFormat().underline(underline);
        return this;
    }

    /**
     * The background color of the cell
     *
     * @param rgbColor RGB color
     */
    public CellBuilder backgroundColorStyle(Color rgbColor) {
        initFormat().backgroundColorStyle(rgbColor);
        return this;
    }

    /**
     * A format describing how number values should be represented to the user.
     *
     * @param pattern Pattern string used for formatting. If not set, a default pattern based on the user's locale will be used if necessary for the given type. See the [Date and Number Formats guide](/ sheets/ api/ guides/ formats) for more information about the supported patterns.
     * @param type    The type of the number format. When writing, this field must be set.
     */
    public CellBuilder numberFormat(NumberFormatType type, String pattern) {
        initFormat().numberFormat(type, pattern);
        return this;
    }


    /**
     * A format describing how number values should be represented to the user.
     * @see #numberFormat(NumberFormatType, String)
     * */
    public CellBuilder numberFormat(String pattern) {
         initFormat().numberFormat(pattern);
        return this;
    }


    /**
     * A format describing how number values should be represented to the user.
     * @see #numberFormat(NumberFormatType, String)
     * */
    public CellBuilder numberFormat(NumberFormatPattern pattern) {
        return numberFormat(pattern.formatType, pattern.pattern);
    }

    /**
     * The horizontal alignment of the value in the cell.
     *
     * @param horizontalAlignment horizontalAlignment or {@code null} for none
     */
    public CellBuilder horizontalAlignment(HorizontalAlign horizontalAlignment) {
        initFormat().horizontalAlignment(horizontalAlignment);
        return this;
    }

    /**
     * The direction of the text in the cell.
     *
     * @param textDirection textDirection or {@code null} for none
     */
    public CellBuilder textDirection(TextDirection textDirection) {
        initFormat().textDirection(textDirection);
        return this;
    }

    /**
     * The background color of the cell
     *
     * @param themeColor Theme color
     */
    public CellBuilder backgroundColorStyle(ThemeColorType themeColor) {
        initFormat().backgroundColorStyle(themeColor);
        return this;
    }

    /**
     * The bottom border of the cell.
     *
     * @param bottom bottom or {@code null} for none
     */
    public CellBuilder bottomBorder(ConsumerReturn<BorderBuilder> bottom) {
        initFormat().bottomBorder(bottom);
        return this;
    }


    /**
     * True if the text has a strikethrough.
     *
     * @param strikethrough strikethrough or {@code null} for none
     */
    public CellBuilder strikethrough(Boolean strikethrough) {
        initFormat().strikethrough(strikethrough);
        return this;
    }

    /**
     * True if the text is italicized.
     *
     * @param italic italic or {@code null} for none
     */
    public CellBuilder italic(Boolean italic) {
        initFormat().italic(italic);
        return this;
    }

    /**
     * The foreground color of the text.
     *
     * @param rgbColor RGB color
     */
    public CellBuilder foregroundColorStyle(Color rgbColor) {
        initFormat().foregroundColorStyle(rgbColor);
        return this;
    }

    /**
     * The foreground color of the text.
     *
     * @param themeColor Theme color
     */
    public CellBuilder foregroundColorStyle(ThemeColorType themeColor) {
        initFormat().foregroundColorStyle(themeColor);
        return this;
    }

    /**
     * The size of the font.
     *
     * @param fontSize fontSize or {@code null} for none
     */
    public CellBuilder fontSize(Integer fontSize) {
        initFormat().fontSize(fontSize);
        return this;
    }


    /**
     * The padding of the cell.
     * @param topBottom The top+bottom padding of the cell.
     * @param leftRight The left+right padding of the cell.
     */
    public CellBuilder padding(Integer topBottom, Integer leftRight) {
        initFormat().padding(topBottom, leftRight);
        return this;
    }

    /**
     * The padding of the cell.
     */
    public CellBuilder padding(ConsumerReturn<Padding> consumer) {
        initFormat().padding(consumer);
        return this;
    }

    /**
     * The top border of the cell.
     *
     * @param top top or {@code null} for none
     */
    public CellBuilder topBorder(ConsumerReturn<BorderBuilder> top) {
        initFormat().topBorder(top);
        return this;
    }

    /**
     * The font family.
     *
     * @param fontFamily fontFamily or {@code null} for none
     */
    public CellBuilder fontFamily(String fontFamily) {
        initFormat().fontFamily(fontFamily);
        return this;
    }

    public CellBuilder format(ConsumerReturn<CellFormatBuilder> consumer) {
        consumer.accept(initFormat());
        return this;
    }

    /**
     * The right border of the cell.
     *
     * @param right right or {@code null} for none
     */
    public CellBuilder rightBorder(ConsumerReturn<BorderBuilder> right) {
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

    private DataValidationRuleBuilder initRuleBuilder() {
        if(ruleBuilder == null) {
            ruleBuilder = new DataValidationRuleBuilder(Utils.setIfNull(cell::getDataValidation,
                    DataValidationRule::new, cell::setDataValidation));
        }
        return ruleBuilder;
    }

    public void applyColumn(ColumnType type) {
        numberFormat(type.getType(), type.getPattern());
    }




}