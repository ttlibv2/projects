package vn.conyeu.cookie;

import org.springframework.http.HttpHeaders;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public final class Cookies {
    public static final String PATH = Helper.PATH;
    public static final String EXPIRES = Helper.EXPIRES;
    public static final String MAX_AGE = Helper.MAX_AGE;
    public static final String DOMAIN = Helper.DOMAIN;
    public static final String SECURE = Helper.SECURE;
    public static final String HTTPONLY = Helper.HTTPONLY;
    public static final String SAME_SITE = Helper.SAME_SITE;
    public static final char SP_CHAR = Helper.SP_CHAR;

    public static ClientCookie.Decoder getClientDecoder() {
        return ClientCookie.Decoder.STRICT;
    }

    public static ServerCookie.Decoder getServerDecoder() {
        return ServerCookie.Decoder.STRICT;
    }

    public static ClientCookie.Encoder getClientEncoder() {
        return ClientCookie.Encoder.STRICT;
    }

    public static ServerCookie.Encoder getServerEncoder() {
        return ServerCookie.Encoder.STRICT;
    }

    /**
     * Convert header {@link HttpHeaders#SET_COOKIE} to cookies
     *
     * @param headers the http header
     */
    public static Set<Cookie> parseSetCookie(HttpHeaders headers) {
        List<String> strings = headers.get(HttpHeaders.SET_COOKIE);
        if (strings == null || strings.isEmpty()) return new HashSet<>();
        return strings.stream().map(str -> getClientDecoder().decode(str)).collect(Collectors.toSet());
    }

    /**
     * Convert header {@link HttpHeaders#COOKIE} to cookies
     *
     * @param headers the http header
     */
    public static Set<Cookie> parseCookie(HttpHeaders headers) {
        return parseCookie(headers.getFirst(HttpHeaders.COOKIE));
    }


    /**
     * Convert header {@link HttpHeaders#COOKIE} to cookies
     *
     * @param cookieString the cookie
     */
    public static Set<Cookie> parseCookie(String cookieString) {
        if (cookieString == null || cookieString.isBlank()) return new HashSet<>();
        return getServerDecoder().decode(cookieString);
    }

}