package vn.conyeu.google.sheet.builder;

import com.google.api.services.sheets.v4.model.BooleanCondition;
import com.google.api.services.sheets.v4.model.ConditionValue;
import vn.conyeu.google.core.Utils;

import java.util.ArrayList;
import java.util.List;

public class ConditionBuilder implements XmlBuilder<BooleanCondition> {
    private final BooleanCondition condition;

    public ConditionBuilder(BooleanCondition condition) {
        this.condition = Utils.getIfNull(condition, BooleanCondition::new);
    }

    @Override
    public BooleanCondition build() {
        return condition;
    }

    /**
     * The type of condition.
     * @param type type or {@code null} for none
     */
    public ConditionBuilder type(ConditionType type) {
        condition.setType(Utils.enumName(type));
        return this;
    }

    /**
     * The values of the condition.
     * @param relativeDate A relative date (based on the current date). Valid only if the type is DATE_BEFORE,
     * DATE_AFTER, DATE_ON_OR_BEFORE or DATE_ON_OR_AFTER. Relative dates are not supported in data validation. They are
     * supported only in conditional formatting and conditional filters.
     */
    public ConditionBuilder addValue(RelativeDate relativeDate) {
        getValues().add(new ConditionValue().setRelativeDate(Utils.enumName(relativeDate)));
        return this;
    }

    /**
     * The values of the condition.
     * @param userEnteredValue A value the condition is based on. The value is parsed as if the user typed into a cell.
     * Formulas are supported (and must begin with an `=` or a '+').
     */
    public ConditionBuilder addValue(String userEnteredValue) {
        getValues().add(new ConditionValue().setUserEnteredValue(userEnteredValue));
        return this;
    }

    private List<ConditionValue> getValues() {
        return Utils.setIfNull(condition::getValues, ArrayList::new, condition::setValues);
    }
}
