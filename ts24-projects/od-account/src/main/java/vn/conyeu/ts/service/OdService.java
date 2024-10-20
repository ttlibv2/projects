package vn.conyeu.ts.service;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.conyeu.common.exception.BaseException;
import vn.conyeu.commons.utils.Classes;
import vn.conyeu.ts.domain.ApiInfo;
import vn.conyeu.ts.domain.UserApi;
import vn.conyeu.ts.dtocls.TsErrors;
import vn.conyeu.ts.odcore.app.OdApp;
import vn.conyeu.ts.odcore.domain.ClsApiCfg;
import vn.conyeu.ts.ticket.service.TSApp;

import java.util.HashMap;
import java.util.Map;

@Service
public class OdService {
    private final Map<String, Class<? extends OdApp>> APP_CLS = new HashMap<>();// Map<APP_UID, OdApp>
    private final Map<String, ClsApiCfg> defaultConfigs = new HashMap<>(); // Map<APP_UID, ClsApiCfg>
    private final Map<Long, AppForUser> UserApp = new HashMap<>();
    private final ApiInfoService aiService;
    private final UserApiService uaService;
    private final HttpLogService logService;

    @Autowired
    public OdService(ApiInfoService aiService, UserApiService apiService, HttpLogService logService) {
        this.aiService = aiService;
        this.uaService = apiService;
        this.logService = logService;
    }

    @PostConstruct()
    protected final void registerDefaultApp() {
        registerApp(TSApp.APP_UID, TSApp.class, TSApp.DEFAULT_CONFIG);

        for(String app_uid:defaultConfigs.keySet()) {
            ClsApiCfg config = defaultConfigs.get(app_uid);
            aiService.tryCreateApi(app_uid, ai -> {
                ai.setAppName(app_uid);
                ai.setAppUID(app_uid);
                ai.setTitle(config.getTitle());
                ai.setBaseUrl(config.getBaseUrl());
                ai.setLoginPath(config.getLoginPath());
                ai.setHeaders(config.getHeaders());
                ai.setQueries(config.getQueries());
                ai.setCfgLink(config.getCfgMenuLinks());
                ai.setAllowCopy(false);
                ai.setIsSystem(true);
            });
        }
    }


    public AppForUser forUser(Long userId) {
        if(!UserApp.containsKey(userId)) {
            UserApp.put(userId, new AppForUser(userId));
        }
        return UserApp.get(userId);
    }



    //===================================================================
    // AppForUser
    //===================================================================
    public final class AppForUser {
        private final Long userId;
        private final Map<String, ClsApiCfg> configs;// Map<APP_NAME, ClsApiCfg>
        private final Map<String, OdApp> apps;// Map<APP_NAME, OdApp>

        AppForUser(Long userId) {
            this.userId = userId;
            this.apps = new HashMap<>();
            this.configs = new HashMap<>();
        }

        public void asyncConfig() {
            for (String apiCode : configs.keySet()) {
                loadConfig(apiCode, true);
            }
        }

        /**
         * Load api config without app_name
         * @param appName  the app name
         * @param override if true then load config from db
         */
        public ClsApiCfg loadConfig(String appName, boolean override) {
            if (override || !configs.containsKey(appName)) {
                editConfig(uaService
                        .findByAppName(userId, appName).map(this::createConfig)
                        .orElseThrow(() -> TsErrors.noApiUser(userId, appName)));
            }

            return configs.get(appName);
        }

        /**
         * update info config
         * @param config the config api
         */
        public void editConfig(ClsApiCfg config) {
            String appName = config.getAppName();
            if(!configs.containsKey(appName)) configs.put(appName, config);
            else configs.get(appName).update(config);
        }

        /**
         * Find or create app without appName
         * @param appName the name to find or create
         * */
        public <A extends OdApp> A loadApp(String appName) {
            if(!apps.containsKey(appName)){
                ClsApiCfg config = loadConfig(appName, false);
                Class<A> clsApp = findClsApp(config.getAppUID());
                OdApp<?> odApp = Classes.newObject(clsApp, config);
                odApp.loginConsumer((cfg, user) -> uaService.updateConsumer(
                        config.getAccountId(), appName,
                        ua -> ua.setUserInfo(user)
                ));
                apps.put(appName, odApp);
                return (A) odApp;
            }
            return (A)apps.get(appName);
        }

        private ClsApiCfg createConfig(UserApi ua) {
            ApiInfo ai = ua.getApi();
            ClsApiCfg cls = new ClsApiCfg();
            cls.setAppName(ai.getAppName());
            cls.setAppUID(ai.getAppUID());
            cls.setTitle(ai.getTitle());
            cls.setBaseUrl(ai.getBaseUrl());
            cls.setHeaders(ai.getHeaders());
            cls.setQueries(ai.getQueries());
            cls.setLoginPath(ai.getLoginPath());
            cls.setUsername(ua.getUserName());
            cls.setPassword(ua.getPassword());
            cls.setCsrfToken(ua.getCsrfToken());
            cls.setCookieValue(ua.getCookie());
            cls.setClsUser(ua.getUserInfo());
            cls.setAutoLogin(ua.isAutoLogin());
            cls.setAccountId(ua.getUserId());


//            cls.setCustomBuilderConsumer(builder -> {
//                LoggingFilter loggingFilter = new LoggingFilter(requestId -> {
//                    ClientLogger logger = new ClientLogger(requestId);
//                    logger.userLogin(cls.getAccountId());
//
//                    logger.submitResponseConsumer(response -> {
//                        logService.save(requestId, response);
//                    });
//
//                    logger.submitRequestConsumer(request -> {
//                        logService.save(requestId, request);
//                    });
//
//                    logger.submitLoggerConsumer(log -> {
//                        logService.save(requestId, logger);
//                    });
//
//                    return logger;
//                });
//
//                builder.filter(loggingFilter);
//            });

            return cls;
        }




    }



    public <C extends OdApp> void registerApp(String app_uid, Class<C> clsApp, ClsApiCfg defaultConfig) {
        if (APP_CLS.containsKey(app_uid)) {
            throw BaseException.e500("register_app")
                    .detail("app_uid", app_uid)
                    .message("The app '%s' has registry", app_uid);
        }

        APP_CLS.put(app_uid, clsApp);
        defaultConfigs.put(app_uid, defaultConfig);
    }

    protected <A extends OdApp> Class<A> findClsApp(String appUID) {
        if(APP_CLS.containsKey(appUID)) return (Class<A>) APP_CLS.get(appUID);
        else throw BaseException.e500("app_uid").detail("app_uid", appUID)
                .message("The app_uid `%s` not register", appUID);
    }

}