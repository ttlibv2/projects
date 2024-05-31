package vn.conyeu.common.context;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.ErrorProperties;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.MessageSource;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.context.support.GenericWebApplicationContext;
import org.springframework.web.servlet.LocaleResolver;


@Component
public final class AppContext implements ApplicationContextAware {
    private static ApplicationContext applicationContext = null;
    private static GenericWebApplicationContext genericCtx = null;

    @Autowired
    public void setApplicationContext(@NonNull ApplicationContext applicationContext) throws BeansException {
        AppContext.applicationContext = applicationContext;
    }

    @Autowired
    public void setGenericWebApplicationContext(GenericWebApplicationContext ctx) {
        AppContext.genericCtx = ctx;
    }

    public static ApplicationContext appContext() {
        return applicationContext;
    }

    public static GenericWebApplicationContext genericCtx() {
        return genericCtx;
    }

    /**
     * Return an instance, which may be shared or independent, of the specified bean.
     * @param name the name of the bean to retrieve
     * @return an instance of the bean.
     */
    public static Object getBean(String name) throws BeansException {
        return appContext().getBean(name);
    }

    /**
     * Return the bean instance that uniquely matches the given object type, if any.
     * @param requiredType type the bean must match; can be an interface or superclass
     * @return an instance of the single bean matching the required type
     */
    public static <T> T getBean(Class<T> requiredType) throws BeansException {
        return appContext().getBean(requiredType);
    }

    /**
     * Return an instance, which may be shared or independent, of the specified bean
     * @param name         the name of the bean to retrieve
     * @param requiredType type the bean must match; can be an interface or superclass
     * @return an instance of the bean.
     */
    public static <T> T getBean(String name, Class<T> requiredType) throws BeansException {
        return appContext().getBean(name, requiredType);
    }

    public static ErrorProperties getErrorProperties() {
        return getBean(ErrorProperties.class);
    }

    public static ErrorAttributes getErrorAttributes() {
        return getBean(ErrorAttributes.class);
    }

    public static MessageSource getMessageSource() {
        return getBean(MessageSource.class);
    }

    public static LocaleResolver localeResolver() {
        return getBean(LocaleResolver.class);
    }



}