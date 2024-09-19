package vn.conyeu.google.sheet.builder;

import com.google.api.services.sheets.v4.model.ValueRange;
import vn.conyeu.commons.utils.Objects;

public class ValueAppendBuilder {
    private String spreadsheetId;
    private boolean includeValuesInResponse;
    private ValueRange valueRange;
    private String a1Range;
    private String fields;
    private InsertDataOption insertDataOption = InsertDataOption.INSERT_ROWS;
    private ValueInputOption valueInputOption = ValueInputOption.USER_ENTERED;
    private ValueRenderOption responseValueRenderOption = ValueRenderOption.FORMATTED_VALUE;
    private DateTimeRenderOption responseDateTimeRenderOption = DateTimeRenderOption.FORMATTED_STRING;

    /**
     * The request body contains an instance of ValueRange.
     * @param b the value
     */
    public ValueAppendBuilder valueRange(ConsumerReturn<ValueRangeBuilder> b) {
        this.valueRange = b.accept(new ValueRangeBuilder()).build();
        return this;
    }

    /**
     * The ID of the spreadsheet to update.
     * @param spreadsheetId the value
     */
    public ValueAppendBuilder spreadsheetId(String spreadsheetId) {
        this.spreadsheetId = spreadsheetId;
        return this;
    }

    /**
     * The A1 notation of a range to search for a logical table of data. Values
     * are appended after the last row of the table.
     */
    public ValueAppendBuilder range(String sheetName, Integer firstRow) {
        range(r -> r.name(sheetName).firstRow(firstRow).firstCol(0));
        return this;
    }

    /**
     * The A1 notation of a range to search for a logical table of data. Values
     * are appended after the last row of the table.
     */
    public ValueAppendBuilder range(ConsumerReturn<A1RangeBuilder> consumer) {
        a1Range = consumer.accept(new A1RangeBuilder()).build();
        return this;
    }

    /**
     * The A1 notation of a range to search for a logical table of data. Values
     * are appended after the last row of the table.
     */
    public ValueAppendBuilder range(A1RangeBuilder builder) {
        a1Range = builder.build();
        return this;
    }

    /**
     * How the input data should be interpreted.
     * @param valueInputOption the value
     */
    public ValueAppendBuilder valueInputOption(ValueInputOption valueInputOption) {
        this.valueInputOption = Objects.firstIfNull(valueInputOption, ValueInputOption.USER_ENTERED);
        return this;
    }

    /**
     * How the input data should be inserted.
     * @param insertDataOption the value
     */
    public ValueAppendBuilder insertDataOption(InsertDataOption insertDataOption) {
        this.insertDataOption = Objects.firstIfNull(insertDataOption, InsertDataOption.INSERT_ROWS);
        return this;
    }

    /**
     * Determines if the update response should include the values of the cells that were appended.
     * By default, responses do not include the updated values.
     * @param includeValuesInResponse the value
     */
    public ValueAppendBuilder includeValuesInResponse(boolean includeValuesInResponse) {
        this.includeValuesInResponse = includeValuesInResponse;
        return this;
    }

    /**
     * Determines how values in the response should be rendered. The default render option is FORMATTED_VALUE.
     * @param responseValueRenderOption the value
     */
    public ValueAppendBuilder responseValueRenderOption(ValueRenderOption responseValueRenderOption) {
        this.responseValueRenderOption = Objects.firstIfNull(responseValueRenderOption, ValueRenderOption.FORMATTED_VALUE);
        return this;
    }

    /**
     * Determines how dates, times, and durations in the response should be rendered. This is ignored if responseValueRenderOption is FORMATTED_VALUE. The default dateTime render option is FORMATTED_VALUE.
     * @param responseDateTimeRenderOption the value
     */
    public ValueAppendBuilder responseDateTimeRenderOption(DateTimeRenderOption responseDateTimeRenderOption) {
        this.responseDateTimeRenderOption = Objects.firstIfNull(responseDateTimeRenderOption, DateTimeRenderOption.FORMATTED_STRING);
        return this;
    }

    /**
     * Set the fields
     * @param fields the value
     */
    public ValueAppendBuilder fields(String fields) {
        this.fields = fields;
        return this;
    }

    public String getFields() {
        return fields;
    }

    public String getSpreadsheetId() {
        return spreadsheetId;
    }

    public ValueInputOption getValueInputOption() {
        return valueInputOption;
    }

    public InsertDataOption getInsertDataOption() {
        return insertDataOption;
    }

    public boolean isIncludeValuesInResponse() {
        return includeValuesInResponse;
    }

    public ValueRange getValueRange() {
        return valueRange;
    }

    public ValueRenderOption getResponseValueRenderOption() {
        return responseValueRenderOption;
    }

    public DateTimeRenderOption getResponseDateTimeRenderOption() {
        return responseDateTimeRenderOption;
    }

    public String getA1Range() {
        return a1Range;
    }
}