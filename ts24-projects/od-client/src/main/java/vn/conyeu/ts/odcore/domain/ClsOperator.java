package vn.conyeu.ts.odcore.domain;

public enum ClsOperator {
    like("ilike"),
    not_like( "not ilike"),
    equal( "="),
    not_equal( "!="),
    is_set( "!="){

        public Object[] build(String key) {
            return super.build(key, false);
        }

        public Object[] build(String key, Object value) {
            return super.build(key, false);
        }
    },
    not_is_set("="){

        public Object[] build(String key) {
            return super.build(key, false);
        }

        public Object[] build(String key, Object value) {
            return super.build(key, false);
        }
    };

    final String opCode;

    ClsOperator(String code) {
        this.opCode = code;
    }

    public String op() {
        return opCode;
    }

    public Object[] build(String key, Object value) {
        return new Object[]{key, opCode, value};
    }

    public static ClsOperator forName(String name) {
        return forName(name, ClsOperator.like);
    }

    public static ClsOperator forName(String name, ClsOperator operatorDefault) {
        try{return valueOf(name.toLowerCase());}
        catch (Exception exp) { return operatorDefault; }
    }

}