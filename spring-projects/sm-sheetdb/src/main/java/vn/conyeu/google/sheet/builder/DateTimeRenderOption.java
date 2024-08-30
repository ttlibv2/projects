package vn.conyeu.google.sheet.builder;

public enum DateTimeRenderOption {
    /**
     * Instructs date, time, datetime, and duration fields to be output as
     * doubles in "serial number" format, as popularized by Lotus 1-2-3. The
     * whole number portion of the value (left of the decimal) counts the days
     * since December 30th 1899. The fractional portion (right of the decimal)
     * counts the time as a fraction of the day. For example, January 1st 1900
     * at noon would be 2.5, 2 because it's 2 days after December 30th 1899, and
     * .5 because noon is half a day. February 1st 1900 at 3pm would be 33.625.
     * This correctly treats the year 1900 as not a leap year.
     */
    SERIAL_NUMBER,

    /**
     * Instructs date, time, datetime, and duration fields to be output as
     * strings in their given number format (which depends on the spreadsheet
     * locale).
     */
    FORMATTED_STRING
}