package vn.conyeu.common.converter;

import jakarta.persistence.Converter;
import vn.conyeu.common.converter.base.SetToString;

@Converter(autoApply = true)
public class SetString extends SetToString<String> {
    public SetString() {
        super(String.class);
    }

    public SetString(String delimiter) {
        super(String.class, delimiter);
    }

}