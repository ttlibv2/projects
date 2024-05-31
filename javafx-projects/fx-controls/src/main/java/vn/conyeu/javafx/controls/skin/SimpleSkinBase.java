package vn.conyeu.javafx.controls.skin;

import javafx.scene.Node;
import javafx.scene.control.Control;
import javafx.scene.control.SkinBase;
import vn.conyeu.javafx.events.LambdaListener;

public abstract class SimpleSkinBase<C extends Control> extends SkinBase<C> implements InitializeSkin<C> {
    protected final LambdaListener listener = new LambdaListener();

    public SimpleSkinBase(C control) {
        super(control);
    }

    public final void initialize(C control) {
        initializeUI(control);
        initializeListenerUI(control);
        initializeListener(control);
        initializeDataUI(control);
    }

    @Override
    public void dispose() {
        listener.dispose();
        super.dispose();
    }

    @Override
    public final LambdaListener getListener() {
        return listener;
    }

    protected void tryAddChildrenNode(Node childNode) {
        tryAddChildrenNode(getChildren(), childNode);
    }

    @Override
    protected void layoutChildren(double layoutX, double layoutY, double insideWidth, double insideHeight) {
        super.layoutChildren(layoutX, layoutY, insideWidth, insideHeight);
    }

    @Override
    protected double computeMaxWidth(double height, double top, double right, double bottom, double left) {
        return super.computeMaxWidth(height, top, right, bottom, left);
    }

    @Override
    protected double computeMaxHeight(double width, double top, double right, double bottom, double left) {
        return super.computeMaxHeight(width, top, right, bottom, left);
    }

    @Override
    protected double computePrefWidth(double height, double top, double right, double bottom, double left) {
        return super.computePrefWidth(height, top, right, bottom, left);
    }

    @Override
    protected double computePrefHeight(double width, double top, double right, double bottom, double left) {
        return super.computePrefHeight(width, top, right, bottom, left);
    }

    @Override
    protected double computeMinWidth(double height, double top, double right, double bottom, double left){
        return super.computeMinWidth(height, top, right, bottom, left);
    }

    @Override
    protected double computeMinHeight(double width, double top, double right, double bottom, double left) {
        return super.computeMinHeight(width, top, right, bottom, left);
    }


}