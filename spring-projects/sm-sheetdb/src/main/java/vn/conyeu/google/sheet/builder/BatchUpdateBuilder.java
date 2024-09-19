package vn.conyeu.google.sheet.builder;

import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.model.*;
import vn.conyeu.google.core.Utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class BatchUpdateBuilder implements XmlBuilder<BatchUpdateSpreadsheetRequest> {
    private final BatchUpdateSpreadsheetRequest bu = new BatchUpdateSpreadsheetRequest();

    public static BatchUpdateBuilder create() {
        return new BatchUpdateBuilder();
    }

    @Override
    public BatchUpdateSpreadsheetRequest build() {
        return bu;
    }

    /**
     * Determines if the update response should include the spreadsheet resource.
     *
     * @param includeSpreadsheetInResponse includeSpreadsheetInResponse or {@code null} for none
     */
    public BatchUpdateBuilder includeResponse(Boolean includeSpreadsheetInResponse) {
        bu.setIncludeSpreadsheetInResponse(includeSpreadsheetInResponse);
        return this;
    }

    /**
     * True if grid data should be returned. Meaningful only if include_spreadsheet_in_response is
     * 'true'. This parameter is ignored if a field mask was set in the request.
     *
     * @param responseIncludeGridData responseIncludeGridData or {@code null} for none
     */
    public BatchUpdateBuilder includeGrid(Boolean responseIncludeGridData) {
        bu.setResponseIncludeGridData(responseIncludeGridData);
        return this;
    }

    /**
     * Limits the ranges included in the response spreadsheet. Meaningful only if
     * include_spreadsheet_in_response is 'true'.
     *
     * @param responseRanges responseRanges or {@code null} for none
     */
    public BatchUpdateBuilder responseRanges(List<String> responseRanges) {
        bu.setResponseRanges(responseRanges);
        return this;
    }

    /**
     * Adds a new banded range
     * The value may be {@code null}.
     */
    public BatchUpdateBuilder addBanding(ConsumerReturn<AddBandingRequest> consumer) {
        AddBandingRequest object = consumer.accept(new AddBandingRequest());
        initRequest().setAddBanding(object);
        return this;
    }

    /**
     * Adds a chart.
     * The value may be {@code null}.
     */
    public BatchUpdateBuilder addChart(ConsumerReturn<AddChartRequest> consumer) {
        AddChartRequest object = consumer.accept(new AddChartRequest());
        initRequest().setAddChart(object);
        return this;
    }

    /**
     * Adds a new conditional format rule.
     * The value may be {@code null}.
     */
    public BatchUpdateBuilder addConditionalFormatRule(ConsumerReturn<AddConditionalFormatRuleRequest> consumer) {
        AddConditionalFormatRuleRequest object = consumer.accept(new AddConditionalFormatRuleRequest());
        initRequest().setAddConditionalFormatRule(object);
        return this;
    }

    /**
     * Creates a group over the specified range.
     * The value may be {@code null}.
     */
    public BatchUpdateBuilder addDimensionGroup(ConsumerReturn<AddDimensionGroupRequest> consumer) {
        AddDimensionGroupRequest object = consumer.accept(new AddDimensionGroupRequest());
        initRequest().setAddDimensionGroup(object);
        return this;
    }

    /**
     * Adds a filter view.
     * The value may be {@code null}.
     */
    public BatchUpdateBuilder addFilterView(ConsumerReturn<AddFilterViewRequest> consumer) {
        AddFilterViewRequest object = consumer.accept(new AddFilterViewRequest());
        initRequest().setAddFilterView(object);
        return this;
    }

    /**
     * Adds a named range.
     * The value may be {@code null}.
     */
    public BatchUpdateBuilder addNamedRange(ConsumerReturn<AddNamedRangeRequest> consumer) {
        AddNamedRangeRequest object = consumer.accept(new AddNamedRangeRequest());
        initRequest().setAddNamedRange(object);
        return this;
    }

    /**
     * Adds a protected range.
     * The value may be {@code null}.
     */
    public BatchUpdateBuilder addProtectedRange(ConsumerReturn<AddProtectedRangeRequest> consumer) {
        AddProtectedRangeRequest object = consumer.accept(new AddProtectedRangeRequest());
        initRequest().setAddProtectedRange(object);
        return this;
    }

    /**
     * Adds a sheet.
     * The value may be {@code null}.
     */
    public BatchUpdateBuilder addSheet(ConsumerReturn<SheetPropertiesBuilder> consumer) {
        SheetPropertiesBuilder object = consumer.accept(new SheetPropertiesBuilder(null));
        AddSheetRequest addSheet = new AddSheetRequest().setProperties(object.build());
        initRequest().setAddSheet(addSheet);
        return this;
    }

    /**
     * Adds a slicer.
     * The value may be {@code null}.
     */
    public BatchUpdateBuilder addSlicer(ConsumerReturn<AddSlicerRequest> consumer) {
        AddSlicerRequest object = consumer.accept(new AddSlicerRequest());
        initRequest().setAddSlicer(object);
        return this;
    }

    /**
     * Appends cells after the last row with data in a sheet.
     * The value may be {@code null}.
     */
    public BatchUpdateBuilder appendCells(ConsumerReturn<AppendCellsRequest> consumer) {
        AppendCellsRequest object = consumer.accept(new AppendCellsRequest());
        initRequest().setAppendCells(object);
        return this;
    }

    /**
     * Appends dimensions to the end of a sheet.
     * The value may be {@code null}.
     */
    public BatchUpdateBuilder appendDimension(Dimension dimension, Integer sheetId, Integer length) {
        initRequest().setAppendDimension(new AppendDimensionRequest()
                .setDimension(Utils.enumName(dimension))
                .setSheetId(sheetId).setLength(length));
        return this;
    }

    /**
     * Automatically fills in more data based on existing data.
     * The value may be {@code null}.
     */
    public BatchUpdateBuilder autoFill(ConsumerReturn<AutoFillRequest> consumer) {
        AutoFillRequest object = consumer.accept(new AutoFillRequest());
        initRequest().setAutoFill(object);
        return this;
    }

    /**
     * Automatically resizes one or more dimensions based on the contents of the cells in that
     * dimension.
     * The value may be {@code null}.
     */
    public BatchUpdateBuilder autoResizeDimensions(ConsumerReturn<AutoResizeDimensionsRequest> consumer) {
        AutoResizeDimensionsRequest object = consumer.accept(new AutoResizeDimensionsRequest());
        initRequest().setAutoResizeDimensions(object);
        return this;
    }

    /**
     * Clears the basic filter on a sheet.
     * The value may be {@code null}.
     */
    public BatchUpdateBuilder clearBasicFilter(ConsumerReturn<ClearBasicFilterRequest> consumer) {
        ClearBasicFilterRequest object = consumer.accept(new ClearBasicFilterRequest());
        initRequest().setClearBasicFilter(object);
        return this;
    }

    /**
     * Copies data from one area and pastes it to another.
     * The value may be {@code null}.
     */
    public BatchUpdateBuilder copyPaste(ConsumerReturn<CopyPasteRequest> consumer) {
        CopyPasteRequest object = consumer.accept(new CopyPasteRequest());
        initRequest().setCopyPaste(object);
        return this;
    }

    /**
     * Creates new developer metadata
     * The value may be {@code null}.
     */
    public BatchUpdateBuilder createDeveloperMetadata(ConsumerReturn<CreateDeveloperMetadataRequest> consumer) {
        CreateDeveloperMetadataRequest object = consumer.accept(new CreateDeveloperMetadataRequest());
        initRequest().setCreateDeveloperMetadata(object);
        return this;
    }

    /**
     * Cuts data from one area and pastes it to another.
     * The value may be {@code null}.
     */
    public BatchUpdateBuilder cutPaste(ConsumerReturn<CutPasteRequest> consumer) {
        CutPasteRequest object = consumer.accept(new CutPasteRequest());
        initRequest().setCutPaste(object);
        return this;
    }

    /**
     * Removes a banded range
     * The value may be {@code null}.
     */
    public BatchUpdateBuilder deleteBanding(ConsumerReturn<DeleteBandingRequest> consumer) {
        DeleteBandingRequest object = consumer.accept(new DeleteBandingRequest());
        initRequest().setDeleteBanding(object);
        return this;
    }

    /**
     * Deletes an existing conditional format rule.
     * The value may be {@code null}.
     */
    public BatchUpdateBuilder deleteConditionalFormatRule(ConsumerReturn<DeleteConditionalFormatRuleRequest> consumer) {
        DeleteConditionalFormatRuleRequest object = consumer.accept(new DeleteConditionalFormatRuleRequest());
        initRequest().setDeleteConditionalFormatRule(object);
        return this;
    }

    /**
     * Deletes developer metadata
     * The value may be {@code null}.
     */
    public BatchUpdateBuilder deleteDeveloperMetadata(ConsumerReturn<DeleteDeveloperMetadataRequest> consumer) {
        DeleteDeveloperMetadataRequest object = consumer.accept(new DeleteDeveloperMetadataRequest());
        initRequest().setDeleteDeveloperMetadata(object);
        return this;
    }

    /**
     * Deletes rows or columns in a sheet.
     * The value may be {@code null}.
     */
    public BatchUpdateBuilder deleteDimension(ConsumerReturn<DimensionRange> consumer) {
        DimensionRange dimensionRange = consumer.accept(new DimensionRange());
        DeleteDimensionRequest object = new DeleteDimensionRequest().setRange(dimensionRange);
        initRequest().setDeleteDimension(object);
        return this;
    }

    /**
     * Deletes a group over the specified range.
     * The value may be {@code null}.
     */
    public BatchUpdateBuilder deleteDimensionGroup(ConsumerReturn<DeleteDimensionGroupRequest> consumer) {
        DeleteDimensionGroupRequest object = consumer.accept(new DeleteDimensionGroupRequest());
        initRequest().setDeleteDimensionGroup(object);
        return this;
    }

    /**
     * Removes rows containing duplicate values in specified columns of a cell range.
     * The value may be {@code null}.
     */
    public BatchUpdateBuilder deleteDuplicates(ConsumerReturn<DeleteDuplicatesRequest> consumer) {
        DeleteDuplicatesRequest object = consumer.accept(new DeleteDuplicatesRequest());
        initRequest().setDeleteDuplicates(object);
        return this;
    }

    /**
     * Deletes an embedded object (e.g, chart, image) in a sheet.
     * The value may be {@code null}.
     */
    public BatchUpdateBuilder deleteEmbeddedObject(ConsumerReturn<DeleteEmbeddedObjectRequest> consumer) {
        DeleteEmbeddedObjectRequest object = consumer.accept(new DeleteEmbeddedObjectRequest());
        initRequest().setDeleteEmbeddedObject(object);
        return this;
    }

    /**
     * Deletes a filter view from a sheet.
     * The value may be {@code null}.
     */
    public BatchUpdateBuilder deleteFilterView(ConsumerReturn<DeleteFilterViewRequest> consumer) {
        DeleteFilterViewRequest object = consumer.accept(new DeleteFilterViewRequest());
        initRequest().setDeleteFilterView(object);
        return this;
    }

    /**
     * Deletes a named range.
     * The value may be {@code null}.
     */
    public BatchUpdateBuilder deleteNamedRange(ConsumerReturn<DeleteNamedRangeRequest> consumer) {
        DeleteNamedRangeRequest object = consumer.accept(new DeleteNamedRangeRequest());
        initRequest().setDeleteNamedRange(object);
        return this;
    }

    /**
     * Deletes a protected range.
     * The value may be {@code null}.
     */
    public BatchUpdateBuilder deleteProtectedRange(ConsumerReturn<DeleteProtectedRangeRequest> consumer) {
        DeleteProtectedRangeRequest object = consumer.accept(new DeleteProtectedRangeRequest());
        initRequest().setDeleteProtectedRange(object);
        return this;
    }

    /**
     * Deletes a range of cells from a sheet, shifting the remaining cells.
     * The value may be {@code null}.
     */
    public BatchUpdateBuilder deleteRange(ConsumerReturn<DeleteRangeRequest> consumer) {
        DeleteRangeRequest object = consumer.accept(new DeleteRangeRequest());
        initRequest().setDeleteRange(object);
        return this;
    }

    /**
     * Deletes a sheet.
     * The value may be {@code null}.
     */
    public BatchUpdateBuilder deleteSheet(ConsumerReturn<DeleteSheetRequest> consumer) {
        DeleteSheetRequest object = consumer.accept(new DeleteSheetRequest());
        initRequest().setDeleteSheet(object);
        return this;
    }

    /**
     * Duplicates a filter view.
     * The value may be {@code null}.
     */
    public BatchUpdateBuilder duplicateFilterView(ConsumerReturn<DuplicateFilterViewRequest> consumer) {
        DuplicateFilterViewRequest object = consumer.accept(new DuplicateFilterViewRequest());
        initRequest().setDuplicateFilterView(object);
        return this;
    }

    /**
     * Duplicates a sheet.
     * The value may be {@code null}.
     */
    public BatchUpdateBuilder duplicateSheet(ConsumerReturn<DuplicateSheetRequest> consumer) {
        DuplicateSheetRequest object = consumer.accept(new DuplicateSheetRequest());
        initRequest().setDuplicateSheet(object);
        return this;
    }

    /**
     * Finds and replaces occurrences of some text with other text.
     * The value may be {@code null}.
     */
    public BatchUpdateBuilder findReplace(ConsumerReturn<FindReplaceRequest> consumer) {
        FindReplaceRequest object = consumer.accept(new FindReplaceRequest());
        initRequest().setFindReplace(object);
        return this;
    }

    /**
     * Inserts new rows or columns in a sheet.
     * The value may be {@code null}.
     */
    public BatchUpdateBuilder insertDimension(ConsumerReturn<InsertDimensionBuilder> consumer) {
        InsertDimensionBuilder builder= consumer.accept(new InsertDimensionBuilder());
        initRequest().setInsertDimension(builder.build());
        return this;
    }

    /**
     * Inserts new rows or columns in a sheet.
     * The value may be {@code null}.
     */
    public BatchUpdateBuilder insertDimension(InsertDimensionBuilder builder) {
        return insertDimension(b -> builder);
    }

    /**
     * Inserts new cells in a sheet, shifting the existing cells.
     * The value may be {@code null}.
     */
    public BatchUpdateBuilder insertRange(ConsumerReturn<InsertRangeRequest> consumer) {
        InsertRangeRequest object = consumer.accept(new InsertRangeRequest());
        initRequest().setInsertRange(object);
        return this;
    }

    /**
     * Merges cells together.
     * The value may be {@code null}.
     */
    public BatchUpdateBuilder mergeCells(ConsumerReturn<MergeCellsRequest> consumer) {
        MergeCellsRequest object = consumer.accept(new MergeCellsRequest());
        initRequest().setMergeCells(object);
        return this;
    }

    /**
     * Moves rows or columns to another location in a sheet.
     * The value may be {@code null}.
     */
    public BatchUpdateBuilder moveDimension(ConsumerReturn<MoveDimensionBuilder> consumer) {
        MoveDimensionBuilder builder = consumer.accept(new MoveDimensionBuilder());
        initRequest().setMoveDimension(builder.build());
        return this;
    }

    /**
     * Pastes data (HTML or delimited) into a sheet.
     * The value may be {@code null}.
     */
    public BatchUpdateBuilder pasteData(ConsumerReturn<PasteDataRequest> consumer) {
        PasteDataRequest object = consumer.accept(new PasteDataRequest());
        initRequest().setPasteData(object);
        return this;
    }

    /**
     * Randomizes the order of the rows in a range.
     * The value may be {@code null}.
     */
    public BatchUpdateBuilder randomizeRange(ConsumerReturn<RandomizeRangeRequest> consumer) {
        RandomizeRangeRequest object = consumer.accept(new RandomizeRangeRequest());
        initRequest().setRandomizeRange(object);
        return this;
    }

    /**
     * Repeats a single cell across a range.
     * The value may be {@code null}.
     */
    public BatchUpdateBuilder repeatCell(ConsumerReturn<RepeatCellBuilder> consumer) {
        RepeatCellBuilder builder = consumer.accept(new RepeatCellBuilder());
        initRequest().setRepeatCell(builder.build());
        return this;
    }

    /**
     * Sets the basic filter on a sheet.
     * The value may be {@code null}.
     */
    public BatchUpdateBuilder setBasicFilter(ConsumerReturn<SetBasicFilterRequest> consumer) {
        SetBasicFilterRequest object = consumer.accept(new SetBasicFilterRequest());
        initRequest().setSetBasicFilter(object);
        return this;
    }

    /**
     * Sets data validation for one or more cells.
     * The value may be {@code null}.
     */
    public BatchUpdateBuilder setDataValidation(ConsumerReturn<SetDataValidationRequest> consumer) {
        SetDataValidationRequest object = consumer.accept(new SetDataValidationRequest());
        initRequest().setSetDataValidation(object);
        return this;
    }

    /**
     * Sorts data in a range.
     * The value may be {@code null}.
     */
    public BatchUpdateBuilder sortRange(ConsumerReturn<SortRangeRequest> consumer) {
        SortRangeRequest object = consumer.accept(new SortRangeRequest());
        initRequest().setSortRange(object);
        return this;
    }

    /**
     * Converts a column of text into many columns of text.
     * The value may be {@code null}.
     */
    public BatchUpdateBuilder textToColumns(ConsumerReturn<TextToColumnsRequest> consumer) {
        TextToColumnsRequest object = consumer.accept(new TextToColumnsRequest());
        initRequest().setTextToColumns(object);
        return this;
    }

    /**
     * Trims cells of whitespace (such as spaces, tabs, or new lines).
     * The value may be {@code null}.
     */
    public BatchUpdateBuilder trimWhitespace(ConsumerReturn<TrimWhitespaceRequest> consumer) {
        TrimWhitespaceRequest object = consumer.accept(new TrimWhitespaceRequest());
        initRequest().setTrimWhitespace(object);
        return this;
    }

    /**
     * Unmerges merged cells.
     * The value may be {@code null}.
     */
    public BatchUpdateBuilder unmergeCells(ConsumerReturn<UnmergeCellsRequest> consumer) {
        UnmergeCellsRequest object = consumer.accept(new UnmergeCellsRequest());
        initRequest().setUnmergeCells(object);
        return this;
    }

    /**
     * Updates a banded range
     * The value may be {@code null}.
     */
    public BatchUpdateBuilder updateBanding(ConsumerReturn<UpdateBandingRequest> consumer) {
        UpdateBandingRequest object = consumer.accept(new UpdateBandingRequest());
        initRequest().setUpdateBanding(object);
        return this;
    }

    /**
     * Updates the borders in a range of cells.
     * The value may be {@code null}.
     */
    public BatchUpdateBuilder updateBorders(ConsumerReturn<UpdateBordersRequest> consumer) {
        UpdateBordersRequest object = consumer.accept(new UpdateBordersRequest());
        initRequest().setUpdateBorders(object);
        return this;
    }

    /**
     * Updates many cells at once.
     * The value may be {@code null}.
     */
    public BatchUpdateBuilder updateCells(ConsumerReturn<UpdateCellsBuilder> consumer) {
        UpdateCellsBuilder builder = consumer.accept(new UpdateCellsBuilder());
        initRequest().setUpdateCells(builder.build());
        return this;
    }

    /**
     * Updates a chart's specifications.
     * The value may be {@code null}.
     */
    public BatchUpdateBuilder updateChartSpec(ConsumerReturn<UpdateChartSpecRequest> consumer) {
        UpdateChartSpecRequest object = consumer.accept(new UpdateChartSpecRequest());
        initRequest().setUpdateChartSpec(object);
        return this;
    }

    /**
     * Updates an existing conditional format rule.
     * The value may be {@code null}.
     */
    public BatchUpdateBuilder updateConditionalFormatRule(ConsumerReturn<UpdateConditionalFormatRuleRequest> consumer) {
        UpdateConditionalFormatRuleRequest object = consumer.accept(new UpdateConditionalFormatRuleRequest());
        initRequest().setUpdateConditionalFormatRule(object);
        return this;
    }

    /**
     * Updates an existing developer metadata entry
     * The value may be {@code null}.
     */
    public BatchUpdateBuilder updateDeveloperMetadata(ConsumerReturn<UpdateDeveloperMetadataRequest> consumer) {
        UpdateDeveloperMetadataRequest object = consumer.accept(new UpdateDeveloperMetadataRequest());
        initRequest().setUpdateDeveloperMetadata(object);
        return this;
    }

    /**
     * Updates the state of the specified group.
     * The value may be {@code null}.
     */
    public BatchUpdateBuilder updateDimensionGroup(ConsumerReturn<UpdateDimensionGroupRequest> consumer) {
        UpdateDimensionGroupRequest object = consumer.accept(new UpdateDimensionGroupRequest());
        initRequest().setUpdateDimensionGroup(object);
        return this;
    }

    /**
     * Updates dimensions' properties.
     * The value may be {@code null}.
     */
    public BatchUpdateBuilder updateDimensionProperties(ConsumerReturn<UpdateDimensionBuilder> consumerReturn) {
        UpdateDimensionBuilder builder = consumerReturn.accept(new UpdateDimensionBuilder());
        initRequest().setUpdateDimensionProperties(builder.build());
        return this;
    }

    /**
     * Updates an embedded object's (e.g. chart, image) position.
     * The value may be {@code null}.
     */
    public BatchUpdateBuilder updateEmbeddedObjectPosition(ConsumerReturn<UpdateEmbeddedObjectPositionRequest> consumer) {
        UpdateEmbeddedObjectPositionRequest object = consumer.accept(new UpdateEmbeddedObjectPositionRequest());
        initRequest().setUpdateEmbeddedObjectPosition(object);
        return this;
    }

    /**
     * Updates the properties of a filter view.
     * The value may be {@code null}.
     */
    public BatchUpdateBuilder updateFilterView(ConsumerReturn<UpdateFilterViewRequest> consumer) {
        UpdateFilterViewRequest object = consumer.accept(new UpdateFilterViewRequest());
        initRequest().setUpdateFilterView(object);
        return this;
    }

    /**
     * Updates a named range.
     * The value may be {@code null}.
     */
    public BatchUpdateBuilder updateNamedRange(ConsumerReturn<UpdateNamedRangeRequest> consumer) {
        UpdateNamedRangeRequest object = consumer.accept(new UpdateNamedRangeRequest());
        initRequest().setUpdateNamedRange(object);
        return this;
    }

    /**
     * Updates a protected range.
     * The value may be {@code null}.
     */
    public BatchUpdateBuilder updateProtectedRange(ConsumerReturn<UpdateProtectedRangeRequest> consumer) {
        UpdateProtectedRangeRequest object = consumer.accept(new UpdateProtectedRangeRequest());
        initRequest().setUpdateProtectedRange(object);
        return this;
    }


    /**
     * Updates a sheet's properties.
     * The value may be {@code null}.
     */
    public BatchUpdateBuilder updateSheetProperties(ConsumerReturn<SheetPropertiesBuilder> sheet) {
        SheetPropertiesBuilder builder = sheet.accept(new SheetPropertiesBuilder(null));
        initRequest().setUpdateSheetProperties(builder.buildUpdate());
        return this;
    }

    /**
     * Updates a slicer's specifications.
     * The value may be {@code null}.
     */
    public BatchUpdateBuilder updateSlicerSpec(ConsumerReturn<UpdateSlicerSpecRequest> consumer) {
        UpdateSlicerSpecRequest object = consumer.accept(new UpdateSlicerSpecRequest());
        initRequest().setUpdateSlicerSpec(object);
        return this;
    }

    /**
     * Updates the spreadsheet's properties.
     * The value may be {@code null}.
     */
    public BatchUpdateBuilder updateSpreadsheetProperties(ConsumerReturn<XslModelBuilder> consumer) {
        XslModelBuilder builder = consumer.accept(new XslModelBuilder(null));
        return updateSpreadsheetProperties(builder);
    }

    /**
     * Updates the spreadsheet's properties.
     * The value may be {@code null}.
     */
    public BatchUpdateBuilder updateSpreadsheetProperties(XslModelBuilder builder) {
        initRequest().setUpdateSpreadsheetProperties(builder.buildUpdate());
        return this;
    }

    public Request initRequest() {
        List<Request> list = Utils.setIfNull(bu::getRequests, ArrayList::new, bu::setRequests);
        Request request = new Request();
        list.add(request);
        return request;
    }

    public XslUpdateResponse execute(Sheets.Spreadsheets service, String spreadsheetId) throws IOException {
            BatchUpdateSpreadsheetRequest request = build().clone();
            BatchUpdateSpreadsheetResponse response = service.batchUpdate(spreadsheetId, request).setFields("*").execute();
            return XslUpdateResponse.from(response);
    }
}