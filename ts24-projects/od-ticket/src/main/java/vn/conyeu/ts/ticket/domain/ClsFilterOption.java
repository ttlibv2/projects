package vn.conyeu.ts.ticket.domain;

import vn.conyeu.commons.beans.ObjectMap;
import vn.conyeu.ts.odcore.domain.ClsHelper;

import java.util.LinkedList;
import java.util.List;

public class ClsFilterOption {
    private final List<String> operators = new LinkedList<>();
    private final List<Object> list = new LinkedList<>();

    public ClsFilterOption IsSet(String key) {
        return add(ClsOperator.IsSet, key, null);
    }

    public ClsFilterOption IsNotSet(String key) {
        return add(ClsOperator.IsNotSet, key, null);
    }

    public ClsFilterOption Like(String key, Object value) {
        return add(ClsOperator.Like, key, value);
    }

    public ClsFilterOption NotLike(String key, Object value) {
        return add(ClsOperator.NotLike, key, value);
    }

    public ClsFilterOption Equal(String key, Object value) {
        return add(ClsOperator.Equal, key, value);
    }

    public ClsFilterOption NotEqual(String key, Object value) {
        return add(ClsOperator.NEqual, key, value);
    }

    public ClsFilterOption add(ClsOperator operator, String key, Object value) {
        list.add(operator.build(key, value));
        if(list.size() > 1) operators.add("&");
        return this;
    }

    public void resetWith(ClsOperator operator, ObjectMap data) {
        ClsFilterOption option = clearAll();
        ClsOperator operatorNew = operator == null ? ClsOperator.Like : operator;
        data.forEach((field, value) -> option.addField(field, value, operatorNew));
    }

    private void addField(String field, Object value, ClsOperator op) {
        ClsOperator newOp = op;
        if(value instanceof String str && str.contains(":")) {
            String[] args = str.split(":");
            if(args.length ==2) {
                ClsOperator operator = ClsOperator.valueByName(args[0]);
                if (operator != null) {
                    newOp = operator;
                    value = args[1];

                   if(ClsHelper.isBool(args[1])) {
                       value = args[1].equals("true");
                   }
                }
            }
        }

        add(newOp, field, value);
    }

    public ClsFilterOption clearAll() {
        operators.clear();
        list.clear();
        return this;
    }

    public List<Object> build() {
        List<Object> all = new LinkedList<>();
        if(!operators.isEmpty()) all.addAll(operators);
        if (!list.isEmpty()) all.addAll(list);
        return all;
    }


}