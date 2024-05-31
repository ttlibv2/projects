package vn.conyeu.javafx.icons.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import vn.conyeu.commons.utils.Asserts;
import vn.conyeu.commons.utils.Objects;

import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Stream;

public abstract class IconResolve<I extends IconDesc, H extends IconHandler<I>> {
    private static final String ICON_STRICT = "javafx.icons.strict";
    protected static final String TX_REGISTER = "registerHandler";
    protected static final String TX_UNREGISTER = "unregisterHandler";
    protected final Logger logger;

    private final Set<H> handlers = new LinkedHashSet<>();
    private final Set<H> customHandlers = new LinkedHashSet<>();
    private final Class<H> handlerClass;

    protected IconResolve(Class<H> handlerClass) {
        this.handlerClass = handlerClass;
        this.logger = LoggerFactory.getLogger(getClass());
        this.loadHandlerFromLoader(this::applyHandlerClasspath);
    }

    /**
     * Register handler for icon
     * @param handler the handler
     * */
    public boolean registerHandler(H handler) {
        if (isLoadedViaClasspath(handler, handlers)) {
            throwOrWarn(TX_REGISTER, handler);
            return false;
        }
        return customHandlers.add(handler);
    }

    /**
     * UnRegister handler for icon
     * @param handler the handler
     * */
    public boolean unregisterHandler(H handler) {
        if (isLoadedViaClasspath(handler, handlers)) {
            throwOrWarn(TX_UNREGISTER, handler);
            return false;
        }
        return customHandlers.remove(handler);
    }

    /**
     * Find handler without description icon
     * @param description the description icon
     * @return handler if exist
     * */
    public final H resolve(String description) {
        Asserts.notBlank(description, "Icon description must not be null");
        return Stream.of(handlers, customHandlers).flatMap(Set::stream)
                .filter(handler -> handler.supports(description)).findFirst()
                .orElseThrow(() -> Objects.newIllegal("Cannot resolve icon `%s`", description));
    }

    protected final void loadHandlerFromLoader(Consumer<H> handlerConsumer) {
        ServiceLoader<H> loader = resolveServiceLoader();
        for (H handler : loader) {
            handlers.add(handler);
            handlerConsumer.accept(handler);
        }
    }

    /**
     * Returns true if handler exists in service loader
     * @see #loadHandlerFromLoader(Consumer)
     * */
    protected boolean isLoadedViaClasspath(H handler, Set<H> handlers) {
        String fontFamily = handler.getFontFamily();
        return handlers.stream().anyMatch(h -> h.getFontFamily().equals(fontFamily));
    }

    /**
     * Returns message for register or unregister handler
     * @param method the method
     * @param handler the handler
     * */
    protected String buildMessageThrowOrWarn(String method, H handler) {
        Asserts.notNull(handler, "The handler is null");
        String handleClsName = handler.getClass().getSimpleName();
        String msg = TX_REGISTER.equals(method) ? "%s for `%s` is already loaded via classpath"
                : "%s for `%s` was loaded via classpath and can't be unregistered";

        return String.format(msg, handleClsName, handler.getFontFamily());
    }

    /**
     * Apply handler for child
     * @param handler the handler
     * */
    protected abstract void applyHandlerClasspath(H handler);

    /**
     * Find ServiceLoader without handler class
     * @return ServiceLoader
     * */
    protected final ServiceLoader<H> resolveServiceLoader() {

        // Check if handlers must be loaded from a ModuleLayer
        final ModuleLayer layer = handlerClass.getModule().getLayer();
        if (layer != null) {
            ServiceLoader<H> handlers = ServiceLoader.load(layer, handlerClass);
            if (handlers.stream().findAny().isPresent()) return handlers;
        }

        // Check if the handlerClass works
        ServiceLoader<H> handlers = ServiceLoader.load(handlerClass, handlerClass.getClassLoader());
        if (handlers.stream().findAny().isPresent()) return handlers;

        // If *nothing* else works
        return ServiceLoader.load(handlerClass);
    }

    protected final boolean strictChecksEnabled() {
        return System.getProperty(ICON_STRICT) == null || Boolean.getBoolean(ICON_STRICT);
    }

    protected final void throwOrWarn(String method, H handler) {
        String message = buildMessageThrowOrWarn(method, handler);
        if (strictChecksEnabled()) throw new IllegalArgumentException(message);
        else logger.warn(message);
    }
}