package vn.conyeu.javafx.controls.skin;

import javafx.scene.Node;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import vn.conyeu.commons.utils.Objects;
import vn.conyeu.javafx.controls.Avatar;
import vn.conyeu.javafx.controls.Avatar.AvatarShape;
import vn.conyeu.javafx.controls.Badge;
import vn.conyeu.javafx.controls.enums.NodeSize;
import vn.conyeu.javafx.utils.CssUtils;
import vn.conyeu.javafx.utils.NodeUtils;

public class AvatarSkin extends SimpleSkinBase<Avatar> {
    private StackPane container;

    private Label label;
    private Badge badge;

    public AvatarSkin(Avatar control) {
        super(control);
        initialize(control);
    }

    @Override
    public void initializeUI(Avatar control) {
        container = new StackPane();
        container.getStyleClass().setAll("container");

        label = new Label();
        label.getStyleClass().add("avatar-label");
        container.getChildren().setAll(label);
    }

    @Override
    public void initializeDataUI(Avatar control) {
        applyShape(null, control.getAvatarShape());
        applySize(null, control.getSize());
        applyLabel(control);
        applyBadge(control);
        updateChildren();
    }

    @Override
    public void initializeListener(Avatar c) {
        changeListener(c.avatarShapeProperty(), (obs, ov, nv)-> applyShape(ov, nv));
        changeListener(c.sizeProperty(), (obs, ov, nv)-> applySize(ov, nv));
        registerChangeListener(c.badgeProperty(), e -> {applyBadge(c);updateChildren();});
        registerChangeListener(c.labelProperty(), e -> {applyLabel(c);updateChildren();});
        registerChangeListener(c.graphicProperty(), e -> {applyLabel(c);updateChildren();});
        registerChangeListener(c.graphicLiteralProperty(), e -> {applyLabel(c);updateChildren();});
        registerChangeListener(c.gapProperty(), e -> applyGap(c));
    }

    private void applyBadge(Avatar c) {
        if(badge != null) badge.setContent(null);
        badge = c.getBadge() != null ? c.getBadge().get() :  null;
    }

    private void updateChildren() {
        if(badge != null) {
            badge.setContent(container);
            getChildren().setAll(badge);
        }
        else {
            getChildren().setAll(container);
        }
    }

    protected void applySize(NodeSize old, NodeSize size) {
        NodeUtils.updateStyleClass(getSkinnable(), "avatar-", old, size);
    }

    protected void applyLabel(Avatar c) {
        NodeUtils.removeStyles(c, "icon", "text");
        Node icon = resolveGraphic(c);

        if(icon != null) {
            label.setGraphic(icon);
            label.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
            NodeUtils.addStyleClass(c, "icon");
        }
        else {
            label.setText(c.getLabel());
            label.setContentDisplay(ContentDisplay.TEXT_ONLY);
            NodeUtils.addStyleClass(c, "text");
        }

    }

    private Node resolveGraphic(Avatar c) {
        Node icon = c.getGraphic();
        String literal = c.getGraphicLiteral();
        if(icon != null) return icon;
        else if(Objects.isBlank(literal)) return null;
        else return resolveIcon(literal);
    }

    protected void applyGap(Avatar c) {
    }

    protected void applyShape(AvatarShape old, AvatarShape shape) {
        CssUtils.setPseudo(getSkinnable(), "", old, shape);
    }


}