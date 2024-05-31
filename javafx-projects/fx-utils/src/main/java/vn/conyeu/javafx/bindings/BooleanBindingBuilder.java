package vn.conyeu.javafx.bindings;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import vn.conyeu.commons.utils.Asserts;

public class BooleanBindingBuilder extends BindingBuilder<Boolean, BooleanBinding> {

	public static BooleanBindingBuilder build() {
		return new BooleanBindingBuilder();
	}

	@Override
	protected BooleanBinding create() {
		Asserts.notNull(mapper, "Mapper has not been set!");
		return Bindings.createBooleanBinding(mapper, getSourcesArray());
	}

}