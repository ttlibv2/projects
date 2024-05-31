package vn.conyeu.javafx.controls.skin;

import javafx.scene.Node;
import javafx.scene.control.ContentDisplay;
import vn.conyeu.javafx.controls.Button;
import vn.conyeu.javafx.controls.SpeedDial;

import java.util.function.Supplier;

public class SpeedDialSkin extends SimpleSkinBase<SpeedDial> {
    private Button buttonNode;

    public SpeedDialSkin(SpeedDial control) {
        super(control);
        initialize(control);
    }

    private Node applyButtonNode(SpeedDial dial) {
        Supplier<Node> supplier = dial.getButtonSuppiler();
        if(supplier == null) supplier = this::defaultButtonNode;
        return supplier.get();
    }

    protected Button defaultButtonNode() {
        if(buttonNode == null) {
            buttonNode = new Button();
            buttonNode.setGraphicLiteral("far-plus");
            buttonNode.setEnableRipple(true);
        }
        return buttonNode;
    }

}