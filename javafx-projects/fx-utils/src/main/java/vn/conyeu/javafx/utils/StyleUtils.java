package vn.conyeu.javafx.utils;

import javafx.scene.Node;
import javafx.scene.effect.Effect;
import javafx.scene.paint.Paint;
import lombok.extern.slf4j.Slf4j;
import vn.conyeu.commons.utils.Objects;

import java.util.HashMap;
import java.util.Map;

@Slf4j
public final class StyleUtils {

    public static StyleUtils of(final Node node) {
        return new StyleUtils(node);
    }

    //---------------------------
    private final Node node;
    private final Map<String, Object> maps;

    StyleUtils(Node node) {
        this.node = node;
        this.maps = new HashMap<>();
        this.parseStyle();
    }

    private void parseStyle() {
        NodeUtils.parseStyle(maps, node.getStyle());
    }

    public StyleUtils fontSize(String size) {
        return addTag("-fx-font-size", size);
    }

    public StyleUtils fontSize(double sizePx) {
        return addTag("-fx-font-size", sizePx);
    }

    public StyleUtils fontFamily(String fontFamily) {
        return addTag("-fx-font-family", fontFamily);
    }

    public StyleUtils opacity(double opacity) {
        return addTag("-fx-opacity", opacity);
    }

    public StyleUtils addTag(String cssProperty, Object value) {
        maps.put(cssProperty, value);
        return this;
    }

    public void apply() {
        StringBuilder sb = new StringBuilder();
        for(Map.Entry entry:maps.entrySet()) {
            Object value = entry.getValue();
            if(value != null) {
                sb.append(entry.getKey()).append(":");
                sb.append(toCssValue(value)).append(";");
            }
        }

        String style = sb.toString();
        node.setStyle(style);
    }

    private String toCssValue(Object value) {
        if(value == null) return null;
        if(value instanceof String str) return str;
        if(value.getClass().isPrimitive()) return String.valueOf(value);
        if(value.getClass().isEnum()) return value.toString();
        if(value instanceof Paint p) return Colors.toCss(p);
        if(value instanceof Effect eff) return Effects.effectToString(eff);
        if(value instanceof Number num) return String.valueOf(num.intValue());
        else throw Objects.newUnsupported("Can't convert value %s to string", value.getClass().getName());
    }


}