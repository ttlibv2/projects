package vn.conyeu.common.converter;

import jakarta.persistence.Converter;
import vn.conyeu.common.converter.base.ObjectToString;
import vn.conyeu.commons.utils.DateHelper;

import java.time.LocalTime;
import java.util.function.Function;

@Converter(autoApply = true)
public class TimeToString extends ObjectToString<LocalTime> {

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