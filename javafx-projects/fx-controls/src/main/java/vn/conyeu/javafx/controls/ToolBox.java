package vn.conyeu.javafx.controls;

import javafx.collections.FXCollections;
import javafx.css.*;
import javafx.beans.property.StringProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.control.Skin;
import javafx.collections.ObservableList;

import javafx.scene.control.Control;
import vn.conyeu.javafx.icons.font.FontIcon;
import vn.conyeu.javafx.styleable.CssFactory;
import vn.conyeu.javafx.controls.skin.ToolBoxSkin;

import java.util.List;
import java.util.stream.Stream;

public class ToolBox extends Control {

    public ToolBox() {
        getStyleClass().setAll("tool-box");
        setLeadingNodes(FXCollections.observableArrayList());
        setTrailingNodes(FXCollections.observableArrayList());
    }

    public ToolBox addLeadingNodes(Node...nodes) {
        getLeadingNodes().addAll(nodes);
        return this;
    }

    public ToolBox addLeadingFontIcons(String...literals) {
        getLeadingNodes().addAll(Stream.of(literals).map(FontIcon::new).toList());
        return this;
    }

    public ToolBox addTrailingNodes(Node...nodes) {
        getTrailingNodes().addAll(nodes);
        return this;
    }

    public ToolBox addTrailingFontIcons(String...literals) {
        getTrailingNodes().addAll(Stream.of(literals).map(FontIcon::new).toList());
        return this;
    }

    //@formatter:off

    /** graphic */
    private ObjectProperty<Node> graphic;
    public final Node getGraphic() {return graphicProperty().get();}
    public final void setGraphic(Node value) {graphicProperty().set(value);}
    public final ObjectProperty<Node> graphicProperty() {
        if(graphic == null) {
            graphic = new SimpleObjectProperty<>(this, "graphic");
        }
        return graphic;
    }

    /** label */
    private StringProperty label;
    public final String getLabel() {return labelProperty().get();}
    public final void setLabel(String value) {labelProperty().set(value);}
    public final StringProperty labelProperty() {
        if(label == null) {
            label = new SimpleStringProperty(this, "label");
        }
        return label;
    }
    
    /** closeNode */
    private ObjectProperty<Node> closeNode;
    public final Node getCloseNode() {return closeNodeProperty().get();}
    public final void setCloseNode(Node value) {closeNodeProperty().set(value);}
    public final ObjectProperty<Node> closeNodeProperty() {
        if(closeNode == null) {
            closeNode = new SimpleObjectProperty<>(this, "closeNode");
        }
        return closeNode;
    }
    
    /** visibleClose */
    private StyleableBooleanProperty visibleClose;
    public final boolean isVisibleClose() {return visibleCloseProperty().get();}
    public final void setVisibleClose(boolean value) {visibleCloseProperty().set(value);}
    public final StyleableBooleanProperty visibleCloseProperty() {
        if(visibleClose == null) {
            visibleClose = FACTORY.createBoolean(this, "visibleClose", "-fx-visible-close");
        }
        return visibleClose;
    }
    
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

    //@formatter:on


    @Override
    protected void layoutChildren() {
        super.layoutChildren();
    }

    @Override
    protected Skin<?> createDefaultSkin() {
        return new ToolBoxSkin(this);
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
    static final CssFactory<ToolBox> FACTORY = new CssFactory<>(Control.getClassCssMetaData()) {
        @Override
        protected void registerCss() {
            FACTORY.createBooleanCss("-fx-visible-close", ToolBox::visibleCloseProperty, true);
            FACTORY.createSizeCss("-fx-leading-spacing", ToolBox::leadingSpacingProperty, 5);
            FACTORY.createSizeCss("-fx-trailing-spacing", ToolBox::trailingSpacingProperty, 5);

        }
    };

}