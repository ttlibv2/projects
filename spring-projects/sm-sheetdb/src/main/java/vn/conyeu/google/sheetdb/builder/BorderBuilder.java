package vn.conyeu.google.sheetdb.builder;

import com.google.api.services.sheets.v4.model.Border;
import com.google.api.services.sheets.v4.model.Color;
import com.google.api.services.sheets.v4.model.ColorStyle;
import vn.conyeu.google.core.Utils;

public class BorderBuilder implements XmlBuilder<Border> {
    private final Border border;

    public BorderBuilder(Border border) {
        this.border = border == null ? new Border() : border;
    }

    @Override
    public Border build() {
        return border;
    }

    /**
     * The color of the border.
     *
     * @param color color or {@code null} for none
     */
    public BorderBuilder color(ConsumerReturn<Color> color) {
        color.accept(Utils.setIfNull(border::getColor, Color::new, border::setColor));
        return this;
    }

    /**
     * The color of the border. If color is also set, this field takes precedence.
     *
     * @param colorStyle colorStyle or {@code null} for none
     */
    public BorderBuilder colorStyle(ConsumerReturn<ColorStyle> colorStyle) {
        colorStyle.accept(Utils.setIfNull(border::getColorStyle, ColorStyle::new, border::setColorStyle));
        return this;
    }

    /**
     * The style of the border.
     *
     * @param style style or {@code null} for none
     */
    public BorderBuilder style(String style) {
        border.setStyle(style);
        return this;
    }

    /**
     * The width of the border, in pixels. Deprecated; the width is determined by the "style" field.
     *
     * @param width width or {@code null} for none
     */
    public BorderBuilder width(Integer width) {
        border.setWidth(width);
        return this;
    }
}