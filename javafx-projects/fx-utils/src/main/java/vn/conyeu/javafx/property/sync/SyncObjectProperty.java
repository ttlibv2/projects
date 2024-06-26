/*
 * Copyright (C) 2022 Parisi Alessandro
 * This file is part of MaterialFX (https://github.com/palexdev/MaterialFX).
 *
 * MaterialFX is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * MaterialFX is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with MaterialFX.  If not, see <http://www.gnu.org/licenses/>.
 */

package vn.conyeu.javafx.property.sync;

import javafx.beans.binding.BooleanExpression;
import javafx.beans.property.Property;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.ReadOnlyBooleanWrapper;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.value.ObservableValue;
import vn.conyeu.javafx.bindings.BiBindingManager;
import vn.conyeu.javafx.bindings.BindingManager;

/**
 * Implementation of {@link SyncProperty} for generic values.
 *
 * @param <T> the type of the wrapped Object
 */
public class SyncObjectProperty<T> extends ReadOnlyObjectWrapper<T> implements SyncProperty<T> {
	//================================================================================
	// Properties
	//================================================================================
	private final ReadOnlyBooleanWrapper waiting = new ReadOnlyBooleanWrapper();

	//================================================================================
	// Constructors
	//================================================================================
	public SyncObjectProperty() {
		initialize();
	}

	public SyncObjectProperty(T initialValue) {
		super(initialValue);
		initialize();
	}

	public SyncObjectProperty(Object bean, String name) {
		super(bean, name);
		initialize();
	}

	public SyncObjectProperty(Object bean, String name, T initialValue) {
		super(bean, name, initialValue);
		initialize();
	}

	//================================================================================
	// Methods
	//================================================================================

	/**
	 * Adds a listener to the property by calling {@link ExecutionUtils#executeWhen(BooleanExpression, Runnable, Runnable, boolean, boolean, boolean, boolean)}
	 * to call {@link #fireValueChangedEvent()} when the property is awakened, {@link #awake()}.
	 */
	private void initialize() {
		ExecutionUtils.executeWhen(
				waiting,
				() -> {},
				this::fireValueChangedEvent,
				false,
				false,
				false,
				false
		);
	}

	//================================================================================
	// Implemented/Overridden Methods
	//================================================================================

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setAndWait(T value, ObservableValue<?> observable) {
		if (!Helper.check(this, value, observable)) return;

		waiting.set(true);
		ExecutionUtils.executeWhen(
				observable,
				this::awake,
				false,
				() -> true,
				true
		);
		set(value);
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
	public void bind(ObservableValue<? extends T> source) {
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
	public void bindBidirectional(Property<T> other) {
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
	public void unbindBidirectional(Property<T> other) {
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
		return BindingManager.instance().isBound(this) && !BindingManager.instance().isIgnoreBinding(this);
	}
}