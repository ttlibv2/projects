package vn.conyeu.commons.function;

@FunctionalInterface
public interface ConsumerAndReturn<T> {
    T accept(T object);
}