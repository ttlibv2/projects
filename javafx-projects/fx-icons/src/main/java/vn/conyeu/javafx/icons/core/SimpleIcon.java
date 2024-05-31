package vn.conyeu.javafx.icons.core;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

public interface SimpleIcon<N extends Node, I extends IconDesc, H extends IconHandler<I>> {
    double DEFAULT_SIZE = 12;
    Color DEFAULT_COLOR = Color.BLACK;

    ObjectProperty<I> iconDescProperty();
    DoubleProperty iconSizeProperty();
    ObjectProperty<Paint> iconColorProperty();
    ObjectProperty<H> iconHandlerProperty();

    void setIconLiteral(String literal);

    abstract class SimpleIconDelegate<N extends Node & SimpleIcon<N, I, H>, I extends IconDesc, H extends IconHandler<I>> {
        protected final N simple;

        public SimpleIconDelegate(N simpleIcon) {
            this.simple = simpleIcon;
            this.initializeListener();
        }

        public void initializeListener() {
            simple.iconHandlerProperty().addListener((obs, ov, handler) -> applyIconHandler(handler));
            simple.iconDescProperty().addListener((obs, ov, desc) -> applyIconDesc(desc));
            simple.iconSizeProperty().addListener((obs, ov, size) -> applyIconSize(size));
            simple.iconColorProperty().addListener((obs, ov, color) -> applyIconColor(color));
        }


        protected abstract void applyIconSize(Number size);
        protected abstract void applyIconDesc(I desc);
        protected abstract void applyIconColor(Paint color);
        protected abstract void applyIconHandler(H handler);


    }
}