package vn.conyeu.javafx.icons.font;

import javafx.scene.Node;
import javafx.scene.text.Font;
import vn.conyeu.commons.function.Callable;
import vn.conyeu.javafx.icons.core.IconHandler;

import java.io.InputStream;
import java.net.URL;

public abstract class FontHandler implements IconHandler<FontDesc> {
    private boolean hasLazyLoad = false;
    private Callable<Font> lazyFont;
    private Object font;

    public abstract boolean supports(String description);

    public abstract FontDesc resolve(String description);

    public abstract URL getFontResource();

    public abstract InputStream getFontResourceAsStream();

    public abstract String getFontFamily();

    public Object getFont() {
        return font;
    }

    public void setFont(Object font) {
        this.font = font;
    }

    public void setLazyFont(Callable<Font> lazyFont) {
        this.lazyFont = lazyFont;
    }

    @Override
    public Node createIcon(String description) {
        return new FontIcon(this, description);
    }

    protected void asyncInitializeData() {
        if (!hasLazyLoad && lazyFont != null) {
            lazyFont.call();
            hasLazyLoad = true;
        }
    }


}