package vn.conyeu.javafx.utils;

import javafx.util.StringConverter;

public final class Converters {

    public static <T> StringConverter<T> defaultConverter() {
        return SimpleStringConverter.getInstance();
    }

    private static class SimpleStringConverter<T> extends StringConverter<T> {
        static SimpleStringConverter holder;

        static SimpleStringConverter getInstance() {
            if (holder == null) holder = new SimpleStringConverter();
            return holder;
        }

        @Override
        public String toString(T object) {
            return object == null ? null : object.toString();
        }


        @Override
        public T fromString(String string) {
            throw new UnsupportedOperationException();
        }
    }


}