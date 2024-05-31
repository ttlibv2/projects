package vn.conyeu.common.converter;

import jakarta.persistence.Converter;
import vn.conyeu.common.converter.base.ListToString;
@Converter(autoApply = true)
public class ListStringToString extends ListToString<String> {

    public ListStringToString() {
        super(String.class);
    }
}