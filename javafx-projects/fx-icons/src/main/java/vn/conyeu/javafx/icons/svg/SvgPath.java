package vn.conyeu.javafx.icons.svg;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.paint.Paint;
import javafx.scene.shape.FillRule;
import javafx.scene.shape.SVGPath;

import java.util.List;

public class SvgPath extends SVGPath {

    public static Builder builder() {
        return new Builder();
    }

    /**
     * Update fill svg path
     * @param fill the fill
     */
    public SvgPath fill(Paint fill) {
        setFill(fill);
        return this;
    }

    /**
     * Update style svg path
     * @param style the style
     */
    public SvgPath style(String style) {
        setStyle(style);
        return this;
    }

    /**
     * Update styleClass svg path
     * @param styleClass the styleClass
     */
    public SvgPath styleClass(String... styleClass) {
        getStyleClass().setAll(styleClass);
        return this;
    }

    /**
     * Update styleClass svg path
     * @param styleClass the styleClass
     */
    public SvgPath styleClass(List<String> styleClass) {
        getStyleClass().setAll(styleClass);
        return this;
    }

    /**
     * Update id svg path
     * @param id the id
     */
    public SvgPath id(String id) {
        setId(id);
        return this;
    }

    /**
     * Update fillRule svg path
     * @param fillRule the fillRule
     */
    public SvgPath fillRule(FillRule fillRule) {
        setFillRule(fillRule);
        return this;
    }

    /**
     * Update content svg path
     * @param content the content
     */
    public SvgPath content(String content) {
        setContent(content);
        return this;
    }

    public void setScale(double scale) {
        setScaleX(scale);
        setScaleY(scale);
        setScaleZ(scale);
    }

    public static final class Builder {
        private final ObservableList<String> styleClass;
        private String content;
        private FillRule fillRule;
        private Paint fill;

        private String style;

        public Builder() {
            this(null, null);
        }

        public Builder(String content) {
            this(content, null);
        }

        public Builder(String content, Paint fill) {
            this.styleClass = FXCollections.observableArrayList();
            this.content = content;
            this.fill = fill;

        }

        public Builder setContent(String content) {
            this.content = content;
            return this;
        }

        public Builder setFillRule(FillRule fillRule) {
            this.fillRule = fillRule;
            return this;
        }

        public Builder setFill(Paint fill) {
            this.fill = fill;
            return this;
        }

        public Builder setStyleClass(String... styleClass) {
            this.styleClass.setAll(styleClass);
            return this;
        }

        public Builder setStyleClass(List<String> styleClass) {
            this.styleClass.setAll(styleClass);
            return this;
        }

        public Builder setStyle(String style) {
            this.style = style;
            return this;
        }

        public SvgPath build() {
            return new SvgPath().fill(fill).style(style)
                    .styleClass(styleClass)
                    .fillRule(fillRule).content(content);
        }
    }
}