package vn.conyeu.javafx.bindings;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.IntegerBinding;
import vn.conyeu.commons.utils.Asserts;

public class IntegerBindingBuilder extends BindingBuilder<Integer, IntegerBinding> {

	public static IntegerBindingBuilder build() {
		return new IntegerBindingBuilder();
	}

	@Override
	protected IntegerBinding create() {
		Asserts.notNull(mapper, "Mapper has not been set!");
		return Bindings.createIntegerBinding(mapper, getSourcesArray());
	}
}