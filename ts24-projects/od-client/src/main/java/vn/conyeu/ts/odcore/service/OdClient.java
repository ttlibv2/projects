package vn.conyeu.ts.odcore.service;

import org.springframework.web.util.UriBuilder;
import vn.conyeu.commons.beans.ObjectMap;
import vn.conyeu.commons.utils.Asserts;
import vn.conyeu.commons.utils.Lists;
import vn.conyeu.commons.utils.Objects;
import vn.conyeu.restclient.ClientBuilder;
import vn.conyeu.restclient.RestClient;
import org.springframework.web.reactive.function.client.WebClient.RequestBodyUriSpec;
import vn.conyeu.ts.odcore.domain.ClsApiConfig;
import vn.conyeu.ts.odcore.domain.ClsHelper;
import vn.conyeu.ts.odcore.domain.ClsRequest;
import vn.conyeu.ts.odcore.domain.ClsUserContext;

import java.net.URI;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;

public abstract class OdClient {
    protected final ClsApiConfig apiConfig;
    protected OdClient(ClsApiConfig apiConfig) {
        this.apiConfig = apiConfig;
    }

    /**
     * Returns the model api
     */
    public abstract String getModel();

//    /**
//     * Returns base path
//     */
//    public abstract String getBasePath();

    /**
     * Returns the api code def
     */
    public abstract String getApiCode();

    /**
     * Create RestClient ClientBuilder
     */
    protected ClientBuilder clientBuilder() {
        return RestClient.builder().baseUrl(getApiUrl());
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
        Long userId = apiConfig.getUserId();
        ClsUserContext ctx = apiConfig.getUserContext();
        return ClsUserContext.fix(userId, ctx);
    }

    /**
     * Returns the api url
     */
    protected final String getApiUrl() {
        return apiConfig.getBaseUrl();
    }



    protected ClientBuilder applyDefaultBuilder() {
        ClientBuilder clientBuilder = clientBuilder();
        clientBuilder.defaultQueries(apiConfig.getQueries());
        clientBuilder.defaultHeaders(apiConfig.getHeaders());
        clientBuilder.defaultCookie(apiConfig.getCookieValue());
        clientBuilder.defaultCsrfToken(apiConfig.getCsrfToken());
        return clientBuilder;
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
     * @deprecated
     */
    public final ObjectMap sendPost(String uri, Object body) {
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
        Asserts.notNull(consumer, "Consumer<RequestBodyUriSpec>");

        ClientBuilder clientBuilder = applyDefaultBuilder();
        RequestBodyUriSpec uriSpec = clientBuilder.build().post();
        consumer.accept(uriSpec);

        ObjectMap response = uriSpec
                .bodyValue(ClsRequest.fromObject(body))
                .retrieve().bodyToMono(ObjectMap.class)
                // .flatMap(object -> checkResponse(uri, body, object))
                //.doOnSuccess(res -> checkResponse(uri, body, res))
                .blockOptional().orElseThrow();

        return checkResponse(body, response);

    }

    protected ObjectMap checkResponse(Object requestBody, ObjectMap responseData) {
       // try{
            ClsHelper.checkResponse(responseData);
            return responseData;
       // }
       // catch (BaseException exp) {
       //     String code = exp.getObject().getCode();
       //     if("SessionExpired".equals(code)) {
       //        ClsUser clsUser = baseService.tryLoginUser();
       //        if(clsUser != null)return sendPost(uri, requestBody);
       //     }
      //      throw exp;
      //  }
    }

}