package vn.conyeu.google.sheet.builder;

import com.google.api.services.sheets.v4.model.BooleanCondition;
import com.google.api.services.sheets.v4.model.DataValidationRule;
import vn.conyeu.google.core.Utils;

public class DataValidationRuleBuilder implements XmlBuilder<DataValidationRule> {
    private final DataValidationRule rule;
    private ConditionBuilder conditionBuilder;

    public DataValidationRuleBuilder(DataValidationRule rule) {
        this.rule = Utils.getIfNull(rule, DataValidationRule::new);
    }

    @Override
    public DataValidationRule build() {
        return rule;
    }

    /**
     * True if invalid data should be rejected.
     * @param strict strict or {@code null} for none
     */
    public DataValidationRuleBuilder strict(Boolean strict) {
        rule.setStrict(strict);
        return this;
    }

    /**
     * True if the UI should be customized based on the kind of condition. If true, "List" conditions will show a
     * dropdown.
     * @param showCustomUi showCustomUi or {@code null} for none
     */
    public DataValidationRuleBuilder showCustomUi(Boolean showCustomUi) {
        rule.setShowCustomUi(showCustomUi);
        return this;
    }

    /**
     * A message to show the user when adding data to the cell.
     * @param inputMessage inputMessage or {@code null} for none
     */
    public DataValidationRuleBuilder inputMessage(String inputMessage) {
        rule.setInputMessage(inputMessage);
        return this;
    }

    /**
     * The type of condition.
     * @param type type or {@code null} for none
     */
    public DataValidationRuleBuilder conditionType(ConditionType type) {
        getConditionBuilder().type(type);
        return this;
    }

    /**
     * The values of the condition.
     * @param relativeDate A relative date (based on the current date). Valid only if the type is DATE_BEFORE,
     * DATE_AFTER, DATE_ON_OR_BEFORE or DATE_ON_OR_AFTER. Relative dates are not supported in data validation. They are
     * supported only in conditional formatting and conditional filters.
     */
    public DataValidationRuleBuilder addConditionValue(RelativeDate relativeDate) {
        getConditionBuilder().addValue(relativeDate);
        return this;
    }

    /**
     * The values of the condition.
     * @param userEnteredValue A value the condition is based on. The value is parsed as if the user typed into a cell.
     * Formulas are supported (and must begin with an `=` or a '+').
     */
    public DataValidationRuleBuilder addConditionValue(String userEnteredValue) {
        getConditionBuilder().addValue(userEnteredValue);
        return this;
    }

    private ConditionBuilder getConditionBuilder() {
        if (conditionBuilder == null) {
            conditionBuilder = new ConditionBuilder(Utils.setIfNull(rule::getCondition,
                    BooleanCondition::new, rule::setCondition));
        }
        return conditionBuilder;
    }
}
