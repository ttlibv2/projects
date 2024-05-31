package vn.conyeu.javafx.controls;

import javafx.beans.binding.ObjectBinding;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ObservableValue;
import javafx.css.*;
import javafx.geometry.*;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.shape.Rectangle;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import vn.conyeu.javafx.bindings.BindingBuilder;
import vn.conyeu.javafx.controls.flex.*;
import vn.conyeu.javafx.events.LambdaListener;
import vn.conyeu.javafx.styleable.CssFactory;
import vn.conyeu.javafx.styleable.simple.Flex;
import vn.conyeu.javafx.utils.CssUtils;
import vn.conyeu.javafx.utils.NodeUtils;

import java.util.*;
import java.util.function.Function;

@Slf4j
public class FlexBox extends Pane {
    private final LambdaListener listener = new LambdaListener();
    private static final String ORDER_CONSTRAINT = "flexbox-order";
    private static final String GROW_CONSTRAINT = "flexbox-flex-grow";
    private static final String MARGIN_CONSTRAINT = "flexbox-margin";
    private static final String SHRINK_CONSTRAINT = "flexbox-flex-shrink";
    private static final String FBASIC_CONSTRAINT = "flexbox-flex-basic";
    private static final String ALIGN_SELF_CONSTRAINT = "flexbox-align-self";
    static final FlexAlignContent DEFAULT_ALIGN_CONTENT = FlexAlignContent.STRETCH;
    static final double DEFAULT_HGAP = 5, DEFAULT_VGAP = 5;

    /**
     * Set the <b>align-self</b> for node
     *
     * @param child the node to set
     * @param value the value
     */
    public static void setAlignSelf(Node child, FlexAlignItem value) {
        NodeUtils.setConstraint(child, ALIGN_SELF_CONSTRAINT, value);
    }

    /**
     * Returns <b>align-self</b> for node
     *
     * @param child the node to set
     */
    public static FlexAlignItem getAlignSelf(Node child) {
        return getAlignSelf(child, null);
    }

    /**
     * Returns <b>align-self</b> for node
     *
     * @param child the node to set
     */
    public static FlexAlignItem getAlignSelf(Node child, FlexAlignItem valueDefault) {
        return NodeUtils.getConstraint(child, ALIGN_SELF_CONSTRAINT, valueDefault);
    }

    /**
     * Set the order for node
     *
     * @param child the node to set
     * @param value the value
     */
    public static void setFlexOrder(Node child, Integer value) {
        NodeUtils.setConstraint(child, ORDER_CONSTRAINT, value);
    }

    /**
     * Returns <b>css: order</b> for node
     *
     * @param child the node to set
     */
    public static int getFlexOrder(Node child) {
        return getFlexOrder(child, 0);
    }

    /**
     * Returns <b>css: order</b> for node
     *
     * @param child the node to set
     */
    public static int getFlexOrder(Node child, int defaultValue) {
        return NodeUtils.getConstraint(child, ORDER_CONSTRAINT, defaultValue);
    }

    /**
     * Set the flex-grow for node
     *
     * @param child the node to set
     * @param value the value
     */
    public static void setFlexGrow(Node child, Integer value) {
        NodeUtils.setConstraint(child, GROW_CONSTRAINT, value);
    }

    /**
     * Returns <b>css: flex-grow</b> for node
     *
     * @param child the node to set
     */
    public static int getFlexGrow(Node child, Integer defaultGrow) {
        return NodeUtils.getConstraint(child, GROW_CONSTRAINT, defaultGrow);
    }

    /**
     * Set the flex-shrink for node
     *
     * @param child the node to set
     * @param value the value
     */
    public static void setFlexShrink(Node child, Integer value) {
        NodeUtils.setConstraint(child, SHRINK_CONSTRAINT, value);
    }

    /**
     * Returns <b>css: flex-shrink</b> for node
     *
     * @param child the node to set
     */
    public static int getFlexShrink(Node child, Integer defaultShrink) {
        return NodeUtils.getConstraint(child, SHRINK_CONSTRAINT, defaultShrink);
    }

    /**
     * Returns <b>css: flex-basic</b> for node
     *
     * @param child the node to set
     */
    public static String getFlexBasic(Node child) {
        return NodeUtils.getConstraint(child, FBASIC_CONSTRAINT);
    }

    /**
     * Set the flex-basic for node
     *
     * @param child the node to set
     * @param value the value
     */
    public static void setFlexBasic(Node child, String value) {
        NodeUtils.setConstraint(child, FBASIC_CONSTRAINT, value);
    }

    public static void setMargin(Node child, Insets margin) {
        NodeUtils.setConstraint(child, MARGIN_CONSTRAINT, margin);
    }

    public static void install(Node child, Flex flex) {
        if(flex == null) {
            setFlexShrink(child, null);
            setFlexGrow(child, null);
            setFlexBasic(child, null);
        }
        else {
            setFlexShrink(child, flex.getShrink());
            setFlexGrow(child, flex.getGrow());
            setFlexBasic(child, flex.getBasic());
        }

    }

    public static Insets getMargin(Node child) {
        return NodeUtils.getMargin(child, MARGIN_CONSTRAINT);
    }

    /**
     * Removes all anchor pane constraints from the child node.
     *
     * @param child the child node
     */
    public static void clearConstraints(Node child) {
        setMargin(child, null);
        setFlexShrink(child, null);
        setFlexGrow(child, null);
        setFlexOrder(child, null);
        setAlignSelf(child, null);
        setFlexBasic(child, null);
    }


    public static ObjectBinding<FlexDirection> createDirectionBinding(boolean reverse, ObservableValue<Orientation> orientationProperty) {
        return BindingBuilder.<FlexDirection>objectBuilder().setMapper(() -> {
                    Orientation orientation = orientationProperty.getValue();
                    if (reverse) return orientation == Orientation.HORIZONTAL ? FlexDirection.COLUMN : FlexDirection.ROW;
                    else return orientation == Orientation.HORIZONTAL ? FlexDirection.ROW : FlexDirection.COLUMN;
                })
                .addSources(orientationProperty).get();
    }

    public FlexBox() {
        this(-1, -1, new Node[0]);
    }

    public FlexBox(Node... children) {
        this(-1, -1, children);
    }

    public FlexBox(double gap, Node... children) {
        this(gap, gap, children);
    }

    public FlexBox(double hgap, double vgap) {
        this(hgap, vgap, new Node[0]);
    }

    public FlexBox(double hgap, double vgap, Node... children) {
        getStyleClass().setAll("flex");
        setHgap(hgap);setVgap(vgap);
        getChildren().setAll(children);
        setDirection(FlexDirection.ROW);
    }

    public void setConstraint(Integer flexGrow, Integer flexShrink, Integer flexOrder) {
        setFlexGrow(this, flexGrow);
        setFlexOrder(this, flexGrow);
        setFlexShrink(this, flexShrink);
    }

    public void removeChild(Node... nodes) {
        getChildren().removeAll(nodes);
    }

    public void addChild(Node childNode, int flexOrder) {
        setFlexOrder(childNode, flexOrder);
        addChild(childNode);
    }

    public void addFlexChild(Node childNode, Integer grow, Integer shrink, String basic) {
        setFlexGrow(childNode, grow);
        setFlexShrink(childNode, shrink);
        setFlexBasic(childNode, basic);
        addChild(childNode);
    }

    public void addChild(Node... nodes) {
        for (Node node : nodes) {
            if (node != null && !getChildren().contains(node)) {
                getChildren().add(node);
            }
        }
    }

    public void handleFocus(boolean flag) {
        setFocused(flag);
    }

    public void setVertical(boolean isVertical) {
        setDirection(isVertical ? FlexDirection.COLUMN : FlexDirection.ROW);
    }

    //@formatter:off

    /** flexDirection */
    private StyleableObjectProperty<FlexDirection> direction;
    public final FlexDirection getDirection() {return directionProperty().get();}
    public final void setDirection(FlexDirection value) {directionProperty().set(value);}
    public final StyleableObjectProperty<FlexDirection> directionProperty() {
        if(direction == null) {
            CssMetaData<FlexBox, FlexDirection> cssMetaData = FACTORY.getCssMetaData("-fx-flex-direction");
            direction = new SimpleStyleableObjectProperty<>(cssMetaData, this, "flexDirection"){

                public void set(FlexDirection v) {
                    super.set(v == null ?  FlexDirection.ROW : v);
                }

                @Override
                protected void invalidated() {
                    final boolean isVer = isVertical();
                    CssUtils.setPseudoVertical(FlexBox.this, isVer);
                    CssUtils.setPseudoHorizontal(FlexBox.this, !isVer);
                }
            };
        }
        return direction;
    }

    /** Sets the alignment of elements in the direction of the main axis*/
    private StyleableObjectProperty<FlexJustify> flexJustify;
    public final FlexJustify getFlexJustify() {return flexJustifyProperty().get();}
    public final void setFlexJustify(FlexJustify value) {flexJustifyProperty().set(value);}
    public final StyleableObjectProperty<FlexJustify> flexJustifyProperty() {
        if(flexJustify == null) {
            CssMetaData<FlexBox, FlexJustify> cssMetaData = FACTORY.getCssMetaData("-fx-justify-content");
            flexJustify = new SimpleStyleableObjectProperty<>(cssMetaData, this, "flexJustify", FlexJustify.FLEX_START){
                @Override
                public void set(FlexJustify v) {
                    super.set(v == null ? FlexJustify.FLEX_START : v);
                }
            };
        }
        return flexJustify;
    }

    /** alignItem */
    private StyleableObjectProperty<FlexAlignItem> alignItem;
    public final FlexAlignItem getAlignItem() {return alignItemProperty().get();}
    public final void setAlignItem(FlexAlignItem value) {alignItemProperty().set(value);}
    public final StyleableObjectProperty<FlexAlignItem> alignItemProperty() {
        if(alignItem == null) {
            CssMetaData<FlexBox, FlexAlignItem> cssMetaData = FACTORY.getCssMetaData("-fx-align-items");
            alignItem = new SimpleStyleableObjectProperty<>(cssMetaData, this, "alignItem", FlexAlignItem.FLEX_START){
                @Override
                public void set(FlexAlignItem v) {
                    super.set(v == null ? FlexAlignItem.FLEX_START : v);
                }
            };
        }
        return alignItem;
    }

    /** The css <b>align-content</b> property require <b>flex-wrap:[wrap | wrap-reverse]</b> */
    private StyleableObjectProperty<FlexAlignContent> alignContent;
    public final FlexAlignContent getAlignContent() {return alignContentProperty().get();}
    public final void setAlignContent(FlexAlignContent value) {alignContentProperty().set(value);}
    public final StyleableObjectProperty<FlexAlignContent> alignContentProperty() {
        if(alignContent == null) {
            CssMetaData<FlexBox, FlexAlignContent> cssMetaData = FACTORY.getCssMetaData("-fx-align-content");
            alignContent = new SimpleStyleableObjectProperty<>(cssMetaData, this, "alignContent", DEFAULT_ALIGN_CONTENT){
                @Override
                public void set(FlexAlignContent v) {
                    super.set(v == null ? DEFAULT_ALIGN_CONTENT : v);
                }
            };
        }
        return alignContent;
    }

    /** hgap */
    private StyleableDoubleProperty hgap;
    public final void setHgap(double value) {hgapProperty().set(value);}
    public final double getHgap() {
        double gap = hgapProperty().get();
        return gap < 0 ? DEFAULT_HGAP : gap;
    }
    public final StyleableDoubleProperty hgapProperty() {
        if(hgap == null) {
            CssMetaData<FlexBox, Number> cssMetaData = FACTORY.getCssMetaData("-fx-flex-hgap");
            hgap = new SimpleStyleableDoubleProperty(cssMetaData, this, "hgap", DEFAULT_HGAP);
        }
        return hgap;
    }

    /** vgap */
    private StyleableDoubleProperty vgap;
    public final void setVgap(double value) {vgapProperty().set(value);}
    public final double getVgap() {
        double gap = vgapProperty().get();
        return gap < 0 ? DEFAULT_VGAP : gap;
    }
    public final StyleableDoubleProperty vgapProperty() {
        if(vgap == null) {
            CssMetaData<FlexBox, Number> cssMetaData = FACTORY.getCssMetaData("-fx-flex-vgap");
            vgap = new SimpleStyleableDoubleProperty(cssMetaData, this, "vgap", DEFAULT_VGAP);
        }
        return vgap;
    }

    /** flexWrap */
    private StyleableObjectProperty<FlexWrap> flexWrap;
    public final FlexWrap getFlexWrap() {return flexWrapProperty().get();}
    public final void setFlexWrap(FlexWrap value) {flexWrapProperty().set(value);}
    public final StyleableObjectProperty<FlexWrap> flexWrapProperty() {
        if(flexWrap == null) {
            CssMetaData<FlexBox, FlexWrap> cssMetaData = FACTORY.getCssMetaData("-fx-flex-wrap");
            flexWrap = new SimpleStyleableObjectProperty<>(cssMetaData, this, "flexWrap", FlexWrap.NOWRAP) {
                @Override
                public void set(FlexWrap v) {
                    super.set(v == null ? FlexWrap.NOWRAP : v);
                }
            };
        }
        return flexWrap;
    }

    /** defaultFlexGrow */
    private StyleableIntegerProperty defaultFlexGrow;
    public final void setDefaultFlexGrow(int value) {defaultFlexGrowProperty().set(value);}
    public final int getDefaultFlexGrow() {return defaultFlexGrowProperty().get();}
    public final StyleableIntegerProperty defaultFlexGrowProperty() {
        if(defaultFlexGrow == null) {
            defaultFlexGrow = FACTORY.createInteger(this, "defaultFlexGrow", "-fx-default-flex-grow");
        }
        return defaultFlexGrow;
    }

    /** defaultFlexShrink */
    private StyleableIntegerProperty defaultFlexShrink;
    public final void setDefaultFlexShrink(int value) {defaultFlexShrinkProperty().set(value);}
    public final int getDefaultFlexShrink() {return defaultFlexShrinkProperty().get();}
    public final StyleableIntegerProperty defaultFlexShrinkProperty() {
        if(defaultFlexShrink == null) {
            defaultFlexShrink = FACTORY.createInteger(this, "defaultFlexShrink", "-fx-default-flex-shrink");
        }
        return defaultFlexShrink;
    }

    //@formatter:on

    /**
     * {@inheritDoc}
     */
    public List<CssMetaData<? extends Styleable, ?>> getCssMetaData() {
        return getClassCssMetaData();
    }

    /**
     * Gets the {@code CssMetaData} associated with this class, which may include the {@code CssMetaData} of its superclasses.
     *
     * @return the {@code CssMetaData}
     */
    public static List<CssMetaData<? extends Styleable, ?>> getClassCssMetaData() {
        return FACTORY.getCssMetaData();
    }

    /**
     * Methods for creating instances of StyleableProperty
     */
    static final CssFactory<FlexBox> FACTORY = new CssFactory<>(Pane.getClassCssMetaData()) {
        @Override
        protected void registerCss() {
            FACTORY.createSizeCss("-fx-flex-hgap", FlexBox::hgapProperty, DEFAULT_HGAP);
            FACTORY.createSizeCss("-fx-flex-vgap", FlexBox::vgapProperty, DEFAULT_VGAP);
            FACTORY.createEnumCss("-fx-flex-wrap", FlexBox::flexWrapProperty, FlexWrap.class, FlexWrap.NOWRAP);
            FACTORY.createEnumCss("-fx-flex-direction", FlexBox::directionProperty, FlexDirection.class, FlexDirection.ROW);
            FACTORY.createEnumCss("-fx-justify-content", FlexBox::flexJustifyProperty, FlexJustify.class, FlexJustify.FLEX_START);
            FACTORY.createEnumCss("-fx-align-items", FlexBox::alignItemProperty, FlexAlignItem.class, FlexAlignItem.FLEX_START);
            FACTORY.createEnumCss("-fx-align-content", FlexBox::alignContentProperty, FlexAlignContent.class, DEFAULT_ALIGN_CONTENT);
            FACTORY.createSizeCss("-fx-default-flex-grow", FlexBox::defaultFlexGrowProperty, 0);
            FACTORY.createSizeCss("-fx-default-flex-shrink", FlexBox::defaultFlexShrinkProperty, 1);
        }
    };

    @Override
    public void requestLayout() {
        if (!performingLayout && !computingRuns) {
            runs = null;
            super.requestLayout();
        }
    }

    @Override
    protected void layoutChildren() {
        try {
            performingLayout = true;
            final StringBuilder logBuilder = new StringBuilder();
            final Insets insets = getInsets();
            final double width = getWidth();
            final double height = getHeight();
            final double top = insets.getTop();
            final double left = insets.getLeft();
            final double bottom = insets.getBottom();
            final double right = insets.getRight();
            final double insideWidth = width - left - right;
            final double insideHeight = height - top - bottom;

            final boolean isVertical = isVertical();
            final double maxLength = isVertical ? insideHeight : insideWidth;

            Runs runs = computeRuns(maxLength);

            // align-content
            applyFlexAlignContent(insideWidth, insideHeight, runs);

            for (int i = 0, max = runs.size(); i < max; i++) {
                RunNode run = runs.get(i);

                if (getDirection().isReverse()) {
                    run = run.reverseNode();
                }

                applyFlexJustify(insideWidth, insideHeight, run);
                applyAlignItem(insideWidth, insideHeight, run);

                // flex-grow
                applyFlexGrow(insideWidth, insideHeight, run);
                applyFlexShrink(insideWidth, insideHeight, run);

                final double xOffsetH = isVertical ? runs.computeContentWidth() : run.areaWidth();
                final double yOffsetH = isVertical ? run.areaHeight() : runs.computeContentHeight();
                final double xOffset = left + NodeUtils.computeXOffset(insideWidth, xOffsetH, run.hAlign());
                final double yOffset = top + NodeUtils.computeYOffset(insideHeight, yOffsetH, run.vAlign());

                int pos = 1;
                for (LayoutRect nodeRect : run) {
                    final double x = xOffset + nodeRect.layoutX();
                    final double y = yOffset + nodeRect.layoutY();
                    final double areaWidth = snapSpaceX(isVertical ? run.areaWidth() : nodeRect.layoutWidth());
                    final double areaHeight = snapSizeY(isVertical ? nodeRect.layoutHeight() : run.areaHeight());

                    layoutInArea(nodeRect.node(), x, y, areaWidth, areaHeight,
                            run.baselineOffset(), nodeRect.getMargin(),
                            nodeRect.setFillWidth(), nodeRect.fillHeight(),
                            nodeRect.hAlign(), nodeRect.vAlign());

                    if(getStyleClass().contains("line-group2")) {
                        Bounds b = nodeRect.node().getBoundsInParent();
                        log.warn("{}::{}", nodeRect.node().getStyleClass(), b.getMinX());
                    }




                }

            }

        } //
        finally {
            performingLayout = false;
            //log.warn("{}", sb);
        }

    }

    @Override
    protected double computePrefWidth(double height) {
        final double prefWidth = computeRuns().computeContentWidth();
        return snapSpaceX(prefWidth + getInsets().getLeft() + getInsets().getRight());
    }

    protected double computePrefHeight(double width) {
        final double prefHeight = computeRuns().computeContentHeight();
        return snapSpaceY(prefHeight + snappedTopInset() + snappedBottomInset());
    }

    @Override
    protected double computeMinWidth(double height) {
        final double minWidth = computeRuns().computeRunsMinWidth(height);
        return snapSpaceX(minWidth + getInsets().getLeft() + getInsets().getRight());
    }

    @Override
    protected double computeMinHeight(double width) {
        final double minHeight = computeRuns().computeRunsMinHeight(width);
        return snapSizeY(minHeight) + getInsets().getTop() + getInsets().getBottom();
    }

    //==================================
    private boolean performingLayout = false;
    private boolean computingRuns = false;
    private double lastRunLength = 0;
    private Runs runs = null;

    public int getFlexShrinkOrDefault(Node node) {
        return getFlexShrink(node, getDefaultFlexShrink());
    }

    public int getFlexGrowOrDefault(Node node) {
        return getFlexGrow(node, getDefaultFlexGrow());
    }

    public boolean isVertical() {
        return getDirection().isVertical();
    }

    public double snapSpaceHgap() {
        return snapSpaceX(getHgap());
    }

    public double snapSpaceVgap() {
        return snapSpaceY(getVgap());
    }

    private Runs computeRuns() {
        final boolean isVertical = isVertical();
        final double insideWidth = computeInsideWidth();
        final double insideHeight = computeInsideHeight();
        return computeRuns(isVertical ? insideHeight : insideWidth);
    }

    private double computeInsideHeight() {
        final Insets insets = getInsets();
        return getWidth() - insets.getLeft() - insets.getRight();
    }

    private double computeInsideWidth() {
        final Insets insets = getInsets();
        return getHeight() - insets.getTop() - insets.getBottom();
    }

    private Runs computeRuns(double maxRunLength) {
        //log.warn("computeRuns: {}", getWidth());

        if (runs == null || lastRunLength != maxRunLength) {
            lastRunLength = maxRunLength;
            computingRuns = true;

            runs = new Runs();

            final double vgap = snapSpaceVgap(), hgap = snapSpaceHgap();
            final boolean isVertical = isVertical();
            final boolean isWrap = getFlexWrap().isWrap();

            double runLength = 0, runOffset = 0;

            RunNode rowNode = new RunNode();

            List<Node> children = new ArrayList<>(getManagedChildren());

            // sort
            children.sort(Comparator.comparingInt(FlexBox::getFlexOrder));

            for (Node child : children) {
                final LayoutRect nodeRect = new LayoutRect(child);
                final double originWidth = nodeRect.computeOriginWidth();
                final double originHeight = nodeRect.computeOriginHeight();
                final double nodeLength = nodeRect.getNodeLength();

                //nodeRect.computeOriginMinWidth();
                // nodeRect.computeOriginMinHeight();

                if (isWrap && runLength > 0 && runLength + nodeLength > maxRunLength) {
                    normalizeRun(rowNode, runOffset);
                    runOffset += rowNode.getRunOffset(hgap, vgap);
                    runs.add(rowNode);
                    runLength = 0;
                    rowNode = new RunNode();
                }

                if (isVertical) {
                    nodeRect.setOriginY(runLength);
                    runLength += originHeight + vgap;
                } else {
                    nodeRect.setOriginX(runLength);
                    runLength += originWidth + hgap;
                }

                rowNode.add(nodeRect);
            }

            // insert last run
            normalizeRun(rowNode, runOffset);
            runs.add(rowNode);
            computingRuns = false;
        }

        return runs;
    }

    private void normalizeRun(final RunNode run, double runOffset) {
        final int totalRectSub = run.size() - 1;
        if (isVertical()) {
            run.setOriginHeight(totalRectSub * snapSpaceVgap());
            //run.setOriginMinHeight(totalRectSub * snapSpaceVgap());

            double maxWidth = 0, minWidth = 0;
            for (LayoutRect nodeRect : run) {
                nodeRect.setOriginX(runOffset);

                run.plusOriginHeight(nodeRect.originHeight());
                // run.plusOriginMinHeight(nodeRect.originMinHeight());

                maxWidth = Math.max(maxWidth, nodeRect.originWidth());
                //minWidth = Math.max(maxWidth, nodeRect.originMinWidth());
            }

            run.setOriginWidth(maxWidth);
            // run.setOriginMinWidth(minWidth);
            run.baselineOffset(run.originHeight());
        } //
        else {
            ArrayList<Node> rowNodes = new ArrayList();
            run.setOriginWidth(totalRectSub * snapSpaceHgap());
            // run.setOriginMinWidth(totalRectSub * snapSpaceHgap());

            for (LayoutRect nodeRect : run) {
                rowNodes.add(nodeRect.node());
                nodeRect.setOriginY(runOffset);

                run.plusOriginWidth(nodeRect.originWidth());
                // run.plusOriginMinWidth(nodeRect.originMinWidth());

            }

            run.setOriginHeight(NodeUtils.computeMaxPrefAreaHeight(FlexBox.this, rowNodes, FlexBox::getMargin, VPos.TOP));
            //run.setOriginMinHeight(NodeUtils.computeMaxMinAreaHeight(FlexBox.this, rowNodes, FlexBox::getMargin, VPos.TOP));
            run.baselineOffset(0); //NodeUtils.getAreaBaselineOffset(rowNodes, marginAccessor, i -> run.getNodeRect(i).width, run.originHeight(), true);
        }
    }

    /**
     * apply css `align-content`
     */
    private void applyFlexAlignContent(double insideWidth, double insideHeight, Runs runs) {
        // if (!getFlexWrap().isWrap() ) return;

        final FlexAlignContent alignContent = getAlignContent();
        final boolean isVertical = isVertical();
        final double emptyLength = runs.calcLengthForAlignContent(insideWidth, insideHeight);
        final double vgap = snapSpaceVgap(), hgap = snapSpaceHgap();
        final int totalNode = runs.size();

        double spacing = 0, offset = 0;
        boolean isStretch = false;

        switch (alignContent) {
            case FLEX_START -> {
                spacing = offset = 0;
            }
            case FLEX_END -> {
                offset = emptyLength;
            }
            case CENTER -> {
                offset = emptyLength / 2;
            }
            case SPACE_BETWEEN -> {
                spacing = emptyLength / (totalNode - 1);
                offset = 0;
            }
            case SPACE_AROUND -> {
                spacing = emptyLength / totalNode;
                offset = spacing / 2;
            }
            case SPACE_EVENLY -> {
                spacing = emptyLength / (totalNode + 1);
                offset = spacing;
            }
            case STRETCH -> {
                spacing = emptyLength / totalNode;
                offset = 0;

                for (RunNode run : runs) {
                    if (isVertical) {
                        final double width = run.originWidth() + spacing;
                        for (LayoutRect nodeRect : run) nodeRect.layoutWidth(width);
                        //log.warn("applyFlexAlignContent -> setAreaWidth: {}", width);
                        run.areaWidth(width);
                    } //
                    else {
                        final double height = run.originHeight() + spacing;
                        for (LayoutRect nodeRect : run) nodeRect.layoutHeight(height);
                        run.areaHeight(height);
                    }
                }
            }
        }

        if (isVertical) {
            for (RunNode run : runs) {
                for (LayoutRect nodeRect : run) nodeRect.layoutX(offset);
                offset += hgap + spacing + run.originWidth();
            }
        } //
        else {
            for (RunNode run : runs) {
                for (LayoutRect nodeRect : run) nodeRect.layoutY(offset);
                offset += (vgap + spacing + run.originHeight());
            }
        }

    }

    /**
     * apply css `justify-content`
     */
    private void applyFlexJustify(double insideWidth, double insideHeight, RunNode run) {
        final FlexJustify flexJustify = getFlexJustify();
        final double vgap = snapSpaceVgap(), hgap = snapSpaceHgap(), sizeSub = run.size() - 1;
        final boolean isReverse = getDirection().isReverse(), isVertical = isVertical();
        final double emptyLength = run.computeLengthBlank(isVertical, insideWidth, insideHeight);

        FlexJustify copyJustify = switch (flexJustify) {
            case FLEX_START -> isReverse ? FlexJustify.FLEX_END : FlexJustify.FLEX_START;
            case FLEX_END -> isReverse ? FlexJustify.FLEX_START : FlexJustify.FLEX_END;
            case CENTER -> FlexJustify.CENTER;
            case SPACE_BETWEEN -> FlexJustify.SPACE_BETWEEN;
            case SPACE_AROUND -> FlexJustify.SPACE_AROUND;
            case SPACE_EVENLY -> FlexJustify.SPACE_EVENLY;
        };

        double spacing = 0, offset = 0;

        switch (copyJustify) {
            case FLEX_START -> {
                spacing = 0;
                offset = 0;
            }
            case FLEX_END -> {
                offset = emptyLength;
            }
            case CENTER -> {
                offset = emptyLength / 2;
            }
            case SPACE_BETWEEN -> {
                spacing = emptyLength / (run.size() - 1);
                offset = 0;
            }
            case SPACE_AROUND -> {
                spacing = emptyLength / run.size();
                offset = spacing / 2;
            }
            case SPACE_EVENLY -> {
                spacing = emptyLength / (run.size() + 1);
                offset = spacing;
            }
        }

        for (LayoutRect nodeRect : run) {
            if (isVertical) {
                nodeRect.layoutY(offset);
                offset += nodeRect.originHeight() + spacing + vgap;
            } else {
                nodeRect.layoutX(offset);
                offset += nodeRect.originWidth() + spacing + hgap;
            }
        }
    }

    /**
     * apply css `align-item` or `align-self`
     */
    private void applyAlignItem(double insideWidth, double insideHeight, RunNode run) {

        Function<FlexAlignItem, Pos> getAlignItem = align -> switch (align) {
            case FLEX_START -> Pos.TOP_LEFT;
            case FLEX_END -> Pos.BOTTOM_RIGHT;
            case CENTER -> Pos.CENTER;
            case STRETCH -> null;
        };

        //---
        final FlexAlignItem alignItem = getAlignItem();
        final double vgap = snapSpaceVgap(), hgap = snapSpaceHgap();
        final boolean isVertical = isVertical();

        for (LayoutRect nodeRect : run) {
            final FlexAlignItem align = getAlignSelf(nodeRect.node(), alignItem);
            final Pos pos = getAlignItem.apply(align);
            if (isVertical) {
                final double width = pos == null ? run.areaWidth() : nodeRect.originWidth();
                nodeRect.hAlign(pos == null ? HPos.LEFT : pos.getHpos());
                nodeRect.setFillWidth(pos == null);
                //nodeRect.fillHeight(false);
                nodeRect.layoutWidth(width);
            }//
            else {
                final double height = pos == null ? run.areaHeight() : nodeRect.originHeight();
                nodeRect.vAlign(pos == null ? VPos.TOP : pos.getVpos());
                //nodeRect.fillWidth(false);
                nodeRect.fillHeight(pos == null);
                nodeRect.layoutHeight(height);
            }
        }

    }

    /**
     * apply css flex-grow
     */
    private void applyFlexGrow(double insideWidth, double insideHeight, RunNode run) {
        if (!run.existsGrow()) return;

        final double totalGrow = run.computeTotalGrow();
        final double insideLength = isVertical() ? insideHeight : insideWidth;
        final double originLength = isVertical() ? run.originHeight() : run.originWidth();

        if (totalGrow > 0 && insideLength > originLength) {
            final boolean isVertical = isVertical();
            final double remaining = run.computeLengthBlank(isVertical, insideWidth, insideHeight);
            final double gap = isVertical ? snapSpaceVgap() : snapSpaceHgap();

            //log.warn("applyFlexGrow: remaining -> {}", remaining);

            double offset = 0;

            for (LayoutRect nodeRect : run) {
                final double nodeGrow = getFlexGrowOrDefault(nodeRect.node()), offsetFinal = offset;
                final double growFactory = (nodeGrow / totalGrow) * remaining;
                final double nodeLength = isVertical ? nodeRect.originHeight() : nodeRect.originWidth();
                final boolean hasSetGrow = nodeGrow > 0;

                double growLength = growFactory + nodeLength;

                offset += (hasSetGrow ? growLength : nodeLength) + gap;


                if (isVertical) {
                    nodeRect.layoutY(offsetFinal);
                    if (hasSetGrow) {
                        nodeRect.layoutHeight(growLength);
                        nodeRect.fillHeight(true);
                    }
                } //
                else {
                    nodeRect.layoutX(offsetFinal);
                    if (hasSetGrow) {
                        nodeRect.layoutWidth(growLength);
                        nodeRect.setFillWidth(true);
                    }
                }
            }
        }
    }

    /**
     * apply css flex-shrink
     */
    private void applyFlexShrink(double containerWidth, double containerHeight, RunNode run) {
        if (!run.existsShrink()) return;

        final double totalShrink = run.computeTotalShrink();
        final double containerLength = isVertical() ? containerHeight : containerWidth;
        final double originLength = isVertical() ? run.originHeight() : run.originWidth();

        if (totalShrink > 0 && containerLength < originLength) {
            final double remaining = containerLength - originLength;
            final boolean isVertical = isVertical();
            final double gap = isVertical ? snapSpaceVgap() : snapSpaceHgap();

            final double total = run.parallelStream()
                    .mapToDouble(nr -> nr.getShrink() * nr.getNodeLength()).sum();

            double offset = 0, i = 0;
            for (LayoutRect nodeRect : run) {
                final double shrink = getFlexShrinkOrDefault(nodeRect.node());
                final double nodeLen = isVertical ? nodeRect.originHeight() : nodeRect.originWidth();

                final double shrinkFactory = ((shrink * nodeLen) / total) * remaining;
                final double offsetFinal = offset;
                final boolean hasSetShrink = shrink > 0;

                double shrinkLen = shrinkFactory + nodeLen;

                if (nodeRect.node() instanceof Region region) {
                    final double minLength = isVertical ? region.minHeight(-1) : region.minWidth(-1);
                    if (shrinkLen < minLength) shrinkLen = minLength;
                }

                offset += (hasSetShrink ? shrinkLen : nodeLen) + gap;

                if (isVertical) {
                    nodeRect.layoutY(offsetFinal);
                    if (hasSetShrink) nodeRect.layoutHeight(shrinkLen);
                } //
                else {
                    nodeRect.layoutX(offsetFinal);
                    if (hasSetShrink) nodeRect.layoutWidth(shrinkLen);
                }
            }

        }

    }

    /**
     * Try to add or remove node
     * <li>add: if {@link Pane#getChildren()} not empty</li>
     * <li>remove: if {@link Pane#getChildren()} is empty</li>
     *
     * @return state add or remove (remove = -1, add = 1, notAdd = 0)
     */
    public int addOrRemoveChild(Pane pane) {
        return addOrRemoveChild(pane, !pane.getChildren().isEmpty());
    }

    /**
     * Try to add or remove node
     * <li>add: if <b>hasAdd = true</b></li>
     * <li>remove: if <b>hasAdd = false</b></li>
     *
     * @return state add or remove (remove = -1, add = 1, notAdd = 0)
     */
    public int addOrRemoveChild(Node child, boolean hasAdd) {
        if (!hasAdd) {
            removeChild(child);
            return -1;
        } else if (!getChildren().contains(child)) {
            getChildren().add(child);
            return 1;
        } else return 0;
    }


    protected class Runs extends ArrayList<RunNode> {

        public double computeContentHeight() {
            final boolean isVertical = isVertical();
            double cHeight = isVertical ? 0 : (size() - 1) * snapSpaceVgap();
            for (RunNode run : this) {
                if (isVertical) cHeight = Math.max(cHeight, run.originHeight());
                else cHeight += run.originHeight();
            }
            return cHeight;
        }

        public double computeContentWidth() {
            final boolean isVertical = isVertical();
            final double totalRect = size() - 1;
            double cWidth = !isVertical ? 0 : totalRect * snapSpaceHgap();
            for (RunNode run : this) {
                if (isVertical) cWidth += run.originWidth();
                else cWidth = Math.max(cWidth, run.originWidth());
            }
            return cWidth;
        }


        public double computeRunsMinHeight(double width) {
            final boolean isVer = isVertical();
            final double totalSize = size() - 1;
            double cHeight = isVer ? 0 : totalSize * snapSpaceVgap();
            for (RunNode run : this) {
                final double minSize = run.computeRunMinHeight(width);
                cHeight = isVer ? Math.max(cHeight, minSize) : cHeight + minSize;
            }
            return cHeight;
        }


        /**
         * compute minWidth for runs
         */
        public double computeRunsMinWidth(double height) {
            final boolean isVer = isVertical();
            final double totalSize = size() - 1;

            double minWidth = isVer ? (snapSpaceHgap() * totalSize) : 0;
            for (RunNode run : this) {
                final double minSize = run.computeRunMinWidth(height);
                minWidth = isVer ? (minWidth + minSize) : Math.max(minWidth, minSize);
            }

            return minWidth;
        }


        public double calcLengthForAlignContent(double insideWidth, double insideHeight) {
            return isVertical() ? insideWidth - computeContentWidth() : insideHeight - computeContentHeight();
        }

    }

    protected class RunNode extends ArrayList<LayoutRect> {
        private double areaWidth, areaHeight;
        private double originWidth, originHeight;

        private double baselineOffset;
        private VPos vAlign = VPos.TOP;
        private HPos hAlign = HPos.LEFT;

        public RunNode() {
        }

        public RunNode(Collection<? extends LayoutRect> c) {
            super(c);
        }

        public void setOriginWidth(double width) {
            this.originWidth = width;
            this.areaWidth = width;
        }

        public void setOriginHeight(double height) {
            this.originHeight = height;
            this.areaHeight = height;
        }

        public void baselineOffset(double baselineOffset) {
            this.baselineOffset = baselineOffset;
        }

        public RunNode areaWidth(double areaWidth) {
            this.areaWidth = areaWidth;
            return this;
        }

        public RunNode areaHeight(double areaHeight) {
            this.areaHeight = areaHeight;
            return this;
        }

        public RunNode vAlign(VPos vAlign) {
            this.vAlign = vAlign;
            return this;
        }

        public RunNode hAlign(HPos hAlign) {
            this.hAlign = hAlign;
            return this;
        }

        public double areaWidth() {
            return areaWidth;
        }

        public double areaHeight() {
            return areaHeight;
        }

        public VPos vAlign() {
            return vAlign;
        }

        public HPos hAlign() {
            return hAlign;
        }

        public double baselineOffset() {
            return baselineOffset;
        }

        public double originWidth() {
            return originWidth;
        }

        public double originHeight() {
            return originHeight;
        }

        public double getRunOffset(double hgap, double vgap) {
            return isVertical() ? originWidth + hgap : originHeight + vgap;
        }

        public void plusOriginHeight(double height) {
            setOriginHeight(originHeight < 0 ? height : originHeight + height);
        }

        public void plusOriginWidth(double width) {
            setOriginWidth(originWidth < 0 ? width : originWidth + width);
        }

        public RunNode reverseNode() {
            RunNode node = new RunNode(this);
            Collections.reverse(node);
            return node;
        }

        public int computeTotalGrow() {
            return parallelStream().mapToInt(n -> getFlexGrowOrDefault(n.node())).sum();
        }

        public int computeTotalShrink() {
            return parallelStream().mapToInt(n -> getFlexShrinkOrDefault(n.node())).sum();
        }

        public double computeLengthBlank(boolean isVertical, double insideWidth, double insideHeight) {
            return isVertical ? insideHeight - originHeight : insideWidth - originWidth;
        }

        public double sumPreWidth() {
            return parallelStream().mapToDouble(rect -> rect.node.prefWidth(-1)).sum();
        }

        public boolean existsGrow() {
            return getDefaultFlexGrow() > 0 || (stream().anyMatch(lr -> lr.getGrow() > 0));
        }

        public boolean existsShrink() {
            return getDefaultFlexShrink() > 0 || (stream().anyMatch(lr -> lr.getShrink() > 0));
        }

        public double computeRunMinWidth(double height) {
            boolean isVer = isVertical();
            double minWidth = isVer ? 0 : snapSpaceHgap() * (size() - 1);
            boolean isLog = FlexBox.this.getStyleClass().contains("center-box");

            for (LayoutRect lrect : this) {
                final double minSize = lrect.minWidth(height);
                minWidth = isVer ? Math.max(minWidth, minSize) : minWidth + minSize;
            }
            return minWidth;
        }

        public double computeRunMinHeight(double width) {
            boolean isVer = isVertical();
            double minHeight = isVer ? snapSpaceVgap() * (size() - 1) : 0;
            for (LayoutRect lRect : this) {
                final double minSize = lRect.minHeight(width);
                minHeight = isVer ? (minHeight + minSize) : Math.max(minHeight, minSize);
            }

            return minHeight;
        }


    }

    @ToString
    protected class LayoutRect {
        private final Node node;
        private double originWidth, originHeight;

        private double originX, originY;
        private double layoutWidth = -1, layoutHeight = -1;
        private double layoutX, layoutY;
        private VPos vAlign = VPos.TOP;
        private HPos hAlign = HPos.LEFT;

        BooleanProperty fillWidth = new SimpleBooleanProperty(false) {
            protected void invalidated() {
                maxWidth(get());
            }
        };

        BooleanProperty fillHeight = new SimpleBooleanProperty(false) {
            protected void invalidated() {
                maxHeight(get());
            }
        };

        protected LayoutRect(Node node) {
            this.node = node;
            maxWidth(false);
            maxHeight(false);
        }

        public void maxWidth(boolean useMaxDouble) {
            if (node instanceof Region region) {
                region.setMaxWidth(useMaxDouble ? Double.MAX_VALUE : Region.USE_COMPUTED_SIZE);
            }
        }

        public void maxHeight(boolean useMaxDouble) {
            if (node instanceof Region region) {
                region.setMaxHeight(useMaxDouble ? Double.MAX_VALUE : Region.USE_COMPUTED_SIZE);
            }
        }

        public void setOriginWidth(double width) {
            this.originWidth = width;
            this.layoutWidth = width;
        }

        public void setOriginHeight(double height) {
            this.originHeight = height;
            this.layoutHeight = height;
        }

        public void setOriginX(double offset) {
            this.originX = offset;
            this.layoutX = offset;
        }

        public void setOriginY(double offset) {
            this.originY = offset;
            this.layoutY = offset;
        }

        public LayoutRect originX(double originX) {
            this.originX = originX;
            return this;
        }

        public LayoutRect originY(double originY) {
            this.originY = originY;
            return this;
        }

        public LayoutRect layoutWidth(double layoutWidth) {
            this.layoutWidth = layoutWidth;
            return this;
        }

        public LayoutRect layoutHeight(double layoutHeight) {
            this.layoutHeight = layoutHeight;
            return this;
        }

        public LayoutRect layoutX(double layoutX) {
            this.layoutX = layoutX;
            return this;
        }

        public LayoutRect layoutY(double layoutY) {
            this.layoutY = layoutY;
            return this;
        }

        public LayoutRect setFillWidth(boolean flag) {
            fillWidth.set(flag);
            return this;
        }

        public LayoutRect fillHeight(boolean flag) {
            fillHeight.set(flag);
            return this;
        }

        public LayoutRect vAlign(VPos vAlign) {
            this.vAlign = vAlign;
            return this;
        }

        public LayoutRect hAlign(HPos hAlign) {
            this.hAlign = hAlign;
            return this;
        }

        public double prefWidth(double height) {
            return node.prefWidth(height);
        }

        public double prefHeight(double width) {
            return node.prefHeight(width);
        }

        public double minWidth(double height) {
            return node.minWidth(height);
        }

        public double minHeight(double width) {
            return node.minHeight(width);
        }

        public double maxWidth(double height) {
            return node.maxWidth(height);
        }

        public double maxHeight(double width) {
            return node.maxHeight(width);
        }

        public Node node() {
            return node;
        }

        public double originWidth() {
            return originWidth;
        }

        public double originHeight() {
            return originHeight;
        }

        public double originX() {
            return originX;
        }

        public double originY() {
            return originY;
        }

        public double layoutWidth() {
            return layoutWidth;
        }

        public double layoutHeight() {
            return layoutHeight;
        }

        public double layoutX() {
            return layoutX;
        }

        public double layoutY() {
            return layoutY;
        }

        public boolean setFillWidth() {
            return fillWidth.get();
        }

        public boolean fillHeight() {
            return fillHeight.get();
        }

        public VPos vAlign() {
            return vAlign;
        }

        public HPos hAlign() {
            return hAlign;
        }


        public Insets getMargin() {
            return FlexBox.getMargin(node);
        }

        public double computeOriginWidth() {
            setOriginWidth(NodeUtils.computeChildPrefAreaWidth(FlexBox.this, node, getMargin()));
            return originWidth;
        }

        public double computeOriginHeight() {
            setOriginHeight(NodeUtils.computeChildPrefAreaHeight(FlexBox.this, node, getMargin()));
            return originHeight;
        }

        public double getNodeLength() {
            return isVertical() ? originHeight() : originWidth();
        }

        public double getShrink() {
            return getFlexShrinkOrDefault(node);
        }

        public double getGrow() {
            return getFlexGrowOrDefault(node);
        }

        public boolean containsStyleClass(String styleClass) {
            return node.getStyleClass().contains(styleClass);
        }
    }

}