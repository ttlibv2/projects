package vn.conyeu.common.converter;

import com.fasterxml.jackson.core.type.TypeReference;
import jakarta.persistence.Converter;
import vn.conyeu.common.converter.base.ConvertAttrDb;
import vn.conyeu.commons.beans.ObjectMap;
import vn.conyeu.commons.utils.MapperHelper;

import java.util.List;
import java.util.function.Function;

@Converter(autoApply = true)
public class ListMapToString extends ConvertAttrDb<List<ObjectMap>> {

    @Override
    protected Function<List<ObjectMap>, String> objectToString() {
        return MapperHelper::serializeToString;
    }

    @Override
    protected Function<String, List<ObjectMap>> stringToObject() {
        return string -> MapperHelper.readValue(string, new TypeReference<List<ObjectMap>>(){});
    }
}