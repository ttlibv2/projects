package vn.conyeu.javafx.controls;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.css.CssMetaData;
import javafx.css.Styleable;
import javafx.css.StyleableIntegerProperty;
import javafx.css.StyleableStringProperty;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.scene.control.Skin;
import javafx.scene.control.TextField;
import vn.conyeu.javafx.controls.skin.SearchBarSkin;
import vn.conyeu.javafx.icons.font.FontIcon;
import vn.conyeu.javafx.styleable.CssFactory;

import java.util.List;

public class SearchBar extends InputGroup<TextField> {

    public SearchBar() {
        super(new TextField());
        getInput().textProperty().addListener((obs, ov, text) -> {
            if(text.length() >= getMinCharacter()) {
                fireEvent(new TextEvent(text));
            }
        });
        setPromptText("Search");
    }

    public void setPromptText(String value) {
        getInput().setPromptText(value);
    }

    public void setText(String value) {
        getInput().setText(value);
    }

    public void setEditable(boolean value) {
        getInput().setEditable(value);
    }

    //@formatter:off

    /** searchIcon */
    private StyleableStringProperty searchIcon;
    public final String getSearchIcon() {return searchIconProperty().get();}
    public final void setSearchIcon(String value) {searchIconProperty().set(value);}
    public final StyleableStringProperty searchIconProperty() {
        if(searchIcon == null) {
            searchIcon = FACTORY.createString(this, "searchIcon", "-fx-search-icon");
        }
        return searchIcon;
    }

    /** clearIcon */
    private StyleableStringProperty clearIcon;
    public final String getClearIcon() {return clearIconProperty().get();}
    public final void setClearIcon(String value) {clearIconProperty().set(value);}
    public final StyleableStringProperty clearIconProperty() {
        if(clearIcon == null) {
            clearIcon = FACTORY.createString(this, "clearIcon", "-fx-clear-icon");
        }
        return clearIcon;
    }

    /** minCharacter */
    private StyleableIntegerProperty minCharacter;
    public final void setMinCharacter(int value) {minCharacterProperty().set(value);}
    public final int getMinCharacter() {return minCharacterProperty().get();}
    public final StyleableIntegerProperty minCharacterProperty() {
        if(minCharacter == null) {
            minCharacter = FACTORY.createInteger(this, "minCharacter", "-fx-min-character");
        }
        return minCharacter;
    }

    /** onChangeText */
    private ObjectProperty<EventHandler<TextEvent>> onChangeText;
    public final EventHandler<TextEvent> getOnChangeText() {return onChangeTextProperty().get();}
    public final void setOnChangeText(EventHandler<TextEvent> value) {onChangeTextProperty().set(value);}
    public final ObjectProperty<EventHandler<TextEvent>> onChangeTextProperty() {
        if(onChangeText == null) {
            onChangeText = new SimpleObjectProperty<>(this, "onChangeText") {
                @Override
                protected void invalidated() {
                    setEventHandler(TextEvent.CHANGE_TEXT, get());
                }
            };
        }
        return onChangeText;
    }

    /** onClear */
    private ObjectProperty<EventHandler<BarEvent>> onClear;
    public final EventHandler<BarEvent> getOnClear() {return onClearProperty().get();}
    public final void setOnClear(EventHandler<BarEvent> value) {onClearProperty().set(value);}
    public final ObjectProperty<EventHandler<BarEvent>> onClearProperty() {
        if(onClear == null) {
            onClear = new SimpleObjectProperty<>(this, "onClear") {
                @Override
                protected void invalidated() {
                    setEventHandler(BarEvent.ON_CLEAR, get());
                }
            };
        }
        return onClear;
    }

    //@formatter:on

    @Override
    protected Skin<?> createDefaultSkin() {
        return new SearchBarSkin(this);
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
    static final CssFactory<SearchBar> FACTORY = new CssFactory<>(InputGroup.getClassCssMetaData()) {
        @Override
        protected void registerCss() {
            FACTORY.createSizeCss("-fx-min-character", SearchBar::minCharacterProperty, 0);
            FACTORY.createStringCss("-fx-clear-icon", SearchBar::clearIconProperty, "far-xmark");
            FACTORY.createStringCss("-fx-search-icon", SearchBar::searchIconProperty, "far-search");

        }
    };


    public static class BarEvent extends Event {
        public static final EventType<BarEvent> ANY = new EventType<>(Event.ANY, "SEARCH_BAR_EVENT");
        public static final EventType<BarEvent> ON_CLEAR = new EventType<>(ANY, "SEARCH_BAR_ON_CLEAR_EVENT");

        public BarEvent(EventType<? extends BarEvent> eventType) {
            super(eventType);
        }
    }

    public static class TextEvent extends BarEvent {
        public static final EventType<TextEvent> CHANGE_TEXT = new EventType<>(BarEvent.ANY, "CHANGE_TEXT");
        private final String text;

        public TextEvent(String text) {
            super(CHANGE_TEXT);
            this.text = text;
        }

        public String getText() {
            return text;
        }
    }

}