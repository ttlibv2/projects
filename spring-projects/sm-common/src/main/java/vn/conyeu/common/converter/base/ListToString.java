package vn.conyeu.common.converter.base;

import jakarta.persistence.Converter;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.function.Supplier;

@Converter
public abstract class ListToString<T> extends CollectionToString<List<T>, T> {

    public ListToString(Class<T> objectCls) {
        super(objectCls);
    }

    public ListToString(Class<T> objectCls, String delimiter) {
        super(objectCls, delimiter);
    }

    public ListToString(Class<T> objectCls, Set<String> fields) {
        super(objectCls, fields);
    }

    public ListToString(Class<T> objectCls, String delimiter, Set<String> fields) {
        super(objectCls, delimiter, fields);
    }

    @Override
    protected Supplier<List<T>> createCollection() {
        return ArrayList::new;
    }
}