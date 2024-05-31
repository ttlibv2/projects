package vn.conyeu.javafx.controls;

import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableMap;
import javafx.css.*;
import javafx.scene.Node;
import javafx.scene.control.Control;
import javafx.scene.control.Skin;
import javafx.scene.paint.Color;
import javafx.util.Callback;
import vn.conyeu.javafx.controls.skin.RatingSkin;
import vn.conyeu.javafx.styleable.CssFactory;

import java.util.List;

public class Rating extends Control {

    public Rating() {
        this(0);
    }

    public Rating(double rating) {
        this(rating, 5);
    }

    public Rating(double rating, int max) {
        getStyleClass().setAll("rating");
        setTooltips(FXCollections.observableHashMap());
        setMax(max);
        setRating(rating);
    }

    //@formatter:off

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

    /** whether to allow semi selection */
    private StyleableBooleanProperty allowHalf;
    public final boolean isAllowHalf() {return allowHalfProperty().get();}
    public final void setAllowHalf(boolean value) {allowHalfProperty().set(value);}
    public final StyleableBooleanProperty allowHalfProperty() {
        if(allowHalf == null) {
            allowHalf = FACTORY.createBoolean(this, "allowHalf", "-fx-allow-half");
        }
        return allowHalf;
    }

    /** The maximum-allowed rating value.*/
    private StyleableIntegerProperty max;
    public final void setMax(int value) {maxProperty().set(value);}
    public final int getMax() {return maxProperty().get();}
    public final StyleableIntegerProperty maxProperty() {
        if(max == null) {
            max = FACTORY.createInteger(this, "count", "-fx-rating-max");
        }
        return max;
    }

    /** value */
    private DoubleProperty rating;
    public final double getRating() {return ratingProperty().get();}
    public final void setRating(double rating) {ratingProperty().set(rating);}
    public final DoubleProperty ratingProperty() {
        if(rating == null) {
            rating = new SimpleDoubleProperty(this, "rating");
        }
        return rating;
    }

    /** &lt;rating_[index=0,...], String&gt; */
    private MapProperty<String, String> tooltips;
    public final ObservableMap<String, String> getTooltips() {return tooltipsProperty().get();}
    protected final void setTooltips(ObservableMap<String, String> rating) {tooltipsProperty().set(rating);}
    public final MapProperty<String, String> tooltipsProperty() {
        if(tooltips == null) {
            tooltips = new SimpleMapProperty<>(this, "ratingTooltips");
        }
        return tooltips;
    }

    /** spacing */
    private StyleableDoubleProperty spacing;
    public final void setSpacing(double value) {spacingProperty().set(value);}
    public final double getSpacing() {return spacingProperty().get();}
    public final StyleableDoubleProperty spacingProperty() {
        if(spacing == null) {
            spacing = FACTORY.createDouble(this, "spacing", "-fx-rating-spacing");
        }
        return spacing;
    }

    /** starFactory */
    private ObjectProperty<Callback<Rating, Node>> starFactory;
    public final Callback<Rating, Node> getStarFactory() {return starFactoryProperty().get();}
    public final void setStarFactory(Callback<Rating, Node> value) {starFactoryProperty().set(value);}
    public final ObjectProperty<Callback<Rating, Node>> starFactoryProperty() {
        if(starFactory == null) {
            starFactory = new SimpleObjectProperty<>(this, "starFactory");
        }
        return starFactory;
    }

    /** ratingIcon */
    private StyleableStringProperty ratingIcon;
    public final String getRatingIcon() {return ratingIconProperty().get();}
    public final void setRatingIcon(String value) {ratingIconProperty().set(value);}
    public final StyleableStringProperty ratingIconProperty() {
        if(ratingIcon == null) {
            ratingIcon = FACTORY.createString(this, "ratingIcon", "-fx-rating-icon");
        }
        return ratingIcon;
    }

    /** updateOnHover */
    private StyleableBooleanProperty updateOnHover;
    public final boolean isUpdateOnHover() {return updateOnHoverProperty().get();}
    public final void setUpdateOnHover(boolean value) {updateOnHoverProperty().set(value);}
    public final StyleableBooleanProperty updateOnHoverProperty() {
        if(updateOnHover == null) {
            updateOnHover = FACTORY.createBoolean(this, "updateOnHover", "-fx-update-hover");
        }
        return updateOnHover;
    }

    /** strongColor */
    private StyleableObjectProperty<Color> strongColor;
    public final Color getStrongColor() {return strongColorProperty().get();}
    public final void setStrongColor(Color value) {strongColorProperty().set(value);}
    public final StyleableObjectProperty<Color> strongColorProperty() {
        if(strongColor == null) {
            strongColor = FACTORY.createColor(this, "starFill", "-fx-rating-strong-color");
        }
        return strongColor;
    }

    /** iconColor */
    private StyleableObjectProperty<Color> iconColor;
    public final Color getIconColor() {return iconColorProperty().get();}
    public final void setIconColor(Color value) {iconColorProperty().set(value);}
    public final StyleableObjectProperty<Color> iconColorProperty() {
        if(iconColor == null) {
            iconColor = FACTORY.createColor(this, "iconColor", "-fx-rating-color");
        }
        return iconColor;
    }

    /** iconSize */
    private StyleableDoubleProperty iconSize;
    public final void setIconSize(double value) {iconSizeProperty().set(value);}
    public final double getIconSize() {return iconSizeProperty().get();}
    public final StyleableDoubleProperty iconSizeProperty() {
        if(iconSize == null) {
            iconSize = FACTORY.createDouble(this, "iconSize", "-fx-icon-size");
        }
        return iconSize;
    }

    /** readonly */
    private StyleableBooleanProperty readonly;
    public final boolean isReadonly() {return readonlyProperty().get();}
    public final void setReadonly(boolean value) {readonlyProperty().set(value);}
    public final StyleableBooleanProperty readonlyProperty() {
        if(readonly == null) {
            readonly = FACTORY.createBoolean(this, "readonly", "-fx-rating-readonly");
        }
        return readonly;
    }

    /** revert */
    private StyleableBooleanProperty verticalRevert;
    public final boolean isVerticalRevert() {return verticalRevertProperty().get();}
    public final void setVerticalRevert(boolean value) {verticalRevertProperty().set(value);}
    public final StyleableBooleanProperty verticalRevertProperty() {
        if(verticalRevert == null) {
            verticalRevert = FACTORY.createBoolean(this, "revert", "-fx-vertical-revert");
        }
        return verticalRevert;
    }

    //@formatter:on


    @Override
    protected Skin<?> createDefaultSkin() {
        return new RatingSkin(this);
    }

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
    static final CssFactory<Rating> FACTORY = new CssFactory<>(Control.getClassCssMetaData()) {
        @Override
        protected void registerCss() {
            FACTORY.createBooleanCss("-fx-allow-half", Rating::allowHalfProperty);
            FACTORY.createBooleanCss("-fx-vertical", Rating::verticalProperty, false);

            FACTORY.createStringCss("-fx-rating-icon", Rating::ratingIconProperty, STAR_ICON);
            FACTORY.createSizeCss("-fx-rating-spacing", Rating::spacingProperty, 5);
            FACTORY.createColorCss("-fx-rating-color", Rating::iconColorProperty, STAR_COLOR);
            FACTORY.createColorCss("-fx-rating-strong-color", Rating::strongColorProperty, STRONG_COLOR);


            FACTORY.createSizeCss("-fx-rating-max", Rating::maxProperty, 5);

            FACTORY.createBooleanCss("-fx-update-hover", Rating::updateOnHoverProperty, false);
            FACTORY.createSizeCss("-fx-icon-size", Rating::iconSizeProperty, STAR_SIZE);
            FACTORY.createBooleanCss("-fx-rating-readonly", Rating::readonlyProperty, false);
            FACTORY.createBooleanCss("-fx-vertical-revert", Rating::verticalRevertProperty, true);


        }
    };

    public static final Color STRONG_COLOR = Color.valueOf("#0999f3");
    public static final Color STAR_COLOR = Color.valueOf("#f5f4f4");
    public static final String STAR_ICON = "fas-star";
    public static final int STAR_SIZE = 20;

}