package vn.conyeu.google.sheet.builder;

/** An enumeration of the possible recalculation interval options. */
public enum RecalculationInterval {
    /** Volatile functions are updated on every change. **/
    ON_CHANGE,

    /** Volatile functions are updated on every change and every minute. **/
    MINUTE,

    /** Volatile functions are updated on every change and hourly. **/
    HOUR
}
