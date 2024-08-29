package vn.conyeu.google.core;

import com.google.api.services.sheets.v4.model.Color;
import com.google.api.services.sheets.v4.model.SpreadsheetProperties;
import vn.conyeu.google.sheetdb.builder.ConsumerReturn;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public final class Utils {

    public static <T> void setIfNotNull(Supplier<T> object,  Consumer<T> consumerSet) {
        T value = object.get();
        if(value != null) consumerSet.accept(value);;
    }

    public static <T> T setIfNull(Supplier<T> object, Supplier<T> supplierNew, Consumer<T> consumerSet) {
        T value = object.get();
        if(value == null) {
            value = supplierNew.get();
            consumerSet.accept(value);;
        }
        return value;
    }

    public static <B, T> B accepBuilder(Supplier<T> object, Supplier<T> supplierNew, Consumer<T> consumerSet, B builder, Function<T, B> supplierBuilder, ConsumerReturn<B> consumerBuilder) {
        T value = setIfNull(object, supplierNew, consumerSet);
        if(builder == null) builder = supplierBuilder.apply(value);
        return consumerBuilder.accept(builder);
    }

    public static Color rgbColor(String hexColor) {
        String color = hexColor.substring(1);
        int len = color.length();

        try {
            int r, g, b, a = 0;

            if (len == 3) {
                r = Integer.parseInt(color.substring(0, 1), 16);
                g = Integer.parseInt(color.substring(1, 2), 16);
                b = Integer.parseInt(color.substring(2, 3), 16);
                return newColor(r, g, b, a);
            }
            else if (len == 4) {
                r = Integer.parseInt(color.substring(0, 1), 16);
                g = Integer.parseInt(color.substring(1, 2), 16);
                b = Integer.parseInt(color.substring(2, 3), 16);
                a = Integer.parseInt(color.substring(3, 4), 16);
                return newColor(r / 15f, g / 15f, b / 15f, a / 15f);
            } //
            else if (len == 6) {
                r = Integer.parseInt(color.substring(0, 2), 16);
                g = Integer.parseInt(color.substring(2, 4), 16);
                b = Integer.parseInt(color.substring(4, 6), 16);
                return newColor(r,g,b,1);
            } //
            else if (len == 8) {
                r = Integer.parseInt(color.substring(0, 2), 16);
                g = Integer.parseInt(color.substring(2, 4), 16);
                b = Integer.parseInt(color.substring(4, 6), 16);
                a = Integer.parseInt(color.substring(6, 8), 16);
                return newColor(r,g,b,a/255f);
            }
        } catch (NumberFormatException ignored) {}

        throw new IllegalArgumentException("Invalid color specification");
    }

    public static void updateRgbColor(Color c, String colorString) {
        Color cn = rgbColor(colorString);
        c.setRed(cn.getRed()).setGreen(cn.getGreen()).setBlue(cn.getBlue()).setAlpha(cn.getAlpha());
    }

    public static Color newColor(float r, float g, float b, float a) {
        return new Color().setRed(r).setGreen(g).setBlue(b).setAlpha(a);
    }


    public static <T> T getIfNull(T object, Supplier<T> aNew) {
        return object == null ? aNew.get() : object;
    }
}