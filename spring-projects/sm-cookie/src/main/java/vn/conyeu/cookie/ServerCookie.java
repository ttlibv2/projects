package vn.conyeu.cookie;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;

import java.util.*;

public final class ServerCookie {

    /**
     * A <a href="https://tools.ietf.org/html/rfc6265">RFC6265</a> compliant cookie decoder to be used server side.
     * <p>
     * Only name and value fields are expected, so old fields are not populated (path, domain, etc).
     * <p>
     * Old <a href="https://tools.ietf.org/html/rfc2965">RFC2965</a> cookies are still supported,
     * old fields will simply be ignored.
     */
    @Slf4j
    public static final class Decoder extends SampleObject implements CookieDecoder<Set<Cookie>> {
        private static final String RFC2965_VERSION = "$Version";
        private static final String RFC2965_PATH = "$" + Cookies.PATH;
        private static final String RFC2965_DOMAIN = "$" + Cookies.DOMAIN;
        private static final String RFC2965_PORT = "$Port";

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
         * Decodes the specified {@code Cookie} HTTP header value into a {@link Cookie}. Unlike {@link #decode(String)},
         * this includes all cookie values present, even if they have the same name.
         *
         * @return the decoded {@link Cookie}
         */
        public Set<Cookie> decodeAll(String header) {
            Set<Cookie> cookies = new HashSet<>();
            decode(cookies, header);
            return Collections.unmodifiableSet(cookies);
        }

        /**
         * Decodes the specified {@code Cookie} HTTP header value into a {@link Cookie}.
         *
         * @return the decoded {@link Cookie}
         */
        public Set<Cookie> decode(String cookieHeader) {
            Set<Cookie> cookies = new TreeSet<>();
            decode(cookies, cookieHeader);
            return cookies;
        }

        /**
         * Decodes the specified {@code Cookie} HTTP header value into a {@link Cookie}.
         */
        private void decode(Collection<? super Cookie> cookies, String header) {
            final int headerLen = Helper.checkNotNull(header, "header").length();

            if (headerLen == 0) {
                return;
            }

            int i = 0;

            boolean rfc2965Style = false;
            if (header.regionMatches(true, 0, RFC2965_VERSION, 0, RFC2965_VERSION.length())) {
                // RFC 2965 style cookie, move to after version value
                i = header.indexOf(';') + 1;
                rfc2965Style = true;
            }

            loop:
            for (; ; ) {

                // Skip spaces and separators.
                for (; ; ) {
                    if (i == headerLen) {
                        break loop;
                    }
                    char c = header.charAt(i);
                    if (c == '\t' || c == '\n' || c == 0x0b || c == '\f'
                            || c == '\r' || c == ' ' || c == ',' || c == ';') {
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

                if (rfc2965Style && (header.regionMatches(nameBegin, RFC2965_PATH, 0, RFC2965_PATH.length()) ||
                        header.regionMatches(nameBegin, RFC2965_DOMAIN, 0, RFC2965_DOMAIN.length()) ||
                        header.regionMatches(nameBegin, RFC2965_PORT, 0, RFC2965_PORT.length()))) {

                    // skip obsolete RFC2965 fields
                    continue;
                }

                Cookie cookie = createCookie(header, nameBegin, nameEnd, valueBegin, valueEnd);
                if (cookie != null) cookies.add(cookie);
            }
        }

        @Override
        public Logger log() {
            return log;
        }
    }

    /**
     * A <a href="https://tools.ietf.org/html/rfc6265">RFC6265</a> compliant cookie encoder to be used server side,
     * so some fields are sent (Version is typically ignored).
     * <p>
     * As Netty's Cookie merges Expires and MaxAge into one single field, only Max-Age field is sent.
     * <p>
     * Note that multiple cookies must be sent as separate "Set-Cookie" headers.
     */
    public static final class Encoder extends SampleObject implements CookieEncoder<List<String>> {

        /**
         * Strict encoder that validates that name and value chars are in the valid scope
         * defined in RFC6265, and (for methods that accept multiple cookies) that only
         * one cookie is encoded with any given name. (If multiple cookies have the same
         * name, the last one is the one that is encoded.)
         */
        public static final Encoder STRICT = new Encoder(SameSite.Strict);

        /**
         * Lax instance that doesn't validate name and value, and that allows multiple
         * cookies with the same name.
         */
        public static final Encoder LAX = new Encoder(SameSite.Lax);

        Encoder(SameSite sameSite) {
            super(sameSite);
        }

        /**
         * Encodes the specified cookie name-value pair into a Set-Cookie header value.
         *
         * @param name  the cookie name
         * @param value the cookie value
         * @return a single Set-Cookie header value
         */
        public String encode(String name, String value) {
            return encode(new Cookie(name, value));
        }

        /**
         * Encodes the specified cookie into a Set-Cookie header value.
         *
         * @param cookie the cookie
         * @return a single Set-Cookie header value
         */
        public String encode(Cookie cookie) {
            final String name = Helper.checkNotNull(cookie, "cookie").name();
            final String value = cookie.value() != null ? cookie.value() : "";

            validateCookie(name, value);

            StringBuilder buf = stringBuilder();
            if (cookie.wrap()) Helper.addQuoted(buf, name, value);
            else Helper.add(buf, name, value);

            if (cookie.maxAge() != Long.MIN_VALUE) {
                Helper.add(buf, Cookies.MAX_AGE, cookie.maxAge());
                Date expires = new Date(cookie.maxAge() * 1000 + System.currentTimeMillis());
                buf.append(Cookies.EXPIRES);
                buf.append('=');
                DateFormat.append(expires, buf);
                buf.append(';');
                buf.append(Cookies.SP_CHAR);
            }

            if (cookie.path() != null) {
                Helper.add(buf, Cookies.PATH, cookie.path());
            }

            if (cookie.domain() != null) {
                Helper.add(buf, Cookies.DOMAIN, cookie.domain());
            }
            if (cookie.isSecure()) {
                Helper.add(buf, Cookies.SECURE);
            }
            if (cookie.isHttpOnly()) {
                Helper.add(buf, Cookies.HTTPONLY);
            }

            if (cookie.sameSite() != null) {
                Helper.add(buf, Cookies.SAME_SITE, cookie.sameSite().name());
            }


            return Helper.stripTrailingSeparator(buf);
        }

        /**
         * Batch encodes cookies into Set-Cookie header values.
         *
         * @param cookies a bunch of cookies
         * @return the corresponding bunch of Set-Cookie headers
         */
        public List<String> encode(Cookie[] cookies) {
            if (Helper.checkNotNull(cookies, "cookies").length == 0) {
                return Collections.emptyList();
            }

            List<String> encoded = new ArrayList<String>(cookies.length);
            Map<String, Integer> nameToIndex = strict() && cookies.length > 1 ? new HashMap<String, Integer>() : null;
            boolean hasDupdName = false;
            for (int i = 0; i < cookies.length; i++) {
                Cookie c = cookies[i];
                encoded.add(encode(c));
                if (nameToIndex != null) {
                    hasDupdName |= nameToIndex.put(c.name(), i) != null;
                }
            }
            return hasDupdName ? dedup(encoded, nameToIndex) : encoded;
        }

        /**
         * Batch encodes cookies into Set-Cookie header values.
         *
         * @param cookies a bunch of cookies
         * @return the corresponding bunch of Set-Cookie headers
         */
        public List<String> encode(Collection<? extends Cookie> cookies) {
            if (Helper.checkNotNull(cookies, "cookies").isEmpty()) {
                return Collections.emptyList();
            }

            List<String> encoded = new ArrayList<String>(cookies.size());
            Map<String, Integer> nameToIndex = strict() && cookies.size() > 1 ? new HashMap<String, Integer>() : null;
            int i = 0;
            boolean hasDupdName = false;
            for (Cookie c : cookies) {
                encoded.add(encode(c));
                if (nameToIndex != null) {
                    hasDupdName |= nameToIndex.put(c.name(), i++) != null;
                }
            }
            return hasDupdName ? dedup(encoded, nameToIndex) : encoded;
        }

        /**
         * Batch encodes cookies into Set-Cookie header values.
         *
         * @param cookies a bunch of cookies
         * @return the corresponding bunch of Set-Cookie headers
         */
        public List<String> encode(Iterable<? extends Cookie> cookies) {
            Iterator<? extends Cookie> cookiesIt = Helper.checkNotNull(cookies, "cookies").iterator();
            if (!cookiesIt.hasNext()) {
                return Collections.emptyList();
            }

            List<String> encoded = new ArrayList<String>();
            Cookie firstCookie = cookiesIt.next();
            Map<String, Integer> nameToIndex = strict() && cookiesIt.hasNext() ? new HashMap<>() : null;
            int i = 0;
            encoded.add(encode(firstCookie));
            boolean hasDupdName = nameToIndex != null && nameToIndex.put(firstCookie.name(), i++) != null;
            while (cookiesIt.hasNext()) {
                Cookie c = cookiesIt.next();
                encoded.add(encode(c));
                if (nameToIndex != null) {
                    hasDupdName |= nameToIndex.put(c.name(), i++) != null;
                }
            }
            return hasDupdName ? dedup(encoded, nameToIndex) : encoded;
        }

        /**
         * Deduplicate a list of encoded cookies by keeping only the last instance with a given name.
         *
         * @param encoded         The list of encoded cookies.
         * @param nameToLastIndex A map from cookie name to index of last cookie instance.
         * @return The encoded list with all but the last instance of a named cookie.
         */
        private static List<String> dedup(List<String> encoded, Map<String, Integer> nameToLastIndex) {
            boolean[] isLastInstance = new boolean[encoded.size()];
            for (int idx : nameToLastIndex.values()) {
                isLastInstance[idx] = true;
            }
            List<String> dedupd = new ArrayList<String>(nameToLastIndex.size());
            for (int i = 0, n = encoded.size(); i < n; i++) {
                if (isLastInstance[i]) {
                    dedupd.add(encoded.get(i));
                }
            }
            return dedupd;
        }
    }
}