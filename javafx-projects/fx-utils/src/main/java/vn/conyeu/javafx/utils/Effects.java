package vn.conyeu.javafx.utils;

import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Effect;

public final class Effects {

    public static String effectToString(Effect eff) {
        if(eff == null) return null;
        if(eff instanceof DropShadow ds) return dropShadowToString(ds);
        else return eff.toString();
    }

    public static String dropShadowToString(DropShadow shadow) {
        return "dropshadow(" + shadow.getBlurType().name() + "," +
                Colors.rgba(shadow.getColor()) + "," +
                shadow.getRadius() + "," +
                shadow.getSpread() + "," +
                shadow.getOffsetX() + "," +
                shadow.getOffsetY() + ")";
    }
}