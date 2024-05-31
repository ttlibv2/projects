package vn.conyeu.javafx.bindings;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.FloatBinding;
import vn.conyeu.commons.utils.Asserts;

public class FloatBindingBuilder extends BindingBuilder<Float, FloatBinding> {

	public static FloatBindingBuilder build() {
		return new FloatBindingBuilder();
	}

	@Override
	protected FloatBinding create() {
		Asserts.notNull(mapper, "Mapper has not been set!");
		return Bindings.createFloatBinding(mapper, getSourcesArray());
	}
}