package vn.conyeu.google.sheet.builder;

import java.util.function.Supplier;

public interface XmlBuilder<T> {

    default void initialize() {}

    T build();

    static <E> E ifNull(E value, Supplier<E> supplier) {
        return value == null ? supplier.get(): value;
    }
}