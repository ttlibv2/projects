package vn.conyeu.javafx.property.sync;

import javafx.beans.binding.BooleanExpression;
import javafx.beans.property.Property;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.ReadOnlyBooleanWrapper;
import javafx.beans.property.ReadOnlyIntegerWrapper;
import javafx.beans.value.ObservableValue;
import vn.conyeu.javafx.bindings.BiBindingManager;
import vn.conyeu.javafx.bindings.BindingManager;

public class SyncIntProperty extends ReadOnlyIntegerWrapper implements SyncProperty<Number> {
    private final ReadOnlyBooleanWrapper waiting = new ReadOnlyBooleanWrapper();

    public SyncIntProperty() {
        initialize();
    }

    public SyncIntProperty(int initialValue) {
        super(initialValue);
        initialize();
    }

    public SyncIntProperty(Object bean, String name) {
        super(bean, name);
        initialize();
    }

    public SyncIntProperty(Object bean, String name, int initialValue) {
        super(bean, name, initialValue);
        initialize();
    }

    /**
     * Adds a listener to the property by calling {@link ExecutionUtils#executeWhen(BooleanExpression, Runnable, Runnable, boolean, boolean, boolean, boolean)}
     * to call {@link #fireValueChangedEvent()} when the property is awakened, {@link #awake()}.
     */
    private void initialize() {
        ExecutionUtils.executeWhen(waiting, () -> {
        }, this::fireValueChangedEvent, false, false, false, false);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void setAndWait(Number value, ObservableValue<?> observable) {
        if (!Helper.check(this, value, observable)) return;

        waiting.set(true);
        ExecutionUtils.executeWhen(
                observable,
                this::awake,
                false,
                () -> true,
                true
        );
        set(value.intValue());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isWaiting() {
        return waiting.get();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ReadOnlyBooleanProperty waiting() {
        return waiting.getReadOnlyProperty();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void awake() {
        waiting.set(false);
    }

    /**
     * {@inheritDoc}
     * <p></p>
     * Overridden to not fire a change event if {@link #waiting()} is true.
     */
    @Override
    protected void fireValueChangedEvent() {
        if (isWaiting()) return;
        super.fireValueChangedEvent();
    }

    //================================================================================
    // Binding
    //================================================================================

    /**
     * Creates a unidirectional bindings with the given observable.
     * <p>
     * The binding is created using the new {@link BindingManager} mechanism.
     * <p></p>
     * If the property is already bound it is automatically unbound before bindings to the new observable.
     *
     * @throws IllegalArgumentException if the given observable is the property itself
     * @see BindingManager
     */
    @Override
    public void bind(ObservableValue<? extends Number> source) {
        if (this == source) {
            throw new IllegalArgumentException("Cannot bind to itself!");
        }

        if (isBound()) unbind();
        BindingManager.instance().bind(this).to(source).create();
    }

    /**
     * Creates a bidirectional bindings between this property and the given property.
     * <p>
     * The binding is created using the new {@link BiBindingManager} mechanism.
     * <p></p>
     * If the property is already bound unidirectionally it is automatically unbound.
     * <p>
     * If the property is already bound bidirectionally it won't be automatically unbound, just like JavaFX,
     * this way you can have multiple bidirectional bindings
     *
     * @throws IllegalArgumentException if the given observable is the property itself
     * @see BiBindingManager
     */
    @Override
    public void bindBidirectional(Property<Number> other) {
        if (this == other) {
            throw new IllegalArgumentException("Cannot bind to itself!");
        }

        if (isBound()) unbind();
        BiBindingManager.instance().bindBidirectional(this).to(other).create();
    }

    /**
     * Overridden to call {@link BindingManager#unbind(ObservableValue)}.
     */
    @Override
    public void unbind() {
        BindingManager.instance().unbind(this);
    }

    /**
     * Overridden to call {@link BiBindingManager#unbind(ObservableValue, ObservableValue)}.
     */
    @Override
    public void unbindBidirectional(Property<Number> other) {
        BiBindingManager.instance().unbind(this, other);
    }

    /**
     * Delegate method for {@link BiBindingManager#disposeFor(ObservableValue)}.
     */
    public void clearBidirectional() {
        BiBindingManager.instance().disposeFor(this);
    }

    /**
     * Overridden to check the {@link BindingManager#isBound(ObservableValue)} flag value and {@link BindingManager#isIgnoreBinding(ObservableValue)}.
     *
     * @return true only if `BindingManager.isBound()` is true and `isIgnoreBound()` is false
     */
    @Override
    public boolean isBound() {
        BindingManager manager = BindingManager.instance();
        return manager.isBound(this) && !manager.isIgnoreBinding(this);
    }

}