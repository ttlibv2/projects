package vn.conyeu.google.sheet.builder;

import com.google.api.services.sheets.v4.model.ValueRange;

import java.util.ArrayList;
import java.util.List;

public class ValueUpdateBuilder extends ValueGetBuilder {
    private boolean includeValuesInResponse;
    private A1RangeBuilder cellAddressValue;
    private ValueRenderOption responseValueRenderOption = ValueRenderOption.UNFORMATTED_VALUE;
    private List<List<Object>> rows = new ArrayList<>();

    /**
     * Determines if the update response should include the values of the cells
     * that were updated. By default, responses do not include the updated
     * values. If the range to write was larger than the range actually written,
     * the response includes all values in the requested range (excluding
     * trailing empty rows and columns).
     * @param includeValuesInResponse the value
     */
    public ValueUpdateBuilder includeValuesInResponse(boolean includeValuesInResponse) {
        this.includeValuesInResponse = includeValuesInResponse;
        return this;
    }

    /**
     * The range the values cover, in A1 notation. For output, this range
     * indicates the entire requested range, even though the values will exclude
     * trailing rows and columns. When appending values, this field represents
     * the range to search for a table, after which values will be appended.
     * @param consumer range or {@code null} for none
     */
    public ValueUpdateBuilder range(ConsumerReturn<A1RangeBuilder> consumer) {
        if(cellAddressValue == null) cellAddressValue = new A1RangeBuilder();
        cellAddressValue = consumer.accept(cellAddressValue);
        return this;
    }

    /**
     * Set the responseValueRenderOption
     * @param responseValueRenderOption the value
     */
    public ValueUpdateBuilder responseValueRenderOption(ValueRenderOption responseValueRenderOption) {
        this.responseValueRenderOption = responseValueRenderOption;
        return this;
    }

    /**
     * The data that was read or to be written.  This is an array of arrays, the
     * outer array representing all the data and each inner array representing a
     * major dimension. Each item in the inner array corresponds with one cell.
     * <p>
     * For output, empty trailing rows and columns will not be included.
     * <p>
     * For input, supported value types are: bool, string, and double. Null
     * values will be skipped. To set a cell to an empty value, set the string
     * value to an empty string.
     * @param values values or {@code null} for none
     */
    public ValueUpdateBuilder addRow(List<Object> values) {
        rows.add(values);
        return this;
    }

    public boolean isIncludeValuesInResponse() {
        return includeValuesInResponse;
    }

    public String getResponseValueRenderOption() {
        return responseValueRenderOption.name();
    }

    public ValueRange buildValue() {
        ValueRange valueRange = new ValueRange();
        valueRange.setMajorDimension(getDimension());
        valueRange.setValues(rows);

        if(cellAddressValue != null) {
            valueRange.setRange(cellAddressValue.build());
        }

        return valueRange;
    }
}