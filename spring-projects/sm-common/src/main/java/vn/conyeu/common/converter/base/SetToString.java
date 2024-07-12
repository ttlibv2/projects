package vn.conyeu.common.converter.base;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Supplier;

public abstract class SetToString<E> extends CollectionToString<Set<E>, E> {
    public SetToString(Class<E> objectCls) {
        super(objectCls);
    }

    public SetToString(Class<E> objectCls, String delimiter) {
        super(objectCls, delimiter);
    }

    public SetToString(Class<E> objectCls, String delimiter, Set<String> fields) {
        super(objectCls, delimiter, fields);
    }

    @Override
    protected Supplier<Set<E>> createCollection() {
        return HashSet::new;
    }
}