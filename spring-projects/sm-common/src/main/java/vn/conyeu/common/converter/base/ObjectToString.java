package vn.conyeu.common.converter.base;

import jakarta.persistence.Converter;
import vn.conyeu.commons.beans.ObjectMap;
import vn.conyeu.commons.utils.Asserts;
import vn.conyeu.commons.utils.MapperHelper;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.function.Function;

/**
 * @param <X> the object type
 * */
@Converter
public abstract class ObjectToString<X> extends ConvertAttrDb<X> {
    protected final Class<X> objectClass;
    protected final Set<String> onlyFields;

    /**
     * Create converter for object
     * @param objectClass the object type
     * */
    public ObjectToString(Class<X> objectClass) {
        this(objectClass, new LinkedHashSet<>());
    }

    /**
     * Create converter for object
     * @param objectClass the object type
     * @param onlyFields the fields export
     * */
    public ObjectToString(Class<X> objectClass, Set<String> onlyFields) {
        this.objectClass = Asserts.notNull(objectClass);
        this.onlyFields = onlyFields;
    }

    protected Function<X, String> objectToString() {
        return object -> onlyFields.isEmpty()
                ? MapperHelper.serializeToString(object)
                : ObjectMap.toJsonString(object, onlyFields);
    }

    /**
     * Returns the objectClass
     */
    public Class<X> getConvertCls() {
        return objectClass;
    }

    protected Function<String, X> stringToObject() {
        return str -> MapperHelper.convert(str, objectClass);
    }
}