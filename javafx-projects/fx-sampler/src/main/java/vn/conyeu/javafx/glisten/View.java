package vn.conyeu.javafx.glisten;

import javafx.event.EventTarget;

import java.util.Properties;

public interface View extends EventTarget {
    Properties getProperties();
    String getName();
}