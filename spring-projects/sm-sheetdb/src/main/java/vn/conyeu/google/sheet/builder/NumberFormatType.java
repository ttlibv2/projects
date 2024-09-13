package vn.conyeu.google.sheet.builder;

public enum NumberFormatType {
    /**
     * Text formatting, e.g 1000.12
     **/
    TEXT,
    /**
     * Number formatting, e.g, 1,000.12
     **/
    NUMBER,
    /**
     * Percent formatting, e.g 10.12%
     **/
    PERCENT,
    /**
     * Currency formatting, e.g $1,000.12
     **/
    CURRENCY,
    /**
     * Date formatting, e.g 9/26/2008
     **/
    DATE,
    /**
     * Time formatting, e.g 3:59:00 PM
     **/
    TIME,
    /**
     * Date+Time formatting, e.g 9/26/08 15:59:00
     **/
    DATE_TIME,
    /**
     * Scientific number formatting, e.g 1.01E+03
     **/
    SCIENTIFIC,

}