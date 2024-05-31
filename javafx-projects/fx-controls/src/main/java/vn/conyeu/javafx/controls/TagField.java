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
import javafx.scene.Node;
import javafx.scene.control.Skin;
import javafx.collections.ObservableList;

import javafx.scene.control.Control;
import javafx.scene.control.TextField;
import javafx.util.Callback;
import javafx.util.StringConverter;
import vn.conyeu.javafx.styleable.CssFactory;
import vn.conyeu.javafx.controls.skin.TagFieldSkin;
import vn.conyeu.javafx.utils.Converters;

import java.util.List;
import java.util.function.Function;

public class TagField<T> extends TextField {

    public TagField() {
        this(FXCollections.observableArrayList());
    }

    public TagField(ObservableList<T> items) {
        getStyleClass().add("tag-field");
        setConverter(Converters.defaultConverter());
        setItems(items == null ? FXCollections.observableArrayList() : items);
    }

    //@formatter:off

    /** items */
    private ListProperty<T> items;
    public final ObservableList<T> getItems() {return itemsProperty().get();}
    public final void setItems(ObservableList<T> value) {itemsProperty().set(value);}
    public final ListProperty<T> itemsProperty() {
        if(items == null) {
            items = new SimpleListProperty<>(this, "items");
        }
        return items;
    }

    /** converter */
    private ObjectProperty<StringConverter<T>> converter;
    public final StringConverter<T> getConverter() {return converterProperty().get();}
    public final void setConverter(StringConverter<T> value) {converterProperty().set(value);}
    public final ObjectProperty<StringConverter<T>> converterProperty() {
        if(converter == null) {
            converter = new SimpleObjectProperty<>(this, "converter");
        }
        return converter;
    }

    /** tagFactory */
    private ObjectProperty<Function<TagField, Tag<T>>> tagFactory;
    public final Function<TagField, Tag<T>> getTagFactory() {return tagFactoryProperty().get();}
    public final void setTagFactory(Function<TagField, Tag<T>> value) {tagFactoryProperty().set(value);}
    public final ObjectProperty<Function<TagField, Tag<T>>> tagFactoryProperty() {
        if(tagFactory == null) {
            tagFactory = new SimpleObjectProperty<>(this, "tagFactory");
        }
        return tagFactory;
    }

    /** hgap */
    private StyleableDoubleProperty hgap;
    public final void setHgap(double value) {hgapProperty().set(value);}
    public final double getHgap() {return hgapProperty().get();}
    public final StyleableDoubleProperty hgapProperty() {
        if(hgap == null) {
            hgap = FACTORY.createDouble(this, "hgap", "-fx-hgap");
        }
        return hgap;
    }

    /** vgap */
    private StyleableDoubleProperty vgap;
    public final void setVgap(double value) {vgapProperty().set(value);}
    public final double getVgap() {return vgapProperty().get();}
    public final StyleableDoubleProperty vgapProperty() {
        if(vgap == null) {
            vgap = FACTORY.createDouble(this, "vgap", "-fx-vgap");
        }
        return vgap;
    }

    /** Set custom text for clear all icon title */
    private StringProperty clearAllText;
    public final String getClearAllText() {return clearAllTextProperty().get();}
    public final void setClearAllText(String value) {clearAllTextProperty().set(value);}
    public final StringProperty clearAllTextProperty() {
        if(clearAllText == null) {
            clearAllText = new SimpleStringProperty(this, "clearAllText");
        }
        return clearAllText;
    }

    /** visibleClear */
    private StyleableBooleanProperty visibleClear;
    public final boolean isVisibleClear() {return visibleClearProperty().get();}
    public final void setVisibleClear(boolean value) {visibleClearProperty().set(value);}
    public final StyleableBooleanProperty visibleClearProperty() {
        if(visibleClear == null) {
            visibleClear = FACTORY.createBoolean(this, "visibleClear", "-fx-visible-clear");
        }
        return visibleClear;
    }

    /** clearFactory */
    private ObjectProperty<Callback<String, Node>> clearFactory;
    public final Callback<String, Node> getClearFactory() {return clearFactoryProperty().get();}
    public final void setClearFactory(Callback<String, Node> value) {clearFactoryProperty().set(value);}
    public final ObjectProperty<Callback<String, Node>> clearFactoryProperty() {
        if(clearFactory == null) {
            clearFactory = new SimpleObjectProperty<>(this, "clearFactory");
        }
        return clearFactory;
    }

    /** Clear selected values one by one when clicking backspace. Default true */
    private StyleableBooleanProperty clearOnBackspace;
    public final boolean isClearOnBackspace() {return clearOnBackspaceProperty().get();}
    public final void setClearOnBackspace(boolean value) {clearOnBackspaceProperty().set(value);}
    public final StyleableBooleanProperty clearOnBackspaceProperty() {
        if(clearOnBackspace == null) {
            clearOnBackspace = FACTORY.createBoolean(this, "clearOnBackspace", "-fx-clear-on-backspace");
        }
        return clearOnBackspace;
    }

    /** Set custom text when using tagging */
    private StringProperty addTagText;
    public final String getAddTagText() {return addTagTextProperty().get();}
    public final void setAddTagText(String value) {addTagTextProperty().set(value);}
    public final StringProperty addTagTextProperty() {
        if(addTagText == null) {
            addTagText = new SimpleStringProperty(this, "addTagText");
        }
        return addTagText;
    }
    
    /** You can set the loading state from the outside (e.g. async items loading) */
    private StyleableBooleanProperty loading;
    public final boolean isLoading() {return loadingProperty().get();}
    public final void setLoading(boolean value) {loadingProperty().set(value);}
    public final StyleableBooleanProperty loadingProperty() {
        if(loading == null) {
            loading = FACTORY.createBoolean(this, "loading", "-fx-loading");
        }
        return loading;
    }

    /** Set custom text when for loading items */
    private StringProperty loadingText;
    public final String getLoadingText() {return loadingTextProperty().get();}
    public final void setLoadingText(String value) {loadingTextProperty().set(value);}
    public final StringProperty loadingTextProperty() {
        if(loadingText == null) {
            loadingText = new SimpleStringProperty(this, "loadingText");
        }
        return loadingText;
    }

    /** Marks first item as focused when opening/filtering. */
    private StyleableBooleanProperty markFirst;
    public final boolean isMarkFirst() {return markFirstProperty().get();}
    public final void setMarkFirst(boolean value) {markFirstProperty().set(value);}
    public final StyleableBooleanProperty markFirstProperty() {
        if(markFirst == null) {
            markFirst = FACTORY.createBoolean(this, "markFirst", "-fx-mark-first");
        }
        return markFirst;
    }
    
    /** Set custom text when filter returns empty result */
    private StringProperty notFoundText;
    public final String getNotFoundText() {return notFoundTextProperty().get();}
    public final void setNotFoundText(String value) {notFoundTextProperty().set(value);}
    public final StringProperty notFoundTextProperty() {
        if(notFoundText == null) {
            notFoundText = new SimpleStringProperty(this, "notFoundText");
        }
        return notFoundText;
    }

    /** Allow to search for value. Default true */
    private StyleableBooleanProperty searchable;
    public final boolean isSearchable() {return searchableProperty().get();}
    public final void setSearchable(boolean value) {searchableProperty().set(value);}
    public final StyleableBooleanProperty searchableProperty() {
        if(searchable == null) {
            searchable = FACTORY.createBoolean(this, "searchable", "-fx-searchable");
        }
        return searchable;
    }
    //@formatter:on

    @Override
    protected Skin<?> createDefaultSkin() {
        return new TagFieldSkin(this);
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
    static final CssFactory<TagField> FACTORY = new CssFactory<>(TextField.getClassCssMetaData()) {
        @Override
        protected void registerCss() {
            FACTORY.createSizeCss("-fx-hgap", TagField::hgapProperty);
            FACTORY.createSizeCss("-fx-vgap", TagField::vgapProperty);
            FACTORY.createBooleanCss("-fx-visible-clear", TagField::visibleClearProperty);
            FACTORY.createBooleanCss("-fx-clear-on-backspace", TagField::clearOnBackspaceProperty);
            FACTORY.createBooleanCss("-fx-loading", TagField::loadingProperty);
            FACTORY.createBooleanCss("-fx-mark-first", TagField::markFirstProperty);
            FACTORY.createBooleanCss("-fx-searchable", TagField::searchableProperty);

        }
    };

}