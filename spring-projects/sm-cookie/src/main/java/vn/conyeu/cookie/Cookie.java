package vn.conyeu.cookie;

import java.util.Objects;

public class Cookie implements Comparable<Cookie> {

    /**
     * Constant for undefined MaxAge attribute value.
     */
    final static long UNDEFINED_MAX_AGE = Long.MIN_VALUE;

    private final String name;
    private String value;
    private boolean wrap;
    private String domain;
    private String path;
    private long maxAge;
    private boolean secure;
    private boolean httpOnly;
    private SameSite sameSite;

    /**
     * Creates a new cookie with the specified name and value.
     */
    public Cookie(String name, String value) {
        this.name = Helper.checkNonEmptyAfterTrim(name, "@name");
        this.setValue(value);
    }

    /**
     * Sets the value of this {@link Cookie}.
     *
     * @param value The value to set
     */
    public Cookie setValue(String value) {
        this.value = Helper.checkNotNull(value, "@value");
        return this;
    }

    /**
     * Returns the value of this {@link Cookie}.
     *
     * @return The value of this {@link Cookie}
     */
    public String value() {
        return value;
    }

    /**
     * Returns the name of this {@link Cookie}.
     *
     * @return The name of this {@link Cookie}
     */
    public String name() {
        return name;
    }

    /**
     * Returns true if the raw value of this {@link Cookie},
     * was wrapped with double quotes in original Set-Cookie header.
     *
     * @return If the value of this {@link Cookie} is to be wrapped
     */
    public boolean wrap() {
        return wrap;
    }

    /**
     * Sets true if the value of this {@link Cookie}
     * is to be wrapped with double quotes.
     *
     * @param wrap true if wrap
     */
    public Cookie setWrap(boolean wrap) {
        this.wrap = wrap;
        return this;
    }

    /**
     * Returns the domain of this {@link Cookie}.
     *
     * @return The domain of this {@link Cookie}
     */
    public String domain() {
        return domain;
    }

    /**
     * Sets the domain of this {@link Cookie}.
     *
     * @param domain The domain to use
     */
    public Cookie setDomain(String domain) {
        this.domain = Helper.validateAttributeValue("domain", domain);
        return this;
    }


    /**
     * Returns the path of this {@link Cookie}.
     *
     * @return The {@link Cookie}'s path
     */
    public String path() {
        return path;
    }

    /**
     * Sets the path of this {@link Cookie}.
     *
     * @param path The path to use for this {@link Cookie}
     */
    public Cookie setPath(String path) {
        this.path =  Helper.validateAttributeValue("path", path);
        return this;
    }

    /**
     * Returns the maximum age of this {@link Cookie} in seconds or {@link Cookie#UNDEFINED_MAX_AGE} if unspecified
     *
     * @return The maximum age of this {@link Cookie}
     */
    public long maxAge() {
        return maxAge;
    }

    /**
     * Sets the maximum age of this {@link Cookie} in seconds.
     * If an age of {@code 0} is specified, this {@link Cookie} will be
     * automatically removed by browser because it will expire immediately.
     * If {@link Cookie#UNDEFINED_MAX_AGE} is specified, this {@link Cookie} will be removed when the
     * browser is closed.
     *
     * @param maxAge The maximum age of this {@link Cookie} in seconds
     */
    public Cookie setMaxAge(long maxAge) {
        this.maxAge = maxAge;
        return this;
    }

    /**
     * Checks to see if this {@link Cookie} is secure
     *
     * @return True if this {@link Cookie} is secure, otherwise false
     */
    public boolean isSecure() {
        return secure;
    }

    /**
     * Sets the security getStatus of this {@link Cookie}
     *
     * @param secure True if this {@link Cookie} is to be secure, otherwise false
     */
    public Cookie setSecure(boolean secure) {
        this.secure = secure;
        return this;
    }

    /**
     * Checks to see if this {@link Cookie} can only be accessed via HTTP.
     * If this returns true, the {@link Cookie} cannot be accessed through
     * client side script - But only if the browser supports it.
     * For more information, please look <a href="https://owasp.org/www-community/HttpOnly">here</a>
     *
     * @return True if this {@link Cookie} is HTTP-only or false if it isn't
     */
    public boolean isHttpOnly() {
        return httpOnly;
    }

    /**
     * Determines if this {@link Cookie} is HTTP only.
     * If set to true, this {@link Cookie} cannot be accessed by a client
     * side script. However, this works only if the browser supports it.
     * For information, please look
     * <a href="https://owasp.org/www-community/HttpOnly">here</a>.
     *
     * @param httpOnly True if the {@link Cookie} is HTTP only, otherwise false.
     */
    public Cookie setHttpOnly(boolean httpOnly) {
        this.httpOnly = httpOnly;
        return this;
    }

    /**
     * Checks to see if this {@link Cookie} can be sent along cross-site requests.
     * For more information, please look
     * <a href="https://tools.ietf.org/html/draft-ietf-httpbis-rfc6265bis-05">here</a>
     * @return <b>same-site-flag</b> value
     */
    public SameSite sameSite() {
        return sameSite;
    }

    /**
     * Determines if this {@link Cookie} can be sent along cross-site requests.
     * For more information, please look
     *  <a href="https://tools.ietf.org/html/draft-ietf-httpbis-rfc6265bis-05">here</a>
     * @param sameSite <b>same-site-flag</b> value
     */
    public Cookie setSameSite(SameSite sameSite) {
        this.sameSite = sameSite;
        return this;
    }

    @Override
    public int compareTo(Cookie c) {
        int v = name().compareTo(c.name());
        if (v != 0) return v;

        if (path() == null) {
            if (c.path() != null) return -1;
        }
        else if (c.path() == null) {
            return 1;
        } else {
            v = path().compareTo(c.path());
            if (v != 0) {
                return v;
            }
        }

        if (domain() == null) {
            if (c.domain() != null) {
                return -1;
            }
        } else if (c.domain() == null) {
            return 1;
        } else {
            v = domain().compareToIgnoreCase(c.domain());
            return v;
        }

        return 0;

    }

    public int hashCode() {
        return name().hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Cookie cookie = (Cookie) o;
        return Objects.equals(name, cookie.name)
                && Objects.equals(path, cookie.path)
                && Helper.equalsIgnoreCase(domain, cookie.domain) ;

    }

    @Override
    public String toString() {
        StringBuilder buf = new StringBuilder();
        buf.append(name()).append('=').append(value());
        if (domain() != null) buf.append(", domain=").append(domain());
        if (path() != null) buf.append(", path=").append(path());
        if (maxAge() >= 0) buf.append(", maxAge=").append(maxAge()).append('s');
        if (isSecure()) buf.append(", secure");
        if (isHttpOnly()) buf.append(", HTTPOnly");
        if (sameSite() != null) buf.append(", SameSite=").append(sameSite());
        return buf.toString();
    }

    public String getNameValue() {
        return "%s=%s".formatted(name, value);
    }
}