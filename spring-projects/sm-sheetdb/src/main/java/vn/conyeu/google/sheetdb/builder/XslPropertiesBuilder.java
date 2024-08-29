package vn.conyeu.google.sheetdb.builder;

import com.google.api.services.sheets.v4.model.IterativeCalculationSettings;
import com.google.api.services.sheets.v4.model.SpreadsheetProperties;
import com.google.api.services.sheets.v4.model.UpdateSpreadsheetPropertiesRequest;

import java.util.HashSet;
import java.util.Set;

public class XslPropertiesBuilder implements XmlBuilder<SpreadsheetProperties> {
    private final SpreadsheetProperties props;
    private Set<String> fields = new HashSet<>();

    public XslPropertiesBuilder(final SpreadsheetProperties props) {
        this.props = XmlBuilder.ifNull(props, SpreadsheetProperties::new);
    }

    @Override
    public SpreadsheetProperties build() {
        return props;
    }

    /**
     * The amount of time to wait before volatile functions are recalculated.
     *
     * @param autoRecalc autoRecalc or {@code null} for none
     */
    public XslPropertiesBuilder autoRecalc(String autoRecalc) {
        props.setAutoRecalc(autoRecalc);
        fields.add("autoRecalc");
        return this;
    }

    /**
     * Determines whether and how circular references are resolved with iterative calculation.
     * Absence of this field means that circular references result in calculation errors.
     *
     * @param iterativeCalculationSettings iterativeCalculationSettings or {@code null} for none
     */
    public XslPropertiesBuilder iterativeCalculationSettings(IterativeCalculationSettings iterativeCalculationSettings) {
        props.setIterativeCalculationSettings(iterativeCalculationSettings);
        fields.add("iterativeCalculationSettings");
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
    public XslPropertiesBuilder locale(String locale) {
        props.setLocale(locale);
        fields.add("locale");
        return this;
    }

    /**
     * Theme applied to the spreadsheet.
     *
     * @param theme spreadsheetTheme or {@code null} for none
     */
    public XslPropertiesBuilder spreadsheetTheme(ConsumerReturn<XslThemeBuilder> theme) {
        XslThemeBuilder builder = new XslThemeBuilder(props.getSpreadsheetTheme());
        props.setSpreadsheetTheme(theme.accept(builder).build());
        return this;
    }

    /**
     * The time zone of the spreadsheet, in CLDR format such as `America/New_York`. If the time zone
     * isn't recognized, this may be a custom time zone such as `GMT-07:00`.
     *
     * @param timeZone timeZone or {@code null} for none
     */
    public XslPropertiesBuilder timeZone(String timeZone) {
        props.setTimeZone(timeZone);
        fields.add("timeZone");
        return this;
    }

    /**
     * The title of the spreadsheet.
     *
     * @param title title or {@code null} for none
     */
    public XslPropertiesBuilder title(String title) {
        props.setTitle(title);
        fields.add("title");
        return this;
    }

    public UpdateSpreadsheetPropertiesRequest buildUpdate() {
        return new UpdateSpreadsheetPropertiesRequest()
                .setProperties(build()).setFields(String.join(",", fields));
    }
}