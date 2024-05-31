package vn.conyeu.javafx.bindings;

import javafx.beans.property.*;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

import java.lang.ref.WeakReference;
import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class BindingManager {
    public static BindingManager instance() {return Holder.bindingManager;}
    private final WeakHashMap<ObservableValue, BindingHelper> bindings = new WeakHashMap<>();

    private static class Holder {
        private static final BindingManager bindingManager = new BindingManager();
    }

    private BindingManager() {}


    /**
     * Creates and returns a new {@link BindingBuilder} with the given target.
     */
    public <T> BindingBuilder<T> bind(ObservableValue<? extends T> target) {
        return new BindingBuilder<>(target);
    }

    /**
     * Creates a new {@link BindingBuilder} with the given target.
     * <p>
     * The difference with the above method is that it accepts a {@link Property},
     * which means that it's not necessary to specify how to update the target,
     * by default it uses {@link Property#setValue(Object)}.
     */
    public <T> BindingBuilder<T> bind(Property<T> target) {
        return new BindingBuilder<>(target).with((oldValue, newValue) -> target.setValue(newValue));
    }

    /**
     * Creates a unidirectional binding with the given arguments.
     * <p></p>
     * The {@link BindingBuilder} specifies the target, the targetUpdater and the source.
     * <p>
     * Calls {@link #biBindingCheck(ObservableValue)} on the target before proceeding.
     * <p>
     * If the target is already bound, {@link #unbind(ObservableValue)} is called on the target before
     * proceeding.
     * <p>
     * At this point puts a new entry in the bindings map with the target and the specified {@link BindingHelper}.
     * Then the actual bind happens by calling the binding helper methods.
     * <p></p>
     * If using {@link BindingBuilder#create()} it's not necessary to call this method too!!
     */
    public <T> BindingManager apply(BindingBuilder<T> bindingBuilder, BindingHelper<T> bindingHelper) {
        ObservableValue<? extends T> target = bindingBuilder.target();
        biBindingCheck(target);

        if (bindings.containsKey(target)) {
            unbind(target);
        }
        bindings.put(target, bindingHelper);
        bindingHelper.bind(target).to(bindingBuilder.source());
        return this;
    }

    /**
     * If the given target is in the map, removes it and calls {@link BindingHelper#unbind()}.
     */
    public <T> BindingManager unbind(ObservableValue<T> target) {
        BindingHelper bindingHelper = bindings.remove(target);
        if (bindingHelper != null) {
            bindingHelper.unbind();
        }
        return this;
    }

    /**
     * Retrieves the read-only property with {@link ReadOnlyBooleanWrapper#getReadOnlyProperty()} and
     * calls {@link #unbind(ObservableValue)} on it.
     */
    public BindingManager unbindReadOnly(ReadOnlyBooleanWrapper target) {
        return unbind(target.getReadOnlyProperty());
    }

    /**
     * Retrieves the read-only property with {@link ReadOnlyStringWrapper#getReadOnlyProperty()} and
     * calls {@link #unbind(ObservableValue)} on it.
     */
    public BindingManager unbindReadOnly(ReadOnlyStringWrapper target) {
        return unbind(target.getReadOnlyProperty());
    }

    /**
     * Retrieves the read-only property with {@link ReadOnlyIntegerWrapper#getReadOnlyProperty()} and
     * calls {@link #unbind(ObservableValue)} on it.
     */
    public BindingManager unbindReadOnly(ReadOnlyIntegerWrapper target) {
        return unbind(target.getReadOnlyProperty());
    }

    /**
     * Retrieves the read-only property with {@link ReadOnlyLongWrapper#getReadOnlyProperty()} and
     * calls {@link #unbind(ObservableValue)} on it.
     */
    public BindingManager unbindReadOnly(ReadOnlyLongWrapper target) {
        return unbind(target.getReadOnlyProperty());
    }

    /**
     * Retrieves the read-only property with {@link ReadOnlyFloatWrapper#getReadOnlyProperty()} and
     * calls {@link #unbind(ObservableValue)} on it.
     */
    public BindingManager unbindReadOnly(ReadOnlyFloatWrapper target) {
        return unbind(target.getReadOnlyProperty());
    }

    /**
     * Retrieves the read-only property with {@link ReadOnlyDoubleWrapper#getReadOnlyProperty()} and
     * calls {@link #unbind(ObservableValue)} on it.
     */
    public BindingManager unbindReadOnly(ReadOnlyDoubleWrapper target) {
        return unbind(target.getReadOnlyProperty());
    }

    /**
     * Retrieves the read-only property with {@link ReadOnlyObjectWrapper#getReadOnlyProperty()} and
     * calls {@link #unbind(ObservableValue)} on it.
     */
    public <T> BindingManager unbindReadOnly(ReadOnlyObjectWrapper<T> target) {
        return unbind(target.getReadOnlyProperty());
    }

    /**
     * Unbinds every property stored in the manager and then clears the bindings map.
     */
    public void dispose() {
        bindings.values().forEach(BindingHelper::unbind);
        bindings.clear();
    }

    /**
     * The number of bindings in the map.
     */
    public int size() {
        return bindings.size();
    }

    /**
     * Checks if {@link #size()} is 0;
     */
    public boolean isEmpty() {
        return size() == 0;
    }

    /**
     * Checks if there's an existing binding for the given target and the
     * associated {@link BindingHelper}'s ignoreBinding state.
     */
    public boolean isIgnoreBinding(ObservableValue target) {
        BindingHelper<?> bindingHelper = bindings.get(target);
        return bindingHelper != null && bindingHelper.isIgnoreBinding();
    }

    /**
     * Checks if the bindings map contains the given target.
     */
    public boolean isBound(ObservableValue target) {
        return bindings.containsKey(target);
    }

    /**
     * Checks if the given target is bound bidirectionally and disposes it!
     */
    private <T> void biBindingCheck(ObservableValue<? extends T> target) {
        if (BiBindingManager.instance().isBound(target)) {
            BiBindingManager.instance().disposeFor(target);
        }
    }


    /**
     * Helper class for the {@link BindingManager}.
     * <p>Makes the creation of unidirectional bindings easier with fluent methods.</p>
     */
    public static class BindingBuilder<T> {
        private final ObservableValue<? extends T> target;
        private ObservableValue<? extends T> source;
        private BiConsumer<T, T> updater;

        public BindingBuilder(ObservableValue<? extends T> target) {
            this.target = target;
        }

        /**
         * Sets the binding's source.
         */
        public BindingBuilder<T> to(ObservableValue<? extends T> source) {
            this.source = source;
            return this;
        }

        /**
         * Sets the {@link BiConsumer} function responsible for updating the target
         * when the source changes.
         */
        public BindingBuilder<T> with(BiConsumer<T, T> updater) {
            this.updater = updater;
            return this;
        }

        /**
         * @return the target observable
         */
        public ObservableValue<? extends T> target() {
            return target;
        }

        /**
         * @return the source observable
         */
        public ObservableValue<? extends T> source() {
            return source;
        }

        /**
         * @return the target updater
         */
        public BiConsumer<T, T> targetUpdater() {
            return updater;
        }

        /**
         * Confirms the creation of the binding by calling {@link BindingManager#apply(BindingBuilder, BindingHelper)}.
         */
        public BindingManager create() {
            BindingHelper<T> bindingHelper = new BindingHelper<>();
            bindingHelper.with(updater);
            return BindingManager.instance().apply(this, bindingHelper);
        }




    }


    /**
     * Base class for binding helpers.
     * <p></p>
     * Specifies common properties such as:
     * <p> - The 'target', which is the property that will be updated when the source changes
     * <p> - The action to perform to update the target (also called targetUpdater), it's a {@link BiConsumer} that offers both the oldValue and the newValue
     * <p> - A listener called 'sourceListener', which is added to the source property and triggers the {@link #updateTarget(ObservableValue, Object, Object)}
     * <p></p>
     * Specifies common behaviors such as:
     * <p> - Abstract base methods to: 'bind' the target to the source, specify the targetUpdater, allow to 'invalidate' the
     * binding and cause computations specified by the helper subclass, specify how to 'dispose' the helper and check if it's been
     * disposed before
     * <p> - The method responsible for updating the target (triggered by the sourceListener)
     * <p> - The actions to perform: before/after the target update, before/after the binding, before/after the unbinding
     *
     * @param <T>
     */
    public static abstract class AbstractBindingHelper<T> {
        protected ObservableValue<? extends T> target;
        protected BiConsumer<T, T> targetUpdater;
        protected final ChangeListener<? super T> sourceListener = this::updateTarget;

        public abstract AbstractBindingHelper<T> bind(ObservableValue<? extends T> target);

        public abstract AbstractBindingHelper<T> with(BiConsumer<T, T> targetUpdater);

        public abstract void invalidate();

        public abstract void dispose();

        public abstract boolean isDispose();


        /**
         * Invoked by the sourceListener, it's responsible for updating the specified target
         * using the specified targetUpdater {@link BiConsumer}.
         * <p>
         * Also calls {@link #beforeUpdateTarget()} and {@link #afterUpdateTarget()}.
         *
         * @param source   the source property
         * @param oldValue the source's oldValue
         * @param newValue the source's newValue
         */
        protected void updateTarget(ObservableValue<? extends T> source, T oldValue, T newValue) {
            beforeUpdateTarget();
            targetUpdater.accept(oldValue, newValue);
            afterUpdateTarget();
        }

        /**
         * Empty by default.
         */
        protected void beforeUpdateTarget() {}

        /**
         * Empty by default.
         */
        protected void afterUpdateTarget() {}

        /**
         * Empty by default.
         */
        protected void beforeBind() {}

        /**
         * Empty by default.
         */
        protected void afterBind() {}

        /**
         * Empty by default.
         */
        protected void beforeUnbind() {}

        /**
         * Empty by default.
         */
        protected void afterUnbind() {}
    }

    /**
     * Binding helper for unidirectional bindings.
     * <p></p>
     * Bindings are syntactical sugar, because basically it's a listener attached to an observable value
     * which acts as a 'source', when it changes the target is updated.
     * <p></p>
     * The issue though is that this 'syntactical sugar' mechanism is managed by JavaFX, some functionalities are
     * private, unchangeable. For example, JavaFX properties are not settable when they are bound because an internal
     * flag doesn't allow it. To replicate this behavior the only way is to override the property's 'isBound' method
     * to use the {@link BindingManager}, an example:
     * <p></p>
     * <pre>
     * {@code
     *         BindingManager bindingManager = BindingManager.instance();
     *         IntegerProperty property = new SimpleIntegerProperty() {
     *             @Override
     *             public boolean isBound() {
     *                 return bindingManager.isBound(this) && !bindingManager.isIgnoreBinding(this);
     *             }
     *         };
     *         IntegerProperty source = new SimpleIntegerProperty();
     *
     *         bindingManager.bind(property).to(source).create();
     *         source.set(8);
     * }
     * </pre>
     * <p></p>
     * The 'ignoreBinding' flag is necessary because when the updateTarget is triggered we must tell
     * the property to allow the modification because it is caused by the binding helper.
     *
     * @param <T> the properties' value type
     */
    public static class BindingHelper<T> extends AbstractBindingHelper<T> {
        private ObservableValue<? extends T> source;
        protected boolean ignoreBinding;

        /**
         * Sets the target to the specified one.
         */
        public BindingHelper<T> bind(ObservableValue<? extends T> target) {
            this.target = target;
            return this;
        }

        /**
         * Sets the targetUpdater {@link BiConsumer}.
         */
        @Override
        public BindingHelper<T> with(BiConsumer<T, T> targetUpdater) {
            this.targetUpdater = targetUpdater;
            return this;
        }

        /**
         * Sets the binding source to the given one.
         * <p>
         * Also calls {@link #beforeBind()} and {@link #afterBind()}.
         */
        public BindingHelper<T> to(ObservableValue<? extends T> source) {
            this.source = source;
            beforeBind();
            source.addListener(sourceListener);
            afterBind();
            return this;
        }

        /**
         * {@inheritDoc}
         * <p></p>
         * Sets the 'ignoreBinding' flag to true then calls the super method.
         * <p>
         * The whole process is wrapped in a try-finally block since it's as important that the flag
         * is reset at the end.
         */
        @Override
        protected void updateTarget(ObservableValue<? extends T> source, T oldValue, T newValue) {
            try {
                ignoreBinding = true;
                super.updateTarget(source, oldValue, newValue);
            } finally {
                ignoreBinding = false;
            }
        }

        /**
         * Causes the target to update with the current source's value.
         * <p></p>
         * This is necessary to 'simulate' the JavaFX's eager evaluation of bindings.
         */
        public void invalidate() {
            T value = source.getValue();
            updateTarget(source, value, value);
        }

        /**
         * Removes the sourceListener from the source, then
         * sets the source to null.
         * <p>
         * This means that the helper won't be usable anymore until {@link #to(ObservableValue)} is called again.
         * <p></p>
         * Also calls {@link #beforeUnbind()}, {@link #afterUnbind()}.
         */
        public void unbind() {
            beforeUnbind();
            source.removeListener(sourceListener);
            source = null;
            afterUnbind();
        }

        /**
         * Calls {@link #unbind()} and in addition to that
         * also the target is set to null.
         * <p>
         * This means that the helper won't be usable anymore until {@link #bind(ObservableValue)} and
         * {@link #to(ObservableValue)} are called again.
         */
        public void dispose() {
            unbind();
            target = null;
        }

        /**
         * Asks the {@link BindingManager} to check if this helper's target
         * is bound.
         */
        public boolean isBound() {
            return BindingManager.instance().isBound(target);
        }

        /**
         * Checks if the binding should be ignored.
         */
        public boolean isIgnoreBinding() {
            return ignoreBinding;
        }

        /**
         * Checks if the helper has been disposed before.
         */
        @Override
        public boolean isDispose() {
            return target == null;
        }




    }


    /**
     * A special {@link WeakHashMap} that allows to retrieve the keys ordered by insertion.
     * <p>
     * Writing a {@code LinkedWeakHashMap} would have been too much work and a very hard task.
     * For this reason this Map simply uses a {@link LinkedList} to store the keys (wrapped in {@link WeakReference}s) by
     * their insertion order.
     * <p></p>
     * Just like the {@link WeakHashMap} this list is cleared (all null references are removed) when
     * major operations occur, such as: put, putAll, combine.
     * <p></p>
     * Allows to retrieve the first and the last keys, and also a <b>copy</b> of the actual {@link LinkedList}.
     */
    public static class BindingsMap<K, V> extends WeakHashMap<K, V> {
        private LinkedList<WeakReference<K>> orderedKeys = new LinkedList<>();

        public BindingsMap() {
        }

        public BindingsMap(Map<? extends K, ? extends V> m) {
            super(m);
        }

        public BindingsMap(int initialCapacity) {
            super(initialCapacity);
        }

        public BindingsMap(int initialCapacity, float loadFactor) {
            super(initialCapacity, loadFactor);
        }

        //================================================================================
        // Methods
        //================================================================================

        /**
         * Scans the {@link LinkedList} contains the keys references and removes the null ones.
         * Also calls {@link #size()} to also trigger the {@link WeakHashMap} cleaning too.
         */
        private void clearReferences() {
            orderedKeys.removeIf(reference -> reference != null && reference.get() == null);
            size();
        }

        @SafeVarargs
        private void updateKeysList(K... keys) {
            LinkedHashSet<K> uniqueKeys = orderedKeys.stream().map(WeakReference::get)
                    .collect(Collectors.toCollection(LinkedHashSet::new));

            uniqueKeys.addAll(Arrays.asList(keys));

            orderedKeys = uniqueKeys.stream().map(WeakReference::new)
                    .collect(Collectors.toCollection(LinkedList::new));

            clearReferences();
        }

        @SafeVarargs
        private void updateKeysList(Map.Entry<K, V>... entries) {
            LinkedHashSet<K> uniqueKeys = orderedKeys.stream().map(WeakReference::get)
                    .collect(Collectors.toCollection(LinkedHashSet::new));

            uniqueKeys.addAll(Stream.of(entries).map(Map.Entry::getKey).toList());

            orderedKeys = uniqueKeys.stream().map(WeakReference::new)
                    .collect(Collectors.toCollection(LinkedList::new));

            clearReferences();
        }


        /**
         * Allows to combine the given {@code BindingsMap} to this one.
         * <p>
         * This method exists to ensure that insertion order is kept with the {@link LinkedList} but most
         * importantly ensures that there are no duplicates in the list by using a {@link LinkedHashSet}.
         */
        public void combine(BindingsMap<K, V> source) {
            LinkedHashSet<K> uniqueKeys = Stream.concat(orderedKeys.stream(),
                            source.orderedKeys.stream()).map(WeakReference::get)
                    .collect(Collectors.toCollection(LinkedHashSet::new));

            orderedKeys = uniqueKeys.stream().map(WeakReference::new)
                    .collect(LinkedList::new, LinkedList::add, LinkedList::addAll);
            clearReferences();
            super.putAll(source);
        }

        /**
         * Adds the given key to the keys {@link LinkedList}, performs {@link #clearReferences()}
         * and then calls the super method.
         */
        @Override
        public V put(K key, V value) {
            updateKeysList(key);
            return super.put(key, value);
        }

        /**
         * Overridden to call {@link #putAll(Map.Entry[])}.
         */
        @Override
        public void putAll(Map<? extends K, ? extends V> m) {
            putAll(m.entrySet().toArray(Map.Entry[]::new));
        }

        /**
         * For each entry adds the key to the keys {@link LinkedList},
         * then calls the super method.
         * At the end performs {@link #clearReferences()}.
         */
        @SafeVarargs
        public final void putAll(Map.Entry<K, V>... entries) {
            updateKeysList(entries);
            for (Map.Entry<K, V> entry : entries) {
                super.put(entry.getKey(), entry.getValue());
            }
        }

        /**
         * Removes the given key from the keys {@link LinkedList}
         * and then calls the super method.
         */
        @Override
        public V remove(Object key) {
            orderedKeys.removeIf(reference -> reference != null && reference.get() == key);
            return super.remove(key);
        }

        /**
         * Clears the keys {@link LinkedList} and then calls the super method.
         */
        @Override
        public void clear() {
            orderedKeys.clear();
            super.clear();
        }

        /**
         * UNSUPPORTED
         */
        @Override
        public void replaceAll(BiFunction<? super K, ? super V, ? extends V> function) {
            throw new UnsupportedOperationException();
        }

        /**
         * @return a copy of the {@link LinkedList} containing the Map's keys ordered by insertion
         */
        public LinkedList<WeakReference<K>> unmodifiableKeysList() {
            return new LinkedList<>(orderedKeys);
        }

        /**
         * @return the first inserted key
         */
        public K getFirstKey() {
            if (isEmpty()) return null;
            return orderedKeys.getFirst().get();
        }

        /**
         * @return the last inserted key
         */
        public K getLastKey() {
            if (isEmpty()) return null;
            return orderedKeys.getLast().get();
        }
    }







}