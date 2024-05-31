package vn.conyeu.javafx.controls;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.css.CssMetaData;
import javafx.css.Styleable;
import javafx.css.StyleableBooleanProperty;
import javafx.geometry.Orientation;
import javafx.scene.control.Skin;
import javafx.util.StringConverter;
import vn.conyeu.javafx.controls.skin.RangeSliderSkin;
import vn.conyeu.javafx.styleable.CssFactory;
import vn.conyeu.javafx.utils.FxUtils;

import java.util.List;

/**
 * The RangeSlider2 control is simply a JavaFX {@link Slider} control with support
 * for two 'thumbs', rather than one. A thumb is the non-technical name for the
 * draggable area inside the Slider / RangeSlider2 that allows for a value to be
 * set.
 *
 * <p>Because the RangeSlider2 has two thumbs, it also has a few additional rules
 * and user interactions:
 *
 * <ol>
 *   <li>The 'lower value' thumb can not move past the 'higher value' thumb.
 *   <li>Whereas the {@link Slider} control only has one
 *       {@link Slider#valueProperty() value} property, the RangeSlider2 has a
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
 * <h3>Code Samples</h3>
 * Instantiating a RangeSlider2 is simple. The first decision is to decide whether
 * a horizontal or a vertical track is more appropriate. By default, RangeSlider2
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
 * required parameters to instantiate a RangeSlider2: the constructor takes
 * four doubles, representing min, max, lowValue and highValue (in that order).
 *
 * <p>For example, here is a simple horizontal RangeSlider2 that has a minimum
 * value of 0, a maximum value of 100, a low value of 10 and a high value of 90:
 *
 * <pre>{@code final RangeSlider2 hSlider = new RangeSlider2(0, 100, 10, 90);}</pre>
 *
 * <p>To configure the hSlider to look like the RangeSlider2 in the horizontal
 * RangeSlider2 screenshot above only requires a few additional properties to be
 * set:
 *
 * <pre>
 * {@code
 * final RangeSlider2 hSlider = new RangeSlider2(0, 100, 10, 90);
 * hSlider.setShowTickMarks(true);
 * hSlider.setShowTickLabels(true);
 * hSlider.setBlockIncrement(10);}</pre>
 *
 * <p>To create a vertical slider, simply do the following:
 *
 * <pre>
 * {@code
 * final RangeSlider2 vSlider = new RangeSlider2(0, 200, 30, 150);
 * vSlider.setOrientation(Orientation.VERTICAL);}</pre>
 *
 * <p>This code creates a RangeSlider2 with a min value of 0, a max value of 200,
 * a low value of 30, and a high value of 150.
 *
 * @see Slider
 */
public class RangeSlider extends javafx.scene.control.Slider {

    /**
     * Creates a new RangeSlider2 instance using default values of 0, 25, 75
     * and 100 for min/lowValue/highValue/max, respectively.
     */
    public RangeSlider() {
        this(0, 100, 25, 75);
    }

    /**
     * Creates a new RangeSlider2 instance using default values of 0, 25, 75
     * and 100 for min/lowValue/highValue/max, respectively.
     */
    public RangeSlider(double min, double max) {
        this(min, max, 25, 75);
    }

    /**
     * Instantiates a default, horizontal RangeSlider2 with the specified
     * min/max/low/high values.
     *
     * @param min The minimum allowable value that the RangeSlider2 will allow.
     * @param max The maximum allowable value that the RangeSlider2 will allow.
     * @param lowValue The initial value for the low value in the RangeSlider2.
     * @param highValue The initial value for the high value in the RangeSlider2.
     */
    public RangeSlider(double min, double max, double lowValue, double highValue) {
        getStyleClass().setAll("range-slider");
        setLabelFormatter(new StringConverter<Double>() {
            @Override
            public String toString(Double object) {
                return object == null ? null : object.toString();
            }

            @Override
            public Double fromString(String string) {
                return null;
            }
        });
        setMax(max);setMin(min);
        adjustValues();
        setValue(lowValue);
        setHighValue(highValue);
    }

    /**
     * {@inheritDoc}
     * @deprecated use method {@link #incrementLowValue()}
     * @see #incrementLowValue()
     * */
    public void increment() {
        incrementLowValue();
    }

    /**
     * Increments the {@link #valueProperty() low value} by the {@link #blockIncrementProperty() block increment} amount.
     */
    public void incrementLowValue() {
        adjustLowValue(getValue() + getBlockIncrement());
    }

    /**
     * {@inheritDoc}
     * @deprecated use method {@link #decrementLowValue()}
     * @see #decrementLowValue()
     * */
    public void decrement() {
        decrementLowValue();
    }

    /**
     * Decrements the {@link #valueProperty() low value} by the {@link #blockIncrementProperty() block increment} amount.
     */
    public void decrementLowValue() {
        adjustLowValue(getValue() - getBlockIncrement());
    }

    /**
     * Increments the {@link #highValueProperty() high value} by the {@link #blockIncrementProperty() block increment} amount.
     */
    public void incrementHighValue() {
        adjustHighValue(getHighValue() + getBlockIncrement());
    }

    /**
     * Decrements the {@link #highValueProperty() high value} by the {@link #blockIncrementProperty() block increment} amount.
     */
    public void decrementHighValue() {
        adjustHighValue(getHighValue() - getBlockIncrement());
    }

    /**
     * {@inheritDoc}
     * @deprecated use method {@link #adjustLowValue(double)}
     * @see #adjustLowValue(double)
     * */
    public void adjustValue(double newValue) {
        adjustLowValue(newValue);
    }

    /**
     * Adjusts {@link #valueProperty() lowValue} to match <code>newValue</code>,
     * or as closely as possible within the constraints imposed by the {@link #minProperty() min} and {@link #maxProperty() max} properties.
     * This function also takes into account {@link #snapToTicksProperty() snapToTicks}, which is the main difference between <code>adjustLowValue</code> and {@link #setValue(double) setValue}.
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
     * Adjusts {@link #highValueProperty() highValue} to match <code>newValue</code>,  or as closely as possible within the constraints imposed by the {@link #minProperty() min} and {@link #maxProperty() max} properties.
     * This function also takes into account {@link #snapToTicksProperty() snapToTicks}, which is the main difference between <code>adjustHighValue</code> and {@link #setHighValue(double) setHighValue}.
     */
    public void adjustHighValue(double newValue) {
        final double min = getMin(), max = getMax();
        if (min < max) {
            newValue = Math.max(newValue, min);
            newValue = Math.min(newValue, max);
            setHighValue(snapValueToTicks(newValue));
        }
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
        final double min = getMin(), max = getMax();
        final double lmin = getValue(), hmax = getHighValue();

        //We first look if the LowValue is between the min and max.
        if (lmin < min || lmin > max) {
            setValue(FxUtils.clamp(lmin, min, max));
        }

        // If the LowValue seems right, we check if it's not superior to
        // HighValue ONLY if the highValue itself is right. Because it may
        // happen that the highValue has not yet been computed and is
        // wrong, and therefore force the lowValue to change in a wrong way
        // which may end up in an infinite loop.
        else if (lmin >= hmax && (hmax >= min && hmax <= max)) {
            setValue(FxUtils.clamp(lmin, min, hmax));
        }
    }

    /**
     * Ensures that min is always < max, that value is always
     * somewhere between the two, and that if snapToTicks is set then the
     * value will always be set to align with a tick mark.
     */
    private void adjustHighValues() {
        final double min = getMin(), max = getMax();
        final double lmin = getValue(), hmax = getHighValue();

        if (hmax < min || hmax > max) {
            setHighValue(FxUtils.clamp(hmax, min, max));
        } //
        else if (hmax < lmin && (lmin >= min && lmin <= max)) {
            setHighValue(FxUtils.clamp(hmax, lmin, max));
        }
    }

    /**
     * Utility function which, given the specified value, will position it
     * either aligned with a tick, or simply clamp between min & max value,
     * depending on whether snapToTicks is set.
     * <p>>Note: This function is intended to be used by experts, primarily
     * by those implementing new Skins or Behaviors. It is not common
     * for developers or designers to access this function directly.</p
     */
    public double snapValueToTicks(double snapValue) {
        final double min = getMin(), max = getMax();
        final int tickCount = getMinorTickCount();
        final double tickUnit = getMajorTickUnit();

        double value = snapValue;

        if (isSnapToTicks()) {
            double tickSpacing = tickCount == 0 ? tickUnit : tickUnit/ (Math.max(tickCount, 0.0) + 1);
            double prevTick = (value - min) / tickSpacing;
            double prevTickValue =  prevTick * tickSpacing + min;
            double nextTickValue = (prevTick + 1) * tickSpacing + min;
            value = FxUtils.nearest(prevTickValue, value, nextTickValue);
        }
        return FxUtils.clamp(value, min, max);
    }

    public boolean isVertical() {
        return getOrientation() == Orientation.VERTICAL;
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
                @Override
                protected void invalidated() {
                    adjustHighValues();
                }
            };
        }
        return highValue;
    }

    /**
     * When true, indicates the current high value of this RangeSlider2 is changing.
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

    /**
     * When true any changes to the axis and its range will be animated.
     * <p>Indicates whether the changes to axis range will be animated or not.</p>
     */
    private StyleableBooleanProperty tickAnimated;
    public final boolean isTickAnimated() {return tickAnimatedProperty().get();}
    public final void setTickAnimated(boolean value) {tickAnimatedProperty().set(value);}
    public final StyleableBooleanProperty tickAnimatedProperty() {
        if(tickAnimated == null) {
            tickAnimated = FACTORY.createBoolean(this, "tickAnimated", "-fx-tick-animated");
        }
        return tickAnimated;
    }

    //@formatter:on

    @Override
    protected Skin<?> createDefaultSkin() {
        return new RangeSliderSkin(this);
    }

    /**
     * {@inheritDoc}
     */
    public List<CssMetaData<? extends Styleable, ?>> getControlCssMetaData() {
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
    static final CssFactory<RangeSlider> FACTORY = new CssFactory<>(javafx.scene.control.Slider.getClassCssMetaData()) {
        @Override
        protected void registerCss() {
            FACTORY.createBooleanCss("-fx-tick-animated", RangeSlider::tickAnimatedProperty, true);

        }
    };

}