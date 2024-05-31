package vn.conyeu.commons.beans;

import vn.conyeu.commons.utils.Asserts;

import java.util.*;

public final class MimeMappings  {
    private static Map<String, String> mappings = new HashMap<>();

    static {
        mappings.put("avi", "video/x-msvideo");
        mappings.put("bin", "application/octet-stream");
        mappings.put("body", "text/html");
        mappings.put("class", "application/java");
        mappings.put("css", "text/css");
        mappings.put("dtd", "application/xml-dtd");
        mappings.put("gif", "image/gif");
        mappings.put("gtar", "application/x-gtar");
        mappings.put("gz", "application/x-gzip");
        mappings.put("htm", "text/html");
        mappings.put("html", "text/html");
        mappings.put("jar", "application/java-archive");
        mappings.put("java", "text/x-java-source");
        mappings.put("jnlp", "application/x-java-jnlp-file");
        mappings.put("jpe", "image/jpeg");
        mappings.put("jpeg", "image/jpeg");
        mappings.put("jpg", "image/jpeg");
        mappings.put("js", "text/javascript");
        mappings.put("json", "application/json");
        mappings.put("otf", "font/otf");
        mappings.put("pdf", "application/pdf");
        mappings.put("png", "image/png");
        mappings.put("ps", "application/postscript");
        mappings.put("tar", "application/x-tar");
        mappings.put("tif", "image/tiff");
        mappings.put("tiff", "image/tiff");
        mappings.put("ttf", "font/ttf");
        mappings.put("txt", "text/plain");
        mappings.put("xht", "application/xhtml+xml");
        mappings.put("xhtml", "application/xhtml+xml");
        mappings.put("xls", "application/vnd.ms-excel");
        mappings.put("xml", "application/xml");
        mappings.put("xsl", "application/xml");
        mappings.put("xslt", "application/xslt+xml");
        mappings.put("wasm", "application/wasm");
        mappings.put("zip", "application/zip");
    }

    public static String get(String extension) {
        Asserts.notBlank(extension);
        return mappings.get(extension.toLowerCase(Locale.ENGLISH));
    }
}