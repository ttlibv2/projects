package vn.conyeu.javafx.property;


import javafx.beans.property.ReadOnlyObjectWrapper;
import vn.conyeu.javafx.beans.IntegerRange;

public class IntegerRangeProperty extends ReadOnlyObjectWrapper<IntegerRange> {
    /**
     * The constructor of {@code ObjectProperty}
     */
    public IntegerRangeProperty() {
    }

    /**
     * The constructor of {@code ObjectProperty}
     *
     * @param initialValue the initial value of the wrapped value
     */
    public IntegerRangeProperty(IntegerRange initialValue) {
        super(initialValue);
    }

    /**
     * The constructor of {@code ObjectProperty}
     *
     * @param bean the bean of this {@code ObjectProperty}
     * @param name the name of this {@code ObjectProperty}
     */
    public IntegerRangeProperty(Object bean, String name) {
        super(bean, name);
    }

    /**
     * The constructor of {@code ObjectProperty}
     *
     * @param bean         the bean of this {@code ObjectProperty}
     * @param name         the name of this {@code ObjectProperty}
     * @param initialValue the initial value of the wrapped value
     */
    public IntegerRangeProperty(Object bean, String name, IntegerRange initialValue) {
        super(bean, name, initialValue);
    }


}