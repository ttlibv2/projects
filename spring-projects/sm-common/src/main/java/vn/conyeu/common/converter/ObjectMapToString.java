package vn.conyeu.common.converter;

import jakarta.persistence.Converter;
import vn.conyeu.common.converter.base.ObjectToString;
import vn.conyeu.commons.beans.ObjectMap;

@Converter(autoApply = true)
public class ObjectMapToString extends ObjectToString<ObjectMap> {
    public ObjectMapToString() {
        super(ObjectMap.class);
    }
}