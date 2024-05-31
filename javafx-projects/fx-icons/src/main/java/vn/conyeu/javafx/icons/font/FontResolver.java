package vn.conyeu.javafx.icons.font;

import javafx.scene.text.Font;
import vn.conyeu.commons.function.Callable;
import vn.conyeu.javafx.icons.core.IconResolve;

import java.util.*;
import java.util.function.BiConsumer;

public class FontResolver extends IconResolve<FontDesc, FontHandler> {
    public static final Map<String, Font> FONTS = new LinkedHashMap<>();
    private static final FontResolver INSTANCE = new FontResolver();
    public static FontResolver getInstance() {return INSTANCE;}
    private FontResolver() {super(FontHandler.class);}

    @Override
    protected final void applyHandlerClasspath(FontHandler handler) {
        String fontResource = handler.getFontResource().toExternalForm();
        handler.setLazyFont(() -> FONTS.computeIfAbsent(fontResource, p -> Font.loadFont(p, 16)));
    }


}