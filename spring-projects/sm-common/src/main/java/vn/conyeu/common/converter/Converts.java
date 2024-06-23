package vn.conyeu.common.converter;

import jakarta.persistence.Converter;
import vn.conyeu.common.domain.ValueDb;
import vn.conyeu.commons.beans.ObjectMap;
import vn.conyeu.commons.utils.DateHelper;
import vn.conyeu.commons.utils.MapperHelper;
import vn.conyeu.commons.utils.Objects;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.function.Function;

public final class Converts {
    @Converter(autoApply = true)
    public static class MapString extends ObjectToString<ObjectMap> {

        public MapString() {
            super(ObjectMap.class);
        }


    }

    @Converter(autoApply = true)
    public static class DateTimeToString extends ObjectToString<LocalDateTime> {

        public DateTimeToString() {
            super(LocalDateTime.class);
        }

        @Override
        protected Function<LocalDateTime, String> objectToString() {
            return DateHelper::toStringIso;
        }

        @Override
        protected Function<String, LocalDateTime> stringToObject() {
            return DateHelper::localDateTime;
        }

    }

    @Converter(autoApply = true)
    public static class DateToString extends ObjectToString<LocalDate> {

        public DateToString() {
            super(LocalDate.class);
        }

        @Override
        protected Function<LocalDate, String> objectToString() {
            return DateHelper::toStringIso;
        }

        @Override
        protected Function<String, LocalDate> stringToObject() {
            return DateHelper::localDate;
        }

    }

    @Converter(autoApply = true)
    public static class ListLongToString extends ListToString<Long> {

        public ListLongToString() {
            super(Long.class);
        }
    }

    @Converter(autoApply = true)
    public static class TimeToString extends ObjectToString<LocalTime> {

        public TimeToString() {
            super(LocalTime.class);
        }

        @Override
        protected Function<LocalTime, String> objectToString() {
            return DateHelper::toStringIso;
        }

        @Override
        protected Function<String, LocalTime> stringToObject() {
            return DateHelper::localTime;
        }

    }

    public static class ValueToString extends ObjectToString<ValueDb> {

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

    @Converter(autoApply = true)
    public static class StringList extends ListToString<String> {
        public StringList() {
            super(String.class);
        }
    }

    @Converter(autoApply = true)
    public static class StringSet extends SetToString<String> {
        public StringSet() {
            super(String.class);
        }

        public StringSet(String delimiter) {
            super(String.class, delimiter);
        }

        public StringSet(Set<String> fields) {
            super(String.class, fields);
        }

        public StringSet(String delimiter, Set<String> fields) {
            super(String.class, delimiter, fields);
        }

    }
}
