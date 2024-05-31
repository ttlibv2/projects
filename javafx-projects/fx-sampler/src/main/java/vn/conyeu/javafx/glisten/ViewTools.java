package vn.conyeu.javafx.glisten;


public class ViewTools {
    private static final String VIEW_NAME = "gls-view-name";

    public ViewTools() {
    }

    public static void storeViewName(View view, String name) {
        view.getProperties().put(VIEW_NAME, name);
    }

    public static String findViewName(View view) {
        if(view != null) {
            Object propN = view.getProperties().get(VIEW_NAME);
            if(propN instanceof String str) return str;
        }
        return null;
    }
}