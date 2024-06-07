package vn.conyeu.commons.beans;

import com.fasterxml.jackson.databind.ObjectMapper;
import vn.conyeu.commons.exception.ConvertException;
import vn.conyeu.commons.utils.*;
import vn.conyeu.commons.utils.Objects;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class ObjectMap extends LinkedHashMap<String, Object> implements Cloneable {
    public static ObjectMap EMPTY = ObjectMap.create();

    public ObjectMap() {
    }

    public ObjectMap(Map<String, ?> m) {
        super(m);
    }

    /**
     * Create new object map
     *
     * @return ObjectMap
     */
    public static ObjectMap create() {
        return new ObjectMap();
    }

    /**
     * Convert json to object map
     *
     * @param jsonString String
     * @throws ConvertException if error
     */
    public static ObjectMap fromJson(String jsonString) {
        return MapperHelper.convert(jsonString, ObjectMap.class);
    }

    public static ObjectMap fromJson(Object object) {
        if (object instanceof Map map) return fromMap(map);
        return MapperHelper.convert(object, ObjectMap.class);
    }

    /**
     * Create object map from other map
     *
     * @param data Map
     * @throws ConvertException if error
     */
    @SafeVarargs
    public static ObjectMap fromMap(Map<String, ?>... data) {
        if(data == null || data.length == 0) return create();
        else {
            ObjectMap objectMap = create();
            for(Map<String, ?> map: data) {
                objectMap.set(map);
            }
            return objectMap;
        }
    }

    /**
     * Create object map if @object is null
     *
     * @param object ObjectMap
     */
    public static ObjectMap ifNull(ObjectMap object) {
        return object == null ? create() : object;
    }

    /**
     * Create object map
     *
     * @param key   ey with which the specified value
     * @param value value to be associated with the specified key
     */
    public static ObjectMap setNew(String key, Object value) {
        return create().set(key, value);
    }

    public static <T> String toJsonString(T object, Set<String> fields) {
        return fromJson(object).get(fields, true).toJson(true);
    }

    public ObjectMap deleteAll() {
        super.clear();
        return this;
    }

    public ObjectMap clearAndSet(ObjectMap map) {
        return deleteAll().set(map);
    }

    /**
     * Associates the specified value with the specified key in this map.
     * If the map previously contained a mapping for the key, the old
     * value is replaced.
     *
     * @param key   key with which the specified value is to be associated
     * @param value value to be associated with the specified key
     */
    public ObjectMap set(String key, Object value) {
        if(value == null) remove(key);
        else super.put(key, value);
        return this;
    }

    /**
     * Associates the specified value with the specified key in this map.
     * If the map previously contained a mapping for the key, the old
     * value is replaced.
     *
     * @param key          key with which the specified value is to be associated
     * @param value        value to be associated with the specified key
     * @param valueDefault if value is null then use valueDefault
     */
    public ObjectMap set(String key, Object value, Object valueDefault) {
        super.put(key, value == null ? valueDefault : value);
        return this;
    }

    /**
     * Update key + value to map
     *
     * @param keys   String[]
     * @param values Object[]
     */
    public ObjectMap set(String[] keys, Object[] values) {
        Asserts.isTrue(keys.length == values.length, "length(keys) <> length(values)");
        IntStream.range(0, keys.length).forEach(i -> set(keys[i], values[i]));
        return this;
    }

    /**
     * Update map if value is not null
     *
     * @param key   key with which the specified value is to be associated
     * @param value value to be associated with the specified key
     */
    public ObjectMap setNotNull(String key, Object value) {
        if (Objects.nonNull(value)) set(key, value);
        return this;
    }

    /**
     * Copies all the mappings from the specified map to this map.
     *
     * @param map – mappings to be stored in this map
     */
    public ObjectMap set(ObjectMap map) {
        if(map != null) super.putAll(map);
        return this;
    }

    /**
     * Copies all the mappings from the specified map to this map.
     *
     * @param map – mappings to be stored in this map
     */
    public ObjectMap set(Map<String, ?> map) {
        Asserts.notNull(map, "@map must be not null.");
        super.putAll(map);
        return this;
    }

    public ObjectMap setIfNotNull(String key, Object value) {
        if (value != null) set(key, value);
        return this;
    }

    public ObjectMap setIfNotNull(String key, Object value, Object defaultValue) {
        Object val = value == null ? defaultValue : value;
        setIfNotNull(key, defaultValue);
        return this;
    }

    /**
     * If the specified key is not already associated with a value (or is mapped to null) associates it with
     * the given value and returns null, else returns the current value.
     */
    public ObjectMap setIfAbsent(Map<String, ?> map) {
        map.forEach(super::putIfAbsent);
        return this;
    }

    public ObjectMap setIfAbsent(String field, Object value) {
        putIfAbsent(field, value);
        return this;
    }

    public <V> V computeIfAbsent(String key, Function<? super String, V> mappingFunction) {
        return (V) super.computeIfAbsent(key, mappingFunction);
    }

    public <T> T computeIfAbsent(String key, Class<T> returnClass, Function<? super String, T> mappingFunction) {
        super.computeIfAbsent(key, mappingFunction);
        return get(key, returnClass);
    }

    /**
     * Convert map to json string
     *
     * @return json string
     */
    public String toJson() {
        return toJson(true);
    }

    /**
     * Convert map to json string
     *
     * @param inline pretty printer configured for ObjectMapper
     * @return json string
     */
    public String toJson(boolean inline) {
        final ObjectMapper mapper = MapperHelper.enablePretty(!inline);
        return MapperHelper.serializeToString(mapper, this);
    }

//    public MultiValueMap<String, String> asMultiValueMap() {
//        MultiValueMap<String, String> valueMap = new LinkedMultiValueMap<>();
//        super.forEach((key, value) -> valueMap.put(key, MapperHelper.toStringList(value)));
//        return valueMap;
//    }

//    /**
//     * Convert map to query
//     *
//     * @return the query
//     */
//    public String toQuery() {
//        return UriComponentsBuilder.newInstance()
//                .queryParams(asMultiValueMap())
//                .build().toUriString().substring(1);
//    }

    /**
     * Applying the given mapping function to the value
     *
     * @param mapper – the mapping function to apply to a value
     */
    public <R> R map(Function<ObjectMap, R> mapper) {
        Asserts.notNull(mapper, "@mapper must be not null.");
        return mapper.apply(this);
    }

    /**
     * Removes the entry for the specified key
     *
     * @param <T> the value type
     * @param key – key whose mapping is to be removed from the map
     * @param def T
     */
    public <T> T removeGet(String key, T def) {
        Asserts.notNull(def, "@def must be not null.");
        return removeGet(key, (Class<T>) def.getClass(), def);
    }

    /**
     * Removes the entry for the specified key
     *
     * @param <T>   the value type
     * @param key   – key whose mapping is to be removed from the map
     * @param clazz the class value cast
     * @param def   T
     */
    public <T> T removeGet(String key, Class<T> clazz, T def) {
        Asserts.notNull(clazz, "@clazz must be not null.");
        Object value = detectObjectValue(key, def);
        super.entrySet().removeIf(e -> Objects.equals(e.getKey(), key));
        return MapperHelper.convert(value, clazz, def);
    }

    /**
     * Change key to other key
     *
     * @param keyOld the key old
     * @param keyNew the key new
     * @return this
     */
    public ObjectMap changeKey(String keyOld, String keyNew) {
        Asserts.isFalse(containsKey(keyNew), "@%s is exist in map.", keyNew);
        if (containsKey(keyOld)) {
            Object object = get(keyOld);
            set(keyNew, object).remove(keyOld);
        }
        return this;
    }

    /**
     * Returns the value to which the specified key is mapped,
     * or defaultValue if this map contains no mapping for the key
     *
     * @param <T>    the value type cast
     * @param key    the key whose associated value is to be returned
     * @param mapper Function<Object, T>
     */
    public <T> T get(String key, Function<Object, T> mapper) {
        Asserts.notNull(mapper, "@mapper");
        return mapper.apply(detectObjectValue(key, null));
    }

    /**
     * Returns the value to which the specified key is mapped,
     * or defaultValue if this map contains no mapping for the key
     *
     * @param <T>         the value type cast
     * @param key         – the key whose associated value is to be returned
     * @param classTarget the class type
     */
    public <T> T get(String key, Class<T> classTarget) {
        return get(key, classTarget, null, null);
    }

    /**
     * Returns the value to which the specified key is mapped,
     * or defaultValue if this map contains no mapping for the key
     *
     * @param <T>          the value type cast
     * @param key          – the key whose associated value is to be returned
     * @param classTarget  the class type
     * @param defaultValue – the default
     */
    public <T> T get(String key, Class<T> classTarget, T defaultValue) {
        return get(key, classTarget, defaultValue, null);
    }

    /**
     * Returns the value to which the specified key is mapped,
     * or defaultValue if this map contains no mapping for the key
     *
     * @param <T>          the value type cast
     * @param key          the key whose associated value is to be returned
     * @param classTarget  the class type
     * @param defaultValue the default
     * @param mapper       callback detect value
     */
    public <T> T get(String key, Class<T> classTarget, T defaultValue, Function<Object, T> mapper) {
        Object targetValue = detectObjectValue(key, defaultValue);
        if (mapper != null) {
            Object value = mapper.apply(targetValue);
            targetValue = Objects.firstNotNull(value, targetValue);
        }
        return MapperHelper.convert(targetValue, classTarget, defaultValue);
    }


    /**
     * Returns map from keys
     *
     * @param keys the all key
     */
    public ObjectMap get(String... keys) {
        return get(Set.of(keys), true);
    }


    /**
     * Returns map from keys
     *
     * @param keys the all key
     */
    public ObjectMap get(Set<String> keys, boolean includeNull) {
        Asserts.notEmpty(keys, "@keys must be not empty.");
        ObjectMap obj = ObjectMap.create();
        Stream<String> stream = keys.stream().filter(Objects::hasLength);
        if(!includeNull) stream = stream.filter(Objects::nonNull);
        stream.forEach(key -> obj.set(key, get(key)));
        return obj;
    }

    /**
     * Returns the object map value to which the specified key is mapped
     *
     * @param key – the key whose associated value is to be returned
     */
    public ObjectMap getMap(String key) {
        return getMap(key, null);
    }

    /**
     * Returns the object map value to which the specified key is mapped
     *
     * @param key   – the key whose associated value is to be returned
     * @param value the default value
     */
    public ObjectMap getMap(String key, ObjectMap value) {
        return get(key, ObjectMap.class, value);
    }

    /**
     * Returns String value
     *
     * @param key field key
     */
    public String getString(String key) {
        return getString(key, null);
    }

    /**
     * Returns String value
     *
     * @param key   field key
     * @param value default value
     */
    public String getString(String key, String value) {
        Object object = detectObjectValue(key, value);
        return Objects.toString(object);
    }

    public String getStringOr(String... keys) {
        return Stream.of(keys).filter(this::containsKey)
                .findFirst().map(this::getString).orElse(null);
    }

    /**
     * Returns Double value
     *
     * @param key field key
     */
    public Double getDouble(String key) {
        return getDouble(key, null);
    }

    /**
     * Returns Double value
     *
     * @param key   field key
     * @param value default value
     */
    public Double getDouble(String key, Double value) {
        Function<Object, Double> function = o -> o instanceof Number num ? num.doubleValue() : null;
        return get(key, Double.class, value, function);
    }

    /**
     * Returns BigDecimal value
     *
     * @param key field key
     */
    public BigDecimal getDecimal(String key) {
        return getDecimal(key, null);
    }

    /**
     * Returns BigDecimal value
     *
     * @param key   field key
     * @param value default value
     */
    public BigDecimal getDecimal(String key, BigDecimal value) {
        return get(key, BigDecimal.class, value);
    }

    /**
     * Returns Boolean value
     *
     * @param key field key
     */
    public Boolean getBoolean(String key) {
        return getBoolean(key, null);
    }

    /**
     * Returns Boolean value
     *
     * @param key   field key
     * @param value default value
     */
    public Boolean getBoolean(String key, Boolean value) {
        Object detectValue = detectObjectValue(key, value);
        return Objects.toBoolean(detectValue);
    }

    /**
     * Returns Integer value
     *
     * @param key String
     */
    public Integer getInteger(String key) {
        return getInteger(key, null);
    }

    /**
     * Returns Integer value
     *
     * @param key   field key
     * @param value default value
     */
    public Integer getInteger(String key, Integer value) {
        return get(key, Integer.class, value, o -> o instanceof Number n ? n.intValue() : null);
    }

    /**
     * Returns Long value
     *
     * @param key String
     */
    public Long getLong(String key) {
        return getLong(key, null);
    }

    /**
     * Returns Long value
     *
     * @param key String
     */
    public Long getLong(String key, Long value) {
        return get(key, Long.class, value, o -> o instanceof Number n ? n.longValue() : null);
    }

    /**
     * Returns {@link LocalTime}
     *
     * @param key String
     */
    public LocalTime getTime(String key) {
        return getTime(key, DateTimeFormatter.ISO_LOCAL_TIME);
    }

    /**
     * Returns {@link LocalTime}
     *
     * @param key     String
     * @param pattern String
     */
    public LocalTime getTime(String key, String pattern) {
        Asserts.notEmpty(pattern, "@pattern must be not blank");
        return getTime(key, DateTimeFormatter.ofPattern(pattern));
    }

    /**
     * Returns {@link LocalTime}
     *
     * @param key       String
     * @param formatter DateTimeFormatter
     */
    public LocalTime getTime(String key, DateTimeFormatter formatter) {
        Asserts.notNull(formatter, "@formatter must be not null");
        Object object = detectObjectValue(key, null);
        return DateHelper.localTime(object, formatter);
    }

    /**
     * Return {@link LocalDate}
     *
     * @param key String
     */
    public LocalDate getDate(String key) {
        return getDate(key, DateTimeFormatter.ISO_LOCAL_DATE);
    }

    /**
     * Return {@link LocalDate}
     *
     * @param key     String
     * @param pattern String
     */
    public LocalDate getDate(String key, String pattern) {
        Asserts.notEmpty(pattern, "@pattern must be not blank");
        return getDate(key, DateTimeFormatter.ofPattern(pattern));
    }

    /**
     * Return {@link LocalDate}
     *
     * @param key       String
     * @param formatter DateTimeFormatter
     */
    public LocalDate getDate(String key, final DateTimeFormatter formatter) {
        Asserts.notNull(formatter, "@formatter must be not null");
        Object object = detectObjectValue(key, null);
        return DateHelper.localDate(object, formatter);
    }

    public LocalDateTime getDateTime(String key, Long div) {
        Object object = detectObjectValue(key, null);
        if (object instanceof Number num) return DateHelper.localDateTime(num.longValue() * div);
        else return getDateTime(key, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
    }

    /**
     * Return {@link LocalDateTime}
     *
     * @param key String
     */
    public LocalDateTime getDateTime(String key) {
        return getDateTime(key, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
    }

    /**
     * Return {@link LocalDateTime}
     *
     * @param key String
     */
    public LocalDateTime getDateTime(String key, String pattern) {
        Asserts.notEmpty(pattern, "@pattern must be not blank");
        return getDateTime(key, DateTimeFormatter.ofPattern(pattern));
    }

    /**
     * Return {@link LocalDateTime}
     *
     * @param key String
     */
    public LocalDateTime getDateTime(String key, DateTimeFormatter formatter) {
        Asserts.notNull(formatter, "@formatter must be not null");
        Object object = detectObjectValue(key, null);
        return DateHelper.localDateTime(object, formatter);
    }

    public List<String> getStringList(String key) {
        return getList(key, String.class);
    }

    public List<ObjectMap> getList(String key) {
        return getList(key, ObjectMap.class);
    }

    /**
     * Cast value to list
     *
     * @param <T>         the value type
     * @param key         the field key
     * @param returnClass Class
     */
    public <T> List<T> getList(String key, Class<T> returnClass) {
        Object object = detectObjectValue(key, new LinkedList<>());
        return Lists.toList(object, returnClass);
    }



    /**
     * Cast value to list
     *
     * @param <T>          the value type
     * @param key          the field key
     * @param returnMapper Function<Object, T>
     */
    public <T> List<T> getList(String key, Function<Object, T> returnMapper) {
        Object object = detectObjectValue(key, null);
        return Lists.toList(object, returnMapper);
    }

    public Stream<ObjectMap> getStream(String key) {
        return getStream(key, ObjectMap.class);
    }

    /**
     * Cast value to stream object
     *
     * @param <T>         the value type
     * @param key         the field key
     * @param returnClass Class<T>
     * @see #getList(String, Class)
     */
    public <T> Stream<T> getStream(String key, Class<T> returnClass) {
        return getList(key, returnClass).stream();
    }

    /**
     * Cast value to stream object
     *
     * @param <T>          the value type
     * @param key          the field key
     * @param returnMapper Function<Object, T>
     * @see #getList(String, Function)
     */
    public <T> Stream<T> getStream(String key, Function<Object, T> returnMapper) {
        return getList(key, returnMapper).stream();
    }

    /**
     * Group data (arrayKey) by mapKey
     *
     * @param arrayKey String
     * @param mapKey   String
     */
    public ObjectMap getStreamAsGroup(String arrayKey, String mapKey) {
        return fromMap(getStream(arrayKey, ObjectMap.class)
                .collect(Collectors.toMap(o -> o.getString(mapKey), o -> o)));
    }

    /**
     * Returns the enum constant of the specified enum class with the specified name
     *
     * @param key       the field map key
     * @param enumClass – the Class object of the enum class from which to return a constant
     * @param value     the default value
     */
    public <T extends Enum<T>> T getEnum(String key, Class<T> enumClass, T value) {
        try {
            String enumString = getString(key, null);
            if (Objects.notBlank(enumString)) {
                return Enum.valueOf(enumClass, enumString);
            }

            Integer enumInt = getInteger(key, null);
            if (Objects.isGreaterThanOrEq(enumInt, 0)) {
                return Objects.enumValueOf(enumClass, enumInt);
            }

            return value;
        } catch (IllegalArgumentException exp) {
            return value;
        }
    }

    /**
     * Returns the enum constant of the specified enum class with the specified name
     *
     * @param key       the field map key
     * @param enumClass – the Class object of the enum class from which to return a constant
     */
    public <T extends Enum<T>> T getEnum(String key, Class<T> enumClass) {
        return getEnum(key, enumClass, null);
    }

    public ObjectMap[] asArray() {
        return new ObjectMap[]{this};
    }


    /**
     * Detect value from field
     *
     * @param field        the key whose associated value is to be returned
     * @param defaultValue – the default mapping of the key
     */
    private Object detectObjectValue(String field, Object defaultValue) {
       /* Asserts.notEmpty(field, "@field must be not blank.");


        if (containsKey(field)) return getOrDefault(field, defaultValue);
        if (!field.contains(".")) return getOrDefault(field, defaultValue);

        String[] parts = field.split("\\.");
        Asserts.allNotEmpty(parts, "@field must be all not empty.");

        ObjectMap targetObj = this;
        for (int i = 0; i < parts.length - 1; i++) {
            String fieldKey = parts[i];
            Object targetVal = targetObj.get(fieldKey);
            if (targetVal == null || targetVal.getClass().isPrimitive()) return defaultValue;
            else targetObj = targetObj.getMap(fieldKey);
        }

        return targetObj == null ? defaultValue
                : targetObj.getOrDefault(parts[parts.length - 1], defaultValue);
        */
        return detectObjectValue2(this, field, defaultValue);
    }

    private Object detectObjectValue2(ObjectMap targetObj, String field, Object defaultValue) {
        Asserts.notEmpty(field, "@field must be not blank.");


        // has key in ObjectMap
        if (targetObj.containsKey(field)) {
            return targetObj.getOrDefault(field, defaultValue);
        }

        // if key not like `.`
        if (!field.contains(".")) {
            return getOrDefault(field, defaultValue);
        }

        int dot = field.indexOf(".");
        String part = field.substring(0, dot);

        // return default if value fail
        Object targetVal = targetObj.get(part);
        if (targetVal == null || targetVal.getClass().isPrimitive() || !(targetVal instanceof Map<?, ?>)) {
            return defaultValue;
        }

        // has map
        targetObj = targetObj.getMap(part);
        String nextField = field.substring(dot + 1);
        return detectObjectValue2(targetObj, nextField, defaultValue);
    }

    public ObjectMap copy() {
        return fromMap(this);
    }

    /**
     * Delete key from map
     *
     * @return this
     */
    public ObjectMap delete(String key) {
        entrySet().removeIf(e -> Objects.equals(e.getKey(), key));
        return this;
    }


    /**
     * Delete key from map and return value of key
     *
     * @param <T>   the value type
     * @param key   the key
     * @param clazz Class
     */
    public <T> T delete(String key, Class<T> clazz) {
        T object = get(key, clazz);
        delete(key);
        return object;
    }
    
    
    /**
     * Delete key and return cast long value
     *
     * @param key the key map
     */
    public ObjectMap deleteToMap(String key) {
        return delete(key, ObjectMap.class);
    }

    /**
     * Delete key and return cast long value
     *
     * @param key the key map
     */
    public Long deleteToLong(String key) {
        return delete(key, Long.class);
    }

    /**
     * Delete key and return cast String value
     *
     * @param key the key map
     */
    public String deleteToString(String key) {
        return delete(key, String.class);
    }

    /**
     * Delete key and return cast Boolean value
     *
     * @param key the key map
     */
    public Boolean deleteToBoolean(String key) {
        return delete(key, Boolean.class);
    }

    public ObjectMap add(String field, Object value) {
        Asserts.notNull(value, "@value must be not null");
        List<Object> list = getStream(field, value.getClass()).collect(Collectors.toList());
        list.add(value);
        return set(field, value);
    }

    /**
     * Returns this
     */
    public ObjectMap deleteKeys(String[] fields) {
        Stream.of(fields).filter(Objects::nonNull).forEach(this::delete);
        return this;
    }


    public <T> T asObject(Class<T> objectClass) {
        return MapperHelper.convert(this, objectClass);
    }


    public void putAllIfAbsent(ObjectMap objectMap) {
        if (objectMap != null) objectMap.forEach(this::putIfAbsent);
    }

//    public <T> T assignToStaticMethod(Class<T> objectClass, String statusMethod) throws Throwable {
//        Class[] parameterCls = new Class[] { ObjectMap.class } ;
//        MethodHandle handle = ClassHelper.findStaticMethod(objectClass, statusMethod, parameterCls);
//        if(handle == null) {
//            String clsPkg = objectClass.getName();
//            String msg = "Don't find static method %s.%s(ObjectMap)";
//            throw Objects.newNoSuchMethod(msg, clsPkg, statusMethod);
//        }
//
//        return (T) handle.invokeWithArguments(this);
//    }

    public <T> T updateTo(T object) {
        MapperHelper.updateToMap(object, this);
        return object;
    }

    public void replaceKey(String keyOld, String keyNew) {
        if(containsKey(keyOld)) {
            set(keyNew, get(keyOld));
            delete(keyOld);
        }
    }

    public static ObjectMap clone(Map<String, ?> map) {
        return map == null ? null : fromMap(map);
    }
}