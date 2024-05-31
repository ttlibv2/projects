package vn.conyeu.javafx.controls;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.css.*;
import javafx.scene.Node;
import javafx.scene.control.Skin;
import javafx.scene.paint.Paint;
import vn.conyeu.commons.utils.Asserts;
import vn.conyeu.javafx.animation.ripple.RippleGenerator;
import vn.conyeu.javafx.controls.enums.Severity;
import vn.conyeu.javafx.controls.enums.NodeSize;
import vn.conyeu.javafx.styleable.simple.Flex;
import vn.conyeu.javafx.controls.skin.ButtonSkin;
import vn.conyeu.javafx.styleable.CssFactory;

import java.util.List;
import java.util.function.Consumer;

public class Button extends javafx.scene.control.Button {
    private final RippleGenerator ripple = new RippleGenerator(this);

    /**
     * Creates a button with an empty string for its label.
     */
    public Button() {
        this(null, null);
    }

    /**
     * Creates a button with the specified text as its label.
     *
     * @param text A text string for its label.
     */
    public Button(String text) {
        this(text, null);
    }

    /**
     * Creates a button with the specified text and icon for its label.
     *
     * @param text    A text string for its label.
     * @param graphic the icon for its label.
     */
    public Button(String text, Node graphic) {
        super(text, graphic);
        getStyleClass().setAll("button");
        setEnableRipple(true);
    }

    public Button customRipple(Consumer<RippleGenerator> consumer) {
        Asserts.notNull(consumer, "@Consumer<RippleGenerator>");
        consumer.accept(ripple);
        return this;
    }

    //@formatter:off

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

    /** loadingIcon */
    private ObjectProperty<Node> loadingIcon;
    public final Node getLoadingIcon() {return loadingIconProperty().get();}
    public final void setLoadingIcon(Node value) {loadingIconProperty().set(value);}
    public final ObjectProperty<Node> loadingIconProperty() {
        if(loadingIcon == null) {
            loadingIcon = new SimpleObjectProperty<>(this, "loadingIcon");
        }
        return loadingIcon;
    }

    /** Whether the button is in loading state. */
    private BooleanProperty loading;
    public final boolean getLoading() {return loadingProperty().get();}
    public final void setLoading(boolean value) {loadingProperty().set(value);}
    public final BooleanProperty loadingProperty() {
        if(loading == null) {
            loading = new SimpleBooleanProperty(this, "visibleLoading", false);
        }
        return loading;
    }

    /** Add a shadow to indicate elevation. */
    private StyleableBooleanProperty raised;
    public final boolean isRaised() {return raisedProperty().get();}
    public final void setRaised(boolean value) {raisedProperty().set(value);}
    public final StyleableBooleanProperty raisedProperty() {
        if(raised == null) {
            raised = FACTORY.createBoolean(this, "raised", "-fx-raised");
        }
        return raised;
    }

    /** Add a circular border radius to the button. */
    private StyleableBooleanProperty rounded;
    public final boolean isRounded() {return roundedProperty().get();}
    public final void setRounded(boolean value) {roundedProperty().set(value);}
    public final StyleableBooleanProperty roundedProperty() {
        if(rounded == null) {
            rounded = FACTORY.createBoolean(this, "rounded", "-fx-rounded");
        }
        return rounded;
    }

    /** Add a plain textual class to the button without a background initially. */
    private StyleableBooleanProperty plain;
    public final boolean isPlain() {return plainProperty().get();}
    public final void setPlain(boolean value) {plainProperty().set(value);}
    public final StyleableBooleanProperty plainProperty() {
        if(plain == null) {
            plain = FACTORY.createBoolean(this, "plain", "-fx-plain");
        }
        return plain;
    }

    /** Defines the style of the button. */
    private StyleableObjectProperty<Severity> severity;
    public final Severity getSeverity() {return severityProperty().get();}
    public final void setSeverity(Severity value) {severityProperty().set(value);}
    public final StyleableObjectProperty<Severity> severityProperty() {
        if(severity == null) {
            severity = FACTORY.createEnum(this, "severity", "-fx-severity", Severity.class);
        }
        return severity;
    }

    /** Add a border class without a background initially. */
    private StyleableBooleanProperty outlined;
    public final boolean isOutlined() {return outlinedProperty().get();}
    public final void setOutlined(boolean value) {outlinedProperty().set(value);}
    public final StyleableBooleanProperty outlinedProperty() {
        if(outlined == null) {
            outlined = FACTORY.createBoolean(this, "outlined", "-fx-outlined");
        }
        return outlined;
    }
    
    /** Add a link style to the button. */
    private StyleableBooleanProperty link;
    public final boolean isLink() {return linkProperty().get();}
    public final void setLink(boolean value) {linkProperty().set(value);}
    public final StyleableBooleanProperty linkProperty() {
        if(link == null) {
            link = FACTORY.createBoolean(this, "link", "-fx-link");
        }
        return link;
    }

    /** Defines the size of the button. */
    private StyleableObjectProperty<NodeSize> size;
    public final NodeSize getSize() {return sizeProperty().get();}
    public final void setSize(NodeSize value) {sizeProperty().set(value);}
    public final StyleableObjectProperty<NodeSize> sizeProperty() {
        if(size == null) {
            size = FACTORY.createEnum(this, "size", "-fx-button-size", NodeSize.class);
        }
        return size;
    }

    /** enableRipple */
    private StyleableBooleanProperty enableRipple;
    public final boolean isEnableRipple() {return enableRippleProperty().get();}
    public final void setEnableRipple(boolean value) {enableRippleProperty().set(value);}
    public final StyleableBooleanProperty enableRippleProperty() {
        if(enableRipple == null) {
            enableRipple = FACTORY.createBoolean(this, "enableRipple", "-fx-enable-ripple");
        }
        return enableRipple;
    }

    /** color */
    private StyleableObjectProperty<Paint> color;
    public final Paint getColor() {return colorProperty().get();}
    public final void setColor(Paint value) {colorProperty().set(value);}
    public final StyleableObjectProperty<Paint> colorProperty() {
        if(color == null) {
            color = FACTORY.createPaint(this, "color", "-fx-button-color");
        }
        return color;
    }

    /** flex */
    private StyleableObjectProperty<Flex> flex;
    public final Flex getFlex() {return flexProperty().get();}
    public final void setFlex(Flex value) {flexProperty().set(value);}
    public final StyleableObjectProperty<Flex> flexProperty() {
        if(flex == null) {
            flex = FACTORY.createFlex(this, (o, flex) -> {
                FlexBox.install(Button.this, flex);
            });
        }
        return flex;
    }

    //@formatter:on


    public final RippleGenerator getRipple() {
        return ripple;
    }

    @Override
    protected Skin<?> createDefaultSkin() {
        return new ButtonSkin(this);
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
    static final CssFactory<Button> FACTORY = new CssFactory<>(javafx.scene.control.Button.getClassCssMetaData()) {
        @Override
        protected void registerCss() {
            FACTORY.createBooleanCss("-fx-raised", Button::raisedProperty, false);
            FACTORY.createBooleanCss("-fx-rounded", Button::roundedProperty, false);
            FACTORY.createBooleanCss("-fx-plain", Button::plainProperty, false);
            FACTORY.createEnumCss("-fx-severity", Button::severityProperty, Severity.class);
            FACTORY.createBooleanCss("-fx-outlined", Button::outlinedProperty, false);
            FACTORY.createBooleanCss("-fx-link", Button::linkProperty);
            FACTORY.createEnumCss("-fx-button-size", Button::sizeProperty, NodeSize.class);
            FACTORY.createStringCss("-fx-graphic-literal", Button::graphicLiteralProperty);
            FACTORY.createBooleanCss("-fx-enable-ripple", Button::enableRippleProperty, false);
            FACTORY.createPaintCss("-fx-button-color", Button::colorProperty);
            FACTORY.createFlexCss(Button::flexProperty);


        }
    };

}