package vn.conyeu.google.sheetdb.builder;

import com.google.api.services.sheets.v4.model.*;

public class XslUpdateResponse {
    private final XslResponseBuilder builder;
    private XslUpdateResponse(XslResponseBuilder builder) {this.builder = builder;}

    public static XslUpdateResponse from(BatchUpdateSpreadsheetResponse response) {
        return new XslUpdateResponse(XslResponseBuilder.from(response));
    }

    public Spreadsheet getSpreadsheet() {
        return builder.getSpreadsheet();
    }

    public SpreadsheetProperties getSpreadsheetProperties() {
        return builder.getSpreadsheetProperties();
    }

    /**
     * A reply from adding a banded range.
     *
     * @return value or {@code null} for none
     */
    public AddBandingResponse getAddBanding() {
        return builder.getAddBanding();
    }

    /**
     * A reply from adding a chart.
     *
     * @return value or {@code null} for none
     */
    public AddChartResponse getAddChart() {
        return builder.getAddChart();
    }

    /**
     * A reply from adding a dimension group.
     *
     * @return value or {@code null} for none
     */
    public AddDimensionGroupResponse getAddDimensionGroup() {
        return builder.getAddDimensionGroup();
    }

    /**
     * A reply from adding a filter view.
     *
     * @return value or {@code null} for none
     */
    public AddFilterViewResponse getAddFilterView() {
        return builder.getAddFilterView();
    }

    /**
     * A reply from adding a named range.
     *
     * @return value or {@code null} for none
     */
    public AddNamedRangeResponse getAddNamedRange() {
        return builder.getAddNamedRange();
    }

    /**
     * A reply from adding a protected range.
     *
     * @return value or {@code null} for none
     */
    public AddProtectedRangeResponse getAddProtectedRange() {
        return builder.getAddProtectedRange();
    }

    /**
     * A reply from adding a sheet.
     *
     * @return value or {@code null} for none
     */
    public AddSheetResponse getAddSheet() {
        return builder.getAddSheet();
    }

    /**
     * A reply from adding a slicer.
     *
     * @return value or {@code null} for none
     */
    public AddSlicerResponse getAddSlicer() {
        return builder.getAddSlicer();
    }

    /**
     * A reply from creating a developer metadata entry.
     *
     * @return value or {@code null} for none
     */
    public CreateDeveloperMetadataResponse getCreateDeveloperMetadata() {
        return builder.getCreateDeveloperMetadata();
    }

    /**
     * A reply from deleting a conditional format rule.
     *
     * @return value or {@code null} for none
     */
    public DeleteConditionalFormatRuleResponse getDeleteConditionalFormatRule() {
        return builder.getDeleteConditionalFormatRule();
    }

    /**
     * A reply from deleting a developer metadata entry.
     *
     * @return value or {@code null} for none
     */
    public DeleteDeveloperMetadataResponse getDeleteDeveloperMetadata() {
        return builder.getDeleteDeveloperMetadata();
    }

    /**
     * A reply from deleting a dimension group.
     *
     * @return value or {@code null} for none
     */
    public DeleteDimensionGroupResponse getDeleteDimensionGroup() {
        return builder.getDeleteDimensionGroup();
    }

    /**
     * A reply from removing rows containing duplicate values.
     *
     * @return value or {@code null} for none
     */
    public DeleteDuplicatesResponse getDeleteDuplicates() {
        return builder.getDeleteDuplicates();
    }

    /**
     * A reply from duplicating a filter view.
     *
     * @return value or {@code null} for none
     */
    public DuplicateFilterViewResponse getDuplicateFilterView() {
        return builder.getDuplicateFilterView();
    }

    /**
     * A reply from duplicating a sheet.
     *
     * @return value or {@code null} for none
     */
    public DuplicateSheetResponse getDuplicateSheet() {
        return builder.getDuplicateSheet();
    }

    /**
     * A reply from doing a find/replace.
     *
     * @return value or {@code null} for none
     */
    public FindReplaceResponse getFindReplace() {
        return builder.getFindReplace();
    }

    /**
     * A reply from trimming whitespace.
     *
     * @return value or {@code null} for none
     */
    public TrimWhitespaceResponse getTrimWhitespace() {
        return builder.getTrimWhitespace();
    }

    /**
     * A reply from updating a conditional format rule.
     *
     * @return value or {@code null} for none
     */
    public UpdateConditionalFormatRuleResponse getUpdateConditionalFormatRule() {
        return builder.getUpdateConditionalFormatRule();
    }

    /**
     * A reply from updating a developer metadata entry.
     *
     * @return value or {@code null} for none
     */
    public UpdateDeveloperMetadataResponse getUpdateDeveloperMetadata() {
        return builder.getUpdateDeveloperMetadata();
    }

    /**
     * A reply from updating an embedded object's position.
     *
     * @return value or {@code null} for none
     */
    public UpdateEmbeddedObjectPositionResponse getUpdateEmbeddedObjectPosition() {
        return builder.getUpdateEmbeddedObjectPosition();
    }
}