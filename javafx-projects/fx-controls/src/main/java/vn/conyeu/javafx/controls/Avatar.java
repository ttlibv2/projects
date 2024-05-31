package vn.conyeu.javafx.controls;

import javafx.css.*;
import javafx.beans.property.StringProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.Node;
import javafx.scene.control.Skin;

import javafx.scene.control.Control;
import vn.conyeu.javafx.controls.enums.NodeSize;
import vn.conyeu.javafx.property.SupplierProperty;
import vn.conyeu.javafx.styleable.CssFactory;
import vn.conyeu.javafx.controls.skin.AvatarSkin;

import java.util.List;
import java.util.function.Supplier;

public class Avatar extends Control {

    public Avatar() {
        getStyleClass().setAll("avatar");
    }

    //@formatter:off

    /** 	Letter type unit distance between left and right sides */
    private StyleableDoubleProperty gap;
    public final void setGap(double value) {gapProperty().set(value);}
    public final double getGap() {return gapProperty().get();}
    public final StyleableDoubleProperty gapProperty() {
        if(gap == null) {
            gap = FACTORY.createDouble(this, "gap", "-fx-gap");
        }
        return gap;
    }

    /** Defines the text to display. */
    private StringProperty label;
    public final String getLabel() {return labelProperty().get();}
    public final void setLabel(String value) {labelProperty().set(value);}
    public final StringProperty labelProperty() {
        if(label == null) {
            label = new SimpleStringProperty(this, "label");
        }
        return label;
    }

    /** Defines the icon to display. */
    private ObjectProperty<Node> graphic;
    public final Node getGraphic() {return graphicProperty().get();}
    public final void setGraphic(Node value) {graphicProperty().set(value);}
    public final ObjectProperty<Node> graphicProperty() {
        if(graphic == null) {
            graphic = new SimpleObjectProperty<>(this, "graphic");
        }
        return graphic;
    }

    /** graphicLiterak */
    private StyleableStringProperty graphicLiteral;
    public final String getGraphicLiteral() {return graphicLiteralProperty().get();}
    public final void setGraphicLiteral(String value) {graphicLiteralProperty().set(value);}
    public final StyleableStringProperty graphicLiteralProperty() {
        if(graphicLiteral == null) {
            graphicLiteral = FACTORY.createString(this, "graphicLiterak", "-fx-graphic-literal");
        }
        return graphicLiteral;
    }

    /** size */
    private StyleableObjectProperty<NodeSize> size;
    public final NodeSize getSize() {return sizeProperty().get();}
    public final void setSize(NodeSize value) {sizeProperty().set(value);}
    public final StyleableObjectProperty<NodeSize> sizeProperty() {
        if(size == null) {
            size = FACTORY.createEnum(this, "size", "-fx-avatar-size", NodeSize.class);
        }
        return size;
    }

    /** avatarShape */
    private StyleableObjectProperty<AvatarShape> avatarShape;
    public final AvatarShape getAvatarShape() {return avatarShapeProperty().get();}
    public final void setAvatarShape(AvatarShape value) {avatarShapeProperty().set(value);}
    public final StyleableObjectProperty<AvatarShape> avatarShapeProperty() {
        if(avatarShape == null) {
            avatarShape = FACTORY.createEnum(this, "avatarShape", "-fx-avatar-shape", AvatarShape.class);
        }
        return avatarShape;
    }

    /** badge */
    private SupplierProperty<Badge> badge;
    public final Supplier<Badge> getBadge() {return badgeProperty().get();}
    public final void setBadge(Supplier<Badge> value) {badgeProperty().set(value);}
    public final SupplierProperty<Badge> badgeProperty() {
        if(badge == null) {
            badge = new SupplierProperty<>(this, "badge");
        }
        return badge;
    }



    //@formatter:on

    @Override
    protected Skin<?> createDefaultSkin() {
        return new AvatarSkin(this);
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
    static final CssFactory<Avatar> FACTORY = new CssFactory<>(Control.getClassCssMetaData()) {
        @Override
        protected void registerCss() {
            FACTORY.createStringCss("-fx-graphic-literal", Avatar::graphicLiteralProperty);
            FACTORY.createEnumCss("-fx-avatar-size", Avatar::sizeProperty, NodeSize.class);
            FACTORY.createSizeCss("-fx-gap", Avatar::gapProperty, 4);
            FACTORY.createEnumCss("-fx-avatar-shape", Avatar::avatarShapeProperty, AvatarShape.class, AvatarShape.square);

        }
    };

    public enum AvatarShape {
        square, circle
    }
}