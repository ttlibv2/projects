package vn.conyeu.javafx.controls.skin;

import javafx.event.EventHandler;
import javafx.scene.AccessibleAttribute;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyEvent;
import javafx.util.StringConverter;
import lombok.extern.slf4j.Slf4j;
import vn.conyeu.javafx.controls.FlexBox;
import vn.conyeu.javafx.controls.TagField;
import vn.conyeu.javafx.utils.FxUtils;

import java.util.List;

@Slf4j
public class TagFieldSkin<T> extends SimpleSkinBase<TagField<T>> {

    private EditorField editor;
    private Container container;
    private ScrollPane scrollPane;

    final EventHandler<KeyEvent> onAnyEvent = ke -> {
        Object target = ke.getTarget();
        Node editor = editor(), skin = getSkinnable();

        if(!target.equals(editor) && target.equals(skin)) {
            switch (ke.getCode()) {
                case ESCAPE, F10 -> {}
                default -> {
                    editor.fireEvent(ke.copyFor(editor, editor));
                    ke.consume();
                }
            }
        }
    };


    public TagFieldSkin(TagField<T> control) {
        super(control);
        initialize(control);
    }

    @Override
    public void initializeUI(TagField<T> control) {
        editor = new EditorField();

        container = new Container();
        container.setEditor(editor);
        getChildren().setAll(container);
    }

    @Override
    public void initializeListener(TagField<T> control) {
        registerChangeListener(control.focusedProperty(), e -> editor.handleFocus(control.isFocused()));
        control.addEventFilter(KeyEvent.ANY, onAnyEvent);

    }

    @Override
    protected double computePrefWidth(double height, double top, double right, double bottom, double left) {
        return snapSizeX(editor.prefWidth(height)) + left + right;
    }

    @Override
    protected double computePrefHeight(double width, double top, double right, double bottom, double left) {
        return snapSizeY(50) + top + bottom;
    }

    @Override
    protected void layoutChildren(double layoutX, double layoutY, double insideWidth, double insideHeight) {
        container.resizeRelocate(layoutX, layoutY, insideWidth, insideHeight);
        editor.resize(insideWidth, 32);
    }

    public EditorField editor() {
        return editor;
    }

    protected class Container extends FlexBox {

        public Container() {
            hgapProperty().bind(getSkinnable().hgapProperty());
            vgapProperty().bind(getSkinnable().vgapProperty());
        }

        public void setEditor(EditorField editor) {
            setFlexOrder(editor, Integer.MAX_VALUE);
            container.getChildren().add(editor);
        }
    }

    protected class EditorField extends TextArea {
        
        public EditorField() {
            getStyleClass().add("editor");
            setPrefRowCount(1);
            setWrapText(true);
           // setManaged(false);
            addEventFilter(KeyEvent.KEY_PRESSED, e -> {
                log.warn("editor click -> {}", e);
                switch (e.getCode()) {
                    case ENTER -> onEnterHandler(e);
                    case TAB -> onTabHandler(e);
                    case BACK_SPACE -> onBackSpaceHandler(e);
                    case SPACE ->  onSpaceHandler(e);
                }
            });
            textProperty().addListener(obs -> {

            });
        }

        private void onSpaceHandler(KeyEvent e) {
//            if (event.isControlDown()) {
//                if (!autoCompletePopup.getFilteredSuggestions().isEmpty()) {
//                    autoCompletePopup.show(editor);
//                }
//            }
        }

        private void onBackSpaceHandler(KeyEvent e) {
            List<T> items = getSkinnable().getItems();
            if(!items.isEmpty() && getText().isBlank()) {
                items.remove(items.size() - 1);
            }
        }

        private void onTabHandler(KeyEvent e) {
            if(getText().isBlank()) {
                if(e.isShiftDown()) FxUtils.traversePrev(getSkinnable());
                else FxUtils.traverseNext(this);
            }
            e.consume();
        }

        private void onEnterHandler(KeyEvent e) {
            if(!getText().trim().isBlank()) {
                final StringConverter<T> sc = getSkinnable().getConverter();
                final T item = sc.fromString(getText());
                if(item != null) {
                    getSkinnable().getItems().add(item);
                    EditorField.this.clear();
                }
            }
            e.consume();
        }

        public void handleFocus(boolean flag) {
            setFocused(flag);
        }

        @Override
        public void requestFocus() {
            if(getSkinnable() != null) {
                getSkinnable().requestFocus();
            }
        }

        @Override
        public Object queryAccessibleAttribute(AccessibleAttribute attribute, Object... parameters) {
            return switch (attribute) {
                case FOCUS_ITEM -> getSkinnable();
                default -> super.queryAccessibleAttribute(attribute, parameters);
            };
        }

    }

}