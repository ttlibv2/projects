package vn.conyeu.cookie;

import java.util.Collection;

public interface CookieEncoder<R>{
    boolean strict();

    /**
     * Encodes the specified cookie
     * @param name  the cookie name
     * @param value the cookie value
     */
    String encode(String name, String value);

    /**
     * Encodes the specified cookie
     * @param cookie the specified cookie
     */
    String encode(Cookie cookie);

    /**
     * Batch encodes cookies into Set-Cookie header values.
     * @param cookies a bunch of cookies
     */
    R encode(Cookie[] cookies);

    /**
     * Batch encodes cookies into Set-Cookie header values.
     * @param cookies a bunch of cookies
     */
    R encode(Collection<? extends Cookie> cookies);

    /**
     * Batch encodes cookies into Set-Cookie header values.
     * @param cookies a bunch of cookies
     */
   R encode(Iterable<? extends Cookie> cookies);
}