package vn.conyeu.google.sheetdb.builder;

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

    public CellFormatBuilder horizontalAlignment(HorizontalAlign align) {
        cellFormat.setHorizontalAlignment(align.name());
        return this;
    }

    public CellFormatBuilder hyperlinkDisplayType(String hyperlinkDisplayType) {
        cellFormat.setHyperlinkDisplayType(hyperlinkDisplayType);
        return this;
    }

    public CellFormatBuilder numberFormat(ConsumerReturn<NumberFormatBuilder> format) {
        format.accept(initNumberFormat());
        return this;
    }

    public CellFormatBuilder padding(ConsumerReturn<Padding> padding) {
        padding.accept(Utils.setIfNull(cellFormat::getPadding, Padding::new, cellFormat::setPadding));
        return this;
    }

    public CellFormatBuilder textDirection(String textDirection) {
        cellFormat.setTextDirection(textDirection);
        return this;
    }

    public CellFormatBuilder textFormat(ConsumerReturn<TextFormatBuilder> textFormat) {
        textFormat.accept(initTextFormat());
        return this;
    }

    public CellFormatBuilder textRotation(ConsumerReturn<TextRotation> textRotation) {
        textRotation.accept(Utils.setIfNull(cellFormat::getTextRotation, TextRotation::new, cellFormat::setTextRotation));
        return this;
    }




    private BordersBuilder initBorders() {
        if(bordersBuilder == null) {
            Borders borders = Utils.setIfNull(cellFormat::getBorders, Borders::new, cellFormat::setBorders);
            bordersBuilder = new BordersBuilder(borders);
        }
        return bordersBuilder;
    }


    private TextFormatBuilder initTextFormat() {
        if(textFormatBuilder == null) {
            TextFormat value = Utils.setIfNull(cellFormat::getTextFormat, TextFormat::new, cellFormat::setTextFormat);
            textFormatBuilder = new TextFormatBuilder(value);
        }
        return textFormatBuilder;
    }
    private NumberFormatBuilder initNumberFormat() {
        if(numberFormatBuilder == null) {
            NumberFormat nf = Utils.setIfNull(cellFormat::getNumberFormat, NumberFormat::new, cellFormat::setNumberFormat);
            numberFormatBuilder = new NumberFormatBuilder(nf);
        }
        return numberFormatBuilder;
    }

}