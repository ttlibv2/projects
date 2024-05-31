package vn.conyeu.javafx.bindings;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.DoubleBinding;
import vn.conyeu.commons.utils.Asserts;

public class DoubleBindingBuilder extends BindingBuilder<Double, DoubleBinding> {

	public static DoubleBindingBuilder build() {
		return new DoubleBindingBuilder();
	}

	@Override
	protected DoubleBinding create() {
		Asserts.notNull(mapper, "Mapper has not been set!");
		return Bindings.createDoubleBinding(mapper, getSourcesArray());
	}
}