package vn.conyeu.javafx.controls.flex;

/**
 * The CSS justify-content property defines how the browser distributes space between and around content items along the main-axis of a flex
 * container, and the inline axis of a grid container.
 * <p>@css: justify-content</p>
 */
public enum FlexJustify {
    /**
     * flex-start (default): items are packed toward the start of the flex-direction.
     */
    FLEX_START,

    /**
     * flex-end: items are packed toward the end of the flex-direction.
     */
    FLEX_END,

    /**
     * center: items are centered along the line
     */
    CENTER,

    /**
     * space-between: items are evenly distributed in the line; first item is on the start line, last item on the end line
     */
    SPACE_BETWEEN,
    /**
     * space-around: items are evenly distributed in the line with equal space around them. Note that visually the spaces aren’t equal, since all
     * the items have equal space on both sides. The first item will have one unit of space against the container edge, but two units of space
     * between the next item because that next item has its own spacing that applies.
     */
    SPACE_AROUND,
    /**
     * space-evenly: items are distributed so that the spacing between any two items (and the space to the edges) is equal.
     */
    SPACE_EVENLY
}