package vn.conyeu.google.sheet.builder;

import java.util.Objects;

@FunctionalInterface
public interface ConsumerReturn<T> {
    T accept(T object);


    /**
     * Returns a composed {@code Consumer} that performs, in sequence, this
     * operation followed by the {@code after} operation. If performing either
     * operation throws an exception, it is relayed to the caller of the
     * composed operation.  If performing this operation throws an exception,
     * the {@code after} operation will not be performed.
     *
     * @param after the operation to perform after this operation
     * @return a composed {@code Consumer} that performs in sequence this
     * operation followed by the {@code after} operation
     * @throws NullPointerException if {@code after} is null
     */
    default ConsumerReturn<T> andThen(ConsumerReturn<T> after) {
        Objects.requireNonNull(after);
        return (T t) -> { accept(t); return after.accept(t); };
    }
}