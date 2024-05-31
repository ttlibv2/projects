module vn.conyeu.javafx.fxdemo {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;
    requires fx.icons;
    requires fx.utils;
    requires fx.effects;
    requires fx.styleable;
   // requires awesome.font.icon;
    requires static lombok;
    requires java.commons;
    requires jakarta.validation;
    requires mfx.resources;
    requires mfx.core;

    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires org.scenicview.scenicview;
    requires mfx.effects;
    requires fr.brouillard.oss.cssfx;
    requires java.prefs;
    requires java.desktop;
    requires org.slf4j;
    requires fx.theme;
    requires fx.controls;
    requires MaterialFX;
    requires VirtualizedFX;
    requires org.hibernate.validator;
    requires jakarta.el;
    requires el.ri;

    exports vn.conyeu.javafx.sampler to  javafx.graphics;
    exports vn.conyeu.javafx.sampler.layout to javafx.graphics;


}