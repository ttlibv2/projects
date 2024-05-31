package vn.conyeu.commons.beans;

import vn.conyeu.commons.utils.Asserts;
import vn.conyeu.commons.utils.Objects;

import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Simple value type to delay the creation of an object using a {@link Supplier} returning the produced object for
 * subsequent lookups. Note, that no concurrency control is applied during the lookup of {@link #get()}, which means in
 * concurrent access scenarios, the provided {@link Supplier} can be called multiple times.
 */
public class LazyLoad<T> implements Supplier<T> {
	private static final LazyLoad EMPTY = new LazyLoad<>(() -> null, null, true);
	static final String UNRESOLVED = "[Unresolved]";

	private final Supplier<? extends T> supplier;

	private T value;
	private volatile boolean resolved;

	/**
	 * Creates a new {@link LazyLoad} instance for the given supplier.
	 *
	 * @param supplier
	 */
	private LazyLoad(Supplier<? extends T> supplier) {
		this(supplier, null, false);
	}

	/**
	 * Creates a new {@link LazyLoad} for the given {@link Supplier}, value and whether it has been resolved or not.
	 *
	 * @param supplier must not be {@literal null}.
	 * @param value can be {@literal null}.
	 * @param resolved whether the value handed into the constructor represents a resolved value.
	 */
	private LazyLoad(Supplier<? extends T> supplier, T value, boolean resolved) {
		this.supplier = supplier;
		this.value = value;
		this.resolved = resolved;
	}

	/**
	 * Creates a new {@link LazyLoad} to produce an object lazily.
	 *
	 * @param <T> the type of which to produce an object of eventually.
	 * @param supplier the {@link Supplier} to create the object lazily.
	 */
	public static <T> LazyLoad<T> of(Supplier<? extends T> supplier) {
		return new LazyLoad<>(supplier);
	}

	/**
	 * Creates a new {@link LazyLoad} to return the given value.
	 *
	 * @param <T> the type of the value to return eventually.
	 * @param value the value to return.
	 * @return
	 */
	public static <T> LazyLoad<T> of(T value) {
		Asserts.notNull(value, "Value must not be null");
		return new LazyLoad<>(() -> value);
	}

	/**
	 * Creates a pre-resolved empty {@link LazyLoad}.
	 * @return
	 */
	public static <T> LazyLoad<T> empty() {
		return EMPTY;
	}

	/**
	 * Returns the value created by the configured {@link Supplier}. Will return the calculated instance for subsequent
	 * lookups.
	 */
	public T get() {
		return Asserts.notNull(getNullable(),
				"Expected lazy evaluation to yield a non-null value but got null");
	}

	/**
	 * Returns the {@link Optional} value created by the configured {@link Supplier}, allowing the absence of values in
	 * contrast to {@link #get()}. Will return the calculated instance for subsequent lookups.
	 */
	public Optional<T> getOptional() {
		return Optional.ofNullable(getNullable());
	}

	/**
	 * Returns a new LazyLoad that will consume the given supplier in case the current one does not yield in a result.
	 *
	 * @param supplier must not be {@literal null}.
	 */
	public LazyLoad<T> or(Supplier<? extends T> supplier) {
		Asserts.notNull(supplier, "Supplier must not be null");
		return LazyLoad.of(() -> orElseGet(supplier));
	}

	/**
	 * Returns a new LazyLoad that will return the given value in case the current one does not yield in a result.
	 *
	 * @param value must not be {@literal null}.
	 */
	public LazyLoad<T> or(T value) {
		Asserts.notNull(value, "Value must not be null");
		return LazyLoad.of(() -> orElse(value));
	}

	/**
	 * Returns the value of the lazy computation or the given default value in case the computation yields
	 * {@literal null}.
	 */

	public T orElse(T value) {
		T nullable = getNullable();
		return nullable == null ? value : nullable;
	}

	/**
	 * Returns the value of the lazy computation or the value produced by the given {@link Supplier} in case the original
	 * value is {@literal null}.
	 *
	 * @param supplier must not be {@literal null}.
	 */
	private T orElseGet(Supplier<? extends T> supplier) {
		Asserts.notNull(supplier, "Default value supplier must not be null");
		T value = getNullable();
		return value == null ? supplier.get() : value;
	}

	/**
	 * Creates a new {@link LazyLoad} with the given {@link Function} lazily applied to the current one.
	 *
	 * @param function must not be {@literal null}.
	 */
	public <S> LazyLoad<S> map(Function<? super T, ? extends S> function) {
		Asserts.notNull(function, "Function must not be null");
		return LazyLoad.of(() -> function.apply(get()));
	}

	/**
	 * Creates a new {@link LazyLoad} with the given {@link Function} lazily applied to the current one.
	 *
	 * @param function must not be {@literal null}.
	 */
	public <S> LazyLoad<S> flatMap(Function<? super T, LazyLoad<? extends S>> function) {
		Asserts.notNull(function, "Function must not be null");
		return LazyLoad.of(() -> function.apply(get()).get());
	}

	/**
	 * Returns the {@link String} representation of the already resolved value or the one provided through the given
	 * {@link Supplier} if the value has not been resolved yet.
	 *
	 * @param fallback must not be {@literal null}.
	 * @return will never be {@literal null}.
	 */
	public String toString(Supplier<String> fallback) {
		Asserts.notNull(fallback, "Fallback must not be null!");
		return resolved ? toString() : fallback.get();
	}

	/**
	 * Returns the value of the lazy evaluation.
	 *
	 * @return
	 * @since 2.2
	 */
	
	public T getNullable() {

		if (resolved) {
			return value;
		}

		this.value = supplier.get();
		this.resolved = true;

		return value;
	}

	@Override
	public String toString() {

		if (!resolved) {
			return UNRESOLVED;
		}

		return value == null ? "null" : value.toString();
	}

	@Override
	public boolean equals( Object o) {
		if (this == o) return true;
		else if (!(o instanceof LazyLoad<?> lazy)) return false;
		else if (resolved != lazy.resolved) return false;
		else if (!Objects.nullSafeEquals(supplier, lazy.supplier)) return false;
		else return Objects.nullSafeEquals(value, lazy.value);
	}

	@Override
	public int hashCode() {
		int result = Objects.nullSafeHashCode(supplier);
		result = 31 * result + Objects.nullSafeHashCode(value);
		result = 31 * result + (resolved ? 1 : 0);

		return result;
	}
}