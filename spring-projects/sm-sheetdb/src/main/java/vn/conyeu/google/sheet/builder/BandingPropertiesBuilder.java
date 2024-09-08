package vn.conyeu.google.sheet.builder;

import com.google.api.services.sheets.v4.model.BandingProperties;
import com.google.api.services.sheets.v4.model.Color;
import com.google.api.services.sheets.v4.model.ColorStyle;
import vn.conyeu.google.core.Utils;

public class BandingPropertiesBuilder implements XmlBuilder<BandingProperties> {
    private final BandingProperties props;

    public BandingPropertiesBuilder(BandingProperties props) {
        this.props = Utils.getIfNull(props, BandingProperties::new);
    }

    @Override
    public BandingProperties build() {
        return props;
    }

    /**
     * The second color that is alternating.
     * @param rgbColor rgb color
     */
    public BandingPropertiesBuilder secondBandColorStyle(Color rgbColor) {
        props.setSecondBandColorStyle(new ColorStyle().setRgbColor(rgbColor));
        return this;
    }

    /**
     * The second color that is alternating.
     * @param themeColor theme color
     */
    public BandingPropertiesBuilder secondBandColorStyle(ThemeColorType themeColor) {
        props.setSecondBandColorStyle(new ColorStyle().setThemeColor(Utils.enumName(themeColor)));
        return this;
    }

    /**
     * The color of the first row or column.
     * @param rgbColor rgb color
     */
    public BandingPropertiesBuilder headerColorStyle(Color rgbColor) {
        props.setHeaderColorStyle(new ColorStyle().setRgbColor(rgbColor));
        return this;
    }

    /**
     * The color of the first row or column.
     * @param themeColor theme color
     */
    public BandingPropertiesBuilder headerColorStyle(ThemeColorType themeColor) {
        props.setHeaderColorStyle(new ColorStyle().setThemeColor(Utils.enumName(themeColor)));
        return this;
    }

    /**
     * The color of the last row or column.
     * @param rgbColor rgb color
     */
    public BandingPropertiesBuilder footerColorStyle(Color rgbColor) {
        props.setFooterColorStyle(new ColorStyle().setRgbColor(rgbColor));
        return this;
    }

    /**
     * The color of the last row or column.
     * @param themeColor theme color
     */
    public BandingPropertiesBuilder footerColorStyle(ThemeColorType themeColor) {
        props.setFooterColorStyle(new ColorStyle().setThemeColor(Utils.enumName(themeColor)));
        return this;
    }

    /**
     * The first color that is alternating.
     * @param rgbColor rgb color
     */
    public BandingPropertiesBuilder firstBandColorStyle(Color rgbColor) {
        props.setFirstBandColorStyle(new ColorStyle().setRgbColor(rgbColor));
        return this;
    }

    /**
     * The first color that is alternating.
     * @param themeColor theme color
     */
    public BandingPropertiesBuilder firstBandColorStyle(ThemeColorType themeColor) {
        props.setFirstBandColorStyle(new ColorStyle().setThemeColor(Utils.enumName(themeColor)));
        return this;
    }

}
