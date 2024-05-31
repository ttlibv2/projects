package vn.conyeu.javafx.animation.beans;


import javafx.scene.Node;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.DropShadow;
import javafx.scene.paint.Color;

import java.util.Arrays;

/**
 * Enumerator which defines 6 levels of {@code DropShadow} effects from {@code LEVEL0} to {@code LEVEL5}.
 */
public enum DepthLevel {
    LEVEL0(Color.rgb(0, 0, 0, 0), 0, 0, 0, 0),
    LEVEL1(Color.rgb(231, 19, 51, 1), 50, 0.12, 0, 0),
    LEVEL2(Color.rgb(0, 0, 0, 0.20), 15, 0.16, 0, 4),
    LEVEL3(Color.rgb(0, 0, 0, 0.20), 20, 0.19, 0, 6),
    LEVEL4(Color.rgb(0, 0, 0, 0.20), 25, 0.25, 0, 8),
    LEVEL5(Color.rgb(0, 0, 0, 0.20), 30, 0.30, 0, 10);

    private final Color color;
    private final double radius;
    private final double spread;
    private final double offsetX;
    private final double offsetY;
    private static final DepthLevel[] valuesArr = values();

    DepthLevel(Color color, double radius, double spread, double offsetX, double offsetY) {
        this.color = color;
        this.radius = radius;
        this.spread = spread;
        this.offsetX = offsetX;
        this.offsetY = offsetY;
    }

    public Color getColor() {
        return color;
    }

    public double getRadius() {
        return radius;
    }

    public double getSpread() {
        return spread;
    }

    public double getOffsetX() {
        return offsetX;
    }

    public double getOffsetY() {
        return offsetY;
    }

    /**
     * Retrieves the next {@code DepthLevel} associated with {@code this} enumerator.
     *
     * @return The next {@code DepthLevel}
     */
    public DepthLevel next() {
        return valuesArr[(this.ordinal() + 1) % valuesArr.length];
    }

    public DropShadow createShadow() {
        return shadowOf(this);
    }

    public void installEffect(Node control) {
        control.setEffect(new DropShadow(BlurType.GAUSSIAN, getColor(),
                getRadius(), getSpread(), getOffsetX(), getOffsetY()));
    }

    /**
     * Attempts to get the corresponding {@code DepthLevel} of the given {@link DropShadow} effect.
     * If the given effect is not recognized as being generated by this class the {@code null} is returned.
     * <p>
     * Uses {@link #levelEqualsShadow(DepthLevel, DropShadow)} to check for equality.
     */
    public static DepthLevel from(DropShadow shadow) {
        return Arrays.stream(values()).filter(depthLevel ->
                levelEqualsShadow(depthLevel, shadow)).findFirst().orElse(null);
    }

    /**
     * Checks if the given {@link DropShadow} effect is equal to the given {@code DepthLevel}.
     */
    public static boolean levelEqualsShadow(DepthLevel level, DropShadow shadow) {
        return level.color.equals(shadow.getColor()) &&
                level.offsetX == shadow.getOffsetX() &&
                level.offsetY == shadow.getOffsetY() &&
                level.radius == shadow.getRadius() &&
                level.spread == shadow.getSpread();
    }

    /**
     * Returns a new instance of {@code DropShadow} with the specified characteristics.
     *
     * @return The desired custom {@code DropShadow} effect
     * @see DropShadow
     */
    public static DropShadow shadowOf(Color color, double radius, double spread, double offsetX, double offsetY) {
        return new DropShadow(BlurType.GAUSSIAN, color, radius, spread, offsetX, offsetY
        );
    }

    /**
     * Retrieves the {@code DropShadow} associated with the specified {@code DepthLevel}.
     *
     * @param level The desired {@code DepthLevel} between 1 and 5
     * @return The desired {@code DropShadow} effect
     */
    public static DropShadow shadowOf(DepthLevel level) {
        return new DropShadow(
                BlurType.GAUSSIAN,
                level.getColor(),
                level.getRadius(),
                level.getSpread(),
                level.getOffsetX(),
                level.getOffsetY()
        );
    }

    /**
     * Retrieves the {@code DropShadow} associated with the specified {@code DepthLevel} added to delta.
     * <p></p>
     * Example 1: for a depth level equal to 3 and a delta equal to 2, the returned {@code DropShadow} effect is
     * the effected associated to a depth level of 5.
     * <p></p>
     * Example 2: for a depth level equal to 5 and a delta equal to whatever integer, the returned {@code DropShadow} effect is
     * the effected associated to a depth level of 5.
     *
     * @param level The desired {@code DepthLevel} between 1 and 5
     * @param delta The number of levels to shift
     * @return The final {@code DropShadow} effect}
     * <p></p>
     * {@link #nextLevel(DepthLevel)}
     */
    public static DropShadow shadowOf(DepthLevel level, int delta) {
        DepthLevel endLevel = level;
        for (int i = 0; i < delta; i++) {
            endLevel = nextLevel(endLevel);
        }
        return shadowOf(endLevel);
    }

    /**
     * From a starting {@code DepthLevel} retrieves the {@code DropShadow} effect associated to the next {@code DepthLevel}.
     *
     * @param startLevel The starting {@code DepthLevel}
     * @return The {@code DropShadow} effect associated to the next {@code DepthLevel}
     * @see DepthLevel
     */
    private static DepthLevel nextLevel(DepthLevel startLevel) {
        return !(startLevel.equals(DepthLevel.LEVEL5)) ? startLevel.next() : DepthLevel.LEVEL5;
    }
}