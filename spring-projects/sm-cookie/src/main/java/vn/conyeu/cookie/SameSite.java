package vn.conyeu.cookie;

import java.util.stream.Stream;

public enum SameSite {
    Lax, Strict, None;

    /**
     * Return the enum value corresponding to the passed in same-site-flag, using a case insensitive comparison.
     *
     * @param name value for the SameSite Attribute
     * @return enum value for the provided name or null
     */
    public static SameSite of(String name) {
        if(Helper.isBlank(name)) return null;
        return Stream.of(values())
                .filter(s -> s.name().equalsIgnoreCase(name))
                .findFirst().orElse(null);
    }
}