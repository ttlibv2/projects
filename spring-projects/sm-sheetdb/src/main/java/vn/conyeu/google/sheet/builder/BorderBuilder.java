package vn.conyeu.google.sheet.builder;

import com.google.api.services.sheets.v4.model.Border;
import com.google.api.services.sheets.v4.model.Color;
import com.google.api.services.sheets.v4.model.ColorStyle;
import vn.conyeu.google.core.Utils;

public class BorderBuilder implements XmlBuilder<Border> {
    private final Border border;

    public BorderBuilder(Border border) {
        this.border = Utils.getIfNull(border, Border::new);
    }

    @Override
    public Border build() {
        return border;
    }

    /**
     * The color of the border.
     *
     * @param rgbColor rgbColor or {@code null} for none
     */
    public BorderBuilder colorStyle(Color rgbColor) {
        border.setColorStyle(new ColorStyle().setRgbColor(rgbColor));
        return this;
    }


    /**
     * The color of the border.
     *
     * @param themeColor theme color or {@code null} for none
     */
    public BorderBuilder colorStyle(ThemeColorType themeColor) {
        border.setColorStyle(new ColorStyle().setThemeColor(Utils.enumName(themeColor)));
        return this;
    }

    /**
     * The style of the border.
     *
     * @param style style or {@code null} for none
     */
    public BorderBuilder style(Style style) {
        border.setStyle(Utils.enumName(style));
        return this;
    }

}