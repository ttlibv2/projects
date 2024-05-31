package vn.conyeu.javafx.sampler.basic;

import java.net.URL;

public class Uris {

    public static URL getUrl(Class<?> clazz, String uriSource) {
        return clazz.getResource(uriSource);
    }

    public static URL getUrl(ClassLoader loader, String uriSource) {
        return loader.getResource(uriSource);
    }
}