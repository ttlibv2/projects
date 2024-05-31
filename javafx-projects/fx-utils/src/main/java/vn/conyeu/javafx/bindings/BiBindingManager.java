package vn.conyeu.javafx.bindings;

import javafx.beans.property.*;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

import java.lang.ref.WeakReference;
import java.util.LinkedList;
import java.util.Map;
import java.util.Objects;
import java.util.WeakHashMap;
import java.util.function.BiConsumer;

/**
 * This singleton class manages bidirectional bindings.
 * <p></p>
 * All bindings are stored in a map which associates the target property to a {@link BiBindingHelper}.
 * For bidirectional bindings a given target property will always have one and only helper at any time.
 * The sources list is managed by that manager, when a new bind is made on the same target, a new {@link BiBindingHelper}
 * is created, which is the combination of the old one and the new one, {@link BiBindingHelper#newFor(BiBindingHelper, BiBindingHelper, boolean)} is used.
 * <p></p>
 * When a binding is not necessary anymore it's opportune to dispose it either with {@link #dispose()} or
 * {@link #unbind(ObservableValue, ObservableValue)} or {@link #clear(ObservableValue)}.
 * <p></p>
 * This mechanism is so flexible that it also allows to bind read-only properties on the sole condition that
 * you must have a way to set that property and it must be specified with {@link BiBindingBuilder}.
 * <p>
 * For these properties be careful during the unbind as in JavaFX the getReadOnlyProperty() method returns
 * a completely different instance. For this reason the manager also offers unbind methods to unbind read-only wrappers.
 * <p></p>
 * To make the bindings building more readable it uses {@link BiBindingBuilder}.
 */
public class BiBindingManager {

    private static class Holder {
        private static final BiBindingManager biBindingManager = new BiBindingManager();
    }

    /**
     * Retrieves the instance of the BiBindingManager
     */
    public static BiBindingManager instance() {
        return Holder.biBindingManager;
    }

    //================================================================================
    // Properties
    //================================================================================
    private final WeakHashMap<ObservableValue, BiBindingHelper> bindings = new WeakHashMap<>();

    //================================================================================
    // Constructors
    //================================================================================
    private BiBindingManager() {
    }

    //================================================================================
    // Methods
    //================================================================================

    /**
     * Creates and returns a new {@link BiBindingBuilder} with the given target.
     */
    public <T> BiBindingBuilder<T> bindBidirectional(ObservableValue<? extends T> target) {
        return new BiBindingBuilder<>(target);
    }

    /**
     * Creates and returns a new {@link BiBindingBuilder} with the given target. Since it is a {@link Property}
     * a default targetUpdater is also set by using {@link Property#setValue(Object)}.
     */
    public <T> BiBindingBuilder<T> bindBidirectional(Property<T> target) {
        return new BiBindingBuilder<>(target).with((oldValue, newValue) -> target.setValue(newValue));
    }

    /**
     * Creates a bidirectional binding with the given arguments.
     * <p></p>
     * The {@link BindingManager.BindingBuilder} specifies the target, the targetUpdater and the source.
     * <p>
     * Calls {@link #bindingCheck(ObservableValue)} on the target before proceeding.
     * <p></p>
     * The {@link BiBindingHelper} to use must be computed properly as there are several cases:
     * <p> - The target is already bound to something: in this case a new helper is created by combining the
     * existing one and the given one using {@link BiBindingHelper#newFor(BiBindingHelper, BiBindingHelper, boolean)}
     * <p> - The given helper is null or equal to the existing one: in this case the existing one is used
     * and all the sources of the given one are added to the existing one
     * <p> The target is not already bound: if the given helper is null a new one is created
     * otherwise the given one is used.
     * <p></p>
     * If the given {@link BiBindingBuilder}'s eager evaluation flag is true (by default it is)
     * {@link BiBindingHelper#invalidate()} is called on the chosen helper.
     * <p></p>
     * At the end the entry, target-helper, in put into the map.
     * <p></p>
     * If using {@link BiBindingBuilder#create()} it's not necessary to call this method too!!
     */
    public <T> BiBindingManager apply(BiBindingBuilder<T> bindingBuilder, BiBindingHelper<T> bindingHelper, boolean override) {
        ObservableValue<? extends T> target = bindingBuilder.target();
        bindingCheck(target);

        BiBindingHelper<T> helper;
        if (bindings.containsKey(target)) {
            BiBindingHelper<T> existing = bindings.get(target);
            if (bindingHelper != null && existing != bindingHelper) {
                helper = BiBindingHelper.newFor(existing, bindingHelper, override);
                helper.bind(bindingBuilder.target());
            } else {
                helper = existing;
                helper.addSources(bindingBuilder.getSources());
            }
        } else {
            helper = Objects.requireNonNullElseGet(bindingHelper, BiBindingHelper::new);
            helper.bind(target).with(bindingBuilder.targetUpdater()).addSources(bindingBuilder.getSources());
        }

        if (bindingBuilder.isEagerEvaluation()) {
            helper.invalidate();
        }
        bindings.put(bindingBuilder.target(), helper);

        return this;
    }

    /**
     * Removes the given source from the given target's binding.
     * <p></p>
     * If by removing the source, the helper becomes empty, the target is removed
     * from the bindings map.
     */
    public <T> BiBindingManager unbind(ObservableValue<? extends T> target, ObservableValue<? extends T> source) {
        BiBindingHelper<T> bindingHelper = bindings.get(target);
        if (bindingHelper != null) {
            bindingHelper.unbind(source);
            if (bindingHelper.size() == 0) {
                bindings.remove(target);
            }
        }
        return this;
    }

    /**
     * Calls {@link BiBindingHelper#clear()} for the given target (if existing in tha map).
     */
    public <T> BiBindingManager clear(ObservableValue<? extends T> target) {
        BiBindingHelper<T> bindingHelper = bindings.get(target);
        if (bindingHelper != null) {
            bindingHelper.clear();
            bindings.remove(target);
        }
        return this;
    }

    /**
     * Calls {@link BiBindingHelper#dispose()} for the given target (if existing in tha map).
     */
    public <T> BiBindingManager disposeFor(ObservableValue<? extends T> target) {
        BiBindingHelper<T> bindingHelper = bindings.get(target);
        if (bindingHelper != null) {
            bindingHelper.dispose();
            bindings.remove(target);
        }
        return this;
    }

    /**
     * Disposes all the bindings and clears the map.
     */
    public BiBindingManager dispose() {
        bindings.forEach((observable, helper) -> helper.dispose());
        bindings.clear();
        return this;
    }

    /**
     * Retrieves the read-only property with {@link ReadOnlyBooleanWrapper#getReadOnlyProperty()} and
     * calls {@link #unbind(ObservableValue, ObservableValue)} on it.
     */
    public BiBindingManager unbindReadOnly(ReadOnlyBooleanWrapper target, ObservableValue<? extends Boolean> source) {
        return unbind(target.getReadOnlyProperty(), source);
    }

    /**
     * Retrieves the read-only property with {@link ReadOnlyStringWrapper#getReadOnlyProperty()} and
     * calls {@link #unbind(ObservableValue, ObservableValue)} on it.
     */
    public BiBindingManager unbindReadOnly(ReadOnlyStringWrapper target, ObservableValue<? extends String> source) {
        return unbind(target.getReadOnlyProperty(), source);
    }

    /**
     * Retrieves the read-only property with {@link ReadOnlyIntegerWrapper#getReadOnlyProperty()} and
     * calls {@link #unbind(ObservableValue, ObservableValue)} on it.
     */
    public BiBindingManager unbindReadOnly(ReadOnlyIntegerWrapper target, ObservableValue<? extends Integer> source) {
        return unbind(target.getReadOnlyProperty(), source);
    }

    /**
     * Retrieves the read-only property with {@link ReadOnlyLongWrapper#getReadOnlyProperty()} and
     * calls {@link #unbind(ObservableValue, ObservableValue)} on it.
     */
    public BiBindingManager unbindReadOnly(ReadOnlyLongWrapper target, ObservableValue<? extends Long> source) {
        return unbind(target.getReadOnlyProperty(), source);
    }

    /**
     * Retrieves the read-only property with {@link ReadOnlyFloatWrapper#getReadOnlyProperty()} and
     * calls {@link #unbind(ObservableValue, ObservableValue)} on it.
     */
    public BiBindingManager unbindReadOnly(ReadOnlyFloatWrapper target, ObservableValue<? extends Float> source) {
        return unbind(target.getReadOnlyProperty(), source);
    }

    /**
     * Retrieves the read-only property with {@link ReadOnlyDoubleWrapper#getReadOnlyProperty()} and
     * calls {@link #unbind(ObservableValue, ObservableValue)} on it.
     */
    public BiBindingManager unbindReadOnly(ReadOnlyDoubleWrapper target, ObservableValue<? extends Double> source) {
        return unbind(target.getReadOnlyProperty(), source);
    }

    /**
     * Retrieves the read-only property with {@link ReadOnlyObjectWrapper#getReadOnlyProperty()} and
     * calls {@link #unbind(ObservableValue, ObservableValue)} on it.
     */
    public <T> BiBindingManager unbindReadOnly(ReadOnlyObjectWrapper<T> target, ObservableValue<? extends T> source) {
        return unbind(target.getReadOnlyProperty(), source);
    }

    /**
     * Checks if the bindings map contains the given target.
     */
    public <T> boolean isBound(ObservableValue<? extends T> target) {
        return bindings.containsKey(target);
    }

    /**
     * @return the number of bindings actually present in the map
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
     * Checks if the given target is bound unidirectionally and unbinds it!
     */
    private <T> void bindingCheck(ObservableValue<? extends T> target) {
        if (BindingManager.instance().isBound(target)) {
            BindingManager.instance().unbind(target);
        }
    }

    /**
     * Binding helper for bidirectional bindings.
     * <p></p>
     * Bidirectional bindings are syntactical sugar, because basically it's a listener attached to one or more observable values
     * which acts as 'sources', when one of them change the target is updated and all the other sources are updated.
     * <p></p>
     * There is one issue though: when a source changes and the value is updated, there's a 'bounce' effect
     * because all the other sources are updated causing the updateTarget to trigger every time. The same unwanted effect
     * occurs when the target changes, as the sources update will then trigger again the updateTarget.
     * To avoid this, and improve performance, two boolean flags are used to stop the listeners from
     * proceeding with useless updates.
     * <p></p>
     * There's also another issue. In JavaFX when you bind the target multiple times
     * (so you have multiple sources) the properties will have the value of the last used source.
     * This helper though, stores the sources in a Map, which as you know it's not ordered. I could have used
     * a LinkedHashMap, but I wanted to use a WeakHashMap to avoid memory leaks (I hope). Writing a
     * WeakLinkedHashMap would have been too much work, and also very complicated, so in the end I created the {@link BindingManager.BindingsMap},
     * which allows to retrieve the sources in order of insertion.
     * <p>
     * The Map associates the source observable with the {@link BiConsumer} responsible for updating it.
     *
     * @param <T> the properties' value type
     */
    public static class BiBindingHelper<T> extends BindingManager.AbstractBindingHelper<T> {
        protected boolean fromSource;
        protected boolean fromTarget;
        private final BindingManager.BindingsMap<ObservableValue<? extends T>, BiConsumer<T, T>> sources = new BindingManager.BindingsMap<>();
        private final ChangeListener<? super T> targetListener = (observable, oldValue, newValue) -> updateSources(oldValue, newValue);


        /**
         * Sets the target to the specified one, and adds the targetListener to it.
         */
        @Override
        public BiBindingHelper<T> bind(ObservableValue<? extends T> target) {
            this.target = target;
            target.addListener(targetListener);
            return this;
        }

        /**
         * Sets the targetUpdater {@link BiConsumer}.
         */
        @Override
        public BiBindingHelper<T> with(BiConsumer<T, T> targetUpdater) {
            this.targetUpdater = targetUpdater;
            return this;
        }

        /**
         * Adds the given source and {@link BiConsumer}cto the sources map.
         * <p></p>
         * Also calls {@link #beforeBind()} and {@link #afterBind()}.
         *
         * @param source  the source observable
         * @param updater the {@link BiConsumer} responsible for updating the source when the target changes
         */
        public BiBindingHelper<T> addSource(ObservableValue<? extends T> source, BiConsumer<T, T> updater) {
            sources.put(source, updater);
            beforeBind();
            source.addListener(sourceListener);
            afterBind();
            return this;
        }

        /**
         * Adds all the given entries (as a Map) to this helper's sources map.
         * <p></p>
         * To ensure that the insertion order is maintained, {@link BindingManager.BindingsMap#combine(BindingManager.BindingsMap)} is used.
         * <p></p>
         * Also calls {@link #beforeBind()} and {@link #afterBind()}.
         */
        public BiBindingHelper<T> addSources(BindingManager.BindingsMap<ObservableValue<? extends T>, BiConsumer<T, T>> sources) {
            beforeBind();
            sources.keySet().forEach(observable -> observable.addListener(sourceListener));
            this.sources.combine(sources);
            afterBind();
            return this;
        }

        /**
         * Invoked by the targetListener, it's responsible for updating the sources by calling {@link #updateSource(ObservableValue, BiConsumer, Object, Object)}.
         * Also calls beforeUpdateSources() and afterUpdateSources()
         * <p></p>
         * If the method is triggered by {@link #updateTarget(ObservableValue, Object, Object)}, so
         * the fromTarget flag is true, exits immediately.
         * <p></p>
         * Sets the fromSource flag to true then updates the sources.
         * The whole process is wrapped in a try-finally block as it's super important that the flag
         * is reset at the end.
         */
        protected void updateSources(T oldValue, T newValue) {
            if (isFromTarget()) return;

            try {
                fromSource = true;
                beforeUpdateSources();
                sources.forEach((source, updater) -> updateSource(source, updater, oldValue, newValue));
                afterUpdateSources();
            } finally {
                fromSource = false;
            }
        }

        /**
         * Updates the given source using the given {@link BiConsumer}
         */
        protected void updateSource(ObservableValue<? extends T> source, BiConsumer<T, T> updater, T oldValue, T newValue) {
            updater.accept(oldValue, newValue);
        }

        /**
         * {@inheritDoc}
         * <p></p>
         * If the method is triggered by {@link #updateSources(Object, Object)}, so
         * the fromSource flag is true, exits immediately.
         * <p></p>
         * Sets the fromTarget flag to true then updates the target.
         * Sets the fromSource flag to true as now it's needed to also update
         * all the other sources (except for the updatingSource, the one that triggered the target update).
         * <p></p>
         * Also calls beforeUpdateTarget() and afterUpdateTarget()
         * <p></p>
         * The whole process is wrapped in a try-finally block as it's super important to reset
         * both the flags at the end.
         *
         * @param updatingSource the source that triggered the target update
         * @param oldValue       the source's oldValue
         * @param newValue       the source's newValue
         */
        @Override
        protected void updateTarget(ObservableValue<? extends T> updatingSource, T oldValue, T newValue) {
            if (isFromSource()) return;

            try {
                fromTarget = true;
                beforeUpdateTarget();
                targetUpdater.accept(oldValue, newValue);

                fromSource = true;
                sources.forEach((source, updater) -> {
                    if (source == updatingSource) return;
                    updateSource(source, updater, oldValue, newValue);
                });

                afterUpdateTarget();
            } finally {
                fromTarget = false;
                fromSource = false;
            }
        }

        /**
         * Causes the target to update with the last source's value.
         * <p>
         * The last source is retrieved using {@link BindingManager.BindingsMap#getLastKey()}.
         * <p></p>
         * This is necessary to 'simulate' the JavaFX's eager evaluation of bindings.
         */
        @Override
        public void invalidate() {
            ObservableValue<? extends T> lastSource = sources.getLastKey();
            if (lastSource != null) {
                T value = lastSource.getValue();
                targetUpdater.accept(value, value);
            }
        }

        /**
         * Removes the given source from the sources map and
         * also removes the sourceListener from it.
         * <p></p>
         * Also calls {@link #beforeUnbind()}, {@link #afterUnbind()}.
         */
        public void unbind(ObservableValue<? extends T> source) {
            if (sources.remove(source) != null) {
                beforeUnbind();
                source.removeListener(sourceListener);
                afterUnbind();
            }
        }

        /**
         * Detaches the sourceListener from all the sources then clears the sources map.
         * <p>
         * This means that the helper won't be usable anymore until {@link #addSource(ObservableValue, BiConsumer)} or
         * {@link #addSources(BindingManager.BindingsMap)} are called again.
         */
        public void clear() {
            sources.forEach((observable, updater) -> observable.removeListener(sourceListener));
            sources.clear();
        }

        /**
         * Calls {@link #clear()} and in addition to that
         * also the target is set to null (and the targetListener removed too).
         * <p>
         * This means that the helper won't be usable anymore until {@link #bind(ObservableValue)} and
         * {@link #addSource(ObservableValue, BiConsumer)} or {@link #addSources(BindingManager.BindingsMap)} are called again.
         */
        @Override
        public void dispose() {
            clear();
            target.removeListener(targetListener);
            target = null;
        }

        /**
         * Empty by default.
         */
        protected void beforeUpdateSources() {}

        /**
         * Empty by default.
         */
        protected void afterUpdateSources() {}

        /**
         * @return whether the updated is triggered by the updateTarget
         */
        public boolean isFromTarget() {
            return fromTarget;
        }

        /**
         * @return whether the update is triggered by the updateSources
         */
        public boolean isFromSource() {
            return fromSource;
        }

        /**
         * Checks if the helper has been disposed before.
         */
        @Override
        public boolean isDispose() {
            return target == null;
        }

        /**
         * @return the number of sources in the map.
         */
        public int size() {
            return sources.size();
        }

        /**
         * @return the sources map
         */
        protected BindingManager.BindingsMap<ObservableValue<? extends T>, BiConsumer<T, T>> getSources() {
            return sources;
        }

        /**
         * @return an unmodifiable view of the map's keys stored in a {@link LinkedList}
         * (to keep track of insertion order), and wrapped in {@link WeakReference}s.
         */
        public LinkedList<WeakReference<ObservableValue<? extends T>>> getUnmodifiableSources() {
            return sources.unmodifiableKeysList();
        }


        /**
         * Creates a new {@code BiBindingHelper} from the two given ones.
         * <p></p>
         * Note that at the end of the process both the helpers are disposed.
         *
         * @param first                 the first helper
         * @param second                the second helper
         * @param overrideTargetUpdater a flag to specify if the targetUpdater of the second helper must be used instead
         */
        public static <T> BiBindingHelper<T> newFor(BiBindingHelper<T> first, BiBindingHelper<T> second, boolean overrideTargetUpdater) {
            BiBindingHelper<T> newHelper = new BiBindingHelper<>();
            newHelper.bind(second.target);
            if (overrideTargetUpdater) {
                newHelper.with(second.targetUpdater);
            } else {
                newHelper.with(first.targetUpdater);
            }

            first.sources.forEach((source, updater) -> source.removeListener(first.sourceListener));
            second.sources.forEach((source, updater) -> source.removeListener(second.sourceListener));

            newHelper.addSources(first.getSources());
            newHelper.addSources(second.getSources());

            first.sources.clear();
            second.sources.clear();

            first.target.removeListener(first.targetListener);
            first.target = null;

            second.target.removeListener(second.targetListener);
            second.target = null;

            return newHelper;
        }

    }

    /**
     * Helper class for {@link BiBindingManager}.
     * <p>
     * Makes the creation of bidirectional bindings easier with fluent methods.
     */
    public static class BiBindingBuilder<T> {

        private final ObservableValue<? extends T> target;
        private BiConsumer<T, T> targetUpdater;
        private final BindingManager.BindingsMap<ObservableValue<? extends T>, BiConsumer<T, T>> sources = new BindingManager.BindingsMap<>();
        private BiBindingHelper<T> bindingHelper;
        private boolean eagerEvaluation = true;
        private boolean override = false;

        public BiBindingBuilder(ObservableValue<? extends T> target) {
            this.target = target;
        }

        /**
         * Sets the {@link BiConsumer} function responsible for updating the target
         * when the source changes.
         */
        public BiBindingBuilder<T> with(BiConsumer<T, T> targetUpdater) {
            this.targetUpdater = targetUpdater;
            return this;
        }

        /**
         * Specifies the {@link BiBindingHelper} that will be used for the binding.
         * <p>
         * This is optional as a default helper will be created when needed.
         */
        public BiBindingBuilder<T> withHelper(BiBindingHelper<T> bindingHelper) {
            this.bindingHelper = bindingHelper;
            return this;
        }

        /**
         * Calls {@link #to(ObservableValue, BiConsumer)} with the given property.
         * Since it is a {@link Property} the {@link BiConsumer} will make use of {@link Property#setValue(Object)}.
         */
        public BiBindingBuilder<T> to(Property<T> source) {
            return to(source, (oldValue, newValue) -> source.setValue(newValue));
        }

        /**
         * Adds a new entry in the map which associates the given source to the given
         * {@link BiConsumer} that is responsible for updating the source when needed.
         */
        public BiBindingBuilder<T> to(ObservableValue<T> source, BiConsumer<T, T> sourceUpdater) {
            this.sources.put(source, sourceUpdater);
            return this;
        }

        /**
         * Allows adding multiple entries to the map at once.
         * Basically the same as {@link #to(ObservableValue, BiConsumer)}.
         */
        @SafeVarargs
        public final BiBindingBuilder<T> to(Map.Entry<ObservableValue<? extends T>, BiConsumer<T, T>>... sources) {
            this.sources.putAll(sources);
            return this;
        }

        /**
         * Disables the binding eager evaluation mechanism.
         * At the time of the binding the values won't be updated automatically.
         */
        public BiBindingBuilder<T> lazy() {
            this.eagerEvaluation = false;
            return this;
        }

        /**
         * If a binding for the specified target is already present specifies
         * if this builder's targetUpdater must replace the existing one.
         */
        public BiBindingBuilder<T> override(boolean override) {
            this.override = override;
            return this;
        }

        /**
         * @return the target observable
         */
        public ObservableValue<? extends T> target() {
            return target;
        }

        /**
         * @return the target updater
         */
        public BiConsumer<T, T> targetUpdater() {
            return targetUpdater;
        }

        /**
         * @return the map containing the sources and their updater
         */
        public BindingManager.BindingsMap<ObservableValue<? extends T>, BiConsumer<T, T>> getSources() {
            return sources;
        }

        /**
         * @return if the bindings uses eager evaluation or it's lazy
         */
        public boolean isEagerEvaluation() {
            return eagerEvaluation;
        }

        /**
         * Confirms the creation of the binding by calling {@link BiBindingManager#apply(BiBindingBuilder, BiBindingHelper, boolean)}.
         */
        public BiBindingManager create() {
            return instance().apply(this, bindingHelper, override);
        }
    }
}