package vn.conyeu.google.sheet.builder;

public enum ValueRenderOption {

    /**
     * Values will not be calculated. The reply will include the formulas. For
     * example, if A1 is 1.23 and A2 is =A1 and formatted as currency, then A2
     * would return "=A1".
     * <p>
     * Sheets treats date and time values as decimal values. This lets you
     * perform arithmetic on them in formulas. For more information on
     * interpreting date and time values, see About date & time values.
     */
    FORMULA,

    /**
     * Values will be calculated & formatted in the response according to the
     * cell's formatting. Formatting is based on the spreadsheet's locale, not
     * the requesting user's locale. For example, if A1 is 1.23 and A2 is =A1
     * and formatted as currency, then A2 would return "$1.23".
     */
    FORMATTED_VALUE,

    /**
     * Values will be calculated, but not formatted in the reply. For example,
     * if A1 is 1.23 and A2 is =A1 and formatted as currency, then A2 would
     * return the number 1.23.
     */
    UNFORMATTED_VALUE
}