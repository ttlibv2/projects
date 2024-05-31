package vn.conyeu.javafx.controls.skin;

import javafx.css.PseudoClass;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.ContentDisplay;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Paint;
import vn.conyeu.javafx.animation.ripple.RippleGenerator;
import vn.conyeu.javafx.controls.Button;
import vn.conyeu.javafx.controls.enums.Severity;
import vn.conyeu.javafx.controls.enums.NodeSize;
import vn.conyeu.javafx.events.LambdaListener;
import vn.conyeu.javafx.utils.Colors;
import vn.conyeu.javafx.utils.CssUtils;
import vn.conyeu.javafx.utils.NodeUtils;
import vn.conyeu.javafx.utils.StyleUtils;

public class ButtonSkin extends javafx.scene.control.skin.ButtonSkin implements InitializeSkin<Button> {
    static final PseudoClass PS_RASISED = PseudoClass.getPseudoClass("raised");
    static final PseudoClass PS_ROUNDED = PseudoClass.getPseudoClass("rounded");

    LambdaListener listener = new LambdaListener();
    EventHandler<MouseEvent> rippleHandler = evt -> getButton()
            .getRipple().generateRipple(evt);

    private Node graphicTemp;

    public ButtonSkin(Button button) {
        super(button);
        initialize(button);
    }

    public void initializeDataUI(Button button) {
        applyRipple(button);
        applyRaised(button);
        applyRounded(button);
        applyButtonAsText(button);
        applyButtonAsOutlined(button);
        applyContentDisplay(button);
        applySeverity(null, button.getSeverity());
        applyButtonSize(null, button.getSize());
    }


    public void initializeListener(Button button) {
        registerChangeListener(button.enableRippleProperty(), e -> applyRipple(button));
        registerChangeListener(button.raisedProperty(), e -> applyRaised(button));
        registerChangeListener(button.roundedProperty(), e -> applyRounded(button));
        registerChangeListener(button.plainProperty(), e -> applyButtonAsText(button));
        registerChangeListener(button.outlinedProperty(), e -> applyButtonAsOutlined(button));
        registerChangeListener(button.contentDisplayProperty(), e -> applyContentDisplay(button));

        changeListener(button.colorProperty(), (obs, ov, nv) -> applyCustomColor(ov, nv));
        changeListener(button.severityProperty(), (obs, ov, nv) -> applySeverity(ov, nv));
        changeListener(button.sizeProperty(), (obs, ov, nv) -> applyButtonSize(ov, nv));
    }

    protected void applyCustomColor(Paint old, Paint color) {
        Button button = getButton();

        Severity severity = button.getSeverity();

        boolean isSetColor = severity == null && color != null;
        CssUtils.setPseudo(getButton(), "has-custom-color", isSetColor);

        String name = Colors.getColorName(color);
        NodeUtils.updateStyleClass(button, "button-"+name, true);



        StyleUtils.of(button)
                .addTag("-color-button-bg", color)
                .addTag("-color-button-fg", color)
                .addTag("-color-button-border", color)
                .addTag("-color-button-bg-hover", color)
                .addTag("-color-button-fg-hover", color)
                .addTag("-color-button-border-hover", color)
                .addTag("-color-button-bg-focused", color)
                .addTag("-color-button-fg-focused", color)
                .addTag("-color-button-border-focused", color)
                .addTag("-color-button-bg-pressed", color)
                .addTag("-color-button-fg-pressed", color)
                .addTag("-color-button-border-pressed", color)
                .apply();


    }

    protected void applyButtonSize(NodeSize old, NodeSize size) {
        Button btn = getButton();
        CssUtils.setPseudo(getButton(), "has-size", size != null);
        NodeUtils.updateStyleClass(getButton(), "button-", old, size);
    }

    protected void applyContentDisplay(Button button) {
        ContentDisplay display = button.getContentDisplay();
        CssUtils.setPseudo(button, "icon", display == ContentDisplay.GRAPHIC_ONLY);
        NodeUtils.updateStyleClass(button, "button-icon", display == ContentDisplay.GRAPHIC_ONLY);
    }

    protected void applyButtonAsText(Button button) {
        CssUtils.setPseudo(button, "plain", button.isPlain());
        NodeUtils.updateStyleClass(button, "button-plain", button.isPlain());
    }


    protected void applyButtonAsOutlined(Button button) {
        CssUtils.setPseudo(button, "outlined", button.isOutlined());
        NodeUtils.updateStyleClass(button, "button-outlined", button.isOutlined());
    }

    protected void applyRounded(Button button) {
        CssUtils.setPseudo(button, "rounded", button.isRounded());
        NodeUtils.updateStyleClass(button, "button-rounded", button.isRounded());
    }

    protected void applyRaised(Button button) {
        CssUtils.setPseudo(button, "raised", button.isRaised());
        NodeUtils.updateStyleClass(button, "button-raised", button.isRaised());
    }

    protected void applySeverity(Severity old, Severity severity) {
        Button btn = getButton();
        applyCustomColor(null, btn.getColor());
        CssUtils.setPseudo(btn, "has-severity", severity != null);
        CssUtils.setPseudo(btn, "", old, severity);
    }

    protected void applyRipple(Button button) {
        RippleGenerator generator = button.getRipple();
        if (button.isEnableRipple()) button.addEventHandler(MouseEvent.MOUSE_CLICKED, rippleHandler);
        else button.removeEventHandler(MouseEvent.MOUSE_CLICKED, rippleHandler);

        updateChildren();
    }

    @Override
    protected void updateChildren() {
        super.updateChildren();
        boolean enableRipple = getButton().isEnableRipple();
        RippleGenerator generator = getButton().getRipple();
        if (!getButton().isEnableRipple()) getChildren().remove(generator);
        else if (!getChildren().contains(generator)) {
            getChildren().add(0, generator);
        }
    }

    public final LambdaListener getListener() {
        return listener;
    }

    private Button getButton() {
        return (Button) getSkinnable();
    }
}