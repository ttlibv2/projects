package vn.conyeu.ts.service;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.conyeu.common.exception.Unauthorized;
import vn.conyeu.commons.utils.Classes;
import vn.conyeu.identity.helper.IdentityHelper;
import vn.conyeu.restclient.ClientLogger;
import vn.conyeu.restclient.LoggingFilter;
import vn.conyeu.ts.domain.ApiInfo;
import vn.conyeu.ts.domain.UserApi;
import vn.conyeu.ts.dtocls.Errors;
import vn.conyeu.ts.odcore.domain.ClsApiCfg;
import vn.conyeu.ts.odcore.service.OdBaseService;
import vn.conyeu.ts.ticket.service.OdTicketService;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service @Slf4j
public class OdService {
    final Map<Long, ServiceForUser> userServiceMap = new HashMap<>();
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

    public ServiceForUser load() {
        Long userId = IdentityHelper.extractUserId();
        if (!userServiceMap.containsKey(userId)) {
            ServiceForUser service = new ServiceForUser(userId);
            userServiceMap.put(userId, service);
            return service;
        }
        return userServiceMap.get(userId);
    }

    @PostConstruct
    protected void trySaveDefaultConfig() {

        cfgDefaultMap.clear();
        cfgDefaultMap.put("od.ticket", OdTicketService.DEFAULT_INFO);

        for (String apiCode : cfgDefaultMap.keySet()) {
            ClsApiCfg cls = cfgDefaultMap.get(apiCode);
            apiInfoService.tryCreateApi(apiCode, ai -> {
                ai.setCode(cls.getApiCode());
                ai.setTitle(cls.getApiTitle());
                ai.setBaseUrl(cls.getBaseUrl());
                ai.setLoginPath(cls.getLoginPath());
                ai.setHeaders(cls.getHeaders());
                ai.setQueries(cls.getQueries());
            });
        }
    }

    public class ServiceForUser {
        private final Long userId;
        private final Map<String, ClsApiCfg> apiCfgMap;
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
            String apiCode = clsApi.getApiCode();
            apiCfgMap.put(apiCode, clsApi);
            if (services.containsKey(apiCode)) {
                services.get(apiCode).updateConfig(clsApi);
            }
        }

        /**
         * Load api config service
         *
         * @param apiCode  the apiCode
         * @param override if true then load config from db
         */
        public ClsApiCfg loadApi(String apiCode, boolean override) {
            if (override || !apiCfgMap.containsKey(apiCode)) {
                updateConfig(userApiService
                        .findByApiCode(userId, apiCode).map(this::createClsApi)
                        //.orElseThrow(() -> Errors.noUserApiCode(apiCode)));
                        .orElseThrow(() -> new Unauthorized("ts_api").detail("ts_api", apiCode)
                                .message("Bạn chưa cấu hình tài khoản kết nối hệ thống")));
            }

            return apiCfgMap.get(apiCode);
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
            String apiCode = OdBaseService.determineServiceName(clsService);
            return loadService(apiCode, cfg -> Classes.newObject(clsService, cfg));
        }

        /**
         * Create or get service without service class
         *
         * @param <S>      the type service
         * @param apiCode  the apiCode service
         * @param function the function create service
         * @see OdBaseService#determineServiceName(Class)
         */
        public <S extends OdBaseService> S loadService(String apiCode, Function<ClsApiCfg, S> function) {
            ClsApiCfg clsApi = loadApi(apiCode, true);
            return setService(clsApi, function);
        }

        private ClsApiCfg createClsApi(UserApi ua) {
            ApiInfo ai = ua.getApi();
            ClsApiCfg cls = new ClsApiCfg();
            cls.setApiCode(ai.getCode());
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
            service.setSaveConfigDbConsumer((code, cls) -> {
                userApiService.updateByCode(userId, code, ua -> {
                    ua.setCsrfToken(cls.getCsrfToken());
                    ua.setCookie(cls.getCookie());
                    ua.setUserInfo(cls);
                });
            });
            services.put(clsApi.getApiCode(), service);
            return service;
        }

    }


}