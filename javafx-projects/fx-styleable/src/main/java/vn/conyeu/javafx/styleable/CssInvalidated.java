package vn.conyeu.javafx.styleable;

@FunctionalInterface
public interface CssInvalidated<S> {
    void accept(S oldValue, S newValue);
}