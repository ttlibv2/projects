package vn.conyeu.javafx.controls;

import javafx.css.CssMetaData;
import javafx.css.Styleable;
import javafx.scene.control.Skin;
import vn.conyeu.javafx.controls.skin.SliderSkin;
import vn.conyeu.javafx.styleable.CssFactory;

import java.util.List;

public class Slider extends javafx.scene.control.Slider {

    public Slider() {}

    //@formatter:off

    //@formatter:on

    @Override
    protected Skin<?> createDefaultSkin() {
        return new SliderSkin(this);
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
    static final CssFactory<Slider> FACTORY = new CssFactory<>(javafx.scene.control.Slider.getClassCssMetaData()) {
        @Override
        protected void registerCss() {
        }
    };

}