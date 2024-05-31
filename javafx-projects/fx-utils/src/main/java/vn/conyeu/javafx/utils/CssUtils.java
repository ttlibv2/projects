package vn.conyeu.javafx.utils;

import javafx.css.PseudoClass;
import javafx.geometry.Orientation;
import javafx.scene.Node;
import vn.conyeu.commons.utils.Asserts;
import vn.conyeu.commons.utils.Objects;

import java.util.EnumSet;

public final class CssUtils {

    public static final PseudoClass SELECTED = getPseudoClass("selected");
    private static final PseudoClass ACTIVE = getPseudoClass("active");
    private static final PseudoClass IN_ACTIVE = getPseudoClass("in-active");
    private static final PseudoClass ERROR = getPseudoClass("error");
    private static final PseudoClass READONLY = getPseudoClass("readonly");
    private static final PseudoClass VERTICAL = getPseudoClass("vertical");
    private static final PseudoClass HORIZONTAL = getPseudoClass("horizontal");

    public static PseudoClass getPseudoClass(String pseudoClass) {
        return PseudoClass.getPseudoClass(pseudoClass.toLowerCase());
    }

    public static <E extends Enum<E>> void registerPseudoClass(Node node, Class<E> enumClass, E value) {
        if (node == null || enumClass == null) return;
        Iterable<E> iterable = EnumSet.allOf(enumClass);
        for (E enumItem : iterable) {
            String psName = enumClass.getSimpleName().toLowerCase() + "-" + enumItem.name().toLowerCase();
            PseudoClass pseudoClass = getPseudoClass(psName);
            node.pseudoClassStateChanged(pseudoClass, enumItem == value);
        }
    }

    public static void setPseudo(Node node, PseudoClass pseudoClass, boolean value) {
        node.pseudoClassStateChanged(pseudoClass, value);
    }

    public static void setPseudo(Node node, String pseudoClass, boolean value) {
        node.pseudoClassStateChanged(PseudoClass.getPseudoClass(pseudoClass), value);
    }

    public static void setPseudo(Node node, String prefix, Enum enumOld, Enum enumNew) {
        if (enumOld != null) node.pseudoClassStateChanged(getPseudoClass(enumToPS(prefix, enumOld)), false);
        if (enumNew != null) node.pseudoClassStateChanged(getPseudoClass(enumToPS(prefix , enumNew)), true);
    }

    private static String enumToPS(String prefix, Enum enumValue) {
        Asserts.notNull(enumValue, "@Enum");
        String enumCls = enumValue.name().toLowerCase();
        return Objects.isBlank(prefix) ? enumCls : prefix + enumCls;
    }

    public static void setPseudoSelected(Node node, boolean selected) {
        node.pseudoClassStateChanged(SELECTED, selected);
    }

    public static void setPseudoActive(Node node, boolean active) {
        node.pseudoClassStateChanged(ACTIVE, active);
    }

    public static void setPseudoInActive(Node node, boolean inactive) {
        node.pseudoClassStateChanged(IN_ACTIVE, inactive);
    }


    public static void setPseudoVertical(Node node, boolean active) {
        node.pseudoClassStateChanged(VERTICAL, active);
    }

    public static void setPseudoHorizontal(Node node, boolean active) {
        node.pseudoClassStateChanged(HORIZONTAL, active);
    }
    public static void setPsOrientation(Node node, Orientation orientation) {
        node.pseudoClassStateChanged(HORIZONTAL, Orientation.HORIZONTAL == orientation);
        node.pseudoClassStateChanged(VERTICAL, Orientation.VERTICAL == orientation);
    }


    public static void setPseudoVertical(Node node, Orientation orientation) {
        node.pseudoClassStateChanged(VERTICAL, Orientation.VERTICAL == orientation);
    }
    public static void setPseudoHorizontal(Node node, Orientation orientation) {
        node.pseudoClassStateChanged(HORIZONTAL, Orientation.HORIZONTAL == orientation);
    }


    public static void registerErrorPseudoClass(Node node, boolean error) {
        node.pseudoClassStateChanged(ERROR, error);
    }

    public static void setPseudoReadonly(Node node, boolean readonly) {
        node.pseudoClassStateChanged(READONLY, readonly);
    }
}