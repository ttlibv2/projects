package vn.conyeu.google.sheet.builder;

import com.google.api.services.sheets.v4.model.NumberFormat;
import vn.conyeu.google.core.Utils;

public class NumberFormatBuilder implements XmlBuilder<NumberFormat> {
    private final NumberFormat format;

    public NumberFormatBuilder(NumberFormat format) {
        this.format = Utils.getIfNull(format, NumberFormat::new);
    }

    @Override
    public NumberFormat build() {
        return format;
    }

    /**
     * Pattern string used for formatting.  If not set, a default pattern based on the user's locale
     * will be used if necessary for the given type. See the [Date and Number Formats
     * guide](/sheets/api/guides/formats) for more information about the supported patterns.
     *
     * @param pattern pattern or {@code null} for none
     */
    public NumberFormatBuilder pattern(String pattern) {
        format.setPattern(pattern);
        return this;
    }

    /**
     * The type of the number format. When writing, this field must be set.
     *
     * @param type type or {@code null} for none
     */
    public NumberFormatBuilder type(NumberFormatType type) {
        format.setType(Utils.enumName(type));
        return this;
    }
}