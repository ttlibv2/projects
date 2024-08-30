package vn.conyeu.google.drives.builder;

import com.querydsl.core.types.Ops;
import com.querydsl.core.types.Templates;
import vn.conyeu.google.query.GOps;

public class DriveTemplates extends Templates {

    public DriveTemplates() {
        add(GOps.EQUAL, "{0} = {1}", Precedence.EQUALITY);
        add(GOps.NOT_EQUAL, "{0} != {1}");

        add(GOps.GREATER_OR_EQUAL, "{0} >= {1}", Precedence.COMPARISON);
        add(GOps.GREATER_THAN, "{0} > {1}", Precedence.COMPARISON);
        add(GOps.LESS_OR_EQUAL, "{0} <= {1}", Precedence.COMPARISON);
        add(GOps.LESS_THAN, "{0} < {1}", Precedence.COMPARISON);

        // boolean
        // boolean
        add(Ops.AND, "{0} and {1}");
        add(Ops.NOT, "not {0}", Precedence.NOT);
        add(Ops.OR, "{0} or {1}");

        // string
        add(GOps.LIKE, "{0} contains '{1}'");
        add(QueryClass.DriveOps.IN, "'{1}' in {0}");
        add(QueryClass.DriveOps.HAS, "{0} has { key = '{1}' and value = '{2}' }");
        add(QueryClass.DriveOps.HAS_KEY, "{0} has { key = '{1}' }");
        add(QueryClass.DriveOps.HAS_VALUE, "{0} has { value = '{2}' }");

    }


}