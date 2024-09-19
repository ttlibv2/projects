package vn.conyeu.google.sheet.builder;

import lombok.Getter;

@Getter
public class AppendOption {
    private Boolean includeValuesInResponse;
    private ValueInputOption valueInputOption = ValueInputOption.USER_ENTERED;
    private InsertDataOption insertDataOption = InsertDataOption.INSERT_ROWS;
    private ValueRenderOption responseValueRenderOption = ValueRenderOption.FORMATTED_VALUE;
    private DateTimeRenderOption responseDateTimeRenderOption = DateTimeRenderOption.FORMATTED_STRING;

    /**
     * Set the includeValuesInResponse
     *
     * @param includeValuesInResponse the value
     */
    public AppendOption includeValuesInResponse(Boolean includeValuesInResponse) {
        this.includeValuesInResponse = includeValuesInResponse;
        return this;
    }

    /**
     * Set the valueInputOption
     *
     * @param valueInputOption the value
     */
    public AppendOption valueInputOption(ValueInputOption valueInputOption) {
        this.valueInputOption = valueInputOption;
        return this;
    }

    /**
     * Set the insertDataOption
     *
     * @param insertDataOption the value
     */
    public AppendOption insertDataOption(InsertDataOption insertDataOption) {
        this.insertDataOption = insertDataOption;
        return this;
    }

    /**
     * Set the responseValueRenderOption
     *
     * @param responseValueRenderOption the value
     */
    public AppendOption responseValueRenderOption(ValueRenderOption responseValueRenderOption) {
        this.responseValueRenderOption = responseValueRenderOption;
        return this;
    }

    /**
     * Set the responseDateTimeRenderOption
     *
     * @param responseDateTimeRenderOption the value
     */
    public AppendOption responseDateTimeRenderOption(DateTimeRenderOption responseDateTimeRenderOption) {
        this.responseDateTimeRenderOption = responseDateTimeRenderOption;
        return this;
    }

    public ValueAppendBuilder apply(ValueAppendBuilder v) {
         return v.valueInputOption(getValueInputOption())
                .responseValueRenderOption(getResponseValueRenderOption())
                .responseDateTimeRenderOption(getResponseDateTimeRenderOption())
                .includeValuesInResponse(getIncludeValuesInResponse());
    }
}