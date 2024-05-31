package vn.conyeu.commons.utils;

import vn.conyeu.commons.function.ToStringFunction;

import java.io.Serializable;
import java.util.Collections;
import java.util.Comparator;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.ToDoubleFunction;
import java.util.function.ToIntFunction;
import java.util.function.ToLongFunction;

public final class Comparators {

    public static <T> Comparator<T> comparingString(ToStringFunction<? super T> keyExtractor) {
        Objects.requireNonNull(keyExtractor, "@keyExtractor");
        return (Comparator<T> & Serializable) (c1, c2) -> CharSequence.compare(keyExtractor.applyAString(c1), keyExtractor.applyAString(c2));
    }


    /**
     * @see return {@link Comparator#reverseOrder()}
     */
    public static <T extends Comparable<? super T>> Comparator<T> reverseOrder() {
        return Collections.reverseOrder();
    }

    /**
     * @see return {@link Comparator#naturalOrder()}
     */
    public static <T extends Comparable<? super T>> Comparator<T> naturalOrder() {
        return java.util.Comparator.naturalOrder();
    }

    /**
     * @see return {@link java.util.Comparator#nullsFirst(Comparator)}
     */
    public static <T> Comparator<T> nullsFirst(Comparator<? super T> comparator) {
        return java.util.Comparator.nullsFirst(comparator);
    }

    /**
     * @see return {@link java.util.Comparator#nullsLast(Comparator)}
     */
    public static <T> Comparator<T> nullsLast(Comparator<? super T> comparator) {
        return java.util.Comparator.nullsLast(comparator);
    }

    /**
     * @see return {@link java.util.Comparator#comparing(Function, Comparator)}
     */
    public static <T, U> Comparator<T> comparing(Function<? super T, ? extends U> keyExtractor, Comparator<? super U> keyComparator) {
       return java.util.Comparator.comparing(keyExtractor, keyComparator);
    }


    /**
     * @see return {@link java.util.Comparator#comparing(Function)}
     */
    public static <T, U extends Comparable<? super U>> Comparator<T> comparing(Function<? super T, ? extends U> keyExtractor) {
        return java.util.Comparator.comparing(keyExtractor);
    }

    /**
     * @see return {@link java.util.Comparator#comparingInt(ToIntFunction)}
     */
    public static <T> Comparator<T> comparingInt(ToIntFunction<? super T> keyExtractor) {
        return java.util.Comparator.comparingInt(keyExtractor);
    }

    /**
     * @see return {@link java.util.Comparator#comparingLong(ToLongFunction)}
     */
    public static <T> Comparator<T> comparingLong(ToLongFunction<? super T> keyExtractor) {
        return java.util.Comparator.comparingLong(keyExtractor);
    }

    /**
     * @see return {@link java.util.Comparator#comparingDouble(ToDoubleFunction)}
     */
    public static<T> Comparator<T> comparingDouble(ToDoubleFunction<? super T> keyExtractor) {
        return java.util.Comparator.comparingDouble(keyExtractor);
    }

}