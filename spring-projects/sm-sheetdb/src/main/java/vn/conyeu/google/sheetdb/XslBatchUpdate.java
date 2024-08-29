package vn.conyeu.google.sheetdb;

import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.model.*;
import vn.conyeu.commons.utils.Objects;
import vn.conyeu.google.core.GoogleException;
import vn.conyeu.google.sheetdb.builder.*;

import java.io.IOException;

public class XslBatchUpdate {
    final Sheets.Spreadsheets service;
    final XslBook xslBook;

    XslBatchUpdate(Sheets.Spreadsheets service, XslBook xslBook) {
        this.service = service;
        this.xslBook = xslBook;
    }

    protected XslUpdateResponse sendOne(ConsumerReturn<BatchUpdateBuilder> consumer) {
        try {
            BatchUpdateBuilder builder = consumer.accept(new BatchUpdateBuilder());
            return builder.execute(service, xslBook.getId());
        } catch (IOException exp) {
            throw new GoogleException(exp);
        }
    }

    public SheetProperties addSheet(ConsumerReturn<SheetPropertiesBuilder> consumer) {
        return sendOne(builder -> builder.addSheet(consumer)).getAddSheet().getProperties();
    }

    public SpreadsheetProperties updateXsl(ConsumerReturn<XslPropertiesBuilder> consumer) {
        return sendOne(builder -> builder.updateSpreadsheetProperties(consumer)).getSpreadsheetProperties();
    }

    public SpreadsheetProperties updateXsl(XslPropertiesBuilder b) {
        return sendOne(builder -> builder.updateSpreadsheetProperties(b)).getSpreadsheetProperties();
    }

    public void deleteSheet(int sheetId) {
        sendOne(builder -> builder.deleteSheet(req -> req.setSheetId(sheetId)));
    }

    public void updateSheet(ConsumerReturn<SheetPropertiesBuilder> b) {
        sendOne(builder -> builder.updateSheetProperties(b));
    }

    public void updateSheet(SheetPropertiesBuilder b) {
        sendOne(builder -> builder.updateSheetProperties(bs -> b));
    }

    /**
     * Hides one or more consecutive columns starting at the given index. Use 0-index for this method.
     *
     * @param columnIndex The starting index of the columns to hide.
     * @param numColumns  The number of columns to hide.
     */
    public void hideColumn(int sheetId, int columnIndex, int numColumns) {
        hiddenByUser(sheetId, true, Dimension.COLUMNS, columnIndex, columnIndex+numColumns);
    }

    /**
     * Hides one or more consecutive rows starting at the given index.
     *
     * @param rowIndex The index of the row to hide.
     * @param numRows  The number of rows to hide.
     */
    public void hideRow(int sheetId, int rowIndex, int numRows) {
        hiddenByUser(sheetId, true, Dimension.ROWS, rowIndex, rowIndex+numRows);
    }

    /**
     * Unhides one or more consecutive columns starting at the given index. Use 0-index for this method.
     *
     * @param columnIndex The starting index of the columns to hide.
     * @param numColumns  The number of columns to hide.
     */
    public void showColumn(int sheetId, int columnIndex, int numColumns) {
        hiddenByUser(sheetId, false, Dimension.COLUMNS, columnIndex, columnIndex+numColumns);
    }

    /**
     * Unhides one or more consecutive rows starting at the given index.
     *
     * @param rowIndex The index of the row to hide.
     * @param numRows  The number of rows to hide.
     */
    public void showRow(int sheetId, int rowIndex, int numRows) {
        hiddenByUser(sheetId, false, Dimension.ROWS, rowIndex, rowIndex+numRows);
    }

    private void hiddenByUser(int sheetId, boolean hidden, Dimension dimension, int beginIndex, int endIndex) {
        sendOne(builder -> builder.updateDimensionProperties(b -> b
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
    public void insertColumns(int sheetId, int columnIndex, int numColumns) {
        insertDimension(sheetId, Dimension.COLUMNS, columnIndex, columnIndex+numColumns);
    }

    /**
     * Inserts a blank column in a sheet at the specified location.
     *
     * @param rowIndex The index of the row to hide.
     * @param numRows  The number of rows to hide.
     */
    public void insertRows(int sheetId, int rowIndex, int numRows) {
        insertDimension(sheetId, Dimension.ROWS, rowIndex, rowIndex+numRows);
    }

    private void insertDimension(int sheetId, Dimension dimension, int beginIndex, int endIndex) {
        sendOne(builder -> builder.insertDimension(b -> b.dimension(dimension)
                .sheetId(sheetId).inheritFromBefore(true)
                .startIndex(beginIndex).endIndex(endIndex)));
    }

    public void moveDimension(int sheetId, Dimension dimension, int startIndex, int endIndex, int destinationIndex) {
        sendOne(req -> req.moveDimension(b -> b.destinationIndex(destinationIndex)
                .dimension(dimension).sheetId(sheetId)
                .startIndex(startIndex).endIndex(endIndex)));
    }

    public void deleteDimension(int sheetId, Dimension dimension, int startIndex, int endIndex) {
        sendOne(req -> req.deleteDimension(b -> b.setDimension(dimension.name())
                .setSheetId(sheetId).setStartIndex(startIndex).setEndIndex(endIndex)));
    }

    public void clearTextFormat(int sheetId, int beginRow, int beginCol) {
       clearAll("textFormat", sheetId, beginRow, beginCol);
    }

    public void clearFormat(int sheetId, int beginRow, int beginCol) {
        clearAll("formatOnly", sheetId, beginRow, beginCol);
    }

    public void clearContent(Integer sheetId, int beginRow, int beginCol) {
        clearAll("contentsOnly", sheetId, beginRow, beginCol);
    }

    public void clearAll(Integer sheetId, int beginRow, int beginCol) {
        clearAll(null, sheetId, beginRow, beginCol);
    }

    private void clearAll(String type, Integer sheetId, int beginRow, int beginCol) {
        ConsumerReturn<BatchUpdateBuilder> builder = b -> {
            boolean isNull = Objects.isBlank(type);
            boolean isFormat = false;

            if(isNull || "contentsOnly".equals(type)) {
                b.repeatCell(c -> c.sheetId(sheetId)
                        .startRowIndex(beginRow).startColumnIndex(beginCol)
                        .userEnteredValue(new ExtendedValue())
                        .fields("userEnteredValue"));
            }

            if(isNull || "formatOnly".equals(type)) {
                isFormat = true;
                b.repeatCell(c -> c.sheetId(sheetId)
                        .startRowIndex(beginRow).startColumnIndex(beginCol)
                        .fields("userEnteredFormat"));
            }

            if(!isFormat &&  "textFormat".equals(type)) {
                b.repeatCell(c -> c.sheetId(sheetId)
                        .startRowIndex(beginRow).startColumnIndex(beginCol)
                        .cellFormat(cf -> cf.textFormat(tx -> tx))
                        .fields("userEnteredFormat.textFormat"));
            }

            return b;
        };

        sendOne(builder);
    }




}