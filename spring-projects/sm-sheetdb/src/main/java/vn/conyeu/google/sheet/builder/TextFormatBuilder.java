package vn.conyeu.google.sheet.builder;

import com.google.api.services.sheets.v4.model.Color;
import com.google.api.services.sheets.v4.model.ColorStyle;
import com.google.api.services.sheets.v4.model.TextFormat;
import vn.conyeu.google.core.Utils;

public class TextFormatBuilder implements XmlBuilder<TextFormat> {
    private final TextFormat format;

    public TextFormatBuilder(TextFormat format) {
        this.format =Utils.getIfNull(format, TextFormat::new);
    }

    @Override
    public TextFormat build() {
        return format;
    }

    /**
     * True if the text is bold.
     *
     * @param bold bold or {@code null} for none
     */
    public TextFormatBuilder bold(Boolean bold) {
        format.setBold(bold);
        return this;
    }

    /**
     * The font family.
     *
     * @param fontFamily fontFamily or {@code null} for none
     */
    public TextFormatBuilder fontFamily(String fontFamily) {
        format.setFontFamily(fontFamily);
        return this;
    }

    /**
     * The size of the font.
     *
     * @param fontSize fontSize or {@code null} for none
     */
    public TextFormatBuilder fontSize(Integer fontSize) {
        format.setFontSize(fontSize);
        return this;
    }

    /**
     * The foreground color of the text.
     *
     * @param rgbColor RGB color
     */
    public TextFormatBuilder foregroundColorStyle(Color rgbColor) {
        format.setForegroundColorStyle(new ColorStyle().setRgbColor(rgbColor));
        return this;
    }

    /**
     * The foreground color of the text.
     *
     * @param themeColor Theme color
     */
    public TextFormatBuilder foregroundColorStyle(ThemeColorType themeColor) {
        format.setForegroundColorStyle(new ColorStyle().setThemeColor(Utils.enumName(themeColor)));
        return this;
    }



    /**
     * True if the text is italicized.
     *
     * @param italic italic or {@code null} for none
     */
    public TextFormatBuilder italic(Boolean italic) {
        format.setItalic(italic);
        return this;
    }

    /**
     * True if the text has a strikethrough.
     *
     * @param strikethrough strikethrough or {@code null} for none
     */
    public TextFormatBuilder strikethrough(Boolean strikethrough) {
        format.setStrikethrough(strikethrough);
        return this;
    }

    /**
     * True if the text is underlined.
     *
     * @param underline underline or {@code null} for none
     */
    public TextFormatBuilder underline(Boolean underline) {
        format.setUnderline(underline);
        return this;
    }
}