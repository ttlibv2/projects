package vn.conyeu.google.sheetdb.builder;

import java.util.function.Supplier;

public interface XmlBuilder<T> {
    T build();

    static <E> E ifNull(E value, Supplier<E> supplier) {
        return value == null ? supplier.get(): value;
    }
}