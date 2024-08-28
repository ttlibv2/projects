package vn.conyeu.demo.querydsl;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;

public class Utils {
    private static final Set<Class<?>> UNMODIFIABLE_TYPES;
    private static final Map<Class, Class> PRIMITIVE_TO_WRAPPER_TYPE;
    private static final Map<Class, Class> WRAPPER_TO_PRIMITIVE_TYPE;
    private static final AnnotatedElement EMPTY = new Annotations();

    static {
        Set<Class<?>> unmodifiableTypes = new HashSet<>();
        unmodifiableTypes.add(Collections.emptyList().getClass());
        unmodifiableTypes.add(Collections.emptySet().getClass());
        unmodifiableTypes.add(Collections.emptyNavigableSet().getClass());
        unmodifiableTypes.add(Collections.emptySortedSet().getClass());
        unmodifiableTypes.add(Collections.emptyMap().getClass());
        unmodifiableTypes.add(Collections.emptySortedMap().getClass());
        unmodifiableTypes.add(Collections.emptyNavigableMap().getClass());
        unmodifiableTypes.add(Collections.singleton(1).getClass());
        unmodifiableTypes.add(Collections.singletonList(1).getClass());
        unmodifiableTypes.add(Collections.singletonMap(1, 1).getClass());
        unmodifiableTypes.add(Collections.unmodifiableNavigableSet(Collections.emptyNavigableSet()).getClass());
        unmodifiableTypes.add(Collections.emptySortedSet().getClass());
        unmodifiableTypes.add(Collections.unmodifiableSortedMap(Collections.emptySortedMap()).getClass());
        unmodifiableTypes.add(Collections.unmodifiableNavigableMap(Collections.emptyNavigableMap()).getClass());

        try {
            unmodifiableTypes.add(Class.forName("com.google.common.collect.ImmutableSet"));
            unmodifiableTypes.add(Class.forName("com.google.common.collect.ImmutableList"));
            unmodifiableTypes.add(Class.forName("com.google.common.collect.ImmutableMap"));
        } catch (ClassNotFoundException e) {
            // Nothing happens
        }

        try {
            unmodifiableTypes.add(Class.forName("java.util.ImmutableCollections$AbstractImmutableCollection"));
            unmodifiableTypes.add(Class.forName("java.util.ImmutableCollections$AbstractImmutableMap"));
        } catch (ClassNotFoundException e) {
            // Nothing happens
        }

        UNMODIFIABLE_TYPES = Collections.unmodifiableSet(unmodifiableTypes);

        //--------------------
        Map<Class<?>, Class<?>> primToWrap = new HashMap<>(16);
        Map<Class<?>, Class<?>> wrapToPrim = new HashMap<>(16);
        add(primToWrap, wrapToPrim, boolean.class, Boolean.class);
        add(primToWrap, wrapToPrim, byte.class, Byte.class);
        add(primToWrap, wrapToPrim, char.class, Character.class);
        add(primToWrap, wrapToPrim, double.class, Double.class);
        add(primToWrap, wrapToPrim, float.class, Float.class);
        add(primToWrap, wrapToPrim, int.class, Integer.class);
        add(primToWrap, wrapToPrim, long.class, Long.class);
        add(primToWrap, wrapToPrim, short.class, Short.class);
        add(primToWrap, wrapToPrim, void.class, Void.class);

        PRIMITIVE_TO_WRAPPER_TYPE = Collections.unmodifiableMap(primToWrap);
        WRAPPER_TO_PRIMITIVE_TYPE = Collections.unmodifiableMap(wrapToPrim);
    }


    /**
     * Returns true if the type is a known unmodifiable type.
     *
     * @param clazz the type
     * @return true if the type is a known unmodifiable type
     */
    public static boolean isUnmodifiableType(Class<?> clazz) {
        for (; clazz != null; clazz = clazz.getSuperclass()) {
            if (UNMODIFIABLE_TYPES.contains(clazz)) {
                return true;
            }
        }
        return false;
    }


    /**
     * Return an unmodifiable copy of a list, or the same list if its already an unmodifiable type.
     *
     * @param list the list
     * @param <T> element type
     * @return unmodifiable copy of a list, or the same list if its already an unmodifiable type
     */
    public static <T> List<T> unmodifiableList(List<T> list) {
        if (isUnmodifiableType(list.getClass())) {
            return list;
        }

        return switch (list.size()) {
            case 0 -> Collections.emptyList();
            case 1 -> Collections.singletonList(list.get(0));
            default -> List.copyOf(list);
        };

    }

    public static <T> Class<T> wrap(Class<T> type) {
        return PRIMITIVE_TO_WRAPPER_TYPE.getOrDefault(type, type);
    }

    public static <T> Class<T> unwrap(Class<T> type) {
        return WRAPPER_TO_PRIMITIVE_TYPE.getOrDefault(type, type);
    }


    /**
     * Get the potentially wrapped expression
     * @param expr expression to analyze
     * @return inner expression
     */
    public static <T extends Expression> T extract(T expr) {
        if(expr == null) return null;

        final Class clazz = expr.getClass();
        if(clazz == PathImpl.class) return expr;
        else if(clazz == ConstantImpl.class) return expr;
        else if (clazz == PredicateOperation.class) return expr;
        else return (Expression<T>) expr.accept(ExtractorVisitor.DEFAULT, null);
    }

    static void add(Map<Class<?>, Class<?>> forward, Map<Class<?>, Class<?>> backward, Class<?> key, Class<?> value) {
        forward.put(key, value);
        backward.put(value, key);
    }

    public static AnnotatedElement getAnnotatedElement(Class<?> beanClass, String propertyName, Class<?> propertyClass) {
        Field field = getFieldOrNull(beanClass, propertyName);
        Method method = getGetterOrNull(beanClass, propertyName, propertyClass);
        if (field == null || field.getAnnotations().length == 0) {
            return (method != null && method.getAnnotations().length > 0) ? method : EMPTY;
        } else if (method == null || method.getAnnotations().length == 0) {
            return field;
        } else {
            return new Annotations(field, method);
        }
    }
    public static Field getFieldOrNull(Class<?> beanClass, String propertyName) {
        while (beanClass != null && !beanClass.equals(Object.class)) {
            try {
                return beanClass.getDeclaredField(propertyName);
            } catch (SecurityException | NoSuchFieldException e) {
                // skip
            }
            beanClass = beanClass.getSuperclass();
        }
        return null;
    }
    public static Method getGetterOrNull(Class<?> beanClass, String name) {
        Method method = getGetterOrNull(beanClass, name, Object.class);
        if (method != null) {
            return method;
        } else {
            return getGetterOrNull(beanClass, name, Boolean.class);
        }
    }
    public static Method getGetterOrNull(Class<?> beanClass, String name, Class<?> type) {
        String methodName = ((type.equals(Boolean.class) || type.equals(boolean.class)) ? "is" : "get") + capitalize(name);
        while (beanClass != null && !beanClass.equals(Object.class)) {
            try {
                return beanClass.getDeclaredMethod(methodName);
            } catch (SecurityException | NoSuchMethodException e) { // skip
            }
            beanClass = beanClass.getSuperclass();
        }
        return null;

    }

    public static String capitalize(String name) {
        if (name.length() > 1 && Character.isUpperCase(name.charAt(1))) {
            return name;
        } else if (name.length() > 1) {
            return Character.toUpperCase(name.charAt(0)) + name.substring(1);
        } else {
            return name.toUpperCase();
        }
    }


    /**
     * Annotations is a merging adapter for the {@link AnnotatedElement} interface
     *
     * @author tiwe
     *
     */
    public static class Annotations implements AnnotatedElement {

        private final Map<Class<? extends Annotation>,Annotation> annotations = new HashMap<Class<? extends Annotation>,Annotation>();

        public Annotations(AnnotatedElement... elements) {
            for (AnnotatedElement element : elements) {
                addAnnotations(element);
            }
        }

        @Override
        public <T extends Annotation> T getAnnotation(Class<T> annotationClass) {
            return (T) annotations.get(annotationClass);
        }

        @Override
        public Annotation[] getAnnotations() {
            return annotations.values().toArray(new Annotation[0]);
        }

        @Override
        public Annotation[] getDeclaredAnnotations() {
            return getAnnotations();
        }

        @Override
        public boolean isAnnotationPresent(Class<? extends Annotation> annotationClass) {
            return annotations.containsKey(annotationClass);
        }

        public void addAnnotation(Annotation annotation) {
            if (annotation != null) {
                annotations.put(annotation.annotationType(), annotation);
            }

        }

        public void addAnnotations(AnnotatedElement element) {
            for (Annotation annotation : element.getAnnotations()) {
                annotations.put(annotation.annotationType(), annotation);
            }
        }
    }
}