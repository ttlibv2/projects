package vn.conyeu.commons.utils;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

public class AesHelper {
    static final Base64.Decoder decoder = Base64.getDecoder();
    static final char[] hex = "0123456789abcdef".toCharArray();

    public static String decrypt(String password, String iv, String encryptText) {
        return decrypt(password, iv, encryptText, StandardCharsets.UTF_8);
    }

    public static String decrypt(String password, String iv, String encryptText, Charset charset) {
        try {
            byte[] keyByte = decoder.decode(password.getBytes(charset));
            SecretKeySpec secretKey = new SecretKeySpec(keyByte, "AES");

            byte[] ivbBytes = decoder.decode(iv.getBytes(charset));
            IvParameterSpec ivparameterspec = new IvParameterSpec(ivbBytes);

            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
            cipher.init(Cipher.DECRYPT_MODE, secretKey, ivparameterspec);

            byte[] cipherText = decoder.decode(encryptText);
            return new String(cipher.doFinal(cipherText), charset);
        }
        catch (Exception ex) {
            throw new RuntimeException(ex.getMessage(), ex);
        }
    }

    public static MessageDigest MD5() {
        try {
            return MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    public static String md5(byte[] input) {
        MessageDigest md5 = MD5();
        md5.update(input);
        return toHexString(md5.digest());
    }

    public static String md5(String text) {
        Asserts.hasLength(text, "@text must be empty.");
        return md5(text.getBytes());
    }

    public static String md5(Path path) throws IOException {
        return md5(Files.readAllBytes(Asserts.isFile(path)));
    }


    public static String toHexString(byte[] bytes) {
        if (null == bytes) return null;
        StringBuilder sb = new StringBuilder(bytes.length << 1);

        for (byte aByte : bytes) {
            sb.append(hex[(aByte & 0xf0) >> 4]);
            sb.append(hex[(aByte & 0x0f)]);
        }

        return sb.toString();
    }

    public static PublicKey readPublicKey(String algorithm, Path publicKeyFile) throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
        byte[] dataBytes = Files.readAllBytes(publicKeyFile);
        X509EncodedKeySpec publicSpec  = new X509EncodedKeySpec(dataBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(algorithm);
        return keyFactory.generatePublic(publicSpec);
    }

    public static PrivateKey readPrivateKey(String algorithm, Path privateKeyFile) throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
        byte[] dataBytes = Files.readAllBytes(privateKeyFile);
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(dataBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(algorithm);
        return keyFactory.generatePrivate(keySpec);
    }
}