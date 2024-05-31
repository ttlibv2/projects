package vn.conyeu.common.converter;

import jakarta.persistence.Converter;
import vn.conyeu.common.converter.base.ObjectToString;
import vn.conyeu.commons.utils.DateHelper;

import java.time.LocalDateTime;
import java.util.function.Function;

@Converter(autoApply = true)
public class DateTimeToString extends ObjectToString<LocalDateTime> {

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