package vn.conyeu.google.sheet.builder;

/**
 * The style of a border.
 */
public enum Style {

    /*The border is dotted.*/
    DOTTED,

    /*The border is dashed.*/
    DASHED,

    /*The border is a thin solid line.*/
    SOLID,

    /*The border is a medium solid line.*/
    SOLID_MEDIUM,

    /*	The border is a thick solid line.*/
    SOLID_THICK,

    /*No border. Used only when updating a border in order to erase it.*/
    NONE,

    /*The border is two solid lines.*/
    DOUBLE
}
