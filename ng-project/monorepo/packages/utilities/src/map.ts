import { Asserts } from "./asserts";
import { BiFunction, Callback } from "./functions";
import { Objects } from "./objects";
const { notNull, isNull, equals } = Objects;

export class TsMap<K extends keyof any, V> extends Map<K, V> {

    /**
    * Returns the value to which the specified key is mapped,
    * or {@code null} if this map contains no mapping for the key.
    *
    * <p>More formally, if this map contains a mapping from a key
    * {@code k} to a value {@code v} such that
    * {@code Objects.equals(key, k)},
    * then this method returns {@code v}; otherwise
    * it returns {@code null}.  (There can be at most one such mapping.)
    *
    * <p>If this map permits null values, then a return value of
    * {@code null} does not <i>necessarily</i> indicate that the map
    * contains no mapping for the key; it's also possible that the map
    * explicitly maps the key to {@code null}.  The {@link #containsKey
    * containsKey} operation may be used to distinguish these two cases.
    *
    * @param key the key whose associated value is to be returned
    * @return the value to which the specified key is mapped, or
    *         {@code null} if this map contains no mapping for the key
    */
    override get(key: K): V | undefined {
        return super.get(key);
    }

    override has(key: K): boolean {
        return super.has(key);
    }

    /**
    * Associates the specified value with the specified key in this map
    * (optional operation).  If the map previously contained a mapping for
    * the key, the old value is replaced by the specified value.  (A map
    * {@code m} is said to contain a mapping for a key {@code k} if and only
    * if {@link #containsKey(Object) m.containsKey(k)} would return
    * {@code true}.)
    *
    * @param key key with which the specified value is to be associated
    * @param value value to be associated with the specified key
    * @return the previous value associated with {@code key}, or
    *         {@code null} if there was no mapping for {@code key}.
    *         (A {@code null} return can also indicate that the map
    *         previously associated {@code null} with {@code key},
    *         if the implementation supports {@code null} values.)
    */
    override set(key: K, value: V): this {
        super.set(key, value);
        return this;
    }

    setAll(data:V[], consumer: Callback<V, K>): this {
        const self = this.clear();
        data.forEach(item => self.set(consumer(item), item))
        return this;
    }

    /**
    * Associates the specified value with the specified key in this map
    * (optional operation).  If the map previously contained a mapping for
    * the key, the old value is replaced by the specified value.  (A map
    * {@code m} is said to contain a mapping for a key {@code k} if and only
    * if {@link #containsKey(Object) m.containsKey(k)} would return
    * {@code true}.)
    *
    * @param key key with which the specified value is to be associated
    * @param value value to be associated with the specified key
    * @return the previous value associated with {@code key}, or
    *         {@code null} if there was no mapping for {@code key}.
    *         (A {@code null} return can also indicate that the map
    *         previously associated {@code null} with {@code key},
    *         if the implementation supports {@code null} values.)
    */
    put(key: K, value: V): V | undefined {
        let v = this.get(key);
        this.set(key, value);
        return v;
    }

    /**
    * Copies all of the mappings from the specified map to this map
    * (optional operation).  The effect of this call is equivalent to that
    * of calling {@link #put(Object,Object) put(k, v)} on this map once
    * for each mapping from key {@code k} to value {@code v} in the
    * specified map.  The behavior of this operation is undefined if the specified map
    * is modified while the operation is in progress. If the specified map has a defined
    * <a href="SequencedCollection.html#encounter">encounter order</a>,
    * processing of its mappings generally occurs in that order.
    *
    * @param map mappings to be stored in this map
    */
    putAll(map: Map<K, V>): this {
        map.forEach((value, key) => this.set(key, value));
        return this;
    }

    putEntries(entries: [K, V][]): this {
        entries.forEach(val => this.put(val[0], val[1]));
        return this;
    }

    /**
   * Removes the mapping for a key from this map if it is present
   * (optional operation).   More formally, if this map contains a mapping
   * from key {@code k} to value {@code v} such that
   * {@code Objects.equals(key, k)}, that mapping
   * is removed.  (The map can contain at most one such mapping.)
   *
   * <p>Returns the value to which this map previously associated the key,
   * or {@code null} if the map contained no mapping for the key.
   *
   * <p>If this map permits null values, then a return value of
   * {@code null} does not <i>necessarily</i> indicate that the map
   * contained no mapping for the key; it's also possible that the map
   * explicitly mapped the key to {@code null}.
   *
   * <p>The map will not contain a mapping for the specified key once the
   * call returns.
   *
   * @param key key whose mapping is to be removed from the map
   * @return the previous value associated with {@code key}, or
   *         {@code null} if there was no mapping for {@code key}.
   */
    override delete(key: K): boolean {
        return super.delete(key);
    }

    /**
     * Removes all of the mappings from this map (optional operation).
     * The map will be empty after this call returns.
     */
    override clear(): this {
        super.clear();
        return this;
    }

    get_size(): number {
        return super.size;
    }

    is_empty(): boolean {
        return this.size === 0;
    }

    /**
     * Returns {@code true} if this map contains a mapping for the specified
     * key.  More formally, returns {@code true} if and only if
     * this map contains a mapping for a key {@code k} such that
     * {@code Objects.equals(key, k)}.  (There can be
     * at most one such mapping.)
     *
     * @param key key whose presence in this map is to be tested
     * @return {@code true} if this map contains a mapping for the specified
     *         key
     */
    containsKey(key: K): boolean {
        return this.has(key);
    }

    /**
    * Returns {@code true} if this map maps one or more keys to the
    * specified value.  More formally, returns {@code true} if and only if
    * this map contains at least one mapping to a value {@code v} such that
    * {@code Objects.equals(value, v)}.  This operation
    * will probably require time linear in the map size for most
    * implementations of the {@code Map} interface.
    *
    * @param value value whose presence in this map is to be tested
    * @return {@code true} if this map maps one or more keys to the
    *         specified value
    */
    containsValue(value: V): boolean {
        return [...this.values()].some(v => equals(v, value));
    }

    /**
    * Returns a {@link Set} view of the keys contained in this map.
    * The set is backed by the map, so changes to the map are
    * reflected in the set, and vice-versa.  If the map is modified
    * while an iteration over the set is in progress (except through
    * the iterator's own {@code remove} operation), the results of
    * the iteration are undefined.  The set supports element removal,
    * which removes the corresponding mapping from the map, via the
    * {@code Iterator.remove}, {@code Set.remove},
    * {@code removeAll}, {@code retainAll}, and {@code clear}
    * operations.  It does not support the {@code add} or {@code addAll}
    * operations.
    *
    * @return a set view of the keys contained in this map
    */
    get_keys(): K[] {
        return [...super.keys()];
    }

    /**
     * Returns a {@link Array} view of the values contained in this map.
     * The collection is backed by the map, so changes to the map are
     * reflected in the collection, and vice-versa.  If the map is
     * modified while an iteration over the collection is in progress
     * (except through the iterators own {@code remove} operation),
     * the results of the iteration are undefined.  The collection
     * supports element removal, which removes the corresponding
     * mapping from the map, via the {@code Iterator.remove},
     * {@code Collection.remove}, {@code removeAll},
     * {@code retainAll} and {@code clear} operations.  It does not
     * support the {@code add} or {@code addAll} operations.
     *
     * @return a collection view of the values contained in this map
     */
    get_values(): V[] {
        return [...super.values()];
    }


    /**
     * Compares the specified object with this map for equality.  Returns
     * {@code true} if the given object is also a map and the two maps
     * represent the same mappings.  More formally, two maps {@code m1} and
     * {@code m2} represent the same mappings if
     * {@code m1.entrySet().equals(m2.entrySet())}.  This ensures that the
     * {@code equals} method works properly across different implementations
     * of the {@code Map} interface.
     *
     * @param object object to be compared for equality with this map
     * @return {@code true} if the specified object is equal to this map
     */
    equals(object: any): boolean {
        if (this === object) return true;
        else if (!(object instanceof TsMap)) return false;
        else if (object.size !== this.size) return false;
        else if (object.keys.length !== this.keys.length) return false;
        else return equals(this, object);
    }

    /**
     * Returns the value to which the specified key is mapped, or
     * {@code defaultValue} if this map contains no mapping for the key.
     *
     * @implSpec
     * The default implementation makes no guarantees about synchronization
     * or atomicity properties of this method. Any implementation providing
     * atomicity guarantees must override this method and document its
     * concurrency properties.
     *
     * @param key the key whose associated value is to be returned
     * @param defaultValue the default mapping of the key
     * @return the value to which the specified key is mapped, or
     * {@code defaultValue} if this map contains no mapping for the key
     */
    getOrDefault(key: K, defaultValue: V): V | undefined {
        if (this.has(key)) return this.get(key);
        else return defaultValue;
    }

    /**
  * Replaces each entry's value with the result of invoking the given
  * function on that entry until all entries have been processed or the
  * function throws an exception.  Exceptions thrown by the function are
  * relayed to the caller.
  *
  * @implSpec
  * <p>The default implementation is equivalent to, for this {@code map}:
  * <pre> {@code
  * for (Map.Entry<K, V> entry : map.entrySet())
  *     entry.setValue(function.apply(entry.getKey(), entry.getValue()));
  * }</pre>
  *
  * <p>The default implementation makes no guarantees about synchronization
  * or atomicity properties of this method. Any implementation providing
  * atomicity guarantees must override this method and document its
  * concurrency properties.
  *
  * @param callback the function to apply to each entry
  */
    replaceAll(callback: BiFunction<K, V, V>): this {
        callback = Asserts.notNull(callback);
        this.forEach((value, key) => {
            let cbValue = callback(key, value);
            this.set(key, cbValue);
        });

        return this;
    }

    /**
   * If the specified key is not already associated with a value (or is mapped
   * to {@code null}) associates it with the given value and returns
   * {@code null}, else returns the current value.
   *
   * @implSpec
   * The default implementation is equivalent to, for this {@code map}:
   *
   * <pre> {@code
   * V v = map.get(key);
   * if (v == null)
   *     v = map.put(key, value);
   *
   * return v;
   * }</pre>
   *
   * <p>The default implementation makes no guarantees about synchronization
   * or atomicity properties of this method. Any implementation providing
   * atomicity guarantees must override this method and document its
   * concurrency properties.
   *
   * @param key key with which the specified value is to be associated
   * @param value value to be associated with the specified key
   * @return the previous value associated with the specified key, or
   *         {@code null} if there was no mapping for the key.
   *         (A {@code null} return can also indicate that the map
   *         previously associated {@code null} with the key,
   *         if the implementation supports null values.)
   */
    putIfAbsent(key: K, value: V): V | undefined {
        let v = this.get(key);
        return isNull(v) ? this.put(key, value) : v;
    }

    remove(key: K): boolean ;

    /**
     * Removes the entry for the specified key only if it is currently
     * mapped to the specified value.
     *
     * @implSpec
     * The default implementation is equivalent to, for this {@code map}:
     *
     * <pre> {@code
     * if (map.containsKey(key) && Objects.equals(map.get(key), value)) {
     *     map.remove(key);
     *     return true;
     * } else
     *     return false;
     * }</pre>
     *
     * <p>The default implementation makes no guarantees about synchronization
     * or atomicity properties of this method. Any implementation providing
     * atomicity guarantees must override this method and document its
     * concurrency properties.
     *
     * @param key key with which the specified value is associated
     * @param value value expected to be associated with the specified key
     * @return {@code true} if the value was removed
     */
    remove(key: K, value: V): boolean;

    //protected
    remove(key: K, ...values: any[]): boolean {
        if(values.length === 0) return this.delete(key);
        else {
        let curValue = this.get(key);
        if (isNull(curValue) && !this.containsKey(key)) return false;
        else if (!equals(curValue, values[0])) return false;
        else {
            this.delete(key);
            return true;
        }}
    }

    /**
     * Replaces the entry for the specified key only if it is
     * currently mapped to some value.
     *
     * @implSpec
     * The default implementation is equivalent to, for this {@code map}:
     *
     * <pre> {@code
     * if (map.containsKey(key)) {
     *     return map.put(key, value);
     * } else
     *     return null;
     * }</pre>
     *
     * <p>The default implementation makes no guarantees about synchronization
     * or atomicity properties of this method. Any implementation providing
     * atomicity guarantees must override this method and document its
     * concurrency properties.
     *
     * @param key key with which the specified value is associated
     * @param value value to be associated with the specified key
     * @return the previous value associated with the specified key, or
     *         {@code null} if there was no mapping for the key.
     *         (A {@code null} return can also indicate that the map
     *         previously associated {@code null} with the key,
     *         if the implementation supports null values.)
     */
    replace(key: K, value: V): V;

    /**
     * Replaces the entry for the specified key only if currently
     * mapped to the specified value.
     *
     * @implSpec
     * The default implementation is equivalent to, for this {@code map}:
     *
     * <pre> {@code
     * if (map.containsKey(key) && Objects.equals(map.get(key), oldValue)) {
     *     map.put(key, newValue);
     *     return true;
     * } else
     *     return false;
     * }</pre>
     *
     * The default implementation does not throw NullPointerException
     * for maps that do not support null values if oldValue is null unless
     * newValue is also null.
     *
     * <p>The default implementation makes no guarantees about synchronization
     * or atomicity properties of this method. Any implementation providing
     * atomicity guarantees must override this method and document its
     * concurrency properties.
     *
     * @param key key with which the specified value is associated
     * @param oldValue value expected to be associated with the specified key
     * @param newValue value to be associated with the specified key
     * @return {@code true} if the value was replaced
     */
    replace(key: K, oldValue: V, newValue: V): boolean;

    //protected
    replace(key: K, ...values: V[]): boolean | V | undefined {
        let curValue = this.get(key);
        if (values.length === 1) {
            const value = values[0];
            const hasUp = this.containsKey(key) || notNull(curValue);
            return hasUp ? this.put(key, value) : curValue;
        }
        else if (values.length === 2) {
            const oldValue = values[0], newValue = values[1];
            if (isNull(curValue) && !this.containsKey(key)) return false;
            else if (!equals(curValue, oldValue)) return false;
            else {
                this.put(key, newValue);
                return true;
            }
        }
        else throw new Error('The method not support');
    }

    /**
    * If the specified key is not already associated with a value (or is mapped
    * to {@code null}), attempts to compute its value using the given mapping
    * function and enters it into this map unless {@code null}.
    *
    * @param key key with which the specified value is to be associated
    * @param mappingFunction the mapping function to compute a value
    * @return the current (existing or computed) value associated with
    *         the specified key, or null if the computed value is null
    */
    computeIfAbsent(key: K, mappingFunction: Callback<K, V>): V | undefined {
        mappingFunction = Asserts.notNull(mappingFunction);
        let v = this.get(key);
        if (notNull(v)) return v;
        else {
            const newValue = mappingFunction(key);
            if (isNull(newValue)) return v;
            else {
                this.put(key, newValue);
                return newValue;
            }
        }

    }

    /**
     * If the value for the specified key is present and non-null, attempts to
     * compute a new mapping given the key and its current mapped value.
     *
     * @param key key with which the specified value is to be associated
     * @param remappingFunction the remapping function to compute a value
     * @return the new value associated with the specified key, or null if none
     */
    computeIfPresent(key: K, remappingFunction: BiFunction<K, V, V>): V | undefined {
        remappingFunction = Asserts.notNull(remappingFunction);
        let oldValue = this.get(key);
        if (isNull(oldValue)) return undefined;
        else {
            let newValue = remappingFunction(key, oldValue);
            if (notNull(newValue)) {
                this.put(key, newValue);
                return newValue;
            }
            else {
                this.delete(key);
                return undefined;
            }
        }

    }

    /**
      * Attempts to compute a mapping for the specified key and its current
      * mapped value (or {@code null} if there is no current mapping).
      * @param key key with which the specified value is to be associated
      * @param remappingFunction the remapping function to compute a value
      * @return the new value associated with the specified key, or null if none
     */
    compute(key: K, remappingFunction: BiFunction<K, V, V>): V | undefined {
        remappingFunction = Asserts.notNull(remappingFunction);
        let oldValue = this.get(key);
        let newValue = remappingFunction(key, <any>oldValue);

        if (notNull(newValue)) {
            this.put(key, newValue);
            return newValue;
        }
        else if (notNull(oldValue) || this.containsKey(key)) {
            this.delete(key);
            return undefined;
        }
        else return undefined;
    }


    /**
      * If the specified key is not already associated with a value or is
      * associated with null, associates it with the given non-null value.
      * Otherwise, replaces the associated value with the results of the given
      * remapping function, or removes if the result is {@code null}. This
      * method may be of use when combining multiple mapped values for a key.
     *
      * @param key key with which the resulting value is to be associated
      * @param value the non-null value to be merged with the existing value
      *        associated with the key or, if no existing value or a null value
      *        is associated with the key, to be associated with the key
      * @param remappingFunction the remapping function to recompute a value if
      *        present
      * @return the new value associated with the specified key, or null if no
      *         value is associated with the key
      */
    merge(key: K, value: V, remappingFunction: BiFunction<V, V, V>): V {
        remappingFunction = Asserts.notNull(remappingFunction);
        value = Asserts.notNull(value);

        let oldValue = this.get(key);
        let newValue = isNull(oldValue) ? value : remappingFunction(oldValue, value);
        if (isNull(newValue)) this.delete(key);
        else this.put(key, newValue);
        return newValue;
    }

}