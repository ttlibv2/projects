package vn.conyeu.ts.odcore.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.util.UriBuilder;
import vn.conyeu.common.exception.BaseException;
import vn.conyeu.commons.beans.ObjectMap;
import vn.conyeu.commons.utils.Asserts;
import vn.conyeu.commons.utils.Lists;
import vn.conyeu.commons.utils.Objects;
import vn.conyeu.restclient.ClientBuilder;
import vn.conyeu.restclient.RestClient;
import org.springframework.web.reactive.function.client.WebClient.RequestBodyUriSpec;
import vn.conyeu.ts.odcore.domain.*;

import java.net.URI;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

@Slf4j
public abstract class OdClient {
    protected final ClsApiCfg cfg;
    protected Supplier<ClsUser> tryLoginFnc;

    protected OdClient(ClsApiCfg apiConfig) {
        this.cfg = apiConfig;
    }

    /**
     * Returns the cfg
     */
    public ClsApiCfg cfg() {
        return cfg;
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
     * Create RestClient ClientBuilder
     */
    protected ClientBuilder clientBuilder() {
        return RestClient.builder().baseUrl(getApiUrl())
                .defaultContentType(MediaType.APPLICATION_JSON)
                .defaultHeader("tsModelName", getLogModel())
                .defaultHeader("tsApiUserId", cfg.getUserId());
    }

    /**
     * Set the tryLoginFnc
     *
     * @param tryLoginFnc the value
     */
    public void setTryLoginFnc(Supplier<ClsUser> tryLoginFnc) {
        this.tryLoginFnc = tryLoginFnc;
    }

    /**
     * Returns dataset uri
     *
     * @param path the path uri
     */
    protected String datasetUri2(String path) {
        if (path.startsWith("/")) path = path.substring(1);
        return "/web/dataset/" + path;
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
        Long userId = cfg.getUserId();
        Object compId = cfg.getClsUser().getCompany_Uid();
        ClsUserContext ctx = cfg.getUserContext();
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

    protected RestClient createClient() {
        ClientBuilder clientBuilder = clientBuilder();
        clientBuilder.defaultQueries(cfg.getQueries());
        clientBuilder.defaultHeaders(cfg.getHeaders());
        clientBuilder.defaultHeader("cookie", cfg.getCookieValue());
        clientBuilder.defaultCsrfToken(cfg.getCsrfToken());
        //clientBuilder.filters(filters -> {
        //    filters.add(ExchangeFilterFunction.ofRequestProcessor());
        //    filters.add(ExchangeFilterFunction.ofResponseProcessor());
        //});

        if(cfg.getCustomBuilderConsumer() != null) {
            cfg.getCustomBuilderConsumer().accept(clientBuilder);
        }

        return clientBuilder.build();
    }

    /**
     * Execute send data with POST
     *
     * @param body the data body
     * @param uri  the uri send
     */
    public final ObjectMap sendPost(Object body, URI uri) {
        return sendBody(body, spec -> spec.uri(uri));
    }

    /**
     * Execute send data with POST
     *
     * @param body the data body
     * @param uri  the URI for the request using a URI template and URI variables
     * @param uriVariables the variables
     */
    public final ObjectMap sendPost(Object body, String uri, Object...uriVariables) {
        return sendBody(body, spec -> spec.uri(uri, uriVariables));
    }

    /**
     * Execute send data with POST
     *
     * @param body the data body
     * @param uri  the URI for the request using a URI template and URI variables
     * @param uriVariables the variables
     */
    public final ObjectMap sendPost(Object body, String uri, Map<String, ?> uriVariables) {
        return sendBody(body, spec -> spec.uri(uri, uriVariables));
    }

    /**
     * Execute send data with POST
     *
     * @param body the data body
     * @param uri  the URI starting with a URI template and finishing off with a {@link UriBuilder} created from the template.
     * @param uriFunction the function
     */
    public final ObjectMap sendPost(Object body, String uri, Function<UriBuilder, URI> uriFunction) {
        return sendBody(body, spec -> spec.uri(uri, uriFunction));
    }

    /**
     * Execute send data with POST
     *
     * @param body the data body
     * @param uriFunction the function
     */
    public final ObjectMap sendPost(Object body, Function<UriBuilder, URI> uriFunction) {
        return sendBody(body, spec -> spec.uri(uriFunction));
    }

    protected ObjectMap sendBody(Object body, Consumer<RequestBodyUriSpec> consumer) {
        return sendBody(body, consumer, 0);
    }

    private ObjectMap sendBody(Object body, Consumer<RequestBodyUriSpec> consumer, int count) {
        Asserts.notNull(consumer, "Consumer<RequestBodyUriSpec>");

        RequestBodyUriSpec uriSpec = createClient().post();
        consumer.accept(uriSpec);

        ObjectMap response = uriSpec
                .bodyValue(ClsRequest.fromObject(body))
                .retrieve().bodyToMono(ObjectMap.class)
                .blockOptional().orElseThrow();

        try {
          //  log.info(response.toJson(false));
            return checkResponse(body, response);
        }//
        catch (BaseException exp) {
            if(exp.getObject().getCode().equals("SessionExpired") && cfg.isAutoLogin() && tryLoginFnc != null && count == 0) {
                tryLoginFnc.get();
                return sendBody(body, consumer, 1);
            }
            else throw exp;
        }

    }

    protected ObjectMap checkResponse(Object requestBody, ObjectMap responseData) {
        ClsHelper.checkResponse(cfg, responseData);
        return responseData;
    }


}