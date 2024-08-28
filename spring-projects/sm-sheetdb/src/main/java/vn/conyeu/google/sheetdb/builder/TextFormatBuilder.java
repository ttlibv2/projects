package vn.conyeu.google.sheetdb.builder;

import com.google.api.services.sheets.v4.model.Color;
import com.google.api.services.sheets.v4.model.ColorStyle;
import com.google.api.services.sheets.v4.model.TextFormat;
import vn.conyeu.google.core.Utils;

public class TextFormatBuilder implements XmlBuilder<TextFormat> {
    private final TextFormat format;

    public TextFormatBuilder(TextFormat format) {
        this.format = XmlBuilder.ifNull(format, TextFormat::new);
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
     * @param foregroundColor foregroundColor or {@code null} for none
     */
    public TextFormatBuilder foregroundColor(ConsumerReturn<Color> foregroundColor) {
        foregroundColor.accept(Utils.setIfNull(format::getForegroundColor, Color::new, format::setForegroundColor));
        return this;
    }

    /**
     * The foreground color of the text. If foreground_color is also set, this field takes precedence.
     *
     * @param foregroundColorStyle foregroundColorStyle or {@code null} for none
     */
    public TextFormatBuilder foregroundColorStyle(ConsumerReturn<ColorStyle> foregroundColorStyle) {
        foregroundColorStyle.accept(Utils.setIfNull(format::getForegroundColorStyle, ColorStyle::new, format::setForegroundColorStyle));
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