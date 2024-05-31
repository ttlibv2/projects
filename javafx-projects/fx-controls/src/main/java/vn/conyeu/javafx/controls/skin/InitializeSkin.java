package vn.conyeu.javafx.controls.skin;

import javafx.beans.Observable;
import javafx.beans.property.MapProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.scene.Node;
import vn.conyeu.javafx.controls.utils.ControlUtils;
import vn.conyeu.javafx.events.LambdaListener;

import java.util.List;
import java.util.function.Consumer;

public interface InitializeSkin<C> {

    /**
     * Initialize all function
     * @param control the skinnable
     * */
    default void initialize(C control) {
        initializeUI(control);
        initializeListenerUI(control);
        initializeListener(control);
        initializeDataUI(control);
    }

    /**
     * Initialize ui
     * @param control the skinnable
     * */
    default void initializeUI(C control) {}

    /**
     * Initialize listener for ui
     * @param control the skinnable
     * */
    default void initializeListenerUI(C control) {}

    /**
     * Initialize listener for skinnable
     * @param control the skinnable
     * */
    default void initializeListener(C control) {}

    /**
     * Initialize data for ui
     * @param control the skinnable
     * */
    default void initializeDataUI(C control) {}

    default void tryAddChildrenNode(List<Node> children, Node childNode) {
        if(!children.contains(childNode)) {
            children.add(childNode);
        }
    }

    default Node resolveIcon(String icon) {
        return ControlUtils.resolveIcon(icon);
    }

    default Node resolveIcon(Node iconNode, String iconLiteral) {
        if(iconNode != null) return iconNode;
        else return ControlUtils.resolveIcon(iconLiteral);
    }

    default <T> void changeListener(ObservableValue<T> property, LambdaListener.ConsumerChange<T> consumer) {
        getListener().registerChangeListener(property, consumer);
    }

    default void invalidationListener(Observable observable, Consumer<Observable> consumer) {
        getListener().registerInvalidationListener(observable, consumer);
    }

    default <K, V> void mapChangeListener(MapProperty<String, String> property, LambdaListener.ConsumerMapChange<K, V> consumer) {
        getListener().registerMapChangeListener(property, consumer);
    }

    default <E> void listChangeListener(ObservableList<E> list, LambdaListener.ConsumerListChangeListener<E> consumer) {
        getListener().registerListChangeListener(list, consumer);
    }

    default <T> LambdaListener.ConsumerChange<ObservableValue<T>> unChangeListeners(ObservableValue<T> property) {
        return getListener().unregisterChangeListeners(property);
    }

    default Consumer<Observable> unInvalidationListeners(Observable observable) {
        return getListener().unregisterInvalidationListeners(observable);
    }

    default <K, V> LambdaListener.ConsumerMapChange<K, V> unMapChangeListeners(ObservableMap<K, V> map) {
        return getListener().unregisterMapChangeListeners(map);
    }

    default <E> LambdaListener.ConsumerListChangeListener<E> unListChangeListeners(ObservableList<E> list) {
        return getListener().unregisterListChangeListeners(list);
    }

    LambdaListener getListener();


}