package vn.conyeu.javafx.utils;

import com.sun.javafx.scene.NodeHelper;
import com.sun.javafx.scene.control.behavior.FocusTraversalInputMap;
import com.sun.javafx.scene.control.skin.Utils;
import com.sun.javafx.scene.text.TextLayout;
import com.sun.javafx.scene.traversal.Direction;
import com.sun.javafx.scene.traversal.TraversalMethod;
import com.sun.javafx.tk.Toolkit;
import javafx.application.ConditionalFeature;
import javafx.application.Platform;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.layout.Region;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextBoundsType;
import javafx.stage.PopupWindow;
import javafx.stage.Screen;
import javafx.stage.Window;
import vn.conyeu.commons.utils.Objects;
import vn.conyeu.javafx.beans.*;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

public final class FxUtils {
    private static TextLayout layout;

    /**
     * Checks if the given number is even or odd, just a convenience method for aesthetic.
     */
    public static boolean isEven(int number) {
        return (number % 2 == 0);
    }
    private static TextLayout textLayout() {
        if (layout == null) {
            layout = Toolkit.getToolkit().getTextLayoutFactory().createLayout();
        }
        return layout;
    }


    /**
     * Helper to remove leading and trailing quotes from a string.
     * Works with single or double quotes.
     */
    public static String stripQuotes(String str) {
        if (str == null) return str;
        if (str.length() == 0) return str;

        int beginIndex = 0;
        final char openQuote = str.charAt(beginIndex);
        if (openQuote == '\"' || openQuote == '\'') beginIndex += 1;

        int endIndex = str.length();
        final char closeQuote = str.charAt(endIndex - 1);
        if (closeQuote == '\"' || closeQuote == '\'') endIndex -= 1;

        if ((endIndex - beginIndex) < 0) return str;

        // note that String.substring returns "this" if beginIndex == 0 && endIndex == count
        // or a new string that shares the character buffer with the original string.
        return str.substring(beginIndex, endIndex);
    }


    /**
     * Because mobile doesn't have string.split(s) function, this function
     * was written.
     */
    public static String[] split(String str, String separator) {
        if (str == null || str.length() == 0) return new String[]{};
        if (separator == null || separator.length() == 0) return new String[]{};
        if (separator.length() > str.length()) return new String[]{};

        List<String> result = new java.util.ArrayList<>();

        int index = str.indexOf(separator);
        while (index >= 0) {
            String newStr = str.substring(0, index);
            if (newStr.length() > 0) {
                result.add(newStr);
            }
            str = str.substring(index + separator.length());
            index = str.indexOf(separator);
        }

        if (str.length() > 0) {
            result.add(str);
        }

        return result.toArray(new String[]{});
    }

    /**
     * Because mobile doesn't have string.contains(s) function, this function
     * was written.
     */
    public static boolean contains(String src, String s) {
        if (src == null || src.length() == 0) return false;
        if (s == null || s.length() == 0) return false;
        if (s.length() > src.length()) return false;

        return src.contains(s);
    }

    public static Insets createInserts(double topBottom, double leftRight) {
        return new Insets(topBottom, leftRight, topBottom, leftRight);
    }

    public static double computeTextWidth(Text node, String text) {
        return computeTextWidth(node.getFont(), text, node.getWrappingWidth());
    }

    public static double computeTextWidth(Font font, String text, double wrappingWidth) {
        return Utils.computeTextWidth(font, text, wrappingWidth);
    }

    public static double computeTextWidth(Font font, String text) {
        return computeTextWidth(font, text, 0);
    }

    public static void traverseNext(Node node) {
        FocusTraversalInputMap.traverse(node, Direction.NEXT, TraversalMethod.KEY);
    }

    public static void traversePrev(Node node) {
        FocusTraversalInputMap.traverse(node,
                Direction.PREVIOUS, TraversalMethod.KEY);
    }

    public static boolean isTreeShowing(Node node) {
        return NodeHelper.isTreeShowing(node);
    }

    public static double getLineHeight(Font font, TextBoundsType boundsType) {
        return Utils.getLineHeight(font, boundsType);
    }

    /**
     * Limits the given value to the given min-max range by returning the nearest bound
     * if it exceeds or val if it's in range.
     *
     * @param val the value
     */
    public static double clamp(double val, double min, double max) {
        return Math.max(min, Math.min(max, val));
    }

    /**
     * Limits the given value to the given min-max range by returning the nearest bound
     * if it exceeds or val if it's in range.
     *
     * @param val the value
     */
    public static float clamp(float val, float min, float max) {
        return Math.max(min, Math.min(max, val));
    }


    /**
     * Limits the given value to the given min-max range by returning the nearest bound
     * if it exceeds or val if it's in range.
     */
    public static int clamp(int val, int min, int max) {
        return Math.max(min, Math.min(max, val));
    }

    /**
     * Limits the given value to the given min-max range by returning the nearest bound
     * if it exceeds or val if it's in range.
     */
    public static int clamp(double val, int min, int max) {
        return Math.max(min, Math.min(max, (int) val));
    }

    /**
     * Limits the given value to the given min-max range by returning the nearest bound
     * if it exceeds or val if it's in range.
     */
    public static long clamp(long val, long min, long max) {
        return Math.max(min, Math.min(max, val));
    }


    /**
     * Given a certain value, the range of possible values, and a different range, converts the given value
     * from its range to the given second range.
     * <p></p>
     * For example let's say I have a value of 0 that can go from -100 to 100 and I want to convert the
     * value to a range of 0 to 100, the converted value will be 50 (0 is at the middle in the -100-100 range, and
     * 50 is at the middle in the 0-100 range).
     */
    public static double mapOneRangeToAnother(double value, DoubleRange fromRange, DoubleRange toRange, int decimalPrecision) {
        double deltaA = fromRange.getMax() - fromRange.getMin();
        double deltaB = toRange.getMax() - toRange.getMin();
        double scale = deltaB / deltaA;
        double negA = -1 * fromRange.getMin();
        double offset = (negA * scale) + toRange.getMin();
        double finalNumber = (value * scale) + offset;
        int calcScale = (int) Math.pow(10, decimalPrecision);
        return (double) Math.round(finalNumber * calcScale) / calcScale;
    }

    /**
     * Given a certain value, the range of possible values, and a different range, converts the given value
     * from its range to the given second range.
     * <p></p>
     * For example let's say I have a value of 0 that can go from -100 to 100 and I want to convert the
     * value to a range of 0 to 100, the converted value will be 50 (0 is at the middle in the -100-100 range, and
     * 50 is at the middle in the 0-100 range).
     */
    public static float mapOneRangeToAnother(float value, FloatRange fromRange, FloatRange toRange, int decimalPrecision) {
        double deltaA = fromRange.getMax() - fromRange.getMin();
        double deltaB = toRange.getMax() - toRange.getMin();
        double scale = deltaB / deltaA;
        double negA = -1 * fromRange.getMin();
        double offset = (negA * scale) + toRange.getMin();
        double finalNumber = (value * scale) + offset;
        int calcScale = (int) Math.pow(10, decimalPrecision);
        return (float) Math.round(finalNumber * calcScale) / calcScale;
    }

    /**
     * Given a certain value, the range of possible values, and a different range, converts the given value
     * from its range to the given second range.
     * <p></p>
     * For example let's say I have a value of 0 that can go from -100 to 100 and I want to convert the
     * value to a range of 0 to 100, the converted value will be 50 (0 is at the middle in the -100-100 range, and
     * 50 is at the middle in the 0-100 range).
     */
    public static int mapOneRangeToAnother(int value, IntegerRange fromRange, IntegerRange toRange, int decimalPrecision) {
        double deltaA = fromRange.getMax() - fromRange.getMin();
        double deltaB = toRange.getMax() - toRange.getMin();
        double scale = deltaB / deltaA;
        double negA = -1 * fromRange.getMin();
        double offset = (negA * scale) + toRange.getMin();
        double finalNumber = (value * scale) + offset;
        int calcScale = (int) Math.pow(10, decimalPrecision);
        return (int) Math.round(finalNumber * calcScale) / calcScale;
    }

    /**
     * Given a certain value, the range of possible values, and a different range, converts the given value
     * from its range to the given second range.
     * <p></p>
     * For example let's say I have a value of 0 that can go from -100 to 100 and I want to convert the
     * value to a range of 0 to 100, the converted value will be 50 (0 is at the middle in the -100-100 range, and
     * 50 is at the middle in the 0-100 range).
     */
    public static long mapOneRangeToAnother(long value, LongRange fromRange, LongRange toRange, int decimalPrecision) {
        double deltaA = fromRange.getMax() - fromRange.getMin();
        double deltaB = toRange.getMax() - toRange.getMin();
        double scale = deltaB / deltaA;
        double negA = -1 * fromRange.getMin();
        double offset = (negA * scale) + toRange.getMin();
        double finalNumber = (value * scale) + offset;
        int calcScale = (int) Math.pow(10, decimalPrecision);
        return Math.round(finalNumber * calcScale) / calcScale;
    }

    /**
     * Given a certain value, the range of possible values, and a different range, converts the given value
     * from its range to the given second range.
     * <p></p>
     * For example let's say I have a value of 0 that can go from -100 to 100 and I want to convert the
     * value to a range of 0 to 100, the converted value will be 50 (0 is at the middle in the -100-100 range, and
     * 50 is at the middle in the 0-100 range).
     */
    public static double mapOneRangeToAnother(double value, DoubleRange fromRange, DoubleRange toRange) {
        double deltaA = fromRange.getMax() - fromRange.getMin();
        double deltaB = toRange.getMax() - toRange.getMin();
        double scale = deltaB / deltaA;
        double negA = -1 * fromRange.getMin();
        double offset = (negA * scale) + toRange.getMin();
        return (value * scale) + offset;
    }

    /**
     * Given a certain value, the range of possible values, and a different range, converts the given value
     * from its range to the given second range.
     * <p></p>
     * For example let's say I have a value of 0 that can go from -100 to 100 and I want to convert the
     * value to a range of 0 to 100, the converted value will be 50 (0 is at the middle in the -100-100 range, and
     * 50 is at the middle in the 0-100 range).
     */
    public static float mapOneRangeToAnother(float value, FloatRange fromRange, FloatRange toRange) {
        double deltaA = fromRange.getMax() - fromRange.getMin();
        double deltaB = toRange.getMax() - toRange.getMin();
        double scale = deltaB / deltaA;
        double negA = -1 * fromRange.getMin();
        double offset = (negA * scale) + toRange.getMin();
        return (float) ((value * scale) + offset);
    }

    /**
     * Given a certain value, the range of possible values, and a different range, converts the given value
     * from its range to the given second range.
     * <p></p>
     * For example let's say I have a value of 0 that can go from -100 to 100 and I want to convert the
     * value to a range of 0 to 100, the converted value will be 50 (0 is at the middle in the -100-100 range, and
     * 50 is at the middle in the 0-100 range).
     */
    public static int mapOneRangeToAnother(int value, IntegerRange fromRange, IntegerRange toRange) {
        double deltaA = fromRange.getMax() - fromRange.getMin();
        double deltaB = toRange.getMax() - toRange.getMin();
        double scale = deltaB / deltaA;
        double negA = -1 * fromRange.getMin();
        double offset = (negA * scale) + toRange.getMin();
        return (int) ((value * scale) + offset);
    }

    /**
     * Given a certain value, the range of possible values, and a different range, converts the given value
     * from its range to the given second range.
     * <p></p>
     * For example let's say I have a value of 0 that can go from -100 to 100 and I want to convert the
     * value to a range of 0 to 100, the converted value will be 50 (0 is at the middle in the -100-100 range, and
     * 50 is at the middle in the 0-100 range).
     */
    public static long mapOneRangeToAnother(long value, LongRange fromRange, LongRange toRange) {
        double deltaA = fromRange.getMax() - fromRange.getMin();
        double deltaB = toRange.getMax() - toRange.getMin();
        double scale = deltaB / deltaA;
        double negA = -1 * fromRange.getMin();
        double offset = (negA * scale) + toRange.getMin();
        return (long) ((value * scale) + offset);
    }

    /**
     * Given a certain value, finds the closest value in the given numbers list.
     */
    public static double closestValueTo(double val, List<Double> list) {
        if (list.isEmpty()) {
            return 0.0;
        }

        double res = list.get(0);
        for (int i = 1; i < list.size(); i++) {
            if (Math.abs(val - res) >
                    Math.abs(val - list.get(i))) {
                res = list.get(i);
            }
        }

        return res;
    }

    /**
     * Given a certain value, finds the closest value in the given numbers list.
     */
    public static float closestValueTo(float val, List<Float> list) {
        if (list.isEmpty()) {
            return 0;
        }

        float res = list.get(0);
        for (int i = 1; i < list.size(); i++) {
            if (Math.abs(val - res) >
                    Math.abs(val - list.get(i))) {
                res = list.get(i);
            }
        }

        return res;
    }

    /**
     * Given a certain value, finds the closest value in the given numbers list.
     */
    public static int closestValueTo(int val, List<Integer> list) {
        if (list.isEmpty()) {
            return 0;
        }

        int res = list.get(0);
        for (int i = 1; i < list.size(); i++) {
            if (Math.abs(val - res) >
                    Math.abs(val - list.get(i))) {
                res = list.get(i);
            }
        }

        return res;
    }

    /**
     * Given a certain value, finds the closest value in the given numbers list.
     */
    public static long closestValueTo(long val, List<Long> list) {
        if (list.isEmpty()) {
            return 0;
        }

        long res = list.get(0);
        for (int i = 1; i < list.size(); i++) {
            if (Math.abs(val - res) >
                    Math.abs(val - list.get(i))) {
                res = list.get(i);
            }
        }

        return res;
    }


    /**
     * Will return a {@link Window} from an object if any can be found. {@code null}
     * value can be given, the program will then try to find the focused window
     * among those available.
     *
     * @param owner the object whose window is to be found.
     * @return the window of the given object.
     */
    public static Window getWindow(Object owner) throws IllegalArgumentException {
        if (owner == null) {
            Window window = null;
            // let's just get the focused stage and show the dialog in there
            List<Window> windows = Window.getWindows();
            for (Window w : windows) {
                window = w;
                if (window.isFocused() && !(window instanceof PopupWindow)) {
                    return window;
                }
            }
            return window;
        } //
        else if (owner instanceof Window w) return w;
        else if (owner instanceof Node n) return n.getScene().getWindow();
        else throw new IllegalArgumentException("Unknown owner: " + owner.getClass());

    }

    public static Optional<Screen> getScreenBounds(Window window) {
        if (window == null) return Optional.empty();

        return Screen.getScreensForRectangle(
                        window.getX(), window.getY(),
                        window.getWidth(), window.getHeight())
                .stream().filter(Objects::nonNull)
                .findFirst();
    }


    public static Screen getScreen(Window window) {
        return getScreenBounds(window).orElse(Screen.getPrimary());
    }


    public static PositionBean computePosition(Region parent, Node child, double areaX, double areaY, double areaWidth, double areaHeight,
                                               double areaBaselineOffset, Insets margin, HPos hAlignment, VPos vAlignment) {
        return computePosition(parent, child, areaX, areaY, areaWidth, areaHeight, areaBaselineOffset, margin, hAlignment, vAlignment, true, true);
    }

    public static PositionBean computePosition(Region parent, Node child, double areaX, double areaY, double areaWidth, double areaHeight,
                                               double areaBaselineOffset, Insets margin, HPos hAlignment, VPos vAlignment, boolean snapToPixel, boolean computeSizes) {

        Insets snappedMargin = margin == null ? Insets.EMPTY : margin;
        if (snapToPixel) {
            snappedMargin = new Insets(
                    parent.snapSpaceY(snappedMargin.getTop()),
                    parent.snapSpaceX(snappedMargin.getRight()),
                    parent.snapSpaceY(snappedMargin.getBottom()),
                    parent.snapSpaceX(snappedMargin.getLeft())
            );
        }

        double xPosition = computeXPosition(parent, child, areaX, areaWidth, snappedMargin, false, hAlignment, snapToPixel, computeSizes);
        double yPosition = computeYPosition(parent, child, areaY, areaHeight, areaBaselineOffset, snappedMargin, false, vAlignment, snapToPixel, computeSizes);
        return PositionBean.of(xPosition, yPosition);
    }

    public static double computeXPosition(Region parent, Node child, double areaX, double areaWidth, Insets margin, boolean snapMargin, HPos hAlignment, boolean snapToPixel, boolean computeSizes) {
        Insets snappedMargin = margin == null ? Insets.EMPTY : margin;
        if (snapMargin) {
            snappedMargin = new Insets(
                    parent.snapSpaceY(snappedMargin.getTop()),
                    parent.snapSpaceX(snappedMargin.getRight()),
                    parent.snapSpaceY(snappedMargin.getBottom()),
                    parent.snapSpaceX(snappedMargin.getLeft())
            );
        }

        final double leftMargin = snappedMargin.getLeft();
        final double rightMargin = snappedMargin.getRight();
        final double xOffset = leftMargin + computeXOffset(areaWidth - leftMargin - rightMargin, computeSizes ? boundWidth(child) : child.getLayoutBounds().getWidth(), hAlignment);
        final double xPosition = areaX + xOffset;
        return snapToPixel ? parent.snapPositionX(xPosition) : xPosition;
    }

    public static double computeYPosition(Region parent, Node child, double areaY, double areaHeight, double areaBaselineOffset, Insets margin, boolean snapMargin, VPos vAlignment, boolean snapToPixel, boolean computeSizes) {
        Insets snappedMargin = margin == null ? Insets.EMPTY : margin;
        if (snapMargin) {
            snappedMargin = new Insets(
                    parent.snapSpaceY(snappedMargin.getTop()),
                    parent.snapSpaceX(snappedMargin.getRight()),
                    parent.snapSpaceY(snappedMargin.getBottom()),
                    parent.snapSpaceX(snappedMargin.getLeft())
            );
        }

        final double topMargin = snappedMargin.getTop();
        final double bottomMargin = snappedMargin.getBottom();
        final double yOffset;
        if (vAlignment == VPos.BASELINE) {
            double bo = child.getBaselineOffset();
            if (bo == Node.BASELINE_OFFSET_SAME_AS_HEIGHT) {
                yOffset = areaBaselineOffset - (computeSizes ? boundHeight(child) : child.getLayoutBounds().getHeight());
            } else {
                yOffset = areaBaselineOffset - bo;
            }
        } else {
            yOffset = topMargin + computeYOffset(areaHeight - topMargin - bottomMargin, computeSizes ? boundHeight(child) : child.getLayoutBounds().getHeight(), vAlignment);
        }
        final double yPosition = areaY + yOffset;
        return snapToPixel ? parent.snapPositionY(yPosition) : yPosition;
    }

    private static double computeXOffset(double areaWidth, double contentWidth, HPos hAlignment) {
        return switch (hAlignment) {
            case LEFT -> 0;
            case CENTER -> (areaWidth - contentWidth) / 2;
            case RIGHT -> areaWidth - contentWidth;
            default -> throw new AssertionError("Unhandled hPos");
        };
    }

    private static double computeYOffset(double areaHeight, double contentHeight, VPos vAlignment) {
        return switch (vAlignment) {
            case BASELINE, TOP -> 0;
            case CENTER -> (areaHeight - contentHeight) / 2;
            case BOTTOM -> areaHeight - contentHeight;
            default -> throw new AssertionError("Unhandled vPos");
        };
    }

    public static void resize(Region parent, Node node, double w, double h) {
        node.resize(parent.snapSizeX(w), parent.snapSizeY(h));
    }

    public static void relocate(Region parent, Node node, double x, double y) {
        node.relocate(parent.snapPositionX(x), parent.snapPositionY(y));
    }

    public static void resizeRelocate(Region parent, Node node, double x, double y, double w, double h) {
        node.resizeRelocate(parent.snapPositionX(x), parent.snapPositionY(y), parent.snapSizeX(w), parent.snapSizeY(h));
    }

    public static double boundedSize(double min, double pref, double max) {
        double a = Math.max(pref, min);
        double b = Math.max(min, max);
        return Math.min(a, b);
    }

    public static double boundWidth(Node node) {
        return boundedSize(node.minWidth(-1), node.prefWidth(-1), node.maxWidth(-1));
    }

    public static double boundHeight(Node node) {
        return boundedSize(node.minHeight(-1), node.prefHeight(-1), node.maxHeight(-1));
    }

    public static double snappedBoundWidth(Node node) {
        Parent p = node.getParent();
        if (p instanceof Region) {
            return ((Region) p).snapSizeX(boundWidth(node));
        }
        return boundWidth(node);
    }

    public static double snappedBoundHeight(Node node) {
        Parent p = node.getParent();
        if (p instanceof Region) {
            return ((Region) p).snapSizeY(boundHeight(node));
        }
        return boundHeight(node);
    }

    /**
     * Returns the given value as a string the specified number of decimal places.
     */
    public static String formatToString(double value, int decimalPrecision) {
        String str = "%." + decimalPrecision + "f";
        return String.format(str, value);
    }


    /**
     * Given a certain value, the range of possible values, and a different range, converts the given value
     * from its range to the given second range.
     * <p></p>
     * For example let's say I have a value of 0 that can go from -100 to 100 and I want to convert the
     * value to a range of 0 to 100, the converted value will be 50 (0 is at the middle in the -100-100 range, and
     * 50 is at the middle in the 0-100 range).
     */
    public static double mapOneRangeToAnother(double value, NumberRange<Double> fromRange, NumberRange<Double> toRange, int decimalPrecision) {
        double deltaA = fromRange.getMax() - fromRange.getMin();
        double deltaB = toRange.getMax() - toRange.getMin();
        double scale = deltaB / deltaA;
        double negA = -1 * fromRange.getMin();
        double offset = (negA * scale) + toRange.getMin();
        double finalNumber = (value * scale) + offset;
        int calcScale = (int) Math.pow(10, decimalPrecision);
        return (double) Math.round(finalNumber * calcScale) / calcScale;
    }

    /**
     * Given a certain value, the range of possible values, and a different range, converts the given value
     * from its range to the given second range.
     * <p></p>
     * For example let's say I have a value of 0 that can go from -100 to 100 and I want to convert the
     * value to a range of 0 to 100, the converted value will be 50 (0 is at the middle in the -100-100 range, and
     * 50 is at the middle in the 0-100 range).
     */
    public static float mapOneRangeToAnother(float value, NumberRange<Float> fromRange, NumberRange<Float> toRange, int decimalPrecision) {
        double deltaA = fromRange.getMax() - fromRange.getMin();
        double deltaB = toRange.getMax() - toRange.getMin();
        double scale = deltaB / deltaA;
        double negA = -1 * fromRange.getMin();
        double offset = (negA * scale) + toRange.getMin();
        double finalNumber = (value * scale) + offset;
        int calcScale = (int) Math.pow(10, decimalPrecision);
        return (float) Math.round(finalNumber * calcScale) / calcScale;
    }

    /**
     * Given a certain value, the range of possible values, and a different range, converts the given value
     * from its range to the given second range.
     * <p></p>
     * For example let's say I have a value of 0 that can go from -100 to 100 and I want to convert the
     * value to a range of 0 to 100, the converted value will be 50 (0 is at the middle in the -100-100 range, and
     * 50 is at the middle in the 0-100 range).
     */
    public static int mapOneRangeToAnother(int value, NumberRange<Integer> fromRange, NumberRange<Integer> toRange, int decimalPrecision) {
        double deltaA = fromRange.getMax() - fromRange.getMin();
        double deltaB = toRange.getMax() - toRange.getMin();
        double scale = deltaB / deltaA;
        double negA = -1 * fromRange.getMin();
        double offset = (negA * scale) + toRange.getMin();
        double finalNumber = (value * scale) + offset;
        int calcScale = (int) Math.pow(10, decimalPrecision);
        return (int) Math.round(finalNumber * calcScale) / calcScale;
    }

    /**
     * Given a certain value, the range of possible values, and a different range, converts the given value
     * from its range to the given second range.
     * <p></p>
     * For example let's say I have a value of 0 that can go from -100 to 100 and I want to convert the
     * value to a range of 0 to 100, the converted value will be 50 (0 is at the middle in the -100-100 range, and
     * 50 is at the middle in the 0-100 range).
     */
    public static long mapOneRangeToAnother(long value, NumberRange<Long> fromRange, NumberRange<Long> toRange, int decimalPrecision) {
        double deltaA = fromRange.getMax() - fromRange.getMin();
        double deltaB = toRange.getMax() - toRange.getMin();
        double scale = deltaB / deltaA;
        double negA = -1 * fromRange.getMin();
        double offset = (negA * scale) + toRange.getMin();
        double finalNumber = (value * scale) + offset;
        int calcScale = (int) Math.pow(10, decimalPrecision);
        return Math.round(finalNumber * calcScale) / calcScale;
    }


    public static <T> void applyConsumer(T value, Consumer<T> consumer) {
        if (value != null) consumer.accept(value);
    }

    /**
     * Utility function which returns either {@code less} or {@code more}
     * depending on which {@code value} is closer to. If {@code value}
     * is perfectly between them, then either may be returned.
     */
    public static double nearest(double less, double value, double more) {
        double lessDiff = value - less;
        double moreDiff = more - value;
        if (lessDiff < moreDiff) return less;
        return more;
    }


    /*
     ** Returns true if the platform is to use Two-Level-Focus.
     ** This is in the Util class to ease any changes in
     ** the criteria for enabling this feature.
     **
     ** TwoLevelFocus is needed on platforms that
     ** only support 5-button navigation (arrow keys and Select/OK).
     **
     */
    public static boolean isTwoLevelFocus() {
        return Platform.isSupported(ConditionalFeature.TWO_LEVEL_FOCUS);
    }

}