package vn.conyeu.google.sheet.builder;

import com.google.api.services.sheets.v4.model.IterativeCalculationSettings;
import com.google.api.services.sheets.v4.model.SpreadsheetProperties;
import com.google.api.services.sheets.v4.model.UpdateSpreadsheetPropertiesRequest;
import vn.conyeu.google.core.Utils;

import java.util.HashSet;
import java.util.Set;

public class XslModelBuilder implements XmlBuilder<SpreadsheetProperties> {
    private final SpreadsheetProperties props;
    private final Set<String> fields = new HashSet<>();

    public XslModelBuilder(final SpreadsheetProperties props) {
        this.props = Utils.getIfNull(props, SpreadsheetProperties::new);
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
    public XslModelBuilder autoRecalc(String autoRecalc) {
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
    public XslModelBuilder iterativeCalculationSettings(IterativeCalculationSettings iterativeCalculationSettings) {
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
    public XslModelBuilder locale(String locale) {
        props.setLocale(locale);
        fields.add("locale");
        return this;
    }

    /**
     * Theme applied to the spreadsheet.
     *
     * @param theme spreadsheetTheme or {@code null} for none
     */
    public XslModelBuilder spreadsheetTheme(ConsumerReturn<XslThemeBuilder> theme) {
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
    public XslModelBuilder timeZone(String timeZone) {
        props.setTimeZone(timeZone);
        fields.add("timeZone");
        return this;
    }

    /**
     * The title of the spreadsheet.
     *
     * @param title title or {@code null} for none
     */
    public XslModelBuilder title(String title) {
        props.setTitle(title);
        fields.add("title");
        return this;
    }

    public UpdateSpreadsheetPropertiesRequest buildUpdate() {
        return new UpdateSpreadsheetPropertiesRequest()
                .setProperties(build()).setFields(String.join(",", fields));
    }
}