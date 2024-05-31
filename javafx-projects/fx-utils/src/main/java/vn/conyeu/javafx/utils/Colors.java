package vn.conyeu.javafx.utils;

import javafx.scene.paint.*;
import vn.conyeu.commons.utils.Asserts;
import vn.conyeu.commons.utils.Objects;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class Colors {
    private static final Random random = new Random(System.currentTimeMillis());
    public static final Map<Color, String> COLOR_NAMES = new HashMap<>();
    public static final Map<String, Color> NAMED_COLORS = new HashMap<>();
    private static boolean initDefaultColor = false;

    static {
        initDefaultColor();
    }

    public static Color get(String name) {
        initDefaultColor();
        return NAMED_COLORS.get(name);
    }

    public static String getColorName(Paint color) {
        if (color == null) return null;
        else if (color instanceof Color c) return getColorName(c);
        //else throw Objects.newIllegalArgument("The %s not Color", color.getClass().getName());
        else return null;
    }

    public static String getColorName(Color color) {
        initDefaultColor();
        return COLOR_NAMES.get(color);
    }


    private static Map.Entry<String, Color> registerColor(String name, Color color) {
        COLOR_NAMES.put(color, name);
        NAMED_COLORS.put(name, color);
        return Map.entry(name, color);
    }

    private static void initDefaultColor() {
        if (!initDefaultColor) {
            initDefaultColor = true;
            registerColor("aliceblue", Color.ALICEBLUE);
            registerColor("antiquewhite", Color.ANTIQUEWHITE);
            registerColor("aqua", Color.AQUA);
            registerColor("aquamarine", Color.AQUAMARINE);
            registerColor("azure", Color.AZURE);
            registerColor("beige", Color.BEIGE);
            registerColor("bisque", Color.BISQUE);
            registerColor("black", Color.BLACK);
            registerColor("blanchedalmond", Color.BLANCHEDALMOND);
            registerColor("blue", Color.BLUE);
            registerColor("blueviolet", Color.BLUEVIOLET);
            registerColor("brown", Color.BROWN);
            registerColor("burlywood", Color.BURLYWOOD);
            registerColor("cadetblue", Color.CADETBLUE);
            registerColor("chartreuse", Color.CHARTREUSE);
            registerColor("chocolate", Color.CHOCOLATE);
            registerColor("coral", Color.CORAL);
            registerColor("cornflowerblue", Color.CORNFLOWERBLUE);
            registerColor("cornsilk", Color.CORNSILK);
            registerColor("crimson", Color.CRIMSON);
            registerColor("cyan", Color.CYAN);
            registerColor("darkblue", Color.DARKBLUE);
            registerColor("darkcyan", Color.DARKCYAN);
            registerColor("darkgoldenrod", Color.DARKGOLDENROD);
            registerColor("darkgray", Color.DARKGRAY);
            registerColor("darkgreen", Color.DARKGREEN);
            registerColor("darkgrey", Color.DARKGREY);
            registerColor("darkkhaki", Color.DARKKHAKI);
            registerColor("darkmagenta", Color.DARKMAGENTA);
            registerColor("darkolivegreen", Color.DARKOLIVEGREEN);
            registerColor("darkorange", Color.DARKORANGE);
            registerColor("darkorchid", Color.DARKORCHID);
            registerColor("darkred", Color.DARKRED);
            registerColor("darksalmon", Color.DARKSALMON);
            registerColor("darkseagreen", Color.DARKSEAGREEN);
            registerColor("darkslateblue", Color.DARKSLATEBLUE);
            registerColor("darkslategray", Color.DARKSLATEGRAY);
            registerColor("darkslategrey", Color.DARKSLATEGREY);
            registerColor("darkturquoise", Color.DARKTURQUOISE);
            registerColor("darkviolet", Color.DARKVIOLET);
            registerColor("deeppink", Color.DEEPPINK);
            registerColor("deepskyblue", Color.DEEPSKYBLUE);
            registerColor("dimgray", Color.DIMGRAY);
            registerColor("dimgrey", Color.DIMGREY);
            registerColor("dodgerblue", Color.DODGERBLUE);
            registerColor("firebrick", Color.FIREBRICK);
            registerColor("floralwhite", Color.FLORALWHITE);
            registerColor("forestgreen", Color.FORESTGREEN);
            registerColor("fuchsia", Color.FUCHSIA);
            registerColor("gainsboro", Color.GAINSBORO);
            registerColor("ghostwhite", Color.GHOSTWHITE);
            registerColor("gold", Color.GOLD);
            registerColor("goldenrod", Color.GOLDENROD);
            registerColor("gray", Color.GRAY);
            registerColor("green", Color.GREEN);
            registerColor("greenyellow", Color.GREENYELLOW);
            registerColor("grey", Color.GREY);
            registerColor("honeydew", Color.HONEYDEW);
            registerColor("hotpink", Color.HOTPINK);
            registerColor("indianred", Color.INDIANRED);
            registerColor("indigo", Color.INDIGO);
            registerColor("ivory", Color.IVORY);
            registerColor("khaki", Color.KHAKI);
            registerColor("lavender", Color.LAVENDER);
            registerColor("lavenderblush", Color.LAVENDERBLUSH);
            registerColor("lawngreen", Color.LAWNGREEN);
            registerColor("lemonchiffon", Color.LEMONCHIFFON);
            registerColor("lightblue", Color.LIGHTBLUE);
            registerColor("lightcoral", Color.LIGHTCORAL);
            registerColor("lightcyan", Color.LIGHTCYAN);
            registerColor("lightgoldenrodyellow", Color.LIGHTGOLDENRODYELLOW);
            registerColor("lightgray", Color.LIGHTGRAY);
            registerColor("lightgreen", Color.LIGHTGREEN);
            registerColor("lightgrey", Color.LIGHTGREY);
            registerColor("lightpink", Color.LIGHTPINK);
            registerColor("lightsalmon", Color.LIGHTSALMON);
            registerColor("lightseagreen", Color.LIGHTSEAGREEN);
            registerColor("lightskyblue", Color.LIGHTSKYBLUE);
            registerColor("lightslategray", Color.LIGHTSLATEGRAY);
            registerColor("lightslategrey", Color.LIGHTSLATEGREY);
            registerColor("lightsteelblue", Color.LIGHTSTEELBLUE);
            registerColor("lightyellow", Color.LIGHTYELLOW);
            registerColor("lime", Color.LIME);
            registerColor("limegreen", Color.LIMEGREEN);
            registerColor("linen", Color.LINEN);
            registerColor("magenta", Color.MAGENTA);
            registerColor("maroon", Color.MAROON);
            registerColor("mediumaquamarine", Color.MEDIUMAQUAMARINE);
            registerColor("mediumblue", Color.MEDIUMBLUE);
            registerColor("mediumorchid", Color.MEDIUMORCHID);
            registerColor("mediumpurple", Color.MEDIUMPURPLE);
            registerColor("mediumseagreen", Color.MEDIUMSEAGREEN);
            registerColor("mediumslateblue", Color.MEDIUMSLATEBLUE);
            registerColor("mediumspringgreen", Color.MEDIUMSPRINGGREEN);
            registerColor("mediumturquoise", Color.MEDIUMTURQUOISE);
            registerColor("mediumvioletred", Color.MEDIUMVIOLETRED);
            registerColor("midnightblue", Color.MIDNIGHTBLUE);
            registerColor("mintcream", Color.MINTCREAM);
            registerColor("mistyrose", Color.MISTYROSE);
            registerColor("moccasin", Color.MOCCASIN);
            registerColor("navajowhite", Color.NAVAJOWHITE);
            registerColor("navy", Color.NAVY);
            registerColor("oldlace", Color.OLDLACE);
            registerColor("olive", Color.OLIVE);
            registerColor("olivedrab", Color.OLIVEDRAB);
            registerColor("orange", Color.ORANGE);
            registerColor("orangered", Color.ORANGERED);
            registerColor("orchid", Color.ORCHID);
            registerColor("palegoldenrod", Color.PALEGOLDENROD);
            registerColor("palegreen", Color.PALEGREEN);
            registerColor("paleturquoise", Color.PALETURQUOISE);
            registerColor("palevioletred", Color.PALEVIOLETRED);
            registerColor("papayawhip", Color.PAPAYAWHIP);
            registerColor("peachpuff", Color.PEACHPUFF);
            registerColor("peru", Color.PERU);
            registerColor("pink", Color.PINK);
            registerColor("plum", Color.PLUM);
            registerColor("powderblue", Color.POWDERBLUE);
            registerColor("purple", Color.PURPLE);
            registerColor("red", Color.RED);
            registerColor("rosybrown", Color.ROSYBROWN);
            registerColor("royalblue", Color.ROYALBLUE);
            registerColor("saddlebrown", Color.SADDLEBROWN);
            registerColor("salmon", Color.SALMON);
            registerColor("sandybrown", Color.SANDYBROWN);
            registerColor("seagreen", Color.SEAGREEN);
            registerColor("seashell", Color.SEASHELL);
            registerColor("sienna", Color.SIENNA);
            registerColor("silver", Color.SILVER);
            registerColor("skyblue", Color.SKYBLUE);
            registerColor("slateblue", Color.SLATEBLUE);
            registerColor("slategray", Color.SLATEGRAY);
            registerColor("slategrey", Color.SLATEGREY);
            registerColor("snow", Color.SNOW);
            registerColor("springgreen", Color.SPRINGGREEN);
            registerColor("steelblue", Color.STEELBLUE);
            registerColor("tan", Color.TAN);
            registerColor("teal", Color.TEAL);
            registerColor("thistle", Color.THISTLE);
            registerColor("tomato", Color.TOMATO);
            registerColor("transparent", Color.TRANSPARENT);
            registerColor("turquoise", Color.TURQUOISE);
            registerColor("violet", Color.VIOLET);
            registerColor("wheat", Color.WHEAT);
            registerColor("white", Color.WHITE);
            registerColor("whitesmoke", Color.WHITESMOKE);
            registerColor("yellow", Color.YELLOW);
            registerColor("yellowgreen", Color.YELLOWGREEN);
        }
    }

    public static Color clone(Color color, double opacity) {
        return Color.color(color.getRed(), color.getGreen(), color.getBlue(), opacity);
    }

    /**
     * @return a new {@link Color} which has the same RGB components of the given one but the opacity
     * is set to the desired value (clamped between 0.0 and 1.0)
     */
    public static Color atAlpha(Color color, double alpha) {
        double cAlpha = FxUtils.clamp(alpha, 0.0, 1.0);
        return Color.color(
                color.getRed(),
                color.getGreen(),
                color.getBlue(),
                cAlpha
        );
    }

    /**
     * Converts a JavaFX Paint object to the right CSS string.
     * <p>
     * Supports: {@link Color}, {@link LinearGradient}, {@link RadialGradient}.
     */
    public static String toCss(Paint paint) {
        if (paint == null) return "";

        if (paint instanceof LinearGradient gradient) {
            return linearGradientToString(gradient);
        }

        if (paint instanceof RadialGradient gradient) {
            return radialGradientToString(gradient);
        }

        if(paint instanceof Color color) {
            return rgba(color);
        }

        throw new UnsupportedOperationException();
    }

    /**
     * Converts a JavaFX's {@code Color} to CSS corresponding rgb function.
     *
     * @return the rgb function as a string
     */
    public static String rgb(Color color) {
        if (color == null) return "";
        return String.format("rgb(%d, %d, %d)",
                (int) (255 * color.getRed()),
                (int) (255 * color.getGreen()),
                (int) (255 * color.getBlue()));
    }

    /**
     * Converts a JavaFX's {@code Color} to CSS corresponding rgba function.
     *
     * @return the rgba function as a string
     */
    public static String rgba(Color color) {
        if (color == null) return "";
        return String.format("rgba(%d, %d, %d, %s)",
                (int) (255 * color.getRed()),
                (int) (255 * color.getGreen()),
                (int) (255 * color.getBlue()),
                color.getOpacity());
    }


    /**
     * Generates a random {@link  Color} using {@link Random}.
     */
    public static Color getRandomColor() {
        return Color.rgb(
                random.nextInt(256),
                random.nextInt(256),
                random.nextInt(256)
        );
    }

    /**
     * Generates a random {@link  Color} using {@link Random} and the given opacity.
     */
    public static Color getRandomColor(double opacity) {
        return Color.rgb(
                random.nextInt(256),
                random.nextInt(256),
                random.nextInt(256),
                opacity
        );
    }

    /**
     * Util method to convert {@link LinearGradient} to a CSS string.
     * <p></p>
     * This is partly a copy of {@link LinearGradient#toString()} but {@code Stops} are correctly converted
     * for CSS.
     *
     * @param gradient the linear gradient to convert
     * @see Stop
     */
    public static String linearGradientToString(LinearGradient gradient) {
        if (gradient == null) return "";
        final StringBuilder s = new StringBuilder("linear-gradient(from ")
                .append(lengthToString(gradient.getStartX(), gradient.isProportional()))
                .append(" ").append(lengthToString(gradient.getStartY(), gradient.isProportional()))
                .append(" to ").append(lengthToString(gradient.getEndX(), gradient.isProportional()))
                .append(" ").append(lengthToString(gradient.getEndY(), gradient.isProportional()))
                .append(", ");

        switch (gradient.getCycleMethod()) {
            case REFLECT -> s.append("reflect").append(", ");
            case REPEAT -> s.append("repeat").append(", ");
        }

        for (Stop stop : gradient.getStops()) {
            s.append(stopToString(stop)).append(", ");
        }

        s.delete(s.length() - 2, s.length());
        s.append(")");

        return s.toString();
    }

    /**
     * Util method to convert {@link RadialGradient} to a CSS string.
     * <p></p>
     * This is partly a copy of {@link RadialGradient#toString()} but {@code Stops} are correctly converted
     * for CSS.
     *
     * @param gradient the radial gradient to convert
     * @see Stop
     */
    public static String radialGradientToString(RadialGradient gradient) {
        if (gradient == null) return "";
        final StringBuilder s = new StringBuilder("radial-gradient(focus-angle ").append(gradient.getFocusAngle())
                .append("deg, focus-distance ").append(gradient.getFocusDistance() * 100)
                .append("% , center ").append(lengthToString(gradient.getCenterX(), gradient.isProportional()))
                .append(" ").append(lengthToString(gradient.getCenterY(), gradient.isProportional()))
                .append(", radius ").append(lengthToString(gradient.getRadius(), gradient.isProportional()))
                .append(", ");

        switch (gradient.getCycleMethod()) {
            case REFLECT -> s.append("reflect").append(", ");
            case REPEAT -> s.append("repeat").append(", ");
        }

        for (Stop stop : gradient.getStops()) {
            s.append(stopToString(stop)).append(", ");
        }

        s.delete(s.length() - 2, s.length());
        s.append(")");

        return s.toString();

    }


    /**
     * Properly converts a {@link Stop} to string. Partly copied from
     * {@link Stop#toString()} but the color is converted using {@link #rgba(Color)}.
     */
    public static String stopToString(Stop stop) {
        return rgba(stop.getColor()) + " " + stop.getOffset() * 100 + "%";
    }

    /**
     * Converts the given color to a String
     * in hexadecimal format.
     */
    public static String toWeb(Color color) {
        if (color == null) return "";
        String cs = color.toString();
        return "#" + cs.substring(2, cs.length() - 2);
    }

    /**
     * Converts the given color to a String
     * in hexadecimal format, also includes the color's opacity.
     */
    public static String toWebAlpha(Color color) {
        if (color == null) return "";
        return "#" + color.toString().substring(2);
    }

    /**
     * Converts the given color to a String
     * in HSL format.
     */
    public static String toHSL(Color color) {
        if (color == null) return "";

        double[] rgb = colorToArray(color);
        double r = rgb[0];
        double g = rgb[1];
        double b = rgb[2];

        double min = Math.min(r, Math.min(g, b));
        double max = Math.max(r, Math.max(g, b));

        // Hue
        double h = 0;
        if (max == min) {
            h = 0;
        } else if (max == r) {
            h = ((60 * (g - b) / (max - min)) + 360) % 360;
        } else if (max == g) {
            h = (60 * (b - r) / (max - min)) + 120;
        } else if (max == b) {
            h = (60 * (r - g) / (max - min)) + 240;
        }

        // Luminance
        double l = (max + min) / 2;

        // Saturation
        double s;
        if (max == min) {
            s = 0;
        } else if (l <= 0.5f) {
            s = (max - min) / (max + min);
        } else {
            s = (max - min) / (2 - max - min);
        }

        return "hsl(" + Math.round(h) + "deg, " + Math.round(s * 100) + "%, " + Math.round(l * 100) + "%)";
    }

    /**
     * Converts the given color to a String
     * in HSB format.
     */
    public static String toHSB(Color color) {
        if (color == null) return "";

        double[] rgb = colorToArray(color);
        double r = rgb[0];
        double g = rgb[1];
        double b = rgb[2];

        double max = Math.max(Math.max(r, g), b);
        double min = Math.min(Math.min(r, g), b);
        double delta = max - min;

        double brightness;
        double saturation;
        double hue;

        // Brightness
        brightness = max;

        // Saturation
        if (max != 0) {
            saturation = delta / max;
        } else {
            saturation = 0;
        }

        // Hue
        if (saturation == 0) {
            hue = 0;
        } else {
            double tmpR = (max - r) / delta;
            double tmpG = (max - g) / delta;
            double tmpB = (max - b) / delta;
            if (r == max)
                hue = tmpB - tmpG;
            else if (g == max)
                hue = 2.0 + tmpR - tmpB;
            else
                hue = 4.0 + tmpG - tmpR;
            hue = hue / 6.0;
            if (hue < 0)
                hue = hue + 1.0;
        }
        hue *= 360;

        return "hsb(" + Math.round(hue) + "deg, " + Math.round(saturation * 100) + "%, " + Math.round(brightness * 100) + "%)";
    }

    private static double[] colorToArray(Color color) {
        return new double[]{
                color.getRed(),
                color.getGreen(),
                color.getBlue()
        };
    }

    private static String lengthToString(double value, boolean proportional) {
        if (proportional) {
            return (value * 100) + "%";
        } else {
            return value + "px";
        }
    }


    public static Paint parse(String value) {
        Asserts.notBlank(value, "@String");
        try {
            return Paint.valueOf(value);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    /**
     * Converts a type Color to a hex string
     * in the format "#RRGGBB"
     */
    public static String colorToHex(Color color) {
        int r = (int)Math.round(color.getRed() * 255.0);
        int g = (int)Math.round(color.getGreen() * 255.0);
        int b = (int)Math.round(color.getBlue() * 255.0);
        return String.format("#%02x%02x%02x" , r, g, b);
    }



}