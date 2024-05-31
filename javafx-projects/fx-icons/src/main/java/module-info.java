import vn.conyeu.javafx.icons.font.FontHandler;
import vn.conyeu.javafx.icons.svg.SvgHandler;

module fx.icons {
    exports vn.conyeu.javafx.icons.core;
    exports vn.conyeu.javafx.icons.font;
    exports vn.conyeu.javafx.icons.svg;

    uses FontHandler;
    uses SvgHandler;

    requires javafx.controls;
    requires fx.styleable;
    requires java.commons;
    requires org.slf4j;
    requires static lombok;
    requires fx.utils;

}