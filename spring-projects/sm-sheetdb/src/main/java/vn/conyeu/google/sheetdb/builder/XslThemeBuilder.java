package vn.conyeu.google.sheetdb.builder;

import com.google.api.services.sheets.v4.model.Color;
import com.google.api.services.sheets.v4.model.ColorStyle;
import com.google.api.services.sheets.v4.model.SpreadsheetTheme;
import com.google.api.services.sheets.v4.model.ThemeColorPair;
import vn.conyeu.google.core.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class XslThemeBuilder implements XmlBuilder<SpreadsheetTheme> {
    private final SpreadsheetTheme theme;
    private final Map<ThemeColorType, ThemeColorPair> colorPairs = new HashMap<>();

    public XslThemeBuilder(SpreadsheetTheme theme) {
        this.theme = theme == null ? new SpreadsheetTheme() : theme;
        this.determineColorPairs(theme);
    }

    public XslThemeBuilder defaultData() {
        fontFamily("Roboto");
        textColor("#000000");
        linkColor("#0097a7");
        backgroundColor("#fff");
        accent1Color("#5891ad");
        accent2Color("#004561");
        accent3Color("#ff6f31");
        accent4Color("#1c7685");
        accent5Color("#0f45a8");
        accent6Color("#4cdc8b");
        return this;
    }

    /**
     * Name of the primary font family.
     *
     * @param fontFamily primaryFontFamily or {@code null} for none
     */
    public XslThemeBuilder fontFamily(String fontFamily) {
        theme.setPrimaryFontFamily(fontFamily);
        return this;
    }

    /*Represents the primary text color*/
    public XslThemeBuilder textColor(Consumer<Color> color) {
        initColorStyle(ThemeColorType.TEXT, color);
        return this;
    }

    /*Represents the primary text color*/
    public XslThemeBuilder textColor(String color) {
        textColor(c -> applyRgbColor(c, color));
        return this;
    }


    /*Represents the color to use for hyperlinks*/
    public XslThemeBuilder linkColor(Consumer<Color> color) {
        initColorStyle(ThemeColorType.LINK, color);
        return this;
    }

    /*Represents the color to use for hyperlinks*/
    public XslThemeBuilder linkColor(String color) {
        linkColor(c -> applyRgbColor(c, color));
        return this;
    }

    /*Represents the primary background color*/
    public XslThemeBuilder backgroundColor(Consumer<Color> color) {
        initColorStyle(ThemeColorType.BACKGROUND, color);
        return this;
    }

    /*Represents the primary background color*/
    public XslThemeBuilder backgroundColor(String color) {
        backgroundColor(c -> applyRgbColor(c, color));
        return this;
    }

    /*Represents the first accent color*/
    public XslThemeBuilder accent1Color(Consumer<Color> color) {
        initColorStyle(ThemeColorType.ACCENT1, color);
        return this;
    }

    /*Represents the first accent color*/
    public XslThemeBuilder accent1Color(String color) {
        accent1Color(c -> applyRgbColor(c, color));
        return this;
    }

    /*Represents the second accent color*/
    public XslThemeBuilder accent2Color(Consumer<Color> color) {
        initColorStyle(ThemeColorType.ACCENT2, color);
        return this;
    }

    /*Represents the second accent color*/
    public XslThemeBuilder accent2Color(String color) {
        accent2Color(c -> applyRgbColor(c, color));
        return this;
    }

    /*Represents the third accent color*/
    public XslThemeBuilder accent3Color(Consumer<Color> color) {
        initColorStyle(ThemeColorType.ACCENT3, color);
        return this;
    }

    /*Represents the third accent color*/
    public XslThemeBuilder accent3Color(String color) {
        accent3Color(c -> applyRgbColor(c, color));
        return this;
    }

    /*Represents the fourth accent color*/
    public XslThemeBuilder accent4Color(Consumer<Color> color) {
        initColorStyle(ThemeColorType.ACCENT4, color);
        return this;
    }

    /*Represents the fourth accent color*/
    public XslThemeBuilder accent4Color(String color) {
        accent4Color(c -> applyRgbColor(c, color));
        return this;
    }

    /*Represents the fifth accent color*/
    public XslThemeBuilder accent5Color(Consumer<Color> color) {
        initColorStyle(ThemeColorType.ACCENT5, color);
        return this;
    }

    /*Represents the fifth accent color*/
    public XslThemeBuilder accent5Color(String color) {
        accent5Color(c -> applyRgbColor(c, color));
        return this;
    }

    /*Represents the sixth accent color*/
    public XslThemeBuilder accent6Color(Consumer<Color> color) {
        initColorStyle(ThemeColorType.ACCENT6, color);
        return this;
    }

    /*Represents the sixth accent color*/
    public XslThemeBuilder accent6Color(String color) {
        accent6Color(c -> applyRgbColor(c, color));
        return this;
    }

    @Override
    public SpreadsheetTheme build() {
        if (!colorPairs.isEmpty()) {
            List<ThemeColorPair> colors = new ArrayList<>(colorPairs.values());
            theme.setThemeColors(colors);
        }
        return theme;
    }

    private void initColorStyle(ThemeColorType colorType, Consumer<Color> consumer) {
        ThemeColorPair pair = colorPairs.get(colorType);
        if (pair == null) {
            pair = new ThemeColorPair();
            pair.setColorType(colorType.name());
            colorPairs.put(colorType, pair);
        }

        ColorStyle style = Utils.setIfNull(pair::getColor, ColorStyle::new, pair::setColor);
        consumer.accept(Utils.setIfNull(style::getRgbColor, Color::new, style::setRgbColor));
    }

    private void determineColorPairs(SpreadsheetTheme theme) {
        if (theme != null) {
            List<ThemeColorPair> list = theme.getThemeColors();
            for (ThemeColorPair pair : list) {
                ThemeColorType colorType = ThemeColorType.valueOf(pair.getColorType());
                colorPairs.put(colorType, pair);
            }
        }
    }

    private void applyRgbColor(Color c, String colorString) {
       Utils.updateRgbColor(c, colorString);
    }
}