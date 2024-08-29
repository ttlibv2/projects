package vn.conyeu.google.sheetdb.builder;

import com.google.api.services.sheets.v4.model.DeveloperMetadata;
import com.google.api.services.sheets.v4.model.DimensionProperties;
import com.google.api.services.sheets.v4.model.DimensionRange;
import com.google.api.services.sheets.v4.model.UpdateDimensionPropertiesRequest;
import vn.conyeu.google.core.Utils;

import java.util.*;
import java.util.function.Consumer;

public class UpdateDimensionBuilder implements XmlBuilder<UpdateDimensionPropertiesRequest> {
    private final UpdateDimensionPropertiesRequest request = new UpdateDimensionPropertiesRequest();
    private final Set<String> fields = new HashSet<>();
    private DimensionProperties dimensionProperties;
    private DimensionRange dimensionRange;
    private List<DeveloperMetadata> metadatas;

    @Override
    public UpdateDimensionPropertiesRequest build() {
        if(!fields.isEmpty()) request.setFields(String.join(",", fields));
        return request;
    }

    /**
     * The fields that should be updated.  At least one field must be specified. The root `properties`
     * is implied and should not be specified. A single `"*"` can be used as short-hand for listing
     * every field.
     *
     * @param fields fields or {@code null} for none
     */
    public UpdateDimensionBuilder fields(String fields) {
        request.setFields(fields);
        return this;
    }

    /**
     * The dimension of the span.
     *
     * @param dimension dimension or {@code null} for none
     */
    public UpdateDimensionBuilder dimension(Dimension dimension) {
        initRange().setDimension(dimension.name());
        return this;
    }

    private DimensionRange initRange() {
        if(dimensionRange == null) {
            dimensionRange = Utils.setIfNull(request::getRange, DimensionRange::new, request::setRange);
        }
        return dimensionRange;
    }

    /**
     * The end (exclusive) of the span, or not set if unbounded.
     *
     * @param endIndex endIndex or {@code null} for none
     */
    public UpdateDimensionBuilder endIndex(Integer endIndex) {
        initRange().setEndIndex(endIndex);
        return this;
    }

    /**
     * The sheet this span is on.
     *
     * @param sheetId sheetId or {@code null} for none
     */
    public UpdateDimensionBuilder sheetId(Integer sheetId) {
        initRange().setSheetId(sheetId);
        return this;
    }

    /**
     * The start (inclusive) of the span, or not set if unbounded.
     *
     * @param startIndex startIndex or {@code null} for none
     */
    public UpdateDimensionBuilder startIndex(Integer startIndex) {
        initRange().setStartIndex(startIndex);
        return this;
    }

    /**
     * The developer metadata associated with a single row or column.
     *
     * @param developerMetadata developerMetadata or {@code null} for none
     */
    public UpdateDimensionBuilder developerMetadata(List<DeveloperMetadata> developerMetadata) {
        initProperties().setDeveloperMetadata(developerMetadata);
        fields.add("developerMetadata");
        return this;
    }

    private DimensionProperties initProperties() {
        if(dimensionProperties == null) {
            dimensionProperties = Utils.setIfNull(request::getProperties, DimensionProperties::new, request::setProperties);
        }
        return dimensionProperties;
    }

    public UpdateDimensionBuilder addDeveloperMetadata(Consumer<DeveloperMetadata> metadata) {
        DeveloperMetadata developerMetadata = new DeveloperMetadata();
        metadata.accept(developerMetadata);
        initDeveloperMetadata().add(developerMetadata);
        fields.add("developerMetadata");
        return this;
    }

    private List<DeveloperMetadata> initDeveloperMetadata() {
        if(metadatas == null) {
            metadatas = Utils.setIfNull(initProperties()::getDeveloperMetadata,
                    ArrayList::new, initProperties()::setDeveloperMetadata);
        }
        return metadatas;
    }

    /**
     * True if this dimension is being filtered. This field is read-only.
     *
     * @param hiddenByFilter hiddenByFilter or {@code null} for none
     */
    public UpdateDimensionBuilder hiddenByFilter(Boolean hiddenByFilter) {
        initProperties().setHiddenByFilter(hiddenByFilter);
        fields.add("hiddenByFilter");
        return this;
    }

    /**
     * True if this dimension is explicitly hidden.
     *
     * @param hiddenByUser hiddenByUser or {@code null} for none
     */
    public UpdateDimensionBuilder hiddenByUser(Boolean hiddenByUser) {
        initProperties().setHiddenByUser(hiddenByUser); fields.add("hiddenByUser");
        return this;
    }

    /**
     * The height (if a row) or width (if a column) of the dimension in pixels.
     *
     * @param pixelSize pixelSize or {@code null} for none
     */
    public UpdateDimensionBuilder pixelSize(Integer pixelSize) {
        initProperties().setPixelSize(pixelSize); fields.add("pixelSize");
        return this;
    }
}