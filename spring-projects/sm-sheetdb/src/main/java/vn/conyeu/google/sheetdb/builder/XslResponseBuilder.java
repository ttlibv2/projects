package vn.conyeu.google.sheetdb.builder;

import com.google.api.services.sheets.v4.model.*;
import vn.conyeu.commons.utils.Objects;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class XslResponseBuilder {

    public static XslResponseBuilder from(BatchUpdateSpreadsheetResponse response) {
        XslResponseBuilder builder = new XslResponseBuilder();

        Spreadsheet ss = response.getUpdatedSpreadsheet();
        if (ss != null) {
            builder.setResponseAt("getSpreadsheet", ss);
            builder.setResponseAt("getSpreadsheetProperties", ss.getProperties());
        }

        List<Response> responses = response.getReplies();
        if (Objects.notEmpty(responses)) {
            responses.forEach(builder::setResponseAt);
        }

        return builder;
    }

    public Spreadsheet getSpreadsheet() {
        return getResponseAt("getSpreadsheet");
    }

    public SpreadsheetProperties getSpreadsheetProperties() {
        return getResponseAt("getSpreadsheetProperties");
    }

    /**
     * A reply from adding a banded range.
     *
     * @return value or {@code null} for none
     */
    public AddBandingResponse getAddBanding() {
        return getResponseAt("getAddBanding");
    }

    /**
     * A reply from adding a chart.
     *
     * @return value or {@code null} for none
     */
    public AddChartResponse getAddChart() {
        return getResponseAt("getAddChart");
    }

    /**
     * A reply from adding a dimension group.
     *
     * @return value or {@code null} for none
     */
    public AddDimensionGroupResponse getAddDimensionGroup() {
        return getResponseAt("getAddDimensionGroup");
    }

    /**
     * A reply from adding a filter view.
     *
     * @return value or {@code null} for none
     */
    public AddFilterViewResponse getAddFilterView() {
        return getResponseAt("getAddFilterView");
    }

    /**
     * A reply from adding a named range.
     *
     * @return value or {@code null} for none
     */
    public AddNamedRangeResponse getAddNamedRange() {
        return getResponseAt("getAddNamedRange");
    }

    /**
     * A reply from adding a protected range.
     *
     * @return value or {@code null} for none
     */
    public AddProtectedRangeResponse getAddProtectedRange() {
        return getResponseAt("getAddProtectedRange");
    }

    /**
     * A reply from adding a sheet.
     *
     * @return value or {@code null} for none
     */
    public AddSheetResponse getAddSheet() {
        return getResponseAt("getAddSheet");
    }

    /**
     * A reply from adding a slicer.
     *
     * @return value or {@code null} for none
     */
    public AddSlicerResponse getAddSlicer() {
        return getResponseAt("getAddSlicer");
    }

    /**
     * A reply from creating a developer metadata entry.
     *
     * @return value or {@code null} for none
     */
    public CreateDeveloperMetadataResponse getCreateDeveloperMetadata() {
        return getResponseAt("getCreateDeveloperMetadata");
    }

    /**
     * A reply from deleting a conditional format rule.
     *
     * @return value or {@code null} for none
     */
    public DeleteConditionalFormatRuleResponse getDeleteConditionalFormatRule() {
        return getResponseAt("getDeleteConditionalFormatRule");
    }

    /**
     * A reply from deleting a developer metadata entry.
     *
     * @return value or {@code null} for none
     */
    public DeleteDeveloperMetadataResponse getDeleteDeveloperMetadata() {
        return getResponseAt("getDeleteDeveloperMetadata");
    }

    /**
     * A reply from deleting a dimension group.
     *
     * @return value or {@code null} for none
     */
    public DeleteDimensionGroupResponse getDeleteDimensionGroup() {
        return getResponseAt("getDeleteDimensionGroup");
    }

    /**
     * A reply from removing rows containing duplicate values.
     *
     * @return value or {@code null} for none
     */
    public DeleteDuplicatesResponse getDeleteDuplicates() {
        return getResponseAt("getDeleteDuplicates");
    }

    /**
     * A reply from duplicating a filter view.
     *
     * @return value or {@code null} for none
     */
    public DuplicateFilterViewResponse getDuplicateFilterView() {
        return getResponseAt("getDuplicateFilterView");
    }

    /**
     * A reply from duplicating a sheet.
     *
     * @return value or {@code null} for none
     */
    public DuplicateSheetResponse getDuplicateSheet() {
        return getResponseAt("getDuplicateSheet");
    }

    /**
     * A reply from doing a find/replace.
     *
     * @return value or {@code null} for none
     */
    public FindReplaceResponse getFindReplace() {
        return getResponseAt("getFindReplace");
    }

    /**
     * A reply from trimming whitespace.
     *
     * @return value or {@code null} for none
     */
    public TrimWhitespaceResponse getTrimWhitespace() {
        return getResponseAt("getTrimWhitespace");
    }

    /**
     * A reply from updating a conditional format rule.
     *
     * @return value or {@code null} for none
     */
    public UpdateConditionalFormatRuleResponse getUpdateConditionalFormatRule() {
        return getResponseAt("getUpdateConditionalFormatRule");
    }

    /**
     * A reply from updating a developer metadata entry.
     *
     * @return value or {@code null} for none
     */
    public UpdateDeveloperMetadataResponse getUpdateDeveloperMetadata() {
        return getResponseAt("getUpdateDeveloperMetadata");
    }

    /**
     * A reply from updating an embedded object's position.
     *
     * @return value or {@code null} for none
     */
    public UpdateEmbeddedObjectPositionResponse getUpdateEmbeddedObjectPosition() {
        return getResponseAt("getUpdateEmbeddedObjectPosition");
    }

    private final Map<String, List<Object>> responseMap = new HashMap<>();

    private <T> T getResponseAt(String name) {
        List<Object> objectSet = responseMap.get(name);
        return Objects.isEmpty(objectSet) ? null : (T) objectSet.get(0);
    }

    private void setResponseAt(String name, Object object) {
        responseMap.computeIfAbsent(name, n -> new ArrayList<>()).add(object);
    }

    private void setResponseAt(Response response) {
        if(supplierMap.isEmpty()) buildSuppiler();
        for(String key:supplierMap.keySet()) {
            Function<Response, Object> function = supplierMap.get(key);
            Object object = function.apply(response);
            if(object != null) {
                setResponseAt(key, object);
                break;
            }
        }

    }


    private static final Map<String, Function<Response, Object>> supplierMap = new HashMap<>();
    private static void buildSuppiler() {
        supplierMap.put("getAddBanding", Response::getAddBanding);
        supplierMap.put("getAddChart", Response::getAddChart);
        supplierMap.put("getAddDimensionGroup", Response::getAddDimensionGroup);
        supplierMap.put("getAddFilterView", Response::getAddFilterView);
        supplierMap.put("getAddNamedRange", Response::getAddNamedRange);
        supplierMap.put("getAddProtectedRange", Response::getAddProtectedRange);
        supplierMap.put("getAddSheet", Response::getAddSheet);
        supplierMap.put("getAddSlicer", Response::getAddSlicer);
        supplierMap.put("getCreateDeveloperMetadata", Response::getCreateDeveloperMetadata);
        supplierMap.put("getDeleteConditionalFormatRule", Response::getDeleteConditionalFormatRule);
        supplierMap.put("getDeleteDeveloperMetadata", Response::getDeleteDeveloperMetadata);
        supplierMap.put("getDeleteDimensionGroup", Response::getDeleteDimensionGroup);
        supplierMap.put("getDeleteDuplicates", Response::getDeleteDuplicates);
        supplierMap.put("getDuplicateFilterView", Response::getDuplicateFilterView);
        supplierMap.put("getDuplicateSheet", Response::getDuplicateSheet);
        supplierMap.put("getFindReplace", Response::getFindReplace);
        supplierMap.put("getTrimWhitespace", Response::getTrimWhitespace);
        supplierMap.put("getUpdateConditionalFormatRule", Response::getUpdateConditionalFormatRule);
        supplierMap.put("getUpdateDeveloperMetadata", Response::getUpdateDeveloperMetadata);
        supplierMap.put("getUpdateEmbeddedObjectPosition", Response::getUpdateEmbeddedObjectPosition);
    }

}