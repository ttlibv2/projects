import { Asserts } from "./asserts";
import { NoSuchElement, NullPointer } from "./errors";
import { Callback, Consumer, Predicate, Runnable, Supplier } from "./function";
import { Objects } from "./objects";
const {notNull, isNull} = Objects;

export class Optional<E> {
    private static readonly EMPTY = new Optional(null);

    private readonly value: E;

    constructor(value: E) {
        this.value = value;
    }

    /**
     * Returns an empty {@code Optional} instance.  No value is present for this
     * {@code Optional}.
     *
     * @apiNote
     * Though it may be tempting to do so, avoid testing if an object is empty
     * by comparing with {@code ==} or {@code !=} against instances returned by
     * {@code Optional.empty()}.  There is no guarantee that it is a singleton.
     * Instead, use {@link #isEmpty()} or {@link #isPresent()}.
     *
     * @param <E> The type of the non-existent value
     * @return an empty {@code Optional}
     */
    static empty<E>(): Optional<E> {
        return Optional.EMPTY;
    }

    /**
    * Returns an {@code Optional} describing the given non-{@code null}
    * value.
    *
    * @param value the value to describe, which must be non-{@code null}
    * @param <E> the type of the value
    * @return an {@code Optional} with the value present
    * @throws Error if value is {@code null}
    */
    static of<E>(value: E): Optional<E> {
        if (isNull(value)) throw new NullPointer();
        else return new Optional(value);
    }

    /**
     * Returns an {@code Optional} describing the given value, if
     * non-{@code null}, otherwise returns an empty {@code Optional}.
     *
     * @param value the possibly-{@code null} value to describe
     * @param <T> the type of the value
     * @return an {@code Optional} with a present value if the specified value
     *         is non-{@code null}, otherwise an empty {@code Optional}
     */
    static ofNullable<E>(value: E): Optional<E> {
        if (isNull(value)) return Optional.EMPTY;
        else return new Optional(value);
    }

    /**
     * If a value is present, returns the value, otherwise throws
     * {@code NoSuchElementException}.
     *
     * @apiNote
     * The preferred alternative to this method is {@link #orElseThrow()}.
     *
     * @return the non-{@code null} value described by this {@code Optional}
     * @throws NoSuchElementException if no value is present
     */
    get(): E {
        if (isNull(this.value)) throw new NoSuchElement(`No value present`);
        else return this.value;
    }

    /**
    * If a value is present, returns {@code true}, otherwise {@code false}.
    *
    * @return {@code true} if a value is present, otherwise {@code false}
    */
    isPresent(): boolean {
        return notNull(this.value);
    }

    /**
 * If a value is  not present, returns {@code true}, otherwise
 * {@code false}.
 *
 * @return  {@code true} if a value is not present, otherwise {@code false}
 */
    isEmpty(): boolean {
        return isNull(this.value);
    }

    /**
    * If a value is present, performs the given action with the value,
    * otherwise does nothing.
    *
    * @param action the action to be performed, if a value is present
    * @throws NullPointer if value is present and the given action is
    *         {@code null}
    */
    ifPresent(action: Consumer<E>): void {
        if (notNull(this.value)) action(this.value);
    }

    /**
   * If a value is present, performs the given action with the value,
   * otherwise performs the given empty-based action.
   *
   * @param action the action to be performed, if a value is present
   * @param emptyAction the empty-based action to be performed, if no value is
   *        present
   * @throws NullPointer if a value is present and the given action
   *         is {@code null}, or no value is present and the given empty-based
   *         action is {@code null}.
   */
    ifPresentOrElse(action: Consumer<E>, emptyAction: Runnable): void {
        if (notNull(this.value)) action(this.value);
        else emptyAction();
    }

    /**
  * If a value is present, and the value matches the given predicate,
  * returns an {@code Optional} describing the value, otherwise returns an
  * empty {@code Optional}.
  *
  * @param predicate the predicate to apply to a value, if present
  * @return an {@code Optional} describing the value of this
  *         {@code Optional}, if a value is present and the value matches the
  *         given predicate, otherwise an empty {@code Optional}
  * @throws NullPointer if the predicate is {@code null}
  */
    filter(predicate: Predicate<E>): Optional<E> {
        predicate = Asserts.notNull(predicate);
        if (this.isEmpty()) return this;
        else return predicate(this.value) ? this : Optional.EMPTY;
    }

    /**
     * If a value is present, returns an {@code Optional} describing (as if by
     * {@link #ofNullable}) the result of applying the given mapping function to
     * the value, otherwise returns an empty {@code Optional}.
     *
     * <p>If the mapping function returns a {@code null} result then this method
     * returns an empty {@code Optional}.
     *
     * @apiNote
     * This method supports post-processing on {@code Optional} values, without
     * the need to explicitly check for a return status.  For example, the
     * following code traverses a stream of URIs, selects one that has not
     * yet been processed, and creates a path from that URI, returning
     * an {@code Optional<Path>}:
     *
     * <pre>{@code
     *     Optional<Path> p =
     *         uris.stream().filter(uri -> !isProcessedYet(uri))
     *                       .findFirst()
     *                       .map(Paths::get);
     * }</pre>
     *
     * Here, {@code findFirst} returns an {@code Optional<URI>}, and then
     * {@code map} returns an {@code Optional<Path>} for the desired
     * URI if one exists.
     *
     * @param mapper the mapping function to apply to a value, if present
     * @param <U> The type of the value returned from the mapping function
     * @return an {@code Optional} describing the result of applying a mapping
     *         function to the value of this {@code Optional}, if a value is
     *         present, otherwise an empty {@code Optional}
     * @throws NullPointer if the mapping function is {@code null}
     */
    map<U>(mapper: Callback<E, U>): Optional<U> {
        mapper = Asserts.notNull(mapper);
        if (this.isEmpty()) return Optional.EMPTY;
        else {
            const mapperVal = mapper(this.value);
            return Optional.ofNullable(mapperVal);
        }

    }

    /**
    * If a value is present, returns the result of applying the given
    * {@code Optional}-bearing mapping function to the value, otherwise returns
    * an empty {@code Optional}.
    *
    * <p>This method is similar to {@link #map(Function)}, but the mapping
    * function is one whose result is already an {@code Optional}, and if
    * invoked, {@code flatMap} does not wrap it within an additional
    * {@code Optional}.
    *
    * @param <U> The type of value of the {@code Optional} returned by the
    *            mapping function
    * @param mapper the mapping function to apply to a value, if present
    * @return the result of applying an {@code Optional}-bearing mapping
    *         function to the value of this {@code Optional}, if a value is
    *         present, otherwise an empty {@code Optional}
    * @throws NullPointer if the mapping function is {@code null} or
    *         returns a {@code null} result
    */
    flatMap<U>(mapper: Callback<E, Optional<U>>): Optional<U> {
        mapper = Asserts.notNull(mapper);
        if (this.isEmpty()) return Optional.EMPTY;
        else {
            const mapperVal = mapper(this.value);
            return Asserts.notNull(mapperVal);
        }
    }

     /**
     * If a value is present, returns an {@code Optional} describing the value,
     * otherwise returns an {@code Optional} produced by the supplying function.
     *
     * @param supplier the supplying function that produces an {@code Optional}
     *        to be returned
     * @return returns an {@code Optional} describing the value of this
     *         {@code Optional}, if a value is present, otherwise an
     *         {@code Optional} produced by the supplying function.
     * @throws NullPointer if the supplying function is {@code null} or
     *         produces a {@code null} result
     */
    or(supplier: Supplier<Optional<E>>): Optional<E> {
        supplier = Asserts.notNull(supplier);
        if(this.isPresent()) return this;
        else return Asserts.notNull(supplier());
    }

     /**
     * If a value is present, returns the value, otherwise returns
     * {@code other}.
     *
     * @param other the value to be returned, if no value is present.
     *        May be {@code null}.
     * @return the value, if present, otherwise {@code other}
     */
    orElse(other: E): E {
        return notNull(this.value) ? this.value : other;
    }

    /**
     * If a value is present, returns the value, otherwise returns the result
     * produced by the supplying function.
     *
     * @param supplier the supplying function that produces a value to be returned
     * @return the value, if present, otherwise the result produced by the
     *         supplying function
     * @throws NullPointer if no value is present and the supplying
     *         function is {@code null}
     */
    orElseGet(supplier: Supplier<E>):E {
        return notNull(this.value) ? this.value : supplier();
    }

     /**
     * If a value is present, returns the value, otherwise throws
     * {@code NoSuchElementException}.
     *
     * @return the non-{@code null} value described by this {@code Optional}
     * @throws NoSuchElement if no value is present
     */
    orElseThrow<X extends Error>(exceptionSupplier?: Supplier<X>): E {
        if(notNull(this.value) ) return this.value;
        else if(isNull(exceptionSupplier)) throw new NoSuchElement('No value present');
        else throw exceptionSupplier();
    }

    equals(object: any) {
        if(this === object) return true;
        else return object instanceof Optional && object.value === this.value;
    }














}