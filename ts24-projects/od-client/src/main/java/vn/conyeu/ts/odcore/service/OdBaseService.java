package vn.conyeu.ts.odcore.service;

import lombok.extern.slf4j.Slf4j;
import vn.conyeu.commons.utils.Objects;
import vn.conyeu.ts.odcore.domain.ClsApiCfg;
import vn.conyeu.ts.odcore.domain.ClsUser;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Supplier;

@Slf4j
public abstract class OdBaseService<T extends OdClient> {
    protected final Map<Class, T> clsMap = new HashMap<>();
    protected final String serviceName = determineServiceName(getClass());
    protected BiConsumer<String, ClsUser> saveConfigDbConsumer;

    protected final ClsApiCfg clsConfig;

    protected OdBaseService(ClsApiCfg clsConfig) {
        this.clsConfig = clsConfig;
    }

    public final void updateConfig(ClsApiCfg config) {
        this.clsConfig.updateFrom(config);
    }

    protected void updateConfig(ClsUser clsUser) {
        clsConfig.setClsUser(clsUser);
        clsConfig.setCookieValue(clsUser.getCookie());
        clsConfig.setCsrfToken(clsUser.getCsrfToken());
    }

    /**
     * Set the saveConfigDbConsumer
     *
     * @param consumer the value
     */
    public final void setSaveConfigDbConsumer(BiConsumer<String, ClsUser> consumer) {
        this.saveConfigDbConsumer = consumer;
    }

    public String getUniqueId() {
        return serviceName;
    }

    /**
     * Returns the clsConfig
     */
    public final ClsApiCfg getConfig() {
        return clsConfig;
    }

    protected <E extends T> E service(Class<E> serviceClass) {

        if (!clsMap.containsKey(serviceClass)) {
            String msg = "The service `%s` not register";
            throw Objects.newIllegal(msg, serviceClass);
        }
        return serviceClass.cast(clsMap.get(serviceClass));
    }

    protected <E extends T> E service(Class<E> serviceClass, Supplier<E> supplierCreate) {
        if(!clsMap.containsKey(serviceClass)) {
            E service = supplierCreate.get();
            service.setTryLoginFnc(this::login);
            log.warn("{}-->{}", serviceClass, service);
            clsMap.put(serviceClass, service);
            return service;
        }
        else {
            Object object = clsMap.get(serviceClass);
            return serviceClass.cast(object);
        }
    }

    public boolean isLogin() {
        return clsConfig.isLogin();
    }

    public static String determineServiceName(Class objectCls) {
        return Objects.camelCaseToDot(objectCls
                .getSimpleName().replace("Service", "")
                .replace("Client", "").trim()).toLowerCase();

    }

    public final ClsUser login() {
        ClsUser clsUser = loginImpl();
        updateConfig(clsUser);

        if(saveConfigDbConsumer != null) {
            saveConfigDbConsumer.accept(getUniqueId(), clsUser);
        }

        return clsUser;
    }

    protected abstract ClsUser loginImpl();
}