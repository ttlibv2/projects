package vn.conyeu.google.sheet.builder;

public enum NumberFormatPattern {

    ;
    final NumberFormatType formatType;
    final String pattern;


    NumberFormatPattern(NumberFormatType formatType, String pattern) {
        this.formatType = formatType;
        this.pattern = pattern;
    }
}