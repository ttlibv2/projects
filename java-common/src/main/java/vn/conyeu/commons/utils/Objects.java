package vn.conyeu.commons.utils;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.output.StringBuilderWriter;
import org.apache.commons.lang3.SerializationUtils;
import org.apache.commons.lang3.StringUtils;
//import org.apache.commons.validator.routines.UrlValidator;
import vn.conyeu.commons.beans.MimeMappings;

import java.io.*;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URLEncoder;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.text.DecimalFormat;
import java.text.Normalizer;
import java.text.ParseException;
import java.time.Duration;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public final class Objects {
    private static final String[] EMPTY_STRING_ARRAY = {};

    public static boolean isVoid(Type type) {
        return void.class.equals(type);
    }

    public static boolean isNull(Object object) {
        return object == null;
    }

    /**
     * Returns true if the provided reference is non-null
     */
    public static boolean nonNull(Object obj) {
        return obj != null;
    }

    public static boolean isEmpty(Collection<?> obj) {
        return obj == null || obj.isEmpty();
    }

    /**
     * Return {@code true} if the supplied Map is {@code null} or empty.
     * Otherwise, return {@code false}.
     * @param map the Map to check
     * @return whether the given Map is empty
     */
    public static boolean isEmpty(Map<?, ?> map) {
        return (map == null || map.isEmpty());
    }

    /**
     * Determine whether the given object is empty.
     */
    public static boolean isEmpty(Object object) {
        if (object == null) return true;
        if (object instanceof Properties prop) return prop.isEmpty();
        if (object instanceof CharSequence c) return c.length() == 0;
        if (object instanceof Map m) return m.isEmpty();
        if (object instanceof Collection c) return c.isEmpty();
        if (object.getClass().isArray()) return Array.getLength(object) == 0;
        else return false;
    }

    public static boolean isEmpty(Object[] objects) {
        return isNull(objects) || objects.length == 0;
    }

    public static boolean notEmpty(Object obj) {
        return !isEmpty(obj);
    }

    public static boolean notEmpty(Collection obj) {
        return !isEmpty(obj);
    }

    public static boolean notEmpty(Map map) {
        return map != null && !map.isEmpty();
    }

    /**
     * Determine whether the given object is an array:
     * either an Object array or a primitive array.
     *
     * @param obj the object to check
     */
    public static boolean isArray(Object obj) {
        return (obj != null && obj.getClass().isArray());
    }


    /**
     * Check whether the given {@code String} contains actual <em>text</em>.
     */
    public static boolean hasText(String str) {
        return hasLength(str) && containsText(str);
    }

    /**
     * Check whether the given {@code CharSequence} contains actual <em>text</em>.
     */
    public static boolean hasText(CharSequence str) {
        return hasLength(str) && containsText(str);
    }

    /**
     * Check that the given {@code CharSequence} is neither {@code null} nor
     * of length 0.
     */
    public static boolean hasLength(CharSequence str) {
        return str != null && str.length() > 0;
    }

    /**
     * Check that the given {@code String} is neither {@code null} nor of length 0.
     */
    public static boolean hasLength(String str) {
        return str != null && !str.isEmpty();
    }


    public static boolean containsText(CharSequence str) {
        int strLen = str.length();
        for (int i = 0; i < strLen; i++) {
            if (!Character.isWhitespace(str.charAt(i))) {
                return true;
            }
        }
        return false;
    }


    public static <E, C extends Collection<E>> C toCollection(String text, String delimiter, Supplier<C> supplierCreate, Function<String, E> convert) {
        Asserts.allNotNull(convert, supplierCreate);
        if(isBlank(text)) return supplierCreate.get();
        else return Stream.of(text.split(delimiter)).map(convert).collect(Collectors.toCollection(supplierCreate));
    }

    public static <E> List<E> toArrayList(String text, String delimiter, Function<String, E> convert) {
        return toCollection(text, delimiter, ArrayList::new, convert);
    }

    public static <E> Set<E> toHashSet(String text, String delimiter, Function<String, E> convert) {
        return toCollection(text, delimiter, HashSet::new, convert);
    }

    /**
     * Convert string to array string
     *
     * @param text      string
     * @param delimiter string
     */
    public static List<String> toArrayList(String text, String delimiter) {
        if (hasText(text)) return Arrays.asList(text.split(delimiter));
        else return new LinkedList<>();
    }

    /**
     * Convert array string to string
     *
     * @param list      List<String>
     * @param delimiter string
     */
    public static String toString(List<String> list, String delimiter) {
        if (isEmpty(list)) return null;
        else return String.join(delimiter, list);
    }

    public static String ifNull(String message, Supplier<String> messageFnc) {
        if (hasText(message)) return message;
        else return messageFnc.get();
    }

    public static <T> T createIfNull(T object, Supplier<T> funcCreateNew) {
        return object == null ? funcCreateNew.get() : object;
    }

    /**
     * Returns {@code true} if the arguments are equal to each other
     * and {@code false} otherwise.
     *
     * @param a an object
     * @param b an object to be compared with {@code a} for equality
     * @return {@code true} if the arguments are equal to each other and {@code false} otherwise
     */
    public static boolean equals(Object a, Object b) {
        return java.util.Objects.equals(a, b);
    }

    /**
     * Compares this {@code String} to another {@code String}, ignoring case
     * considerations.  Two strings are considered equal ignoring case if they
     * are of the same length and corresponding Unicode code points in the two
     * strings are equal ignoring case.
     */
    public static boolean equalsIgnoreCase(String a, String b) {
        return equals(a, b) || (a != null && a.equalsIgnoreCase(b));
    }

    /**
     * Returns {@code true} if the arguments are deeply equal to each other
     * and {@code false} otherwise.
     * <p>
     * Two {@code null} values are deeply equal.  If both arguments are
     * arrays, the algorithm in {@link Arrays#deepEquals(Object[],
     * Object[]) Arrays.deepEquals} is used to determine equality.
     * Otherwise, equality is determined by using the {@link
     * Object#equals equals} method of the first argument.
     *
     * @param a an object
     * @param b an object to be compared with {@code a} for deep equality
     * @return {@code true} if the arguments are deeply equal to each other
     * and {@code false} otherwise
     * @see Arrays#deepEquals(Object[], Object[])
     * @see java.util.Objects#equals(Object, Object)
     */
    public static boolean deepEquals(Object a, Object b) {
        return java.util.Objects.deepEquals(a, b);
    }

    public static String formatString(String string, Object... args) {
        if (args == null || args.length == 0 || string == null) return string;
        else return String.format(string, args);
    }

    /**
     * Returns the index of the first character in this string that contains a character in
     * [delimiters]. Returns endIndex if there is no such character.
     */
    public static int delimiterOffset(String string, String delimiters, int startIndex) {
        final int endIndex = string.length();
        return delimiterOffset(string, delimiters, startIndex, endIndex);
    }

    /**
     * Returns the index of the first character in this string that contains a character in
     * [delimiters]. Returns endIndex if there is no such character.
     */
    public static int delimiterOffset(String string, String delimiters, int startIndex, int endIndex) {
        final IntStream chars = delimiters.chars();
        for (int i = startIndex; i < endIndex; i++) {
            final int index = i;
            final Function<Integer, Boolean> fnc = s -> s == string.charAt(index);
            if (chars.anyMatch(fnc::apply)) return i;
        }
        return endIndex;
    }

    /**
     * Returns the index of the first character in this string that is [delimiter]. Returns [endIndex]
     * if there is no such character.
     */
    public static int delimiterOffset(String string, char delimiter, int startIndex) {
        return delimiterOffset(string, delimiter, startIndex, string.length());
    }

    /**
     * Returns the index of the first character in this string that is [delimiter]. Returns [endIndex]
     * if there is no such character.
     */
    public static int delimiterOffset(String string, char delimiter, int startIndex, int endIndex) {
        for (int i = startIndex; i < endIndex; i++) {
            if (string.charAt(i) == delimiter) return i;
        }
        return endIndex;
    }

    /**
     * Get the field represented by the supplied {@link Field field object} on the
     * specified {@link Object target object}. In accordance with {@link Field#get(Object)}
     * semantics, the returned value is automatically wrapped if the underlying field
     * has a primitive type.
     * <p>Thrown exceptions are handled via a call to {@link Asserts#handleReflectionException(Exception)}.
     *
     * @param field  the field to get
     * @param target the target object from which to get the field
     *               (or {@code null} for a static field)
     * @return the field's current value
     */
    public static Object getField(Field field, Object target) {
        try {
            return field.get(target);
        } catch (IllegalAccessException ex) {
            Asserts.handleReflectionException(ex);
            return null;
        }
    }


    public static <A> List<A> getNonNull(List<A> list) {
        return list.stream().filter(Objects::nonNull).toList();
    }

    public static Boolean toBoolean(Object value) {
        if (value == null) return null;
        if (value instanceof CharSequence sequence) {
            String str = sequence.toString();
            if ("true".equalsIgnoreCase(str)) return true;
            if ("false".equalsIgnoreCase(str)) return false;
            if ("1".equalsIgnoreCase(str)) return true;
            if ("0".equalsIgnoreCase(str)) return false;
            if ("t".equalsIgnoreCase(str)) return true;
            if ("f".equalsIgnoreCase(str)) return false;
            else {
                String msg = "The value type '%s' not format boolean";
                throw newIllegal(msg, value.getClass());
            }
        }

        if (value instanceof Boolean bool) return bool;
        if (value instanceof Number num) {
            int numBool = num.intValue();
            if (numBool == 1) return true;
            if (numBool == 0) return false;
        }

        String msg = "The value type '%s' not format boolean";
        throw newIllegal(msg, value.getClass());
    }


    public static DateTimeFormatter patternDateTime(String pattern, DateTimeFormatter dtf) {
        return hasLength(pattern) ? DateTimeFormatter.ofPattern(pattern) : dtf;
    }

    public static <E> int findListIndex(List<E> list, Function<E, Boolean> predicate) {
        return IntStream.range(0, list.size())
                .filter(i -> predicate.apply(list.get(i)))
                .findFirst().orElse(-1);
    }

    @SafeVarargs
    public static <E> boolean anyEquals(E object, E... items) {
        Asserts.notNull(object, "@object");
        return Stream.of(items).anyMatch(p -> object == p);
    }

    @SafeVarargs
    public static <E> boolean allEquals(E object, E... items) {
        Asserts.notNull(object, "@object");
        return Stream.of(items).allMatch(p -> object == p);
    }

    /**
     * Tests if this string starts with the specified prefix.
     *
     * @param prefix the prefix.
     * @return {@code true} if the character sequence represented by the
     * argument is a prefix of the character sequence represented by
     * this string; {@code false} otherwise.
     * Note also that {@code true} will be returned if the
     * argument is an empty string or is equal to this
     * {@code String} object as determined by the
     * {@link #equals(Object)} method.
     */
    public static boolean startsWith(String string, String prefix) {
        return allNotNull(string, prefix) && string.startsWith(prefix);
    }

    public static boolean allNotNull(Object... objects) {
        return Stream.of(objects).noneMatch(Objects::isNull);
    }

    @SafeVarargs
    public static <T> T firstIfNull(T... items) {
        return Stream.of(items).filter(Objects::nonNull).findFirst().orElse(null);
    }

    @SafeVarargs
    public static String firstNonBlankSupplier(String first, Supplier<String>... last) {
        if (notBlank(first)) return first;
        for (Supplier<String> consumer : last) {
            String string = consumer.get();
            if (notBlank(string)) return string;
        }
        return first;
    }

    @SafeVarargs
    public static <T> T firstNonNullSupplier(T first, Supplier<T>... last) {
        if (nonNull(first)) return first;
        for (Supplier<T> consumer : last) {
            T string = consumer.get();
            if (nonNull(string)) return string;
        }
        return first;
    }

    /**
     * Determines if the specified Class object represents a primitive type.
     *
     * @param object The object value
     */
    public static boolean isPrimitive(Object object) {
        return object != null && object.getClass().isPrimitive();
    }

    public static boolean isMap(Object object) {
        return object instanceof Map;
    }

    public static <T> T firstNotNull(T... values) {
        return Stream.of(values).filter(Objects::nonNull).findFirst().orElse(null);
    }

    public static String firstNotBlank(String... values) {
        return Stream.of(values).filter(Objects::notBlank).findFirst().orElse(null);
    }


    public static Integer randomNumber(int length) {
        return randomNumber(length, length * 9);
    }

    public static Integer randomNumber(int min, int max) {
        Random r = new Random();
        return r.nextInt((max - min) + 1) + min;
    }

    public static <T> void setIfNotNull(T object, Consumer<T> consumer) {
        if (object != null && consumer != null) consumer.accept(object);
    }

//    /**
//     * <p>Checks if a field has a valid e-mail address.</p>
//     *
//     * @param email The value validation is being performed on.  A <code>null</code>
//     *              value is considered invalid.
//     * @return true if the email address is valid.
//     */
//    public static boolean isEmail(String email) {
//        return EmailValidator.getInstance().isValid(email);
//    }

    public static Optional<String> firstIfEmpty(String... strings) {
        return Stream.of(strings).filter(Objects::notBlank).findFirst();
    }

    public static boolean allNull(Object... objects) {
        return Stream.of(objects).noneMatch(Objects::nonNull);
    }

    public static boolean anyNotNull(Iterator<Object> iterator) {
        return Lists.newArrayList(iterator).stream().anyMatch(Objects::nonNull);
    }

    public static boolean anyNull(Collection<Object> collection) {
        return Stream.of(collection).anyMatch(Objects::isNull);
    }

    public static boolean anyNull(Iterator<Object> iterator) {
        while (iterator.hasNext()) {
            boolean empty = isNull(iterator.next());
            if (empty) return true;
        }
        return false;
    }

    public static <T> List<T> initList(List<T> list, Callable<List<T>> o) {
        try {
            return list == null ? o.call() : list;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * Whether the given source string ends with the given suffix, ignoring case.
     *
     * @param source the source
     * @param suffix the suffix
     * @return Whether the given source string ends with the given suffix, ignoring case.
     */
    public static boolean endsWithIgnoreCase(String source, String suffix) {
        if (!hasLength(suffix)) return true;
        if (!hasLength(source)) return false;
        if (suffix.length() > source.length()) return false;
        return source.substring(source.length() - suffix.length())
                .toLowerCase().endsWith(suffix.toLowerCase());
    }

    /**
     * Convert list to string
     * @param list         List
     * @param delimiter    String
     * @param stringMapper Function<T, String>
     */
    public static <T> String toString(Collection<T> list, String delimiter, Function<T, String> stringMapper) {
        Asserts.notEmpty(delimiter, "@delimiter must be not blank");
        Asserts.notNull(stringMapper, "@stringMapper must be not null");
        return Objects.isEmpty(list) ? null : list.stream().map(stringMapper)
                .collect(Collectors.joining(delimiter));
    }

    public static String toString(String... strings) {
        StringBuilder sb = new StringBuilder().append("[");
        for (String string : strings) sb.append("\"").append(string).append("\",");
        return sb.substring(0, sb.length() - 1) + "]";
    }

    public static String toString(Object object) {
        return MapperHelper.serializeToString(object);
    }

    /**
     * @param buffer ByteBuffer
     *               //@param charset the character set of the bytes
     * @return the {@code String} representation of the bytes
     */
    public static String toString(ByteBuffer buffer) {
        if (buffer == null) return null;

        byte[] bytes;
        if (buffer.hasArray())
            bytes = buffer.array();
        else {
            bytes = new byte[buffer.remaining()];
            buffer.get(bytes, 0, bytes.length);
        }
        return new String(bytes, StandardCharsets.UTF_8);
    }

    public static String appendToString(String delimiter, Object... arguments) {
        return Stream.of(arguments)
                .filter(Objects::notEmpty)
                .map(Objects::toString)
                .collect(Collectors.joining(delimiter));
    }

    public static String firstIfEmpty(Collection<String> collection) {
        if (collection == null || collection.isEmpty()) return null;
        else return collection.stream().filter(Objects::hasLength).findFirst().orElse(null);
    }

    public static <T> T getFirstList(List<T> list) {
        return list == null || list.isEmpty() ? null : list.get(0);
    }

    /**
     * Gives the substring of the given text before the last occurrence of the given separator.
     * If the text does not contain the given separator then the given text is returned.
     *
     * @param text      the text
     * @param separator the separator
     */
    public static String substringBeforeLast(String text, char separator) {
        if (!hasLength(text)) return text;
        int cPos = text.lastIndexOf(separator);
        return cPos < 0 ? text : text.substring(0, cPos);
    }

    /**
     * Gives the substring of the given text after the last occurrence of the given separator.
     * If the text does not contain the given separator then "" is returned.
     *
     * @param text      the text
     * @param separator the separator
     */
    public static String substringAfterLast(String text, char separator) {
        if (!hasLength(text)) return text;
        int cPos = text.lastIndexOf(separator);
        return cPos < 0 ? "" : text.substring(cPos + 1);
    }

    /**
     * Gives the substring of the given text before the given separator.
     * If the text does not contain the given separator then the given text is returned.
     *
     * @param text      the text
     * @param separator the separator
     */
    public static String substringBefore(String text, char separator) {
        if (!hasLength(text)) return text;
        int sepPos = text.indexOf(separator);
        return sepPos < 0 ? text : text.substring(sepPos);
    }

    public static boolean isBlank(CharSequence string) {
        return string == null || string.isEmpty();
    }

    public static boolean isBlank(String string) {
        return string == null || string.trim().isBlank();
    }

    public static boolean notBlank(String string) {
        return !isBlank(string);
    }

    public static boolean notBlank(CharSequence string) {
        return !isBlank(string);
    }


    public static boolean regexMatches(String regex, String input) {
        return java.util.regex.Pattern.matches(regex, input);
    }

    public static IllegalArgumentException newIllegal(String message, Object... args) {
        return new IllegalArgumentException(createMsgError(message, args));
    }

    public static String createMsgError(String message, Object... args) {
        if (isBlank(message)) return "message is null";
        if (args == null || args.length == 0) return message;
        if (args.length == 1 && message.startsWith("@") && message.split(" ").length == 1) {
            return joinMessageIfBegin(message, args[0].toString());
        } else return message.formatted(args);
    }

    public static String joinMessageIfBegin(String msg, String suffix) {
        if (Objects.isEmpty(msg)) return msg;
        if (!msg.startsWith("@")) return msg;
        if (msg.split(" ").length > 1) return msg;
        else return msg + " " + suffix;
    }

    /**
     * Translates a string into application/x-www-form-urlencoded format using a specific Charset.
     * This method uses the supplied charset to obtain the bytes for unsafe characters.
     *
     * @param path – String to be translated.
     */
    public static String encodeUri(String path) {
        return URLEncoder.encode(path, StandardCharsets.UTF_8);
    }

    public static String seoAlias(String string) {
        String seo = Normalizer.normalize(string, Normalizer.Form.NFKC).toLowerCase();
        seo = seo.replaceAll("đ", "d");
        seo = seo.replaceAll("\\p{M}", "");
        seo = seo.replaceAll(" ", "-");
        seo = StringUtils.stripAccents(seo).toLowerCase();
        seo = seo.replaceAll("[^\\p{ASCII}]", "-");
        seo = seo.replaceAll("-+", "-").replace(",", "").replace(".", "");
        if (seo.startsWith("-")) seo = seo.substring(1);
        if (seo.endsWith("-")) seo = seo.substring(0, seo.length() - 1);
        return seo;
    }

    public static void threadSleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException | IllegalArgumentException ignored) {
        }
    }

    public static void threadSleep(Duration duration) {
        try {
            Thread.sleep(duration);
        } catch (InterruptedException | IllegalArgumentException ignored) {
        }
    }

    public static Integer parseInteger(String text, Integer value) {
        try {
            return Integer.parseInt(text);
        } catch (NumberFormatException exp) {
            return value;
        }
    }

    public static void createFile(Path path) {
        createFolder(path.getParent());
        try {
            if (Files.notExists(path)) {
                Files.createFile(path);
            }
        } catch (IOException exp) {
            throw new RuntimeException(exp);
        }
    }

    public static OutputStream newOutputStream(Path path, OpenOption option) {
        try {
            return Files.newOutputStream(path, option);
        } catch (IOException exp) {
            throw new RuntimeException(exp);
        }
    }

    public static void appendLines(Path path, String... lines) {
        createFolder(path.getParent());
        createFile(path);

        try {
            OutputStream stream = newOutputStream(path, StandardOpenOption.APPEND);
            IOUtils.writeLines(List.of(lines), null, stream, StandardCharsets.UTF_8);
        } catch (IOException exp) {
            throw new RuntimeException(exp);
        }

    }

    public static String getExtension(String fileName) {
        return FilenameUtils.getExtension(fileName);
    }


    public static String determineMimeType(String filePath) {
        String ext = getExtension(filePath);
        return MimeMappings.get(ext);
    }

    public static String determineMimeType(Path file) {
        try {
            return Files.probeContentType(file);
        } catch (IOException exp) {
            return determineMimeType(file.toFile());
        }
    }


    public static String determineMimeType(File file) {
        return determineMimeType(file.getName());
    }

    public static String determineFolder(String filePath) {
        int last = filePath.lastIndexOf("/");
        return last == 0 ? null : last == -1
                ? filePath : filePath.substring(0, last);
    }

    /**
     * @throws IllegalArgumentException if create folder error
     */
    public static Path createFolder(Path path) {
        if (Files.exists(path)) return path;
        try {
            return Files.createDirectories(path);
        }

        // FileAlreadyExistsException
        catch (FileAlreadyExistsException exp) {
            String msg = "file exists and is not a directory";
            throw newIllegal(msg, exp);
        }

        // SecurityException
        catch (SecurityException exp) {
            String msg = "don't have permission to get absolute path";
            throw newIllegal(msg, exp);
        }

        // FileSystemException
        catch (FileSystemException exp) {
            throw newIllegal(exp.getMessage(), exp);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> T getItemAt(List<T> list, int index, T value) {
        return getItemAt(list, index, value, null);
    }

    public static <T> T getItemAt(List<T> list, int index) {
        return getItemAt(list, index, null);
    }


    public static <T> T getItemAt(List<T> list, int index, T defaultValue, Supplier<RuntimeException> throwSupplier) {
        if (isEmpty(list) || index < 0 || list.size() <= index) {
            if (throwSupplier != null) throw throwSupplier.get();
            else return defaultValue;
        }

        return list.get(index);
    }

    public static <T> T removeLast(List<T> list) {
        Asserts.notEmpty(list, "@list");
        return list.remove(list.size() - 1);
    }

    public static boolean anyEmpty(String... strings) {
        return Stream.of(strings).anyMatch(Objects::isEmpty);
    }

    public static <T> List<T> filter(Set<T> objects, Predicate<T> predicate) {
        return objects.stream().filter(predicate).collect(Collectors.toList());
    }

    public static <T> List<T> newList(T data) {
        return Stream.of(data).collect(Collectors.toList());
    }

    /**
     * Return list item duplicate
     *
     * @param items the items check
     * @see #checkDuplicateItem(Function, Collection[])
     */
    public static <T> List<T> checkDuplicateItem(Collection<T>... items) {
        Asserts.notNull(items, "@items");
        ;
        return checkDuplicateItem(item -> item, items);
    }

    /**
     * Return list item duplicate
     *
     * @param items the items check
     */
    public static <T, R> List<T> checkDuplicateItem(Function<T, R> getFunc, Collection<T>... items) {
        Asserts.notNull(getFunc, "@getFunc");
        Set<R> itemSet = new HashSet<>();
        return Arrays.stream(items).flatMap(Collection::stream)
                .filter(item -> !itemSet.add(getFunc.apply(item)))
                .toList();
    }

    public static Number parseDecimal(String pattern, String formatValue) {
        try {
            return new DecimalFormat(pattern).parse(formatValue);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * takes in a column reference portion of a CellRef and converts it from
     * ALPHA-26 number format to 0-based base 10.
     * 'A' -> 0
     * 'Z' -> 25
     * 'AA' -> 26
     * 'IV' -> 255
     *
     * @return zero based column index
     */
    public static int letterToNumber(String letter) {
        int pos = 0, retval = 0;
        for (int k = letter.length() - 1; k >= 0; k--) {
            char charAt = letter.charAt(k);
            if (charAt == '$') {
                Asserts.isTrue(k == 0, "Bad col ref format '%s'", letter);
                break;
            }
            // Character.getNumericValue() returns the values
            //  10-35 for the letter A-Z
            int shift = (int) Math.pow(26, pos);
            retval += (Character.getNumericValue(charAt) - 9) * shift;
            pos++;
        }
        return retval - 1;
    }

    public static boolean isCollection(Object object) {
        return object instanceof Collection<?>;
    }

    public static <A> List<A> cloneList(List<A> list) {
        List<A> newList = new LinkedList<>();
        if (nonNull(list)) newList.addAll(list);
        return newList;
    }

    public static <K, V> Map<K, V> cloneHashMap(Map<K, V> map) {
        Map<K, V> newMap = new HashMap<>();
        if (nonNull(map)) newMap.putAll(map);
        ;
        return newMap;
    }

    public static <A> boolean isAssignableFrom(Class type, Class... assignableCls) {
        return Stream.of(assignableCls).anyMatch(cls -> cls.isAssignableFrom(type));
    }


    /**
     * Takes in a 0-based base-10 column and returns a ALPHA-26 representation.
     * eg column #3 -> D
     *
     * @param number the number begin > 0
     */
    public static String numberToLetter(int number) {
        // Excel counts column A as the 1st column, we
        //  treat it as the 0th one

        StringBuilder colRef = new StringBuilder();
        int colRemain = number;

        while (colRemain > 0) {
            int thisPart = colRemain % 26;
            if (thisPart == 0) {
                thisPart = 26;
            }
            colRemain = (colRemain - thisPart) / 26;

            // The letter A is at 65
            char colChar = (char) (thisPart + 64);
            colRef.insert(0, colChar);
        }

        return colRef.toString();
    }

    public static String toHexString(Object target) {
        return toHexString(target.getClass().getName() + "@", target::hashCode);
    }

    public static String toHexString(String prefix, Supplier<Integer> consumer) {
        return prefix + Integer.toHexString(consumer.get());
    }

    public static boolean isNumberCls(Class cls) {
        return Number.class.isAssignableFrom(cls);
    }

    /**
     *
     */
    public static <T extends Comparable> boolean isGreaterThanOrEq(T first, T second) {
        return first.compareTo(second) >= 0;
    }

    public static <T extends Enum<T>> T enumValueOf(Class<T> enumClass, Integer ordinal) {
        T[] array = enumClass.getEnumConstants();
        if (ordinal < 0 || ordinal >= array.length) return null;
        else return array[ordinal];
    }

    public static <T extends Enum<T>> T enumValueOf(Class<T> enumClass, CharSequence name) {
        if (enumClass == null || isBlank(name) || hasAnySpace(name)) return null;
        else return Enum.valueOf(enumClass, name.toString());
    }

    public static IllegalArgumentException newInvalidType(Object value) {
        return newInvalidType(value, null);
    }

    public static IllegalArgumentException newInvalidType(Object value, Class clazz) {
        String msg = "The value `%s` not support";
        if (clazz != null) msg += " for `"+ clazz.getName()+"`";
        return newIllegal(msg, value);
    }


    public static <T> String join(T[] array, Function<T, String> objectMapper, CharSequence delimiter) {
        return Stream.of(array).map(objectMapper).collect(Collectors.joining(delimiter));
    }

    public static <T> String join(Collection<T> collection, Function<T, String> objectMapper, CharSequence delimiter) {
        return collection.stream().map(objectMapper).collect(Collectors.joining(delimiter));
    }

    /**
     * <p>Converts the specified primitive Class object to its corresponding
     * wrapper Class object.</p>
     *
     * @param cls the class to convert, may be null
     */
    public static Class<?> primitiveToWrapper(final Class<?> cls) {
        Class<?> convertedClass = cls;
        if (cls != null && cls.isPrimitive()) {
            convertedClass = primitiveWrapperMap.get(cls);
        }
        return convertedClass;
    }


    /**
     * Maps primitive {@code Class}es to their corresponding wrapper {@code Class}.
     */
    private static final Map<Class<?>, Class<?>> primitiveWrapperMap = new HashMap<>();

    static {
        primitiveWrapperMap.put(Boolean.TYPE, Boolean.class);
        primitiveWrapperMap.put(Byte.TYPE, Byte.class);
        primitiveWrapperMap.put(Character.TYPE, Character.class);
        primitiveWrapperMap.put(Short.TYPE, Short.class);
        primitiveWrapperMap.put(Integer.TYPE, Integer.class);
        primitiveWrapperMap.put(Long.TYPE, Long.class);
        primitiveWrapperMap.put(Double.TYPE, Double.class);
        primitiveWrapperMap.put(Float.TYPE, Float.class);
        primitiveWrapperMap.put(Void.TYPE, Void.TYPE);
    }


    /**
     * Tokenize the given {@code String} into a {@code String} array via a
     * {@link StringTokenizer}.
     * <p>The given {@code delimiters} string can consist of any number of
     * delimiter characters. Each of those characters can be used to separate
     * tokens. A delimiter is always a single character; for multi-character
     * delimiters, consider using {@link #delimitedListToStringArray}.
     *
     * @param str               the {@code String} to tokenize (potentially {@code null} or empty)
     * @param delimiters        the delimiter characters, assembled as a {@code String}
     *                          (each of the characters is individually considered as a delimiter)
     * @param trimTokens        trim the tokens via {@link String#trim()}
     * @param ignoreEmptyTokens omit empty tokens from the result array
     *                          (only applies to tokens that are empty after trimming; StringTokenizer
     *                          will not consider subsequent delimiters as token in the first place).
     * @return an array of the tokens
     * @see StringTokenizer
     * @see String#trim()
     * @see #delimitedListToStringArray
     */
    public static String[] tokenizeToStringArray(String str, String delimiters, boolean trimTokens, boolean ignoreEmptyTokens) {
        if (str == null) return EMPTY_STRING_ARRAY;
        StringTokenizer st = new StringTokenizer(str, delimiters);
        List<String> tokens = new ArrayList<>();
        while (st.hasMoreTokens()) {
            String token = st.nextToken();
            if (trimTokens) token = token.trim();
            if (!ignoreEmptyTokens || token.length() > 0) tokens.add(token);
        }
        return toStringArray(tokens);
    }

    /**
     * Take a {@code String} that is a delimited list and convert it into a
     * {@code String} array.
     * <p>A single {@code delimiter} may consist of more than one character,
     * but it will still be considered as a single delimiter string, rather
     * than as a bunch of potential delimiter characters, in contrast to
     * {@link #tokenizeToStringArray}.
     *
     * @param str       the input {@code String} (potentially {@code null} or empty)
     * @param delimiter the delimiter between elements (this is a single delimiter,
     *                  rather than a bunch individual delimiter characters)
     * @return an array of the tokens in the list
     * @see #tokenizeToStringArray
     */
    public static String[] delimitedListToStringArray(String str, String delimiter) {
        return delimitedListToStringArray(str, delimiter, null);
    }

    /**
     * Take a {@code String} that is a delimited list and convert it into
     * a {@code String} array.
     * <p>A single {@code delimiter} may consist of more than one character,
     * but it will still be considered as a single delimiter string, rather
     * than as a bunch of potential delimiter characters, in contrast to
     * {@link #tokenizeToStringArray}.
     *
     * @param str           the input {@code String} (potentially {@code null} or empty)
     * @param delimiter     the delimiter between elements (this is a single delimiter,
     *                      rather than a bunch individual delimiter characters)
     * @param charsToDelete a set of characters to delete; useful for deleting unwanted
     *                      line breaks: e.g. "\r\n\f" will delete all new lines and line feeds in a {@code String}
     * @return an array of the tokens in the list
     * @see #tokenizeToStringArray
     */
    public static String[] delimitedListToStringArray(String str, String delimiter, String charsToDelete) {
        if (str == null) return EMPTY_STRING_ARRAY;
        if (delimiter == null) return new String[]{str};

        List<String> result = new ArrayList<>();
        if (delimiter.isEmpty()) {
            for (int i = 0; i < str.length(); i++) {
                result.add(deleteAny(str.substring(i, i + 1), charsToDelete));
            }
        } else {
            int pos = 0;
            int delPos;
            while ((delPos = str.indexOf(delimiter, pos)) != -1) {
                result.add(deleteAny(str.substring(pos, delPos), charsToDelete));
                pos = delPos + delimiter.length();
            }
            if (str.length() > 0 && pos <= str.length()) {
                // Add rest of String, but not in case of empty input.
                result.add(deleteAny(str.substring(pos), charsToDelete));
            }
        }
        return toStringArray(result);
    }

    /**
     * Copy the given {@link Collection} into a {@code String} array.
     * <p>The {@code Collection} must contain {@code String} elements only.
     *
     * @param collection the {@code Collection} to copy
     *                   (potentially {@code null} or empty)
     * @return the resulting {@code String} array
     */
    public static String[] toStringArray(Collection<String> collection) {
        return isEmpty(collection) ? EMPTY_STRING_ARRAY : collection.toArray(String[]::new);
    }

    /**
     * Delete all occurrences of the given substring.
     *
     * @param inString the original {@code String}
     * @param pattern  the pattern to delete all occurrences of
     * @return the resulting {@code String}
     */
    public static String delete(String inString, String pattern) {
        return replace(inString, pattern, "");
    }

    /**
     * Delete any character in a given {@code String}.
     *
     * @param inString      the original {@code String}
     * @param charsToDelete a set of characters to delete.
     *                      E.g. "az\n" will delete 'a's, 'z's and new lines.
     * @return the resulting {@code String}
     */
    public static String deleteAny(String inString, String charsToDelete) {
        if (!hasLength(inString) || !hasLength(charsToDelete)) {
            return inString;
        }

        int lastCharIndex = 0;
        char[] result = new char[inString.length()];
        for (int i = 0; i < inString.length(); i++) {
            char c = inString.charAt(i);
            if (charsToDelete.indexOf(c) == -1) {
                result[lastCharIndex++] = c;
            }
        }
        if (lastCharIndex == inString.length()) {
            return inString;
        }
        return new String(result, 0, lastCharIndex);
    }


    /**
     * Replace all occurrences of a substring within a string with another string.
     *
     * @param inString   {@code String} to examine
     * @param oldPattern {@code String} to replace
     * @param newPattern {@code String} to insert
     * @return a {@code String} with the replacements
     */
    public static String replace(String inString, String oldPattern, String newPattern) {
        if (!hasLength(inString) || !hasLength(oldPattern) || newPattern == null) {
            return inString;
        }
        int index = inString.indexOf(oldPattern);
        if (index == -1) {
            // no occurrence -> can return input as-is
            return inString;
        }

        int capacity = inString.length();
        if (newPattern.length() > oldPattern.length()) {
            capacity += 16;
        }
        StringBuilder sb = new StringBuilder(capacity);

        int pos = 0;  // our position in the old string
        int patLen = oldPattern.length();
        while (index >= 0) {
            sb.append(inString, pos, index);
            sb.append(newPattern);
            pos = index + patLen;
            index = inString.indexOf(oldPattern, pos);
        }

        // append any characters to the right of a match
        sb.append(inString, pos, inString.length());
        return sb.toString();
    }

    public static <T> void setIfNotBlank(String string, Consumer<String> setFnc) {
        if (notBlank(string)) setFnc.accept(string);
    }

    public static boolean hasAnySpace(CharSequence string) {
        return string.toString().matches(".*\\s.*");
    }

    public static boolean isTrue(CharSequence str) {
        if (notBlank(str)) {
            String string = str.toString();
            if ("true".equalsIgnoreCase(string)) return true;
            if ("1".equalsIgnoreCase(string)) return true;
            if ("t".equalsIgnoreCase(string)) return true;
            if ("false".equalsIgnoreCase(string)) return true;
            if ("0".equalsIgnoreCase(string)) return true;
            if ("f".equalsIgnoreCase(string)) return true;
        }
        return false;
    }

    public static boolean isTrue(Character str) {
        return str != null && (str == '1' || str == 't');
    }

    public static boolean isInteger(Class cls) {
        return cls == Integer.TYPE || Integer.class.equals(cls);
    }

    public static boolean isLong(Class cls) {
        return cls == Long.TYPE || Long.class.equals(cls);
    }

    public static boolean isDouble(Class cls) {
        return cls == Double.TYPE || Double.class.equals(cls);
    }

    public static boolean isBigDecimal(Class cls) {
        return BigDecimal.class.equals(cls);
    }

    public static boolean isBigInteger(Class cls) {
        return BigInteger.class.equals(cls);
    }

    public static boolean isFloat(Class cls) {
        return cls == Float.TYPE || Float.class.equals(cls);
    }

    public static boolean isByte(Class cls) {
        return cls == Byte.TYPE || Byte.class.equals(cls);
    }

    public static boolean isShort(Class cls) {
        return cls == Short.TYPE || Short.class.equals(cls);
    }

    public static String toStringAndClose(Reader reader) {
        try (reader) {
            return IOUtils.toString(reader);
        } catch (IOException exp) {
            throw new RuntimeException(exp);
        }
    }

    public static <X> Number parseNumber(Class<X> type, String str) {
        if (isInteger(type)) return Integer.parseInt(str);
        if (isLong(type)) return Long.parseLong(str);
        if (isDouble(type)) return Double.parseDouble(str);
        if (isBigDecimal(type)) return new BigDecimal(str);
        if (isBigInteger(type)) return new BigInteger(str);
        if (isFloat(type)) return Float.parseFloat(str);
        if (isByte(type)) return Byte.parseByte(str);
        if (isShort(type)) return Short.parseShort(str);
        else throw newIllegal("invalid parseNumber(%s,%s)", type.getName(), str);
    }

    public static byte[] serializeToBytes(Serializable object) {
        return SerializationUtils.serialize(object);
    }

    public static <T extends Serializable> T deserializeFromBytes(byte[] value) {
        return SerializationUtils.deserialize(value);
    }

    public static <T> Set<T> newSet(List<T> objects) {
        return new HashSet<>(objects);
    }

    public static <T> T newInstanceClass(Class<T> factoryClass) {
        if (factoryClass == null) throw newIllegal("Class is null.");
        else return Classes.instantiateClass(factoryClass);
    }

    public static Path getUserDir() {
        String userDir = System.getProperty("user.dir");
        return Paths.get(userDir);
    }

//    public static boolean isValidUrl(String url) {
//        return new UrlValidator().isValid(url);
//    }

    public static boolean isHttps(String url) {
        return url.startsWith("https://") || url.startsWith("http://");
    }

    public static List<String> readLine(Path path) {
        if (path == null || !Files.exists(path)) return new LinkedList<>();
        else try {
            return Files.readAllLines(path);
        } catch (IOException exp) {
            throw new IllegalArgumentException(exp);
        }
    }

    /**
     * Gets the contents of a {@code Reader} as a String.
     * <p>
     * This method buffers the input internally, so there is no need to use a
     * {@code BufferedReader}.
     *
     * @param reader the {@code Reader} to read from
     * @param length number of chars to copy
     * @return the requested String
     * @throws NullPointerException if the input is null
     * @throws IOException          if an I/O error occurs
     */
    public static String toString(final Reader reader, int length) throws IOException {
        return toString(reader, 0, length);
    }

    /**
     * Gets the contents of a {@code Reader} as a String.
     * <p>
     * This method buffers the input internally, so there is no need to use a
     * {@code BufferedReader}.
     *
     * @param reader      the {@code Reader} to read from
     * @param inputOffset number of chars to skip from input before copying
     * @param length      number of chars to copy
     * @return the requested String
     * @throws NullPointerException if the input is null
     * @throws IOException          if an I/O error occurs
     */
    public static String toString(final Reader reader, int inputOffset, int length) throws IOException {
        try (final StringBuilderWriter sw = new StringBuilderWriter()) {
            IOUtils.copyLarge(reader, sw, inputOffset, length);
            return sw.toString();
        }
    }

    public static <T extends Number> T maxNumber(Set<T> items, Comparator<T> comparator) {
        return items.stream().max(comparator).orElse(null);
    }

    public static Integer max(Set<Integer> items) {
        return maxNumber(items, Integer::compareTo);
    }

    public static Object[] toObjectArray(Object source) {
        if (source == null) return new Object[0];
        if (source instanceof Object[] objects) return objects;
        if (source instanceof Collection<?> col) return col.toArray();

        boolean hasArray = source.getClass().isArray();
        if (!hasArray) return new Object[]{source};

        int length = Array.getLength(source);
        if (length == 0) return new Object[0];

        Class<?> wrapperType = Array.get(source, 0).getClass();
        Object[] newArray = (Object[]) Array.newInstance(wrapperType, length);
        for (int i = 0; i < length; i++) newArray[i] = Array.get(source, i);

        return newArray;

    }

    public static List<Object> toArrayList(Object source) {
        if(source instanceof Collection col) return Lists.copyOf(col);
        else if(source instanceof Object[] objects) return Arrays.stream(objects).toList();
        else return Arrays.stream(toObjectArray(source)).toList();
    }

    public static List<String> toStringList(Object source) {
        Object[] objects = toObjectArray(source);
        return Stream.of(objects)
                .map(o -> o == null ? null : o.toString()).toList();
    }

    public static String camelCaseToDot(String string) {
        if (isBlank(string)) return "";
        StringBuilder builder = new StringBuilder(string.replace('.', '_'));
        for (int i = 1; i < builder.length() - 1; i++) {
            if (isUnderscoreRequired(builder.charAt(i - 1), builder.charAt(i), builder.charAt(i + 1))) {
                builder.insert(i++, '.');
            }
        }
        return builder.toString();
    }

    private static boolean isUnderscoreRequired(final char before, final char current, final char after) {
        return Character.isLowerCase(before) && Character.isUpperCase(current) && Character.isLowerCase(after);
    }

    public static String joinString(String delimiter, String... strings) {
        return Stream.of(strings).filter(Objects::notBlank).collect(Collectors.joining(delimiter));
    }

    public static NoSuchMethodException newNoSuchMethod(String message, Object... args) {
        return new NoSuchMethodException(formatString(message, args));
    }

    public static UnsupportedOperationException unsupported(String message, Object... args) {
        return new UnsupportedOperationException(formatString(message, args));
    }

    public static int hashCode(Object... objects) {
        return java.util.Objects.hash(objects);
    }

    public static boolean isLoe(Integer number, Integer second) {
        return number == null || number <= second;
    }

    public static boolean containsIgnoreCase(String string, String containsText) {
        return string.toLowerCase().contains(containsText.toLowerCase());
    }


    /**
     * Determine if the given objects are equal, returning {@code true} if
     * both are {@code null} or {@code false} if only one is {@code null}.
     * <p>Compares arrays with {@code Arrays.equals}, performing an equality
     * check based on the array elements rather than the array reference.
     *
     * @param o1 first Object to compare
     * @param o2 second Object to compare
     * @return whether the given objects are equal
     * @see Object#equals(Object)
     * @see java.util.Arrays#equals
     */
    public static boolean nullSafeEquals(Object o1, Object o2) {
        if (o1 == o2) return true;
        else if (o1 == null || o2 == null) return false;
        else if (o1.equals(o2)) return true;
        else if (o1.getClass().isArray() && o2.getClass().isArray()) return arrayEquals(o1, o2);
        else return false;
    }

    /**
     * Compare the given arrays with {@code Arrays.equals}, performing an equality
     * check based on the array elements rather than the array reference.
     * @param o1 first array to compare
     * @param o2 second array to compare
     * @return whether the given objects are equal
     * @see #nullSafeEquals(Object, Object)
     * @see java.util.Arrays#equals
     */
    private static boolean arrayEquals(Object o1, Object o2) {
        if (o1 instanceof Object[] objects1 && o2 instanceof Object[] objects2) {
            return Arrays.equals(objects1, objects2);
        }
        if (o1 instanceof boolean[] booleans1 && o2 instanceof boolean[] booleans2) {
            return Arrays.equals(booleans1, booleans2);
        }
        if (o1 instanceof byte[] bytes1 && o2 instanceof byte[] bytes2) {
            return Arrays.equals(bytes1, bytes2);
        }
        if (o1 instanceof char[] chars1 && o2 instanceof char[] chars2) {
            return Arrays.equals(chars1, chars2);
        }
        if (o1 instanceof double[] doubles1 && o2 instanceof double[] doubles2) {
            return Arrays.equals(doubles1, doubles2);
        }
        if (o1 instanceof float[] floats1 && o2 instanceof float[] floats2) {
            return Arrays.equals(floats1, floats2);
        }
        if (o1 instanceof int[] ints1 && o2 instanceof int[] ints2) {
            return Arrays.equals(ints1, ints2);
        }
        if (o1 instanceof long[] longs1 && o2 instanceof long[] longs2) {
            return Arrays.equals(longs1, longs2);
        }
        if (o1 instanceof short[] shorts1 && o2 instanceof short[] shorts2) {
            return Arrays.equals(shorts1, shorts2);
        }
        return false;
    }

    /**
     * Return a hash code for the given object; typically the value of
     * {@code Object#hashCode()}}. If the object is an array,
     * this method will delegate to any of the {@code Arrays.hashCode}
     * methods. If the object is {@code null}, this method returns 0.
     * @see Object#hashCode()
     * @see Arrays
     */
    public static int nullSafeHashCode(Object obj) {
        if (obj == null) {
            return 0;
        }
        if (obj.getClass().isArray()) {
            if (obj instanceof Object[] objects) {
                return Arrays.hashCode(objects);
            }
            if (obj instanceof boolean[] booleans) {
                return Arrays.hashCode(booleans);
            }
            if (obj instanceof byte[] bytes) {
                return Arrays.hashCode(bytes);
            }
            if (obj instanceof char[] chars) {
                return Arrays.hashCode(chars);
            }
            if (obj instanceof double[] doubles) {
                return Arrays.hashCode(doubles);
            }
            if (obj instanceof float[] floats) {
                return Arrays.hashCode(floats);
            }
            if (obj instanceof int[] ints) {
                return Arrays.hashCode(ints);
            }
            if (obj instanceof long[] longs) {
                return Arrays.hashCode(longs);
            }
            if (obj instanceof short[] shorts) {
                return Arrays.hashCode(shorts);
            }
        }
        return obj.hashCode();
    }

    public static UnsupportedOperationException newUnsupported(String message, Object... args) {
        message = formatString(message, args);
        return new UnsupportedOperationException(message);
    }

    public static void applyConsumer(boolean b, Object o) {
    }


    /**
     * Clones an array returning a typecast result and handling
     * {@code null}.
     * <p>
     * This method returns {@code null} for a {@code null} input array.
     * </p>
     *
     * @param array  the array to clone, may be {@code null}
     * @return the cloned array, {@code null} if {@code null} input
     */
    public static boolean[] copyArray(final boolean[] array) {
        return array != null ? array.clone() : null;
    }

    /**
     * Clones an array returning a typecast result and handling
     * {@code null}.
     * <p>
     * This method returns {@code null} for a {@code null} input array.
     * </p>
     *
     * @param array  the array to clone, may be {@code null}
     * @return the cloned array, {@code null} if {@code null} input
     */
    public static byte[] copyArray(final byte[] array) {
        return array != null ? array.clone() : null;
    }

    /**
     * Clones an array returning a typecast result and handling
     * {@code null}.
     * <p>
     * This method returns {@code null} for a {@code null} input array.
     * </p>
     *
     * @param array  the array to clone, may be {@code null}
     * @return the cloned array, {@code null} if {@code null} input
     */
    public static char[] copyArray(final char[] array) {
        return array != null ? array.clone() : null;
    }

    /**
     * Clones an array returning a typecast result and handling
     * {@code null}.
     * <p>
     * This method returns {@code null} for a {@code null} input array.
     * </p>
     *
     * @param array  the array to clone, may be {@code null}
     * @return the cloned array, {@code null} if {@code null} input
     */
    public static double[] copyArray(final double[] array) {
        return array != null ? array.clone() : null;
    }

    /**
     * Clones an array returning a typecast result and handling
     * {@code null}.
     * <p>
     * This method returns {@code null} for a {@code null} input array.
     * </p>
     *
     * @param array  the array to clone, may be {@code null}
     * @return the cloned array, {@code null} if {@code null} input
     */
    public static float[] copyArray(final float[] array) {
        return array != null ? array.clone() : null;
    }

    /**
     * Clones an array returning a typecast result and handling
     * {@code null}.
     * <p>
     * This method returns {@code null} for a {@code null} input array.
     * </p>
     *
     * @param array  the array to clone, may be {@code null}
     * @return the cloned array, {@code null} if {@code null} input
     */
    public static int[] copyArray(final int[] array) {
        return array != null ? array.clone() : null;
    }

    /**
     * Clones an array returning a typecast result and handling
     * {@code null}.
     * <p>
     * This method returns {@code null} for a {@code null} input array.
     * </p>
     *
     * @param array  the array to clone, may be {@code null}
     * @return the cloned array, {@code null} if {@code null} input
     */
    public static long[] copyArray(final long[] array) {
        return array != null ? array.clone() : null;
    }

    /**
     * Clones an array returning a typecast result and handling
     * {@code null}.
     * <p>
     * This method returns {@code null} for a {@code null} input array.
     * </p>
     *
     * @param array  the array to clone, may be {@code null}
     * @return the cloned array, {@code null} if {@code null} input
     */
    public static short[] copyArray(final short[] array) {
        return array != null ? array.clone() : null;
    }

    /**
     * Shallow clones an array returning a typecast result and handling
     * {@code null}.
     * <p>
     * The objects in the array are not cloned, thus there is no special
     * handling for multi-dimensional arrays.
     * </p>
     * <p>
     * This method returns {@code null} for a {@code null} input array.
     * </p>
     *
     * @param <T> the component type of the array
     * @param array  the array to shallow clone, may be {@code null}
     * @return the cloned array, {@code null} if {@code null} input
     */
    public static <T> T[] copyArray(final T[] array) {
        return array != null ? array.clone() : null;
    }

        public static boolean isBoolean(String arg) {
            return !isBlank(arg) && (arg.equals("false") || arg.equals("true"));
    }

    public static String decodeBase64ToString(String password) {
        byte[] data = Base64.getDecoder().decode(password);
        return new String(data);
    }

}