package vn.conyeu.javafx.controls.enums;

import javafx.scene.Cursor;

public enum ECursor {

    /**
     * The default cursor type (gets set if no cursor is defined).
     */
    DEFAULT(Cursor.DEFAULT),

    /**
     * The cross hair cursor type.
     */
    CROSSHAIR(Cursor.CROSSHAIR),

    /**
     * The text cursor type.
     */
    TEXT(Cursor.TEXT),

    /**
     * The wait cursor type.
     */
    WAIT(Cursor.WAIT),

    /**
     * The south-west-resize cursor type.
     */
    SW_RESIZE(Cursor.SW_RESIZE),

    /**
     * The south-east-resize cursor type.
     */
    SE_RESIZE(Cursor.SE_RESIZE),

    /**
     * The north-west-resize cursor type.
     */
    NW_RESIZE(Cursor.NW_RESIZE),

    /**
     * The north-east-resize cursor type.
     */
    NE_RESIZE(Cursor.NE_RESIZE),

    /**
     * The north-resize cursor type.
     */
    N_RESIZE(Cursor.N_RESIZE),

    /**
     * The south-resize cursor type.
     */
    S_RESIZE(Cursor.S_RESIZE),

    /**
     * The west-resize cursor type.
     */
    W_RESIZE(Cursor.W_RESIZE),

    /**
     * The east-resize cursor type.
     */
    E_RESIZE(Cursor.E_RESIZE),

    /**
     * A cursor with a hand which is open
     */
    OPEN_HAND(Cursor.OPEN_HAND),

    /**
     * A cursor with a hand that is closed, often used when
     * "grabbing", for example, when panning.
     */
    CLOSED_HAND(Cursor.CLOSED_HAND),

    /**
     * The hand cursor type, resembling a pointing hand, often
     * used to indicate that something can be clicked, such as
     * a hyperlink.
     */
    HAND(Cursor.HAND),

    /**
     * The move cursor type.
     */
    MOVE(Cursor.MOVE),

    /**
     * The disappear cursor type. This is often used when dragging
     * something, such that when the user releases the mouse, the
     * item will disappear. On Mac, this is used when dragging items
     * off a toolbar or in other such situations.
     */
    DISAPPEAR(Cursor.DISAPPEAR),

    /**
     * The horizontal cursor type.
     */
    H_RESIZE(Cursor.H_RESIZE),

    /**
     * The vertical cursor type.
     */
    V_RESIZE(Cursor.V_RESIZE),

    /**
     * The none cursor type. On platforms that don't support
     * custom cursors, this will be the same as {@code DEFAULT}.
     */
    NONE(Cursor.NONE);

    final Cursor cursor;

    ECursor(Cursor cursor) {
        this.cursor = cursor;
    }

    /**
     * Returns the cursor
     */
    public Cursor cursor() {
        return cursor;
    }
}