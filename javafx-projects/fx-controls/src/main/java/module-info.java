module fx.controls {
    requires javafx.controls;
    requires javafx.web;
    requires java.commons;
    requires fx.styleable;
    requires fx.utils;
    requires fx.effects;
    requires org.slf4j;
    requires static lombok;
    requires fx.icons;
    requires jakarta.validation;

    opens vn.conyeu.javafx.controls.behavior to javafx.web;


    exports vn.conyeu.javafx.controls;
    exports vn.conyeu.javafx.controls.behavior;
    exports vn.conyeu.javafx.controls.virtual;
    exports vn.conyeu.javafx.controls.skin;
    exports vn.conyeu.javafx.controls.enums;
    exports vn.conyeu.javafx.controls.flex;
    exports vn.conyeu.javafx.controls.simple;
}