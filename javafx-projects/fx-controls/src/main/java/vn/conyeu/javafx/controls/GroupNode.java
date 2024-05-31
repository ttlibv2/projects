package vn.conyeu.javafx.controls;

import javafx.collections.FXCollections;
import javafx.css.*;
import javafx.css.SimpleStyleableBooleanProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.control.Skin;
import javafx.collections.ObservableList;

import javafx.scene.control.Control;
import vn.conyeu.javafx.styleable.CssFactory;
import vn.conyeu.javafx.controls.skin.GroupNodeSkin;

import java.util.List;

public class GroupNode extends Control {

    public GroupNode() {
        getStyleClass().setAll("group-node");
        setPrefixNodes(FXCollections.observableArrayList());
        setLeadingNodes(FXCollections.observableArrayList());
        setCenterNodes(FXCollections.observableArrayList());
        setTrailingNodes(FXCollections.observableArrayList());
        setSuffixNodes(FXCollections.observableArrayList());
        setOrientation(Orientation.HORIZONTAL);
    }

    //@formatter:off

   /** orientation */
   private StyleableObjectProperty<Orientation> orientation;
   public final Orientation getOrientation() {return orientationProperty().get();}
   public final void setOrientation(Orientation value) {orientationProperty().set(value);}
   public final StyleableObjectProperty<Orientation> orientationProperty() {
       if(orientation == null) {
           orientation = FACTORY.createEnum(this, "orientation", "-fx-orientation", Orientation.class);
       }
       return orientation;
   }

    /** prefixNodes */
    private ListProperty<Node> prefixNodes;
    public final ObservableList<Node> getPrefixNodes() {return prefixNodesProperty().get();}
    public final void setPrefixNodes(ObservableList<Node> value) {prefixNodesProperty().set(value);}
    public final ListProperty<Node> prefixNodesProperty() {
        if(prefixNodes == null) {
            prefixNodes = new SimpleListProperty<>(this, "prefixNodes");
        }
        return prefixNodes;
    }

    /** leadingNodes */
    private ListProperty<Node> leadingNodes;
    public final ObservableList<Node> getLeadingNodes() {return leadingNodesProperty().get();}
    public final void setLeadingNodes(ObservableList<Node> value) {leadingNodesProperty().set(value);}
    public final ListProperty<Node> leadingNodesProperty() {
        if(leadingNodes == null) {
            leadingNodes = new SimpleListProperty<>(this, "leadingNodes");
        }
        return leadingNodes;
    }

    /** centerNodes */
    private ListProperty<Node> centerNodes;
    public final ObservableList<Node> getCenterNodes() {return centerNodesProperty().get();}
    public final void setCenterNodes(ObservableList<Node> value) {centerNodesProperty().set(value);}
    public final ListProperty<Node> centerNodesProperty() {
        if(centerNodes == null) {
            centerNodes = new SimpleListProperty<>(this, "centerNodes");
        }
        return centerNodes;
    }

    /** trailingNodes */
    private ListProperty<Node> trailingNodes;
    public final ObservableList<Node> getTrailingNodes() {return trailingNodesProperty().get();}
    public final void setTrailingNodes(ObservableList<Node> value) {trailingNodesProperty().set(value);}
    public final ListProperty<Node> trailingNodesProperty() {
        if(trailingNodes == null) {
            trailingNodes = new SimpleListProperty<>(this, "trailingNodes");
        }
        return trailingNodes;
    }

    /** suffixNodes */
    private ListProperty<Node> suffixNodes;
    public final ObservableList<Node> getSuffixNodes() {return suffixNodesProperty().get();}
    public final void setSuffixNodes(ObservableList<Node> value) {suffixNodesProperty().set(value);}
    public final ListProperty<Node> suffixNodesProperty() {
        if(suffixNodes == null) {
            suffixNodes = new SimpleListProperty<>(this, "suffixNodes");
        }
        return suffixNodes;
    }

    //@formatter:on

    @Override
    protected Skin<?> createDefaultSkin() {
        return new GroupNodeSkin(this);
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
    static final CssFactory<GroupNode> FACTORY = new CssFactory<>(Control.getClassCssMetaData()) {
        @Override
        protected void registerCss() {
            FACTORY.createEnumCss("-fx-orientation", GroupNode::orientationProperty, Orientation.class);

        }
    };

}