export type Consumer<E> = (value: E) => void;
export type Runnable = () => void;
export type Predicate<E> = (value: E) => boolean;
export type Callback<E, R> = (value: E) => R;
export type Supplier<E> = () => E;
export type BiFunction<T, U, R> =  (t: T, u: U) => R;