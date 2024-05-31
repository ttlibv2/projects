package vn.conyeu.commons.utils;

import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.util.Base64;

public class Base64s {
    private static final Base64.Encoder encoder = Base64.getEncoder();

    public static String encodeImage(String contentType, byte[] source) {
        return "data:%s;base64,%s".formatted(contentType, encodeToString(source));
    }

    /**
     * Encodes all bytes from the specified byte array
     * @param src the byte array to encode
     */
    public static byte[] encode(byte[] src) {
        return encoder.encode(src);
    }

    /**
     * Encodes all bytes from the specified byte array
     * @param src the byte array to encode
     * @param dst the output byte array
     * @return The number of bytes written to the output byte array
     */
    public static int encode(byte[] src, byte[] dst) {
        return encoder.encode(src, dst);
    }

    /**
     * Encodes the specified byte array
     * @param src the byte array to encode
     * @return A String containing the resulting Base64 encoded characters
     */
    public static String encodeToString(byte[] src) {
        return encoder.encodeToString(src);
    }


    /**
     * Encodes the specified byte array
     * @param src the string to encode
     * @return A String containing the resulting Base64 encoded characters
     */
    public static String encodeToString(String src) {
        Asserts.notNull(src, "@src");
        return encodeToString(src.getBytes());
    }


    /**
     * Encodes all remaining bytes from the specified byte buffer
     * @param buffer the source ByteBuffer to encode
     * @return A newly-allocated byte buffer containing the encoded bytes.
     */
    public static ByteBuffer encode(ByteBuffer buffer) {
        return encoder.encode(buffer);
    }

    /**
     * Wraps an output stream for encoding byte data
     * @param os the output stream.
     * @return the output stream for encoding the byte data into the specified Base64 encoded format
     */
    public static OutputStream wrap(OutputStream os) {
        return encoder.wrap(os);
    }

    public static String basicAuth(String userName, String password) {
        byte[] bytes = "%s:%s".formatted(userName, password).getBytes();
        return encodeToString(bytes);
    }
}