package vn.conyeu.javafx.bindings;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.StringBinding;
import vn.conyeu.commons.utils.Asserts;

public class StringBindingBuilder extends BindingBuilder<String, StringBinding> {

	public static StringBindingBuilder build() {
		return new StringBindingBuilder();
	}

	@Override
	protected StringBinding create() {
		Asserts.notNull(mapper, "Mapper has not been set!");
		return Bindings.createStringBinding(mapper, getSourcesArray());
	}
}