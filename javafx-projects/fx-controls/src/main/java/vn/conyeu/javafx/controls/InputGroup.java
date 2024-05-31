package vn.conyeu.javafx.controls;

import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.css.CssMetaData;
import javafx.css.Styleable;
import javafx.css.StyleableDoubleProperty;
import javafx.scene.Node;
import javafx.scene.control.Skin;
import javafx.collections.ObservableList;

import javafx.scene.control.Control;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputControl;
import vn.conyeu.commons.utils.Asserts;
import vn.conyeu.javafx.styleable.CssFactory;
import vn.conyeu.javafx.controls.skin.InputGroupSkin;

import java.util.List;

public class InputGroup<N extends TextInputControl> extends Control {

    public static InputGroup<TextField> field() {
        TextField txt = new TextField();
        txt.setPrefColumnCount(0);
        return new InputGroup<>(txt);
    }

    public InputGroup(N inputNode) {
        getStyleClass().setAll("input-group");
        setInput(Asserts.notNull(inputNode));
        setLeadingNodes(FXCollections.observableArrayList());
        setTrailingNodes(FXCollections.observableArrayList());
        setLeftNodes(FXCollections.observableArrayList());
        setRightNodes(FXCollections.observableArrayList());
    }

    //@formatter:off

    /** leadingNodes */
    private ListProperty<Node> leadingNodes;
    public final ObservableList<Node> getLeadingNodes() {return leadingNodesProperty().get();}
    protected final void setLeadingNodes(ObservableList<Node> value) {leadingNodesProperty().set(value);}
    public final ListProperty<Node> leadingNodesProperty() {
        if(leadingNodes == null) {
            leadingNodes = new SimpleListProperty<>(this, "leadingNodes");
        }
        return leadingNodes;
    }

    /** trailingNodes */
    private ListProperty<Node> trailingNodes;
    public final ObservableList<Node> getTrailingNodes() {return trailingNodesProperty().get();}
    protected final void setTrailingNodes(ObservableList<Node> value) {trailingNodesProperty().set(value);}
    public final ListProperty<Node> trailingNodesProperty() {
        if(trailingNodes == null) {
            trailingNodes = new SimpleListProperty<>(this, "trailingNodes");
        }
        return trailingNodes;
    }

    /** rightNodes */
    private ListProperty<Node> rightNodes;
    public final ObservableList<Node> getRightNodes() {return rightNodesProperty().get();}
    protected final void setRightNodes(ObservableList<Node> value) {rightNodesProperty().set(value);}
    public final ListProperty<Node> rightNodesProperty() {
        if(rightNodes == null) {
            rightNodes = new SimpleListProperty<>(this, "rightNodes");
        }
        return rightNodes;
    }

    /** leftNodes */
    private ListProperty<Node> leftNodes;
    public final ObservableList<Node> getLeftNodes() {return leftNodesProperty().get();}
    public final void setLeftNodes(ObservableList<Node> value) {leftNodesProperty().set(value);}
    public final ListProperty<Node> leftNodesProperty() {
        if(leftNodes == null) {
            leftNodes = new SimpleListProperty<>(this, "leftNodes");
        }
        return leftNodes;
    }

    /** input */
    private ReadOnlyObjectWrapper<N> input;
    public final N getInput() {return inputPropertyImpl().get();}
    public final ReadOnlyObjectProperty<N> inputProperty() {return inputPropertyImpl().getReadOnlyProperty();}
    protected final void setInput(N input) {inputPropertyImpl().set(input);}
    protected final ReadOnlyObjectWrapper<N> inputPropertyImpl() {
        if(input == null) {
            input = new ReadOnlyObjectWrapper<>(this, "input");
        }
        return input;
    }

    /** leftSpacing */
    private StyleableDoubleProperty leftSpacing;
    public final void setLeftSpacing(double value) {leftSpacingProperty().set(value);}
    public final double getLeftSpacing() {return leftSpacingProperty().get();}
    public final StyleableDoubleProperty leftSpacingProperty() {
        if(leftSpacing == null) {
            leftSpacing = FACTORY.createDouble(this, "leftSpacing", "-fx-left-spacing");
        }
        return leftSpacing;
    }

    /** rightSpacing */
    private StyleableDoubleProperty rightSpacing;
    public final void setRightSpacing(double value) {rightSpacingProperty().set(value);}
    public final double getRightSpacing() {return rightSpacingProperty().get();}
    public final StyleableDoubleProperty rightSpacingProperty() {
        if(rightSpacing == null) {
            rightSpacing = FACTORY.createDouble(this, "rightSpacing", "-fx-right-spacing");
        }
        return rightSpacing;
    }

    /** leadingSpacing */
    private StyleableDoubleProperty leadingSpacing;
    public final void setLeadingSpacing(double value) {leadingSpacingProperty().set(value);}
    public final double getLeadingSpacing() {return leadingSpacingProperty().get();}
    public final StyleableDoubleProperty leadingSpacingProperty() {
        if(leadingSpacing == null) {
            leadingSpacing = FACTORY.createDouble(this, "leadingSpacing", "-fx-leading-spacing");
        }
        return leadingSpacing;
    }

    /** trailingSpacing */
    private StyleableDoubleProperty trailingSpacing;
    public final void setTrailingSpacing(double value) {trailingSpacingProperty().set(value);}
    public final double getTrailingSpacing() {return trailingSpacingProperty().get();}
    public final StyleableDoubleProperty trailingSpacingProperty() {
        if(trailingSpacing == null) {
            trailingSpacing = FACTORY.createDouble(this, "trailingSpacing", "-fx-trailing-spacing");
        }
        return trailingSpacing;
    }

    /** inputSpacing */
    private StyleableDoubleProperty inputSpacing;
    public final void setInputSpacing(double value) {inputSpacingProperty().set(value);}
    public final double getInputSpacing() {return inputSpacingProperty().get();}
    public final StyleableDoubleProperty inputSpacingProperty() {
        if(inputSpacing == null) {
            inputSpacing = FACTORY.createDouble(this, "inputSpacing", "-fx-input-spacing");
        }
        return inputSpacing;
    }

    /** minWidthInput */
    private StyleableDoubleProperty minWidthInput;
    public final void setMinWidthInput(double value) {minWidthInputProperty().set(value);}
    public final double getMinWidthInput() {return minWidthInputProperty().get();}
    public final StyleableDoubleProperty minWidthInputProperty() {
        if(minWidthInput == null) {
            minWidthInput = FACTORY.createDouble(this, "minWidthInput", "-fx-min-width-input");
        }
        return minWidthInput;
    }
    //@formatter:on

    @Override
    protected Skin<?> createDefaultSkin() {
        return new InputGroupSkin(this);
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
    static final CssFactory<InputGroup> FACTORY = new CssFactory<>(Control.getClassCssMetaData()) {
        @Override
        protected void registerCss() {
            FACTORY.createSizeCss("-fx-left-spacing", InputGroup::leftSpacingProperty, 5);
            FACTORY.createSizeCss("-fx-right-spacing", InputGroup::rightSpacingProperty, 5);
            FACTORY.createSizeCss("-fx-leading-spacing", InputGroup::leadingSpacingProperty, 5);
            FACTORY.createSizeCss("-fx-trailing-spacing", InputGroup::trailingSpacingProperty, 5);
            FACTORY.createSizeCss("-fx-input-spacing", InputGroup::inputSpacingProperty, 5);
            FACTORY.createSizeCss("-fx-min-width-input", InputGroup::minWidthInputProperty, 50);




        }
    };

}