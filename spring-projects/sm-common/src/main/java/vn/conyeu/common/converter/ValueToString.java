package vn.conyeu.common.converter;

import vn.conyeu.common.converter.base.ObjectToString;
import vn.conyeu.common.domain.ValueDb;
import vn.conyeu.commons.utils.MapperHelper;
import vn.conyeu.commons.utils.Objects;

import java.util.function.Function;

public class ValueToString extends ObjectToString<ValueDb> {

    public ValueToString() {
        super(ValueDb.class);
    }

    @Override
    protected Function<ValueDb, String> objectToString() {
        return object -> object.getJavaType() + "::" + MapperHelper.serializeToString(object.getData());
    }

    @Override
    protected Function<String, ValueDb> stringToObject() {
        return str -> {
            String[] segments = str.split("::");
            if(segments.length != 2) {
                String msg = "The value `%s` invalid format value (javaType::stringData)";
                throw Objects.newIllegal(msg, str);
            }

            Class javaType = ValueDb.forName(segments[0]);
            Object data = MapperHelper.convert(segments[1], javaType);
            return new ValueDb(javaType, data);
        };
    }

}