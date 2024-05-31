package vn.conyeu.javafx.animation.beans;

import javafx.animation.Interpolator;
import javafx.beans.value.WritableValue;

import java.util.function.Supplier;

/**
 * @param <T> the type of the {@code KeyValue}
 * */
public final class KeyValue<T> {
    private final WritableValue<T> target;
    private final T endValue;
    private final Interpolator interpolator;
    private final Supplier<Boolean> animateCondition;
    private final Supplier<WritableValue<T>> targetSupplier;
    private final Supplier<T> endValueSupplier;

    public static <T> Builder<T> builder() {
        return new Builder<>();
    }

    /**
     * Creates a {@code KeyValue}.
     * @param target  the target
     * @param endValue the end value
     * @param interpolator the {@link Interpolator}
     */
    private KeyValue(WritableValue<T> target, T endValue, Supplier<WritableValue<T>> targetSupplier,
                     Supplier<T> endValueSupplier, Supplier<Boolean> animateCondition, Interpolator interpolator) {
        this.target = target;
        this.endValue = endValue;
        this.interpolator = interpolator;
        this.targetSupplier = targetSupplier;
        this.endValueSupplier = endValueSupplier;
        this.animateCondition = animateCondition;
    }

    /**
     * Returns the end value of this {@code KeyValue}
     *
     * @return the end value
     */
    public T getEndValue() {
        return endValue == null ? endValueSupplier.get() : endValue;
    }

    /**
     * Returns the target of this {@code KeyValue}
     * */
    public WritableValue<T> getTarget() {
        return target == null ? targetSupplier.get() : target;
    }

    /**
     * {@link Interpolator} to be used for calculating the key value along the
     * particular interval. By default, {@link Interpolator#LINEAR} is used.
     * @return the interpolator to be used for calculating the key value along
     * the particular interval
     */
    public Interpolator getInterpolator() {
        return interpolator;
    }

    public boolean isValid() {
        return animateCondition == null || animateCondition.get();
    }

    public static final class Builder<T> {
        private T endValue;
        private WritableValue<T> target;
        private Supplier<WritableValue<T>> targetSupplier;
        private Supplier<T> endValueSupplier;
        private Supplier<Boolean> animateCondition = () -> true;
        private Interpolator interpolator = Interpolator.EASE_BOTH;

        private Builder() {
        }

        public Builder<T> setTarget(WritableValue<T> target) {
            this.target = target;
            return this;
        }

        public Builder<T> setTargetSupplier(Supplier<WritableValue<T>> targetSupplier) {
            this.targetSupplier = targetSupplier;
            return this;
        }

        public Builder<T> setEndValueSupplier(Supplier<T> endValueSupplier) {
            this.endValueSupplier = endValueSupplier;
            return this;
        }

        public Builder<T> setEndValue(T endValue) {
            this.endValue = endValue;
            return this;
        }

        public Builder<T> setAnimateCondition(Supplier<Boolean> animateCondition) {
            this.animateCondition = animateCondition;
            return this;
        }

        public Builder<T> setInterpolator(Interpolator interpolator) {
            this.interpolator = interpolator;
            return this;
        }

        public KeyValue<T> build() {
            return new KeyValue<>(
                    target, endValue, targetSupplier,
                    endValueSupplier, animateCondition, interpolator
            );
        }
    }




}