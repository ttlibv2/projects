package vn.conyeu.ts.service;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.conyeu.commons.utils.Classes;
import vn.conyeu.identity.helper.IdentityHelper;
import vn.conyeu.ts.domain.ApiInfo;
import vn.conyeu.ts.domain.UserApi;
import vn.conyeu.ts.dtocls.Errors;
import vn.conyeu.ts.odcore.domain.ClsApiCfg;
import vn.conyeu.ts.odcore.domain.ClsUser;
import vn.conyeu.ts.odcore.service.OdBaseService;
import vn.conyeu.ts.ticket.service.OdTicketService;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;

@Service
@Slf4j
public class OdService {
    final Map<Long, ServiceForUser> userServiceMap = new HashMap<>();
    final Map<String, ClsApiCfg> cfgDefaultMap = new HashMap<>();

    private final UserApiService userApiService;
    private final ApiInfoService apiInfoService;

    @Autowired
    public OdService(UserApiService userApiService, ApiInfoService apiInfoService) {
        this.userApiService = userApiService;
        this.apiInfoService = apiInfoService;
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
                services.get(apiCode).setConfig(clsApi);
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
                        .loadByApiCode(userId, apiCode).map(this::createClsApi)
                        .orElseThrow(() -> Errors.noUserApiCode(apiCode)));
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

            // get from cache
            //if (services.containsKey(apiCode)) {
           //     return (S) services.get(apiCode);
         //   }

            // get from config
           // if (apiCfgMap.containsKey(apiCode)) {
           //     ClsApiCfg clsApi = apiCfgMap.get(apiCode);
          //      return setService(clsApi, function);
          //  }

            // get from db
          //  else {
                ClsApiCfg clsApi = loadApi(apiCode, true);
                return setService(clsApi, function);
           // }

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