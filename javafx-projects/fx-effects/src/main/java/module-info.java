module fx.effects {
    requires javafx.graphics;
    requires java.commons;
    requires fx.styleable;
    requires fx.utils;
    requires javafx.controls;

    exports vn.conyeu.javafx.animation.animate;
    exports vn.conyeu.javafx.animation.beans;
    exports vn.conyeu.javafx.animation.ripple;
    exports vn.conyeu.javafx.animation.utils to fx.utils, vn.conyeu.javafx.fxdemo, fx.controls;
}