package vn.conyeu.javafx.styleable;

import javafx.beans.property.Property;
import javafx.css.*;
import javafx.css.converter.EnumConverter;
import javafx.geometry.Insets;
import javafx.scene.effect.Effect;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.util.Duration;
import javafx.util.Pair;
import javafx.util.StringConverter;
import lombok.extern.slf4j.Slf4j;
import vn.conyeu.commons.utils.Asserts;
import vn.conyeu.javafx.styleable.converter.FlexConverter;
import vn.conyeu.javafx.styleable.simple.Flex;

import java.lang.ref.WeakReference;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;

@Slf4j
public abstract class CssFactory<S extends Styleable> {
    private final Map<String, Pair<Class, CssMetaData>> metaDataMap;
    private final List<CssMetaData<? extends Styleable, ?>> unmodifiableMetaDataList;
    private final List<CssMetaData<? extends Styleable, ?>> metaDataList;
    private boolean hasRegisterCssMeta = false;

    /**
     * The constructor is passed the CssMetaData of the parent class of &lt;S&gt;, typically by calling the
     * static <code>getClassCssMetaData()</code> method of the parent.
     *
     * @param parentCssMetaData The CssMetaData of the parent class of &lt;S&gt;, or null.
     */
    public CssFactory(List<CssMetaData<? extends Styleable, ?>> parentCssMetaData) {
        this.metaDataList = new ArrayList<>();
        this.metaDataMap = new HashMap<>();
        this.unmodifiableMetaDataList = Collections.unmodifiableList(this.metaDataList);
        if (parentCssMetaData != null) this.metaDataList.addAll(parentCssMetaData);
    }


    /**
     * Get the CssMetaData for the given Styleable.
     *
     * @return the CssMetaData for the given Styleable
     */
    public final List<CssMetaData<? extends Styleable, ?>> getCssMetaData() {
        runStaticApply();
        return unmodifiableMetaDataList;
    }

    ////////////////////////////////StyleableBooleanProperty///////////////////////////////////////////

    /**
     * Create a StyleableBooleanProperty  with initial value and inherit flag.
     *
     * @param styleable    The <code>this</code> reference of the returned property. This is also the property bean.
     * @param propertyName The field name of the StyleableBooleanProperty
     * @param cssProperty  The CSS property name
     * @param function     A function that returns the StyleableBooleanProperty  that was created by this method call.
     * @param initialValue The initial value of the property. CSS may reset the property to this value.
     * @param inherits     Whether the CSS style can be inherited by child nodes
     * @return a StyleableProperty created with initial value and inherit flag
     */
    public final StyleableBooleanProperty createBoolean(S styleable, String propertyName, String cssProperty,
                                                        Function<S, StyleableProperty<Boolean>> function, boolean initialValue, boolean inherits) {
        CssMetaData<S, Boolean> cssMetaData = createBooleanCss(cssProperty, function, initialValue, inherits);
        return new SimpleStyleableBooleanProperty(cssMetaData, styleable, propertyName, initialValue);
    }

    /**
     * Create a StyleableBooleanProperty  with initial value. The inherit flag defaults to false.
     *
     * @param styleable    The <code>this</code> reference of the returned property. This is also the property bean.
     * @param propertyName The field name of the StyleableBooleanProperty
     * @param cssProperty  The CSS property name
     * @param function     A function that returns the StyleableBooleanProperty  that was created by this method call.
     * @param initialValue The initial value of the property. CSS may reset the property to this value.
     * @return a StyleableProperty created with initial value
     */
    public final StyleableBooleanProperty createBoolean(S styleable, String propertyName, String cssProperty,
                                                        Function<S, StyleableProperty<Boolean>> function, boolean initialValue) {
        return createBoolean(styleable, propertyName, cssProperty, function, initialValue, false);
    }

    /**
     * Create a StyleableBooleanProperty . The initialValue and inherit flag default to false.
     *
     * @param styleable    The <code>this</code> reference of the returned property. This is also the property bean.
     * @param propertyName The field name of the StyleableBooleanProperty
     * @param cssProperty  The CSS property name
     * @param function     A function that returns the StyleableBooleanProperty  that was created by this method call.
     * @return a StyleableProperty created with default initialValue and inherit flag
     */
    public final StyleableBooleanProperty createBoolean(S styleable, String propertyName, String cssProperty,
                                                        Function<S, StyleableProperty<Boolean>> function) {
        return createBoolean(styleable, propertyName, cssProperty, function, false, false);
    }


    /**
     * Create a StyleableBooleanProperty  using previously created CssMetaData for the given <code>cssProperty</code>.
     *
     * @param styleable    The <code>this</code> reference of the returned property. This is also the property bean.
     * @param propertyName The field name of the StyleableBooleanProperty
     * @param cssProperty  The CSS property name
     * @return a StyleableProperty created using previously created CssMetaData
     * @throws IllegalArgumentException if <code>cssProperty</code> is null or empty
     * @throws NoSuchElementException   if the CssMetaData for <code>cssProperty</code> was not created prior to this method invocation
     */
    public final StyleableBooleanProperty createBoolean(S styleable, String propertyName, String cssProperty) {
        return createBoolean(styleable, propertyName, cssProperty, (CssInvalidated<Boolean>) null);
    }

    public final StyleableBooleanProperty createBoolean(S styleable, String propertyName, String cssProperty, CssInvalidated<Boolean> invalidated) {
        checkNotNullCssProperty(cssProperty);
        CssMetaData<S, Boolean> cssMetaData = getCssMetaData(Boolean.class, cssProperty);
        return new SimpleStyleableBooleanProperty(cssMetaData, styleable, propertyName, cssMetaData.getInitialValue(styleable)) {
            private WeakReference<Boolean> weakReference = new WeakReference<>(null);

            @Override
            protected void invalidated() {
                Boolean old = weakReference.get();
                acceptValue(cssMetaData, invalidated, old, get());
                weakReference = new WeakReference<>(get());
            }
        };
    }
    /**
     * Create a StyleableBooleanProperty  using previously created CssMetaData for the given <code>cssProperty</code>.
     *
     * @param styleable    The <code>this</code> reference of the returned property. This is also the property bean.
     * @param propertyName The field name of the StyleableBooleanProperty
     * @return a StyleableProperty created using previously created CssMetaData
     * @throws IllegalArgumentException if <code>cssProperty</code> is null or empty
     * @throws NoSuchElementException   if the CssMetaData for <code>cssProperty</code> was not created prior to this method invocation
     */
    protected final StyleableBooleanProperty createBoolean(S styleable, String propertyName, CssMetaData<S, Boolean> cssMetaData, Boolean initialValue, CssInvalidated<Boolean> invalidated) {
        return new SimpleStyleableBooleanProperty(cssMetaData, styleable, propertyName, initialValue) {
            private WeakReference<Boolean> weakReference = new WeakReference<>(null);

            @Override
            protected void invalidated() {
                Boolean old = weakReference.get();
                acceptValue(cssMetaData, invalidated, old, get());
                weakReference = new WeakReference<>(get());
            }
        };
    }


    private void checkNotNullCssProperty(String cssProperty) {
        Asserts.notBlank(cssProperty, "cssProperty cannot be null or empty string");
    }

    //============================= create StyleableProperty<Color> =============================//

    /**
     * Create a StyleableProperty&lt;Color&gt; with initial value and inherit flag.
     *
     * @param styleable    The <code>this</code> reference of the returned property. This is also the property bean.
     * @param propertyName The field name of the StyleableProperty&lt;Color&gt;
     * @param cssProperty  The CSS property name
     * @param function     A function that returns the StyleableProperty&lt;Color&gt; that was created by this method call.
     * @param initialValue The initial value of the property. CSS may reset the property to this value.
     * @param inherits     Whether the CSS style can be inherited by child nodes
     * @return a StyleableProperty created with initial value and inherit flag
     */
    public final StyleableProperty<Color> createColor(S styleable, String propertyName, String cssProperty,
                                                      Function<S, StyleableProperty<Color>> function, Color initialValue, boolean inherits) {
        CssMetaData<S, Color> cssMetaData = createColorCss(cssProperty, function, initialValue, inherits);
        return new SimpleStyleableObjectProperty<>(cssMetaData, styleable, propertyName, initialValue);
    }

    /**
     * Create a StyleableProperty&lt;Color&gt; with initial value. The inherit flag defaults to false.
     *
     * @param styleable    The <code>this</code> reference of the returned property. This is also the property bean.
     * @param propertyName The field name of the StyleableProperty&lt;Color&gt;
     * @param cssProperty  The CSS property name
     * @param function     A function that returns the StyleableProperty&lt;Color&gt; that was created by this method call.
     * @param initialValue The initial value of the property. CSS may reset the property to this value.
     * @return a StyleableProperty created with initial value
     */
    public final StyleableProperty<Color> createColor(
            S styleable, String propertyName, String cssProperty,
            Function<S, StyleableProperty<Color>> function, Color initialValue) {
        return createColor(styleable, propertyName, cssProperty, function, initialValue, false);
    }

    /**
     * Create a StyleableProperty&lt;Color&gt;. The initial value defaults to Color.BLACK and the
     * inherit flag defaults to false.
     *
     * @param styleable    The <code>this</code> reference of the returned property. This is also the property bean.
     * @param propertyName The field name of the StyleableProperty&lt;Color&gt;
     * @param cssProperty  The CSS property name
     * @param function     A function that returns the StyleableProperty&lt;Color&gt; that was created by this method call.
     * @return a StyleableProperty created with default initial value and inherit flag
     */
    public final StyleableProperty<Color> createColor(S styleable, String propertyName, String cssProperty,
                                                      Function<S, StyleableProperty<Color>> function) {
        return createColor(styleable, propertyName, cssProperty, function, Color.BLACK, false);
    }

    /**
     * Create a StyleableProperty&lt;Color&gt; using previously created CssMetaData for the given <code>cssProperty</code>.
     *
     * @param styleable    The <code>this</code> reference of the returned property. This is also the property bean.
     * @param propertyName The field name of the StyleableProperty&lt;Color&gt;
     * @param cssProperty  The CSS property name
     * @return a StyleableProperty created using previously created CssMetaData
     * @throws IllegalArgumentException if <code>cssProperty</code> is null or empty
     * @throws NoSuchElementException   if the CssMetaData for <code>cssProperty</code> was not created prior to this method invocation
     */
    public final SimpleStyleableObjectProperty<Color> createColor(S styleable, String propertyName, String cssProperty) {
        return createColor(styleable, propertyName, cssProperty, (CssInvalidated<Color>) null);
    }

    public final SimpleStyleableObjectProperty<Color> createColor(S styleable, String propertyName, String cssProperty, CssInvalidated<Color> invalidated) {
        checkNotNullCssProperty(cssProperty);
        CssMetaData<S, Color> cssMetaData = getCssMetaData(Color.class, cssProperty);
        return new SimpleStyleableObjectProperty<>(cssMetaData, styleable, propertyName, cssMetaData.getInitialValue(styleable)) {
            private WeakReference<Color> weakReference = new WeakReference<>(null);

            @Override
            protected void invalidated() {
                Color old = weakReference.get();
                acceptValue(cssMetaData, invalidated, old, get());
                weakReference = new WeakReference<>(get());
            }
        };
    }

    //============================= create StyleableProperty<Duration> =============================//

    /**
     * Create a StyleableProperty&lt;Duration&gt; with initial value and inherit flag.
     *
     * @param styleable    The <code>this</code> reference of the returned property. This is also the property bean.
     * @param propertyName The field name of the StyleableProperty&lt;Duration&gt;
     * @param cssProperty  The CSS property name
     * @param function     A function that returns the StyleableProperty&lt;Duration&gt; that was created by this method call.
     * @param initialValue The initial value of the property. CSS may reset the property to this value.
     * @param inherits     Whether the CSS style can be inherited by child nodes
     * @return a StyleableProperty created with initial value and inherit flag
     */
    public final StyleableObjectProperty<Duration> createDuration(S styleable, String propertyName, String cssProperty,
                                                                  Function<S, StyleableProperty<Duration>> function, Duration initialValue, boolean inherits) {
        CssMetaData<S, Duration> cssMetaData = createDurationCss(cssProperty, function, initialValue, inherits);
        return new SimpleStyleableObjectProperty<>(cssMetaData, styleable, propertyName, initialValue);
    }

    /**
     * Create a StyleableProperty&lt;Duration&gt; with initial value. The inherit flag defaults to false.
     *
     * @param styleable    The <code>this</code> reference of the returned property. This is also the property bean.
     * @param propertyName The field name of the StyleableProperty&lt;Duration&gt;
     * @param cssProperty  The CSS property name
     * @param function     A function that returns the StyleableProperty&lt;Duration&gt; that was created by this method call.
     * @param initialValue The initial value of the property. CSS may reset the property to this value.
     * @return a StyleableProperty created with initial value and false inherit flag
     */
    public final StyleableObjectProperty<Duration> createDuration(S styleable, String propertyName, String cssProperty,
                                                                  Function<S, StyleableProperty<Duration>> function, Duration initialValue) {
        return createDuration(styleable, propertyName, cssProperty, function, initialValue, false);
    }

    /**
     * Create a StyleableProperty&lt;Duration&gt;. The initial value defaults to Duration.BLACK and the
     * inherit flag defaults to false.
     *
     * @param styleable    The <code>this</code> reference of the returned property. This is also the property bean.
     * @param propertyName The field name of the StyleableProperty&lt;Duration&gt;
     * @param cssProperty  The CSS property name
     * @param function     A function that returns the StyleableProperty&lt;Duration&gt; that was created by this method call.
     * @return a StyleableProperty created with default initial value and false inherit flag
     */
    public final StyleableObjectProperty<Duration> createDuration(S styleable, String propertyName, String cssProperty,
                                                                  Function<S, StyleableProperty<Duration>> function) {
        return createDuration(styleable, propertyName, cssProperty, function, Duration.UNKNOWN, false);
    }

    /**
     * Create a StyleableProperty&lt;Duration&gt; using previously created CssMetaData for the given <code>cssProperty</code>.
     *
     * @param styleable    The <code>this</code> reference of the returned property. This is also the property bean.
     * @param propertyName The field name of the StyleableProperty&lt;Duration&gt;
     * @param cssProperty  The CSS property name
     * @return a StyleableProperty created using previously created CssMetaData
     * @throws IllegalArgumentException if <code>cssProperty</code> is null or empty
     * @throws NoSuchElementException   if the CssMetaData for <code>cssProperty</code> was not created prior to this method invocation
     */
    public final StyleableObjectProperty<Duration> createDuration(S styleable, String propertyName, String cssProperty) {
        checkNotNullCssProperty(cssProperty);
        CssMetaData<S, Duration> cssMetaData = getCssMetaData(Duration.class, cssProperty);
        return new SimpleStyleableObjectProperty<>(cssMetaData, styleable, propertyName, cssMetaData.getInitialValue(styleable));
    }

    //============================= create StyleableProperty<Effect> =============================//

    /**
     * Create a StyleableProperty&lt;Effect&gt; with initial value and inherit flag.
     *
     * @param <E>          The type of StyleableProperty
     * @param styleable    The <code>this</code> reference of the returned property. This is also the property bean.
     * @param propertyName The field name of the StyleableProperty&lt;Effect&gt;
     * @param cssProperty  The CSS property name
     * @param function     A function that returns the StyleableProperty&lt;Effect&gt; that was created by this method call.
     * @param initialValue The initial value of the property. CSS may reset the property to this value.
     * @param inherits     Whether the CSS style can be inherited by child nodes
     * @return a StyleableProperty created with initial value and inherit flag
     */
    public final <E extends Effect> StyleableProperty<E> createEffect(S styleable, final Class<E> effectClass,
                                                                      String propertyName, String cssProperty, Function<S, StyleableProperty<E>> function, E initialValue, boolean inherits) {
        CssMetaData<S, E> cssMetaData = createEffectCss(effectClass, cssProperty, function, initialValue, inherits);
        return new SimpleStyleableObjectProperty<>(cssMetaData, styleable, propertyName, initialValue);
    }

    /**
     * Create a StyleableProperty&lt;Effect&gt; with initial value. The inherit flag defaults to false.
     *
     * @param <E>          The StyleableProperty created with initial value and false inherit flag
     * @param styleable    The <code>this</code> reference of the returned property. This is also the property bean.
     * @param propertyName The field name of the StyleableProperty&lt;Effect&gt;
     * @param cssProperty  The CSS property name
     * @param function     A function that returns the StyleableProperty&lt;Effect&gt; that was created by this method call.
     * @param initialValue The initial value of the property. CSS may reset the property to this value.
     * @return a StyleableProperty created with initial value and false inherit flag
     */
    public final <E extends Effect> StyleableProperty<E> createEffect(S styleable, final Class<E> effectClass,
                                                                      String propertyName, String cssProperty, Function<S, StyleableProperty<E>> function, E initialValue) {
        return createEffect(styleable, effectClass, propertyName, cssProperty, function, initialValue, false);
    }

    /**
     * Create a StyleableProperty&lt;Effect&gt;. The initial value is null and the inherit flag defaults to false.
     *
     * @param <E>          The StyleableProperty created with null initial value and false inherit flag
     * @param styleable    The <code>this</code> reference of the returned property. This is also the property bean.
     * @param propertyName The field name of the StyleableProperty&lt;Effect&gt;
     * @param cssProperty  The CSS property name
     * @param function     A function that returns the StyleableProperty&lt;Effect&gt; that was created by this method call.
     * @return a StyleableProperty created with null initial value and false inherit flag
     */
    public final <E extends Effect> StyleableProperty<E> createEffect(S styleable, final Class<E> effectClass,
                                                                      String propertyName, String cssProperty, Function<S, StyleableProperty<E>> function) {
        return createEffect(styleable, effectClass, propertyName, cssProperty, function, null, false);
    }

    /**
     * Create a StyleableProperty&lt;Effect&gt; using previously created CssMetaData for the given <code>cssProperty</code>.
     *
     * @param styleable    The <code>this</code> reference of the returned property. This is also the property bean.
     * @param propertyName The field name of the StyleableProperty&lt;Effect&gt;
     * @param cssProperty  The CSS property name
     * @return StyleableProperty created using previously created CssMetaData for the given <code>cssProperty</code>
     * @throws IllegalArgumentException if <code>cssProperty</code> is null or empty
     * @throws NoSuchElementException   if the CssMetaData for <code>cssProperty</code> was not created prior to this method invocation
     */
    public final StyleableProperty<Effect> createEffect(S styleable, String propertyName, String cssProperty) {
        checkNotNullCssProperty(cssProperty);
        CssMetaData<S, Effect> cssMetaData = getCssMetaData(Effect.class, cssProperty);
        return new SimpleStyleableObjectProperty<>(cssMetaData, styleable, propertyName, cssMetaData.getInitialValue(styleable));
    }

    //============================= create StyleableProperty<? extends Enum<?>> =============================//

    /**
     * Create a StyleableProperty&lt;E extends Enum&lt;E&gt;&gt; with initial value and inherit flag.
     * The <code>enumClass</code> parameter is the Class of the Enum that is the value of the property. For example,
     * <pre><code>
     * {@literal
     *     private static final StyleablePropertyFactory<MyControl> FACTORY = new StyleablePropertyFactory<>();
     *     StyleableProperty<Orientation> orientation =
     *         FACTORY.createEnum(
     *             this,
     *             "orientation",
     *             "-my-orientation",
     *             s -> ((MyControl)s).orientation,
     *             Orientation.class,
     *             Orientation.HORIZONTAL,
     *             false);
     * }
     * </code></pre>
     *
     * @param <E>          The StyleableProperty created with initial value and inherit flag
     * @param styleable    The <code>this</code> reference of the returned property. This is also the property bean.
     * @param propertyName The field name of the StyleableProperty&lt;E extends Enum&lt;E&gt;&gt;
     * @param cssProperty  The CSS property name
     * @param function     A function that returns the StyleableProperty&lt;E extends Enum&lt;E&gt;&gt; that was created by this method call.
     * @param enumClass    The Enum class that is the type of the StyleableProperty&lt;E extends Enum&lt;E&gt;&gt;.
     * @param initialValue The initial value of the property. CSS may reset the property to this value.
     * @param inherits     Whether the CSS style can be inherited by child nodes
     * @return a StyleableProperty created with initial value and inherit flag
     */
    public final <E extends Enum<E>> StyleableObjectProperty<E> createEnum(S styleable, String propertyName, String cssProperty,
                                                                           Function<S, StyleableObjectProperty<E>> function, Class<E> enumClass, E initialValue, boolean inherits) {
        CssMetaData<S, E> cssMetaData = createEnumCss(enumClass, cssProperty, function, initialValue, inherits);
        return new SimpleStyleableObjectProperty<>(cssMetaData, styleable, propertyName, initialValue);
    }


    /**
     * Create a StyleableProperty&lt;E extends Enum&lt;E&gt;&gt; with initial value. The inherit flag defaults to false.
     * The <code>enumClass</code> parameter is the Class of the Enum that is the value of the property. For example,
     * <pre><code>
     * {@literal
     *     private static final StyleablePropertyFactory<MyControl> FACTORY = new StyleablePropertyFactory<>();
     *     StyleableProperty<Orientation> orientation =
     *         FACTORY.createEnum(
     *             this,
     *             "orientation",
     *             "-my-orientation",
     *             s -> ((MyControl)s).orientation,
     *             Orientation.class,
     *             Orientation.HORIZONTAL);
     * }
     * </code></pre>
     *
     * @param <E>          The StyleableProperty created with initial value and false inherit flag
     * @param styleable    The <code>this</code> reference of the returned property. This is also the property bean.
     * @param propertyName The field name of the StyleableProperty&lt;E extends Enum&lt;E&gt;&gt;
     * @param cssProperty  The CSS property name
     * @param function     A function that returns the StyleableProperty&lt;E extends Enum&lt;E&gt;&gt; that was created by this method call.
     * @param enumClass    The Enum class that is the type of the StyleableProperty&lt;E extends Enum&lt;E&gt;&gt;.
     * @param initialValue The initial value of the property. CSS may reset the property to this value.
     * @return a StyleableProperty created with initial value and false inherit flag
     */
    public final <E extends Enum<E>> StyleableObjectProperty<E> createEnum(S styleable, String propertyName, String cssProperty, Function<S, StyleableObjectProperty<E>> function,
                                                                           Class<E> enumClass, E initialValue) {
        return createEnum(styleable, propertyName, cssProperty, function, enumClass, initialValue, false);
    }

    /**
     * Create a StyleableProperty&lt;E extends Enum&lt;E&gt;&gt;. The initial value is null and inherit flag defaults to false.
     * The <code>enumClass</code> parameter is the Class of the Enum that is the value of the property. For example,
     * <pre><code>
     * {@literal
     *     private static final StyleablePropertyFactory<MyControl> FACTORY = new StyleablePropertyFactory<>();
     *     StyleableProperty<Orientation> orientation =
     *         FACTORY.createEnum(
     *             this,
     *             "orientation",
     *             "-my-orientation",
     *             s -> ((MyControl)s).orientation,
     *             Orientation.class);
     * }
     * </code></pre>
     *
     * @param <E>          The StyleableProperty created with null initial value and false inherit flag
     * @param styleable    The <code>this</code> reference of the returned property. This is also the property bean.
     * @param propertyName The field name of the StyleableProperty&lt;E extends Enum&lt;E&gt;&gt;
     * @param cssProperty  The CSS property name
     * @param function     A function that returns the StyleableProperty&lt;E extends Enum&lt;E&gt;&gt; that was created by this method call.
     * @param enumClass    The Enum class that is the type of the StyleableProperty&lt;E extends Enum&lt;E&gt;&gt;.
     * @return a StyleableProperty created with null initial value and false inherit flag
     */
    public final <E extends Enum<E>> StyleableObjectProperty<E> createEnum(S styleable, String propertyName,
                                                                           String cssProperty, Function<S, StyleableObjectProperty<E>> function, Class<E> enumClass) {
        return createEnum(styleable, propertyName, cssProperty, function, enumClass, null, false);
    }

    public final <E extends Enum<E>> StyleableObjectProperty<E> createEnum(S styleable, String propertyName,
                                                                           String cssProperty, Class<E> enumClass, CssInvalidated<E> invalidated) {
        checkNotNullCssProperty(cssProperty);
        CssMetaData<S, E> cssMetaData = getCssMetaData(enumClass, cssProperty);
        return new SimpleStyleableObjectProperty<>(cssMetaData, styleable, propertyName, cssMetaData.getInitialValue(styleable)) {
            private WeakReference<E> weakReference = new WeakReference<>(null);

            @Override
            protected void invalidated() {
                E old = weakReference.get();
                acceptValue(cssMetaData, invalidated, old, get());
                weakReference = new WeakReference<>(get());
            }
        };
    }


    /**
     * Create a StyleableProperty&lt;E extends Enum&lt;E&gt;&gt;. The initial value is null and inherit flag defaults to false.
     * The <code>enumClass</code> parameter is the Class of the Enum that is the value of the property. For example,
     * <pre><code>
     * {@literal
     *     private static final StyleablePropertyFactory<MyControl> FACTORY = new StyleablePropertyFactory<>();
     *     StyleableProperty<Orientation> orientation =
     *         FACTORY.createEnum(
     *             this,
     *             "orientation",
     *             "-my-orientation",
     *             s -> ((MyControl)s).orientation,
     *             Orientation.class);
     * }
     * </code></pre>
     *
     * @param <E>          The StyleableProperty created with null initial value and false inherit flag
     * @param styleable    The <code>this</code> reference of the returned property. This is also the property bean.
     * @param propertyName The field name of the StyleableProperty&lt;E extends Enum&lt;E&gt;&gt;
     * @param cssProperty  The CSS property name
     * @param enumClass    The Enum class that is the type of the StyleableProperty&lt;E extends Enum&lt;E&gt;&gt;.
     * @return a StyleableProperty created with null initial value and false inherit flag
     */
    public final <E extends Enum<E>> StyleableObjectProperty<E> createEnum(S styleable, String propertyName, String cssProperty, Class<E> enumClass) {
        checkNotNullCssProperty(cssProperty);
        CssMetaData<S, E> cssMetaData = getCssMetaData(enumClass, cssProperty);
        return new SimpleStyleableObjectProperty<>(cssMetaData, styleable, propertyName, cssMetaData.getInitialValue(styleable));
    }

    public final <E extends Enum<E>> StyleableObjectProperty<E> createEnum(S styleable, String propertyName, String cssProperty) {
        checkNotNullCssProperty(cssProperty);
        CssMetaData<S, E> cssMetaData = getCssMetaData(cssProperty);
        return new SimpleStyleableObjectProperty<>(cssMetaData, styleable, propertyName, cssMetaData.getInitialValue(styleable));
    }


    /**
     * Create a StyleableProperty&lt;E extends Enum&lt;E&gt;&gt; using previously created CssMetaData for the given <code>cssProperty</code>.
     *
     * @param <E>          The StyleableProperty created using previously created CssMetaData
     * @param styleable    The <code>this</code> reference of the returned property. This is also the property bean.
     * @param propertyName The field name of the StyleableProperty&lt;E extends Enum&lt;E&gt;&gt;
     * @param cssProperty  The CSS property name
     * @param enumClass    The Enum class that is the type of the StyleableProperty&lt;E extends Enum&lt;E&gt;&gt;.
     * @return a StyleableProperty created using previously created CssMetaData
     * @throws IllegalArgumentException if <code>cssProperty</code> is null or empty
     * @throws NoSuchElementException   if the CssMetaData for <code>cssProperty</code> was not created prior to this method invocation
     */
    public final <E extends Enum<E>> StyleableProperty<E> createEffect(S styleable, String propertyName, String cssProperty, Class<E> enumClass) {
        checkNotNullCssProperty(cssProperty);
        CssMetaData<S, E> cssMetaData = getCssMetaData(enumClass, cssProperty);
        return new SimpleStyleableObjectProperty<>(cssMetaData, styleable, propertyName, cssMetaData.getInitialValue(styleable));
    }

    //============================= create StyleableProperty<Font>
    //
    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////=============================//

    /**
     * Create a StyleableProperty&lt;Font&gt; with initial value and inherit flag.
     *
     * @param styleable    The <code>this</code> reference of the returned property. This is also the property bean.
     * @param propertyName The field name of the StyleableProperty&lt;Font&gt;
     * @param cssProperty  The CSS property name
     * @param function     A function that returns the StyleableProperty&lt;Font&gt; that was created by this method call.
     * @param initialValue The initial value of the property. CSS may reset the property to this value.
     * @param inherits     Whether the CSS style can be inherited by child nodes
     * @return a StyleableProperty created with initial value and inherit flag
     */
    public final StyleableObjectProperty<Font> createFont(
            S styleable, String propertyName, String cssProperty,
            Function<S, StyleableProperty<Font>> function,
            Font initialValue,
            boolean inherits) {

        CssMetaData<S, Font> cssMetaData = createFontCss(cssProperty, function, initialValue, inherits);
        return new SimpleStyleableObjectProperty<>(cssMetaData, styleable, propertyName, initialValue);
    }

    /**
     * Create a StyleableProperty&lt;Font&gt; with initial value. The inherit flag defaults to true.
     *
     * @param styleable    The <code>this</code> reference of the returned property. This is also the property bean.
     * @param propertyName The field name of the StyleableProperty&lt;Font&gt;
     * @param cssProperty  The CSS property name
     * @param function     A function that returns the StyleableProperty&lt;Font&gt; that was created by this method call.
     * @param initialValue The initial value of the property. CSS may reset the property to this value.
     * @return a StyleableProperty created with initial value and true inherit flag
     */
    public final StyleableObjectProperty<Font> createFont(
            S styleable, String propertyName, String cssProperty,
            Function<S, StyleableProperty<Font>> function,
            Font initialValue) {
        return createFont(styleable, propertyName, cssProperty, function, initialValue, true);
    }

    /**
     * Create a StyleableProperty&lt;Font&gt;. The initial value defaults to {@link Font#getDefault()}
     * and the inherit flag defaults to true.
     *
     * @param styleable    The <code>this</code> reference of the returned property. This is also the property bean.
     * @param propertyName The field name of the StyleableProperty&lt;Font&gt;
     * @param cssProperty  The CSS property name
     * @param function     A function that returns the StyleableProperty&lt;Font&gt; that was created by this method call.
     * @return a StyleableProperty created with default font initial value and true inherit flag
     */
    public final StyleableObjectProperty<Font> createFont(
            S styleable, String propertyName, String cssProperty,
            Function<S, StyleableProperty<Font>> function) {
        return createFont(styleable, propertyName, cssProperty, function, Font.getDefault(), true);
    }

    /**
     * Create a StyleableProperty&lt;Font&gt; using previously created CssMetaData for the given <code>cssProperty</code>.
     *
     * @param styleable    The <code>this</code> reference of the returned property. This is also the property bean.
     * @param propertyName The field name of the StyleableProperty&lt;Font&gt;
     * @param cssProperty  The CSS property name
     * @return a StyleableProperty created using previously created CssMetaData
     * @throws IllegalArgumentException if <code>cssProperty</code> is null or empty
     * @throws NoSuchElementException   if the CssMetaData for <code>cssProperty</code> was not created prior to this method invocation
     */
    public final StyleableObjectProperty<Font> createFont(S styleable, String propertyName, String cssProperty) {
        checkNotNullCssProperty(cssProperty);
        CssMetaData<S, Font> cssMetaData = getCssMetaData(Font.class, cssProperty);
        return new SimpleStyleableObjectProperty<>(cssMetaData, styleable, propertyName, cssMetaData.getInitialValue(styleable));
    }

    //============================= create StyleableObjectProperty<Insets> =============================//

    /**
     * Create a StyleableProperty&lt;Inset&gt; with initial value and inherit flag.
     *
     * @param styleable    The <code>this</code> reference of the returned property. This is also the property bean.
     * @param propertyName The field name of the StyleableProperty&lt;Inset&gt;
     * @param cssProperty  The CSS property name
     * @param function     A function that returns the StyleableProperty&lt;Inset&gt; that was created by this method call.
     * @param initialValue The initial value of the property. CSS may reset the property to this value.
     * @param inherits     Whether the CSS style can be inherited by child nodes
     * @return a StyleableProperty created with initial value and inherit flag
     */
    public final StyleableObjectProperty<Insets> createInsets(
            S styleable, String propertyName, String cssProperty,
            Function<S, StyleableProperty<Insets>> function,
            Insets initialValue,
            boolean inherits) {

        CssMetaData<S, Insets> cssMetaData = createInsetsCss(cssProperty, function, initialValue, inherits);
        return new SimpleStyleableObjectProperty<>(cssMetaData, styleable, propertyName, initialValue);
    }

    /**
     * Create a StyleableProperty&lt;Inset&gt; with initial value. The inherit flag defaults to false.
     *
     * @param styleable    The <code>this</code> reference of the returned property. This is also the property bean.
     * @param propertyName The field name of the StyleableProperty&lt;Inset&gt;
     * @param cssProperty  The CSS property name
     * @param function     A function that returns the StyleableProperty&lt;Inset&gt; that was created by this method call.
     * @param initialValue The initial value of the property. CSS may reset the property to this value.
     * @return a StyleableProperty created with initial value and false inherit flag
     */
    public final StyleableObjectProperty<Insets> createInsets(
            S styleable, String propertyName, String cssProperty,
            Function<S, StyleableProperty<Insets>> function,
            Insets initialValue) {
        return createInsets(styleable, propertyName, cssProperty, function, initialValue, false);
    }

    /**
     * Create a StyleableProperty&lt;Inset&gt;. The initial value is {@link Insets#EMPTY} and the inherit flag defaults to false.
     *
     * @param styleable    The <code>this</code> reference of the returned property. This is also the property bean.
     * @param propertyName The field name of the StyleableProperty&lt;Inset&gt;
     * @param cssProperty  The CSS property name
     * @param function     A function that returns the StyleableProperty&lt;Inset&gt; that was created by this method call.
     * @return a StyleableProperty created with initial value and false inherit flag
     */
    public final StyleableObjectProperty<Insets> createInsets(
            S styleable, String propertyName, String cssProperty,
            Function<S, StyleableProperty<Insets>> function) {
        return createInsets(styleable, propertyName, cssProperty, function, Insets.EMPTY, false);
    }

    /**
     * Create a StyleableProperty&lt;Insets&gt; using previously created CssMetaData for the given <code>cssProperty</code>.
     *
     * @param styleable    The <code>this</code> reference of the returned property. This is also the property bean.
     * @param propertyName The field name of the StyleableProperty&lt;Insets&gt;
     * @param cssProperty  The CSS property name
     * @return a StyleableProperty created using previously created CssMetaData
     * @throws IllegalArgumentException if <code>cssProperty</code> is null or empty
     * @throws NoSuchElementException   if the CssMetaData for <code>cssProperty</code> was not created prior to this method invocation
     */
    public final StyleableObjectProperty<Insets> createInsets(S styleable, String propertyName, String cssProperty) {

        checkNotNullCssProperty(cssProperty);
        CssMetaData<S, Insets> cssMetaData = getCssMetaData(Insets.class, cssProperty);
        return new SimpleStyleableObjectProperty<>(cssMetaData, styleable, propertyName, cssMetaData.getInitialValue(styleable));
    }

    //============================= create StyleableProperty<Paint> =============================//

    /**
     * Create a StyleableProperty&lt;Paint&gt; with initial value and inherit flag.
     *
     * @param styleable    The <code>this</code> reference of the returned property. This is also the property bean.
     * @param propertyName The field name of the StyleableProperty&lt;Paint&gt;
     * @param cssProperty  The CSS property name
     * @param function     A function that returns the StyleableProperty&lt;Paint&gt; that was created by this method call.
     * @param initialValue The initial value of the property. CSS may reset the property to this value.
     * @param inherits     Whether the CSS style can be inherited by child nodes
     * @return a StyleableProperty created with initial value and inherit flag
     */
    public final StyleableObjectProperty<Paint> createPaint(
            S styleable, String propertyName, String cssProperty,
            Function<S, StyleableProperty<Paint>> function,
            Paint initialValue,
            boolean inherits) {

        CssMetaData<S, Paint> cssMetaData = createPaintCss(cssProperty, function, initialValue, inherits);
        return new SimpleStyleableObjectProperty<>(cssMetaData, styleable, propertyName, initialValue);
    }

    /**
     * Create a StyleableProperty&lt;Paint&gt; with initial value. The inherit flag defaults to false.
     *
     * @param styleable    The <code>this</code> reference of the returned property. This is also the property bean.
     * @param propertyName The field name of the StyleableProperty&lt;Paint&gt;
     * @param cssProperty  The CSS property name
     * @param function     A function that returns the StyleableProperty&lt;Paint&gt; that was created by this method call.
     * @param initialValue The initial value of the property. CSS may reset the property to this value.
     * @return a StyleableProperty created with initial value and false inherit flag
     */
    public final StyleableObjectProperty<Paint> createPaint(
            S styleable, String propertyName, String cssProperty,
            Function<S, StyleableProperty<Paint>> function,
            Paint initialValue) {
        return createPaint(styleable, propertyName, cssProperty, function, initialValue, false);
    }

    /**
     * Create a StyleableProperty&lt;Paint&gt;. The initial value defautls to Color.BLACK and the inherit flag defaults to false.
     *
     * @param styleable    The <code>this</code> reference of the returned property. This is also the property bean.
     * @param propertyName The field name of the StyleableProperty&lt;Paint&gt;
     * @param cssProperty  The CSS property name
     * @param function     A function that returns the StyleableProperty&lt;Paint&gt; that was created by this method call.
     * @return a StyleableProperty created with initial value and false inherit flag
     */
    public final StyleableObjectProperty<Paint> createPaint(S styleable, String propertyName, String cssProperty,
                                                            Function<S, StyleableProperty<Paint>> function) {
        return createPaint(styleable, propertyName, cssProperty, function, Color.BLACK, false);
    }

    public final StyleableObjectProperty<Paint> createPaint(S styleable, String propertyName, String cssProperty, CssInvalidated<Paint> invalidated) {
        checkNotNullCssProperty(cssProperty);
        CssMetaData<S, Paint> cssMetaData = getCssMetaData(Paint.class, cssProperty);
        return new SimpleStyleableObjectProperty<>(cssMetaData, styleable, propertyName, cssMetaData.getInitialValue(styleable)) {
            private WeakReference<Paint> weakReference = new WeakReference<>(null);

            @Override
            protected void invalidated() {
                Paint old = weakReference.get();
                acceptValue(cssMetaData, invalidated, old, get());
                weakReference = new WeakReference<>(get());
            }
        };
    }

    /**
     * Create a StyleableProperty&lt;Paint&gt; using previously created CssMetaData for the given <code>cssProperty</code>.
     *
     * @param styleable    The <code>this</code> reference of the returned property. This is also the property bean.
     * @param propertyName The field name of the StyleableProperty&lt;Paint&gt;
     * @param cssProperty  The CSS property name
     * @return a StyleableProperty created using previously created CssMetaData
     * @throws IllegalArgumentException if <code>cssProperty</code> is null or empty
     * @throws NoSuchElementException   if the CssMetaData for <code>cssProperty</code> was not created prior to this method invocation
     */
    public final StyleableObjectProperty<Paint> createPaint(S styleable, String propertyName, String cssProperty) {
        checkNotNullCssProperty(cssProperty);
        CssMetaData<S, Paint> cssMetaData = getCssMetaData(Paint.class, cssProperty);
        return new SimpleStyleableObjectProperty<>(cssMetaData, styleable, propertyName, cssMetaData.getInitialValue(styleable));
    }

    //============================= create StyleableProperty<Number> =============================//

    /**
     * Create a StyleableProperty&lt;Number&gt; with initial value and inherit flag.
     *
     * @param styleable    The <code>this</code> reference of the returned property. This is also the property bean.
     * @param propertyName The field name of the StyleableProperty&lt;Number&gt;
     * @param cssProperty  The CSS property name
     * @param function     A function that returns the StyleableProperty&lt;Number&gt; that was created by this method call.
     * @param initialValue The initial value of the property. CSS may reset the property to this value.
     * @param inherits     Whether the CSS style can be inherited by child nodes
     * @return a StyleableProperty created with initial value and inherit flag
     */
    public final StyleableProperty<Number> createNumber(S styleable, String propertyName, String cssProperty,
                                                        Function<S, StyleableProperty<Number>> function, Number initialValue, boolean inherits) {

        CssMetaData<S, Number> cssMetaData = createSizeCss(cssProperty, function, initialValue, inherits);
        return new SimpleStyleableObjectProperty<>(cssMetaData, styleable, propertyName, initialValue);
    }

    /**
     * Create a StyleableProperty&lt;Number&gt; with initial value. The inherit flag defaults to false.
     *
     * @param styleable    The <code>this</code> reference of the returned property. This is also the property bean.
     * @param propertyName The field name of the StyleableProperty&lt;Number&gt;
     * @param cssProperty  The CSS property name
     * @param function     A function that returns the StyleableProperty&lt;Number&gt; that was created by this method call.
     * @param initialValue The initial value of the property. CSS may reset the property to this value.
     * @return a StyleableProperty created with initial value and false inherit flag
     */
    public final StyleableProperty<Number> createNumber(S styleable, String propertyName, String cssProperty,
                                                        Function<S, StyleableProperty<Number>> function, Number initialValue) {
        return createNumber(styleable, propertyName, cssProperty, function, initialValue, false);
    }

    /**
     * Create a StyleableProperty&lt;Number&gt;. The initial value defaults to zero. The inherit flag defaults to false.
     *
     * @param styleable    The <code>this</code> reference of the returned property. This is also the property bean.
     * @param propertyName The field name of the StyleableProperty&lt;Number&gt;
     * @param cssProperty  The CSS property name
     * @param function     A function that returns the StyleableProperty&lt;Number&gt; that was created by this method call.
     * @return a StyleableProperty created with zero initial value and false inherit flag
     */
    public final StyleableProperty<Number> createNumber(S styleable, String propertyName, String cssProperty,
                                                        Function<S, StyleableProperty<Number>> function) {
        return createNumber(styleable, propertyName, cssProperty, function, null, false);
    }

    /**
     * Create a StyleableProperty&lt;Number&gt; using previously created CssMetaData for the given <code>cssProperty</code>.
     *
     * @param styleable    The <code>this</code> reference of the returned property. This is also the property bean.
     * @param propertyName The field name of the StyleableProperty&lt;Number&gt;
     * @param cssProperty  The CSS property name
     * @return a StyleableProperty created using previously created CssMetaData
     * @throws IllegalArgumentException if <code>cssProperty</code> is null or empty
     * @throws NoSuchElementException   if the CssMetaData for <code>cssProperty</code> was not created prior to this method invocation
     */
    public final StyleableProperty<Number> createNumber(S styleable, String propertyName, String cssProperty) {
        checkNotNullCssProperty(cssProperty);
        CssMetaData<S, Number> cssMetaData = getCssMetaData(Number.class, cssProperty);
        return new SimpleStyleableObjectProperty<>(cssMetaData, styleable, propertyName, cssMetaData.getInitialValue(styleable));
    }


    //============================= create StyleableDoubleProperty =============================//

    /**
     * Create a StyleableDoubleProperty with initial value. The inherit flag defaults to false.
     *
     * @param styleable    The <code>this</code> reference of the returned property. This is also the property bean.
     * @param propertyName The field name of the StyleableDoubleProperty
     * @param cssProperty  The CSS property name
     * @param function     A function that returns the StyleableDoubleProperty that was created by this method call.
     * @param initialValue The initial value of the property. CSS may reset the property to this value.
     * @return a StyleableProperty created with initial value and false inherit flag
     */
    public final StyleableDoubleProperty createDouble(S styleable, String propertyName, String cssProperty,
                                                      Function<S, StyleableProperty<Number>> function, Double initialValue) {
        return createDouble(styleable, propertyName, cssProperty, function, initialValue, false);
    }

    /**
     * Create a StyleableDoubleProperty. The initial value defaults to zero. The inherit flag defaults to false.
     *
     * @param styleable    The <code>this</code> reference of the returned property. This is also the property bean.
     * @param propertyName The field name of the StyleableDoubleProperty
     * @param cssProperty  The CSS property name
     * @param function     A function that returns the StyleableDoubleProperty that was created by this method call.
     * @return a StyleableProperty created with zero initial value and false inherit flag
     */
    public final StyleableDoubleProperty createDouble(S styleable, String propertyName, String cssProperty,
                                                      Function<S, StyleableProperty<Number>> function) {
        return createDouble(styleable, propertyName, cssProperty, function, null, false);
    }

    /**
     * Create a StyleableDoubleProperty with initial value and inherit flag.
     *
     * @param styleable    The <code>this</code> reference of the returned property. This is also the property bean.
     * @param propertyName The field name of the StyleableDoubleProperty
     * @param cssProperty  The CSS property name
     * @param function     A function that returns the StyleableDoubleProperty that was created by this method call.
     * @param initialValue The initial value of the property. CSS may reset the property to this value.
     * @param inherits     Whether the CSS style can be inherited by child nodes
     * @return a StyleableProperty created with initial value and inherit flag
     */
    public final StyleableDoubleProperty createDouble(S styleable, String propertyName, String cssProperty,
                                                      Function<S, StyleableProperty<Number>> function, Double initialValue, boolean inherits) {
        CssMetaData<S, Number> cssMetaData = createSizeCss(cssProperty, function, initialValue, inherits);
        return createDouble(styleable, cssMetaData, propertyName, initialValue);
    }

    /**
     * Create a StyleableDoubleProperty using previously created CssMetaData for the given <code>cssProperty</code>.
     *
     * @param styleable    The <code>this</code> reference of the returned property. This is also the property bean.
     * @param propertyName The field name of the StyleableDoubleProperty
     * @param cssProperty  The CSS property name
     * @return a StyleableProperty created using previously created CssMetaData
     * @throws IllegalArgumentException if <code>cssProperty</code> is null or empty
     * @throws NoSuchElementException   if the CssMetaData for <code>cssProperty</code> was not created prior to this method invocation
     */
    public final StyleableDoubleProperty createDouble(S styleable, String propertyName, String cssProperty) {
        checkNotNullCssProperty(cssProperty);
        CssMetaData<S, Number> cssMetaData = getCssMetaData(Number.class, cssProperty);
        Number initialValue = cssMetaData.getInitialValue(styleable);
        return createDouble(styleable, cssMetaData, propertyName, initialValue);
    }

    public final StyleableDoubleProperty createDouble(S styleable, String propertyName, String cssProperty, StyleOrigin styleOrigin) {
        checkNotNullCssProperty(cssProperty);
        CssMetaData<S, Number> cssMetaData = getCssMetaData(Number.class, cssProperty);
        Number initialValue = cssMetaData.getInitialValue(styleable);
        return createDouble(styleable, cssMetaData, propertyName, initialValue, null, null);
    }

    public final StyleableDoubleProperty createDouble(S styleable, String propertyName, String cssProperty, CssInvalidated<Double> invalidated) {
        checkNotNullCssProperty(cssProperty);
        CssMetaData<S, Number> cssMetaData = getCssMetaData(Number.class, cssProperty);
        Number initialValue = cssMetaData.getInitialValue(styleable);
        return createDouble(styleable, cssMetaData, propertyName, initialValue, invalidated);
    }

    public final StyleableDoubleProperty createDouble(S styleable, String propertyName, String cssProperty, Number initialValue) {
        CssMetaData<S, Number> cssMetaData = getCssMetaData(Number.class, cssProperty);
        return createDouble(styleable, cssMetaData, propertyName, initialValue, null);
    }

    public final StyleableDoubleProperty createDouble(S styleable, CssMetaData<S, Number> cssMetaData, String propertyName, Number initialValue) {
        return createDouble(styleable, cssMetaData, propertyName, initialValue, null);
    }

    public final StyleableDoubleProperty createDouble(S styleable, CssMetaData<S, Number> cssMetaData, String propertyName, Number initialValue, CssInvalidated<Double> invalidated) {
        return createDouble(styleable, cssMetaData, propertyName, initialValue, null, invalidated);
    }

    public final StyleableDoubleProperty createDouble(S styleable, CssMetaData<S, Number> cssMetaData, String propertyName, Number initialValue, StyleOrigin styleOrigin, CssInvalidated<Double> invalidated) {
        Double aDouble = initialValue == null ? null : initialValue.doubleValue();
        return new SimpleStyleableDoubleProperty(cssMetaData, styleable, propertyName, aDouble) {
            private WeakReference<Double> weakReference = new WeakReference<>(null);

            @Override
            public void set(double v) {
                super.set(v);
            }

            @Override
            protected void invalidated() {
                Double old = weakReference.get();
                acceptValue(cssMetaData, invalidated, old, get());
                weakReference = new WeakReference<>(get());
            }

            @Override
            public StyleOrigin getStyleOrigin() {
                return styleOrigin != null ? styleOrigin : super.getStyleOrigin();
            }
        };
    }

    private <T> void acceptValue(CssMetaData cssMetaData, CssInvalidated<T> invalidated, T oldValue, T newValue) {
        if (log.isDebugEnabled()) {
            log.debug("invalidated -- {} --> {}::{}", cssMetaData.getProperty(), oldValue, newValue);
        }
        if (invalidated != null) invalidated.accept(oldValue, newValue);
    }

    //============================= create StyleableIntegerProperty =============================//

    /**
     * Create a StyleableIntegerProperty with initial value. The inherit flag defaults to false.
     *
     * @param styleable    The <code>this</code> reference of the returned property. This is also the property bean.
     * @param propertyName The field name of the StyleableIntegerProperty
     * @param cssProperty  The CSS property name
     * @param function     A function that returns the StyleableIntegerProperty that was created by this method call.
     * @param initialValue The initial value of the property. CSS may reset the property to this value.
     * @return a StyleableProperty created with initial value and false inherit flag
     */
    public final StyleableIntegerProperty createInteger(S styleable, String propertyName, String cssProperty,
                                                        Function<S, StyleableProperty<Number>> function, Integer initialValue) {
        return createInteger(styleable, propertyName, cssProperty, function, initialValue, false);
    }

    /**
     * Create a StyleableIntegerProperty. The initial value defaults to zero. The inherit flag defaults to false.
     *
     * @param styleable    The <code>this</code> reference of the returned property. This is also the property bean.
     * @param propertyName The field name of the StyleableIntegerProperty
     * @param cssProperty  The CSS property name
     * @param function     A function that returns the StyleableIntegerProperty that was created by this method call.
     * @return a StyleableProperty created with zero initial value and false inherit flag
     */
    public final StyleableIntegerProperty createInteger(S styleable, String propertyName, String cssProperty,
                                                        Function<S, StyleableProperty<Number>> function) {
        return createInteger(styleable, propertyName, cssProperty, function, null, false);
    }

    /**
     * Create a StyleableIntegerProperty with initial value and inherit flag.
     *
     * @param styleable    The <code>this</code> reference of the returned property. This is also the property bean.
     * @param propertyName The field name of the StyleableIntegerProperty
     * @param cssProperty  The CSS property name
     * @param function     A function that returns the StyleableIntegerProperty that was created by this method call.
     * @param initialValue The initial value of the property. CSS may reset the property to this value.
     * @param inherits     Whether the CSS style can be inherited by child nodes
     * @return a StyleableProperty created with initial value and inherit flag
     */
    public final StyleableIntegerProperty createInteger(S styleable, String propertyName, String cssProperty,
                                                        Function<S, StyleableProperty<Number>> function, Integer initialValue, boolean inherits) {
        CssMetaData<S, Number> cssMetaData = createSizeCss(cssProperty, function, initialValue, inherits);
        return createInteger(styleable, cssMetaData, propertyName, initialValue);
    }

    /**
     * Create a StyleableIntegerProperty using previously created CssMetaData for the given <code>cssProperty</code>.
     *
     * @param styleable    The <code>this</code> reference of the returned property. This is also the property bean.
     * @param propertyName The field name of the StyleableIntegerProperty
     * @param cssProperty  The CSS property name
     * @return a StyleableProperty created using previously created CssMetaData
     * @throws IllegalArgumentException if <code>cssProperty</code> is null or empty
     * @throws NoSuchElementException   if the CssMetaData for <code>cssProperty</code> was not created prior to this method invocation
     */
    public final StyleableIntegerProperty createInteger(S styleable, String propertyName, String cssProperty) {
        return createInteger(styleable, propertyName, cssProperty, (o, v) -> {
        });
    }

    public final StyleableIntegerProperty createInteger(S styleable, String propertyName, String cssProperty, CssInvalidated<Number> invalidated) {
        CssMetaData<S, Number> cssMetaData = getCssMetaData(Number.class, cssProperty);
        Number initialValue = cssMetaData.getInitialValue(styleable);
        return createInteger(styleable, cssMetaData, propertyName, initialValue, invalidated);
    }

    private StyleableIntegerProperty createInteger(S styleable, CssMetaData<S, Number> cssMetaData, String propertyName, Number initialValue) {
        return createInteger(styleable, cssMetaData, propertyName, initialValue, null);
    }

    private StyleableIntegerProperty createInteger(S styleable, CssMetaData<S, Number> cssMetaData, String propertyName, Number initialValue,
                                                   CssInvalidated<Number> invalidated) {
        return new SimpleStyleableIntegerProperty(cssMetaData, styleable, propertyName, initialValue.intValue()) {
            private WeakReference<Number> weakReference = new WeakReference<>(null);

            @Override
            protected void invalidated() {
                Number old = weakReference.get();
                acceptValue(cssMetaData, invalidated, old, get());
                weakReference = new WeakReference<>(get());
            }


        };
    }


    //============================= create StyleableFloatProperty =============================//

    /**
     * Create a StyleableFloatProperty with initial value. The inherit flag defaults to false.
     *
     * @param styleable    The <code>this</code> reference of the returned property. This is also the property bean.
     * @param propertyName The field name of the StyleableFloatProperty
     * @param cssProperty  The CSS property name
     * @param function     A function that returns the StyleableFloatProperty that was created by this method call.
     * @param initialValue The initial value of the property. CSS may reset the property to this value.
     * @return a StyleableProperty created with initial value and false inherit flag
     */
    public final StyleableFloatProperty createFloat(S styleable, String propertyName, String cssProperty,
                                                    Function<S, StyleableProperty<Number>> function, Float initialValue) {
        return createFloat(styleable, propertyName, cssProperty, function, initialValue, false);
    }

    /**
     * Create a StyleableFloatProperty. The initial value defaults to zero. The inherit flag defaults to false.
     *
     * @param styleable    The <code>this</code> reference of the returned property. This is also the property bean.
     * @param propertyName The field name of the StyleableFloatProperty
     * @param cssProperty  The CSS property name
     * @param function     A function that returns the StyleableFloatProperty that was created by this method call.
     * @return a StyleableProperty created with zero initial value and false inherit flag
     */
    public final StyleableFloatProperty createFloat(S styleable, String propertyName, String cssProperty,
                                                    Function<S, StyleableProperty<Number>> function) {
        return createFloat(styleable, propertyName, cssProperty, function, null, false);
    }

    /**
     * Create a StyleableFloatProperty with initial value and inherit flag.
     *
     * @param styleable    The <code>this</code> reference of the returned property. This is also the property bean.
     * @param propertyName The field name of the StyleableFloatProperty
     * @param cssProperty  The CSS property name
     * @param function     A function that returns the StyleableFloatProperty that was created by this method call.
     * @param initialValue The initial value of the property. CSS may reset the property to this value.
     * @param inherits     Whether the CSS style can be inherited by child nodes
     * @return a StyleableProperty created with initial value and inherit flag
     */
    public final StyleableFloatProperty createFloat(S styleable, String propertyName, String cssProperty,
                                                    Function<S, StyleableProperty<Number>> function, Float initialValue, boolean inherits) {
        CssMetaData<S, Number> cssMetaData = createSizeCss(cssProperty, function, initialValue, inherits);
        return createFloat(styleable, cssMetaData, propertyName, initialValue);
    }

    /**
     * Create a StyleableFloatProperty using previously created CssMetaData for the given <code>cssProperty</code>.
     *
     * @param styleable    The <code>this</code> reference of the returned property. This is also the property bean.
     * @param propertyName The field name of the StyleableFloatProperty
     * @param cssProperty  The CSS property name
     * @return a StyleableProperty created using previously created CssMetaData
     * @throws IllegalArgumentException if <code>cssProperty</code> is null or empty
     * @throws NoSuchElementException   if the CssMetaData for <code>cssProperty</code> was not created prior to this method invocation
     */
    public final StyleableFloatProperty createFloat(S styleable, String propertyName, String cssProperty) {
        checkNotNullCssProperty(cssProperty);
        CssMetaData<S, Number> cssMetaData = getCssMetaData(Number.class, cssProperty);
        Number initialValue = cssMetaData.getInitialValue(styleable);
        return createFloat(styleable, cssMetaData, propertyName, initialValue);
    }

    private StyleableFloatProperty createFloat(S styleable, CssMetaData<S, Number> cssMetaData, String propertyName, Number initialValue) {
        if (initialValue == null) return new SimpleStyleableFloatProperty(cssMetaData, styleable, propertyName);
        else return new SimpleStyleableFloatProperty(cssMetaData, styleable, propertyName, initialValue.floatValue());
    }

    //============================= create StyleableLongProperty =============================//

    /**
     * Create a StyleableLongProperty with initial value. The inherit flag defaults to false.
     *
     * @param styleable    The <code>this</code> reference of the returned property. This is also the property bean.
     * @param propertyName The field name of the StyleableLongProperty
     * @param cssProperty  The CSS property name
     * @param function     A function that returns the StyleableLongProperty that was created by this method call.
     * @param initialValue The initial value of the property. CSS may reset the property to this value.
     * @return a StyleableProperty created with initial value and false inherit flag
     */
    public final StyleableLongProperty createLong(S styleable, String propertyName, String cssProperty,
                                                  Function<S, StyleableProperty<Number>> function, Long initialValue) {
        return createLong(styleable, propertyName, cssProperty, function, initialValue, false);
    }

    /**
     * Create a StyleableLongProperty. The initial value defaults to zero. The inherit flag defaults to false.
     *
     * @param styleable    The <code>this</code> reference of the returned property. This is also the property bean.
     * @param propertyName The field name of the StyleableLongProperty
     * @param cssProperty  The CSS property name
     * @param function     A function that returns the StyleableLongProperty that was created by this method call.
     * @return a StyleableProperty created with zero initial value and false inherit flag
     */
    public final StyleableLongProperty createLong(S styleable, String propertyName, String cssProperty,
                                                  Function<S, StyleableProperty<Number>> function) {
        return createLong(styleable, propertyName, cssProperty, function, null, false);
    }

    /**
     * Create a StyleableLongProperty with initial value and inherit flag.
     *
     * @param styleable    The <code>this</code> reference of the returned property. This is also the property bean.
     * @param propertyName The field name of the StyleableLongProperty
     * @param cssProperty  The CSS property name
     * @param function     A function that returns the StyleableLongProperty that was created by this method call.
     * @param initialValue The initial value of the property. CSS may reset the property to this value.
     * @param inherits     Whether the CSS style can be inherited by child nodes
     * @return a StyleableProperty created with initial value and inherit flag
     */
    public final StyleableLongProperty createLong(S styleable, String propertyName, String cssProperty,
                                                  Function<S, StyleableProperty<Number>> function, Long initialValue, boolean inherits) {
        CssMetaData<S, Number> cssMetaData = createSizeCss(cssProperty, function, initialValue, inherits);
        return createLong(styleable, cssMetaData, propertyName, initialValue);
    }

    /**
     * Create a StyleableLongProperty using previously created CssMetaData for the given <code>cssProperty</code>.
     *
     * @param styleable    The <code>this</code> reference of the returned property. This is also the property bean.
     * @param propertyName The field name of the StyleableLongProperty
     * @param cssProperty  The CSS property name
     * @return a StyleableProperty created using previously created CssMetaData
     * @throws IllegalArgumentException if <code>cssProperty</code> is null or empty
     * @throws NoSuchElementException   if the CssMetaData for <code>cssProperty</code> was not created prior to this method invocation
     */
    public final StyleableLongProperty createLong(S styleable, String propertyName, String cssProperty) {
        checkNotNullCssProperty(cssProperty);
        CssMetaData<S, Number> cssMetaData = getCssMetaData(Number.class, cssProperty);
        Number initialValue = cssMetaData.getInitialValue(styleable);
        return createLong(styleable, cssMetaData, propertyName, initialValue);
    }

    private StyleableLongProperty createLong(S styleable, CssMetaData<S, Number> cssMetaData, String propertyName, Number initialValue) {
        if (initialValue == null) return new SimpleStyleableLongProperty(cssMetaData, styleable, propertyName);
        else return new SimpleStyleableLongProperty(cssMetaData, styleable, propertyName, initialValue.longValue());
    }

    //============================= create StyleableStringProperty =============================//

    /**
     * Create a StyleableStringProperty  with initial value and inherit flag.
     *
     * @param styleable    The <code>this</code> reference of the returned property. This is also the property bean.
     * @param propertyName The field name of the StyleableStringProperty
     * @param cssProperty  The CSS property name
     * @param function     A function that returns the StyleableStringProperty  that was created by this method call.
     * @param initialValue The initial value of the property. CSS may reset the property to this value.
     * @param inherits     Whether the CSS style can be inherited by child nodes
     * @return a StyleableProperty created with initial value and inherit flag
     */
    public final StyleableStringProperty createString(S styleable, String propertyName, String cssProperty,
                                                      Function<S, StyleableProperty<String>> function, String initialValue, boolean inherits) {
        CssMetaData<S, String> cssMetaData = createStringCss(cssProperty, function, initialValue, inherits);
        return new SimpleStyleableStringProperty(cssMetaData, styleable, propertyName, initialValue);
    }

    /**
     * Create a StyleableStringProperty  with initial value. The inherit flag defaults to false.
     *
     * @param styleable    The <code>this</code> reference of the returned property. This is also the property bean.
     * @param propertyName The field name of the StyleableStringProperty
     * @param cssProperty  The CSS property name
     * @param function     A function that returns the StyleableStringProperty  that was created by this method call.
     * @param initialValue The initial value of the property. CSS may reset the property to this value.
     * @return a StyleableProperty created with initial value and false inherit flag
     */
    public final StyleableStringProperty createString(S styleable, String propertyName, String cssProperty,
                                                      Function<S, StyleableProperty<String>> function, String initialValue) {
        return createString(styleable, propertyName, cssProperty, function, initialValue, false);
    }

    /**
     * Create a StyleableStringProperty . The initial value defaults to null and the inherit flag defaults to false.
     *
     * @param styleable    The <code>this</code> reference of the returned property. This is also the property bean.
     * @param propertyName The field name of the StyleableStringProperty
     * @param cssProperty  The CSS property name
     * @param function     A function that returns the StyleableStringProperty  that was created by this method call.
     * @return a StyleableProperty created with null initial value and false inherit flag
     */
    public final StyleableStringProperty createString(S styleable, String propertyName, String cssProperty,
                                                      Function<S, StyleableProperty<String>> function) {
        return createString(styleable, propertyName, cssProperty, function, null, false);
    }

    public final StyleableStringProperty createString(S styleable, String propertyName, String cssProperty, CssInvalidated<String> invalidated) {
        checkNotNullCssProperty(cssProperty);
        CssMetaData<S, String> cssMetaData = getCssMetaData(String.class, cssProperty);
        return new SimpleStyleableStringProperty(cssMetaData, styleable, propertyName, cssMetaData.getInitialValue(styleable)) {
            private WeakReference<String> weakReference = new WeakReference<>(null);

            @Override
            protected void invalidated() {
                String old = weakReference.get();
                acceptValue(cssMetaData, invalidated, old, get());
                weakReference = new WeakReference<>(get());
            }
        };
    }

    /**
     * Create a StyleableStringProperty  using previously created CssMetaData for the given <code>cssProperty</code>.
     *
     * @param styleable    The <code>this</code> reference of the returned property. This is also the property bean.
     * @param propertyName The field name of the StyleableStringProperty
     * @param cssProperty  The CSS property name
     * @return a StyleableProperty created using previously created CssMetaData
     * @throws IllegalArgumentException if <code>cssProperty</code> is null or empty
     * @throws NoSuchElementException   if the CssMetaData for <code>cssProperty</code> was not created prior to this method invocation
     */
    public final StyleableStringProperty createString(S styleable, String propertyName, String cssProperty) {
        checkNotNullCssProperty(cssProperty);
        CssMetaData<S, String> cssMetaData = getCssMetaData(String.class, cssProperty);
        return new SimpleStyleableStringProperty(cssMetaData, styleable, propertyName, cssMetaData.getInitialValue(styleable));
    }

    //============================= create StyleableStringProperty where String is a URL ========================//

    /**
     * Create a StyleableStringProperty  with initial value and inherit flag. Here, the String value represents
     * a URL converted from a <a href="http://www.w3.org/TR/CSS21/syndata.html#uri">CSS</a> url("{@literal <path>}").
     *
     * @param styleable    The <code>this</code> reference of the returned property. This is also the property bean.
     * @param propertyName The field name of the StyleableStringProperty
     * @param cssProperty  The CSS property name
     * @param function     A function that returns the StyleableStringProperty  that was created by this method call.
     * @param initialValue The initial value of the property. CSS may reset the property to this value.
     * @param inherits     Whether the CSS style can be inherited by child nodes
     * @return a StyleableProperty created with initial value and inherit flag
     */
    public final StyleableStringProperty createUrl(S styleable, String propertyName, String cssProperty,
                                                   Function<S, StyleableProperty<String>> function, String initialValue, boolean inherits) {
        CssMetaData<S, String> cssMetaData = createUrlCss(cssProperty, function, initialValue, inherits);
        return new SimpleStyleableStringProperty(cssMetaData, styleable, propertyName, initialValue);
    }

    /**
     * Create a StyleableStringProperty  with initial value. The inherit flag defaults to false.
     * Here, the String value represents a URL converted from a
     * <a href="http://www.w3.org/TR/CSS21/syndata.html#uri">CSS</a> url("{@literal <path>}").
     *
     * @param styleable    The <code>this</code> reference of the returned property. This is also the property bean.
     * @param propertyName The field name of the StyleableStringProperty
     * @param cssProperty  The CSS property name
     * @param function     A function that returns the StyleableStringProperty  that was created by this method call.
     * @param initialValue The initial value of the property. CSS may reset the property to this value.
     * @return a StyleableProperty created with initial value and false inherit flag
     */
    public final StyleableStringProperty createUrl(S styleable, String propertyName, String cssProperty,
                                                   Function<S, StyleableProperty<String>> function, String initialValue) {
        return createUrl(styleable, propertyName, cssProperty, function, initialValue, false);
    }

    /**
     * Create a StyleableStringProperty  with initial value. The inherit flag defaults to false.
     * Here, the String value represents a URL converted from a
     * <a href="http://www.w3.org/TR/CSS21/syndata.html#uri">CSS</a> url("{@literal <path>}").
     *
     * @param styleable    The <code>this</code> reference of the returned property. This is also the property bean.
     * @param propertyName The field name of the StyleableStringProperty
     * @param cssProperty  The CSS property name
     * @param function     A function that returns the StyleableStringProperty  that was created by this method call.
     * @return a StyleableProperty created with initial value and false inherit flag
     */
    public final StyleableStringProperty createUrl(S styleable, String propertyName, String cssProperty,
                                                   Function<S, StyleableProperty<String>> function) {
        return createUrl(styleable, propertyName, cssProperty, function, null, false);
    }

    /**
     * Create a StyleableStringProperty  using previously created CssMetaData for the given <code>cssProperty</code>.
     *
     * @param styleable    The <code>this</code> reference of the returned property. This is also the property bean.
     * @param propertyName The field name of the StyleableStringProperty
     * @param cssProperty  The CSS property name
     * @return a StyleableProperty created using previously created CssMetaData
     * @throws IllegalArgumentException if <code>cssProperty</code> is null or empty
     * @throws NoSuchElementException   if the CssMetaData for <code>cssProperty</code> was not created prior to this method invocation
     */
    public final StyleableStringProperty createUrl(S styleable, String propertyName, String cssProperty) {
        checkNotNullCssProperty(cssProperty);
        CssMetaData<S, String> cssMetaData = getCssMetaData(String.class, cssProperty);
        return new SimpleStyleableStringProperty(cssMetaData, styleable, propertyName, cssMetaData.getInitialValue(styleable));
    }

    public final StyleableStringProperty createUrl(S styleable, String propertyName, String cssProperty,
                                                   Consumer<String> invalidatedFnc) {
        checkNotNullCssProperty(cssProperty);
        CssMetaData<S, String> cssMetaData = getCssMetaData(String.class, cssProperty);
        return new SimpleStyleableStringProperty(cssMetaData, styleable, propertyName,
                cssMetaData.getInitialValue(styleable)) {
            @Override
            protected void invalidated() {
                if (invalidatedFnc != null) invalidatedFnc.accept(get());
            }
        };
    }

    //============================= =============================//

    /**
     * Create a CssMetaData&lt;S, Boolean&gt; with initial value, and inherit flag.
     *
     * @param cssProperty     The CSS property name.
     * @param function     A function that returns the StyleableBooleanProperty  that corresponds to this CssMetaData.
     * @param initialValue The initial value of the property. CSS may reset the property to this value.
     * @param inherits     Whether the CSS style can be inherited by child nodes
     * @return a CssMetaData created with initial value, and inherit flag
     * @throws IllegalArgumentException if <code>property</code> is null or an empty string, or <code>function</code> is null.
     */
    public final CssMetaData<S, Boolean> createBooleanCss(final String cssProperty, final Function<S, StyleableProperty<Boolean>> function, final boolean initialValue, final boolean inherits) {
        checkNotNullProperty(cssProperty, function);
        return getCssMetaData(Boolean.class, cssProperty, key -> {
            final StyleConverter<String, Boolean> converter = StyleConverter.getBooleanConverter();
            return new SimpleCssMetaData<>(key, function, converter, initialValue, inherits);
        });
    }

    /**
     * Create a CssMetaData&lt;S, Boolean&gt; with initial value, and inherit flag defaulting to false.
     *
     * @param cssProperty     The CSS property name.
     * @param function     A function that returns the StyleableBooleanProperty  that corresponds to this CssMetaData.
     * @param initialValue The initial value of the property. CSS may reset the property to this value.
     * @return a CssMetaData created with initial value, and false inherit flag
     * @throws IllegalArgumentException if <code>property</code> is null or an empty string, or <code>function</code> is null.
     */
    public final CssMetaData<S, Boolean> createBooleanCss(final String cssProperty, final Function<S, StyleableProperty<Boolean>> function, final boolean initialValue) {
        return createBooleanCss(cssProperty, function, initialValue, false);
    }

    /**
     * Create a CssMetaData&lt;S, Boolean&gt; with initial value and inherit flag both defaulting to false.
     *
     * @param cssProperty The CSS property name.
     * @param function A function that returns the StyleableBooleanProperty  that corresponds to this CssMetaData.
     * @return a CssMetaData created with false initial value, and false inherit flag
     * @throws IllegalArgumentException if <code>property</code> is null or an empty string, or <code>function</code> is null.
     */
    public final CssMetaData<S, Boolean> createBooleanCss(final String cssProperty, final Function<S, StyleableProperty<Boolean>> function) {
        return createBooleanCss(cssProperty, function, false, false);
    }

    //============================= =============================//

    /**
     * Create a CssMetaData&lt;S, Color&gt; with initial value, and inherit flag.
     *
     * @param cssProperty     The CSS property name.
     * @param function     A function that returns the StyleableProperty&lt;Color&gt; that corresponds to this CssMetaData.
     * @param initialValue The initial value of the property. CSS may reset the property to this value.
     * @param inherits     Whether the CSS style can be inherited by child nodes
     * @return a CssMetaData created with initial value, and inherit flag
     * @throws IllegalArgumentException if <code>property</code> is null or an empty string, or <code>function</code> is null.
     */
    public final CssMetaData<S, Color> createColorCss(final String cssProperty, final Function<S, StyleableProperty<Color>> function, final Color initialValue, final boolean inherits) {
        checkNotNullProperty(cssProperty, function);
        return getCssMetaData(Color.class, cssProperty, key -> {
            final StyleConverter<String, Color> converter = StyleConverter.getColorConverter();
            return new SimpleCssMetaData<>(cssProperty, function, converter, initialValue, inherits);
        });
    }

    /**
     * Create a CssMetaData&lt;S, Color&gt; with initial value, and inherit flag defaulting to false.
     *
     * @param cssProperty     The CSS property name.
     * @param function     A function that returns the StyleableProperty&lt;Color&gt; that corresponds to this CssMetaData.
     * @param initialValue The initial value of the property. CSS may reset the property to this value.
     * @return a CssMetaData created with initial value, and false inherit flag
     * @throws IllegalArgumentException if <code>property</code> is null or an empty string, or <code>function</code> is null.
     */
    public final CssMetaData<S, Color> createColorCss(final String cssProperty, final Function<S, StyleableProperty<Color>> function, final Color initialValue) {
        return createColorCss(cssProperty, function, initialValue, false);
    }

    /**
     * Create a CssMetaData&lt;S, Color&gt; with initial value of Color.BLACK, and inherit flag defaulting to false.
     *
     * @param cssProperty The CSS property name.
     * @param function A function that returns the StyleableProperty&lt;Color&gt; that corresponds to this CssMetaData.
     * @return a CssMetaData created with initial value, and false inherit flag
     * @throws IllegalArgumentException if <code>property</code> is null or an empty string, or <code>function</code> is null.
     */
    public final CssMetaData<S, Color> createColorCss(final String cssProperty, final Function<S, StyleableProperty<Color>> function) {
        return createColorCss(cssProperty, function, Color.BLACK, false);
    }

    //============================= =============================//

    /**
     * Create a CssMetaData&lt;S, Duration&gt; with initial value, and inherit flag.
     *
     * @param cssProperty     The CSS property name.
     * @param function     A function that returns the StyleableProperty&lt;Duration&gt; that corresponds to this CssMetaData.
     * @param initialValue The initial value of the property. CSS may reset the property to this value.
     * @param inherits     Whether the CSS style can be inherited by child nodes
     * @return a CssMetaData created with initial value, and inherit flag
     * @throws IllegalArgumentException if <code>property</code> is null or an empty string, or <code>function</code> is null.
     */
    public final CssMetaData<S, Duration> createDurationCss(final String cssProperty, final Function<S, StyleableProperty<Duration>> function, final Duration initialValue, final boolean inherits) {
        checkNotNullProperty(cssProperty, function);
        return getCssMetaData(Duration.class, cssProperty, key -> {
            final StyleConverter<?, Duration> converter = StyleConverter.getDurationConverter();
            return new SimpleCssMetaData<>(cssProperty, function, converter, initialValue, inherits);
        });
    }

    /**
     * Create a CssMetaData&lt;S, Duration&gt; with initial value, and inherit flag defaulting to false.
     *
     * @param cssProperty     The CSS property name.
     * @param function     A function that returns the StyleableProperty&lt;Duration&gt; that corresponds to this CssMetaData.
     * @param initialValue The initial value of the property. CSS may reset the property to this value.
     * @return a CssMetaData created with initial value, and false inherit flag
     * @throws IllegalArgumentException if <code>property</code> is null or an empty string, or <code>function</code> is null.
     */
    public final CssMetaData<S, Duration> createDurationCss(final String cssProperty, final Function<S, StyleableProperty<Duration>> function, final Duration initialValue) {
        return createDurationCss(cssProperty, function, initialValue, false);
    }

    /**
     * Create a CssMetaData&lt;S, Duration&gt; with initial value of Duration.BLACK, and inherit flag defaulting to false.
     *
     * @param cssProperty The CSS property name.
     * @param function A function that returns the StyleableProperty&lt;Duration&gt; that corresponds to this CssMetaData.
     * @return a CssMetaData created with initial value, and false inherit flag
     * @throws IllegalArgumentException if <code>property</code> is null or an empty string, or <code>function</code> is null.
     */
    public final CssMetaData<S, Duration> createDurationCss(final String cssProperty, final Function<S, StyleableProperty<Duration>> function) {
        return createDurationCss(cssProperty, function, Duration.UNKNOWN, false);
    }

    //============================= =============================//
    //                                                                                                              //
    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Create a CssMetaData&lt;S, Effect&gt; with initial value, and inherit flag.
     *
     * @param <E>          The CssMetaData created with initial value and inherit flag
     * @param cssProperty     The CSS property name.
     * @param function     A function that returns the StyleableProperty&lt;Effect&gt; that corresponds to this CssMetaData.
     * @param initialValue The initial value of the property. CSS may reset the property to this value.
     * @param inherits     Whether the CSS style can be inherited by child nodes
     * @return a CssMetaData created with initial value, and inherit flag
     * @throws IllegalArgumentException if <code>property</code> is null or an empty string, or <code>function</code> is null.
     */
    public final <E extends Effect> CssMetaData<S, E> createEffectCss(final Class<E> effectClass, final String cssProperty, final Function<S, StyleableProperty<E>> function, final E initialValue, final boolean inherits) {
        checkNotNullProperty(cssProperty, function);
        return getCssMetaData(effectClass, cssProperty, key -> {
            final StyleConverter<ParsedValue[], Effect> converter = StyleConverter.getEffectConverter();
            return new SimpleCssMetaData(cssProperty, function, converter, initialValue, inherits);
        });
    }

    /**
     * Create a CssMetaData&lt;S, Effect&gt; with initial value, and inherit flag defaulting to false.
     *
     * @param <E>          The CssMetaData created with initial value and false inherit flag
     * @param cssProperty     The CSS property name.
     * @param function     A function that returns the StyleableProperty&lt;Effect&gt; that corresponds to this CssMetaData.
     * @param initialValue The initial value of the property. CSS may reset the property to this value.
     * @return a CssMetaData created with initial value, and false inherit flag
     * @throws IllegalArgumentException if <code>property</code> is null or an empty string, or <code>function</code> is null.
     */
    public final <E extends Effect> CssMetaData<S, E> createEffectCss(final Class<E> effectClass, final String cssProperty, final Function<S, StyleableProperty<E>> function, final E initialValue) {
        return createEffectCss(effectClass, cssProperty, function, initialValue, false);
    }

    /**
     * Create a CssMetaData&lt;S, Effect&gt; with initial value of null, and inherit flag defaulting to false.
     *
     * @param <E>      The CssMetaData created with null initial value and false inherit flag
     * @param cssProperty The CSS property name.
     * @param function A function that returns the StyleableProperty&lt;Effect&gt; that corresponds to this CssMetaData.
     * @return a CssMetaData created with null initial value, and false inherit flag
     * @throws IllegalArgumentException if <code>property</code> is null or an empty string, or <code>function</code> is null.
     */
    public final <E extends Effect> CssMetaData<S, E> createEffectCss(final Class<E> effectClass, final String cssProperty, final Function<S, StyleableProperty<E>> function) {
        return createEffectCss(effectClass, cssProperty, function, null, false);
    }

    //============================= =============================//
    //                                                                                                              //
    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Create a CssMetaData&lt;S, Enum&gt; with initial value, and inherit flag.
     *
     * @param <E>          The CssMetaData created with initial value and inherit flag
     * @param enumClass    The Enum class that is the type of the CssMetaData&lt;E extends Enum&lt;E&gt;&gt;.
     * @param cssProperty     The CSS property name.
     * @param function     A function that returns the StyleableProperty&lt;Enum&gt; that corresponds to this CssMetaData.
     * @param initialValue The initial value of the property. CSS may reset the property to this value.
     * @param inherits     Whether the CSS style can be inherited by child nodes
     * @return a CssMetaData created with initial value, and inherit flag
     * @throws IllegalArgumentException if <code>property</code> is null or an empty string, or <code>function</code> is null.
     */
    public final <E extends Enum<E>> CssMetaData<S, E> createEnumCss(Class<E> enumClass, final String cssProperty, final Function<S, StyleableObjectProperty<E>> function, final E initialValue, final boolean inherits) {
        checkNotNullProperty(cssProperty, function);
        return getCssMetaData(enumClass, cssProperty, key -> {
            final EnumConverter<E> converter = new EnumConverter(enumClass);
            return new SimpleCssMetaData<>(cssProperty, function, converter, initialValue, inherits);
        });
    }

    /**
     * Create a CssMetaData&lt;S, Enum&gt; with initial value of null, and inherit flag defaulting to false.
     *
     * @param <E>       The CssMetaData created with null initial value and false inherit flag
     * @param cssProperty  The CSS property name.
     * @param function  A function that returns the StyleableProperty&lt;Enum&gt; that corresponds to this CssMetaData.
     * @param enumClass The Enum class that is the type of the CssMetaData&lt;E extends Enum&lt;E&gt;&gt;.
     * @return a CssMetaData created with null initial value, and false inherit flag
     * @throws IllegalArgumentException if <code>property</code> is null or an empty string, or <code>function</code> is null.
     */
    public final <E extends Enum<E>> CssMetaData<S, E> createEnumCss(final String cssProperty, final Function<S, StyleableObjectProperty<E>> function, Class<E> enumClass) {
        return createEnumCss(enumClass, cssProperty, function, null, false);
    }

    /**
     * Create a CssMetaData&lt;S, Enum&gt; with initial value, and inherit flag defaulting to false.
     *
     * @param <E>          The CssMetaData created with initial value and false inherit flag
     * @param cssProperty     The CSS property name.
     * @param function     A function that returns the StyleableProperty&lt;Enum&gt; that corresponds to this CssMetaData.
     * @param enumClass    The Enum class that is the type of the CssMetaData&lt;E extends Enum&lt;E&gt;&gt;.
     * @param initialValue The initial value of the property. CSS may reset the property to this value.
     * @return a CssMetaData created with initial value, and false inherit flag
     * @throws IllegalArgumentException if <code>property</code> is null or an empty string, or <code>function</code> is null.
     */
    public final <E extends Enum<E>> CssMetaData<S, E> createEnumCss(final String cssProperty, final Function<S, StyleableObjectProperty<E>> function, Class<E> enumClass, final E initialValue) {
        return createEnumCss(enumClass, cssProperty, function, initialValue, false);
    }

    //============================= =============================//
    //                                                                                                              //
    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Create a CssMetaData&lt;S, Font&gt; with initial value, and inherit flag.
     *
     * @param cssProperty     The CSS property name.
     * @param function     A function that returns the StyleableProperty&lt;Font&gt; that corresponds to this CssMetaData.
     * @param initialValue The initial value of the property. CSS may reset the property to this value.
     * @param inherits     Whether the CSS style can be inherited by child nodes
     * @return a CssMetaData created with initial value, and inherit flag
     * @throws IllegalArgumentException if <code>property</code> is null or an empty string, or <code>function</code> is null.
     */
    public final CssMetaData<S, Font> createFontCss(final String cssProperty, final Function<S, StyleableProperty<Font>> function, final Font initialValue, final boolean inherits) {
        checkNotNullProperty(cssProperty, function);
        return getCssMetaData(Font.class, cssProperty, key -> {
            final StyleConverter<ParsedValue[], Font> converter = StyleConverter.getFontConverter();
            return new SimpleCssMetaData<>(cssProperty, function, converter, initialValue, inherits);
        });
    }

    /**
     * Create a CssMetaData&lt;S, Font&gt; with initial value, and inherit flag defaulting to true.
     *
     * @param cssProperty     The CSS property name.
     * @param function     A function that returns the StyleableProperty&lt;Font&gt; that corresponds to this CssMetaData.
     * @param initialValue The initial value of the property. CSS may reset the property to this value.
     * @return a CssMetaData created with initial value, and true inherit flag
     * @throws IllegalArgumentException if <code>property</code> is null or an empty string, or <code>function</code> is null.
     */
    public final CssMetaData<S, Font> createFontCss(final String cssProperty, final Function<S, StyleableProperty<Font>> function, final Font initialValue) {
        return createFontCss(cssProperty, function, initialValue, true);
    }

    /**
     * Create a CssMetaData&lt;S, Font&gt; with initial value of {@link Font#getDefault()}, and inherit flag defaulting to true.
     *
     * @param cssProperty The CSS property name.
     * @param function A function that returns the StyleableProperty&lt;Font&gt; that corresponds to this CssMetaData.
     * @return a CssMetaData created with initial value, and true inherit flag
     * @throws IllegalArgumentException if <code>property</code> is null or an empty string, or <code>function</code> is null.
     */
    public final CssMetaData<S, Font> createFontCss(final String cssProperty, final Function<S, StyleableProperty<Font>> function) {
        return createFontCss(cssProperty, function, Font.getDefault(), true);
    }

    //============================= =============================//
    //                                                                                                              //
    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Create a CssMetaData&lt;S, Insets&gt; with initial value, and inherit flag.
     *
     * @param cssProperty     The CSS property name.
     * @param function     A function that returns the StyleableProperty&lt;Insets&gt; that corresponds to this CssMetaData.
     * @param initialValue The initial value of the property. CSS may reset the property to this value.
     * @param inherits     Whether the CSS style can be inherited by child nodes
     * @return a CssMetaData created with initial value, and inherit flag
     * @throws IllegalArgumentException if <code>property</code> is null or an empty string, or <code>function</code> is null.
     */
    public final CssMetaData<S, Insets> createInsetsCss(final String cssProperty, final Function<S, StyleableProperty<Insets>> function, final Insets initialValue, final boolean inherits) {
        checkNotNullProperty(cssProperty, function);
        return getCssMetaData(Insets.class, cssProperty, key -> {
            final StyleConverter<ParsedValue[], Insets> converter = StyleConverter.getInsetsConverter();
            return new SimpleCssMetaData<>(cssProperty, function, converter, initialValue, inherits);
        });
    }

    /**
     * Create a CssMetaData&lt;S, Insets&gt; with initial value, and inherit flag defaulting to false.
     *
     * @param cssProperty     The CSS property name.
     * @param function     A function that returns the StyleableProperty&lt;Insets&gt; that corresponds to this CssMetaData.
     * @param initialValue The initial value of the property. CSS may reset the property to this value.
     * @return a CssMetaData created with initial value, and false inherit flag
     * @throws IllegalArgumentException if <code>property</code> is null or an empty string, or <code>function</code> is null.
     */
    public final CssMetaData<S, Insets> createInsetsCss(final String cssProperty, final Function<S, StyleableProperty<Insets>> function, final Insets initialValue) {
        return createInsetsCss(cssProperty, function, initialValue, false);

    }

    /**
     * Create a CssMetaData&lt;S, Insets&gt; with initial value of {@link Insets#EMPTY}, and inherit flag defaulting to false.
     *
     * @param cssProperty The CSS property name.
     * @param function A function that returns the StyleableProperty&lt;Insets&gt; that corresponds to this CssMetaData.
     * @return a CssMetaData created with initial value, and false inherit flag
     * @throws IllegalArgumentException if <code>property</code> is null or an empty string, or <code>function</code> is null.
     */
    public final CssMetaData<S, Insets> createInsetsCss(final String cssProperty, final Function<S, StyleableProperty<Insets>> function) {
        return createInsetsCss(cssProperty, function, Insets.EMPTY, false);
    }

    //============================= =============================//
    //                                                                                                              //
    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Create a CssMetaData&lt;S, Paint&gt; with initial value, and inherit flag.
     *
     * @param cssProperty     The CSS property name.
     * @param function     A function that returns the StyleableProperty&lt;Paint&gt; that corresponds to this CssMetaData.
     * @param initialValue The initial value of the property. CSS may reset the property to this value.
     * @param inherits     Whether the CSS style can be inherited by child nodes
     * @return a CssMetaData created with initial value, and inherit flag
     * @throws IllegalArgumentException if <code>property</code> is null or an empty string, or <code>function</code> is null.
     */
    public final CssMetaData<S, Paint> createPaintCss(final String cssProperty, final Function<S, StyleableProperty<Paint>> function, final Paint initialValue, final boolean inherits) {
        checkNotNullProperty(cssProperty, function);
        return getCssMetaData(Paint.class, cssProperty, key -> {
            final StyleConverter<ParsedValue<?, Paint>, Paint> converter = StyleConverter.getPaintConverter();
            return new SimpleCssMetaData<>(cssProperty, function, converter, initialValue, inherits);
        });
    }

    /**
     * Create a CssMetaData&lt;S, Paint&gt; with initial value, and inherit flag defaulting to false.
     *
     * @param cssProperty     The CSS property name.
     * @param function     A function that returns the StyleableProperty&lt;Paint&gt; that corresponds to this CssMetaData.
     * @param initialValue The initial value of the property. CSS may reset the property to this value.
     * @return a CssMetaData created with initial value, and false inherit flag
     * @throws IllegalArgumentException if <code>property</code> is null or an empty string, or <code>function</code> is null.
     */
    public final CssMetaData<S, Paint> createPaintCss(final String cssProperty, final Function<S, StyleableProperty<Paint>> function, final Paint initialValue) {
        return createPaintCss(cssProperty, function, initialValue, false);
    }

    /**
     * Create a CssMetaData&lt;S, Paint&gt; with initial value of Color.BLACK, and inherit flag defaulting to false.
     *
     * @param cssProperty The CSS property name.
     * @param function A function that returns the StyleableProperty&lt;Paint&gt; that corresponds to this CssMetaData.
     * @return a CssMetaData created with initial value, and false inherit flag
     * @throws IllegalArgumentException if <code>property</code> is null or an empty string, or <code>function</code> is null.
     */
    public final CssMetaData<S, Paint> createPaintCss(final String cssProperty, final Function<S, StyleableProperty<Paint>> function) {
        return createPaintCss(cssProperty, function, null, false);
    }

    //============================= =============================//

    /**
     * Create a CssMetaData&lt;S, Number&gt; with initial value, and inherit flag.
     *
     * @param cssProperty     The CSS property name.
     * @param function     A function that returns the StyleableProperty&lt;Number&gt; that corresponds to this CssMetaData.
     * @param initialValue The initial value of the property. CSS may reset the property to this value.
     * @param inherits     Whether the CSS style can be inherited by child nodes
     * @return a CssMetaData created with initial value, and inherit flag
     * @throws IllegalArgumentException if <code>property</code> is null or an empty string, or <code>function</code> is null.
     */
    public final CssMetaData<S, Number> createSizeCss(final String cssProperty, final Function<S, StyleableProperty<Number>> function,
                                                      final Number initialValue, final boolean inherits) {
        checkNotNullProperty(cssProperty, function);
        return getCssMetaData(Number.class, cssProperty, key -> {
            final StyleConverter<?, Number> converter = StyleConverter.getSizeConverter();
            return new SimpleCssMetaData<>(cssProperty, function, converter, initialValue, inherits);
        });
    }

    /**
     * Create a CssMetaData&lt;S, Number&gt; with initial value, and inherit flag defaulting to false.
     *
     * @param cssProperty     The CSS property name.
     * @param function     A function that returns the StyleableProperty&lt;Number&gt; that corresponds to this CssMetaData.
     * @param initialValue The initial value of the property. CSS may reset the property to this value.
     * @return a CssMetaData created with initial value, and false inherit flag
     * @throws IllegalArgumentException if <code>property</code> is null or an empty string, or <code>function</code> is null.
     */
    public final CssMetaData<S, Number> createSizeCss(final String cssProperty, final Function<S, StyleableProperty<Number>> function, final Number initialValue) {
        return createSizeCss(cssProperty, function, initialValue, false);
    }

    /**
     * Create a CssMetaData&lt;S, Number&gt; with initial value of <code>0d</code>, and inherit flag defaulting to false.
     *
     * @param cssProperty The CSS property name.
     * @param function A function that returns the StyleableProperty&lt;Number&gt; that corresponds to this CssMetaData.
     * @return a CssMetaData created with initial value, and false inherit flag
     * @throws IllegalArgumentException if <code>property</code> is null or an empty string, or <code>function</code> is null.
     */
    public final CssMetaData<S, Number> createSizeCss(final String cssProperty, final Function<S, StyleableProperty<Number>> function) {
        return createSizeCss(cssProperty, function, 0d, false);
    }

    //============================= =============================//

    /**
     * Create a CssMetaData&lt;S, String&gt; with initial value, and inherit flag.
     *
     * @param cssProperty     The CSS property name.
     * @param function     A function that returns the StyleableStringProperty  that corresponds to this CssMetaData.
     * @param initialValue The initial value of the property. CSS may reset the property to this value.
     * @param inherits     Whether the CSS style can be inherited by child nodes
     * @return a CssMetaData created with initial value, and inherit flag
     * @throws IllegalArgumentException if <code>property</code> is null or an empty string, or <code>function</code> is null.
     */
    public final CssMetaData<S, String> createStringCss(final String cssProperty, final Function<S, StyleableProperty<String>> function, final String initialValue, final boolean inherits) {
        checkNotNullProperty(cssProperty, function);
        return getCssMetaData(String.class, cssProperty, key -> {
            final StyleConverter<String, String> converter = StyleConverter.getStringConverter();
            return new SimpleCssMetaData<>(cssProperty, function, converter, initialValue, inherits);
        });
    }

    /**
     * Create a CssMetaData&lt;S, String&gt; with initial value, and inherit flag defaulting to false.
     *
     * @param cssProperty     The CSS property name.
     * @param function     A function that returns the StyleableStringProperty  that corresponds to this CssMetaData.
     * @param initialValue The initial value of the property. CSS may reset the property to this value.
     * @return a CssMetaData created with initial value, and false inherit flag
     * @throws IllegalArgumentException if <code>property</code> is null or an empty string, or <code>function</code> is null.
     */
    public final CssMetaData<S, String> createStringCss(final String cssProperty, final Function<S, StyleableProperty<String>> function, final String initialValue) {
        return createStringCss(cssProperty, function, initialValue, false);
    }

    /**
     * Create a CssMetaData&lt;S, String&gt; with initial value of null, and inherit flag defaulting to false.
     *
     * @param cssProperty The CSS property name.
     * @param function A function that returns the StyleableStringProperty  that corresponds to this CssMetaData.
     * @return a CssMetaData created with null initial value, and false inherit flag
     * @throws IllegalArgumentException if <code>property</code> is null or an empty string, or <code>function</code> is null.
     */
    public final CssMetaData<S, String> createStringCss(final String cssProperty, final Function<S, StyleableProperty<String>> function) {
        return createStringCss(cssProperty, function, null, false);
    }


    //============================= =============================//

    public final <TP> StyleableObjectProperty<TP> createObject(S styleable, String propertyName, String cssProperty, final Class<TP> objectClass) {
        final CssInvalidated<TP> invalidated = (old, newVal) -> {
        };
        return createObject(styleable, propertyName, cssProperty, objectClass, invalidated);
    }

    public final <TP> StyleableObjectProperty<TP> createObject(S styleable, String propertyName, String cssProperty, final Class<TP> objectClass, CssInvalidated<TP> invalidatedFunc) {
        checkNotNullCssProperty(cssProperty);
        final CssMetaData<S, TP> cssMetaData = getCssMetaData(objectClass, cssProperty);
        final TP initialValue = cssMetaData.getInitialValue(styleable);
        return createObject(styleable, propertyName, cssProperty, objectClass, cssMetaData, initialValue, invalidatedFunc);
    }

    public final <TP> StyleableObjectProperty<TP> createObject(S styleable, String propertyName, String cssProperty,
                                                               final Class<TP> objectClass, CssMetaData<S, TP> cssMetaData, final TP initialValue, CssInvalidated<TP> invalidatedFunc) {
        checkNotNullCssProperty(cssProperty);
        return new SimpleStyleableObjectProperty<>(cssMetaData, styleable, propertyName, initialValue) {
            private WeakReference<TP> weakReference = new WeakReference<>(null);

            @Override
            protected void invalidated() {
                TP old = weakReference.get();
                acceptValue(cssMetaData, invalidatedFunc, old, get());
                weakReference = new WeakReference<>(get());
            }

        };
    }

    public final <F, TP> CssMetaData<S, TP> createObjectCss(final String cssProperty,
                                                            final Function<S, StyleableProperty<TP>> function, final StyleConverter<F, TP> converter,
                                                            final Class<TP> objectClass, final TP initialValue, final boolean inherits) {
        checkNotNullProperty(cssProperty, function);
        return getCssMetaData(objectClass, cssProperty, key -> {
            return new SimpleCssMetaData<>(cssProperty, function, converter, initialValue, inherits);
        });
    }

    public final <F, TP> CssMetaData<S, TP> createObjectCss(final String cssProperty,
                                                            final Function<S, StyleableProperty<TP>> function, final StyleConverter<F, TP> converter, final Class<TP> objectClass, final TP initialValue) {
        return createObjectCss(cssProperty, function, converter, objectClass, initialValue, false);
    }

    public final <F, TP> CssMetaData<S, TP> createObjectCss(final String cssProperty,
                                                            final Function<S, StyleableProperty<TP>> function, final StyleConverter<F, TP> converter, final Class<TP> objectClass) {
        return createObjectCss(cssProperty, function, converter, objectClass, null, false);
    }


    //============================= -fx-flex =============================//

    /**
     * Create a <b>StyleableProperty&lt;Flex&gt;</b> using previously created CssMetaData for the given <code>-fx-flex</code>.
     * @param styleable    The <code>this</code> reference of the returned property. This is also the property bean.
     * @return a StyleableProperty created using previously created CssMetaData
     */
    public final SimpleStyleableObjectProperty<Flex> createFlex(S styleable) {
        return createFlex(styleable, null);
    }

    /**
     * Create a <b>StyleableProperty&lt;Flex&gt;</b> using previously created CssMetaData for the given <code>cssProperty</code>.
     *
     * @param styleable    The <code>this</code> reference of the returned property. This is also the property bean.
     * @return a StyleableProperty created using previously created CssMetaData
     * @throws IllegalArgumentException if <code>cssProperty</code> is null or empty
     * @throws NoSuchElementException   if the CssMetaData for <code>cssProperty</code> was not created prior to this method invocation
     */
    public SimpleStyleableObjectProperty<Flex> createFlex(S styleable, CssInvalidated<Flex> invalidated) {
        String cssProperty =  "-fx-flex";

        CssMetaData<S, Flex> cssMetaData = getCssMetaData(Flex.class, cssProperty);
        return new SimpleStyleableObjectProperty<>(cssMetaData, styleable, "flex", cssMetaData.getInitialValue(styleable)) {
            private WeakReference<Flex> weakReference = new WeakReference<>(null);

            @Override
            protected void invalidated() {
                Flex old = weakReference.get();
                acceptValue(cssMetaData, invalidated, old, get());
                weakReference = new WeakReference<>(get());
            }
        };
    }


    /**
     * Create a <b>CssMetaData&lt;S, Flex&gt;</b> with initial value, and inherit flag.
     * @param function     A function that returns the StyleableProperty&lt;Flex&gt; that corresponds to this CssMetaData.
     * @return a CssMetaData created with initial value, and inherit flag
     * @throws IllegalArgumentException if <code>property</code> is null or an empty string, or <code>function</code> is null.
     */
    public final CssMetaData<S, Flex> createFlexCss(final Function<S, StyleableProperty<Flex>> function) {
        final String cssProperty = "-fx-flex";

        checkNotNullProperty(cssProperty, function);
        return getCssMetaData(Flex.class, cssProperty, key -> {
            final StyleConverter<String, Flex> converter = FlexConverter.getInstance();
            return new SimpleCssMetaData<>(cssProperty, function, converter, null, false);
        });
    }

    //============================= =============================//


    /**
     * Create a CssMetaData&lt;S, String&gt; with initial value, and inherit flag.
     * Here, the String value represents a URL converted from a
     * <a href="http://www.w3.org/TR/CSS21/syndata.html#uri">CSS</a> url({@literal "<path>"}).
     *
     * @param cssProperty     The CSS property name.
     * @param function     A function that returns the StyleableStringProperty  that corresponds to this CssMetaData.
     * @param initialValue The initial value of the property. CSS may reset the property to this value.
     * @param inherits     Whether the CSS style can be inherited by child nodes
     * @return a CssMetaData created with initial value, and inherit flag
     * @throws IllegalArgumentException if <code>property</code> is null or an empty string, or <code>function</code> is null.
     */
    public final CssMetaData<S, String> createUrlCss(final String cssProperty, final Function<S, StyleableProperty<String>> function, final String initialValue, final boolean inherits) {
        checkNotNullProperty(cssProperty, function);
        return getCssMetaData(String.class, cssProperty, key -> {
            final StyleConverter<ParsedValue[], String> converter = StyleConverter.getUrlConverter();
            return new SimpleCssMetaData<>(cssProperty, function, converter, initialValue, inherits);
        });
    }

    /**
     * Create a CssMetaData&lt;S, String&gt; with initial value, and inherit flag defaulting to false.
     * Here, the String value represents a URL converted from a
     * <a href="http://www.w3.org/TR/CSS21/syndata.html#uri">CSS</a> url({@literal "<path>"}).
     *
     * @param cssProperty     The CSS property name.
     * @param function     A function that returns the StyleableStringProperty  that corresponds to this CssMetaData.
     * @param initialValue The initial value of the property. CSS may reset the property to this value.
     * @return a CssMetaData created with initial value, and false inherit flag
     * @throws IllegalArgumentException if <code>property</code> is null or an empty string, or <code>function</code> is null.
     */
    public final CssMetaData<S, String> createUrlCss(final String cssProperty, final Function<S, StyleableProperty<String>> function, final String initialValue) {
        return createUrlCss(cssProperty, function, initialValue, false);
    }

    /**
     * Create a CssMetaData&lt;S, String&gt; with initial value of null, and inherit flag defaulting to false.
     * Here, the String value represents a URL converted from a
     * <a href="http://www.w3.org/TR/CSS21/syndata.html#uri">CSS</a> url("{@literal <path>}").
     *
     * @param cssProperty The CSS property name.
     * @param function A function that returns the StyleableStringProperty  that corresponds to this CssMetaData.
     * @return a CssMetaData created with null initial value, and false inherit flag
     * @throws IllegalArgumentException if <code>property</code> is null or an empty string, or <code>function</code> is null.
     */
    public final CssMetaData<S, String> createUrlCss(final String cssProperty, final Function<S, StyleableProperty<String>> function) {
        return createUrlCss(cssProperty, function, null, false);
    }

    //=============================================================================================//


    private <E> void checkNotNullProperty(String property, Function function) {
        Asserts.notBlank(property, "property cannot be null or empty string");
        Asserts.notNull(function, "function cannot be null");
    }


    public <E> CssMetaData<S, E> getCssMetaData(String fxProperty) {
        return getCssMetaData(null, fxProperty, null);
    }

    private <E> CssMetaData<S, E> getCssMetaData(final Class<E> convertCssCls, String fxProperty) {
        return getCssMetaData(convertCssCls, fxProperty, null);
    }

    /**
     * @param fxProperty the css name -> [-fx-property-css]
     * */
    private <E> CssMetaData<S, E> getCssMetaData(final Class<E> convertCssCls, String fxProperty, final Function<String, CssMetaData<S, E>> createCssMetaDataFunction) {
        runStaticApply();

        final String cssFxKey = fxProperty.toLowerCase();
        final Pair<Class, CssMetaData> entry = metaDataMap.get(cssFxKey);

        if (entry == null && createCssMetaDataFunction == null) {
            throw new NoSuchElementException("No CssMetaData for " + cssFxKey);
        }

        if (entry != null) {

            if (convertCssCls != null && entry.getKey() != convertCssCls) {
                String msg = "CssMetaData value is not %s: %s";
                throw new ClassCastException(msg.formatted(convertCssCls, entry.getValue()));
            }

            return entry.getValue();
        }

        // Entry was null
        CssMetaData<S, E> cssMetaData = createCssMetaDataFunction.apply(cssFxKey);
        metaDataMap.put(cssFxKey, new Pair(convertCssCls, cssMetaData));
        metaDataList.add(cssMetaData);
        return cssMetaData;
    }

    protected abstract void registerCss();

    private void runStaticApply() {
        if (!hasRegisterCssMeta) {
            hasRegisterCssMeta = true;
            registerCss();
        }
    }

    //=======================================

    private static class SimpleCssMetaData<S extends Styleable, V> extends CssMetaData<S, V> {
        protected final Function<S, ? extends StyleableProperty<V>> function;

        SimpleCssMetaData(final String property,
                          final Function<S, ? extends StyleableProperty<V>> function,
                          final StyleConverter<?, V> converter, final V initialValue,
                          final boolean inherits) {
            super(property, converter, initialValue, inherits);
            this.function = function;
        }


        public final boolean isSettable(S styleable) {
            final StyleableProperty<V> prop = getStyleableProperty(styleable);
            return prop instanceof Property pp ? !pp.isBound() : prop != null;
        }

        /**
         * {@inheritDoc}
         */
        public StyleableProperty<V> getStyleableProperty(S styleable) {
            return styleable != null ? function.apply(styleable) : null;
        }

    }

}