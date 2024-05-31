package vn.conyeu.javafx.controls;

import javafx.css.CssMetaData;
import javafx.css.Styleable;
import javafx.beans.property.StringProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.control.Skin;

import javafx.scene.control.Control;
import javafx.scene.paint.Color;
import vn.conyeu.javafx.controls.enums.LineStyle;
import vn.conyeu.javafx.styleable.CssFactory;
import vn.conyeu.javafx.controls.skin.DividerSkin;

import java.util.List;

import javafx.css.*;
import javafx.geometry.HPos;
import javafx.scene.paint.Paint;

public class Divider extends Control {

    public Divider() {
        getStyleClass().setAll("divider");
    }

    //@formatter:off

    /** label */
    private StringProperty label;
    public final String getLabel() {return labelProperty().get();}
    public final void setLabel(String value) {labelProperty().set(value);}
    public final StringProperty labelProperty() {
        if(label == null) {
            label = new SimpleStringProperty(this, "label");
        }
        return label;
    }

    /** graphic */
    private ObjectProperty<Node> graphic;
    public final Node getGraphic() {return graphicProperty().get();}
    public final void setGraphic(Node value) {graphicProperty().set(value);}
    public final ObjectProperty<Node> graphicProperty() {
        if(graphic == null) {
            graphic = new SimpleObjectProperty<>(this, "graphic");
        }
        return graphic;
    }

    /** graphicLiteral */
    private StyleableStringProperty graphicLiteral;
    public final String getGraphicLiteral() {return graphicLiteralProperty().get();}
    public final void setGraphicLiteral(String value) {graphicLiteralProperty().set(value);}
    public final StyleableStringProperty graphicLiteralProperty() {
        if(graphicLiteral == null) {
            graphicLiteral = FACTORY.createString(this, "graphicLiteral", "-fx-graphic-literal");
        }
        return graphicLiteral;
    }

    /** graphicGap */
    private StyleableDoubleProperty graphicGap;
    public final void setGraphicGap(double value) {graphicGapProperty().set(value);}
    public final double getGraphicGap() {return graphicGapProperty().get();}
    public final StyleableDoubleProperty graphicGapProperty() {
        if(graphicGap == null) {
            graphicGap = FACTORY.createDouble(this, "graphicGap", "-fx-graphic-gap");
        }
        return graphicGap;
    }

    /** labelGap */
    private StyleableDoubleProperty labelGap;
    public final void setLabelGap(double value) {labelGapProperty().set(value);}
    public final double getLabelGap() {return labelGapProperty().get();}
    public final StyleableDoubleProperty labelGapProperty() {
        if(labelGap == null) {
            labelGap = FACTORY.createDouble(this, "labelGap", "-fx-label-gap");
        }
        return labelGap;
    }

    /** labelPos */
    private StyleableObjectProperty<Pos> labelPos;
    public final Pos getLabelPos() {return labelPosProperty().get();}
    public final void setLabelPos(Pos value) {labelPosProperty().set(value);}
    public final StyleableObjectProperty<Pos> labelPosProperty() {
        if(labelPos == null) {
            labelPos = FACTORY.createEnum(this, "labelPos", "-fx-label-pos", Pos.class);
        }
        return labelPos;
    }

    /** vertical */
    private StyleableBooleanProperty vertical;
    public final boolean isVertical() {return verticalProperty().get();}
    public final void setVertical(boolean value) {verticalProperty().set(value);}
    public final StyleableBooleanProperty verticalProperty() {
        if(vertical == null) {
            vertical = FACTORY.createBoolean(this, "vertical", "-fx-vertical");
        }
        return vertical;
    }
    
    /** lineStyle */
    private StyleableObjectProperty<LineStyle> lineStyle;
    public final LineStyle getLineStyle() {return lineStyleProperty().get();}
    public final void setLineStyle(LineStyle value) {lineStyleProperty().set(value);}
    public final StyleableObjectProperty<LineStyle> lineStyleProperty() {
        if(lineStyle == null) {
            lineStyle = FACTORY.createEnum(this, "lineStyle", "-fx-line-style", LineStyle.class);
        }
        return lineStyle;
    }

    /** lineWeight */
    private StyleableDoubleProperty lineWeight;
    public final void setLineWeight(double value) {lineWeightProperty().set(value);}
    public final double getLineWeight() {return lineWeightProperty().get();}
    public final StyleableDoubleProperty lineWeightProperty() {
        if(lineWeight == null) {
            lineWeight = FACTORY.createDouble(this, "lineWeight", "-fx-line-weight");
        }
        return lineWeight;
    }

    /** lineColor */
    private StyleableObjectProperty<Paint> lineColor;
    public final Paint getLineColor() {return lineColorProperty().get();}
    public final void setLineColor(Paint value) {lineColorProperty().set(value);}
    public final StyleableObjectProperty<Paint> lineColorProperty() {
        if(lineColor == null) {
            lineColor = FACTORY.createPaint(this, "lineColor", "-fx-line-color");
        }
        return lineColor;
    }

    /** leftLineColor */
    private StyleableObjectProperty<Paint> leftLineColor;
    public final Paint getLeftLineColor() {return leftLineColorProperty().get();}
    public final void setLeftLineColor(Paint value) {leftLineColorProperty().set(value);}
    public final StyleableObjectProperty<Paint> leftLineColorProperty() {
        if(leftLineColor == null) {
            leftLineColor = FACTORY.createPaint(this, "leftLineColor", "-fx-line-left-color");
        }
        return leftLineColor;
    }

    /** rightLineColor */
    private StyleableObjectProperty<Paint> rightLineColor;
    public final Paint getRightLineColor() {return rightLineColorProperty().get();}
    public final void setRightLineColor(Paint value) {rightLineColorProperty().set(value);}
    public final StyleableObjectProperty<Paint> rightLineColorProperty() {
        if(rightLineColor == null) {
            rightLineColor = FACTORY.createPaint(this, "rightLineColor", "-fx-line-right-color");
        }
        return rightLineColor;
    }

    /** linePercent */
    private StyleableIntegerProperty linePercent;
    public final void setLinePercent(int value) {linePercentProperty().set(value);}
    public final int getLinePercent() {return linePercentProperty().get();}
    public final StyleableIntegerProperty linePercentProperty() {
        if(linePercent == null) {
            linePercent = FACTORY.createInteger(this, "linePercent", "-fx-line-percent");
        }
        return linePercent;
    }

    /** visibleLabel */
    private StyleableBooleanProperty visibleLabel;
    public final boolean isVisibleLabel() {return visibleLabelProperty().get();}
    public final void setVisibleLabel(boolean value) {visibleLabelProperty().set(value);}
    public final StyleableBooleanProperty visibleLabelProperty() {
        if(visibleLabel == null) {
            visibleLabel = FACTORY.createBoolean(this, "visibleLabel", "-fx-visible-label");
        }
        return visibleLabel;
    }

    //@formatter:on

    @Override
    protected Skin<?> createDefaultSkin() {
        return new DividerSkin(this);
    }

    /**
     * {@inheritDoc}
     */
    public List<CssMetaData<? extends Styleable, ?>> getControlCssMetaData() {
        return getClassCssMetaData();
    }

    /**
     * Gets the {@code CssMetaData} associated with this class, which may include the {@code CssMetaData} of its superclasses.
     *
     * @return the {@code CssMetaData}
     */
    public static List<CssMetaData<? extends Styleable, ?>> getClassCssMetaData() {
        return FACTORY.getCssMetaData();
    }

    /**
     * Methods for creating instances of StyleableProperty
     */
    static final CssFactory<Divider> FACTORY = new CssFactory<>(Control.getClassCssMetaData()) {
        @Override
        protected void registerCss() {
            FACTORY.createStringCss("-fx-graphic-literal", Divider::graphicLiteralProperty);
            FACTORY.createSizeCss("-fx-graphic-gap", Divider::graphicGapProperty, 10);
            FACTORY.createSizeCss("-fx-label-gap", Divider::labelGapProperty, 10);
            FACTORY.createEnumCss("-fx-label-pos", Divider::labelPosProperty, Pos.class, Pos.CENTER);
            FACTORY.createBooleanCss("-fx-vertical", Divider::verticalProperty, false);
            FACTORY.createEnumCss("-fx-line-style", Divider::lineStyleProperty, LineStyle.class);
            FACTORY.createSizeCss("-fx-line-weight", Divider::lineWeightProperty, 1);
            FACTORY.createPaintCss("-fx-line-color", Divider::lineColorProperty);
            FACTORY.createPaintCss("-fx-line-left-color", Divider::leftLineColorProperty);
            FACTORY.createPaintCss("-fx-line-right-color", Divider::rightLineColorProperty);
            FACTORY.createSizeCss("-fx-line-percent", Divider::linePercentProperty, -1);
            FACTORY.createBooleanCss("-fx-visible-label", Divider::visibleLabelProperty, true);


        }
    };

}