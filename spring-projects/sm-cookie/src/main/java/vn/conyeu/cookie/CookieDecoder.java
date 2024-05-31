package vn.conyeu.cookie;

import org.slf4j.Logger;

import java.nio.CharBuffer;

import static vn.conyeu.cookie.Helper.*;

public interface CookieDecoder<R> {
    boolean strict();
    Logger log();


    /**
     * Decodes the specified {@code Cookie} HTTP header value into a {@link Cookie}.
     *
     * @return the decoded {@link Cookie}
     */
    R decode(String cookieHeader);



    default Cookie createCookie(String header, int nameBegin, int nameEnd, int valueBegin, int valueEnd) {
        if (nameBegin == -1 || nameBegin == nameEnd) {
            log().debug("Skipping cookie with null name");
            return null;
        }

        if (valueBegin == -1) {
            log().debug("Skipping cookie with null value");
            return null;
        }

        CharSequence wrappedValue = CharBuffer.wrap(header, valueBegin, valueEnd);
        CharSequence unwrappedValue = unwrapValue(wrappedValue);
        if (unwrappedValue == null) {
            log().debug("Skipping cookie because starting quotes are not properly balanced in '{}'", wrappedValue);
            return null;
        }

        final String name = header.substring(nameBegin, nameEnd);

        int invalidOctetPos;
        if (strict() && (invalidOctetPos = firstInvalidCookieNameOctet(name)) >= 0) {
            log().debug("Skipping cookie because name '{}' contains invalid char '{}'",
                        name, name.charAt(invalidOctetPos));
            return null;
        }

        final boolean wrap = unwrappedValue.length() != valueEnd - valueBegin;

        if (strict() && (invalidOctetPos = firstInvalidCookieValueOctet(unwrappedValue)) >= 0) {
            log().debug("Skipping cookie because value '{}' contains invalid char '{}'",
                        unwrappedValue, unwrappedValue.charAt(invalidOctetPos));

            return null;
        }

        return new Cookie(name, unwrappedValue.toString()).setWrap(wrap);
    }

}