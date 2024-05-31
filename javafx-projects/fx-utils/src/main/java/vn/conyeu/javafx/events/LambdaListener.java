package vn.conyeu.javafx.events;


import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.WeakInvalidationListener;
import javafx.beans.property.MapProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.beans.value.WeakChangeListener;
import javafx.collections.*;

import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;

public final class LambdaListener {
    private static final Consumer EMPTY_CONSUMER = e -> {};
    private static final ConsumerChange EMPTY_TREE_CONSUMER = (obs, old, value) -> {};
    static final ConsumerMapChange EMPTY_MAP_CONSUMER = change -> {};
    static final ConsumerListChangeListener EMPTY_LIST_CONSUMER = change -> {};

    // support change listeners
    private final Map<ObservableValue, ConsumerChange> propertyReferenceMap;
    private final ChangeListener<Object> propertyChangedListener;
    private final WeakChangeListener<Object> weakPropertyChangedListener;

    // support invalidation listeners
    private final Map<Observable, Consumer<Observable>> observableReferenceMap;
    private final InvalidationListener invalidationListener;
    private final WeakInvalidationListener weakInvalidationListener;

    // support list change listeners
    private final Map<ObservableList, ConsumerListChangeListener> observableListReferenceMap;
    private final ListChangeListener<Object> listChangeListener;
    private final WeakListChangeListener<Object> weakListChangeListener;

    // support map change listeners
    private final MapChangeListener<Object, Object> mapChangeListener;
    private final WeakMapChangeListener<Object, Object> weakMapChangeListener;
    private final Map<ObservableMap, ConsumerMapChange> observableMapReferenceMap;

    public LambdaListener() {

        // change listening support
        propertyReferenceMap = new HashMap<>();
        propertyChangedListener = this::acceptChangeListenerMap;
        weakPropertyChangedListener = new WeakChangeListener<>(propertyChangedListener);

        // invalidation listening support
        observableReferenceMap = new HashMap<>();
        invalidationListener = obs -> observableReferenceMap.getOrDefault(obs, EMPTY_CONSUMER).accept(obs);
        weakInvalidationListener = new WeakInvalidationListener(invalidationListener);

        // list change listening support
        observableListReferenceMap = new IdentityHashMap<>();
        listChangeListener = change -> observableListReferenceMap.getOrDefault(change.getList(), EMPTY_LIST_CONSUMER).accept(change);
        weakListChangeListener = new WeakListChangeListener<>(listChangeListener);

        // map change listening support
        observableMapReferenceMap = new IdentityHashMap<>();
        mapChangeListener = change -> observableMapReferenceMap.getOrDefault(change.getMap(), EMPTY_MAP_CONSUMER).accept(change);
        weakMapChangeListener = new WeakMapChangeListener<>(mapChangeListener);
    }

    private <V> void acceptChangeListenerMap(ObservableValue observable, V ov, V nv) {
        propertyReferenceMap.getOrDefault(observable, EMPTY_TREE_CONSUMER).accept(observable, ov, nv);
    }

    /**
     * Registers a consumer to be invoked on change notification from the given property. Does nothing
     * if property or consumer is null. Consumers registered to the same property will be executed
     * in the order they have been registered.
     *
     * @param property the property to observe for change notification
     * @param consumer the consumer to be invoked on change notification from the property
     */
    public <T> void registerChangeListener(ObservableValue<T> property, ConsumerChange<T> consumer) {
        if (property != null && consumer != null) {
            if (!propertyReferenceMap.containsKey(property)) property.addListener(weakPropertyChangedListener);
            propertyReferenceMap.merge(property, consumer, ConsumerChange::andThen);
        }
    }

    /**
     * Stops observing the given property for change notification. Returns
     * a single chained consumer consisting of all consumers registered with
     * {@link #registerChangeListener(ObservableValue, ConsumerChange)} in the order they
     * have been registered.
     *
     * @param property the property to stop observing for change notification
     * @return a single chained consumer consisting of all consumers registered for the given property
     * or null if none has been registered or the property is null
     */
    public <T> ConsumerChange<ObservableValue<T>> unregisterChangeListeners(ObservableValue<T> property) {
        if (property != null) {
            property.removeListener(weakPropertyChangedListener);
            return propertyReferenceMap.remove(property);
        }
        return null;
    }

    /**
     * Registers a consumer to be invoked on invalidation notification from the given observable.
     * Does nothing if observable or consumer is null. Consumers registered to the same observable will be executed
     * in the order they have been registered.
     *
     * @param observable the observable to observe for invalidation notification
     * @param consumer   the consumer to be invoked on invalidation notification from the observable
     */
    public void registerInvalidationListener(Observable observable, Consumer<Observable> consumer) {
        if (observable != null && consumer != null) {
            if (!observableReferenceMap.containsKey(observable)) observable.addListener(weakInvalidationListener);
            observableReferenceMap.merge(observable, consumer, Consumer::andThen);
        }
    }

    /**
     * Stops observing the given observable for invalidation notification.
     * Returns a single chained consumer consisting of all consumers registered with
     * {@link #registerInvalidationListener(Observable, Consumer)} in the
     * order they have been registered.
     *
     * @param observable the observable to stop observing for invalidation notification
     * @return a single chained consumer consisting of all consumers registered for given observable
     * or null if none has been registered or the observable is null
     */
    public Consumer<Observable> unregisterInvalidationListeners(Observable observable) {
        if (observable == null) return null;
        observable.removeListener(weakInvalidationListener);
        return observableReferenceMap.remove(observable);
    }

    /**
     * Registers a consumer to be invoked on map change notification from the given observable map.
     * Does nothing if map or consumer is null. Consumers registered to the same observable map
     * will be executed in the order they have been registered.
     *
     * @param map      the observable map observe for list change notification
     * @param consumer the consumer to be invoked on map change notification from the map
     */
    public <K, V> void registerMapChangeListener(MapProperty<String, String> property, ConsumerMapChange<K, V> consumer) {
        if (property != null && consumer != null) {
            if (!observableMapReferenceMap.containsKey(property)) property.addListener(weakMapChangeListener);
            observableMapReferenceMap.merge(property, consumer, ConsumerMapChange::andThen);
        }
    }

    /**
     * Stops observing the given observable map for map change notification.
     * Returns a single chained consumer consisting of all consumers registered with
     * {@link #registerMapChangeListener(ObservableMap, ConsumerMapChange)} in the order they have been registered.
     *
     * @param map the observable map to stop observing for map change notification
     * @return a single chained consumer consisting of all consumers added for the given map
     * or null if none has been registered or the map is null
     */
    public <K, V> ConsumerMapChange<K, V> unregisterMapChangeListeners(ObservableMap<K, V> map) {
        if (map == null) return null;
        map.removeListener(weakMapChangeListener);
        return observableMapReferenceMap.remove(map);
    }

    /**
     * Registers a consumer to be invoked on list change notification from the given observable list.
     * Does nothing if list or consumer is null. Consumers registered to the same observable list
     * will be executed in the order they have been registered.
     *
     * @param list     the observable list observe for list change notification
     * @param consumer the consumer to be invoked on list change notification from the list
     */
    public <E> void registerListChangeListener(ObservableList<E> list, ConsumerListChangeListener<E> consumer) {
        if (list != null && consumer != null) {
            if (!observableListReferenceMap.containsKey(list)) list.addListener(weakListChangeListener);
            observableListReferenceMap.merge(list, consumer, ConsumerListChangeListener::andThen);
        }
    }

    /**
     * Stops observing the given observable list for list change notification.
     * Returns a single chained consumer consisting of all consumers registered with
     * {@link #registerListChangeListener(ObservableList, ConsumerListChangeListener)} in the order they have been registered.
     *
     * @param list the observable list to stop observing for list change notification
     * @return a single chained consumer consisting of all consumers added for the given list
     * or null if none has been registered or the list is null
     */
    public <E> ConsumerListChangeListener<E> unregisterListChangeListeners(ObservableList<E> list) {
        if (list == null) return null;
        list.removeListener(weakListChangeListener);
        return observableListReferenceMap.remove(list);
    }


    /**
     * Stops observing all types of notification from all registered observables.
     * <p>
     * Note: this handler is still usable after calling this method.
     */
    public void dispose() {

        // unhook change listeners
        for (ObservableValue<?> value : propertyReferenceMap.keySet()) {
            value.removeListener(weakPropertyChangedListener);
        }

        propertyReferenceMap.clear();

        // unhook invalidation listeners
        for (Observable value : observableReferenceMap.keySet()) {
            value.removeListener(weakInvalidationListener);
        }

        observableReferenceMap.clear();

        // unhook list change listeners
        for (ObservableList<?> list : observableListReferenceMap.keySet()) {
            list.removeListener(weakListChangeListener);
        }

        observableListReferenceMap.clear();
    }


    @FunctionalInterface
    public interface ConsumerListChangeListener<E> {

        /**
         * Performs this operation on the given argument.
         *
         * @param eChange the input argument
         */
        void accept(ListChangeListener.Change<E> eChange);

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
        default ConsumerListChangeListener<E> andThen(ConsumerListChangeListener<E> after) {
            Objects.requireNonNull(after);
            return change -> { accept(change); after.accept(change); };
        }

    }

    @FunctionalInterface
    public interface ConsumerMapChange<K, V> {

        /**
         * Performs this operation on the given argument.
         * @param change the input argument
         */
        void accept(MapChangeListener.Change<K, V> change);

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
        default ConsumerMapChange< K, V> andThen(ConsumerMapChange<K, V> after) {
            Objects.requireNonNull(after);
            return change -> { accept(change); after.accept(change);};
        }
    }

    @FunctionalInterface
    public interface ConsumerChange<T> {

        /**
         * Performs this operation on the given argument.
         * @param observable the Observable argument
         * @param oldValue the old value
         * @param newValue the new value
         */
        void accept(ObservableValue<T> observable, T oldValue, T newValue);

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
        default ConsumerChange<T> andThen(ConsumerChange<T> after) {
            Objects.requireNonNull(after);
            return (obs, ov, nv) -> {
                accept(obs, ov, nv);
                after.accept(obs, ov, nv);
            };
        }
    }
}