package vn.conyeu.javafx.icons.svg;

import javafx.beans.property.ListProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.css.*;
import javafx.scene.Group;
import javafx.scene.paint.Paint;
import vn.conyeu.commons.utils.Objects;
import vn.conyeu.javafx.icons.core.SimpleIcon;
import vn.conyeu.javafx.styleable.CssFactory;
import vn.conyeu.javafx.utils.StyleUtils;
import vn.conyeu.javafx.utils.NodeUtils;

import java.util.List;

public class SvgIcon extends Group implements SimpleIcon<SvgIcon, SvgDesc, SvgHandler> {

    public SvgIcon() {
        initialize(null);
    }

    /**
     * Create svg icon
     *
     * @see #SvgIcon(ObservableList)
     */
    public SvgIcon(String svgContent) {
        this(createSvgPath(null, svgContent, null, null));
    }

    /**
     * Create svg icon
     *
     * @see #SvgIcon(ObservableList)
     */
    public SvgIcon(String svgName, String svgContent) {
        this(createSvgPath(svgName, svgContent, null, null));
    }

    /**
     * Create svg icon
     *
     * @see #SvgIcon(ObservableList)
     */
    public SvgIcon(String svgName, String svgContent, Paint fill) {
        this(createSvgPath(svgName, svgContent, fill, null));
    }

    /**
     * Create svg icon
     *
     * @see #SvgIcon(ObservableList)
     */
    public SvgIcon(String svgName, String svgContent, Paint fill, String style) {
        this(createSvgPath(svgName, svgContent, fill, style));
    }

    /**
     * Create svg icon
     *
     * @param paths the array {@link SvgPath}
     * @see #SvgIcon(ObservableList)
     */
    public SvgIcon(SvgPath... paths) {
        this(FXCollections.observableArrayList(paths));
    }

    /**
     * Create svg icon
     *
     * @param paths the list {@link SvgPath}
     */
    public SvgIcon(ObservableList<SvgPath> paths) {
        initialize(paths);
    }

    private void initialize(ObservableList<SvgPath> svgPaths) {
        delegate.initializeListener();
        getStyleClass().setAll("icon", "svg-icon");
        setPaths(svgPaths == null ? FXCollections.observableArrayList() : svgPaths);
        setAutoSizeChildren(true);
    }

    /**
     * Add svg path to icon
     *
     * @param svgContent the svg content
     */
    public SvgPath addSvgPath(String svgContent) {
        return addSvgPath(createSvgPath(null, svgContent, null, null));
    }

    /**
     * Add svg path to icon
     *
     * @param svgName    the name of path
     * @param svgContent the svg content
     */
    public SvgPath addSvgPath(String svgName, String svgContent) {
        return addSvgPath(createSvgPath(svgName, svgContent, null, null));
    }

    /**
     * Add svg path to icon
     *
     * @param svgName    the name of path
     * @param svgContent the svg content
     * @param fill       the color path
     */
    public SvgPath addSvgPath(String svgName, String svgContent, Paint fill) {
        return addSvgPath(createSvgPath(svgName, svgContent, fill, null));
    }

    /**
     * Add svg path to icon
     *
     * @param svgName    the name of path
     * @param svgContent the svg content
     * @param fill       the color path
     * @param style      the style path
     */
    public SvgPath addSvgPath(String svgName, String svgContent, Paint fill, String style) {
        return addSvgPath(createSvgPath(svgName, svgContent, fill, style));
    }

    /**
     * Add svg path to icon
     * @param svg the path svg
     */
    public SvgPath addSvgPath(SvgPath svg) {
        getPaths().add(svg);
        return svg;
    }

    public SvgPath getPath(int index) {
        if (index < 0 || index >= sizePath()) {
            throw new IndexOutOfBoundsException("@index");
        }
        return getPaths().get(index);
    }

    public int sizePath() {
        return getPaths().size();
    }

    public void setOpacityAt(int pathIndex, double opacity) {
        SvgPath svgPath = getPath(pathIndex);
        StyleUtils.of(svgPath).opacity(opacity).apply();
    }

    public void setPixelSize(double pixel) {
        final double scale = pixel / prefWidth(-1);
        for(SvgPath svgPath:getPaths()) {
            svgPath.setScale(scale);
        }
    }

    @Override
    public void setIconLiteral(String literal) {
        throw new UnsupportedOperationException();
    }

    //@formatter:off

    /** iconDesc */
    private StyleableObjectProperty<SvgDesc> iconDesc;
    public final SvgDesc getIconDesc() {return iconDescProperty().get();}
    public final void setIconDesc(SvgDesc value) {iconDescProperty().set(value);}
    public final StyleableObjectProperty<SvgDesc> iconDescProperty() {
        if(iconDesc == null) {
            CssMetaData<SvgIcon, SvgDesc> cssMetaData = FACTORY.getCssMetaData("-fx-icon-desc");
            iconDesc = new SimpleStyleableObjectProperty<>(cssMetaData, this, "iconDesc"){};
        }
        return iconDesc;
    }

    /** iconColor */
    private StyleableObjectProperty<Paint> iconColor;
    public final Paint getIconColor() {return iconColorProperty().get();}
    public final void setIconColor(Paint value) {iconColorProperty().set(value);}
    public final StyleableObjectProperty<Paint> iconColorProperty() {
        if(iconColor == null) {
            CssMetaData<SvgIcon, Paint> cssMetaData = FACTORY.getCssMetaData("-fx-icon-color");
            iconColor = new SimpleStyleableObjectProperty<>(cssMetaData, this, "iconColor", DEFAULT_COLOR){};
        }
        return iconColor;
    }

    /** iconSize */
    private StyleableDoubleProperty iconSize;
    public final void setIconSize(double value) {iconSizeProperty().set(value);}
    public final double getIconSize() {return iconSizeProperty().get();}
    public final StyleableDoubleProperty iconSizeProperty() {
        if(iconSize == null) {
            CssMetaData<SvgIcon, Number> cssMetaData = FACTORY.getCssMetaData("-fx-icon-size");
            iconSize = new SimpleStyleableDoubleProperty(cssMetaData, this, "iconSize", DEFAULT_SIZE){};

        }
        return iconSize;
    }

    /** iconHandler */
    private ObjectProperty<SvgHandler> iconHandler;
    public final SvgHandler getIconHandler() {return iconHandlerProperty().get();}
    public final void setIconHandler(SvgHandler value) {iconHandlerProperty().set(value);}
    public final ObjectProperty<SvgHandler> iconHandlerProperty() {
        if(iconHandler == null) {
            iconHandler = new SimpleObjectProperty<>(this, "iconHandler");
        }
        return iconHandler;
    }

    /** paths */
    private ListProperty<SvgPath> paths;
    public final ObservableList<SvgPath> getPaths() {return pathsProperty().get();}
    public final void setPaths(ObservableList<SvgPath> value) {pathsProperty().set(value);}
    public final ListProperty<SvgPath> pathsProperty() {
        if(paths == null) {
            paths = new SimpleListProperty<>(this, "paths");
        }
        return paths;
    }


    //@formatter:on

    /**
     * {@inheritDoc}
     */
    public List<CssMetaData<? extends Styleable, ?>> getCssMetaData() {
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
    static final CssFactory<SvgIcon> FACTORY = new CssFactory<>(Group.getClassCssMetaData()) {
        @Override
        protected void registerCss() {
            FACTORY.createPaintCss("-fx-icon-color", SvgIcon::iconColorProperty, DEFAULT_COLOR);
            FACTORY.createSizeCss("-fx-icon-size", SvgIcon::iconSizeProperty,DEFAULT_SIZE);
            FACTORY.createObjectCss("-fx-icon-desc", SvgIcon::iconDescProperty, SvgDescConverter.getInstance(), SvgDesc.class);


        }
    };

    final SimpleIconDelegate<SvgIcon, SvgDesc, SvgHandler> delegate = new SimpleIconDelegate<>(this) {

        @Override
        public void initializeListener() {
            pathsProperty().addListener((ListChangeListener<? super SvgPath>) change -> {
                while (change.next()) {
                    int index = 1;
                    for (SvgPath svgPath : getPaths()) {
                        NodeUtils.removeStyleClassStartsWith(svgPath, "svg-path-i");
                        NodeUtils.addStyleClass(svgPath, "svg-path-i" + (index++));
                    }
                }
            });

            super.initializeListener();
        }

        @Override
        protected void applyIconSize(Number iconSize) {

        }

        @Override
        protected void applyIconDesc(SvgDesc iconDesc) {

        }

        @Override
        protected void applyIconColor(Paint color) {

        }

        @Override
        protected void applyIconHandler(SvgHandler iconHandler) {

        }
    };


    static SvgPath createSvgPath(String svgName, String svgContent, Paint fill, String style) {
        SvgPath svgPath = new SvgPath();
        if (Objects.notBlank(svgName)) svgPath.getStyleClass().setAll(svgName);
        if (Objects.notBlank(svgContent)) svgPath.setContent(svgContent);
        if (Objects.notBlank(style)) svgPath.setStyle(style);
        if (fill != null) svgPath.setFill(fill);
        return svgPath;
    }

    public static class SvgDescConverter extends StyleConverter<String, SvgDesc> {

        public static SvgDescConverter getInstance() {
            return new SvgDescConverter();
        }

    }
}