package vn.conyeu.google.drives;

public class QueryBuilder {

    public enum Operator {
        CONTAINS("contains"),
        EQ("="), NEQ("!="),
        LE("<"), LT("<="),
        GE(">"), GT(">="),
        IN("in"), NOT("not"),
        HAS("has");

        final String value;

        Operator(String value) {
            this.value = value;
        }
    }
}