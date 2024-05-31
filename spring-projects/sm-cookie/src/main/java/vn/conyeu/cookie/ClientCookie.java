package vn.conyeu.cookie;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.springframework.util.MultiValueMap;

import java.util.*;

import static vn.conyeu.cookie.Helper.stripTrailingSeparator;
import static vn.conyeu.cookie.Helper.checkNotNull;
import static vn.conyeu.cookie.Helper.stripTrailingSeparatorOrNull;
import static vn.conyeu.cookie.Helper.addQuoted;
import static vn.conyeu.cookie.Helper.add;

public class ClientCookie {

    /**
     * A <a href="https://tools.ietf.org/html/rfc6265">RFC6265</a> compliant cookie decoder to be used client side.
     * It will store the way the raw value was wrapped in {@link Cookie#setWrap(boolean)} so it can be
     * eventually sent back to the Origin server as is.
     */
    @Slf4j
    public static final class Decoder extends SampleObject implements CookieDecoder<Cookie> {

        /**
         * Strict encoder that validates that name and value chars are in the valid scope
         * defined in RFC6265
         */
        public static final Decoder STRICT = new Decoder(SameSite.Strict);

        /**
         * Lax instance that doesn't validate name and value
         */
        public static final Decoder LAX = new Decoder(SameSite.Lax);

        Decoder(SameSite sameSite) {
            super(sameSite);
        }

        /**
         * Decodes the specified Set-Cookie HTTP header value into a {@link Cookie}.
         *
         * @return the decoded {@link Cookie}
         */
        public Cookie decode(String header) {
            final int headerLen = checkNotNull(header, "header").length();
            if (headerLen == 0) return null;

            CookieBuilder cookieBuilder = null;

            loop:
            for (int i = 0; ; ) {

                // Skip spaces and separators.
                for (; ; ) {
                    if (i == headerLen) {
                        break loop;
                    }
                    char c = header.charAt(i);
                    if (c == ',') {
                        // Having multiple cookies in a single Set-Cookie header is
                        // deprecated, modern browsers only parse the first one
                        break loop;

                    } else if (c == '\t' || c == '\n' || c == 0x0b || c == '\f'
                            || c == '\r' || c == ' ' || c == ';') {
                        i++;
                        continue;
                    }
                    break;
                }

                int nameBegin = i;
                int nameEnd;
                int valueBegin;
                int valueEnd;

                for (; ; ) {
                    char curChar = header.charAt(i);
                    if (curChar == ';') {
                        // NAME; (no value till ';')
                        nameEnd = i;
                        valueBegin = valueEnd = -1;
                        break;

                    } else if (curChar == '=') {
                        // NAME=VALUE
                        nameEnd = i;
                        i++;
                        if (i == headerLen) {
                            // NAME= (empty value, i.e. nothing after '=')
                            valueBegin = valueEnd = 0;
                            break;
                        }

                        valueBegin = i;
                        // NAME=VALUE;
                        int semiPos = header.indexOf(';', i);
                        valueEnd = i = semiPos > 0 ? semiPos : headerLen;
                        break;
                    } else {
                        i++;
                    }

                    if (i == headerLen) {
                        // NAME (no value till the end of string)
                        nameEnd = headerLen;
                        valueBegin = valueEnd = -1;
                        break;
                    }
                }

                if (valueEnd > 0 && header.charAt(valueEnd - 1) == ',') {
                    // old multiple cookies separator, skipping it
                    valueEnd--;
                }

                if (cookieBuilder == null) {
                    Cookie defaultCookie = createCookie(header, nameBegin, nameEnd, valueBegin, valueEnd);
                    if (defaultCookie == null) return null;

                    cookieBuilder = new CookieBuilder(defaultCookie, header);
                } //
                else {
                    // cookie attribute
                    cookieBuilder.appendAttribute(nameBegin, nameEnd, valueBegin, valueEnd);
                }
            }
            return cookieBuilder != null ? cookieBuilder.cookie() : null;
        }

        @Override
        public Logger log() {
            return log;
        }

        private static class CookieBuilder {

            private final String header;
            private final Cookie cookie;
            private String domain;
            private String path;
            private long maxAge = Long.MIN_VALUE;
            private int expiresStart;
            private int expiresEnd;
            private boolean secure;
            private boolean httpOnly;
            private SameSite sameSite;

            CookieBuilder(Cookie cookie, String header) {
                this.cookie = cookie;
                this.header = header;
            }

            private long mergeMaxAgeAndExpires() {
                // max age has precedence over expires
                if (maxAge != Long.MIN_VALUE) {
                    return maxAge;
                } else if (isValueDefined(expiresStart, expiresEnd)) {
                    Date expiresDate = DateFormat.parseHttpDate(header, expiresStart, expiresEnd);
                    if (expiresDate != null) {
                        long maxAgeMillis = expiresDate.getTime() - System.currentTimeMillis();
                        return maxAgeMillis / 1000 + (maxAgeMillis % 1000 != 0 ? 1 : 0);
                    }
                }
                return Long.MIN_VALUE;
            }

            Cookie cookie() {
                cookie.setDomain(domain);
                cookie.setPath(path);
                cookie.setMaxAge(mergeMaxAgeAndExpires());
                cookie.setSecure(secure);
                cookie.setHttpOnly(httpOnly);
                cookie.setSameSite(sameSite);
                return cookie;
            }

            /**
             * Parse and store a key-value pair. First one is considered to be the
             * cookie name/value. Unknown attribute names are silently discarded.
             *
             * @param keyStart   where the key starts in the header
             * @param keyEnd     where the key ends in the header
             * @param valueStart where the value starts in the header
             * @param valueEnd   where the value ends in the header
             */
            void appendAttribute(int keyStart, int keyEnd, int valueStart, int valueEnd) {
                int length = keyEnd - keyStart;
                if (length == 4) parse4(keyStart, valueStart, valueEnd);
                else if (length == 6) parse6(keyStart, valueStart, valueEnd);
                else if (length == 7) parse7(keyStart, valueStart, valueEnd);
                else if (length == 8) parse8(keyStart, valueStart, valueEnd);

            }

            private void parse4(int nameStart, int valueStart, int valueEnd) {
                if (header.regionMatches(true, nameStart, Cookies.PATH, 0, 4)) {
                    path = computeValue(valueStart, valueEnd);
                }
            }

            private void parse6(int nameStart, int valueStart, int valueEnd) {
                if (header.regionMatches(true, nameStart, Cookies.DOMAIN, 0, 5)) {
                    domain = computeValue(valueStart, valueEnd);
                } else if (header.regionMatches(true, nameStart, Cookies.SECURE, 0, 5)) {
                    secure = true;
                }
            }

            private void setMaxAge(String value) {
                try {
                    maxAge = Math.max(Long.parseLong(value), 0L);
                } catch (NumberFormatException e1) {
                    // ignore failure to parse -> treat as session cookie
                }
            }

            private void parse7(int nameStart, int valueStart, int valueEnd) {
                if (header.regionMatches(true, nameStart, Cookies.EXPIRES, 0, 7)) {
                    expiresStart = valueStart;
                    expiresEnd = valueEnd;
                } else if (header.regionMatches(true, nameStart, Cookies.MAX_AGE, 0, 7)) {
                    setMaxAge(computeValue(valueStart, valueEnd));
                }
            }

            private void parse8(int nameStart, int valueStart, int valueEnd) {
                if (header.regionMatches(true, nameStart, Cookies.HTTPONLY, 0, 8)) {
                    httpOnly = true;
                } else if (header.regionMatches(true, nameStart, Cookies.SAME_SITE, 0, 8)) {
                    sameSite = SameSite.of(computeValue(valueStart, valueEnd));
                }
            }

            private static boolean isValueDefined(int valueStart, int valueEnd) {
                return valueStart != -1 && valueStart != valueEnd;
            }

            private String computeValue(int valueStart, int valueEnd) {
                return isValueDefined(valueStart, valueEnd) ? header.substring(valueStart, valueEnd) : null;
            }
        }
    }

    /**
     * A <a href="https://tools.ietf.org/html/rfc6265">RFC6265</a> compliant cookie encoder to be used client side, so
     * only name=value pairs are sent.
     * Note that multiple cookies are supposed to be sent at once in a single "Cookie" header.
     */
    public static final class Encoder extends SampleObject implements CookieEncoder<String> {

        /**
         * Strict encoder that validates that name and value chars are in the valid scope and (for methods that accept
         * multiple cookies) sorts cookies into order of decreasing path length, as specified in RFC6265.
         */
        public static final Encoder STRICT = new Encoder(SameSite.Strict);

        /**
         * Lax instance that doesn't validate name and value, and (for methods that accept multiple cookies) keeps
         * cookies in the order in which they were given.
         */
        public static final Encoder LAX = new Encoder(SameSite.Lax);

        Encoder(SameSite sameSite) {
            super(sameSite);
        }

        /**
         * Encodes the specified cookie into a Cookie header value.
         *
         * @param name  the cookie name
         * @param value the cookie value
         * @return a Rfc6265 style Cookie header value
         */
        public String encode(String name, String value) {
            return encode(new Cookie(name, value));
        }

        /**
         * Encodes the specified cookie into a Cookie header value.
         *
         * @param cookie the specified cookie
         * @return a Rfc6265 style Cookie header value
         */
        public String encode(Cookie cookie) {
            StringBuilder buf = stringBuilder();
            encode(buf, checkNotNull(cookie, "cookie"));
            return stripTrailingSeparator(buf);
        }

        /**
         * Encodes the specified cookies into a single Cookie header value.
         *
         * @param cookies some cookies
         * @return a Rfc6265 style Cookie header value, null if no cookies are passed.
         */
        public String encode(Cookie[] cookies) {
            if (checkNotNull(cookies, "cookies").length == 0) {
                return null;
            }

            StringBuilder buf = stringBuilder();
            if (strict()) {
                if (cookies.length == 1) {
                    encode(buf, cookies[0]);
                } else {
                    Cookie[] cookiesSorted = Arrays.copyOf(cookies, cookies.length);
                    Arrays.sort(cookiesSorted, COOKIE_COMPARATOR);
                    for (Cookie c : cookiesSorted) {
                        encode(buf, c);
                    }
                }
            } else {
                for (Cookie c : cookies) {
                    encode(buf, c);
                }
            }
            return stripTrailingSeparatorOrNull(buf);
        }

        /**
         * Encodes the specified cookies into a single Cookie header value.
         *
         * @param cookies some cookies
         * @return a Rfc6265 style Cookie header value, null if no cookies are passed.
         */
        public String encode(Collection<? extends Cookie> cookies) {
            if (checkNotNull(cookies, "cookies").isEmpty()) {
                return null;
            }

            StringBuilder buf = stringBuilder();
            if (strict()) {
                if (cookies.size() == 1) {
                    encode(buf, cookies.iterator().next());
                } else {
                    Cookie[] cookiesSorted = cookies.toArray(new Cookie[0]);
                    Arrays.sort(cookiesSorted, COOKIE_COMPARATOR);
                    for (Cookie c : cookiesSorted) {
                        encode(buf, c);
                    }
                }
            } else {
                for (Cookie c : cookies) {
                    encode(buf, c);
                }
            }
            return stripTrailingSeparatorOrNull(buf);
        }

        /**
         * Encodes the specified cookies into a single Cookie header value.
         *
         * @param cookies some cookies
         * @return a Rfc6265 style Cookie header value, null if no cookies are passed.
         */
        public String encode(Iterable<? extends Cookie> cookies) {
            Iterator<? extends Cookie> cookiesIt = checkNotNull(cookies, "cookies").iterator();
            if (!cookiesIt.hasNext()) {
                return null;
            }

            StringBuilder buf = stringBuilder();
            if (strict()) {
                Cookie firstCookie = cookiesIt.next();
                if (!cookiesIt.hasNext()) {
                    encode(buf, firstCookie);
                } else {
                    List<Cookie> cookiesList = new ArrayList<>();
                    cookiesList.add(firstCookie);
                    while (cookiesIt.hasNext()) {
                        cookiesList.add(cookiesIt.next());
                    }
                    Cookie[] cookiesSorted = cookiesList.toArray(new Cookie[0]);
                    Arrays.sort(cookiesSorted, COOKIE_COMPARATOR);
                    for (Cookie c : cookiesSorted) {
                        encode(buf, c);
                    }
                }
            } else {
                while (cookiesIt.hasNext()) {
                    encode(buf, cookiesIt.next());
                }
            }
            return stripTrailingSeparatorOrNull(buf);
        }

        private void encode(StringBuilder buf, Cookie c) {
            final String name = c.name(), value = c.value() != null ? c.value() : "";
            validateCookie(name, value);
            if (c.wrap()) addQuoted(buf, name, value);
            else add(buf, name, value);

        }

        /**
         * Sort cookies into decreasing order of path length, breaking ties by sorting into increasing chronological
         * order of creation time, as recommended by RFC 6265.
         */
        // package-private for testing only.
        static final Comparator<Cookie> COOKIE_COMPARATOR = (c1, c2) -> {
            String path1 = c1.path();
            String path2 = c2.path();
            // Cookies with unspecified path default to the path of the request. We don't
            // know the request path here, but we assume that the length of an unspecified
            // path is longer than any specified path (i.e. pathless cookies come first),
            // because setting cookies with a path longer than the request path is of
            // limited use.
            int len1 = path1 == null ? Integer.MAX_VALUE : path1.length();
            int len2 = path2 == null ? Integer.MAX_VALUE : path2.length();

            // Rely on Arrays.sort's stability to retain creation order in cases where
            // cookies have same path length.
            return len2 - len1;
        };
    }
}