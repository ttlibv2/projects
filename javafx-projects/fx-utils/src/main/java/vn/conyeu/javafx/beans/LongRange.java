package vn.conyeu.javafx.beans;


import java.util.*;
import java.util.stream.LongStream;

/**
 * Implementation of {@link NumberRange} to represent a Long range.
 */
public class LongRange extends NumberRange<Long> implements Iterable<Long> {

    //================================================================================
    // Constructors
    //================================================================================
    public LongRange(Long min, Long max) {
        super(min, max);
    }

    //================================================================================
    // Static Methods
    //================================================================================

    /**
     * @return a new instance of {@code LongRange} with the given min and max bounds.
     */
    public static LongRange of(Long min, Long max) {
        return new LongRange(min, max);
    }

    /**
     * @return a new instance of {@code LongRange} with the given val as both min and max bounds.
     */
    public static LongRange of(Long val) {
        return new LongRange(val, val);
    }

    /**
     * Checks if the given value is contained in the given range (bounds are included).
     */
    public static boolean inRangeOf(long val, LongRange range) {
        return Math.max(range.getMin(), val) == Math.min(val, range.getMax());
    }

    /**
     * Expands a range of longs to a {@code List} with the given step.
     */
    public static List<Long> expandRange(LongRange range, long step) {
        List<Long> l = new ArrayList<>();
        long start = range.getMin();
        do {
            l.add(start);
            start += step;
        } while (start <= range.getMax());
        return l;
    }

    /**
     * Expands a range of longs to a {@code Set} with the given step.
     * <p>
     * The {@code Set} is ordered.
     */
    public static Set<Long> expandRangeToSet(LongRange range, long step) {
        Set<Long> s = new LinkedHashSet<>();
        long start = range.getMin();
        do {
            s.add(start);
            start += step;
        } while (start <= range.getMax());
        return s;
    }

    /**
     * Expands a range of long to an array.
     */
    public static Long[] expandRangeToArray(long min, long max, long step) {
        return expandRange(of(min, max), step).toArray(Long[]::new);
    }

    //================================================================================
    // Overridden Methods
    //================================================================================
    @Override
    public Long sum() {
        return getMin() + getMax();
    }

    @Override
    public Long diff() {
        return getMax() - getMin();
    }

    @Override
    public PrimitiveIterator.OfLong iterator() {
        return LongStream.rangeClosed(getMin(), getMax()).iterator();
    }
}