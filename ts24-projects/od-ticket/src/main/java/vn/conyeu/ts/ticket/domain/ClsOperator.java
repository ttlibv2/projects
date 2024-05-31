package vn.conyeu.ts.ticket.domain;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Stream;

public enum ClsOperator {
    Like("like", "ilike"),
    NotLike("nlike", "not ilike"),
    Equal("eq", "="),
    NEqual("neq", "!="),
    IsSet("is", "!="){

        public Object[] build(String key) {
            return super.build(key, false);
        }

        public Object[] build(String key, Object value) {
            return super.build(key, false);
        }
    },
    IsNotSet("nis", "="){

        public Object[] build(String key) {
            return super.build(key, false);
        }

        public Object[] build(String key, Object value) {
            return super.build(key, false);
        }
    };

    final String opCode;
    final String opName;

    ClsOperator(String name, String code) {
        this.opCode = code;
        this.opName = name;
    }

    public String op() {
        return opCode;
    }

    public Object[] build(String key, Object value) {
        return new Object[]{key, opCode, value};
    }

    public static ClsOperator valueByName(String name) {
        return ALLCODE.get(name);
    }

    private static Map<String, ClsOperator> ALLCODE;
    static {
        ALLCODE = new LinkedHashMap<>();
        Stream.of(values()).forEach(cls -> ALLCODE.put(cls.opName, cls));
    }
}