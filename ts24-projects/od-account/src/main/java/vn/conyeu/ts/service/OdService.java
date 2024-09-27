package vn.conyeu.ts.service;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.conyeu.common.exception.BaseException;
import vn.conyeu.common.exception.Unauthorized;
import vn.conyeu.commons.utils.Classes;
import vn.conyeu.identity.helper.IdentityHelper;
import vn.conyeu.restclient.ClientLogger;
import vn.conyeu.restclient.LoggingFilter;
import vn.conyeu.ts.domain.ApiInfo;
import vn.conyeu.ts.domain.UserApi;
import vn.conyeu.ts.odcore.domain.ClsApiCfg;
import vn.conyeu.ts.odcore.service.OdBaseService;
import vn.conyeu.ts.ticket.service.OdTicketService;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service @Slf4j
public class OdService {

    //Map<SERVICE_UID, Class<OdBaseService>>
    final Map<String, Class<? extends OdBaseService>> ALL_SERVICE_CLS = new HashMap<>();

    //Map<USER_ID, ServiceForUser>
    final Map<Long, ServiceForUser> userServiceMap = new HashMap<>();

    //Map<SERVICE_UID, ClsApiCfg>
    final Map<String, ClsApiCfg> cfgDefaultMap = new HashMap<>();

    private final UserApiService userApiService;
    private final ApiInfoService apiInfoService;
    private final HttpLogService logService;

    @Autowired
    public OdService(UserApiService userApiService, ApiInfoService apiInfoService, HttpLogService logService) {
        this.userApiService = userApiService;
        this.apiInfoService = apiInfoService;
        this.logService = logService;
    }

    public ServiceForUser load(Long userId) {
        if (!userServiceMap.containsKey(userId)) {
            ServiceForUser service = new ServiceForUser(userId);
            userServiceMap.put(userId, service);
            return service;
        }
        return userServiceMap.get(userId);
    }

    public ServiceForUser load() {
        return load(IdentityHelper.extractUserId());
    }

    private <S> Class<S> getClsService(String serviceUid) {
        if(!ALL_SERVICE_CLS.containsKey(serviceUid)) {
            throw BaseException.e500("no_sid").message("Khong tim thay service -- [%s]", serviceUid);
        }
        return (Class<S>) ALL_SERVICE_CLS.get(serviceUid);
    }

    @PostConstruct
    protected void trySaveDefaultConfig() {

        cfgDefaultMap.clear();
        cfgDefaultMap.put("od.ticket", OdTicketService.DEFAULT_INFO);
        ALL_SERVICE_CLS.put(OdTicketService.SERVICE_NAME, OdTicketService.class);

        for (String serviceUid : cfgDefaultMap.keySet()) {
            ClsApiCfg cls = cfgDefaultMap.get(serviceUid);
            apiInfoService.tryCreateApi(serviceUid, ai -> {
                ai.setServiceName(serviceUid);
                ai.setServiceUid(serviceUid);
                ai.setTitle(cls.getApiTitle());
                ai.setBaseUrl(cls.getBaseUrl());
                ai.setLoginPath(cls.getLoginPath());
                ai.setHeaders(cls.getHeaders());
                ai.setQueries(cls.getQueries());
                ai.setIsSystem(true);
            });
        }
    }

    public class ServiceForUser {
        private final Long userId;

        // Map<SERVICE_NAME, ClsApiCfg>
        private final Map<String, ClsApiCfg> apiCfgMap;

        // Map<SERVICE_NAME, OdBaseService>
        private final Map<String, OdBaseService> services;

        public ServiceForUser(Long userId) {
            this.userId = userId;
            this.apiCfgMap = new HashMap<>();
            this.services = new HashMap<>();
        }

        public OdTicketService ticketService() {
            return loadService(OdTicketService.class);
        }

        public ServiceForUser updateConfig() {
            for (String apiCode : apiCfgMap.keySet()) {
                loadApi(apiCode, true);
            }
            return this;
        }

        /**
         * Set config api
         *
         * @param clsApi the config api
         */
        public void updateConfig(ClsApiCfg clsApi) {
            String serviceName = clsApi.getServiceName();
            apiCfgMap.put(serviceName, clsApi);
            if (services.containsKey(serviceName)) {
                services.get(serviceName).updateConfig(clsApi);
            }
        }

        /**
         * Load api config service
         *
         * @param serviceName  the apiCode
         * @param override if true then load config from db
         */
        public ClsApiCfg loadApi(String serviceName, boolean override) {
            if (override || !apiCfgMap.containsKey(serviceName)) {
                updateConfig(userApiService
                        .findByServiceName(userId, serviceName).map(this::createClsApi)
                        //.orElseThrow(() -> TsErrors.noUserApiCode(apiCode)));
                        .orElseThrow(() -> new Unauthorized("ts_api").detail("ts_api", serviceName)
                                .message("Bạn chưa cấu hình tài khoản kết nối hệ thống")));
            }

            return apiCfgMap.get(serviceName);
        }

        public <S extends OdBaseService> S loadService(String serviceName) {
            ClsApiCfg clsApiCfg = loadApi(serviceName, false);
            return loadService(getClsService(clsApiCfg.getServiceUid()));
        }

        /**
         * Create or get service without service class
         *
         * @param <S>        the type service
         * @param clsService the class service
         * @see #loadService(String, Function)
         * @see OdBaseService#determineServiceName(Class)
         */
        public <S extends OdBaseService> S loadService(Class<S> clsService) {
            String serviceName = OdBaseService.determineServiceName(clsService);
            return loadService(serviceName, cfg -> Classes.newObject(clsService, cfg));
        }

        /**
         * Create or get service without service class
         *
         * @param <S>         the type service
         * @param serviceName the apiCode service
         * @param function    the function create service
         * @see OdBaseService#determineServiceName(Class)
         */
        public <S extends OdBaseService> S loadService(String serviceName, Function<ClsApiCfg, S> function) {
            ClsApiCfg clsApi = loadApi(serviceName, true);
            return setService(clsApi, function);
        }

        private ClsApiCfg createClsApi(UserApi ua) {
            ApiInfo ai = ua.getApi();
            ClsApiCfg cls = new ClsApiCfg();
            cls.setServiceName(ai.getServiceName());
            cls.setServiceUid(ai.getServiceUid());
            cls.setApiTitle(ai.getTitle());
            cls.setBaseUrl(ai.getBaseUrl());
            cls.setHeaders(ai.getHeaders());
            cls.setQueries(ai.getQueries());
            cls.setLoginPath(ai.getLoginPath());
            cls.setUserName(ua.getUserName());
            cls.setPassword(ua.getPassword());
            cls.setCsrfToken(ua.getCsrfToken());
            cls.setCookieValue(ua.getCookie());
            cls.setClsUser(ua.getUserInfo());
            cls.setAutoLogin(ua.isAutoLogin());
            cls.setCustomBuilderConsumer(builder -> {
                LoggingFilter loggingFilter = new LoggingFilter(requestId -> {
                    ClientLogger logger = new ClientLogger(requestId);
                    logger.userLogin(IdentityHelper.extractUserId());

                    logger.submitResponseConsumer(response -> {
                        logService.save(requestId, response);
                    });

                    logger.submitRequestConsumer(request -> {
                        logService.save(requestId, request);
                    });

                    logger.submitLoggerConsumer(log -> {
                        logService.save(requestId, logger);
                    });

                    return logger;
                });

                builder.filter(loggingFilter);
            });

            return cls;
        }

        private <S extends OdBaseService<?>> S setService(ClsApiCfg clsApi, Function<ClsApiCfg, S> function) {
            S service = function.apply(clsApi);
            service.setLoginConsumer((cfg, cls) -> {
                userApiService.updateConsumer(userId, cfg.getServiceName(), ua -> {
                    ua.setCsrfToken(cls.getCsrfToken());
                    ua.setCookie(cls.getCookie());
                    ua.setUserInfo(cls);
                });
            });
            services.put(clsApi.getServiceName(), service);
            return service;
        }

    }


}