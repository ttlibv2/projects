package vn.conyeu.ts.odcore.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient.RequestBodyUriSpec;
import vn.conyeu.common.exception.BaseException;
import vn.conyeu.common.exception.Unauthorized;
import vn.conyeu.commons.beans.ObjectMap;
import vn.conyeu.commons.utils.Asserts;
import vn.conyeu.commons.utils.Lists;
import vn.conyeu.commons.utils.Objects;
import vn.conyeu.restclient.ClientBuilder;
import vn.conyeu.restclient.ClientLogger;
import vn.conyeu.restclient.LoggingFilter;
import vn.conyeu.restclient.RClient;
import vn.conyeu.ts.odcore.domain.*;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

@Slf4j
public abstract class OdClient {
    protected final ClsApiCfg cfg;
    protected Function<String, ClientLogger> loggerFunc;

    //protected Consumer<LoggingFilter> loggingFilterConsumer;

    public OdClient(ClsApiCfg apiConfig) {
        this.cfg = apiConfig;
    }

    /**
     * Returns the cfg
     */
    public final ClsApiCfg cfg() {
        return cfg;
    }

    public static BaseException notLogin(String apiCode) {
        return new Unauthorized("ts_api").detail("ts_api", apiCode)
                .message("Bạn chưa cấu hình tài khoản kết nối hệ thống");
    }

    /**
     * Set the loggerFunc
     * @param loggerFunc the value
     */
    public OdClient loggerFunc(Function<String, ClientLogger> loggerFunc) {
        this.loggerFunc = loggerFunc;
        return this;
    }

    /**
     * Returns the model api
     */
    public abstract String getModel();

    public String getLogModel() {
        String name = getClass().getSimpleName();
        if(name.startsWith("Od")) {
            return "od."+name.substring(2).toLowerCase();
        }
        return name;
    }

    /**
     * Create RClient ClientBuilder
     */
    protected ClientBuilder clientBuilder() {
        ClientBuilder cb = RClient.builder().baseUrl(getApiUrl())
                .defaultContentType(MediaType.APPLICATION_JSON)
                .defaultHeader("tsModelName", getLogModel())
                .defaultHeader("tsApiUserId", cfg.getAccountId());

        if(loggerFunc != null) {
            cb.filter(new LoggingFilter(loggerFunc));
        }

        return cb;
    }

    protected RClient createClient() {
        ClientBuilder clientBuilder = clientBuilder();
        clientBuilder.defaultQueries(cfg.getQueries());
        clientBuilder.defaultHeaders(cfg.getHeaders());
        clientBuilder.defaultHeader("cookie", cfg.getCookie());
        clientBuilder.defaultCsrfToken(cfg.getCsrfToken());
        return clientBuilder.build();
    }
    /**
     * Returns basePath -> /dataset/[URL]
     *
     * @param paths the url dataset
     */
    protected String datasetUri(String... paths) {
        List<String> strings = Lists.newList("/dataset", Arrays.asList(paths));
        return String.join("/", strings);
    }

    /**
     * Returns call_kw uri
     *
     * @param method the method call
     */
    protected String call_kwUri(String method) {
        return datasetUri("/call_kw/" + getModel() + "/" + method);
    }

    /**
     * Returns list field of model
     *
     * @param key       the key to get
     * @param modelName the model to get
     */
    protected List<String> getFields(String key, String modelName) {
        modelName = Objects.firstNotBlank(modelName, getModel());
        ObjectMap map = ClsHelper.loadJson().getMap("fields." + modelName);
         return Objects.isEmpty(map) ? new LinkedList<>() : map.getStringList(key);
    }

    protected ClsUserContext createUserContext() {
        cfg.checkUserLogin();
        ClsUser clsUser = cfg.getClsUser();
        Long userId = clsUser.getId();
        Object compId = clsUser.getCompany_Uid();
        ClsUserContext ctx = clsUser.getContext();
        ctx.set("allowed_company_ids", new Object[]{compId});
        return ClsUserContext.fix(userId, ctx);
    }

    /**
     * Returns the api url
     */
    protected String getApiUrl() {
        return cfg.getBaseUrl();
    }

    protected String getApiUrlNoWeb() {
        String str = cfg.getBaseUrl();
        if(str.endsWith("/web")) return str.substring(0, str.length()-4);
        else return str;
    }


//    /**
//     * Execute send data with POST
//     *
//     * @param body the data body
//     * @param uri  the uri send
//     */
//    public final ObjectMap post(Object body, URI uri) {
//        return sendBody(body, spec -> spec.uri(uri));
//    }

    /**
     * Execute send data with POST
     *
     * @param body the data body
     * @param uri  the URI for the request using a URI template and URI variables
     * @param uriVariables the variables
     */
    public final ObjectMap post(Object body, String uri, Object...uriVariables) {
        return sendBody(body, spec -> spec.uri(uri, uriVariables), 0);
    }

    //    /**
//     * Execute send data with POST
//     *
//     * @param body the data body
//     * @param uri  the URI for the request using a URI template and URI variables
//     * @param uriVariables the variables
//     */
//    public final ObjectMap post(Object body, String uri, Map<String, ?> uriVariables) {
//        return sendBody(body, spec -> spec.uri(uri, uriVariables));
//    }
//
//    /**
//     * Execute send data with POST
//     *
//     * @param body the data body
//     * @param uri  the URI starting with a URI template and finishing off with a {@link UriBuilder} created from the template.
//     * @param uriFunction the function
//     */
//    public final ObjectMap post(Object body, String uri, Function<UriBuilder, URI> uriFunction) {
//        return sendBody(body, spec -> spec.uri(uri, uriFunction));
//    }
//
//    /**
//     * Execute send data with POST
//     *
//     * @param body the data body
//     * @param uriFunction the function
//     */
//    public final ObjectMap post(Object body, Function<UriBuilder, URI> uriFunction) {
//        return sendBody(body, spec -> spec.uri(uriFunction));
//    }
//
//    public ObjectMap sendBody(Object body, Consumer<RequestBodyUriSpec> consumer) {
//        return sendBody(body, consumer, 0);
//    }

    private ObjectMap sendBody(Object body, Consumer<RequestBodyUriSpec> consumer, int count) {
        Asserts.notNull(consumer, "Consumer<RequestBodyUriSpec>");
        RequestBodyUriSpec uriSpec = createClient().post();
        consumer.accept(uriSpec);
        return checkResponse(body, uriSpec
                .bodyValue(ClsRequest.fromObject(body))
                .retrieve().bodyToMono(ObjectMap.class)
                .blockOptional().orElseThrow());
    }


    protected ObjectMap checkResponse(Object requestBody, ObjectMap responseData) {
        ClsHelper.checkResponse(cfg, responseData);
        return responseData;
    }


}