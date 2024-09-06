package vn.conyeu.google.sheet.builder;

import com.google.api.services.sheets.v4.model.*;
import vn.conyeu.google.core.Utils;

public class CellFormatBuilder implements XmlBuilder<CellFormat> {
    private final CellFormat cellFormat;
    private BordersBuilder bordersBuilder;
    private NumberFormatBuilder numberFormatBuilder;
    private TextFormatBuilder textFormatBuilder;

    public CellFormatBuilder() {
        this(null);
    }

    public CellFormatBuilder(CellFormat cellFormat) {
        this.cellFormat = cellFormat == null ? new CellFormat() : cellFormat;
    }

    public static CellFormatBuilder create(CellFormat cellFormat) {
        return new CellFormatBuilder(cellFormat);
    }

    @Override
    public CellFormat build() {
        return cellFormat;
    }

    public CellFormatBuilder verticalAlignment(VerticalAlign alignment) {
        cellFormat.setVerticalAlignment(alignment.name());
        return this;
    }

    public CellFormatBuilder wrapStrategy(WrapStrategy strategy) {
        cellFormat.setWrapStrategy(strategy.name());
        return this;
    }

    public CellFormatBuilder backgroundColorStyle(ConsumerReturn<ColorStyle> backgroundColorStyle) {
        backgroundColorStyle.accept(Utils.setIfNull(cellFormat::getBackgroundColorStyle,
                ColorStyle::new, cellFormat::setBackgroundColorStyle));
        return this;
    }

    public CellFormatBuilder borders(ConsumerReturn<BordersBuilder> borders) {
        borders.accept(initBorders());
        return this;
    }

    /**
     * The top border of the cell.
     *
     * @param top top or {@code null} for none
     */
    public CellFormatBuilder borderTop(ConsumerReturn<BorderBuilder> top) {
        initBorders().top(top);
        return this;
    }

    /**
     * The bottom border of the cell.
     *
     * @param bottom bottom or {@code null} for none
     */
    public CellFormatBuilder borderBottom(ConsumerReturn<BorderBuilder> bottom) {
        initBorders().bottom(bottom);
        return this;
    }

    /**
     * The left border of the cell.
     *
     * @param left left or {@code null} for none
     */
    public CellFormatBuilder borderLeft(ConsumerReturn<BorderBuilder> left) {
        initBorders().left(left);
        return this;
    }

    /**
     * The right border of the cell.
     *
     * @param right right or {@code null} for none
     */
    public CellFormatBuilder borderRight(ConsumerReturn<BorderBuilder> right) {
        initBorders().right(right);
        return this;
    }

    /**
     * The background color of the cell.
     *
     * @param backgroundColor backgroundColor or {@code null} for none
     */
    public CellFormatBuilder backgroundColor(Color backgroundColor) {
        cellFormat.setBackgroundColor(backgroundColor);
        return this;
    }

    /**
     * The background color of the cell. If background_color is also set, this field takes precedence.
     *
     * @param backgroundColorStyle backgroundColorStyle or {@code null} for none
     */
    public CellFormatBuilder backgroundColorStyle(ColorStyle backgroundColorStyle) {
        cellFormat.setBackgroundColorStyle(backgroundColorStyle);
        return this;
    }

    /**
     * The borders of the cell.
     *
     * @param borders borders or {@code null} for none
     */
    public CellFormatBuilder borders(Borders borders) {
        cellFormat.setBorders(borders);
        return this;
    }

    /**
     * The horizontal alignment of the value in the cell.
     *
     * @param horizontalAlignment horizontalAlignment or {@code null} for none
     */
    public CellFormatBuilder horizontalAlignment(String horizontalAlignment) {
        cellFormat.setHorizontalAlignment(horizontalAlignment);
        return this;
    }

    /**
     * How a hyperlink, if it exists, should be displayed in the cell.
     *
     * @param hyperlinkDisplayType hyperlinkDisplayType or {@code null} for none
     */
    public CellFormatBuilder hyperlinkDisplayType(String hyperlinkDisplayType) {
        cellFormat.setHyperlinkDisplayType(hyperlinkDisplayType);
        return this;
    }

    /**
     * A format describing how number values should be represented to the user.
     *
     * @param numberFormat numberFormat or {@code null} for none
     */
    public CellFormatBuilder numberFormat(NumberFormat numberFormat) {
        cellFormat.setNumberFormat(numberFormat);
        return this;
    }

    /**
     * The padding of the cell.
     *
     * @param padding padding or {@code null} for none
     */
    public CellFormatBuilder padding(Padding padding) {
        cellFormat.setPadding(padding);
        return this;
    }

    /**
     * The direction of the text in the cell.
     *
     * @param textDirection textDirection or {@code null} for none
     */
    public CellFormatBuilder textDirection(String textDirection) {
        cellFormat.setTextDirection(textDirection);
        return this;
    }


    /**
     * The rotation applied to text in a cell
     *
     * @param textRotation textRotation or {@code null} for none
     */
    public CellFormatBuilder textRotation(TextRotation textRotation) {
        cellFormat.setTextRotation(textRotation);
        return this;
    }

    /**
     * The vertical alignment of the value in the cell.
     *
     * @param verticalAlignment verticalAlignment or {@code null} for none
     */
    public CellFormatBuilder verticalAlignment(String verticalAlignment) {
        cellFormat.setVerticalAlignment(verticalAlignment);
        return this;
    }

    /**
     * The wrap strategy for the value in the cell.
     *
     * @param wrapStrategy wrapStrategy or {@code null} for none
     */
    public CellFormatBuilder wrapStrategy(String wrapStrategy) {
        cellFormat.setWrapStrategy(wrapStrategy);
        return this;
    }

    /**
     * A format describing how number values should be represented to the user.
     * @param format numberFormat or {@code null} for none
     */
    public CellFormatBuilder numberFormat(ConsumerReturn<NumberFormatBuilder> format) {
        format.accept(initNumberFormat());
        return this;
    }

    /**
     * The padding of the cell.
     * @param padding padding or {@code null} for none
     */
    public CellFormatBuilder padding(ConsumerReturn<Padding> padding) {
        padding.accept(Utils.setIfNull(cellFormat::getPadding, Padding::new, cellFormat::setPadding));
        return this;
    }

    /**
     * The format of the text in the cell (unless overridden by a format run).
     *
     * @param consumer textFormat or {@code null} for none
     */
    public CellFormatBuilder textFormat(ConsumerReturn<TextFormatBuilder> consumer) {
        consumer.accept(initTextFormat());
        return this;
    }

    /**
     * The rotation applied to text in a cell
     * @param textRotation textRotation or {@code null} for none
     */
    public CellFormatBuilder textRotation(ConsumerReturn<TextRotation> textRotation) {
        textRotation.accept(Utils.setIfNull(cellFormat::getTextRotation, TextRotation::new, cellFormat::setTextRotation));
        return this;
    }


    private BordersBuilder initBorders() {
        if (bordersBuilder == null) {
            Borders borders = Utils.setIfNull(cellFormat::getBorders, Borders::new, cellFormat::setBorders);
            bordersBuilder = new BordersBuilder(borders);
        }
        return bordersBuilder;
    }


    private TextFormatBuilder initTextFormat() {
        if (textFormatBuilder == null) {
            TextFormat value = Utils.setIfNull(cellFormat::getTextFormat, TextFormat::new, cellFormat::setTextFormat);
            textFormatBuilder = new TextFormatBuilder(value);
        }
        return textFormatBuilder;
    }

    private NumberFormatBuilder initNumberFormat() {
        if (numberFormatBuilder == null) {
            NumberFormat nf = Utils.setIfNull(cellFormat::getNumberFormat, NumberFormat::new, cellFormat::setNumberFormat);
            numberFormatBuilder = new NumberFormatBuilder(nf);
        }
        return numberFormatBuilder;
    }

}