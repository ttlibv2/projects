package vn.conyeu.javafx.icons.font;

import java.util.EnumSet;

public class EnumIconHandler<E extends Enum<E> & FontDesc> extends LazyFontHandler {

    public EnumIconHandler(Class<E> enumClass, String prefix, String family, String version, String orgName, String fileName) {
        super(prefix, family, version, orgName, fileName, self -> EnumSet.allOf(enumClass).iterator());
    }


}