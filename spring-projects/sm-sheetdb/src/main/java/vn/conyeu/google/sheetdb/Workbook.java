package vn.conyeu.google.sheetdb;

import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.model.*;
import vn.conyeu.commons.utils.Asserts;

import java.io.IOException;

public final class Workbook {
    public final Spreadsheet xsl;
    private final Sheets.Spreadsheets service;

    Workbook(Sheets.Spreadsheets service, Spreadsheet xsl) {
        this.service = Asserts.notNull(service);
        this.xsl = Asserts.notNull(xsl);
    }

    /**
     * The ID of the spreadsheet
     * @return value or {@code null} for none
     */
    public String getId() {
        return xsl.getSpreadsheetId();
    }

    /**
     * The url of the spreadsheet
     * @return value or {@code null} for none
     */
    public String getUrl() {
        return xsl.getSpreadsheetUrl();
    }

    /**
     * The amount of time to wait before volatile functions are recalculated.
     *
     * @return value or {@code null} for none
     */
    public String getAutoRecalc() {
        return getProperties().getAutoRecalc();
    }

    /**
     * The default format of all cells in the spreadsheet. 
     * @return value or {@code null} for none
     */
    public CellFormat getDefaultFormat() {
        return getProperties().getDefaultFormat();
    }

    /**
     * Determines whether and how circular references are resolved with iterative calculation.
     * Absence of this field means that circular references result in calculation errors.
     *
     * @return value or {@code null} for none
     */
    public IterativeCalculationSettings getIterativeCalculationSettings() {
        return getProperties().getIterativeCalculationSettings();
    }

    /**
     * The locale of the spreadsheet
     * @return value or {@code null} for none
     */
    public String getLocale() {
        return getProperties().getLocale();
    }

    /**
     * Theme applied to the spreadsheet.
     * @return value or {@code null} for none
     */
    public SpreadsheetTheme getSpreadsheetTheme() {
        return getProperties().getSpreadsheetTheme();
    }

    /**
     * The time zone of the spreadsheet, in CLDR format such as `America/New_York`. If the time zone
     * isn't recognized, this may be a custom time zone such as `GMT-07:00`.
     * @return value or {@code null} for none
     */
    public String getTimeZone() {
        return getProperties().getTimeZone();
    }

    /**
     * The title of the spreadsheet.
     *
     * @return value or {@code null} for none
     */
    public String getTitle() {
        return getProperties().getTitle();
    }

    private SpreadsheetProperties getProperties() {
        return xsl.getProperties();
    }

    /**
     * Inserts a new sheet into the spreadsheet, using a default sheet name. The new sheet becomes the active sheet.
     * */
    public WorkSheet addSheet(String name) throws IOException {
        BatchUpdateBuilder builder = BatchUpdateBuilder.create();
        builder.addSheet(sheet -> sheet.title(name));

        AddSheetResponse response = builder.execute(service, getId()).getReplies().get(0);


        service.batchUpdate(getId(), builder.build()).execute();
    }
}