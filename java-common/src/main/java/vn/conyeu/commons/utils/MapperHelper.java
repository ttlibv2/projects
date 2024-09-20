package vn.conyeu.commons.utils;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.apache.commons.lang3.ClassUtils;
import vn.conyeu.commons.beans.ObjectMap;
import vn.conyeu.commons.exception.ConvertException;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.function.Consumer;
import java.util.function.Supplier;

public final class MapperHelper {
    static ObjectMapper mapper = defaultBuilder().build();

    public static void install(Consumer<JsonMapper.Builder> consumer) {
        JsonMapper.Builder builder = defaultBuilder();
        consumer.accept(builder);
        mapper = builder.build();
    }

    /**
     * Method for creating a new {@link ObjectMapper} instance that
     * has same initial configuration as this instance.
     */
    public static ObjectMapper mapper() {
        return mapper.copy();
    }

    public static ObjectMapper enablePretty(boolean pretty) {
        ObjectMapper mapper = mapper();
        return pretty ? mapper.enable(SerializationFeature.INDENT_OUTPUT)
                : mapper.disable(SerializationFeature.INDENT_OUTPUT);
    }

    /**
     * @param valueToUpdate Object to update
     * @param overrides     Object to conceptually serialize and merge into value to update
     * @throws IllegalArgumentException if there are structural incompatibilities that prevent update.
     */
    public static <T> T update(T valueToUpdate, Object overrides) {
        try {
            return mapper.updateValue(valueToUpdate, overrides);
        } catch (JsonMappingException ex) {
            throw new IllegalArgumentException(ex);
        }
    }

    public static <T> T updateToMap(T valueToUpdate, Object overrides, String... excludeField) {
        ObjectMap mapOverride = overrides instanceof  ObjectMap map ? map : ObjectMap.fromJson(overrides);
        if (Objects.notEmpty(excludeField)) mapOverride.deleteKeys(excludeField);
        return update(valueToUpdate, mapOverride);
    }

    /**
     * Convert object to string
     *
     * @param obj the object to string
     * @see #serializeToString(ObjectMapper, Object)
     */
    public static String serializeToString(Object obj) {
      return serializeToString(mapper, obj);
    }

    /**
     * Convert object to string
     *
     * @param obj the object to string
     * @see #serializeToString(ObjectMapper, Object)
     */
    public static String serializeToString(Object obj, boolean pretty) {
        ObjectMapper mapperNew;
        if(pretty) mapperNew = mapper.copy().enable(SerializationFeature.INDENT_OUTPUT);
        else mapperNew = mapper().copy().disable(SerializationFeature.INDENT_OUTPUT);
        return serializeToString(mapperNew, obj);
    }

    /**
     * Convert object to string
     *
     * @param mapper the object mapper
     * @param obj    the object to string
     */
    public static String serializeToString(ObjectMapper mapper, Object obj) {
        try {
            if (obj == null) return null;
            else if (obj instanceof String str) return str;
            else if (Classes.isPrimitiveOrWrapper(obj.getClass())) return obj.toString();
            else if(obj instanceof LocalDate ld) return DateHelper.toStringIso(ld);
            else if(obj instanceof LocalDateTime ld) return DateHelper.toStringIso(ld);
            else if(obj instanceof LocalTime ld) return DateHelper.toStringIso(ld);
            else return mapper.writeValueAsString(obj);
        }//
        catch (JsonProcessingException e) {
            throw new ConvertException("MapperHelper.asText(Object)", e);
        }
    }

    /**
     * Convert value to object
     *
     * @param <T>         class type
     * @param fromValue   Object
     * @param classTarget Class
     */
    public static <T> T convert(Object fromValue, Class<T> classTarget) {
        T valueDef = ClassUtils.isPrimitiveOrWrapper(classTarget) ? null : Objects.newInstanceClass(classTarget);
        return convertImpl(fromValue, classTarget, () -> valueDef, true);
    }

    /**
     * Convert value to object
     *
     * @param <T>         class type
     * @param object      Object
     * @param classTarget Class
     * @param defaultVal  T
     */
    public static <T> T convert(Object object, Class<T> classTarget, T defaultVal) {
        return convertImpl(object, classTarget, () -> defaultVal, false);
    }


    static <T> T convertImpl(Object source, Class<T> classTarget, Supplier<T> defaultValue, boolean hasThrow) {
        try {
            if (Objects.isNull(source)) return defaultValue.get();
            if (classTarget.isInstance(source)) return classTarget.cast(source);
            else return mapper.convertValue(convert(source), classTarget);
        } //
        catch (IllegalArgumentException e) {
            if (!hasThrow) return defaultValue.get();
            else {
                String msg = String.format("xay ra loi convert -> %s", e.getMessage());
                throw new ConvertException(msg, e);
            }
        }
    }

    static Object convert(Object object) {
        if (Objects.isEmpty(object)) return object;
        if (object.getClass().isPrimitive()) return object;
        if (object instanceof String str) {
            try {
                return mapper.readTree(str);
            } catch (Exception ex) {
                return object;
            }
        }
        return object;
    }

    static JsonMapper.Builder defaultBuilder() {
        return JsonMapper.builder().addModule(new JavaTimeModule())
                .configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true)
                .configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true)
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                .disable(SerializationFeature.FAIL_ON_EMPTY_BEANS)
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                .enable(SerializationFeature.INDENT_OUTPUT)
                .serializationInclusion(JsonInclude.Include.NON_NULL)
                .propertyNamingStrategy(PropertyNamingStrategies.LOWER_CAMEL_CASE);
    }


    public static <T> T readValue(File src, TypeReference<T> valueTypeRef) throws IOException {
            return mapper().readValue(src, valueTypeRef);
    }

    public static <T> T readValue(String jsonContent, TypeReference<T> valueTypeRef) throws ConvertException {
        try {
            return mapper().readValue(jsonContent, valueTypeRef);
        }catch (IOException exp) {
            throw new ConvertException(exp.getMessage(), exp);
        }
    }

    public static <T> T readValue(InputStream src, Class<T> valueType) throws IOException {
            return mapper().readValue(src, valueType);
    }


}