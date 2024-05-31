package vn.conyeu.javafx.beans;


import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * Implementation of {@link NumberRange} to represent a Float range.
 */
public class FloatRange extends NumberRange<Float> {

    //================================================================================
    // Constructors
    //================================================================================
    public FloatRange(Float min, Float max) {
        super(min, max);
    }

    //================================================================================
    // Static Methods
    //================================================================================

    /**
     * @return a new instance of {@code FloatRange} with the given min and max bounds.
     */
    public static FloatRange of(Float min, Float max) {
        return new FloatRange(min, max);
    }

    /**
     * @return a new instance of {@code FloatRange} with the given val as both min and max bounds.
     */
    public static FloatRange of(Float val) {
        return new FloatRange(val, val);
    }

    /**
     * Checks if the given value is contained in the given range (bounds are included).
     */
    public static boolean inRangeOf(float val, FloatRange range) {
        return Math.max(range.getMin(), val) == Math.min(val, range.getMax());
    }

    /**
     * Expands a range of floats to a {@code List} with the given step.
     */
    public static List<Float> expandRange(FloatRange range, float step) {
        List<Float> l = new ArrayList<>();
        float start = range.getMin();
        do {
            l.add(start);
            start += step;
        } while (start <= range.getMax());
        return l;
    }

    /**
     * Expands a range of floats to a {@code Set} with the given step.
     * <p>
     * The {@code Set} is ordered.
     */
    public static Set<Float> expandRangeToSet(FloatRange range, float step) {
        Set<Float> s = new LinkedHashSet<>();
        float start = range.getMin();
        do {
            s.add(start);
            start += step;
        } while (start <= range.getMax());
        return s;
    }

    /**
     * Expands a range of floats to an array.
     */
    public static Float[] expandRangeToArray(float min, float max, float step) {
        return expandRange(of(min, max), step).toArray(Float[]::new);
    }

    //================================================================================
    // Overridden Methods
    //================================================================================
    @Override
    public Float sum() {
        return getMin() + getMax();
    }

    @Override
    public Float diff() {
        return getMax() - getMin();
    }
}