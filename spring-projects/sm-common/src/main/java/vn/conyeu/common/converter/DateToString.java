package vn.conyeu.common.converter;

import jakarta.persistence.Converter;
import vn.conyeu.common.converter.base.ObjectToString;
import vn.conyeu.commons.utils.DateHelper;

import java.time.LocalDate;
import java.util.function.Function;

@Converter(autoApply = true)
public class DateToString extends ObjectToString<LocalDate> {

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