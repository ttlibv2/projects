package vn.conyeu.commons.function;

@FunctionalInterface
public interface Callable<V> {
    V call();
}