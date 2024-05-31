package vn.conyeu.javafx.controls.flex;

/**
 * @css: `flex-wrap`
 */
public enum FlexWrap {
    WRAP, REVERSE, NOWRAP;

    public boolean isWrap() {
        return this == WRAP || this == REVERSE;
    }

}