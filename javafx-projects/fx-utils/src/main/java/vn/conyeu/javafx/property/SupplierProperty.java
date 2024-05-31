package vn.conyeu.javafx.property;

import javafx.beans.property.SimpleObjectProperty;

import java.util.function.Supplier;

public class SupplierProperty<T> extends SimpleObjectProperty<Supplier<T>> {

    public SupplierProperty() {
    }

    public SupplierProperty(Supplier<T> initialValue) {
        super(initialValue);
    }

    public SupplierProperty(Object bean, String name) {
        super(bean, name);
    }

    public SupplierProperty(Object bean, String name, Supplier<T> initialValue) {
        super(bean, name, initialValue);
    }
}