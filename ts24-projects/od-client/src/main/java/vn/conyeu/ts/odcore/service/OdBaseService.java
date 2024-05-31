package vn.conyeu.ts.odcore.service;

import vn.conyeu.commons.utils.Objects;
import vn.conyeu.ts.odcore.domain.ClsApiConfig;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public abstract class OdBaseService<T extends OdClient> {
    protected final Map<Class, T> clsMap = new HashMap<>();
    protected final ClsApiConfig clsConfig;

    protected OdBaseService(ClsApiConfig clsConfig) {
        this.clsConfig = clsConfig;
    }

    public abstract String getUniqueId();

    public void updateConfig(ClsApiConfig config) {
        clsConfig.updateFrom(config);
        clsConfig.validate();
    }

    /**
     * Returns the clsConfig
     */
    public final ClsApiConfig getConfig() {
        return clsConfig;
    }

    protected  <E extends T> E service(Class<E> serviceClass) {

        if(!clsMap.containsKey(serviceClass)) {
            String msg = "The service `%s` not register";
            throw Objects.newIllegal(msg, serviceClass);
        }
        return serviceClass.cast(clsMap.get(serviceClass));
    }

    protected <E extends T> E service(Class<E> serviceClass, Supplier<E> supplierCreate) {
        Object object = clsMap.computeIfAbsent(serviceClass, cls -> supplierCreate.get());
        return serviceClass.cast(object);
    }

    public boolean isLogin() {
        return clsConfig.isLogin();
    }


}