package vn.conyeu.common.converter.base;

import jakarta.persistence.Converter;
import vn.conyeu.commons.beans.ObjectMap;
import vn.conyeu.commons.utils.MapperHelper;
import vn.conyeu.commons.utils.Objects;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Function;

@Converter
public abstract class ListToString<T> extends ConvertAttrDb<List<T>> {
    protected static final String DELIMITER = ";";
    protected final Class<T> objectCls;
    protected final String delimiter;
    protected final Set<String> fields;

    public ListToString(Class<T> objectCls) {
        this(objectCls, DELIMITER);
    }

    public ListToString(Class<T> objectCls, String delimiter) {
        this(objectCls, delimiter, new LinkedHashSet<>());
    }

    public ListToString(Class<T> objectCls, Set<String> fields) {
        this(objectCls, DELIMITER, fields);
    }

    public ListToString(Class<T> objectCls, String delimiter, Set<String> fields) {
        this.objectCls = objectCls;
        this.delimiter = delimiter;
        this.fields = fields;
    }

    protected Function<T, String> itemToString() {
        return object -> fields.isEmpty()
                ? MapperHelper.serializeToString(object)
                : ObjectMap.toJsonString(object, fields);
    }

    protected Function<String, T> stringToItem() {
        return str -> MapperHelper.convert(str, objectCls);
    }


    protected final Function<List<T>, String> objectToString() {
        return list -> Objects.toString(list, delimiter, itemToString());
    }

    protected final Function<String, List<T>> stringToObject() {
        return string -> Objects.toList(string, delimiter, stringToItem());
    }


}