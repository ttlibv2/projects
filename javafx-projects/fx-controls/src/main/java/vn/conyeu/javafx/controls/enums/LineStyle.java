package vn.conyeu.javafx.controls.enums;

public enum LineStyle {

    /**
     * Displays no line. The computed value of the line width is 0 even if a width value is specified. In the case of
     * table cell and border collapsing, the none value has the lowest priority. If any other conflicting border is set,
     * it will be displayed. The none value is similar to hidden.
     */
    NONE,

    /**
     * Displays no line. The computed width of the line is 0 even if a width value is specified. In the case of table
     * cell and border collapsing, the hidden value has the highest priority. If any other conflicting border is set, it
     * won't be displayed. The hidden value is similar to none, but hidden is not a valid value for outline styles.
     */
    HIDDEN,

    /**
     * Displays a series of round dots. The radius of the dots is half the computed value of the line's width. The
     * spacing of the dots is not defined by the specification and is implementation-specific.
     */
    DOTTED,

    /**
     * Displays a series of short square-ended dashes or line segments. The exact size and length of the segments are
     * not defined by the specification and are implementation-specific.
     */
    DASHED,

    /**
     * Displays a single, straight solid line.
     */
    SOLID, DOUBLE,
    GROOVE, RIDGE,
    INSET, OUTSET
}