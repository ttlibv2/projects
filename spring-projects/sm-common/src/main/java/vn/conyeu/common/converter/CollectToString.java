package vn.conyeu.common.converter;

import vn.conyeu.commons.beans.ObjectMap;
import vn.conyeu.commons.utils.MapperHelper;
import vn.conyeu.commons.utils.Objects;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Supplier;

public abstract class CollectToString<E, C extends Collection<E>> extends ConvertAttrDb<C> {
    protected static final String DELIMITER = ";";
    protected final Class<E> objectCls;
    protected final String delimiter;
    protected final Set<String> fields;

    public CollectToString(Class<E> objectCls) {
        this(objectCls, DELIMITER);
    }

    public CollectToString(Class<E> objectCls, String delimiter) {
        this(objectCls, delimiter, new LinkedHashSet<>());
    }

    public CollectToString(Class<E> objectCls, Set<String> fields) {
        this(objectCls, DELIMITER, fields);
    }

    public CollectToString(Class<E> objectCls, String delimiter, Set<String> fields) {
        this.objectCls = objectCls;
        this.delimiter = delimiter;
        this.fields = fields;
    }

    protected abstract Supplier<C> supplierNew();

    protected Function<E, String> itemToString() {
        return object -> fields.isEmpty()
                ? MapperHelper.serializeToString(object)
                : ObjectMap.toJsonString(object, fields);
    }

    protected Function<String, E> stringToItem() {
        return str -> MapperHelper.convert(str, objectCls);
    }


    protected final Function<C, String> objectToString() {
        return list -> Objects.toString(list, delimiter, itemToString());
    }

    protected final Function<String, C> stringToObject() {
        return string -> Objects.toCollect(string, delimiter, stringToItem(), supplierNew());
    }
}
