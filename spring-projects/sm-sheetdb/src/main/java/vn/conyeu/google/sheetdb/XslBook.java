package vn.conyeu.google.sheetdb;

import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.model.*;
import vn.conyeu.commons.utils.Asserts;
import vn.conyeu.commons.utils.Objects;
import vn.conyeu.google.core.GoogleException;
import vn.conyeu.google.core.Utils;
import vn.conyeu.google.sheetdb.builder.ConsumerReturn;
import vn.conyeu.google.sheetdb.builder.SheetPropertiesBuilder;
import vn.conyeu.google.sheetdb.builder.XslPropertiesBuilder;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class XslBook {
    private final Spreadsheet xsl;
    private final Sheets.Spreadsheets service;
    private final XslBatchUpdate batchUpdate;
    private final Map<String, XslSheet> sheetNames = new HashMap<>();
    private final Map<Integer, String> sheetIds = new HashMap<>();
    private SpreadsheetProperties properties;

    //builder
    private XslPropertiesBuilder propertiesBuilder;

    XslBook(Sheets.Spreadsheets service, Spreadsheet xsl) {
        this.service = Asserts.notNull(service);
        this.xsl = Asserts.notNull(xsl);
        this.batchUpdate = new XslBatchUpdate(service, this);
        this.properties = Utils.getIfNull(xsl.getProperties(), SpreadsheetProperties::new);
        this.initWorkSheet(false);
    }

    /**
     * The ID of the spreadsheet
     *
     * @return value or {@code null} for none
     */
    public String getId() {
        return xsl.getSpreadsheetId();
    }

    /**
     * The url of the spreadsheet
     *
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
        return properties.getAutoRecalc();
    }

    /**
     * The default format of all cells in the spreadsheet.
     *
     * @return value or {@code null} for none
     */
    public CellFormat getDefaultFormat() {
        return properties.getDefaultFormat();
    }

    /**
     * Determines whether and how circular references are resolved with iterative calculation.
     * Absence of this field means that circular references result in calculation errors.
     *
     * @return value or {@code null} for none
     */
    public IterativeCalculationSettings getIterativeCalculationSettings() {
        return properties.getIterativeCalculationSettings();
    }

    /**
     * The locale of the spreadsheet
     *
     * @return value or {@code null} for none
     */
    public String getLocale() {
        return properties.getLocale();
    }

    /**
     * Theme applied to the spreadsheet.
     *
     * @return value or {@code null} for none
     */
    public SpreadsheetTheme getSpreadsheetTheme() {
        return properties.getSpreadsheetTheme();
    }

    /**
     * The time zone of the spreadsheet, in CLDR format such as `America/New_York`. If the time zone
     * isn't recognized, this may be a custom time zone such as `GMT-07:00`.
     *
     * @return value or {@code null} for none
     */
    public String getTimeZone() {
        return properties.getTimeZone();
    }

    /**
     * The title of the spreadsheet.
     *
     * @return value or {@code null} for none
     */
    public String getTitle() {
        return properties.getTitle();
    }

    /**
     * Renames the document.
     */
    public void setTitle(String title) {
        properties.setTitle(title);
        getBuilder().title(title);
    }

    /**
     * The amount of time to wait before volatile functions are recalculated.
     *
     * @param autoRecalc autoRecalc or {@code null} for none
     */
    public void setAutoRecalc(String autoRecalc) {
        properties.setAutoRecalc(autoRecalc);
        getBuilder().autoRecalc(autoRecalc);
    }

    /**
     * The locale of the spreadsheet in one of the following formats:
     * <p>
     * * an ISO 639-1 language code such as `en`
     * <p>
     * * an ISO 639-2 language code such as `fil`, if no 639-1 code exists
     * <p>
     * * a combination of the ISO language code and country code, such as `en_US`
     * <p>
     * Note: when updating this field, not all locales/languages are supported.
     *
     * @param locale locale or {@code null} for none
     */
    public void setLocale(String locale) {
        properties.setLocale(locale);
        getBuilder().locale(locale);
    }

    /**
     * The time zone of the spreadsheet, in CLDR format such as `America/New_York`. If the time zone
     * isn't recognized, this may be a custom time zone such as `GMT-07:00`.
     *
     * @param timeZone timeZone or {@code null} for none
     */
    public void setTimeZone(String timeZone) {
        properties.setTimeZone(timeZone);
        getBuilder().timeZone(timeZone);
    }

    /**
     * Inserts a new sheet into the spreadsheet, using a default sheet name.
     */
    public XslSheet addSheet() {
        return addSheet(props -> props);
    }

    /**
     * Inserts a new sheet into the spreadsheet at the given index
     *
     * @param sheetIndex The index of the newly created sheet.
     */
    public XslSheet addSheet(int sheetIndex) {
        return addSheet(props -> props.index(sheetIndex));
    }

    /**
     * Inserts a new sheet into the spreadsheet with the given name
     *
     * @param name The name of the new sheet.
     */
    public XslSheet addSheet(String name) {
        return addSheet(props -> props.title(name));
    }

    /**
     * Inserts a new sheet into the spreadsheet with the given name and uses optional advanced arguments.
     *
     * @param name     The name of the new sheet.
     * @param consumer advanced arguments.
     */
    public XslSheet addSheet(String name, ConsumerReturn<SheetPropertiesBuilder> consumer) {
        return addSheet(props -> consumer.accept(props).title(name));
    }

    /**
     * Inserts a new sheet into the spreadsheet with the given name at the given index.
     *
     * @param name       The name of the new sheet.
     * @param sheetIndex The index of the newly created sheet.
     */
    public XslSheet addSheet(String name, int sheetIndex) {
        return addSheet(props -> props.title(name).index(sheetIndex));
    }

    /**
     * Inserts a new sheet into the spreadsheet with the given name at the given index and uses optional advanced arguments.
     *
     * @param name       The name of the new sheet.
     * @param sheetIndex The index of the newly created sheet.
     * @param consumer   advanced arguments.
     */
    public XslSheet addSheet(String name, int sheetIndex, ConsumerReturn<SheetPropertiesBuilder> consumer) {
        return addSheet(props -> consumer.accept(props).title(name).index(sheetIndex));
    }

    /**
     * Inserts a new sheet into the spreadsheet, using a default sheet name. The new sheet becomes the active sheet.
     */
    public XslSheet addSheet(ConsumerReturn<SheetPropertiesBuilder> consumer) {
        SheetProperties sp = batchUpdate.addSheet(consumer);
        return newSheet(new Sheet().setProperties(sp));
    }

    /**
     * Deletes the specified sheet.
     * @param sheet The sheet to delete
     */
    public void deleteSheet(XslSheet sheet) {
       deleteSheet(sheet.getSheetId());
    }

    /**
     * Deletes the specified sheet.
     * @param sheetName The sheet name to delete
     */
    public void deleteSheet(String sheetName) {
       XslSheet sheet = sheetNames.get(sheetName);
       if(sheet != null) deleteSheet(sheet.getSheetId());
    }

    /**
     * Deletes the specified sheet.
     * @param sheetId The sheet id to delete
     */
    public void deleteSheet(int sheetId) {
        batchUpdate.deleteSheet(sheetId);
        String name = sheetIds.remove(sheetId);
        if(name != null) sheetNames.remove(name);
    }



    /**
     * Gets all the sheets in this spreadsheet.
     */
    public List<XslSheet> getSheets() {
        this.initWorkSheet(false);
        return List.copyOf(sheetNames.values());
    }

    /**
     * Returns a sheet with the given name. Returns null if there is no sheet with the given `name`.
     * @param name 	The name of the sheet to get.
     * */
    public XslSheet getSheetByName(String name) {
        return sheetNames.get(name);
    }


    public XslBook save() {
        if(propertiesBuilder != null) {
            this.properties = batchUpdate.updateXsl(propertiesBuilder);
            this.propertiesBuilder = null;
        }
        return this;
    }

    public XslBook async() {
        return this.async("*");
    }

    private XslPropertiesBuilder getBuilder() {
        if(propertiesBuilder == null) propertiesBuilder = new XslPropertiesBuilder(null);
        return propertiesBuilder;
    }

    private XslBook async(String fields) {
        try {
            Spreadsheet ss = service.get(getId()).setFields(fields).execute();
            Utils.setIfNotNull(ss::getSheets, xsl::setSheets);
            Utils.setIfNotNull(ss::getProperties, xsl::setProperties);
            Utils.setIfNotNull(ss::getDeveloperMetadata, xsl::setDeveloperMetadata);
            Utils.setIfNotNull(ss::getNamedRanges, xsl::setNamedRanges);
            if (Objects.nonNull(ss.getSheets())) this.initWorkSheet(true);
            return this;
        }//
        catch (IOException exp) {
            throw new GoogleException(exp);
        }
    }

    private void initWorkSheet(boolean override) {
        if (override || sheetNames.isEmpty()) {
            sheetNames.clear();
            sheetIds.clear();
            xsl.getSheets().forEach(this::newSheet);
        }
    }

    private XslSheet newSheet(Sheet sheet) {
        XslSheet xslSheet = new XslSheet(service, batchUpdate, this, sheet);
        String sheetName = xslSheet.getTitle();
        sheetNames.put(sheetName, xslSheet);
        sheetIds.put(xslSheet.getSheetId(), sheetName);
        return xslSheet;
    }


}