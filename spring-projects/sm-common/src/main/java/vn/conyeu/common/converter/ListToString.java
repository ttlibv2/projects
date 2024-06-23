package vn.conyeu.common.converter;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.function.Supplier;

//@Converter
public abstract class ListToString<E> extends CollectToString<E, List<E>> {

    public ListToString(Class<E> objectCls) {
        super(objectCls);
    }

    public ListToString(Class<E> objectCls, String delimiter) {
        super(objectCls, delimiter);
    }

    public ListToString(Class<E> objectCls, Set<String> fields) {
        super(objectCls, fields);
    }

    public ListToString(Class<E> objectCls, String delimiter, Set<String> fields) {
        super(objectCls, delimiter, fields);
    }

    @Override
    protected final Supplier<List<E>> supplierNew() {
        return ArrayList::new;
    }


}