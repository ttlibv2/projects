package vn.conyeu.google.sheet.builder;

/**
 * Determines how existing data is changed when new data is input.
 */
public enum InsertDataOption {
    /**
     * The new data overwrites existing data in the areas it is written. (Note:
     * adding data to the end of the sheet will still insert new rows or columns
     * so the data can be written.)
     */
    OVERWRITE,

    /** Rows are inserted for the new data. */
    INSERT_ROWS
}