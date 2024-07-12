package vn.conyeu.common.converter.base;

import vn.conyeu.commons.beans.ObjectMap;
import vn.conyeu.commons.utils.Asserts;
import vn.conyeu.commons.utils.MapperHelper;
import vn.conyeu.commons.utils.Objects;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Supplier;

public abstract class CollectionToString<C extends Collection<T>, T> extends ConvertAttrDb<C> {
    protected static final String DELIMITER = ";";
    protected final Class<T> objectCls;
    protected final String delimiter;
    protected final Set<String> fields;

    public CollectionToString(Class<T> objectCls) {
        this(objectCls, null, null);
    }

    public CollectionToString(Class<T> objectCls, String delimiter) {
        this(objectCls, delimiter, null);
    }

    public CollectionToString(Class<T> objectCls, Set<String> fields) {
        this(objectCls, null, fields);
    }
    public CollectionToString(Class<T> objectCls, String delimiter, Set<String> fields) {
        this.delimiter = Objects.isBlank(delimiter) ? DELIMITER: delimiter;
        this.fields = fields == null ? new HashSet<>():fields;
        this.objectCls = Asserts.notNull(objectCls);
    }

    protected abstract Supplier<C> createCollection();

    protected Function<T, String> itemToString() {
        return object -> fields.isEmpty()
                ? MapperHelper.serializeToString(object)
                : ObjectMap.toJsonString(object, fields);
    }

    protected Function<String, T> stringToItem() {
        return str -> MapperHelper.convert(str, objectCls);
    }


    protected final Function<C, String> objectToString() {
        return list -> Objects.toString(list, delimiter, itemToString());
    }

    protected final Function<String, C> stringToObject() {
        Supplier<C> createCollection = Asserts.notNull(createCollection());
        return string -> Objects.toCollection(string, delimiter, createCollection, stringToItem());
    }
}