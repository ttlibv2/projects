package vn.conyeu.google.xsldb.query;

import com.querydsl.core.types.Ops;
import com.querydsl.core.types.Templates;

public class XlTemplates extends Templates {

    public XlTemplates() {

        // numeric
        //add(Ops.NEGATE, "-{0}", Precedence.NEGATE);
        add(XlOps.ADD, "{0} + {1}", Precedence.ARITH_LOW);
        add(XlOps.DIV, "{0} / {1}", Precedence.ARITH_HIGH);
        add(XlOps.MOD, "{0} % {1}", Precedence.ARITH_HIGH);
        add(XlOps.MULT, "{0} * {1}", Precedence.ARITH_HIGH);
        add(XlOps.SUB, "{0} - {1}", Precedence.ARITH_LOW);

        // comparison
        add(Ops.BETWEEN, "{0} between {1} and {2}", Precedence.COMPARISON);
        add(Ops.GOE, "{0} >= {1}", Precedence.COMPARISON);
        add(Ops.GT, "{0} > {1}", Precedence.COMPARISON);
        add(Ops.LOE, "{0} <= {1}", Precedence.COMPARISON);
        add(Ops.LT, "{0} < {1}", Precedence.COMPARISON);

        add(XlOps.EQUAL, "{0} = {1}", Precedence.EQUALITY);
        add(XlOps.NOT_EQUAL, "{0} != {1}", Precedence.EQUALITY);
        add(XlOps.IS_NULL, "{0} is null", Precedence.COMPARISON);
        add(XlOps.IS_NOT_NULL, "{0} is not null", Precedence.COMPARISON);
        add(XlOps.ALIAS, "{0} {1}", 0);
        add(XlOps.LABEL, "label {0}", Precedence.LIST);

        // boolean
        add(XlOps.AND, "{0} and {1}");
        add(XlOps.NOT, "not {0}", Precedence.NOT);
        add(XlOps.OR, "{0} or {1}");


        // String
        add(XlOps.LOWER, "lower({0})", Precedence.DOT);
        add(XlOps.UPPER, "upper({0})", Precedence.DOT);

        add(XlOps.LIKE, "{0} like {1}", Precedence.COMPARISON);
        add(XlOps.LIKE_IC, "{0l} like {1l}", Precedence.COMPARISON);

        add(XlOps.MATCHES, "{0} matches '{1}'");
        add(XlOps.MATCHES_IC, "{0l} matches '{1l}'");
        add(XlOps.STARTS_WITH, "{0} starts with {1}");
        add(XlOps.STARTS_WITH_IC, "{0l} starts with {1l}");
        add(XlOps.ENDS_WITH, "{0} ends with {1}");
        add(XlOps.ENDS_WITH_IC, "{0l} ends with {1l}");
        add(XlOps.CONTAINS, "{0} contains {1}");
        add(XlOps.CONTAINS_IC, "{0l} contains {1l}");

        // Date and Time
        add(XlOps.DateTimeOps.DAY_OF_MONTH, "day (date {0})", Precedence.DOT);
        add(XlOps.DateTimeOps.DAY_OF_WEEK, "{0}.getDayOfWeek()", Precedence.DOT);

        add(Ops.DateTimeOps.DAY_OF_YEAR, "{0}.getDayOfYear()", Precedence.DOT);
        add(Ops.DateTimeOps.HOUR, "{0}.getHour()", Precedence.DOT);
        add(Ops.DateTimeOps.MINUTE, "{0}.getMinute()", Precedence.DOT);
        add(Ops.DateTimeOps.MONTH, "{0}.getMonth()", Precedence.DOT);
        add(Ops.DateTimeOps.MILLISECOND, "{0}.getMilliSecond()", Precedence.DOT);
        add(Ops.DateTimeOps.SECOND, "{0}.getSecond()", Precedence.DOT);
        add(Ops.DateTimeOps.WEEK, "{0}.getWeek()", Precedence.DOT);
        add(Ops.DateTimeOps.YEAR, "{0}.getYear()", Precedence.DOT);
    }


}