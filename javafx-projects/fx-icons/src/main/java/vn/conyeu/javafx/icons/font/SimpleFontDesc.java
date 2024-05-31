package vn.conyeu.javafx.icons.font;

public class SimpleFontDesc implements FontDesc {
    private final String name;
    private final int unicode;

    public SimpleFontDesc(String name, int unicode) {
        this.name = name;
        this.unicode = unicode;
    }

    @Override
    public String getName() {
        return name;
    }

    public int getUnicode() {
        return unicode;
    }

    public SimpleFontDesc cloneWithPrefix(String prefix) {
        return new SimpleFontDesc(prefix + name, unicode);
    }
}