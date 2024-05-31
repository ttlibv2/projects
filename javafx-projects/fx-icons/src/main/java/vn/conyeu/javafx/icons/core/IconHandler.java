package vn.conyeu.javafx.icons.core;

import javafx.scene.Node;

public interface IconHandler<I extends IconDesc> {
    boolean supports(String name);
    I resolve(String description);
    String getFontFamily();
    Node createIcon(String description);

}