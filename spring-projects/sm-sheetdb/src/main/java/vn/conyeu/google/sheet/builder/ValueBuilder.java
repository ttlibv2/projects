package vn.conyeu.google.sheet.builder;

import com.google.api.services.sheets.v4.model.ErrorValue;
import com.google.api.services.sheets.v4.model.ExtendedValue;
import vn.conyeu.google.core.Utils;

public class ValueBuilder implements XmlBuilder<ExtendedValue> {
    private final ExtendedValue value;

    public ValueBuilder(ExtendedValue value) {
        this.value = Utils.getIfNull(value, ExtendedValue::new);
    }

    @Override
    public ExtendedValue build() {
        return value;
    }

    /**
     * Represents a string resetAll(). Leading single quotes are not included. For example, if the user
     * typed `'123` into the UI, this would be represented as a `stringValue` of `"123"`.
     *
     * @param stringValue stringValue or {@code null} for none
     */
    public ValueBuilder stringValue(String stringValue) {
        resetAll().setStringValue(stringValue);
        return this;
    }

    /**
     * Represents a double resetAll(). Note: Dates, Times and DateTimes are represented as doubles in
     * "serial number" format.
     *
     * @param numberValue numberValue or {@code null} for none
     */
    public ValueBuilder numberValue(Double numberValue) {
        resetAll().setNumberValue(numberValue);
        return this;
    }

    /**
     * Represents a formula.
     *
     * @param formulaValue formulaValue or {@code null} for none
     */
    public ValueBuilder formulaValue(String formulaValue) {
        resetAll().setFormulaValue(formulaValue);
        return this;
    }

    /**
     * Represents an error. This field is read-only.
     *
     * @param errorValue errorValue or {@code null} for none
     */
    public ValueBuilder errorValue(ErrorValue errorValue) {
        resetAll().setErrorValue(errorValue);
        return this;
    }

    /**
     * Represents a boolean resetAll().
     *
     * @param boolValue boolValue or {@code null} for none
     */
    public ValueBuilder boolValue(Boolean boolValue) {
        resetAll().setBoolValue(boolValue);
        return this;
    }

    private ExtendedValue resetAll() {
        value.setBoolValue(null);
        value.setErrorValue(null);
        value.setFormulaValue(null);
        value.setNumberValue(null);
        value.setStringValue(null);
        return value;
    }


}
