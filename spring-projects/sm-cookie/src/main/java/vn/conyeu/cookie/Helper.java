package vn.conyeu.cookie;

import java.util.BitSet;
import java.util.List;
import java.util.Objects;

final class Helper {
    static final BitSet VALID_COOKIE_NAME_OCTETS = validCookieNameOctets();
    static final BitSet VALID_COOKIE_VALUE_OCTETS = validCookieValueOctets();
    static final BitSet VALID_COOKIE_ATTRIBUTE_VALUE_OCTETS = validCookieAttributeValueOctets();
    static final List<Character> SEPARATORS;// = List.of('(', ')', '<', '>', '@', ',', ';', ':', '\\', '"', '/', '[', ']', '?', '=', '{', '}', ' ', '\t' );
    public static final String PATH = "Path";
    public static final String EXPIRES = "Expires";
    public static final String MAX_AGE = "Max-Age";
    public static final String DOMAIN = "Domain";
    public static final String SECURE = "Secure";
    public static final String HTTPONLY = "HTTPOnly";
    public static final String SAME_SITE = "SameSite";
    static final byte SP = 32;
    static final char SP_CHAR = (char) SP;

    static {
        SEPARATORS = List.of('(', ')', '<', '>', '@', ',', ';', ':', '\\', '"', '/', '[', ']', '?', '=', '{', '}', ' ', '\t');
    }

    // token = 1*<any CHAR except CTLs or separators>
    // separators = "(" | ")" | "<" | ">" | "@"
    // | "," | ";" | ":" | "\" | <">
    // | "/" | "[" | "]" | "?" | "="
    // | "{" | "}" | SP | HT
    public static BitSet validCookieNameOctets() {
        final List<Character> SEPARATORS = List.of('(', ')', '<', '>', '@', ',', ';', ':', '\\', '"',
                '/', '[', ']', '?', '=', '{', '}', ' ', '\t');
        BitSet bits = new BitSet();
        for (int i = 32; i < 127; i++) {
            bits.set(i);
        }
        for (Character separator : SEPARATORS) {
            bits.set(separator, false);
        }
        return bits;
    }

    // cookie-octet = %x21 / %x23-2B / %x2D-3A / %x3C-5B / %x5D-7E
    // US-ASCII characters excluding CTLs, whitespace, DQUOTE, comma, semicolon, and backslash
    public static BitSet validCookieValueOctets() {
        BitSet bits = new BitSet();
        bits.set(0x21);
        for (int i = 0x23; i <= 0x2B; i++) {
            bits.set(i);
        }
        for (int i = 0x2D; i <= 0x3A; i++) {
            bits.set(i);
        }
        for (int i = 0x3C; i <= 0x5B; i++) {
            bits.set(i);
        }
        for (int i = 0x5D; i <= 0x7E; i++) {
            bits.set(i);
        }
        return bits;
    }

    // path-value        = <any CHAR except CTLs or ";">
    public static BitSet validCookieAttributeValueOctets() {
        BitSet bits = new BitSet();
        for (int i = 32; i < 127; i++) {
            bits.set(i);
        }
        bits.set(';', false);
        return bits;
    }

    public static int firstInvalidOctet(CharSequence cs, BitSet bits) {
        for (int i = 0; i < cs.length(); i++) {
            char c = cs.charAt(i);
            if (!bits.get(c)) {
                return i;
            }
        }
        return -1;
    }

    public static String validateAttributeValue(String name, String value) {
        if (isBlank(value)) return null;
        int i = firstInvalidOctet(value, VALID_COOKIE_ATTRIBUTE_VALUE_OCTETS);
        if (i != -1) {
            throw new IllegalArgumentException(name + " contains the prohibited characters: " + value.charAt(i));
        }
        return value;
    }

    public static boolean isBlank(CharSequence string) {
        return string == null || string.isEmpty();
    }


    /**
     * Checks that the given argument is not null. If it is, throws {@link NullPointerException}.
     * Otherwise, returns the argument.
     */
    public static <T> T checkNotNull(T arg, String text) {
        if (arg == null) throw new NullPointerException(text);
        else return arg;
    }


    /**
     * Trims the given argument and checks whether it is neither null nor empty.
     * If it is, throws {@link NullPointerException} or {@link IllegalArgumentException}.
     * Otherwise, returns the trimmed argument.
     *
     * @param value to trim and check.
     * @param name  of the parameter.
     * @return the trimmed (not the original) value.
     * @throws NullPointerException     if value is null.
     * @throws IllegalArgumentException if the trimmed value is empty.
     */
    public static String checkNonEmptyAfterTrim(final String value, final String name) {
        String trimmed = checkNotNull(value, name).trim();
        return checkNonEmpty(trimmed, name);
    }

    /**
     * Checks that the given argument is neither null nor empty.
     * If it is, throws {@link NullPointerException} or {@link IllegalArgumentException}.
     * Otherwise, returns the argument.
     */
    public static String checkNonEmpty(final String value, final String name) {
        if (checkNotNull(value, name).isEmpty()) {
            throw new IllegalArgumentException("Param '" + name + "' must not be empty");
        }
        return value;
    }

    /**
     * Compares this {@code String} to another {@code String}, ignoring case
     * considerations.  Two strings are considered equal ignoring case if they
     * are of the same length and corresponding Unicode code points in the two
     * strings are equal ignoring case.
     */
    public static boolean equalsIgnoreCase(String a, String b) {
        return Objects.equals(a, b) || (a != null && a.equalsIgnoreCase(b));
    }

    public static CharSequence unwrapValue(CharSequence cs) {
        final int len = cs.length();
        if (len > 0 && cs.charAt(0) == '"') {
            if (len >= 2 && cs.charAt(len - 1) == '"') {
                // properly balanced
                return len == 2 ? "" : cs.subSequence(1, len - 1);
            } else {
                return null;
            }
        }
        return cs;
    }

    public static int firstInvalidCookieNameOctet(CharSequence cs) {
        return firstInvalidOctet(cs, VALID_COOKIE_NAME_OCTETS);
    }

    public static int firstInvalidCookieValueOctet(CharSequence cs) {
        return firstInvalidOctet(cs, VALID_COOKIE_VALUE_OCTETS);
    }


    /**
     * If the character is uppercase - converts the character to lowercase,
     * otherwise returns the character as it is. Only for ASCII characters.
     *
     * @return lowercase ASCII character equivalent
     */
    public static char toLowerCaseAsciiString(char c) {
        return isUpperCaseAsciiString(c) ? (char) (c + 32) : c;
    }

    public static boolean isUpperCaseAsciiString(char value) {
        return value >= 'A' && value <= 'Z';
    }

    public static String stripTrailingSeparator(StringBuilder buf) {
        if (buf.length() > 0) {
            buf.setLength(buf.length() - 2);
        }
        return buf.toString();
    }

    /**
     * @param buf a buffer where some cookies were maybe encoded
     * @return the buffer String without the trailing separator, or null if no cookie was appended.
     */
    public static String stripTrailingSeparatorOrNull(StringBuilder buf) {
        return buf.length() == 0 ? null : stripTrailingSeparator(buf);
    }

    public static void addQuoted(StringBuilder sb, String name, String val) {
        if (val == null) val = "";
        sb.append(name).append('=').append('"')
                .append(val).append('"')
                .append(';').append(SP_CHAR);
    }

    public static void add(StringBuilder sb, String name, String val) {
        sb.append(name);sb.append('=');sb.append(val);
        sb.append(';');sb.append(SP_CHAR);
    }

    public static void add(StringBuilder sb, String name, long val) {
        sb.append(name);sb.append('=');sb.append(val);sb.append(';');sb.append(SP_CHAR);
    }

    public static void add(StringBuilder sb, String name) {
        sb.append(name);sb.append(';');sb.append(SP_CHAR);
    }

    public static void validateCookieWithSameSiteIsStrict(String name, String value) {
        int pos;

        if ((pos = firstInvalidCookieNameOctet(name)) >= 0) {
            throw new IllegalArgumentException("Cookie name contains an invalid char: " + name.charAt(pos));
        }

        CharSequence unwrappedValue = unwrapValue(value);
        if (unwrappedValue == null) {
            throw new IllegalArgumentException("Cookie value wrapping quotes are not balanced: " + value);
        }

        if ((pos = firstInvalidCookieValueOctet(unwrappedValue)) >= 0) {
            throw new IllegalArgumentException("Cookie value contains an invalid char: " +
                    unwrappedValue.charAt(pos));
        }
    }
}