package vn.conyeu.javafx.bindings;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.LongBinding;
import vn.conyeu.commons.utils.Asserts;

public class LongBindingBuilder extends BindingBuilder<Long, LongBinding> {

	public static LongBindingBuilder build() {
		return new LongBindingBuilder();
	}

	@Override
	protected LongBinding create() {
		Asserts.notNull(mapper, "Mapper has not been set!");
		return Bindings.createLongBinding(mapper, getSourcesArray());
	}
}