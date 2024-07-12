package vn.conyeu.commons.utils;

import java.sql.Time;
import java.sql.Timestamp;
import java.text.ParsePosition;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.time.temporal.ChronoField;

public final class DateHelper {
    public static final String ISO_DATE = "yyyy-MM-dd'T'HH:mm:ss";
    static final DateTimeFormatter ISO_LOCAL_TIME = DateTimeFormatter.ISO_LOCAL_TIME;
    static final DateTimeFormatter ISO_LOCAL_DATE = DateTimeFormatter.ISO_LOCAL_DATE;
    static final DateTimeFormatter ISO_LOCAL_DATE_TIME = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    /**
     * Convert epochMilli to {@link LocalDateTime}
     *
     * @param epochMilli the number of milliseconds from 1970-01-01T00:00:00Z
     * @return LocalDateTime
     */
    public static LocalDateTime localDateTime(long epochMilli) {
        return LocalDateTime.ofInstant(Instant.ofEpochMilli(epochMilli), ZoneId.systemDefault());
    }

    /**
     * Convert text to datetime
     *
     * @param text the text to parse such as "2007-12-03T10:15:30"
     * @return the parsed local date-time
     */
    public static LocalDateTime localDateTime(String text) {
        if (!Objects.hasLength(text)) return null;
        else return LocalDateTime.parse(text);
    }

    public static LocalDateTime localDateTime(Calendar c) {
        return LocalDateTime.ofInstant( c.toInstant(), c.getTimeZone().toZoneId() );
    }

    public static LocalDateTime localDateTime(Date date) {
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
    }

    public static LocalDateTime localDateTime(Instant instant) {
        return LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
    }

    public static LocalDateTime localDateTime(Object object, String datePattern) {
        return localDateTime(object, DateTimeFormatter.ofPattern(datePattern));
    }

    public static LocalDateTime localDateTime(Object object, DateTimeFormatter formatter) {
        if (Objects.isEmpty(object)) return null;
        if (object instanceof LocalDateTime dt) return dt;
        if (object instanceof Number num) return localDateTime(num.longValue());
        if (object instanceof LocalDate ld) return LocalDateTime.of(ld, LocalTime.MIN);
        if (object instanceof String str) return LocalDateTime.parse(str, formatter);
        if(object instanceof GregorianCalendar calendar) return calendar.toZonedDateTime().toLocalDateTime();
        else throw Asserts.invalidValueType(LocalDateTime.class);
    }

    /**
     * Convert epochMilli to {@link LocalDate}
     *
     * @param epochMilli the number of milliseconds from 1970-01-01
     * @return LocalDate
     */
    public static LocalDate localDate(long epochMilli) {
        return LocalDate.ofInstant(Instant.ofEpochMilli(epochMilli), ZoneId.systemDefault());
    }

    public static LocalDate localDate(Date date) {
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }

    /**
     * Convert object to LocalDate
     *
     * @param object      the object to convert
     * @param datePattern the date pattern
     * @see #localDate(Object, DateTimeFormatter)
     */
    public static LocalDate localDate(Object object, String datePattern) {
        return localDate(object, DateTimeFormatter.ofPattern(datePattern));
    }

    /**
     * Convert object to LocalDate
     *
     * @param object    the object to convert
     * @param formatter the formatter date
     */
    public static LocalDate localDate(Object object, DateTimeFormatter formatter) {
        if (Objects.isEmpty(object)) return null;
        if (object instanceof Number num) return localDate(num.longValue());
        if (object instanceof LocalDate ld) return ld;
        if (object instanceof LocalDateTime dt) return dt.toLocalDate();
        if (object instanceof String str) return localDate(str, formatter);
        else throw Asserts.invalidValueType(LocalDate.class);
    }

    public static LocalDate localDate(String string) {
        Asserts.notEmpty(string, "@string must be not empty.");
        return LocalDate.parse(string);
    }

    /**
     * Parse string to date with format
     *
     * @param string    the date string
     * @param formatter the date formatter
     */
    private static LocalDate localDate(String string, DateTimeFormatter formatter) {
        Asserts.notNull(formatter, "@formatter");
        return LocalDate.parse(string, formatter);
    }

    public static LocalTime localTime(long epochMilli) {
        return LocalTime.ofInstant(Instant.ofEpochMilli(epochMilli), ZoneId.systemDefault());
    }

    /**
     * Convert text to datetime
     *
     * @param text the text to parse such as "2007-12-03T10:15:30"
     * @return the parsed local date-time
     */
    public static LocalTime localTime(String text) {
        if (!Objects.hasLength(text)) return null;
        else return LocalTime.parse(text);
    }

    public static LocalTime localTime(Object object, String timePattern) {
        return localTime(object, DateTimeFormatter.ofPattern(timePattern));
    }

    public static LocalTime localTime(Object object, DateTimeFormatter formatter) {
        if (Objects.isEmpty(object)) return null;
        if (object instanceof Number num) return localTime(num.longValue());
        if (object instanceof LocalTime lt) return lt;
        if (object instanceof LocalDateTime dt) return dt.toLocalTime();
        if (object instanceof String str) return LocalTime.parse(str, formatter);
        else throw Asserts.invalidValueType(LocalTime.class);
    }

    public static LocalTime localTime(Date date) {
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalTime();
    }

    public static LocalTime localTime(Timestamp ts) {
        return LocalDateTime.ofInstant( ts.toInstant(), ZoneId.systemDefault() ).toLocalTime();
    }

    public static LocalTime localTime(Time time) {
        final LocalTime localTime = time.toLocalTime();
        final long millis = time.getTime() % 1000;
        if ( millis == 0 ) return localTime;
        return localTime.with( ChronoField.NANO_OF_SECOND, millis * 1_000_000L );
    }

    /**
     * Convert datetime to iso string `yyyy-MM-ddTHH:mm:ss.Z`
     *
     * @param dateTime LocalDateTime
     * @return the formatted date-time string
     * @see DateTimeFormatter#ISO_LOCAL_DATE_TIME
     */
    public static String toStringIso(LocalDateTime dateTime) {
        if (Objects.isNull(dateTime)) return null;
        else return dateTime.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
    }

    /**
     * Convert datetime to iso string `yyyy-MM-dd
     *
     * @param date LocalDate
     * @return the formatted date string
     * @see DateTimeFormatter#ISO_LOCAL_DATE
     */
    public static String toStringIso(LocalDate date) {
        if (Objects.isNull(date)) return null;
        else return date.format(DateTimeFormatter.ISO_LOCAL_DATE);
    }

    /**
     * Convert datetime to iso string `HH:mm:ss.Z`
     *
     * @param time LocalTime
     * @return the formatted time string
     * @see DateTimeFormatter#ISO_LOCAL_TIME
     */
    public static String toStringIso(LocalTime time) {
        if (Objects.isNull(time)) return null;
        else return time.format(ISO_LOCAL_TIME);
    }

    public static long epochSecond() {
       return epochSecond(LocalDateTime.now());
    }

    public static long epochSecond(LocalDateTime dateTime) {
        return dateTime.atZone(ZoneId.systemDefault())
                .toInstant().getEpochSecond();
    }

    public static long epochMilli() {
        return epochSecond(LocalDateTime.now());
    }

    public static long epochMilli(LocalDateTime dateTime) {
        return dateTime.atZone(ZoneId.systemDefault())
                .toInstant().toEpochMilli();
    }

    public static Object localTime(DateTimeFormatter dtf, String source, ParsePosition pos) {
        TemporalAccessor accessor = dtf.parse(source, pos);
        return LocalTime.from(accessor);
    }

    public static String format(LocalDate date, String pattern) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern(pattern);
        return date.format(dtf);
    }

    public static String formatIsoDate(Date date) {
        return localDate(date).format(DateTimeFormatter.ISO_LOCAL_DATE);
    }

    public static String formatIsoTimestamp(Timestamp timestamp) {
        return localDateTime(timestamp).format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
    }

    public static String formatIsoTime(Time time) {
        return localTime(time).format(DateTimeFormatter.ISO_LOCAL_TIME);
    }

    public static String formatIsoDate(LocalDate date) {
        return date == null ? null : ISO_LOCAL_DATE.format(date);
    }

    public static String formatIsoTime(LocalTime time) {
        return time == null ? null : ISO_LOCAL_TIME.format(time);
    }

    public static String formatIsoDateTime(LocalDateTime dateTime) {
        return dateTime == null ? null : ISO_LOCAL_DATE_TIME.format(dateTime);
    }

    public static LocalDate localDate(Calendar calendar) {
        return LocalDateTime.ofInstant( calendar.toInstant(), calendar.getTimeZone().toZoneId() ).toLocalDate();
    }

    public static LocalTime localTime(Calendar calendar) {
        return LocalDateTime.ofInstant( calendar.toInstant(), calendar.getTimeZone().toZoneId() ).toLocalTime();
    }
}