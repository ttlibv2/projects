package vn.conyeu.javafx.glisten;

import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.input.KeyCombination;
import javafx.stage.Screen;
import javafx.stage.Stage;
import vn.conyeu.commons.utils.Asserts;
import vn.conyeu.javafx.beans.CachedFactory;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class AppManager {

    /**
     * All Glisten-based applications should define one View with its name specified as HOME_VIEW.
     */
    public static final String HOME_VIEW = "home";

    /**
     * A Glisten-based application defines a View with its name specified as SPLASH_VIEW intended to be a one-time splash view.
     */
    public static final String SPLASH_VIEW = "splash";

    private static AppManager APPLICATION_MANAGER;

    private final Consumer<Scene> postInit;

    private Stage primaryStage;
    private Scene primaryScene;
    private GlassPane glassPane;
    private StatusBar statusBar;
    private AppBar appBar;

    private final CachedFactory<View> viewFactory = new CachedFactory();
    private final CachedFactory<Layer> layerFactory = new CachedFactory();
    private final Deque<String> viewStack = new ArrayDeque();

    /**
     * Creates an instance of the AppManager, providing a consumer that will be called for additional initialization code. It should be called only once.
     *
     * @param postInit a consumer with additional initialization code
     */
    public static AppManager initialize(Consumer<Scene> postInit) {
        Asserts.isNull(APPLICATION_MANAGER, "The AppManager was initialized already.");
        APPLICATION_MANAGER = new AppManager(postInit);
        return APPLICATION_MANAGER;
    }

    /**
     * Creates an instance of the AppManager. It should be called only once.
     */
    public static AppManager initialize() {
        return initialize(null);
    }

    public static AppManager getInstance() {
        return APPLICATION_MANAGER;
    }

    private AppManager(Consumer<Scene> postInit) {
        this.postInit = postInit;
    }

    /**
     * Once the {@link javafx.application.Application#start(javafx.stage.Stage)} method is called, this method should be called to initialize the scene and the {@link GlassPane}
     *
     * @param primaryStage the primary stage for this application, onto which the application scene will be set.
     */
    public final void start(Stage primaryStage) {
        this.primaryStage = Asserts.notNull(primaryStage);
        this.appBar = new AppBar();
        this.glassPane = new GlassPane();

        primaryScene = new Scene(glassPane, getScreenWidth(), getScreenHeight());

        //Swatch.getDefault().assignTo(primaryScene);
        //Theme.getDefault().assignTo(primaryScene);

        primaryStage.setScene(primaryScene);

        boolean splashInited = false;
        Optional<View> splashView = viewFactory.get(SPLASH_VIEW);
        if (splashView.isPresent() && splashView.get() instanceof SplashView) {
            splashInited = true;
            switchView(SPLASH_VIEW);
        }


    }

    /**
     * Returns the height of the screen that this application is running on.
     * */
    public final double getScreenHeight() {
        return Screen.getPrimary().getVisualBounds().getHeight();
    }

    /**
     * Returns the width of the screen that this application is running on.
     * */
    public final double getScreenWidth() {
        return Screen.getPrimary().getVisualBounds().getWidth();
    }

    public void setFullScreen(boolean value) {
        primaryStage.setFullScreen(value);
    }

    public void setTitle(String value) {
        primaryStage.setTitle(value);
    }

    public void setIconified(boolean value) {
        primaryStage.setIconified(value);
    }

    public void setMaximized(boolean value) {
        primaryStage.setMaximized(value);
    }

    public void setAlwaysOnTop(boolean value) {
        primaryStage.setAlwaysOnTop(value);
    }

    public void setResizable(boolean value) {
        primaryStage.setResizable(value);
    }

    public void setMinWidth(double value) {
        primaryStage.setMinWidth(value);
    }

    public void setMinHeight(double value) {
        primaryStage.setMinHeight(value);
    }

    public void setMaxWidth(double value) {
        primaryStage.setMaxWidth(value);
    }

    public void setMaxHeight(double value) {
        primaryStage.setMaxHeight(value);
    }

    public void setFullScreenExitKeyCombination(KeyCombination keyCombination) {
        primaryStage.setFullScreenExitKeyCombination(keyCombination);
    }

    public void setFullScreenExitHint(String value) {
        primaryStage.setFullScreenExitHint(value);
    }

    /**
     * @return Returns the {@link GlassPane} instance that is part of this application.
     */
    public GlassPane getGlassPane() {
        return glassPane;
    }

    /**
     * Returns the {@link AppBar} instance that is a part of this application.
     */
    public AppBar getAppBar() {
        return appBar;
    }

    /**
     * @return the {@link StatusBar} instance that is a part of this application.
     */
    public StatusBar getStatusBar() {
        return statusBar;
    }

    /**
     * Attempts to switch the view from the current view to a view represented by the given view name. If the view name does not exist, then the current view remains visible to users. If the view name does exist, then the view will be switched to the new view represented by this name. Previous view will be pushed on top of the view stack.
     *
     * @param <T>      the view type
     * @param viewName name of the view to switch to.
     * @return optional view for which switch was requested
     * @see #switchView(String, ViewStackPolicy)
     */
    public final <T extends View> Optional<T> switchView(String viewName) {
        return switchView(viewName, ViewStackPolicy.USE);
    }

    /**
     * Attempts to switch the view from the current view to a view represented by the given view name. If the view name does not exist, then the current view remains visible to users. If the view name does exist, then the view will be switched to the new view represented by this name. Previous view will be pushed on top of the view stack.
     *
     * @param <T>             the view type
     * @param viewName        name of the view to switch to.
     * @param viewStackPolicy ViewStackPolicy
     * @return optional view for which switch was requested
     */
    public final <T extends View> Optional<T> switchView(String viewName, ViewStackPolicy viewStackPolicy) {
        if (closeCurrentView()) return Optional.empty();
        return doSwitchView(viewName, viewStackPolicy);
    }

    /**
     * Attempts to switch to a previously used view if one exists.
     *
     * @param <T> the view type
     * @return optional previous view
     */
    public final <T extends View> Optional<T> switchToPreviousView() {
        if (!closeCurrentView() && viewStack.isEmpty()) {
            String viewName = viewStack.pop();
            return Optional.ofNullable(viewName).flatMap((vn) ->
                    doSwitchView(vn, ViewStackPolicy.SKIP)
            );
        }
        return Optional.empty();
    }

    /**
     * Returns to the home view and clears view stack.
     *
     * @param <T> the view type
     * @return optional view for which switch was requested
     */
    public final <T extends View> Optional<T> goHome() {
        return switchView(HOME_VIEW, ViewStackPolicy.CLEAR);
    }

    /**
     * Attempts to retrieve the view represented by the given view name. The result is wrapped in an Optional, to prevent null pointer exceptions.
     *
     * @param viewName The name of the {@link View}
     * @return an {@link Optional} with the {@link View}, if not empty
     */
    public final Optional<View> retrieveView(String viewName) {
        return viewFactory.get(viewName);
    }

    /**
     * Adds a new view factory to this application, if the given view name is unique (otherwise an IllegalArgumentException is thrown).
     * @param viewName  The name of the view - this will be used at other points in the application lifecycle to reference this view, and to switch to it.
     * @param supplier A Supplier instance that, when called, should return the view. This view is cached inside the mobile application, and as such is reused throughout the life of the application instance.
     * */
    public final void addViewFactory(String viewName, Supplier<View> supplier) {
        Asserts.isFalse(viewFactory.containsKey(viewName), "View with name '" + viewName + "' already exists - names must be unique");
        viewFactory.put(viewName, supplier);
    }

    /**
     * Removes a view factory with the given view name from this application, if it is present (optional operation).
     * @param viewName The name of the view which is mapped to the view factory.
     * @return true if the view name is present and is successfully removed from the application. false if the view name contained no mapping for a view factory.
     * */
    public final boolean removeViewFactory(String viewName) {
        boolean exists = viewFactory.containsKey(viewName);
        if (exists) viewFactory.remove(viewName);
        return exists;
    }

    /**
     * Returns true if a view factory with the view name exists in the application.
     * @param viewName The name of the view which is mapped to the view factory.
     * @return true if a view factory with the view name exists in the application.
     * */
    public final boolean isViewPresent(String viewName) {
        return viewFactory.containsKey(viewName);
    }

    /**
     * Adds a new layer factory to this application, if the given layer name is unique (otherwise an IllegalArgumentException is thrown).
     * @param layerName The name of the layer - this will be used at other points in the application lifecycle to reference this layer, and to show it.
     * @param supplier A Supplier instance that, when called, should return the Layer. This layer is cached inside the mobile application, and as such is reused throughout the life of the application instance.*/
    public final void addLayerFactory(String layerName, Supplier<Layer> supplier) {
        Asserts.isFalse(layerFactory.containsKey(layerName), "Layer with name '" + layerName + "' already exists - names must be unique");
            layerFactory.put(layerName, supplier);
    }

    /**
     * Removes a layer factory with the given layer name from this application, if it is present (optional operation).
     * @param layerName The name of the layer which is mapped to the layer factory.
     * @return true if the layer name is present and is successfully removed from the application. false if the layer name contained no mapping for a layer factory.
     * */
    public final boolean removeLayerFactory(String layerName) {
        boolean exists = layerFactory.containsKey(layerName);
        if (exists) layerFactory.remove(layerName);
        return exists;
    }

    /**
     * Returns true if a layer factory with the layer name exists in the application.
     * @param layerName The name of the layer which is mapped to the layer factory.
     * @return true if a layer factory with the layer name exists in the application.
     * */
    public final boolean isLayerPresent(String layerName) {
        return layerFactory.containsKey(layerName);
    }

    /**
     * Attempts to show a layer with the given name. Is equivalent to calling {@link Layer#show()}.
     * @param layerName The name of the layer which is mapped to the layer factory.
     * */
    public final void showLayer(String layerName) {
        layerFactory.get(layerName).ifPresent((layer) -> {
            layer.setId(layerName);
            layer.show();
        });
    }

    /**
     * Attempts to hide a layer with the given name. Is equivalent to calling {@link Layer#hide()}.
     * @param layerName The name of the layer which is mapped to the layer factory.
     * */
    public final void hideLayer(String layerName) {
        layerFactory.get(layerName).ifPresent(Layer::hide);
    }

    /**
     * If set to true, all visible layers, starting from top-most layer, are hidden when user interacts with the opaque region. By default, the value is set to false and only the top most layer is hidden on user interaction.
     * @param hideAllLayers boolean argument to hide all layers when user interacts with opaque layer
     * */
    public final void hideAllLayers(boolean hideAllLayers) {
        //GlassPaneHelper.setHideAllLayers(hideAllLayers);
    }

    private <T extends View> Optional<T> doSwitchView(String viewName, ViewStackPolicy viewStackPolicy) {
        if (ViewStackPolicy.CLEAR == viewStackPolicy) {
            retrieveView(viewName).map((newView) -> {
                viewStack.clear();
                setView(newView);
                return (T)newView;
            });
        }
        // ViewStackPolicy.USE | ViewStackPolicy.SKIP
        else {
            View currentView = getView();
            String currentViewName = ViewTools.findViewName(currentView);
            return currentView != null && currentViewName != null && currentViewName.equalsIgnoreCase(viewName) ? Optional.empty() : this.viewFactory.get(viewName).map((newView) -> {
                String newViewName = ViewTools.findViewName(newView);
                if (newViewName == null || newViewName.isEmpty()) {
                    ViewTools.storeViewName(newView, viewName);
                }

                if (ViewStackPolicy.USE == viewStackPolicy && currentView != null) {
                    this.viewStack.push(currentViewName);
                }

                if (currentView != null) {
                    Event.fireEvent(currentView, new LifecycleEvent(currentView, LifecycleEvent.HIDING));
                }

                this.setView(newView);
                return newView;
            });
        }
    }


    private boolean existsPreviousView() {
        return !this.viewStack.isEmpty();
    }

    private boolean closeCurrentView() {
        View currentView = getView();
        if (currentView != null) {
            Event onCloseRequest = new LifecycleEvent(currentView, LifecycleEvent.CLOSE_REQUEST);
            Event.fireEvent(currentView, onCloseRequest);
            return onCloseRequest.isConsumed();
        }
        return false;
    }


    //@formatter:off

    /** The currently showing View within this application. */
    private ReadOnlyObjectWrapper<View> view;
    public final View getView() {return viewPropertyImpl().get();}
    public final ReadOnlyObjectProperty<View> viewProperty() {return viewPropertyImpl().getReadOnlyProperty();}
    protected final void setView(View view) {viewPropertyImpl().set(view);}
    protected final ReadOnlyObjectWrapper<View> viewPropertyImpl() {
        if(view == null) {
            view = new ReadOnlyObjectWrapper<>(this, "view");
        }
        return view;
    }

    //@formatter:on
}