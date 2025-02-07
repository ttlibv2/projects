package vn.conyeu.google.sheet;

import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.model.*;
import lombok.extern.slf4j.Slf4j;
import vn.conyeu.commons.utils.Jsons;
import vn.conyeu.commons.utils.Objects;
import vn.conyeu.google.core.GoogleException;
import vn.conyeu.google.sheet.builder.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static vn.conyeu.google.core.ExecuteFunc.simple;

@Slf4j
public class XslService {
    public final Sheets sheets;
    public final Sheets.Spreadsheets.Values values;

    public XslService(Sheets sheets) {
        this.sheets = sheets;
        this.values = sheets.spreadsheets().values();
    }

    /**
     * Creates a new spreadsheet with the given name.
     *
     * @param name   The name for the spreadsheet.
     * @param custom the consumer custom properties xsl
     */
    public Spreadsheet createXsl(String name, ConsumerReturn<XslBuilder> custom) {
        return simple(() -> {
            XslBuilder xslBuilder = custom.accept(new XslBuilder()).title(name);
            return sheets.spreadsheets().create(xslBuilder.build());
        });
    }

    /**
     * Opens the spreadsheet with the given ID.
     *
     * @param fileId The unique identifier for the spreadsheet.
     */
    public Spreadsheet openXsl(String fileId, String fields) {
        return openXsl(fileId, fields, new ArrayList<>());
    }


    /**
     * Opens the spreadsheet with the given ID.
     *
     * @param fileId The unique identifier for the spreadsheet.
     */
    public Spreadsheet openXsl(String fileId, String fields, List<String> ranges) {
        return simple(() -> sheets.spreadsheets().get(fileId)
                .setIncludeGridData(ranges != null && !ranges.isEmpty())
                .setRanges(ranges).setFields(fields));
    }


    /**
     * Inserts a new sheet into the spreadsheet
     */
    public SheetProperties addSheet(String fileId, ConsumerReturn<SheetPropertiesBuilder> consumer) {
        return batchUpdate(fileId, builder -> builder.addSheet(consumer)).getAddSheet().getProperties();
    }

    public SpreadsheetProperties updateXsl(String fileId, ConsumerReturn<XslModelBuilder> consumer) {
        return batchUpdate(fileId, builder -> builder.updateSpreadsheetProperties(consumer)).getSpreadsheetProperties();
    }

    public SpreadsheetProperties updateXsl(String fileId, XslModelBuilder b) {
        return batchUpdate(fileId, builder -> builder.updateSpreadsheetProperties(b)).getSpreadsheetProperties();
    }

    public void updateSheet(String fileId, ConsumerReturn<SheetPropertiesBuilder> b) {
        batchUpdate(fileId, builder -> builder.updateSheetProperties(b));
    }

    public void updateSheet(String fileId, SheetPropertiesBuilder b) {
        batchUpdate(fileId, builder -> builder.updateSheetProperties(bs -> b));
    }

    public void deleteSheet(String fileId, int sheetId) {
        batchUpdate(fileId, builder -> builder.deleteSheet(req -> req.setSheetId(sheetId)));
    }


    /**
     * Hides one or more consecutive columns starting at the given index. Use 0-index for this method.
     *
     * @param columnIndex The starting index of the columns to hide.
     * @param numColumns  The number of columns to hide.
     */
    public void hideColumn(String fileId, int sheetId, int columnIndex, int numColumns) {
        hiddenByUser(fileId, sheetId, true, Dimension.COLUMNS, columnIndex, columnIndex + numColumns);
    }

    /**
     * Hides one or more consecutive rows starting at the given index.
     *
     * @param rowIndex The index of the row to hide.
     * @param numRows  The number of rows to hide.
     */
    public void hideRow(String fileId, int sheetId, int rowIndex, int numRows) {
        hiddenByUser(fileId, sheetId, true, Dimension.ROWS, rowIndex, rowIndex + numRows);
    }

    /**
     * Unhides one or more consecutive columns starting at the given index. Use 0-index for this method.
     *
     * @param columnIndex The starting index of the columns to hide.
     * @param numColumns  The number of columns to hide.
     */
    public void showColumn(String fileId, int sheetId, int columnIndex, int numColumns) {
        hiddenByUser(fileId, sheetId, false, Dimension.COLUMNS, columnIndex, columnIndex + numColumns);
    }

    /**
     * Unhides one or more consecutive rows starting at the given index.
     *
     * @param rowIndex The index of the row to hide.
     * @param numRows  The number of rows to hide.
     */
    public void showRow(String fileId, int sheetId, int rowIndex, int numRows) {
        hiddenByUser(fileId, sheetId, false, Dimension.ROWS, rowIndex, rowIndex + numRows);
    }

    private void hiddenByUser(String fileId, int sheetId, boolean hidden, Dimension dimension, int beginIndex, int endIndex) {
        batchUpdate(fileId, builder -> builder.updateDimensionProperties(b -> b
                .hiddenByUser(hidden).dimension(dimension)
                .sheetId(sheetId).startIndex(beginIndex)
                .endIndex(endIndex)));
    }

    /**
     * Inserts a blank column in a sheet at the specified location.
     *
     * @param columnIndex The index indicating where to insert a column.
     * @param numColumns  The number of columns to insert.
     */
    public void insertColumns(String fileId, int sheetId, int columnIndex, int numColumns) {
        insertDimension(fileId, sheetId, Dimension.COLUMNS, columnIndex, columnIndex + numColumns);
    }

    /**
     * Inserts a blank column in a sheet at the specified location.
     *
     * @param rowIndex The index of the row to hide.
     * @param numRows  The number of rows to hide.
     */
    public void insertRows(String fileId, int sheetId, int rowIndex, int numRows) {
        insertDimension(fileId, sheetId, Dimension.ROWS, rowIndex, rowIndex + numRows);
    }

    private void insertDimension(String fileId, int sheetId, Dimension dimension, int beginIndex, int endIndex) {
        batchUpdate(fileId, builder -> builder.insertDimension(b -> b.dimension(dimension)
                .sheetId(sheetId).inheritFromBefore(true)
                .startIndex(beginIndex).endIndex(endIndex)));
    }

    public void moveDimension(Dimension dimension, String fileId, int sheetId, int beginIndex, int endIndex, int destinationIndex) {
        batchUpdate(fileId, req -> req.moveDimensions(dimension, fileId, sheetId, beginIndex, endIndex, destinationIndex));
    }

    public void deleteDimension(String fileId, int sheetId, Dimension dimension, int startIndex, int endIndex) {
        batchUpdate(fileId, req -> req.deleteDimension(b -> b.setDimension(dimension.name())
                .setSheetId(sheetId).setStartIndex(startIndex).setEndIndex(endIndex)));
    }


    public void clearTextFormat(String fileId, int sheetId, int beginRow, int beginCol) {
        clearAll(fileId, sheetId, beginRow, beginCol, "textFormat");
    }

    public void clearFormat(String fileId, int sheetId, int beginRow, int beginCol) {
        clearAll(fileId, sheetId, beginRow, beginCol, "formatOnly");
    }

    public void clearContent(String fileId, Integer sheetId, int beginRow, int beginCol) {
        clearAll(fileId, sheetId, beginRow, beginCol, "contentsOnly");
    }

    public void clearAll(String fileId, Integer sheetId, int beginRow, int beginCol) {
        clearAll(fileId, sheetId, beginRow, beginCol, null);
    }

    private void clearAll(String fileId, Integer sheetId, int beginRow, int beginCol, String type) {
        batchUpdate(fileId, b -> {
            boolean isNull = Objects.isBlank(type);
            boolean isFormat = false;

            if (isNull || "contentsOnly".equals(type)) {
                b.repeatCell(c -> c.sheetId(sheetId)
                        .beginRow(beginRow).beginCol(beginCol)
                        .userEnteredValue(new ExtendedValue())
                        .fields("userEnteredValue"));
            }

            if (isNull || "formatOnly".equals(type)) {
                isFormat = true;
                b.repeatCell(c -> c.sheetId(sheetId)
                        .beginRow(beginRow).beginCol(beginCol)
                        .fields("userEnteredFormat"));
            }

            if (!isFormat && "textFormat".equals(type)) {
                b.repeatCell(c -> c.sheetId(sheetId)
                        .beginRow(beginRow).beginCol(beginCol)
                        .cellFormat(CellFormatBuilder::clearFormat)
                        .fields("userEnteredFormat.textFormat"));
            }

            return b;
        });
    }

    /**
     * Copies a single sheet from a spreadsheet to another spreadsheet.
     *
     * @param fileId            The ID of the spreadsheet containing the sheet to copy.
     * @param sheetId           The ID of the sheet to copy.
     * @param destinationFileId The ID of the spreadsheet to copy the sheet to
     */
    public SheetProperties copyTo(String fileId, Integer sheetId, String destinationFileId) {
        try {
            CopySheetToAnotherSpreadsheetRequest request = new CopySheetToAnotherSpreadsheetRequest().setDestinationSpreadsheetId(destinationFileId);
            return sheets.spreadsheets().sheets().copyTo(fileId, sheetId, request).execute();
        } catch (IOException exp) {
            throw new GoogleException(exp);
        }
    }

    /**
     * Returns a range of values from a spreadsheet.
     *
     * @return the request
     */
    public ValueRange get(String fileId, ConsumerReturn<ValueGetBuilder> consumer) {
        try {
            ValueGetBuilder builder = consumer.accept(new ValueGetBuilder());
            return values.get(fileId, builder.buildRange())
                    .setValueRenderOption(builder.getValueOption())
                    .setMajorDimension(builder.getDimension())
                    .setDateTimeRenderOption(builder.getDateTimeOption())
                    .setFields(builder.getFields()).execute();
        }//
        catch (IOException exp) {
            throw new GoogleException(exp);
        }
    }

    public ValueRange update(String fileId, ConsumerReturn<ValueUpdateBuilder> consumer) {
        try {
            ValueUpdateBuilder b = consumer.accept(new ValueUpdateBuilder());
            return values.update(fileId, b.buildRange(), b.buildValue())
                    .setIncludeValuesInResponse(b.isIncludeValuesInResponse())
                    .setValueInputOption(b.getValueOption())
                    .setResponseDateTimeRenderOption(b.getDateTimeOption())
                    .setResponseValueRenderOption(b.getResponseValueRenderOption())
                    .setFields(b.getFields()).execute().getUpdatedData();
        }//
        catch (IOException exp) {
            throw new GoogleException(exp);
        }
    }

    /**
     * @param fileId  The ID of the spreadsheet to update.
     * @param range   The A1 notation of a range to search for a logical table of data. Values are appended after the last row of the table.
     * @param dataRow The request body contains an instance of ValueRange.
     */
    public ValueRange appendValueRow(String fileId, String range, List<Object> dataRow) {
        return appendValueRows(fileId, range, List.of(dataRow), new AppendOption());
    }

    /**
     * @param fileId   The ID of the spreadsheet to update.
     * @param range    The A1 notation of a range to search for a logical table of data. Values are appended after the last row of the table.
     * @param dataRows The request body contains an instance of ValueRange.
     */
    public ValueRange appendValueRows(String fileId, String range, List<List<Object>> dataRows) {
        return appendValueRows(fileId, range, dataRows, new AppendOption());
    }

    /**
     * @param fileId   The ID of the spreadsheet to update.
     * @param range    The A1 notation of a range to search for a logical table of data. Values are appended after the last row of the table.
     * @param rowValue The request body contains an instance of ValueRange.
     * @param option   The option Query parameters
     */
    public ValueRange appendValueRows(String fileId, String range, List<List<Object>> rowValue, AppendOption option) {
        return appendValue(fileId, v -> option.apply(v).range(A1RangeBuilder.valueOf(range))
                .valueRange(r -> r.majorDimension(Dimension.ROWS).addValues(rowValue))
        );
    }

    public ValueRange getColumnValues(String fileId, String range) {
        return getValues(Dimension.COLUMNS, fileId, range);
    }

    public ValueRange getRowValues(String fileId, String range) {
        return getValues(Dimension.ROWS, fileId, range);
    }

    public ValueRange getValues(Dimension dimension, String fileId, String range) {
        return simple(() -> values.get(fileId, range)
                .setMajorDimension(dimension.name())
                .setValueRenderOption(ValueRenderOption.FORMATTED_VALUE.name())
                .setDateTimeRenderOption(DateTimeRenderOption.FORMATTED_STRING.name()));
    }

    private ValueRange appendValue(String fileId, ConsumerReturn<ValueAppendBuilder> consumer) {
        return simple(() -> {
            ValueAppendBuilder b = consumer.accept(new ValueAppendBuilder());
            return values.append(fileId, b.getA1Range(), b.getValueRange())
                    .setIncludeValuesInResponse(b.isIncludeValuesInResponse())
                    .setValueInputOption(b.getValueInputOption().name())
                    .setResponseDateTimeRenderOption(b.getResponseDateTimeRenderOption().name())
                    .setResponseValueRenderOption(b.getResponseValueRenderOption().name())
                    .setInsertDataOption(b.getInsertDataOption().name())
                    .setFields(b.getFields());
        }).getUpdates().getUpdatedData();

    }

    public XslUpdateResponse batchUpdate(String fileId, ConsumerReturn<BatchUpdateBuilder> consumer) {
        return batchUpdate(fileId, consumer.accept(new BatchUpdateBuilder()));
    }


    public XslUpdateResponse batchUpdate(String fileId, BatchUpdateBuilder builder) {
        return XslUpdateResponse.from(simple(() -> {
            BatchUpdateSpreadsheetRequest request = builder.build().clone();
            log.warn("batchUpdate({}, {})", fileId, Jsons.serializeToString(request, true));
            return sheets.spreadsheets().batchUpdate(fileId, request).setFields(builder.getFields());
        }));
    }

    /**
     * Updates many cells at once.
     * The value may be {@code null}.
     */
    public void updateCells(String fileId, ConsumerReturn<UpdateCellsBuilder> consumer) {
        batchUpdate(fileId, b -> b.updateCells(consumer));
    }
}