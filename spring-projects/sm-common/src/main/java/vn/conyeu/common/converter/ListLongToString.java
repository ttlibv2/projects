package vn.conyeu.common.converter;

import jakarta.persistence.Converter;
import vn.conyeu.common.converter.base.ListToString;

@Converter(autoApply = true)
public class ListLongToString extends ListToString<Long> {

    public ListLongToString() {
        super(Long.class);
    }
}