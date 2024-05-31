package vn.conyeu.javafx.demo.event;

import javafx.scene.input.KeyCodeCombination;

public final class HotkeyEvent extends Event {

    private final KeyCodeCombination keys;

    public HotkeyEvent(KeyCodeCombination keys) {
        this.keys = keys;
    }

    public KeyCodeCombination getKeys() {
        return keys;
    }

}