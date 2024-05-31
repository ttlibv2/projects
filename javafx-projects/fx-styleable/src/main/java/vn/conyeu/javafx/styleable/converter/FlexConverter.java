package vn.conyeu.javafx.styleable.converter;

import javafx.css.ParsedValue;
import javafx.css.StyleConverter;
import javafx.scene.text.Font;
import vn.conyeu.commons.utils.Objects;
import vn.conyeu.javafx.styleable.simple.Flex;

public class FlexConverter extends StyleConverter<String, Flex> {

    private static class Holder {
        static final FlexConverter INSTANCE = new FlexConverter();
    }

    // lazy, thread-safe instatiation

    /**
     * Gets the {@code FlexConverter} instance.
     *
     * @return the {@code FlexConverter} instance
     */
    public static FlexConverter getInstance() {
        return Holder.INSTANCE;
    }

    private FlexConverter() {
    }

    @Override
    public Flex convert(ParsedValue<String, Flex> value, Font font) {

        String text = value.getValue();
        if(text == null || text.isBlank()) {
            return Flex.empty();
        }

        String[] segments = value.getValue().split(" ");
        Integer grow = 1, shrink = 1;
        String basic = "auto";

        // grow || basic
        if(segments.length == 1) {
            String s0 = segments[0];
            boolean isNumber = isNumber(s0);
            grow = isNumber ? parseInt(s0) : 1;
            basic = isNumber ? "0%" : s0;
        }

        // grow shrink || grow basic || basic grow
        else if(segments.length == 2) {
            String s0 = segments[0], s1 = segments[1];

            boolean isNum0 = isNumber(s0), isNum1 = isNumber(s1);

            // grow shrink
            if(isNum0 && isNum1) {
                grow = parseInt(s0);
                shrink = parseInt(s1);
            }

            // grow basic
            else if(isNum0) {
                grow = parseInt(s0);
                basic = s1;
            }

            // basic grow
            else if(isNum1) {
                grow = parseInt(s1);
                basic = s0;
            }
            else throw invalidFlex(text);
        }

        // grow shrink basic || basic grow shrink
        else if(segments.length == 3) {
            String s0 = segments[0], s1 = segments[1], s2 = segments[2];
            boolean isNum0 = isNumber(s0), isNum1 = isNumber(s1), isNum2 = isNumber(s2);

            // [s0] == basic --> typeof string
            if(!isNum0) {
                if(isNum1 && isNum2) {
                    grow = parseInt(s1);
                    shrink = parseInt(s2);
                }
                else throw invalidFlex(text);
            }

            // [s2] == basic --> typeof string
            else if(!isNum2) {
                if(isNum1) {
                    grow = parseInt(s0);
                    shrink = parseInt(s1);
                }
                else throw invalidFlex(text);
            }
        }
        else throw invalidFlex(text);

        return new Flex(
                Math.max(0, grow),
                Math.max(0, shrink),
                basic
        );

    }

    boolean isNumber(String segment) {
        try{ Integer.parseInt(segment); return true;}
        catch (NumberFormatException exp) { return false; }
    }

    Integer parseInt(String segment) {
        try{ return Integer.parseInt(segment); }
        catch (NumberFormatException exp) {
          throw new NumberFormatException("The segment "+segment+" not number");
        }
    }

    IllegalArgumentException invalidFlex(String text) {
        String msg = "The css value '%s' invalid format '-fx-flex'";
        return Objects.newIllegal(msg, text);
    }


}