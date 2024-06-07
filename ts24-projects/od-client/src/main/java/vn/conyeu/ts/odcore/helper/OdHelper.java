package vn.conyeu.ts.odcore.helper;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public final class OdHelper {
    public static final DateTimeFormatter ISO_DATETIME = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static boolean isNull(Object object) {
        return object == null || "false".equals(object);
    }

    public static String toString(Object object) {
        return isNull(object) ? null : object.toString();
    }

    public static LocalDateTime toDateTime(Object date) {
        return isNull(date) ? null : LocalDateTime.parse(date.toString(), ISO_DATETIME);
    }

    public static Object[] createList(Object...args) {
        return new Object[] {args};
    }
}