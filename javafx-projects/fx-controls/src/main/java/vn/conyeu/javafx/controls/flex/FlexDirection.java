package vn.conyeu.javafx.controls.flex;

/**
 * @css: `flex-direction`
 */
public enum FlexDirection {
    ROW, ROW_REVERSE,
    COLUMN, COLUMN_REVERSE;

    public boolean isHorizontal() {
        return this == ROW || this == ROW_REVERSE;
    }

    public boolean isVertical() {
        return this == COLUMN || this == COLUMN_REVERSE;
    }

    public boolean isReverse() {
        return this == ROW_REVERSE || this == COLUMN_REVERSE;
    }

}