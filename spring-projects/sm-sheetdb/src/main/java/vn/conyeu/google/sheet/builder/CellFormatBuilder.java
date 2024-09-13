package vn.conyeu.google.sheet.builder;

import com.google.api.services.sheets.v4.model.*;
import vn.conyeu.google.core.Utils;

public class CellFormatBuilder implements XmlBuilder<CellFormat> {
    private final CellFormat cellFormat;
    private BordersBuilder bordersBuilder;
    private TextFormatBuilder textFormatBuilder;

    public CellFormatBuilder(CellFormat cellFormat) {
        this.cellFormat = Utils.getIfNull(cellFormat, CellFormat::new);
    }

    @Override
    public CellFormat build() {
        return cellFormat;
    }

    public CellFormatBuilder clearFormat() {
        cellFormat.setTextFormat(new TextFormat());
        return this;
    }

    /**
     * The wrap strategy for the value in the cell.
     *
     * @param wrapStrategy wrapStrategy or {@code null} for none
     */
    public CellFormatBuilder wrapStrategy(WrapStrategy wrapStrategy) {
        cellFormat.setWrapStrategy(Utils.enumName(wrapStrategy));
        return this;
    }

    /**
     * The vertical alignment of the value in the cell.
     *
     * @param verticalAlignment verticalAlignment or {@code null} for none
     */
    public CellFormatBuilder verticalAlignment(VerticalAlign verticalAlignment) {
        cellFormat.setVerticalAlignment(Utils.enumName(verticalAlignment));
        return this;
    }

    /**
     * The rotation applied to text in a cell
     *
     * @param angle    The angle between the standard orientation and the desired orientation. Measured in degrees. Valid values are between -90 and 90. Positive angles are angled upwards, negative are angled downwards. Note: For LTR text direction positive angles are in the counterclockwise direction, whereas for RTL they are in the clockwise direction
     * @param vertical If true, text reads top to bottom, but the orientation of individual characters is unchanged. For example: | V | | e | | r | | t | | i | | c | | a | | l |
     */
    public CellFormatBuilder textRotation(Integer angle, Boolean vertical) {
        cellFormat.setTextRotation(new TextRotation().setAngle(angle).setVertical(vertical));
        return this;
    }

    /**
     * True if the text is underlined.
     *
     * @param underline underline or {@code null} for none
     */
    public CellFormatBuilder underline(Boolean underline) {
        initFormat().underline(underline);
        return this;
    }

    /**
     * True if the text has a strikethrough.
     *
     * @param strikethrough strikethrough or {@code null} for none
     */
    public CellFormatBuilder strikethrough(Boolean strikethrough) {
        initFormat().strikethrough(strikethrough);
        return this;
    }

    /**
     * True if the text is italicized.
     *
     * @param italic italic or {@code null} for none
     */
    public CellFormatBuilder italic(Boolean italic) {
        initFormat().italic(italic);
        return this;
    }

    /**
     * The foreground color of the text.
     *
     * @param rgbColor RGB color
     */
    public CellFormatBuilder foregroundColorStyle(Color rgbColor) {
        initFormat().foregroundColorStyle(rgbColor);
        return this;
    }

    /**
     * The foreground color of the text.
     *
     * @param themeColor Theme color
     */
    public CellFormatBuilder foregroundColorStyle(ThemeColorType themeColor) {
        initFormat().foregroundColorStyle(themeColor);
        return this;
    }

    /**
     * The size of the font.
     *
     * @param fontSize fontSize or {@code null} for none
     */
    public CellFormatBuilder fontSize(Integer fontSize) {
        initFormat().fontSize(fontSize);
        return this;
    }

    /**
     * The font family.
     *
     * @param fontFamily fontFamily or {@code null} for none
     */
    public CellFormatBuilder fontFamily(String fontFamily) {
        initFormat().fontFamily(fontFamily);
        return this;
    }

    /**
     * True if the text is bold.
     *
     * @param bold bold or {@code null} for none
     */
    public CellFormatBuilder bold(Boolean bold) {
        initFormat().bold(bold);
        return this;
    }

    /**
     * The direction of the text in the cell.
     *
     * @param textDirection textDirection or {@code null} for none
     */
    public CellFormatBuilder textDirection(TextDirection textDirection) {
        cellFormat.setTextDirection(Utils.enumName(textDirection));
        return this;
    }

    /**
     * The padding of the cell.
     * @param topBottom The top+bottom padding of the cell.
     * @param leftRight The left+right padding of the cell.
     */
    public CellFormatBuilder padding(Integer topBottom, Integer leftRight) {
        padding(p -> p.setTop(topBottom).setBottom(topBottom).setLeft(leftRight).setRight(leftRight));
        return this;
    }

    /**
     * The padding of the cell.
     */
    public CellFormatBuilder padding(ConsumerReturn<Padding> consumer) {
        consumer.accept(initPadding());
        return this;
    }

    private Padding initPadding() {
        return Utils.setIfNull(cellFormat::getPadding, Padding::new, cellFormat::setPadding);
    }

    private NumberFormat initNumberFormat() {
        return Utils.setIfNull(cellFormat::getNumberFormat, NumberFormat::new, cellFormat::setNumberFormat);
    }

    /**
     * A format describing how number values should be represented to the user.
     *
     * @param pattern Pattern string used for formatting. If not set, a default pattern based on the user's locale will be used if necessary for the given type. See the [Date and Number Formats guide](/ sheets/ api/ guides/ formats) for more information about the supported patterns.
     * @param type    The type of the number format. When writing, this field must be set.
     */
    public CellFormatBuilder numberFormat(NumberFormatType type, String pattern) {
        initNumberFormat().setPattern(pattern).setType(Utils.enumName(type));
        return this;
    }

    /**
     * A format describing how number values should be represented to the user.
     * @see #numberFormat(NumberFormatType, String)
     * */
    public CellFormatBuilder numberFormat(NumberFormatPattern pattern) {
        return numberFormat(pattern.formatType, pattern.pattern);
    }
    /**
     * A format describing how number values should be represented to the user.
     * @see #numberFormat(NumberFormatType, String)
     * */
    public CellFormatBuilder numberFormat( String pattern) {
        initNumberFormat().setPattern(pattern);
        return this;
    }

    /**
     * How a hyperlink, if it exists, should be displayed in the cell.
     *
     * @param hyperlinkDisplayType hyperlinkDisplayType or {@code null} for none
     */
    public CellFormatBuilder hyperlinkDisplayType(HyperlinkDisplayType hyperlinkDisplayType) {
        cellFormat.setHyperlinkDisplayType(Utils.enumName(hyperlinkDisplayType));
        return this;
    }

    /**
     * The horizontal alignment of the value in the cell.
     *
     * @param horizontalAlignment horizontalAlignment or {@code null} for none
     */
    public CellFormatBuilder horizontalAlignment(HorizontalAlign horizontalAlignment) {
        cellFormat.setHorizontalAlignment(Utils.enumName(horizontalAlignment));
        return this;
    }

    /**
     * The background color of the cell
     *
     * @param rgbColor RGB color
     */
    public CellFormatBuilder backgroundColorStyle(Color rgbColor) {
        cellFormat.setBackgroundColorStyle(new ColorStyle().setRgbColor(rgbColor));
        return this;
    }

    /**
     * The background color of the cell
     *
     * @param themeColor Theme color
     */
    public CellFormatBuilder backgroundColorStyle(ThemeColorType themeColor) {
        cellFormat.setBackgroundColorStyle(new ColorStyle().setThemeColor(Utils.enumName(themeColor)));
        return this;
    }

    /**
     * The bottom border of the cell.
     *
     * @param bottom bottom or {@code null} for none
     */
    public BordersBuilder bottomBorder(ConsumerReturn<BorderBuilder> bottom) {
        return initBorders().bottom(bottom);
    }

    /**
     * The left border of the cell.
     *
     * @param left left or {@code null} for none
     */
    public BordersBuilder leftBorder(ConsumerReturn<BorderBuilder> left) {
        return initBorders().left(left);
    }

    /**
     * The right border of the cell.
     *
     * @param right right or {@code null} for none
     */
    public BordersBuilder rightBorder(ConsumerReturn<BorderBuilder> right) {
        return initBorders().right(right);
    }

    /**
     * The top border of the cell.
     *
     * @param top top or {@code null} for none
     */
    public BordersBuilder topBorder(ConsumerReturn<BorderBuilder> top) {
        return initBorders().top(top);
    }

    private TextFormatBuilder initFormat() {
        if (textFormatBuilder == null) {
            textFormatBuilder = new TextFormatBuilder(Utils.setIfNull(cellFormat::getTextFormat,
                    TextFormat::new, cellFormat::setTextFormat));
        }
        return textFormatBuilder;
    }

    private BordersBuilder initBorders() {
        if(bordersBuilder == null) {
            bordersBuilder = new BordersBuilder(Utils.setIfNull(cellFormat::getBorders, Borders::new, cellFormat::setBorders));
        }
        return bordersBuilder;
    }

}