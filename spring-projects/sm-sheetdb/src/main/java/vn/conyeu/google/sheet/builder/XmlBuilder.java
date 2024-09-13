package vn.conyeu.google.sheet.builder;

public interface XmlBuilder<T> {

    default void initialize() {}

    T build();
    default XmlBuilder<T> copy() {
     throw new UnsupportedOperationException();
    }

}