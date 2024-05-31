package vn.conyeu.ts.service;

import vn.conyeu.common.context.AppContext;
import vn.conyeu.common.exception.BaseException;
import vn.conyeu.commons.beans.ObjectMap;
import vn.conyeu.ts.domain.ApiInfo;
import vn.conyeu.ts.domain.UserApi;
import vn.conyeu.ts.odcore.domain.ClsApiConfig;
import vn.conyeu.ts.odcore.service.OdBaseService;
import vn.conyeu.ts.ticket.service.OdTicketService;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;

public class OdService {

    // Map<UserId, OdService>
    static final Map<Long, OdService> UserMap = new HashMap<>();

    /**
     * Get or create service for user
     * @param userId the user id
     * */
    public static OdService forUser(Long userId) {
        if(!UserMap.containsKey(userId)) {
            OdService service = new OdService(userId);
            UserMap.put(userId, service);
        }
        return UserMap.get(userId);
    }

    public static OdService updateApi(UserApi info) {
        Long userId = info.getUser().getId();
        OdService service = forUser(userId);
        service.updateConfig(info);
        return service;
    }

    //-----------------------------
    private final Long userId;
    private final Map<String, OdBaseService> serviceMap;
    private final ApiInfoService configService;

    public OdService(Long tsUserId) {
        this.userId = tsUserId;
        this.serviceMap = new HashMap<>();
        this.configService = AppContext.getBean(ApiInfoService.class);
    }

    /**
     * Update config info for api
     *
     * @param apiConfig the api info
     */
    public void updateConfig(ClsApiConfig apiConfig) {
        String apiCode = apiConfig.getApiCode();
        if (serviceMap.containsKey(apiCode)) {
            serviceMap.get(apiCode).updateConfig(apiConfig);
        }
    }

    /**
     * Update config info for api
     *
     * @param info the api info
     */
    public void updateConfig(UserApi info) {
        String apiCode = info.getConfig().getCode();
        if (serviceMap.containsKey(apiCode)) {
            OdBaseService service = serviceMap.get(apiCode);
            updateConfig(service.getConfig(), info);
        }
    }


    public OdTicketService ticketService() {
        return loadService(OdTicketService.SERVICE_NAME,
                OdTicketService.class, OdTicketService::new);
    }

    private <S extends OdBaseService<?>> S loadService(final String apiCode,
         final Class<S> clsService, Function<ClsApiConfig, S> newFunction) {

        if(serviceMap.containsKey(apiCode)) {
            OdBaseService service = serviceMap.get(apiCode);
            if(clsService.isInstance(service)) return (S) service;
            else throw BaseException.e500("invalid_service");
        }//
        else
        {
            ClsApiConfig cls = OdTicketService.DEFAULT;
            UserApi userApi = configService.loadByUser(userId,
                    apiCode, apiInfoConsumer(cls));

            ClsApiConfig clsNew = updateConfig(cls.clone(), userApi);
            S service = newFunction.apply(clsNew);
            serviceMap.put(apiCode, service);
            return service;

        }
    }

    private ClsApiConfig updateConfig(ClsApiConfig cls, UserApi api) {
        ApiInfo config = api.getConfig();

        cls.set(config.getBaseUrl(), cls::setBaseUrl);
        cls.set(config.getLoginPath(), cls::setLoginPath);
        cls.set(api.getCsrfToken(), cls::setCsrfToken);
        cls.set(config.getTitle(), cls::setApiTitle);
        cls.set(config.getLoginInfo(), cls::setLoginInfo);

        cls.set(ObjectMap.fromMap(
                config.getDefaultHeader(),
                api.getHeaders()), cls::setHeaders);

        cls.set(ObjectMap.fromMap(
                config.getDefaultQuery(),
                api.getQueries()), cls::setQueries);

        return cls;
    }

    private Consumer<ApiInfo> apiInfoConsumer(ClsApiConfig cls) {
        return info -> {
            info.setBaseUrl(cls.getBaseUrl());
            info.setLoginPath(cls.getLoginPath());
            info.setCode(cls.getApiCode());
            info.setTitle(cls.getApiTitle());
            info.setDefaultHeader(cls.getHeaders());
            info.setDefaultQuery(cls.getQueries());
            info.setDefaultUser(cls.getUserName());
            info.setDefaultSecret(cls.getPassword());
        };
    }


}