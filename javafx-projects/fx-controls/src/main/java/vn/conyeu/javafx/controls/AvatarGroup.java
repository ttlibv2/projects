package vn.conyeu.javafx.controls;

import javafx.collections.FXCollections;
import javafx.css.*;
import javafx.css.SimpleStyleableBooleanProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.control.Skin;
import javafx.collections.ObservableList;

import javafx.scene.control.Control;
import vn.conyeu.javafx.styleable.CssFactory;
import vn.conyeu.javafx.controls.skin.AvatarGroupSkin;

import java.util.List;

public class AvatarGroup extends Control {

    public AvatarGroup() {
        getStyleClass().setAll("avatar-group");
        setAvatars(FXCollections.observableArrayList());
    }

    //@formatter:off

    /** avatars */
    private ListProperty<Avatar> avatars;
    public final ObservableList<Avatar> getAvatars() {return avatarsProperty().get();}
    public final void setAvatars(ObservableList<Avatar> value) {avatarsProperty().set(value);}
    public final ListProperty<Avatar> avatarsProperty() {
        if(avatars == null) {
            avatars = new SimpleListProperty<>(this, "avatars");
        }
        return avatars;
    }

    /** overflow */
    private StyleableIntegerProperty overflow;
    public final void setOverflow(int value) {overflowProperty().set(value);}
    public final int getOverflow() {return overflowProperty().get();}
    public final StyleableIntegerProperty overflowProperty() {
        if(overflow == null) {
            overflow = FACTORY.createInteger(this, "overflow", "-fx-overflow");
        }
        return overflow;
    }

    //@formatter:on

    @Override
    protected Skin<?> createDefaultSkin() {
        return new AvatarGroupSkin(this);
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
    static final CssFactory<AvatarGroup> FACTORY = new CssFactory<>(Control.getClassCssMetaData()) {
        @Override
        protected void registerCss() {
            FACTORY.createSizeCss("-fx-overflow", AvatarGroup::overflowProperty);

        }
    };

}