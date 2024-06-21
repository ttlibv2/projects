export type Consumer<E> = (value: E) => void;
export type Runnable = () => void;
export type Predicate<E> = (value: E) => boolean;
export type Callback<E, R> = (value: E) => R;
export type Supplier<E> = () => E;