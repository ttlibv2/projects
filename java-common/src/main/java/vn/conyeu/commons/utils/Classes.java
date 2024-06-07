package vn.conyeu.commons.utils;

import org.apache.commons.lang3.ClassUtils;
import vn.conyeu.commons.exception.ClassException;

import java.beans.Introspector;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodHandles.Lookup;
import java.lang.invoke.MethodType;
import java.lang.invoke.VarHandle;
import java.lang.reflect.*;
import java.util.*;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public final class Classes {

    public static final Class[] NO_PARAM_SIGNATURE = new Class[0];
    public static final Class[] SINGLE_OBJECT_PARAM_SIGNATURE = new Class[] { Object.class };
    private static final Method OBJECT_EQUALS;
    private static final Method OBJECT_HASHCODE;

    /**
     * The package separator character: {@code '.'}.
     */
    private static final char PACKAGE_SEPARATOR = '.';

    /**
     * The path separator character: {@code '/'}.
     */
    private static final char PATH_SEPARATOR = '/';

    /**
     * The nested class separator character: {@code '$'}.
     */
    private static final char NESTED_CLASS_SEPARATOR = '$';

    /**
     * The CGLIB class separator: {@code "$$"}.
     */
    public static final String CGLIB_CLASS_SEPARATOR = "$$";

    private static final Class<?> RECORD_CLASS;
    private static boolean isInitializePrimitive = false;

    /**
     */
    private static final ClassLoaderAccessor THREAD_CL_ACCESSOR = new ExceptionIgnoringAccessor() {
        @Override
        protected ClassLoader doGetClassLoader() {
            return Thread.currentThread().getContextClassLoader();
        }
    };

    /**
     */
    private static final ClassLoaderAccessor CLASS_CL_ACCESSOR = new ExceptionIgnoringAccessor() {
        @Override
        protected ClassLoader doGetClassLoader() {
            return Classes.class.getClassLoader();
        }
    };

    /**
     */
    private static final ClassLoaderAccessor SYSTEM_CL_ACCESSOR = new ExceptionIgnoringAccessor() {
        @Override
        protected ClassLoader doGetClassLoader() {
            return ClassLoader.getSystemClassLoader();
        }
    };

    static {

        try {
            OBJECT_EQUALS = extractEqualsMethod( Object.class );
            OBJECT_HASHCODE = extractHashCodeMethod( Object.class );
        }
        catch ( Exception e ) {
            throw new IllegalArgumentException( "Could not find Object.equals() or Object.hashCode()", e );
        }

        try {
            RECORD_CLASS = Class.forName("java.lang.Record");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }



    }

    public static boolean isPrimitiveOrWrapper(final Class<?> type) {
        if(String.class.equals(type)) return true;
        return ClassUtils.isPrimitiveOrWrapper(type);
    }


    /**
     * Get the class name without the qualified package name.
     *
     * @param className the className to get the short name for
     * @return the class name of the class without the package name
     * @throws IllegalArgumentException if the className is empty
     */
    public static String getShortName(String className) {
        Asserts.hasLength(className, "Class name must not be empty");
        int lastDotIndex = className.lastIndexOf(PACKAGE_SEPARATOR);
        int nameEndIndex = className.indexOf(CGLIB_CLASS_SEPARATOR);
        if (nameEndIndex == -1) nameEndIndex = className.length();
        String shortName = className.substring(lastDotIndex + 1, nameEndIndex);
        shortName = shortName.replace(NESTED_CLASS_SEPARATOR, PACKAGE_SEPARATOR);
        return shortName;
    }

    /**
     * Get the class name without the qualified package name.
     *
     * @param clazz the class to get the short name for
     * @return the class name of the class without the package name
     */
    public static String getShortName(Class<?> clazz) {
        return getShortName(getQualifiedName(clazz));
    }

    /**
     * Return the short string name of a Java class in uncapitalized JavaBeans
     * property format. Strips the outer class name in case of a nested class.
     *
     * @param clazz the class
     * @return the short name rendered in a standard JavaBeans property format
     * @see Introspector#decapitalize(String)
     */
    public static String getShortNameAsProperty(Class<?> clazz) {
        String shortName = getShortName(clazz);
        int dotIndex = shortName.lastIndexOf(PACKAGE_SEPARATOR);
        if (dotIndex != -1) shortName = shortName.substring(dotIndex + 1);
        return Introspector.decapitalize(shortName);
    }

    /**
     * Return the qualified name of the given class: usually simply
     * the class name, but component type class name + "[]" for arrays.
     *
     * @param clazz the class
     * @return the qualified name of the class
     */
    public static String getQualifiedName(Class<?> clazz) {
        Asserts.notNull(clazz, "Class must not be null");
        return clazz.getTypeName();
    }


    public static Class forName(String className) {
        try {
            return Class.forName(className);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }


    public static MethodHandle findStaticMethod(Class refClass, String method, Class[] parameterCls) {
        try {
            MethodType methodType = MethodType.methodType(refClass, parameterCls);
            return MethodHandles.lookup().findStatic(refClass, method, methodType);
        } catch (NoSuchMethodException ex) {
            return null;
        } catch (IllegalAccessException ex) {
            throw new RuntimeException("if access checking fails, or if the method is not static, " +
                    "or if the method's variable arity modifier bit is set and asVarargsCollector fails", ex);
        }
    }

    public static VarHandle findPrivateField(Class objectCls, String fieldName, Class fieldCls) {
        try {
            Lookup lookup = MethodHandles.lookup();
            Lookup privateLookup = MethodHandles.privateLookupIn(objectCls, lookup);
            return privateLookup.findVarHandle(objectCls, fieldName, fieldCls);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Encapsulation of getting hold of a class's {@link Object#equals equals}  method.
     * @param clazz The class from which to extract the equals' method.
     * @return The equals method reference
     * @throws NoSuchMethodException Should indicate an attempt to extract equals method from interface.
     */
    public static Method extractEqualsMethod(Class clazz) throws NoSuchMethodException {
        return clazz.getMethod( "equals", SINGLE_OBJECT_PARAM_SIGNATURE );
    }

    /**
     * Encapsulation of getting hold of a class's {@link Object#hashCode hashCode} method.
     * @param clazz The class from which to extract the hashCode method.
     * @return The hashCode method reference
     * @throws NoSuchMethodException Should indicate an attempt to extract hashCode method from interface.
     */
    public static Method extractHashCodeMethod(Class clazz) throws NoSuchMethodException {
        return clazz.getMethod( "hashCode", NO_PARAM_SIGNATURE );
    }

    /**
     * Determine if the given class defines an {@link Object#equals} override.
     * @param clazz The class to check
     * @return True if clazz defines an equals override.
     */
    public static boolean overridesEquals(Class clazz) {
        try {
            Method equals = extractEqualsMethod( clazz );
            return !OBJECT_EQUALS.equals( equals );
        }
        catch ( NoSuchMethodException exp ) {return false;}
    }

    /**
     * Determine if the given class defines a {@link Object#hashCode} override.
     *
     * @param clazz The class to check
     * @return True if clazz defines an hashCode override.
     */
    public static boolean overridesHashCode(Class clazz) {
        try {
            Method hashCode = extractHashCodeMethod( clazz );
            return !OBJECT_HASHCODE.equals( hashCode );
        }
        catch ( NoSuchMethodException exp ) {return false;}

    }

    /**
     * Determine if the given class implements the given interface.
     *
     * @param clazz The class to check
     * @param infCls The interface to check it against.
     * @return True if the class does implement the interface, false otherwise.
     */
    public static boolean implementsInterface(Class clazz, Class infCls) {
        Asserts.isTrue(infCls.isInterface(), "Interface to check was not an interface");
        return infCls.isAssignableFrom( clazz );
    }

    /**
     * Perform resolution of a class name.
     * <p>
     * Here we first check the context classloader, if one, before delegating to
     * {@link Class#forName(String, boolean, ClassLoader)} using the caller's classloader
     *
     * @param name The class name
     * @param caller The class from which this call originated (in order to access that class's loader).
     * @return The class reference.
     * @throws ClassNotFoundException From {@link Class#forName(String, boolean, ClassLoader)}.
     */
    public static Class classForName(String name, Class caller) throws ClassNotFoundException {
        try {
            ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
            if ( classLoader != null ) return classLoader.loadClass( name );
        }
        catch ( Throwable ignore ) {
        }
        return Class.forName( name, true, caller.getClassLoader() );
    }

    /**
     * Is this member publicly accessible.
     *
     * @param clazz The class which defines the member
     * @param member The member.
     * @return True if the member is publicly accessible, false otherwise.
     */
    public static boolean isPublic(Class clazz, Member member) {
        return Modifier.isPublic( member.getModifiers() ) && Modifier.isPublic( clazz.getModifiers() );
    }

    /**
     * Retrieve the default (no arg) constructor from the given class.
     * @param clazz The class for which to retrieve the default ctor.
     * @return The default constructor.
     */
    public static <T> Constructor<T> getDefaultConstructor(Class<T> clazz)  {
        if ( isAbstractClass( clazz ) ) return null;

        try {
            Constructor<T> constructor = clazz.getDeclaredConstructor( NO_PARAM_SIGNATURE );
            ensureAccessibility( constructor );
            return constructor;
        }
        catch ( NoSuchMethodException nme ) {
            String msg = "Object class [%s] must declare a default (no-argument) constructor";
            throw Objects.newIllegal(msg, clazz.getName());
        }
    }

    /**
     * Determine if the given class is declared abstract.
     *
     * @param clazz The class to check.
     * @return True if the class is abstract, false otherwise.
     */
    public static boolean isAbstractClass(Class clazz) {
        int modifier = clazz.getModifiers();
        return Modifier.isAbstract(modifier) || Modifier.isInterface(modifier);
    }

    /**
     * Determine is the given class is declared final.
     *
     * @param clazz The class to check.
     * @return True if the class is final, false otherwise.
     */
    public static boolean isFinalClass(Class clazz) {
        return Modifier.isFinal( clazz.getModifiers() );
    }

    public static <T> Constructor<T> getConstructor(Class<T> clazz, Class... constructorArgs) {
        Constructor<T> constructor = null;
        try {
            constructor = clazz.getDeclaredConstructor( constructorArgs );
            try {ensureAccessibility( constructor );}
            catch ( SecurityException e ) {constructor = null;}
        }
        catch ( NoSuchMethodException ignore ) {}
        return constructor;
    }

    public static Method getMethod(Class clazz, Method method) {
        try {
            return clazz.getMethod( method.getName(), method.getParameterTypes() );
        }
        catch (Exception e) {
            return null;
        }
    }

    public static Method getMethod(Class clazz, String methodName, Class... paramTypes) {
        try {return clazz.getMethod( methodName, paramTypes );}
        catch (Exception e) {return null;}
    }

    public static Field findField(Class containerClass, String propertyName) {
        if ( containerClass == null ) {throw new IllegalArgumentException( "Class on which to find field [" + propertyName + "] cannot be null" );}
        else if ( containerClass == Object.class ) {throw new IllegalArgumentException( "Illegal attempt to locate field [" + propertyName + "] on Object.class" );}

        Field field = locateField( containerClass, propertyName );

        if ( field == null ) {
            String msg = "Could not locate field name [%s] on class [%s]";
            throw Objects.newIllegal(msg, propertyName, containerClass.getName());
        }

        ensureAccessibility( field );

        return field;
    }

    public static void ensureAccessibility(AccessibleObject accessibleObject) {
        accessibleObject.trySetAccessible( );
    }

    private static Field locateField(Class clazz, String propertyName) {
        if ( clazz == null || Object.class.equals( clazz ) ) {
            return null;
        }

        try {
            Field field = clazz.getDeclaredField( propertyName );
            if ( !isStaticField( field ) ) return field;

            return locateField( clazz.getSuperclass(), propertyName );
        }
        catch ( NoSuchFieldException nsfe ) {
            return locateField( clazz.getSuperclass(), propertyName );
        }
    }

    public static boolean isStaticField(Field field) {
        return field != null && ( field.getModifiers() & Modifier.STATIC ) == Modifier.STATIC;
    }

    public static Method findGetterMethod(Class containerClass, String propertyName) {
        Class checkClass = containerClass;
        Method getter = null;

        if ( isRecord( containerClass ) ) {
            try {
                getter = containerClass.getMethod( propertyName, NO_PARAM_SIGNATURE );
            }
            catch (NoSuchMethodException e) {
                // Ignore
            }
        }

        // check containerClass, and then its super types (if any)
        while ( getter == null && checkClass != null ) {
            if ( checkClass.equals( Object.class ) ) {
                break;
            }

            getter = getGetterOrNull( checkClass, propertyName );

            // if no getter found yet, check all implemented interfaces
            if ( getter == null ) {
                getter = getGetterOrNull( checkClass.getInterfaces(), propertyName );
            }

            checkClass = checkClass.getSuperclass();
        }


        if ( getter == null ) {
            throw Objects.newIllegal(
                            "Could not locate getter method for property [%s#%s]",
                            containerClass.getName(),
                            propertyName
                    );
        }

        ensureAccessibility( getter );

        return getter;
    }

    private static Method getGetterOrNull(Class[] interfaces, String propertyName) {
        Method getter = null;
        for ( int i = 0; getter == null && i < interfaces.length; ++i ) {
            final Class anInterface = interfaces[i];
            getter = getGetterOrNull( anInterface, propertyName );
            if ( getter == null ) {
                // if no getter found yet, check all implemented interfaces of interface
                getter = getGetterOrNull( anInterface.getInterfaces(), propertyName );
            }
        }
        return getter;
    }

    /**
     * Find the method that can be used as the setter for this property.
     *
     * @param containerClass The Class which contains the property
     * @param propertyName The name of the property
     *
     * @return The getter method, or {@code null} if there is none.
     */
    public static Method getGetterOrNull(Class containerClass, String propertyName) {
        if ( isRecord( containerClass ) ) {
            try {
                return containerClass.getMethod( propertyName, NO_PARAM_SIGNATURE );
            }
            catch (NoSuchMethodException e) {
                // Ignore
            }
        }

        for ( Method method : containerClass.getDeclaredMethods() ) {
            // if the method has parameters, skip it
            if ( method.getParameterCount() != 0 ) {
                continue;
            }

            // if the method is a "bridge", skip it
            if ( method.isBridge() ) {
                continue;
            }

            // transientCls
            if (getAnnotationTransient(method) != null ) {
                continue;
            }

            if ( Modifier.isStatic( method.getModifiers() ) ) {
                continue;
            }

            final String methodName = method.getName();

            // try "get"
            if ( methodName.startsWith( "get" ) ) {
                final String stemName = methodName.substring( 3 );
                final String decapitalizedStemName = Introspector.decapitalize( stemName );
                if ( stemName.equals( propertyName ) || decapitalizedStemName.equals( propertyName ) ) {
                    verifyNoIsVariantExists( containerClass, propertyName, method, stemName );
                    return method;
                }

            }

            // if not "get", then try "is"
            if ( methodName.startsWith( "is" ) ) {
                final String stemName = methodName.substring( 2 );
                String decapitalizedStemName = Introspector.decapitalize( stemName );
                if ( stemName.equals( propertyName ) || decapitalizedStemName.equals( propertyName ) ) {
                    // not sure that this can ever really happen given the handling of "get" above.
                    // but be safe
                    verifyNoGetVariantExists( containerClass, propertyName, method, stemName );
                    return method;
                }
            }
        }

        return null;
    }

    private static Annotation getAnnotationTransient(Method method) {
        try{
            Class cls = Class.forName("jakarta.persistence.Transient");
            return method.getAnnotation(cls);
        }
        catch (ClassNotFoundException exp) { return null; }
    }

    public static void verifyNoIsVariantExists(
            Class<?> containerClass,
            String propertyName,
            Method getMethod,
            String stemName) {
        // verify that the Class does not also define a method with the same stem name with 'is'
        try {
            final Method isMethod = containerClass.getDeclaredMethod( "is" + stemName );
            if ( !Modifier.isStatic( isMethod.getModifiers() ) && getAnnotationTransient(getMethod) == null ) {
                // No such method should throw the caught exception.  So if we get here, there was
                // such a method.
                checkGetAndIsVariants( containerClass, propertyName, getMethod, isMethod );
            }
        }
        catch (NoSuchMethodException ignore) {
        }
    }


    public static void checkGetAndIsVariants(
            Class containerClass,
            String propertyName,
            Method getMethod,
            Method isMethod) {
        // Check the return types.  If they are the same, its ok.  If they are different
        // we are in a situation where we could not reasonably know which to use.
        if ( !isMethod.getReturnType().equals( getMethod.getReturnType() ) ) {
            throw Objects.newIllegal(
                            "In trying to locate getter for property [%s], Class [%s] defined " +
                                    "both a `get` [%s] and `is` [%s] variant",
                            propertyName,
                            containerClass.getName(),
                            getMethod.toString(),
                            isMethod.toString()
            );
        }
    }

    public static void verifyNoGetVariantExists(Class containerClass, String propertyName, Method isMethod, String stemName) {
        // verify that the Class does not also define a method with the same stem name with 'is'
        try {
            final Method getMethod = containerClass.getDeclaredMethod( "get" + stemName );
            // No such method should throw the caught exception.  So if we get here, there was
            // such a method.
            if ( !Modifier.isStatic( getMethod.getModifiers() ) &&getAnnotationTransient(getMethod) == null ) {
                checkGetAndIsVariants( containerClass, propertyName, getMethod, isMethod );
            }
        }
        catch (NoSuchMethodException ignore) {
        }
    }

    public static Method getterMethodOrNull(Class containerJavaType, String propertyName) {
        try {return findGetterMethod( containerJavaType, propertyName );}
        catch (IllegalArgumentException e) {
            return null;
        }
    }

    public static Method setterMethodOrNull(final Class containerClass, final  String propertyName, final Class propertyType) {
        Class checkClass = containerClass;
        Method setter = null;

        // check containerClass, and then its super types (if any)
        while ( setter == null && checkClass != null ) {
            if ( checkClass.equals( Object.class ) ) {
                break;
            }

            setter = setterOrNull( checkClass, propertyName, propertyType );

            // if no setter found yet, check all implemented interfaces
            if ( setter == null ) {
                setter = setterOrNull( checkClass.getInterfaces(), propertyName, propertyType );
            }
            else {
                ensureAccessibility( setter );
            }

            checkClass = checkClass.getSuperclass();
        }
        return setter; // might be null
    }

    public static Method setterMethodOrNullBySetterName(final Class containerClass, final  String setterName, final Class propertyType) {
        Class checkClass = containerClass;
        Method setter = null;

        // check containerClass, and then its super types (if any)
        while ( setter == null && checkClass != null ) {
            if ( checkClass.equals( Object.class ) ) {
                break;
            }

            setter = setterOrNullBySetterName( checkClass, setterName, propertyType );

            // if no setter found yet, check all implemented interfaces
            if ( setter == null ) {
                setter = setterOrNullBySetterName( checkClass.getInterfaces(), setterName, propertyType );
            }
            else {
                ensureAccessibility( setter );
            }

            checkClass = checkClass.getSuperclass();
        }
        return setter; // might be null
    }

    private static Method setterOrNullBySetterName(Class[] interfaces, String setterName, Class propertyType) {
        Method setter = null;
        for ( int i = 0; setter == null && i < interfaces.length; ++i ) {
            final Class anInterface = interfaces[i];
            setter = setterOrNullBySetterName( anInterface, setterName, propertyType );
            if ( setter == null ) {
                // if no setter found yet, check all implemented interfaces of interface
                setter = setterOrNullBySetterName( anInterface.getInterfaces(), setterName, propertyType );
            }
        }
        return setter;
    }

    private static Method setterOrNullBySetterName(Class theClass, String setterName, Class propertyType) {
        Method potentialSetter = null;

        for ( Method method : theClass.getDeclaredMethods() ) {
            final String methodName = method.getName();
            if ( method.getParameterCount() == 1 && methodName.equals( setterName ) ) {
                potentialSetter = method;
                if ( propertyType == null || method.getParameterTypes()[0].equals( propertyType ) ) {
                    break;
                }
            }
        }

        return potentialSetter;
    }

    public static Method findSetterMethod(final Class containerClass, final String propertyName, final Class propertyType) {
        final Method setter = setterMethodOrNull( containerClass, propertyName, propertyType );
        if ( setter == null ) {
            throw Objects.newIllegal(
                            "Could not locate setter method for property [%s#%s]",
                            containerClass.getName(),
                            propertyName
            );
        }
        return setter;
    }

    private static Method setterOrNull(Class[] interfaces, String propertyName, Class propertyType) {
        Method setter = null;
        for ( int i = 0; setter == null && i < interfaces.length; ++i ) {
            final Class anInterface = interfaces[i];
            setter = setterOrNull( anInterface, propertyName, propertyType );
            if ( setter == null ) {
                // if no setter found yet, check all implemented interfaces of interface
                setter = setterOrNull( anInterface.getInterfaces(), propertyName, propertyType );
            }
        }
        return setter;
    }

    private static Method setterOrNull(Class theClass, String propertyName, Class propertyType) {
        Method potentialSetter = null;

        for ( Method method : theClass.getDeclaredMethods() ) {
            final String methodName = method.getName();
            if ( method.getParameterCount() == 1 && methodName.startsWith( "set" ) ) {
                final String testOldMethod = methodName.substring( 3 );
                final String testStdMethod = Introspector.decapitalize( testOldMethod );
                if ( testStdMethod.equals( propertyName ) || testOldMethod.equals( propertyName ) ) {
                    potentialSetter = method;
                    if ( propertyType == null || method.getParameterTypes()[0].equals( propertyType ) ) {
                        break;
                    }
                }
            }
        }

        return potentialSetter;
    }

    /**
     * Similar to {@link #getterMethodOrNull}, except that here we are just looking for the
     * corresponding getter for a field (defined as field access) if one exists.
     *
     * We do not look at supers, although conceivably the super could declare the method
     * as an abstract - but again, that is such an edge case...
     */
    public static Method findGetterMethodForFieldAccess(Field field, String propertyName) {
        for ( Method method : field.getDeclaringClass().getDeclaredMethods() ) {
            // if the method has parameters, skip it
            if ( method.getParameterCount() != 0 ) {
                continue;
            }

            if ( Modifier.isStatic( method.getModifiers() ) ) {
                continue;
            }

            if ( ! method.getReturnType().isAssignableFrom( field.getType() ) ) {
                continue;
            }

            final String methodName = method.getName();

            // try "get"
            if ( methodName.startsWith( "get" ) ) {
                final String stemName = methodName.substring( 3 );
                final String decapitalizedStemName = Introspector.decapitalize( stemName );
                if ( stemName.equals( propertyName ) || decapitalizedStemName.equals( propertyName ) ) {
                    return method;
                }

            }

            // if not "get", then try "is"
            if ( methodName.startsWith( "is" ) ) {
                final String stemName = methodName.substring( 2 );
                String decapitalizedStemName = Introspector.decapitalize( stemName );
                if ( stemName.equals( propertyName ) || decapitalizedStemName.equals( propertyName ) ) {
                    return method;
                }
            }
        }
        if ( isRecord( field.getDeclaringClass() ) ) {
            try {
                return field.getDeclaringClass().getMethod( field.getName(), NO_PARAM_SIGNATURE );
            }
            catch (NoSuchMethodException e) {
                // Ignore
            }
        }

        return null;
    }

    public static boolean isRecord(Class<?> declaringClass) {
        return RECORD_CLASS != null && RECORD_CLASS.isAssignableFrom( declaringClass );
    }

    public static <T> Class<T> getClass(Type type) {
        if ( type == null ) {return null;}
        else if ( type instanceof Class<?> ) {return (Class<T>) type;}
        else if ( type instanceof ParameterizedType ) {
            return (Class<T>) ( (ParameterizedType) type ).getRawType();
        }
        else if ( type instanceof TypeVariable ) {
            return getClass( ( (TypeVariable) type ).getBounds()[0] );
        }
        else if ( type instanceof WildcardType ) {
            return getClass( ( (WildcardType) type ).getUpperBounds()[0] );
        }
        throw new UnsupportedOperationException( "Can't get java type class from type: " + type );
    }

    public static Class<?> getPropertyType(Member member) {
        if (member instanceof Field) {
            return ( (Field) member ).getType();
        }
        else if (member instanceof Method) {
            return ( (Method) member ).getReturnType();
        }
        else {
            throw new IllegalArgumentException("member should have been a method or field");
        }
    }


    public static <T> T getPrivateField(Object object, String fieldName, Class<T> fieldCls) {
        return (T) findPrivateField(object.getClass(), fieldName, fieldCls).get(object);
    }

    public static Class<?> getFieldType(Class objectCls, String property) {
        return findField(objectCls, property).getType();
    }

    public static <T> T invokePrivateField(Object object, String fieldName) {
        try {
            Field field = findField(object.getClass(), fieldName);
            return (T) field.get(object);
        }
        catch (IllegalAccessException exp) {
            throw new IllegalArgumentException(exp);
        }
    }


    /**
     * @param clazz the class to instantiate
     * @throws ClassException if the bean cannot be instantiated.
     */
    public static <T> T instantiateClass(Class<T> clazz) throws ClassException {
        Asserts.notNull(clazz, "Class must not be null");

        if (clazz.isInterface()) {
            throw new ClassException(clazz, "Specified class is an interface");
        }

        Constructor<T> ctor;
        try {ctor = clazz.getDeclaredConstructor();}
        catch (NoSuchMethodException ex) {
            throw new ClassException(clazz, "No default constructor found", ex);
        }

        return instantiateClass(ctor);
    }

    /**
     * @param clazz the class to instantiate
     * @throws ClassException if the bean cannot be instantiated.
     */
    public static <T> T instantiateClass(Class<T> clazz, Class[] parameterTypes, Object[] initargs) throws ClassException {
        Asserts.notNull(clazz, "Class must not be null");

        if (clazz.isInterface()) {
            throw new ClassException(clazz, "Specified class is an interface");
        }

        Constructor<T> ctor;
        try {

            ctor = clazz.getDeclaredConstructor(parameterTypes);
        }
        catch (NoSuchMethodException ex) {
            throw new ClassException(clazz, "No default constructor found", ex);
        }

        return instantiateClass(ctor, initargs);
    }

    /**
     * Convenience method to instantiate a class using the given constructor.
     * @param ctor the constructor to instantiate
     * @param args the constructor arguments to apply (use {@code null} for an unspecified parameter
     */
    public static <T> T instantiateClass(Constructor<T> ctor, Object... args) throws ClassException {
        Asserts.notNull(ctor, "Constructor must not be null");

        try {
            int parameterCount = ctor.getParameterCount();
            Asserts.isTrue(args.length <= parameterCount, "Can't specify more arguments than constructor parameters");

            if (parameterCount == 0) {
                return ctor.newInstance();
            }

            Class<?>[] parameterTypes = ctor.getParameterTypes();
            Object[] argsWithDefaultValues = new Object[args.length];
            for (int i = 0 ; i < args.length; i++) {
                if (args[i] == null) {
                    final Class<?> parameterType = parameterTypes[i];
                    final boolean primitive = parameterType.isPrimitive();
                    argsWithDefaultValues[i] = primitive ? DEFAULT_TYPE_VALUES.get(parameterType) : null;
                }
                else {
                    argsWithDefaultValues[i] = args[i];
                }
            }
            return ctor.newInstance(argsWithDefaultValues);

        }
        catch (InstantiationException ex) {
            throw new ClassException(ctor, "Is it an abstract class?", ex);
        }
        catch (IllegalAccessException ex) {
            throw new ClassException(ctor, "Is the constructor accessible?", ex);
        }
        catch (IllegalArgumentException ex) {
            throw new ClassException(ctor, "Illegal arguments for constructor", ex);
        }
        catch (InvocationTargetException ex) {
            throw new ClassException(ctor, "Constructor threw exception", ex.getTargetException());
        }
    }

    public static boolean isAvailable(String fullyQualifiedClassName) {
        forName(fullyQualifiedClassName);
        return true;
    }

    public static <T> T loadFromService(Class<T> clazz) {
        return loadFromService(clazz, "ServiceLoader failed to find implementation for class: " + clazz);
    }

    public static <T> T loadFromService(Class<T> clazz, String errorMessage) {
        try {
            ServiceLoader<T> serviceLoader = ServiceLoader.load(clazz);
            Optional<T> result = StreamSupport.stream(serviceLoader.spliterator(), false).findFirst();
            return result.orElseThrow(() -> new IllegalStateException(errorMessage));
        } catch(ServiceConfigurationError e) {
            throw new IllegalStateException(errorMessage, e);
        }
    }

    private static final Map<Class<?>, Object> DEFAULT_TYPE_VALUES = Map.of(
            boolean.class, false,
            byte.class, (byte) 0,
            short.class, (short) 0,
            int.class, 0,
            long.class, 0L,
            float.class, 0F,
            double.class, 0D,
            char.class, '\0');

    public static <S > S newObject(Class<S> objectCls, Object value) {
        Asserts.allNotNull(objectCls, value);
        try {
            Constructor<S> constructor = objectCls.getConstructor(value.getClass());
            return constructor.newInstance(value);
        }
        catch (NoSuchMethodException e) {
            String msg = "The class %s not exist constructor %s(%s)";
            throw Objects.newIllegal(msg, objectCls, objectCls.getSimpleName(), value.getClass());
        } //
        catch (InvocationTargetException | InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     */
    private interface ClassLoaderAccessor {
        <T> Class<T> loadClass(String fqcn);
        InputStream getResourceStream(String name);
    }

    private static abstract class ExceptionIgnoringAccessor implements ClassLoaderAccessor {

        public <T> Class<T> loadClass(String fqcn) {
            Class<T> clazz = null;
            ClassLoader cl = getClassLoader();
            if (cl != null) {
                try {
                    clazz = (Class<T>)cl.loadClass(fqcn);
                } catch (ClassNotFoundException ignored) {
                }
            }
            return clazz;
        }

        public InputStream getResourceStream(String name) {
            InputStream is = null;
            ClassLoader cl = getClassLoader();
            if (cl != null) {
                is = cl.getResourceAsStream(name);
            }
            return is;
        }

        protected final ClassLoader getClassLoader() {
            try {
                return doGetClassLoader();
            } catch (Throwable ignored) {
            }
            return null;
        }

        abstract ClassLoader doGetClassLoader() throws Throwable;
    }
}