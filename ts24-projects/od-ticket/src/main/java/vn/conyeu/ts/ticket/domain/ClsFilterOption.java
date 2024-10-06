package vn.conyeu.ts.ticket.domain;

import vn.conyeu.common.exception.BaseException;
import vn.conyeu.commons.beans.ObjectMap;
import vn.conyeu.ts.odcore.domain.ClsHelper;
import vn.conyeu.ts.odcore.domain.ClsOperator;
import vn.conyeu.ts.odcore.domain.ClsSearch;

import java.util.LinkedList;
import java.util.List;

public class ClsFilterOption {
    private final List<String> operators = new LinkedList<>();
    private final List<Object> list = new LinkedList<>();

    public ClsFilterOption() {
    }

    public ClsFilterOption(ClsSearch cls) {
        resetWith(cls.getOperator(), cls);
    }

    public void resetWith(ClsOperator operator, ClsSearch cls) {
        ClsFilterOption option = clearAll();

        if(cls.getFilter() != null) {
            cls.getFilter().forEach(this::add);
        }

        if(cls.getData() != null) {
            ClsOperator operatorNew = operator == null ? ClsOperator.like : operator;

            cls.getData().forEach((field, value) ->
                    option.addField(field, value, operatorNew));
        }
    }

    public ClsFilterOption IsSet(String key) {
        return add(ClsOperator.is_set, key, null);
    }

    public ClsFilterOption IsNotSet(String key) {
        return add(ClsOperator.not_is_set, key, null);
    }

    public ClsFilterOption Like(String key, Object value) {
        return add(ClsOperator.like, key, value);
    }

    public ClsFilterOption NotLike(String key, Object value) {
        return add(ClsOperator.not_like, key, value);
    }

    public ClsFilterOption Equal(String key, Object value) {
        return add(ClsOperator.equal, key, value);
    }

    public ClsFilterOption NotEqual(String key, Object value) {
        return add(ClsOperator.not_equal, key, value);
    }

    public ClsFilterOption add(List<Object> objects) {
        if(objects == null || objects.size() != 3) {
            throw BaseException.e400("filter_invalid")
                    .detail("objects", objects == null ? null : objects.size())
                    .message("The filter data invalid (is null or size <> 3)");
        }

        list.add(objects);
        if(list.size() > 1) operators.add("&");
        return this;
    }

    public ClsFilterOption add(ClsOperator operator, String key, Object value) {
        list.add(operator.build(key, value));
        if(list.size() > 1) operators.add("&");
        return this;
    }

    public void resetWith(ClsOperator operator, ObjectMap data) {
        ClsFilterOption option = clearAll();
        ClsOperator operatorNew = operator == null ? ClsOperator.like : operator;
        data.forEach((field, value) -> option.addField(field, value, operatorNew));
    }

    private void addField(String field, Object value, ClsOperator op) {
        ClsOperator newOp = op;
        if(value instanceof String str && str.contains(":")) {
            String[] args = str.split(":");
            if(args.length ==2) {
                ClsOperator operator = ClsOperator.forName(args[0]);
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