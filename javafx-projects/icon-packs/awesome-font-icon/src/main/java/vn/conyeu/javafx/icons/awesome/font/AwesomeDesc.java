package vn.conyeu.javafx.icons.awesome.font;

import vn.conyeu.commons.utils.Asserts;
import vn.conyeu.javafx.icons.font.SimpleFontDesc;

public class AwesomeDesc extends SimpleFontDesc {

    public AwesomeDesc(String name, int unicode) {
        super(name, unicode);
    }

    public static AwesomeDesc parse(String prefix, String text) {
        Asserts.notBlank(text, "@text");

        String[] parts = text.split(":");
        Asserts.isTrue(parts.length == 2, "@text");

        return new AwesomeDesc(prefix + parts[0], Integer.parseInt(parts[1]));
    }
}