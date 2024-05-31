package vn.conyeu.common.converter.base;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import vn.conyeu.commons.utils.Objects;

import java.util.function.Function;

@Converter
public abstract class ConvertAttrDb<X> implements AttributeConverter<X, String> {

    protected abstract Function<X, String> objectToString();

    protected abstract Function<String, X> stringToObject();

    public final String convertToDatabaseColumn(X object) {
        if(Objects.isNull(object)) return null;
        return objectToString().apply(object);
    }

    public final X convertToEntityAttribute(String string) {
        return Objects.isEmpty(string) ? null : stringToObject().apply(string);
    }
}