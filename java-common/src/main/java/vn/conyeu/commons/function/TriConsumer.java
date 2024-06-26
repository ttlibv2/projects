package vn.conyeu.commons.function;

import java.util.Objects;

@FunctionalInterface
public interface TriConsumer<T1, T2, T3>{

    /**
     * Performs this operation on the given argument.
     */
    void accept(T1 t1, T2 t2, T3 t3);

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
    default TriConsumer<T1, T2, T3> andThen(TriConsumer<T1, T2, T3> after) {
        Objects.requireNonNull(after);
        return (T1 t1, T2 t2, T3 t3) -> { accept(t1, t2, t3); after.accept(t1, t2, t3); };
    }
}