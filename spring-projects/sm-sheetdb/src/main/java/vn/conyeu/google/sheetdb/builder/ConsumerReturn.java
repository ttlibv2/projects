package vn.conyeu.google.sheetdb.builder;

@FunctionalInterface
public interface ConsumerReturn<T> {
    T accept(T object);
}