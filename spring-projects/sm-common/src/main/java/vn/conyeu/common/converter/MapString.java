package vn.conyeu.common.converter;

import jakarta.persistence.Converter;
import vn.conyeu.common.converter.base.ObjectToString;
import vn.conyeu.commons.beans.ObjectMap;

@Converter(autoApply = true)
public class MapString extends ObjectToString<ObjectMap> {
    public MapString() {
        super(ObjectMap.class);
    }
}