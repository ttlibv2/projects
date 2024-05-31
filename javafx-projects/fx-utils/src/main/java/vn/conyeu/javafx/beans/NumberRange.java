package vn.conyeu.javafx.beans;

import java.util.*;
import java.util.stream.IntStream;

/**
 * Simple bean to represent a range of values from min to max.
 * @param <T> The type of Number to represent
 */
public class NumberRange<T extends Number> {
    /**
     * Returns a new instance of NumberRange with the given min and max bounds.
     */
    public static <T extends Number> NumberRange<T> of(T min, T max) {
        return new NumberRange<>(min, max);
    }

    /**
     * Returns a new instance of NumberRange with the given val as both min and max bounds.
     */
    public static <T extends Number> NumberRange<T> of(T val) {
        return new NumberRange<>(val, val);
    }

    /**
     * Checks if the given value is contained in the given range (bounds are included).
     */
    public static boolean inRangeOf(double val, NumberRange<Double> range) {
        return Math.max(range.getMin(), val) == Math.min(val, range.getMax());
    }

    /**
     * Checks if the given value is contained in the given range (bounds are included).
     */
    public static boolean inRangeOf(float val, NumberRange<Float> range) {
        return Math.max(range.getMin(), val) == Math.min(val, range.getMax());
    }

    /**
     * Checks if the given value is contained in the given range (bounds are included).
     */
    public static boolean inRangeOf(int val, NumberRange<Integer> range) {
        return Math.max(range.getMin(), val) == Math.min(val, range.getMax());
    }

    /**
     * Checks if the given value is contained in the given range (bounds are included).
     */
    public static boolean inRangeOf(long val, NumberRange<Long> range) {
        return Math.max(range.getMin(), val) == Math.min(val, range.getMax());
    }

    /**
     * Checks if the given value is contained in any of the given ranges (bounds are included).
     */
    public static boolean inRangeOf(double val, List<DoubleRange> ranges) {
        return ranges.stream().anyMatch(range -> inRangeOf(val, range));
    }

    /**
     * Checks if the given value is contained in any of the given ranges (bounds are included).
     */
    public static boolean inRangeOf(float val, List<FloatRange> ranges) {
        return ranges.stream().anyMatch(range -> inRangeOf(val, range));
    }

    /**
     * Checks if the given value is contained in any of the given ranges (bounds are included).
     */
    public static boolean inRangeOf(int val, List<IntegerRange> ranges) {
        return ranges.stream().anyMatch(range -> inRangeOf(val, range));
    }

    /**
     * Checks if the given value is contained in any of the given ranges (bounds are included).
     */
    public static boolean inRangeOf(long val, List<LongRange> ranges) {
        return ranges.stream().anyMatch(range -> inRangeOf(val, range));
    }

    /**
     * Expands a range of integers to a List of integers.
     */
    public static List<Integer> expandRange(NumberRange<Integer> range) {
        return IntStream.rangeClosed(range.getMin(), range.getMax()).collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
    }

    /**
     * Expands a range of integers to a List of integers.
     */
    public static List<Integer> expandRange(int min, int max) {
        return IntStream.rangeClosed(min, max).collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
    }

    /**
     * Expands a range of integers to a Set of integers.
     */
    public static Set<Integer> expandRangeToSet(NumberRange<Integer> range) {
        return IntStream.rangeClosed(range.getMin(), range.getMax()).collect(HashSet::new, HashSet::add, HashSet::addAll);
    }

    /**
     * Expands a range of integers to a Set of integers.
     */
    public static Set<Integer> expandRangeToSet(int min, int max) {
        return IntStream.rangeClosed(min, max).collect(HashSet::new, HashSet::add, HashSet::addAll);
    }

    /**
     * Expands a range of integers to an array of integers.
     */
    public static Integer[] expandRangeToArray(int min, int max) {
        return IntStream.rangeClosed(min, max).boxed().toArray(Integer[]::new);
    }

    //==========================================

    private final T min;
    private final T max;

    public NumberRange(T min, T max) {
        this.min = min;
        this.max = max;
    }

    /**
     * @return the lower bound
     */
    public T getMin() {
        return min;
    }

    /**
     * @return the upper bound
     */
    public T getMax() {
        return max;
    }

    public T sum() {
        throw new UnsupportedOperationException();
    }

    public  T diff() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NumberRange<?> that = (NumberRange<?>) o;
        return Objects.equals(min, that.min) && Objects.equals(max, that.max);
    }

    @Override
    public int hashCode() {
        return Objects.hash(min, max);
    }

    @Override
    public String toString() {
        return "Min[" + min + "], Max[" + max + "]";
    }

}