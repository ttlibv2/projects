package vn.conyeu.google.sheet.builder;

import com.google.api.services.sheets.v4.model.ValueRange;
import vn.conyeu.google.core.Utils;

import java.util.ArrayList;
import java.util.List;

public class ValueRangeBuilder implements XmlBuilder<ValueRange> {
    private final ValueRange valueRange = new ValueRange();

    @Override
    public ValueRange build() {
        return valueRange;
    }

    /**
     * The major dimension of the values.
     * <p>
     * For output, if the spreadsheet data is: `A1=1,B1=2,A2=3,B2=4`, then
     * requesting `range=A1:B2,majorDimension=ROWS` will return `[[1,2],[3,4]]`,
     * whereas requesting `range=A1:B2,majorDimension=COLUMNS` will return
     * `[[1,3],[2,4]]`.
     * <p>
     * For input, with `range=A1:B2,majorDimension=ROWS` then `[[1,2],[3,4]]`
     * will set `A1=1,B1=2,A2=3,B2=4`. With `range=A1:B2,majorDimension=COLUMNS`
     * then `[[1,2],[3,4]]` will set `A1=1,B1=3,A2=2,B2=4`.
     * <p>
     * When writing, if this field is not set, it defaults to ROWS.
     * @param majorDimension majorDimension or {@code null} for none
     */
    public ValueRangeBuilder majorDimension(Dimension majorDimension) {
        valueRange.setMajorDimension(Utils.enumName(majorDimension));
        return this;
    }

    /**
     * The range the values cover, in A1 notation. For output, this range
     * indicates the entire requested range, even though the values will exclude
     * trailing rows and columns. When appending values, this field represents
     * the range to search for a table, after which values will be appended.
     * @param range range or {@code null} for none
     */
    public ValueRangeBuilder range(ConsumerReturn<A1RangeBuilder> range) {
        A1RangeBuilder builder = range.accept(new A1RangeBuilder());
        valueRange.setRange(builder.build());
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
    public ValueRangeBuilder addValue(List<Object> values) {
        List list = Utils.setIfNull(valueRange::getValues, ArrayList::new, valueRange::setValues);
        list.add(values);
        return this;
    }


    public ValueRangeBuilder addValues(List<List<Object>> rowValue) {
        rowValue.forEach(this::addValue);
        return this;
    }
}