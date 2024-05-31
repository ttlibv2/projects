package vn.conyeu.javafx.controls;


import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.css.CssMetaData;
import javafx.css.Styleable;
import javafx.css.StyleableBooleanProperty;
import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.control.Skin;
import javafx.scene.control.Slider;
import javafx.stage.PopupWindow;
import javafx.stage.Window;
import vn.conyeu.javafx.controls.skin.RangeSliderSkin;
import vn.conyeu.javafx.property.FunctionProperty;
import vn.conyeu.javafx.styleable.CssFactory;

import java.util.List;
import java.util.function.Function;

/**
 * The RangeSlider control is simply a JavaFX {@link Slider} control with support
 * for two 'thumbs', rather than one. A thumb is the non-technical name for the
 * draggable area inside the Slider / RangeSlider that allows for a value to be
 * set.
 *
 * <p>Because the RangeSlider has two thumbs, it also has a few additional rules
 * and user interactions:
 *
 * <ol>
 *   <li>The 'lower value' thumb can not move past the 'higher value' thumb.
 *   <li>Whereas the {@link Slider} control only has one
 *       {@link Slider#valueProperty() value} property, the RangeSlider has a
 *       {@link #valueProperty() low value} and a
 *       {@link #highValueProperty() high value} property, not surprisingly
 *       represented by the 'low value' and 'high value' thumbs.
 *   <li>The area between the low and high values represents the allowable range.
 *       For example, if the low value is 2 and the high value is 8, then the
 *       allowable range is between 2 and 8.
 *   <li>The allowable range area is rendered differently. This area is able to
 *       be dragged with mouse / touch input to allow for the entire range to
 *       be modified. For example, following on from the previous example of the
 *       allowable range being between 2 and 8, if the user drags the range bar
 *       to the right, the low value will adjust to 3, and the high value 9, and
 *       so on until the user stops adjusting.
 * </ol>
 *
 * <h3>Screenshots</h3>
 * Because the RangeSlider supports both horizontal and vertical
 * {@link #orientationProperty() orientation}, there are two screenshots below:
 *
 * <table border="0" summary="Screenshot of RangeSlider orientation">
 *   <tr>
 *     <td width="75" valign="center"><strong>Horizontal:</strong></td>
 *     <td><img src="rangeSlider-horizontal.png" alt="Screenshot of a horizontal RangeSlider"></td>
 *   </tr>
 *   <tr>
 *     <td width="75" valign="top"><strong>Vertical:</strong></td>
 *     <td><img src="rangeSlider-vertical.png" alt="Screenshot of a vertical RangeSlider"></td>
 *   </tr>
 * </table>
 *
 * <h3>Code Samples</h3>
 * Instantiating a RangeSlider is simple. The first decision is to decide whether
 * a horizontal or a vertical track is more appropriate. By default RangeSlider
 * instances are horizontal, but this can be changed by setting the
 * {@link #orientationProperty() orientation} property.
 *
 * <p>Once the orientation is determined, the next most important decision is
 * to determine what the {@link #minProperty() min} / {@link #maxProperty() max}
 * and default {@link #valueProperty() low} / {@link #highValueProperty() high}
 * values are. The min / max values represent the smallest and largest legal
 * values for the thumbs to be set to, whereas the low / high values represent
 * where the thumbs are currently, within the bounds of the min / max values.
 * Because all four values are required in all circumstances, they are all
 * required parameters to instantiate a RangeSlider: the constructor takes
 * four doubles, representing min, max, lowValue and highValue (in that order).
 *
 * <p>For example, here is a simple horizontal RangeSlider that has a minimum
 * value of 0, a maximum value of 100, a low value of 10 and a high value of 90:
 *
 * <pre>{@code final RangeSlider hSlider = new RangeSlider(0, 100, 10, 90);}</pre>
 *
 * <p>To configure the hSlider to look like the RangeSlider in the horizontal
 * RangeSlider screenshot above only requires a few additional properties to be
 * set:
 *
 * <pre>
 * {@code
 * final RangeSlider hSlider = new RangeSlider(0, 100, 10, 90);
 * hSlider.setShowTickMarks(true);
 * hSlider.setShowTickLabels(true);
 * hSlider.setBlockIncrement(10);}</pre>
 *
 * <p>To create a vertical slider, simply do the following:
 *
 * <pre>
 * {@code
 * final RangeSlider vSlider = new RangeSlider(0, 200, 30, 150);
 * vSlider.setOrientation(Orientation.VERTICAL);}</pre>
 *
 * <p>This code creates a RangeSlider with a min value of 0, a max value of 200,
 * a low value of 30, and a high value of 150.
 *
 * @see Slider
 */
public class RangeSlider extends Slider {

    /**
     * Creates a new RangeSlider instance using default values of 0.0, 0.25, 0.75
     * and 1.0 for min/lowValue/highValue/max, respectively.
     */
    public RangeSlider() {
        this(0, 100, 25, 75);
    }

    public RangeSlider(double min, double max) {
        getStyleClass().setAll("slider", "range-slider");
        setMax(max);
        setMin(min);
        adjustValues();
    }

    /**
     * Instantiates a default, horizontal RangeSlider with the specified
     * min/max/low/high values.
     *
     * @param min       The minimum allowable value that the RangeSlider will allow.
     * @param max       The maximum allowable value that the RangeSlider will allow.
     * @param lowValue  The initial value for the low value in the RangeSlider.
     * @param highValue The initial value for the high value in the RangeSlider.
     */
    public RangeSlider(double min, double max, double lowValue, double highValue) {
        this(min, max);
        setValue(lowValue);
        setHighValue(highValue);
    }


    /**
     * Increments the {@link #valueProperty() low value} by the
     * {@link #blockIncrementProperty() block increment} amount.
     */
    public void incrementLowValue() {
        adjustLowValue(getValue() + getBlockIncrement());
    }

    /**
     * Decrements the {@link #valueProperty() low value} by the
     * {@link #blockIncrementProperty() block increment} amount.
     */
    public void decrementLowValue() {
        adjustLowValue(getValue() - getBlockIncrement());
    }

    /**
     * Increments the {@link #highValueProperty() high value} by the
     * {@link #blockIncrementProperty() block increment} amount.
     */
    public void incrementHighValue() {
        adjustHighValue(getHighValue() + getBlockIncrement());
    }

    /**
     * Decrements the {@link #highValueProperty() high value} by the
     * {@link #blockIncrementProperty() block increment} amount.
     */
    public void decrementHighValue() {
        adjustHighValue(getHighValue() - getBlockIncrement());
    }

    /**
     * Adjusts {@link #valueProperty() lowValue} to match <code>newValue</code>,
     * or as closely as possible within the constraints imposed by the
     * {@link #minProperty() min} and {@link #maxProperty() max} properties.
     * This function also takes into account
     * {@link #snapToTicksProperty() snapToTicks}, which is the main difference
     * between <code>adjustLowValue</code> and
     * {@link #setValue(double) setLowValue}.
     */
    public void adjustLowValue(double newValue) {
        final double min = getMin(), max = getMax();
        if (min < max) {
            newValue = Math.max(newValue, min);
            newValue = Math.min(newValue, max);
            setValue(snapValueToTicks(newValue));
        }
    }

    /**
     * Adjusts {@link #highValueProperty() highValue} to match <code>newValue</code>,
     * or as closely as possible within the constraints imposed by the
     * {@link #minProperty() min} and {@link #maxProperty() max} properties.
     * This function also takes into account
     * {@link #snapToTicksProperty() snapToTicks}, which is the main difference
     * between <code>adjustHighValue</code> and
     * {@link #setHighValue(double) setHighValue}.
     */
    public void adjustHighValue(double newValue) {
        final double min = getMin(), max = getMax();
        if (min < max) {
            newValue = Math.max(newValue, min);
            newValue = Math.min(newValue, max);
            setHighValue(snapValueToTicks(newValue));
        }
    }

    //@formatter:off

    /**
     * The high value property represents the current position of the high value
     * thumb, and is within the allowable range as specified by the
     * {@link #minProperty() min} and {@link #maxProperty() max} properties. By
     * default, this value is 100.
     */
    private DoubleProperty highValue;
    public final double getHighValue() {return highValueProperty().get();}
    public final void setHighValue(double value) {highValueProperty().set(value);}
    public final DoubleProperty highValueProperty() {
        if(highValue == null) {
            highValue = new SimpleDoubleProperty(this, "highValue", 100) {
                protected void invalidated() {
                    adjustHighValues();
                }
            };
        }
        return highValue;
    }

    /**
     * When true, indicates the current high value of this RangeSlider is changing.
     * It provides notification that the high value is changing. Once the high
     * value is computed, it is set back to false.
     */
    private BooleanProperty highValueChanging;
    public final boolean isHighValueChanging() {return highValueChangingProperty().get();}
    public final void setHighValueChanging(boolean value) {highValueChangingProperty().set(value);}
    public final BooleanProperty highValueChangingProperty() {
        if(highValueChanging == null) {
            highValueChanging = new SimpleBooleanProperty(this, "highValueChanging", false);
        }
        return highValueChanging;
    }

    /** visibleRangeBar */
    private StyleableBooleanProperty visibleRangeBar;
    public final boolean isVisibleRangeBar() {return visibleRangeBarProperty().get();}
    public final void setVisibleRangeBar(boolean value) {visibleRangeBarProperty().set(value);}
    public final StyleableBooleanProperty visibleRangeBarProperty() {
        if(visibleRangeBar == null) {
            visibleRangeBar = FACTORY.createBoolean(this, "visibleRangeBar", "-fx-visible-range-bar");
        }
        return visibleRangeBar;
    }

    /** thumbFactory */
    private FunctionProperty<RangeSlider, Node> thumbFactory;
    public final Function<RangeSlider, Node> getThumbFactory() {return thumbFactoryProperty().get();}
    public final void setThumbFactory(Function<RangeSlider, Node> value) {thumbFactoryProperty().set(value);}
    public final FunctionProperty<RangeSlider, Node> thumbFactoryProperty() {
        if(thumbFactory == null) {
            thumbFactory = new FunctionProperty<>(this, "thumbFactory");
        }
        return thumbFactory;
    }

    //@formatter:on


    /**
     * {@inheritDoc}
     */
    @Override
    protected Skin<?> createDefaultSkin() {
        return new RangeSliderSkin(this);
    }

    /**
     * Ensures that min is always < max, that value is always
     * somewhere between the two, and that if snapToTicks is set then the
     * value will always be set to align with a tick mark.
     */
    private void adjustValues() {
        adjustLowValues();
        adjustHighValues();
    }

    private void adjustLowValues() {
        //We first look if the LowValue is between the min and max.
        if (getValue() < getMin() || getValue() > getMax()) {
            double value = Utils.clamp(getMin(), getValue(), getMax());
            setValue(value);

        }

        // If the LowValue seems right, we check if it's not superior to
        // HighValue ONLY if the highValue itself is right. Because it may
        // happen that the highValue has not yet been computed and is
        // wrong, and therefore force the lowValue to change in a wrong way
        // which may end up in an infinite loop.
        else if (getValue() >= getHighValue() && (getHighValue() >= getMin() && getHighValue() <= getMax())) {
            double value = Utils.clamp(getMin(), getValue(), getHighValue());
            setValue(value);
        }
    }

    public double snapValueToTicks(double d) {
        double d1 = d;
        if (isSnapToTicks()) {
            double d2 = 0.0D;
            if (getMinorTickCount() != 0) {
                d2 = getMajorTickUnit() / (double) (Math.max(getMinorTickCount(), 0) + 1);
            } else {
                d2 = getMajorTickUnit();
            }
            int i = (int) ((d1 - getMin()) / d2);
            double d3 = (double) i * d2 + getMin();
            double d4 = (double) (i + 1) * d2 + getMin();
            d1 = Utils.nearest(d3, d1, d4);
        }
        return Utils.clamp(getMin(), d1, getMax());
    }

    private void adjustHighValues() {
        if (getHighValue() < getMin() || getHighValue() > getMax()) {
            setHighValue(Utils.clamp(getMin(), getHighValue(), getMax()));
        } else if (getHighValue() < getValue() && (getValue() >= getMin() && getValue() <= getMax())) {
            setHighValue(Utils.clamp(getValue(), getHighValue(), getMax()));
        }
    }

    public boolean isVertical() {
        return getOrientation() == Orientation.VERTICAL;
    }


    @Override
    protected List<CssMetaData<? extends Styleable, ?>> getControlCssMetaData() {
        return getClassCssMetaData();
    }

    /**
     * Gets the {@code CssMetaData} associated with this class, which may include the {@code CssMetaData} of its superclasses.
     *
     * @return the {@code CssMetaData}
     */
    public static List<CssMetaData<? extends Styleable, ?>> getClassCssMetaData() {
        return FACTORY.getCssMetaData();
    }

    /**
     * Methods for creating instances of StyleableProperty
     */
    static final CssFactory<RangeSlider> FACTORY = new CssFactory<>(Slider.getClassCssMetaData()) {
        @Override
        protected void registerCss() {
            FACTORY.createBooleanCss("-fx-visible-range-bar", RangeSlider::visibleRangeBarProperty, true);

        }

    };

    public static class Utils {

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
                // lets just get the focused stage and show the dialog in there
                List<Window> windows = Window.getWindows();
                for (Window w : windows) {
                    window = w;
                    if (window.isFocused() && !(window instanceof PopupWindow)) {
                        return window;
                    }
                }
                return window;
            } else if (owner instanceof Window) {
                return (Window) owner;
            } else if (owner instanceof Node) {
                return ((Node) owner).getScene().getWindow();
            } else {
                throw new IllegalArgumentException("Unknown owner: " + owner.getClass()); //$NON-NLS-1$
            }
        }

        /**
         * Return a letter (just like Excel) associated with the number. When the
         * number is under 26, a simple letter is returned. When the number is
         * superior, concatenated letters are returned.
         *
         *
         * For example: 0 -&gt; A 1 -&gt; B 26 -&gt; AA 32 -&gt; AG 45 -&gt; AT
         *
         *
         * @param number the number whose Excel Letter is to be found.
         * @return a letter (like) associated with the number.
         */
        public static final String getExcelLetterFromNumber(int number) {
            String letter = ""; //$NON-NLS-1$
            // Repeatedly divide the number by 26 and convert the
            // remainder into the appropriate letter.
            while (number >= 0) {
                final int remainder = number % 26;
                letter = (char) (remainder + 'A') + letter;
                number = number / 26 - 1;
            }

            return letter;
        }

        /**
         * Simple utility function which clamps the given value to be strictly
         * between the min and max values.
         */
        public static double clamp(double min, double value, double max) {
            if (value < min) return min;
            if (value > max) return max;
            return value;
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
    }
}