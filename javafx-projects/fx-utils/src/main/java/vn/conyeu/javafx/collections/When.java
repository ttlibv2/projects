package vn.conyeu.javafx.collections;

import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import vn.conyeu.commons.function.TriConsumer;
import java.lang.ref.WeakReference;
import java.util.HashSet;
import java.util.Set;
import java.util.WeakHashMap;
import java.util.function.*;

/**
 * Useful class to listen to changes for a given {@link ObservableValue} and perform any
 * specified action when it changes.
 * <p>
 * You can read this construct as "When condition changes, then do this"
 * <p>
 * This is just an abstract class that defines common properties and behavior, but it has two concrete
 * implementations, {@link OnChanged} and {@link OnInvalidated}.
 * <p>
 * This construct also allows to define one-shot listeners, meaning that the
 * above phrase changes like this: "When condition changes, then do this, then dispose(remove listener)"
 * <p></p>
 * <b>Note:</b>Once the construct is not needed anymore it's highly recommended to dispose it
 * using {@link #disposeFor(ObservableValue)} to avoid memory leaks.
 * When constructs and ObservableValues are stored in a {@link WeakHashMap} for this purpose.
 */
public abstract class When<T> {
    protected static final WeakHashMap<ObservableValue<?>, WeakReference<When<?>>> whens = new WeakHashMap<>();
    protected final ObservableValue<T> observableValue;
    protected final Set<Observable> invalidatingObservables;
    protected InvalidationListener invalidationListener;
    protected boolean oneShot = false;

    protected When(ObservableValue<T> observableValue) {
        this.observableValue = observableValue;

        this.invalidatingObservables = new HashSet<>();
        this.invalidationListener = o -> invalidate();
    }

    public abstract When<T> listen();

    /**
     * This is responsible for registering the {@code When} construct in a map that
     * keep references to all the built constructs. This is to avoid garbage collection and to
     * handle {@code When}s disposal easily.
     * <p></p>
     * This should be called by implementations of {@link #listen()}.
     */
    protected final void register() {
        if (whens.containsKey(observableValue))
            throw new IllegalArgumentException("Cannot register this When construct as the given observable is already being observed");
        whens.put(observableValue, new WeakReference<>(this));
    }

    /**
     * Adds an {@link Observable} to listen to, when it changes it will cause the invalidation of this construct
     * by calling {@link #invalidate()}.
     */
    public When<T> invalidating(Observable obs) {
        invalidatingObservables.add(obs);
        return this;
    }

    /**
     * The default implementation does nothing.
     */
    protected When<T> invalidate() {
        return this;
    }

    /**
     * Implementation of this should allow executing the specified action before the
     * listener is attached to the observable.
     * By default, does nothing.
     */
    public When<T> executeNow() {
        return this;
    }

    /**
     * Calls {@link #executeNow()} if the given condition is true.
     */
    public When<T> executeNow(Supplier<Boolean> condition) {
        if (condition.get()) executeNow();
        return this;
    }

    /**
     * @return whether the construct is "one-shot"
     * @see #oneShot()
     */
    public boolean isOneShot() {
        return oneShot;
    }

    /**
     * Specifies that the construct is "one-shot", meaning that once the
     * value changes the first time, the construct will automatically dispose itself.
     */
    public When<T> oneShot() {
        this.oneShot = true;
        return this;
    }

    /**
     * Removes all the invalidating sources added through {@link #invalidating(Observable)} and removes the listener
     * from them.
     */
    protected void dispose() {
        invalidatingObservables.forEach(o -> o.removeListener(invalidationListener));
        invalidatingObservables.clear();
        if (invalidationListener != null)
            invalidationListener = null;
    }

    /**
     * Convenience method to create and instance of {@link OnInvalidated}.
     */
    public static <T> OnInvalidated<T> onInvalidated(ObservableValue<T> observableValue) {
        return OnInvalidated.forObservable(observableValue);
    }

    /**
     * Convenience method to create an instance of {@link OnChanged}.
     */
    public static <T> OnChanged<T> onChanged(ObservableValue<T> observableValue) {
        return OnChanged.forObservable(observableValue);
    }

    /**
     * If a When construct exists for the given {@link ObservableValue},
     * {@link #dispose()} is invoked.
     */
    public static void disposeFor(ObservableValue<?> observableValue) {
        WeakReference<When<?>> ref = whens.remove(observableValue);
        When<?> remove = ref != null ? ref.get() : null;
        if (remove != null) remove.dispose();
    }

    /**
     * Concrete implementation of {@link When} that uses {@link ChangeListener}s to
     * listen for changes for a given {@link ObservableValue}.
     * <p></p>
     * You can specify the action to perform when this happens using a {@link BiConsumer},
     * {@link #then(BiConsumer)}.
     * <p>
     * You can also set a condition that has to be met for the action to be executed (see {@link #condition(BiFunction)}),
     * and an "else" action that is executed when it is not met, (see {@link #otherwise(TriConsumer)}).
     * <p></p>
     * Optionally you could also tell the construct to execute te given action immediately, the {@link BiConsumer} will
     * take null and {@link ObservableValue#getValue()} as the old and new values.
     * <p></p>
     * To activate the construct do not forget to call {@link #listen()} at the end.
     * <p></p>
     * An example:
     * <pre>
     * {@code
     *      IntegerProperty aNumber = new SimpleIntegerProperty(69);
     *      When.onChanged(aNumber) // You can also use... OnChanged.forObservable(...)
     *              .condition(aCondition)
     *              .then((oldValue, newValue) -> System.out.println("Value switched from: " + oldValue + " to " + newValue))
     *              .otherwise((ref, oldValue, newValue) -> System.out.println("Condition not met, execution action B"))
     *              .oneShot()
     *              .listen();
     * }
     * </pre>
     */
    public static class OnChanged<T> extends When<T> {
        private ChangeListener<T> listener;
        private BiConsumer<T, T> action;
        private TriConsumer<WeakReference<When<T>>, T, T> otherwise = (w, o, n) -> {};
        private BiFunction<T, T, Boolean> condition = (o, n) -> true;

        private OnChanged(ObservableValue<T> observableValue) {
            super(observableValue);
        }

        /**
         * Creates and instance of this construct for the given {@link ObservableValue}.
         */
        public static <T> OnChanged<T> forObservable(ObservableValue<T> observableValue) {
            return new OnChanged<>(observableValue);
        }

        /**
         * To set the action to perform when the specified {@link ObservableValue}
         * changes. The action is a {@link BiConsumer} that carries both the old value
         * and the new value of the observable.
         */
        public OnChanged<T> then(BiConsumer<T, T> action) {
            this.action = action;
            return this;
        }

        /**
         * Allows to set an action to perform when the given {@link #condition(BiFunction)} is not met.
         * <p></p>
         * This makes the "system" much more versatile. Imagine having a one-shot listener that you want to
         * dispose anyway even if the condition is not met, you can write something like this;
         * <pre>
         * {@code
         * When.onChanged(observable)
         *      .condition(aCondition)
         *      .then(action)
         *      .otherwise((w, o, n) -> Optional.ofNullable(w.get()).ifPresent(When::dispose)) // Note the null check
         *      .listen();
         *
         * }
         * </pre>
         * <p></p>
         * Also note that the otherwise action also carries the reference to this object wrapped in a {@link WeakReference}.
         */
        public OnChanged<T> otherwise(TriConsumer<WeakReference<When<T>>, T, T> otherwise) {
            this.otherwise = otherwise;
            return this;
        }

        /**
         * Allows to specify a condition under which the set action (see {@link #then(BiConsumer)})
         * is to be executed.
         * <p></p>
         * The condition is specified through a {@link BiFunction} that provides both the old and new values
         * of the {@link ObservableValue}.
         * <p></p>
         * In case the condition is not met the {@link #otherwise(TriConsumer)} action is executed instead.
         * <p></p>
         * For one-shot listeners, the action is executed and the listener disposed only if the condition is met, else
         * the {@link #otherwise(TriConsumer)} action is executed instead.
         */
        public OnChanged<T> condition(BiFunction<T, T, Boolean> condition) {
            this.condition = condition;
            return this;
        }

        /**
         * Executes the given action immediately with null as the old value and the current value of the
         * given {@link ObservableValue} as the new value.
         */
        public OnChanged<T> executeNow() {
            action.accept(null, observableValue.getValue());
            return this;
        }

        /**
         * Calls {@link #executeNow()} if the given condition is true.
         */
        public OnChanged<T> executeNow(Supplier<Boolean> condition) {
            if (condition.get()) executeNow();
            return this;
        }

        //================================================================================
        // Overridden Methods
        //================================================================================

        /**
         * Activates the {@code OnChanged} construct with the previously specified parameters.
         * So, builds the {@link ChangeListener} according to the {@link #isOneShot()} parameter,
         * then adds the listener to the specified {@link ObservableValue} and finally puts the Observable and
         * the OnChanged construct in the map.
         * <p></p>
         * Before activating the listener, it also activates all the invalidating sources added through {@link #invalidating(Observable)}.
         */
        @Override
        public OnChanged<T> listen() {
            if (oneShot) {
                listener = (observable, oldValue, newValue) -> {
                    if (condition.apply(oldValue, newValue)) {
                        action.accept(oldValue, newValue);
                        dispose();
                    } else {
                        otherwise.accept(new WeakReference<>(this), oldValue, newValue);
                    }
                };
            } else {
                listener = (observable, oldValue, newValue) -> {
                    if (condition.apply(oldValue, newValue)) {
                        action.accept(oldValue, newValue);
                    } else {
                        otherwise.accept(new WeakReference<>(this), oldValue, newValue);
                    }
                };
            }

            invalidatingObservables.forEach(o -> o.addListener(invalidationListener));
            register();
            observableValue.addListener(listener);
            return this;
        }

        /**
         * When one of the invalidating sources added through {@link #invalidating(Observable)} changes, this method will be
         * invoked and causes {@link #executeNow(Supplier)} to execute. The condition function is supplied with 'null' as the
         * old value, and {@link ObservableValue#getValue()} as the new value.
         */
        @Override
        protected When<T> invalidate() {
            executeNow(() -> condition.apply(null, observableValue.getValue()));
            return this;
        }

        /**
         * {@inheritDoc}
         * <p></p>
         * Disposes the {@code OnChanged} construct by removing the {@link ChangeListener}
         * from the {@link ObservableValue}, then sets the listener to null and finally removes
         * the observable from the map.
         */
        @Override
        public void dispose() {
            super.dispose();
            if (observableValue != null && listener != null) {
                observableValue.removeListener(listener);
                listener = null;
                whens.remove(observableValue);
            }
        }
    }

    /**
     * Concrete implementation of {@link When} that uses {@link InvalidationListener}s to
     * listen for changes for a given {@link ObservableValue}.
     * <p></p>
     * You can specify the action to perform when this happens using a {@link Consumer},
     * {@link #then(Consumer)}.
     * <p>
     * You can also set a condition that has to be met for the action to be executed (see {@link #condition(Function)}),
     * and an "else" action that is executed when it is not met, (see {@link #otherwise(BiConsumer)}).
     * <p></p>
     * Optionally you could also tell the construct to execute the given action immediately, the {@link Consumer} will
     * take {@link ObservableValue#getValue()} as input.
     * <p></p>
     * To activate the construct do not forget to call {@link #listen()} at the end.
     * <p></p>
     * An example:
     * <pre>
     * {@code
     *      BooleanProperty aSwitch = new SimpleBooleanProperty(false);
     *      When.onInvalidated(aSwitch) // You can also use... OnInvalidated.forObservable(...)
     *              .condition(aCondition)
     *              .then(value -> System.out.println("Value switched to: " + value))
     *              .otherwise((ref, oldValue, newValue) -> System.out.println("Condition not met, execution action B"))
     *              .oneShot()
     *              .executeNow() // This could also be moved after the listen method
     *              .listen();
     * }
     * </pre>
     */
    public static class OnInvalidated<T> extends When<T> {
        private InvalidationListener listener;
        private Consumer<T> action;
        private BiConsumer<WeakReference<When<T>>, T> otherwise = (w, t) -> {};
        private Function<T, Boolean> condition = t -> true;

        private OnInvalidated(ObservableValue<T> observableValue) {
            super(observableValue);
        }

        /**
         * Creates and instance of this construct for the given {@link ObservableValue}.
         */
        public static <T> OnInvalidated<T> forObservable(ObservableValue<T> observableValue) {
            return new OnInvalidated<>(observableValue);
        }

        //================================================================================
        // Methods
        //================================================================================

        /**
         * To set the action to perform when the specified {@link ObservableValue}
         * becomes invalid. The action is a {@link Consumer} that carries the new value
         * of the observable.
         */
        public OnInvalidated<T> then(Consumer<T> action) {
            this.action = action;
            return this;
        }

        /**
         * Allows to set an action to perform when the given {@link #condition(Function)} is not met.
         * <p></p>
         * This makes the "system" much more versatile. Imagine having a one-shot listener that you want to
         * dispose anyway even if the condition is not met, you can write something like this;
         * <pre>
         * {@code
         * When.onChanged(observable)
         *      .condition(aCondition)
         *      .then(action)
         *      .otherwise((w, t) -> Optional.ofNullable(w.get()).ifPresent(When::dispose)) // Note the null check
         *      .listen();
         *
         * }
         * </pre>
         * <p></p>
         * Also note that the otherwise action also carries the reference to this object wrapped in a {@link WeakReference}.
         */
        public OnInvalidated<T> otherwise(BiConsumer<WeakReference<When<T>>, T> otherwise) {
            this.otherwise = otherwise;
            return this;
        }

        /**
         * Allows to specify a condition under which the set action (see {@link #then(Consumer)})
         * is to be executed.
         * <p></p>
         * The condition is specified through a {@link Function} that provides the current value
         * of the {@link ObservableValue}.
         * <p></p>
         * In case the condition is not met the {@link #otherwise(BiConsumer)} action is executed instead.
         * <p></p>
         * For one-shot listeners, the action is executed and the listener disposed only if the condition is met, else
         * the {@link #otherwise(BiConsumer)} action is executed instead.
         */
        public OnInvalidated<T> condition(Function<T, Boolean> condition) {
            this.condition = condition;
            return this;
        }

        /**
         * Executes the given action immediately with the current value of the
         * given {@link ObservableValue}.
         */
        public OnInvalidated<T> executeNow() {
            action.accept(observableValue.getValue());
            return this;
        }

        /**
         * Calls {@link #executeNow()} if the given condition is true.
         */
        public OnInvalidated<T> executeNow(Supplier<Boolean> condition) {
            if (condition.get()) executeNow();
            return this;
        }

        /**
         * Activates the {@code OnInvalidated} construct with the previously specified parameters.
         * So, builds the {@link InvalidationListener} according to the {@link #isOneShot()} parameter,
         * then adds the listener to the specified {@link ObservableValue} and finally puts the Observable and
         * the OnInvalidated construct in the map.
         * <p></p>
         * Before activating the listener, it also activates all the invalidating sources added through {@link #invalidating(Observable)}.
         */
        @Override
        public OnInvalidated<T> listen() {
            if (oneShot) {
                listener = invalidated -> {
                    T value = observableValue.getValue();
                    if (condition.apply(value)) {
                        action.accept(value);
                        dispose();
                    } else {
                        otherwise.accept(new WeakReference<>(this), value);
                    }
                };
            } else {
                listener = invalidated -> {
                    T value = observableValue.getValue();
                    if (condition.apply(value)) {
                        action.accept(value);
                    } else {
                        otherwise.accept(new WeakReference<>(this), value);
                    }
                };
            }

            invalidatingObservables.forEach(o -> o.addListener(invalidationListener));
            register();
            observableValue.addListener(listener);
            return this;
        }

        /**
         * When one of the invalidating sources added through {@link #invalidating(Observable)} changes, this method will be
         * invoked and causes {@link #executeNow(Supplier)} to execute. The condition function is supplied with {@link ObservableValue#getValue()}.
         */
        @Override
        protected When<T> invalidate() {
            executeNow(() -> condition.apply(observableValue.getValue()));
            return this;
        }

        /**
         * {@inheritDoc}
         * <p></p>
         * Disposes the {@code OnInvalidated} construct by removing the {@link InvalidationListener}
         * from the {@link ObservableValue}, then sets the listener to null and finally removes
         * the observable from the map.
         */
        @Override
        public void dispose() {
            super.dispose();
            if (observableValue != null && listener != null) {
                observableValue.removeListener(listener);
                listener = null;
                whens.remove(observableValue);
            }
        }
    }
}