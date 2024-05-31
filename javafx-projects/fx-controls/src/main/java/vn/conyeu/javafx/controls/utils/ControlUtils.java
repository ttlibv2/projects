package vn.conyeu.javafx.controls.utils;

import javafx.scene.Node;
import vn.conyeu.commons.utils.Objects;
import vn.conyeu.javafx.icons.font.FontIcon;
import vn.conyeu.javafx.icons.svg.SvgIcon;

public final class ControlUtils {

    public static Node resolveIcon(String icon) {
        if(Objects.isBlank(icon)) return null;
        else if(icon.startsWith("svg::")) return new SvgIcon(icon.substring(5));
        else if(icon.endsWith(".svg")) return new SvgIcon(icon.substring(0, icon.length()-4));
        else if(icon.startsWith("font::")) return new FontIcon(icon.substring(6));
        else return new FontIcon(icon);
    }
}