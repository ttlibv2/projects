package vn.conyeu.google.sheet.builder;

import com.google.api.services.sheets.v4.model.*;
import vn.conyeu.google.core.Utils;

import java.util.ArrayList;
import java.util.List;

public class XslBuilder implements XmlBuilder<Spreadsheet> {
    private final Spreadsheet ss = new Spreadsheet();
    private XslPropertiesBuilder propBuilder;

    @Override
    public Spreadsheet build() {
       return ss;
    }

    public XslBuilder developerMetadata(List<DeveloperMetadata> developerMetadata) {
        ss.setDeveloperMetadata(developerMetadata);
        return this;
    }

    public XslBuilder namedRanges(List<NamedRange> namedRanges) {
        ss.setNamedRanges(namedRanges);
        return this;
    }

    /**
     * The amount of time to wait before volatile functions are recalculated.
     * @param autoRecalc autoRecalc or {@code null} for none
     */
    public XslBuilder autoRecalc(String autoRecalc) {
        props().autoRecalc(autoRecalc);
        return this;
    }

    /**
     * Determines whether and how circular references are resolved with iterative calculation.
     * Absence of this field means that circular references result in calculation errors.
     *
     * @param iterativeCalculationSettings iterativeCalculationSettings or {@code null} for none
     */
    public XslBuilder iterativeCalculationSettings(IterativeCalculationSettings iterativeCalculationSettings) {
        props().iterativeCalculationSettings(iterativeCalculationSettings);
        return this;
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
    public XslBuilder locale(String locale) {
        props().locale(locale);
        return this;
    }

    /**
     * Theme applied to the spreadsheet.
     *
     * @param theme spreadsheetTheme or {@code null} for none
     */
    public XslBuilder theme(ConsumerReturn<XslThemeBuilder> theme) {
        props().spreadsheetTheme(theme);
        return this;
    }

    /**
     * The time zone of the spreadsheet, in CLDR format such as `America/New_York`. If the time zone
     * isn't recognized, this may be a custom time zone such as `GMT-07:00`.
     *
     * @param timeZone timeZone or {@code null} for none
     */
    public XslBuilder timeZone(String timeZone) {
        props().timeZone(timeZone);
        return this;
    }

    /**
     * The title of the spreadsheet.
     *
     * @param title title or {@code null} for none
     */
    public XslBuilder title(String title) {
        props().title(title);
        return this;
    }

    public XslBuilder sheets(List<Sheet> sheets) {
        ss.setSheets(sheets);
        return this;
    }

    public XslBuilder addSheet(ConsumerReturn<SheetBuilder> sheet) {
        SheetBuilder builder = sheet.accept(new SheetBuilder());
        sheets().add(builder.build());
        return this;
    }

    public XslBuilder addSheet(String name, ConsumerReturn<SheetBuilder> sheet) {
        return addSheet(s -> sheet.accept(s).title(name));
    }

    public XslBuilder addSheet(String name) {
        return addSheet(sheet -> sheet.title(name));
    }

    public XslBuilder addSheet(String name, int rows, int columns) {
        return addSheet(sheet -> sheet.title(name).rowCount(rows).columnCount(columns));
    }

    public XslBuilder addSheet(String name, int rows, int columns, ConsumerReturn<SheetBuilder> sheet) {
        return addSheet(s -> sheet.accept(s).title(name).rowCount(rows).columnCount(columns));
    }

    private List<Sheet> sheets() {
        if (ss.getSheets() == null) {
            sheets(new ArrayList<>());
        }
        return ss.getSheets();
    }

    private XslPropertiesBuilder props() {
        if (propBuilder == null) {
            propBuilder = new XslPropertiesBuilder(Utils.setIfNull(ss::getProperties,
                    SpreadsheetProperties::new, ss::setProperties));
        }
        return propBuilder;
    }
}