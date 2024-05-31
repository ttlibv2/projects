package vn.conyeu.javafx.icons.font;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.css.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import lombok.extern.slf4j.Slf4j;
import vn.conyeu.commons.utils.Asserts;
import vn.conyeu.commons.utils.Objects;
import vn.conyeu.javafx.icons.core.SimpleIcon;
import vn.conyeu.javafx.styleable.CssFactory;
import vn.conyeu.javafx.utils.Colors;
import vn.conyeu.javafx.utils.NodeUtils;
import vn.conyeu.javafx.utils.StyleUtils;

import java.util.List;
import java.util.Optional;

@Slf4j
public class FontIcon extends Text implements SimpleIcon<FontIcon, FontDesc, FontHandler> {
    private static final double EPSILON = 0.000001d;
    private FontHandler defaultHandler;

    /**
     * Create new font icon
     *
     * @see FontIcon(String, Double, Color)
     */
    public FontIcon() {
        SimpleIconDelegate<FontIcon, FontDesc, FontHandler> delegate = new SimpleIconDelegate<>(this) {

            @Override
            public void initializeListener() {
                super.initializeListener();
                fontProperty().addListener((obs, ov, font) -> applyFont(font));
                fillProperty().addListener((obs, ov, fill) -> applyFill(fill));

            }

            @Override
            protected void applyIconSize(Number size) {
                Font font = FontIcon.this.getFont();
                if (Math.abs(font.getSize() - size.doubleValue()) >= EPSILON) {
                    setFont(Font.font(font.getFamily(), size.doubleValue()));
                }
            }

            @Override
            protected void applyIconDesc(FontDesc icon) {
                if (icon != null) {
                    FontHandler handler = resolveHandler(icon.getName());
                    setFont(Font.font(handler.getFontFamily(), getIconSize()));
                    //StyleUtils.of(simple).fontFamily(handler.getFontFamily()).apply();
                    int code = icon.getUnicode();
                    if (code <= '\uFFFF') setText(String.valueOf((char) code));
                    else setText(new String(Character.toChars(code)));
                }
            }

            @Override
            protected void applyIconColor(Paint color) {
                setFill(color);
            }

            @Override
            protected void applyIconHandler(FontHandler handler) {
                FontDesc desc = getIconDesc();
                if (desc != null) {
                    resolveCode(handler, desc.getName());
                }
            }
        };
        delegate.initializeListener();
        getStyleClass().setAll("icon", "font-icon");
        setIconSize(DEFAULT_SIZE);
        setIconColor(DEFAULT_COLOR);
    }

    /**
     * Create new font icon
     *
     * @param literal the icon literal
     * @see FontIcon(String, Double, Color)
     */
    public FontIcon(String literal) {
        this(literal, null, null);
    }

    /**
     * Create new font icon
     *
     * @param literal the icon literal
     * @param color   the icon color
     * @see FontIcon(String, Double, Color)
     */
    public FontIcon(String literal, Color color) {
        this(literal, null, color);
    }

    /**
     * Create new font icon
     *
     * @param literal the icon literal
     * @param size    the icon size
     * @see FontIcon(String, Double, Color)
     */
    public FontIcon(String literal, Double size) {
        this(literal, size, null);
    }

    /**
     * Create new font icon
     *
     * @param desc the icon description
     * @see FontIcon(FontDesc, Double, Color)
     */
    public FontIcon(FontDesc desc) {
        this(desc, null, null);
    }

    /**
     * Create new font icon
     *
     * @param desc the icon description
     * @param size the icon size
     * @see FontIcon(FontDesc, Double, Color)
     */
    public FontIcon(FontDesc desc, Double size) {
        this(desc, size, null);
    }

    /**
     * Create new font icon
     *
     * @param desc  the icon description
     * @param color the icon color
     * @see FontIcon(FontDesc, Double, Color)
     */
    public FontIcon(FontDesc desc, Color color) {
        this(desc, null, color);
    }

    /**
     * Create new font icon
     *
     * @param literal the icon literal
     * @param size    the icon size
     * @param color   the icon color
     */
    public FontIcon(String literal, Double size, Color color) {
        this();
        setIconLiteral(literal);
        if (size != null) setIconSize(size);
        if (color != null) setIconColor(color);
    }

    /**
     * Create new font icon
     *
     * @param desc  the icon description
     * @param size  the icon size
     * @param color the icon color
     */
    public FontIcon(FontDesc desc, Double size, Color color) {
        this();
        setIconDesc(desc);
        if (size != null) setIconSize(size);
        if (color != null) setIconColor(color);
    }

    public FontIcon(FontHandler handler, String literal) {
        this();
        setIconHandler(handler);
        setIconLiteral(literal);
    }



    public void setIconLiteral(String literal) {
        String[] parts = literal.split(":");
        FontHandler handler = resolveHandler(parts[0]);
        resolveCode(handler, parts[0]);
        resolveSize(literal, parts);
        resolvePaint(literal, parts);
    }


    //@formatter:off


    /** iconName */
    private StyleableStringProperty iconName;
    public final String getIconName() {return iconNameProperty().get();}
    public final void setIconName(String value) {iconNameProperty().set(value);}
    public final StyleableStringProperty iconNameProperty() {
        if(iconName == null) {
            iconName = FACTORY.createString(this, "iconName", "-fx-icon-name");
        }
        return iconName;
    }

    /** iconSize */
    private StyleableDoubleProperty iconSize;
    public final void setIconSize(double value) {iconSizeProperty().set(value);}
    public final double getIconSize() {return iconSizeProperty().get();}
    public final StyleableDoubleProperty iconSizeProperty() {
        if(iconSize == null) {
            CssMetaData<FontIcon, Number> cssMetaData = FACTORY.getCssMetaData("-fx-icon-size");
            iconSize = new SimpleStyleableDoubleProperty(cssMetaData, this, "iconSize") {
                @Override
                public void set(double size) {
                    super.set(size <=0 ? DEFAULT_SIZE : size);
                }
            };
        }
        return iconSize;
    }

    /** iconColor */
    private StyleableObjectProperty<Paint> iconColor;
    public final Paint getIconColor() {return iconColorProperty().get();}
    public final void setIconColor(Paint value) {iconColorProperty().set(value);}
    public final StyleableObjectProperty<Paint> iconColorProperty() {
        if(iconColor == null) {
            CssMetaData<FontIcon, Paint> cssMetaData = FACTORY.getCssMetaData("-fx-icon-color");
            iconColor = new SimpleStyleableObjectProperty<>(cssMetaData, this, "iconColor"){
                @Override
                public void set(Paint paint) {
                    super.set(paint == null ? DEFAULT_COLOR : paint);
                }
            };
        }
        return iconColor;
    }

    /** iconHandler */
    private ObjectProperty<FontHandler> iconHandler;
    public final FontHandler getIconHandler() {return iconHandlerProperty().get();}
    public final void setIconHandler(FontHandler value) {iconHandlerProperty().set(value);}
    public final ObjectProperty<FontHandler> iconHandlerProperty() {
        if(iconHandler == null) {
            iconHandler = new SimpleObjectProperty<>(this, "iconHandler");
        }
        return iconHandler;
    }

    /** iconDesc */
    private ObjectProperty<FontDesc> iconDesc;
    public final FontDesc getIconDesc() {return iconDescProperty().get();}
    public final void setIconDesc(FontDesc value) {iconDescProperty().set(value);}
    public final ObjectProperty<FontDesc> iconDescProperty() {
        if(iconDesc == null) {
            iconDesc = new SimpleObjectProperty<>(this, "iconDesc"){
                @Override
                public void set(FontDesc newValue) {
                    Asserts.notNull(newValue);
                    super.set(newValue);
                }
                @Override protected void invalidated() {
                    NodeUtils.addStyleClass(FontIcon.this, get().getName().toLowerCase());
                }
            };
        }
        return iconDesc;
    }

    //@formatter:on

    private FontHandler resolveHandler(String description) {
        return Optional.ofNullable(getIconHandler())
                .or(() -> Optional.ofNullable(FontResolver.getInstance().resolve(description)))
                .orElseThrow(() -> Objects.newIllegal("The handler not support icon description `%s`", description));
    }

    protected void resolveSize(String literal, String[] parts) {
        if (parts.length > 1) {
            try {
                setIconSize(Integer.parseInt(parts[1]));
            } catch (NumberFormatException e) {
                throw invalidDescription(literal, e);
            }
        }
    }

    private void resolvePaint(String literal, String[] parts) {
        if (parts.length > 2) {
            String paintTx = parts[2];
            Paint paint = Colors.parse(paintTx);
            if (paint != null) setIconColor(paint);
        }
    }

    private void resolveCode(FontHandler handler, String description) {
        setIconDesc(handler.resolve(description));
    }

    private void applyFont(Font font) {
        double size = font.getSize();
        if (getIconSize() != size) {
            setIconSize(size);
        }
    }

    private void applyFill(Paint fill) {
        Paint iconColor = getIconColor();
        if (!Objects.equals(fill, iconColor)) {
            setIconColor(fill);
        }
    }


    private static IllegalArgumentException invalidDescription(String description, Exception e) {
        throw new IllegalArgumentException("Description " + description + " is not a valid icon description", e);
    }

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
    static final CssFactory<FontIcon> FACTORY = new CssFactory<>(Text.getClassCssMetaData()) {

        @Override
        protected void registerCss() {
            FACTORY.createSizeCss("-fx-icon-size", FontIcon::iconSizeProperty, DEFAULT_SIZE);
            FACTORY.createPaintCss("-fx-icon-color", FontIcon::iconColorProperty, DEFAULT_COLOR);
            FACTORY.createStringCss("-fx-icon-name", FontIcon::iconNameProperty);

        }

    };

}