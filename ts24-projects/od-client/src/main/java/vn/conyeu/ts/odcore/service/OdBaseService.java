//package vn.conyeu.ts.odcore.service;
//
//import lombok.extern.slf4j.Slf4j;
//import vn.conyeu.commons.beans.ObjectMap;
//import vn.conyeu.commons.utils.Objects;
//import vn.conyeu.ts.odcore.domain.ClsApiCfg;
//import vn.conyeu.ts.odcore.domain.ClsHelper;
//import vn.conyeu.ts.odcore.domain.ClsUser;
//
//import java.lang.annotation.Annotation;
//import java.util.HashMap;
//import java.util.Map;
//import java.util.function.BiConsumer;
//import java.util.function.Supplier;
//
//@Slf4j
//public abstract class OdBaseService<T extends OdClient2> {
//    protected final Map<Class, T> clsMap = new HashMap<>();
//    //protected BiConsumer<String, ClsUser> saveConfigDbConsumer;
//    protected BiConsumer<ClsApiCfg, ClsUser> loginConsumer;
//
//    protected final ClsApiCfg clsConfig;
//
//    protected OdBaseService(ClsApiCfg clsConfig) {
//        this.clsConfig = clsConfig;
//    }
//
//    public final void updateConfig(ClsApiCfg config) {
//        this.clsConfig.updateFrom(config);
//    }
//
//    protected void updateConfig(ClsUser clsUser) {
//        ClsHelper.updateConfig(clsConfig, clsUser);
//    }
//
//    /**
//     * Set the loginConsumer
//     *
//     * @param loginConsumer the value
//     */
//    public void setLoginConsumer(BiConsumer<ClsApiCfg, ClsUser> loginConsumer) {
//        this.loginConsumer = loginConsumer;
//    }
//
//
//    public abstract String getUniqueId();
//
//    /**
//     * Returns the clsConfig
//     */
//    public final ClsApiCfg getConfig() {
//        return clsConfig;
//    }
//
//    protected <E extends T> E service(Class<E> serviceClass) {
//
//        if (!clsMap.containsKey(serviceClass)) {
//            String msg = "The service `%s` not register";
//            throw Objects.newIllegal(msg, serviceClass);
//        }
//        return serviceClass.cast(clsMap.get(serviceClass));
//    }
//
//    protected <E extends T> E service(Class<E> serviceClass, Supplier<E> supplierCreate) {
//        if(!clsMap.containsKey(serviceClass)) {
//            E service = supplierCreate.get();
//            service.setTryLoginFnc(this::login);
//            log.warn("{}-->{}", serviceClass, service);
//            clsMap.put(serviceClass, service);
//            return service;
//        }
//        else {
//            Object object = clsMap.get(serviceClass);
//            return serviceClass.cast(object);
//        }
//    }
//
//    public boolean isLogin() {
//        return clsConfig.isLogin();
//    }
//
//    public static String determineServiceName(Class objectCls) {
//        Annotation serviceUID = objectCls.getAnnotation(ServiceUID.class);
//        if(serviceUID instanceof ServiceUID uid) return uid.value();
//
//        return Objects.camelCaseToDot(objectCls
//                .getSimpleName().replace("Service", "")
//                .replace("Client", "").trim()).toLowerCase();
//
//    }
//
//    public final ClsUser login() {
//        ClsUser clsUser = loginImpl();
//
//        // update credential
//        updateConfig(clsUser);
//
//        if(loginConsumer != null) {
//            loginConsumer.accept(clsConfig, clsUser);
//        }
//
//        return clsUser;
//    }
//
//    protected abstract ClsUser loginImpl();
//
//    public abstract ObjectMap loadMenus();
//}