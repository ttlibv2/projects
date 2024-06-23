package vn.conyeu.common.domain;

import com.fasterxml.jackson.annotation.JsonRawValue;
import vn.conyeu.commons.beans.ObjectMap;
import vn.conyeu.commons.utils.Asserts;
import vn.conyeu.commons.utils.Classes;
import vn.conyeu.commons.utils.Objects;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;

public class ValueDb implements Serializable {
    private final Class javaType;
    private final Object data;

    public ValueDb(Object data) {
        this.data = Asserts.notNull(data);
        this.javaType = data.getClass();
    }

    public ValueDb(Class javaType, Object data) {
        this.javaType = Asserts.notNull(javaType);
        this.data = data;
    }

    @JsonRawValue
    public Object getData() {
        return data;
    }

    /**
     * Returns the javaType
     */
    public Class getJavaType() {
        return javaType;
    }

    @Override
    public String toString() {
        return javaType.getName() + "::" + data;
    }

    //--------------------------------------------
    private static final Map<String, Class> JAVA_TYPE = new HashMap<>();

    public static String registerCls(Class cls) {
        String jvName = cls.getSimpleName().toUpperCase();
        registerCls(cls.getSimpleName().toUpperCase(), cls);
        return jvName;
    }

    public static String registerCls(String name, Class cls) {
        name = name.toUpperCase();

        if(JAVA_TYPE.containsKey(name)) {
            String msg = "The name of `%s` already exists.";
            throw Objects.newIllegal(msg, cls.getName());
        }

        JAVA_TYPE.put(name, cls);
        return name;
    }

    public static Class forName(String name) {
        Class cls = JAVA_TYPE.get(name.toUpperCase());
        if(cls != null) return cls;
        else return Classes.forName(name);
    }

    // register default
    static {
        registerCls("int", Integer.class);
        registerCls("long", Long.class);
        registerCls("double", Double.class);
        registerCls("string", String.class);
        registerCls("float", Float.class);
        registerCls("json", ObjectMap.class);
        registerCls("bool", Boolean.class);
        registerCls("date", LocalDate.class);
        registerCls("datetime", LocalDateTime.class);
        registerCls("time", LocalTime.class);
    }








}