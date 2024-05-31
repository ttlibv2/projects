package vn.conyeu.commons.function;

@FunctionalInterface
public interface ToStringFunction<T> {
    CharSequence applyAString(T value);
}