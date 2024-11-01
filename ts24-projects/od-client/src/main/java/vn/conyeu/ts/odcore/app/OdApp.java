package vn.conyeu.ts.odcore.app;

import vn.conyeu.common.exception.BaseException;
import vn.conyeu.commons.beans.ObjectMap;
import vn.conyeu.commons.utils.Asserts;
import vn.conyeu.commons.utils.Objects;
import vn.conyeu.restclient.ClientLogger;
import vn.conyeu.restclient.LoggingFilter;
import vn.conyeu.ts.odcore.domain.ClsApiCfg;
import vn.conyeu.ts.odcore.domain.ClsUser;
import vn.conyeu.ts.odcore.service.OdClient;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public abstract class OdApp<C extends OdClient> {
    private final ClsApiCfg cfg;
    private final Map<Class<? extends C>, C> clsMap = new HashMap<>();
    private BiConsumer<ClsApiCfg, ClsUser> loginConsumer;
    private Function<String, ClientLogger> loggerFunc;

    public OdApp(ClsApiCfg config) {
        this.cfg = Asserts.notNull(config);
        this.validateConfig();
    }

    /**
     * Returns the APP_UID
     */
    public abstract String getAppUID();

    /**
     * Returns config info
     *
     * @return ClsApiCfg
     */
    public final ClsApiCfg config() {
        return cfg;
    }

    /**
     * Set the loginConsumer
     *
     * @param consumer the value
     */
    public OdApp loginConsumer(BiConsumer<ClsApiCfg, ClsUser> consumer) {
        if (loginConsumer == null) loginConsumer = consumer;
        else loginConsumer = loginConsumer.andThen(consumer);
        return this;
    }

    /**
     * Set the loggerFunc
     * @param loggerFunc the value
     */
    public OdApp<C> loggerFunc(Function<String, ClientLogger> loggerFunc) {
        this.loggerFunc = loggerFunc;
        this.clsMap.values().forEach(c -> c.loggerFunc(loggerFunc));
        return this;
    }

    /**
     * Action login app
     *
     * @see #login(String, String)
     */
    public ClsUser login() {
        cfg.validateLogin();

        // action login
        String user = cfg.getUsername();
        String pass = cfg.getPassword();
        ClsUser cls = login(user, pass);

        // update credential to config
        cfg.setCookieValue(cls.getCookie());
        cfg.setCsrfToken(cls.getCsrfToken());
        cfg.setClsUser(cls);

        // save to db
        if (loginConsumer != null) {
            loginConsumer.accept(cfg, cls);
        }

        return cls;
    }

    public abstract ObjectMap loadMenus();

    /**
     * Action login app
     *
     * @param username the username
     * @param password the password
     */
    protected abstract ClsUser login(String username, String password);

    /**
     * Validate config info
     */
    public void validateConfig() {
        String st = getAppUID(), cst = cfg.getAppUID();
        if (Objects.notEqual(st, cst)) throwSiteNameInvalid(cst, st);
        else this.cfg.validateBaseUrl();
    }

    /**
     * Returns service if register
     *
     * @param <E>        the service type
     * @param clsService the class service to get
     * @return service
     */
    protected <E extends C> E service(Class<E> clsService) {
        if (!clsMap.containsKey(clsService)) {
            String msg = "The service `%s` not register";
            throw Objects.newIllegal(msg, clsService);
        }

        return clsService.cast(clsMap.get(clsService));
    }

    /**
     * Find or create service
     *
     * @param <E> the service type
     * @param clsService the class service to get
     * @param supplierCreate the function create service if not exist
     * @return service
     */
    protected final <E extends C> E service(Class<E> clsService, Supplier<E> supplierCreate) {
        if (!clsMap.containsKey(clsService)) {
            E service = supplierCreate.get();
            service.loggerFunc(loggerFunc);
            clsMap.put(clsService, service);
            return service;
        } else {
            Object object = clsMap.get(clsService);
            return clsService.cast(object);
        }
    }

    public static void throwSiteNameInvalid(String inputSiteName, String siteName2) {
        throw BaseException.e500("site_name")
                .detail("api_site_name", inputSiteName).detail("site_name", siteName2)
                .message("Thông tin [site_name] truyền vào không đúng với ứng dụng (%s <> %s", siteName2, inputSiteName);
    }
}