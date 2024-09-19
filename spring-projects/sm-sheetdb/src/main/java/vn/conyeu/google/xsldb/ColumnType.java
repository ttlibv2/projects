package vn.conyeu.google.xsldb;

import vn.conyeu.google.sheet.builder.NumberFormatType;

public enum ColumnType {
    STRING(NumberFormatType.TEXT, null),
    JSON(NumberFormatType.TEXT, null),
    DATE(NumberFormatType.DATE, "yyyy-MM-dd"),
    DATE_TIME(NumberFormatType.DATE_TIME, "yyyy-MM-dd hh:mm:ss"),
    TIME(NumberFormatType.TIME, "hh:mm:ss"),
    YEAR(NumberFormatType.NUMBER, "0"),
    BOOLEAN(NumberFormatType.NUMBER, null),
    ENUM(NumberFormatType.TEXT, null),
    FILE(NumberFormatType.TEXT, null),

    ;
    final NumberFormatType formatType;
    final String pattern;


    ColumnType(NumberFormatType formatType, String pattern) {
        this.formatType = formatType;
        this.pattern = pattern;
    }

    /**
     * Returns the formatType
     */
    public NumberFormatType getType() {
        return formatType;
    }

    /**
     * Returns the pattern
     */
    public String getPattern() {
        return pattern;
    }
}