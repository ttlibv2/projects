package vn.conyeu.javafx.bindings;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.ObjectBinding;
import vn.conyeu.commons.utils.Asserts;

public class ObjectBindingBuilder<T> extends BindingBuilder<T, ObjectBinding<T>> {

	public static <T> ObjectBindingBuilder<T> build() {
		return new ObjectBindingBuilder<>();
	}

	@Override
	protected ObjectBinding<T> create() {
		Asserts.notNull(mapper, "Mapper has not been set!");
		return Bindings.createObjectBinding(mapper, getSourcesArray());
	}
}