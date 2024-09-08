package vn.conyeu.google.sheet.builder;

import com.google.api.services.sheets.v4.model.BandedRange;
import com.google.api.services.sheets.v4.model.BandingProperties;
import com.google.api.services.sheets.v4.model.GridRange;
import vn.conyeu.google.core.Utils;

public class BandedRangeBuilder implements XmlBuilder<BandedRange> {
    private final BandedRange range = new BandedRange();
    private BandingPropertiesBuilder rowBuilder;
    private BandingPropertiesBuilder columnBuilder;
    private GridRangeBuilder rangeBuilder;

    @Override
    public BandedRange build() {
        return range;
    }

    /**
     * The id of the banded range.
     * @param bandedRangeId bandedRangeId or {@code null} for none
     */
    public BandedRangeBuilder bandedRangeId(Integer bandedRangeId) {
        range.setBandedRangeId(bandedRangeId);
        return this;
    }

    /**
     * Properties for row bands.
     * @param consumer the custom row properties
     */
    public BandedRangeBuilder rowProperties(ConsumerReturn<BandingPropertiesBuilder> consumer) {
        consumer.accept(getRowBuilder());
        return this;
    }

    /**
     * Properties for column bands.
     * @param consumer the custom column properties
     */
    public BandedRangeBuilder columnProperties(ConsumerReturn<BandingPropertiesBuilder> consumer) {
        consumer.accept(getColumnBuilder());
        return this;
    }

    /**
     * The range over which these properties are applied.
     * @param consumer range or {@code null} for none
     */
    public BandedRangeBuilder range(ConsumerReturn<GridRangeBuilder> consumer) {
        consumer.accept(getRangeBuilder());
        return this;
    }

    private GridRangeBuilder getRangeBuilder() {
        if (rangeBuilder == null) {
            rangeBuilder = new GridRangeBuilder(Utils.setIfNull(range::getRange, GridRange::new, range::setRange));
        }
        return rangeBuilder;
    }

    private BandingPropertiesBuilder getRowBuilder() {
        if (rowBuilder == null) {
            rowBuilder = new BandingPropertiesBuilder(Utils.setIfNull(range::getRowProperties,
                    BandingProperties::new, range::setRowProperties));
        }
        return rowBuilder;
    }

    private BandingPropertiesBuilder getColumnBuilder() {
        if (columnBuilder == null) {
            columnBuilder = new BandingPropertiesBuilder(Utils.setIfNull(range::getColumnProperties,
                    BandingProperties::new, range::setColumnProperties));
        }
        return columnBuilder;
    }
}
