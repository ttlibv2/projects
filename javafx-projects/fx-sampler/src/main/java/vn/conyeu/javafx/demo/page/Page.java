package vn.conyeu.javafx.demo.page;

import javafx.scene.Node;
import javafx.scene.Parent;

import java.net.URI;

public interface Page {
    String getName();
    Parent getView();

    void reset();
    boolean canDisplaySourceCode();
    URI getJavadocUri();
    Node getSnapshotTarget();



}