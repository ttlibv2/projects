package vn.conyeu.commons.utils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.UndeclaredThrowableException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public final class Asserts {

    public static RuntimeException invalidValueType(Class<?> simpleName) {
        return new IllegalArgumentException("The value not support type [" + simpleName.getSimpleName() + "]");
    }

    private static IllegalArgumentException newIllegalArgument(String message, Object... args) {
        throw new IllegalArgumentException(buildMessage(message, args));
    }

    /**
     * Checks that the specified object reference is not null.
     *
     * @throws IllegalArgumentException if the object is {@code null}
     */
    public static <T> T notNull(T object, String message, Object... args) {
        if (Objects.nonNull(object)) return object;
        else {
            String newMessage = Objects.ifNull(message, () -> "");
            throw new NullPointerException(buildMessage(newMessage, args));
        }
    }

    /**
     * Asserts a boolean expression, throwing an {@code IllegalArgumentException}
     * if the expression evaluates to {@code false}.
     * <pre class="code">Asserts.isTrue(i &gt; 0, "The value must be greater than zero");</pre>
     *
     * @param expression a boolean expression
     * @param message    the exception message to use if the assertion fails
     * @throws IllegalArgumentException if {@code expression} is {@code false}
     */
    public static void isTrue(boolean expression, String message, Object... args) {
        if (!expression) throw new IllegalArgumentException(buildMessage(message, args));
    }

    public static void isFalse(boolean expression, String message, Object... args) {
        if (expression) throw new IllegalArgumentException(buildMessage(message, args));
    }

    public static void isEqual(Object obj1, Object obj2) {
        if (!Objects.equals(obj1, obj1)) {
            throw new IllegalArgumentException(obj1 + " != " + obj2);
        }
    }

    /**
     * Asserts a boolean expression
     *
     * @param expression      a boolean expression
     * @param messageSupplier a supplier for the exception message to use if the assertion fails
     * @throws IllegalArgumentException if {@code expression} is {@code false}
     */
    public static void isTrue(boolean expression, Supplier<String> messageSupplier) {
        if (!expression) throw new IllegalArgumentException(nullSafeGet(messageSupplier));
    }

    public static void isTrueCustom(boolean expression, Supplier<RuntimeException> exceptionSupplier) {
        if (!expression && exceptionSupplier != null) throw exceptionSupplier.get();
    }

    /**
     * Asserts that an object is {@code null}.
     *
     * @param object  the object to check
     * @param message the exception message to use if the assertion fails
     * @throws IllegalArgumentException if the object is not {@code null}
     */
    public static void isNull(Object object, String message, Object...arguments) {
        if (object != null) {
            message = Objects.formatString(message, arguments);
            throw new IllegalArgumentException(message);
        }
    }

    /**
     * Asserts that an object is {@code null}.
     *
     * @param object          the object to check
     * @param messageSupplier a supplier for the exception message to use if the assertion fails
     * @throws IllegalArgumentException if the object is not {@code null}
     */
    public static void isNull(Object object, Supplier<String> messageSupplier) {
        if (object != null) throw new IllegalArgumentException(nullSafeGet(messageSupplier));
    }


    /**
     * Asserts that an object is not {@code null}.
     *
     * @param object the object to check
     * @throws IllegalArgumentException if the object is {@code null}
     */
    public static <T> T notNull(T object) {
        return notNull(object, "The value must be not null.");
    }

    /**
     * Asserts that an object is not {@code null}.
     *
     * @param object  the object to check
     * @param message the exception message to use if the assertion fails
     * @throws IllegalArgumentException if the object is {@code null}
     */
    public static <T> T notNull(T object, String message) {
        if (object != null) return object;
        else {
            message = Objects.joinMessageIfBegin(message, "must be not null");
            throw new NullPointerException(message);
        }
    }

    /**
     * Asserts that an object is not {@code null}.
     *
     * @param object          the object to check
     * @param messageSupplier a supplier for the exception message to use if the assertion fails
     * @throws IllegalArgumentException if the object is {@code null}
     */
    public static void notNull(Object object, Supplier<String> messageSupplier) {
        if (object == null) {
            throw new IllegalArgumentException(nullSafeGet(messageSupplier));
        }
    }

    /**
     * Asserts that the given String is not empty;
     *
     * @param text    the String to check
     * @param message the exception message to use if the assertion fails
     * @throws IllegalArgumentException if the text is empty
     */
    public static String hasLength(String text, String message) {
        return hasLength(text, message, new Object[0]);
    }


    /**
     * Asserts that the given String is not empty;
     *
     * @param text    the String to check
     * @param message the exception message to use if the assertion fails
     * @throws IllegalArgumentException if the text is empty
     */
    public static String hasLength(String text, String message, Object... args) {
        if (!Objects.hasLength(text)) {
            message = buildMessage(message, args);
            throw new IllegalArgumentException(message);
        }
        return text;
    }

    private static String buildMessage(String message, Object... args) {
        if (args == null || args.length == 0 || message == null) return message;
        else return String.format(message, args);
    }

    /**
     * Asserts that the given String is not empty
     *
     * @param text            the String to check
     * @param messageSupplier a supplier for the exception message to use if the assertion fails
     * @throws IllegalArgumentException if the text is empty
     */
    public static String hasLength( String text, Supplier<String> messageSupplier) {
        if (!Objects.hasLength(text)) throw new IllegalArgumentException(nullSafeGet(messageSupplier));
        else return text;
    }

    /**
     * Asserts that the given String contains valid text content
     *
     * @param text    the String to check
     * @param message the exception message to use if the assertion fails
     * @throws IllegalArgumentException if the text does not contain valid text content
     */
    public static String hasText( String text, String message) {
        if (!Objects.hasText(text)) throw new IllegalArgumentException(message);
        else return text;
    }

    /**
     * Asserts that the given String contains valid text content
     *
     * @param text            the String to check
     * @param messageSupplier a supplier for the exception message to use if the assertion fails
     * @throws IllegalArgumentException if the text does not contain valid text content
     */
    public static String hasText( String text, Supplier<String> messageSupplier) {
        if (!Objects.hasText(text)) throw new IllegalArgumentException(nullSafeGet(messageSupplier));
        else return text;
    }

    /**
     * Asserts that the given text does not contain the given substring.
     *
     * @param textToSearch the text to search
     * @param substring    the substring to find within the text
     * @param message      the exception message to use if the assertion fails
     * @throws IllegalArgumentException if the text contains the substring
     */
    public static void doesNotContain( String textToSearch, String substring, String message) {
        if (Objects.hasLength(textToSearch) && Objects.hasLength(substring) &&
                textToSearch.contains(substring)) {
            throw new IllegalArgumentException(message);
        }
    }

    /**
     * Asserts that the given text does not contain the given substring.
     *
     * @param textToSearch    the text to search
     * @param substring       the substring to find within the text
     * @param messageSupplier a supplier for the exception message to use if the assertion fails
     * @throws IllegalArgumentException if the text contains the substring
     */
    public static void doesNotContain( String textToSearch, String substring, Supplier<String> messageSupplier) {
        if (Objects.hasLength(textToSearch) && Objects.hasLength(substring) &&
                textToSearch.contains(substring)) {
            throw new IllegalArgumentException(nullSafeGet(messageSupplier));
        }
    }

    public static <T> T[] notEmpty( T[] array) {
        return notEmpty(array, "array must be not empty.");
    }

    /**
     * Asserts that an array contains elements
     *
     * @param array   the array to check
     * @param message the exception message to use if the assertion fails
     * @throws IllegalArgumentException if the object array is {@code null} or contains no elements
     */
    public static <T> T[] notEmpty( T[] array, String message) {
        if (Objects.isEmpty(array)) throw Objects.newIllegal(message, "must be not empty.");
        else return array;
    }

    /**
     * Asserts that an array contains elements
     *
     * @param array           the array to check
     * @param messageSupplier a supplier for the exception message to use if the assertion fails
     * @throws IllegalArgumentException if the object array is {@code null} or contains no elements
     */
    public static void notEmpty( Object[] array, Supplier<String> messageSupplier) {
        if (Objects.isEmpty(array)) {
            throw new IllegalArgumentException(nullSafeGet(messageSupplier));
        }
    }

    public static String notEmpty(String string) {
        return notEmpty(string, "string must be not empty.");
    }

    public static String notEmpty(String string, String message) {
        if (Objects.isEmpty(string)) throw new NullPointerException(message);
        else return string;
    }

    /**
     * Asserts that an array contains no {@code null} elements.
     *
     * @param array   the array to check
     * @param message the exception message to use if the assertion fails
     * @throws IllegalArgumentException if the object array contains a {@code null} element
     */
    public static void noNullElements( Object[] array, String message) {
        if (array != null) {
            for (Object element : array) {
                if (element == null) {
                    throw new IllegalArgumentException(message);
                }
            }
        }
    }

    /**
     * Asserts that an array contains no {@code null} elements.
     *
     * @param array           the array to check
     * @param messageSupplier a supplier for the exception message to use if the assertion fails
     * @throws IllegalArgumentException if the object array contains a {@code null} element
     */
    public static void noNullElements( Object[] array, Supplier<String> messageSupplier) {
        if (array != null) {
            for (Object element : array) {
                if (element == null) {
                    throw new IllegalArgumentException(nullSafeGet(messageSupplier));
                }
            }
        }
    }

    /**
     * Asserts that a collection contains elements
     *
     * @param collection the collection to check
     * @param message    the exception message to use if the assertion fails
     * @throws IllegalArgumentException if the collection is {@code null} or contains no elements
     */
    public static void notEmpty( Collection<?> collection, String message) {
        if (Objects.isEmpty(collection)) {
            throw new IllegalArgumentException(message);
        }
    }

    /**
     * Asserts that a collection contains elements
     *
     * @param collection      the collection to check
     * @param messageSupplier a supplier for the exception message to use if the assertion fails
     * @throws IllegalArgumentException if the collection is {@code null} or contains no elements
     */
    public static void notEmpty( Collection<?> collection, Supplier<String> messageSupplier) {
        if (Objects.isEmpty(collection)) {
            throw new IllegalArgumentException(nullSafeGet(messageSupplier));
        }
    }


    /**
     * Asserts that a collection contains no {@code null} elements.
     *
     * @param collection the collection to check
     * @param message    the exception message to use if the assertion fails
     * @throws IllegalArgumentException if the collection contains a {@code null} element
     */
    public static void noNullElements( Collection<?> collection, String message) {
        if (collection != null) {
            for (Object element : collection) {
                if (element == null) {
                    throw new IllegalArgumentException(message);
                }
            }
        }
    }

    /**
     * Asserts that a collection contains no {@code null} elements.
     *
     * @param collection      the collection to check
     * @param messageSupplier a supplier for the exception message to use if the assertion fails
     * @throws IllegalArgumentException if the collection contains a {@code null} element
     */
    public static void noNullElements( Collection<?> collection, Supplier<String> messageSupplier) {
        if (collection != null) {
            for (Object element : collection) {
                if (element == null) {
                    throw new IllegalArgumentException(nullSafeGet(messageSupplier));
                }
            }
        }
    }

    /**
     * Asserts that a Map contains entries; that is, it must not be {@code null}
     * and must contain at least one entry.
     * <pre class="code">Asserts.notEmpty(map, "Map must contain entries");</pre>
     *
     * @param map     the map to check
     * @param message the exception message to use if the assertion fails
     * @throws IllegalArgumentException if the map is {@code null} or contains no entries
     */
    public static void notEmpty( Map<?, ?> map, String message) {
        if (Objects.isEmpty(map)) {
            throw new IllegalArgumentException(message);
        }
    }

    /**
     * Asserts that a Map contains entries; that is, it must not be {@code null}
     * and must contain at least one entry.
     * <pre class="code">
     * Asserts.notEmpty(map, () -&gt; "The " + mapType + " map must contain entries");
     * </pre>
     *
     * @param map             the map to check
     * @param messageSupplier a supplier for the exception message to use if the
     *                        assertion fails
     * @throws IllegalArgumentException if the map is {@code null} or contains no entries
     * @since 5.0
     */
    public static void notEmpty( Map<?, ?> map, Supplier<String> messageSupplier) {
        if (Objects.isEmpty(map)) {
            throw new IllegalArgumentException(nullSafeGet(messageSupplier));
        }
    }

    /**
     * Asserts that a Map contains entries; that is, it must not be {@code null}
     * and must contain at least one entry.
     *
     * @deprecated as of 4.3.7, in favor of {@link #notEmpty(Map, String)}
     */
    @Deprecated
    public static void notEmpty( Map<?, ?> map) {
        notEmpty(map, "[Assertion failed] - this map must not be empty; it must contain at least one entry");
    }

    /**
     * Asserts that the provided object is an instance of the provided class.
     * <pre class="code">Asserts.instanceOf(Foo.class, foo, "Foo expected");</pre>
     *
     * @param type    the type to check against
     * @param obj     the object to check
     * @param message a message which will be prepended to provide further context.
     *                If it is empty or ends in ":" or ";" or "," or ".", a full exception message
     *                will be appended. If it ends in a space, the name of the offending object's
     *                type will be appended. In any other case, a ":" with a space and the name
     *                of the offending object's type will be appended.
     * @throws IllegalArgumentException if the object is not an instance of type
     */
    public static void isInstanceOf(Class<?> type,  Object obj, String message) {
        notNull(type, "Type to check against must not be null");
        if (!type.isInstance(obj)) {
            instanceCheckFailed(type, obj, message);
        }
    }

    /**
     * Asserts that the provided object is an instance of the provided class.
     * <pre class="code">
     * Asserts.instanceOf(Foo.class, foo, () -&gt; "Processing " + Foo.class.getSimpleName() + ":");
     * </pre>
     *
     * @param type            the type to check against
     * @param obj             the object to check
     * @param messageSupplier a supplier for the exception message to use if the
     *                        assertion fails. See {@link #isInstanceOf(Class, Object, String)} for details.
     * @throws IllegalArgumentException if the object is not an instance of type
     * @since 5.0
     */
    public static <T> T isInstanceOf(Class<T> type, Object obj, Supplier<String> messageSupplier) {
        notNull(type, "Type to check against must not be null");
        if (!type.isInstance(obj)) throw createInstanceCheckFailed(type, obj, nullSafeGet(messageSupplier));
        else return type.cast(obj);
    }

    /**
     * Asserts that the provided object is an instance of the provided class.
     * <pre class="code">Asserts.instanceOf(Foo.class, foo);</pre>
     *
     * @param type the type to check against
     * @param obj  the object to check
     * @throws IllegalArgumentException if the object is not an instance of type
     */
    public static <T> T isInstanceOf(Class<T> type, Object obj) {
        return isInstanceOf(type, obj, () -> obj + " not instanceOf " + type.getName());
    }

    /**
     * Asserts that {@code superType.isAssignableFrom(subType)} is {@code true}.
     * <pre class="code">Asserts.isAssignable(Number.class, myClass, "Number expected");</pre>
     *
     * @param superType the super type to check against
     * @param subType   the sub type to check
     * @param message   a message which will be prepended to provide further context.
     *                  If it is empty or ends in ":" or ";" or "," or ".", a full exception message
     *                  will be appended. If it ends in a space, the name of the offending sub type
     *                  will be appended. In any other case, a ":" with a space and the name of the
     *                  offending sub type will be appended.
     * @throws IllegalArgumentException if the classes are not assignable
     */
    public static void isAssignable(Class<?> superType,  Class<?> subType, String message) {
        notNull(superType, "Super type to check against must not be null");
        if (subType == null || !superType.isAssignableFrom(subType)) {
            assignableCheckFailed(superType, subType, message);
        }
    }

    /**
     * Asserts that {@code superType.isAssignableFrom(subType)} is {@code true}.
     * <pre class="code">
     * Asserts.isAssignable(Number.class, myClass, () -&gt; "Processing " + myAttributeName + ":");
     * </pre>
     *
     * @param superType       the super type to check against
     * @param subType         the sub type to check
     * @param messageSupplier a supplier for the exception message to use if the
     *                        assertion fails. See {@link #isAssignable(Class, Class, String)} for details.
     * @throws IllegalArgumentException if the classes are not assignable
     * @since 5.0
     */
    public static void isAssignable(Class<?> superType,  Class<?> subType, Supplier<String> messageSupplier) {
        notNull(superType, "Super type to check against must not be null");
        if (subType == null || !superType.isAssignableFrom(subType)) {
            assignableCheckFailed(superType, subType, nullSafeGet(messageSupplier));
        }
    }

    /**
     * Asserts that {@code superType.isAssignableFrom(subType)} is {@code true}.
     * <pre class="code">Asserts.isAssignable(Number.class, myClass);</pre>
     *
     * @param superType the super type to check
     * @param subType   the sub type to check
     * @throws IllegalArgumentException if the classes are not assignable
     */
    public static void isAssignable(Class<?> superType, Class<?> subType) {
        isAssignable(superType, subType, "");
    }


    private static void instanceCheckFailed(Class<?> type,  Object obj,  String msg) {
        throw createInstanceCheckFailed(type, obj, msg);
    }

    public static IllegalArgumentException createInstanceCheckFailed(Class<?> type, Object obj, String msg) {
        String className = (obj != null ? obj.getClass().getName() : "null");
        String result = "";
        boolean defaultMessage = true;
        if (Objects.hasLength(msg)) {
            if (endsWithSeparator(msg)) {
                result = msg + " ";
            } else {
                result = messageWithTypeName(msg, className);
                defaultMessage = false;
            }
        }
        if (defaultMessage) {
            result = result + ("Object of class [" + className + "] must be an instance of " + type);
        }

        return new IllegalArgumentException(result);

    }

    private static void assignableCheckFailed(Class<?> superType,  Class<?> subType,  String msg) {
        String result = "";
        boolean defaultMessage = true;
        if (Objects.hasLength(msg)) {
            if (endsWithSeparator(msg)) {
                result = msg + " ";
            } else {
                result = messageWithTypeName(msg, subType);
                defaultMessage = false;
            }
        }
        if (defaultMessage) {
            result = result + (subType + " is not assignable to " + superType);
        }
        throw new IllegalArgumentException(result);
    }

    private static boolean endsWithSeparator(String msg) {
        return (msg.endsWith(":") || msg.endsWith(";") || msg.endsWith(",") || msg.endsWith("."));
    }

    private static String messageWithTypeName(String msg,  Object typeName) {
        return msg + (msg.endsWith(" ") ? "" : ": ") + typeName;
    }

    
    private static <T> T nullSafeGet( Supplier<T> messageSupplier) {
        return messageSupplier != null ? messageSupplier.get() : null;
    }

    public static String createParameterIsNullExceptionMessage(String paramName) {
        StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();
        StackTraceElement caller = stackTraceElements[4];
        String className = caller.getClassName();
        String methodName = caller.getMethodName();
        return "Parameter specified as non-null is null: method " + className + "." + methodName + ", parameter " + paramName;
    }


    public static void checkNotNullParameter(Object object, String paramName) {
        if (object == null) {
            String message = createParameterIsNullExceptionMessage(paramName);
            throw new NullPointerException(message);
        }
    }

    public static void allNotEmpty(String[] array, String message) {
        boolean isEmpty = Stream.of(array).anyMatch(Objects::isEmpty);
        if (isEmpty) throw new IllegalArgumentException(message);
    }

    public static String regexMatches(String regex, String input) {
        return regexMatches(regex, input, "The input does not match pattern");
    }

    public static String regexMatches(String regex, String input, String message) {
        if (!Objects.regexMatches(regex, input)) throw new IllegalArgumentException(message);
        else return input;
    }

    public static String notBlank(String object) {
        if (Objects.notBlank(object)) return object;
        else throw Objects.newIllegal("The string must be not blank");
    }

    public static String notBlank(String object, String message, Object... args) {
        if (Objects.notBlank(object)) return object;
       else throw Objects.newIllegal(message, args);
    }



    /**
     * @param objects the array object check null
     * @throws NullPointerException error if any object is null
     */
    public static void anyNull(Object[] objects, String message) {
        boolean isNull = Stream.of(objects).anyMatch(Objects::isNull);
        if (isNull) throw new NullPointerException(message);
    }

    public static <T> T castObject(Object object, Class<T> classObject) {
        if (classObject.isInstance(object)) return (T) object;
        else throw new IllegalArgumentException("object not instanceof " + classObject.getName());

    }

    public static Enum<?> isEnum(Object object, String message, Object... args) {
        if (object instanceof Enum<?> en) return en;
        else throw new IllegalArgumentException(Objects.formatString(message, args));
    }

    public static void isNotArray(Object object, String message) {
        boolean isArray = Objects.isArray(object) || Objects.isCollection(object);
        if (isArray) throw new IllegalArgumentException(message);
    }

    public static <T extends Comparable> T isGOE(T first, T second, String message) {
        if (Objects.isGreaterThanOrEq(first, second)) return first;
        else throw new IllegalArgumentException(message);
    }

    public static void notSupport(Class<?> aClass) {
    }

    /**
     * Handle the given reflection exception.
     * <p>Should only be called if no checked exception is expected to be thrown
     * by a target method, or if an error occurs while accessing a method or field.
     * <p>Throws the underlying RuntimeException or Error in case of an
     * InvocationTargetException with such a root cause. Throws an
     * IllegalStateException with an appropriate message or
     * UndeclaredThrowableException otherwise.
     *
     * @param ex the reflection exception to handle
     */
    public static void handleReflectionException(Exception ex) {
        if (ex instanceof NoSuchMethodException) {
            throw new IllegalStateException("Method not found: " + ex.getMessage());
        }

        if (ex instanceof IllegalAccessException) {
            throw new IllegalStateException("Could not access method or field: " + ex.getMessage());
        }

        if (ex instanceof InvocationTargetException invocationTargetException) {
            handleInvocationTargetException(invocationTargetException);
        }

        if (ex instanceof RuntimeException runtimeException) {
            throw runtimeException;
        }

        throw new UndeclaredThrowableException(ex);
    }


    /**
     * Handle the given invocation target exception. Should only be called if no
     * checked exception is expected to be thrown by the target method.
     * <p>Throws the underlying RuntimeException or Error in case of such a root
     * cause. Throws an UndeclaredThrowableException otherwise.
     *
     * @param ex the invocation target exception to handle
     */
    public static void handleInvocationTargetException(InvocationTargetException ex) {
        rethrowRuntimeException(ex.getTargetException());
    }

    /**
     * Rethrow the given {@link Throwable exception}, which is presumably the
     * <em>target exception</em> of an {@link InvocationTargetException}.
     * Should only be called if no checked exception is expected to be thrown
     * by the target method.
     * <p>Rethrows the underlying exception cast to a {@link RuntimeException} or
     * {@link Error} if appropriate; otherwise, throws an
     * {@link UndeclaredThrowableException}.
     *
     * @param ex the exception to rethrow
     * @throws RuntimeException the rethrown exception
     */
    public static void rethrowRuntimeException(Throwable ex) {
        if (ex instanceof RuntimeException runtimeException) {
            throw runtimeException;
        }
        if (ex instanceof Error error) {
            throw error;
        }
        throw new UndeclaredThrowableException(ex);
    }


    /**
     * Throw IllegalArgumentException if the condition is false, otherwise return the value.
     * @param <T>         the value type
     * @param condition   the condition about the parameter to check
     * @param returnValue the value of the argument
     * @param message     the message if error
     * @throws IllegalArgumentException if the condition is false
     */
    public static <T> T isTrue(final boolean condition, final T returnValue, final String message) {
        if (!condition) {throw new IllegalArgumentException(message);}
        return returnValue;
    }

    public static <T> T notNullWithThrowable(T object, Supplier<RuntimeException> throwableSupplier) {
        if(object == null) throw throwableSupplier.get();
        else return object;
    }

    public static Path isFile(Path path) {
        if(path == null) return null;
        else return isFile(path, "The path `%s` is null or not file", path.toString());
    }
    public static Path isFile(Path path, String msg, Object...args) {
        if(Objects.isNull(path) || Files.isDirectory(path)) {
            throw new IllegalArgumentException(buildMessage(msg, args));
        }
        return path;
    }

    public static <T> List<T> isEmpty(List<T> list, String message) {
        if(list == null || !list.isEmpty()) throw new IllegalArgumentException(message);
        else return list;
    }

    public static void allNotNull(Object... objects) {
        int index = IntStream.range(0, objects.length).filter(pos -> Objects.isNull(objects[pos])).findFirst().orElse(-1);
        if(index != -1) throw Objects.newIllegal("The value at [%s] is null", index);
    }
}