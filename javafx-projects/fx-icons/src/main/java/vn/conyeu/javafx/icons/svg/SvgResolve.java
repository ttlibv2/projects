package vn.conyeu.javafx.icons.svg;

import vn.conyeu.javafx.icons.core.IconResolve;

public final class SvgResolve extends IconResolve<SvgDesc, SvgHandler> {
    private static final SvgResolve INSTANCE = new SvgResolve();
    public static SvgResolve getInstance() { return INSTANCE; }
    private SvgResolve() {super(SvgHandler.class);}

    @Override
    protected void applyHandlerClasspath(SvgHandler handler) {}
}