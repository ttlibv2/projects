package vn.conyeu.ts.odcore.domain;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.NoArgsConstructor;
import vn.conyeu.commons.beans.ObjectMap;
import vn.conyeu.commons.utils.MapperHelper;
import vn.conyeu.commons.utils.Objects;

import java.io.Serializable;
import java.util.*;

@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ClsModel<T> implements Serializable {

    public static ObjectMap NAME_FIELD = ObjectMap
            .setNew("id", 0).set("name", 1).set("display_name", 1);


    protected ObjectMap custom;

    protected boolean isNull(Object object) {
        return object == null || "false".equals(object);
    }

    /**
     * Returns the custom
     */
    @JsonAnyGetter
    public ObjectMap getCustom() {
        custom = ObjectMap.ifNull(custom);
        return custom;
    }

    @JsonAnySetter
    public T set(String key, Object value) {
        getCustom().set(key, value);
        return (T) this;
    }

    public void removeIf(String... objKeys) {
        List<String> listKey = Arrays.asList(objKeys);
        getCustom().entrySet().removeIf(e -> listKey.contains(e.getKey()));
    }

    public Set<String> fieldCloneIgnore() {
        return new LinkedHashSet<>();
    }

    public ObjectMap cloneMap() {
        ObjectMap map = ObjectMap.fromJson(this);
        fieldCloneIgnore().forEach(map::delete);
        return map;
    }

    /**
     * Convert object to field id
     *
     * @param object Object
     */
    protected Long getObjectID(Object object) {
        return getObjectID(object, "id");
    }


    /**
     * Convert object to field id
     *
     * @param object Object
     */
    protected Long getObjectID(Object object, String fieldId) {
        ObjectMap objMap = objectToMap(object);
        return objMap == null ? null : objMap.getLong(fieldId);
    }


    //============================================

    public static ObjectMap objectToMap(Object arr) {
        return objectToMap(arr, ObjectMap.create());
    }

    public static ObjectMap objectToMap(Object object, ObjectMap valueDefault) {
        if (object == null) return valueDefault;
        if (object instanceof ObjectMap map) return map;
        if (object instanceof Map map) return ObjectMap.fromMap(map);

        Object[] array = Objects.toObjectArray(object);
        return array == null || array.length <= 1 ? valueDefault : objectToMap(array);
    }

    public static ObjectMap objectToMap(Object[] arr) {
        return objectToMap(arr, NAME_FIELD);
    }

    public static ObjectMap objectToMap(Object[] arr, ObjectMap field) {
        ObjectMap obj = ObjectMap.create();
        if (arr != null && arr.length > 0) {
            field.keySet().forEach(key -> obj.set(key, arr[field.getInteger(key)]));
        }

        return obj;
    }

    public ClsModel updateFromMap(ObjectMap mapData) {
        MapperHelper.updateToMap(this, mapData);
        return this;
    }






}