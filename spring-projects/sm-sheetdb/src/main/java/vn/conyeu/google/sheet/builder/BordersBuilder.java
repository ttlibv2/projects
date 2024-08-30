package vn.conyeu.google.sheet.builder;

import com.google.api.services.sheets.v4.model.Border;
import com.google.api.services.sheets.v4.model.Borders;
import vn.conyeu.google.core.Utils;

public class BordersBuilder implements XmlBuilder<Borders> {
    private final Borders borders;

    public BordersBuilder(Borders borders) {
        this.borders = borders == null ? new Borders() : borders;
    }

    @Override
    public Borders build() {
        return borders;
    }

    /**
     * The bottom border of the cell.
     *
     * @param bottom bottom or {@code null} for none
     */
    public BordersBuilder bottom(ConsumerReturn<BorderBuilder> bottom) {
        initBorderBuilder("BOTTOM", bottom);
        return this;
    }

    /**
     * The left border of the cell.
     *
     * @param left left or {@code null} for none
     */
    public BordersBuilder left(ConsumerReturn<BorderBuilder> left) {
        initBorderBuilder("LEFT", left);
        return this;
    }

    /**
     * The right border of the cell.
     *
     * @param right right or {@code null} for none
     */
    public BordersBuilder right(ConsumerReturn<BorderBuilder> right) {
        initBorderBuilder("RIGHT", right);
        return this;
    }

    /**
     * The top border of the cell.
     *
     * @param top top or {@code null} for none
     */
    public BordersBuilder top(ConsumerReturn<BorderBuilder> top) {
        initBorderBuilder("TOP", top);
        return this;
    }


    private void initBorderBuilder(String pos, ConsumerReturn<BorderBuilder> consumer) {
        Border border = switch (pos) {
            case "TOP" -> Utils.setIfNull(borders::getTop, Border::new, borders::setTop);
            case "BOTTOM" -> Utils.setIfNull(borders::getBottom, Border::new, borders::setBottom);
            case "LEFT" -> Utils.setIfNull(borders::getLeft, Border::new, borders::setLeft);
            case "RIGHT" -> Utils.setIfNull(borders::getRight, Border::new, borders::setRight);
            default -> throw new UnsupportedOperationException();
        };

        consumer.accept(new BorderBuilder(border));
    }
}