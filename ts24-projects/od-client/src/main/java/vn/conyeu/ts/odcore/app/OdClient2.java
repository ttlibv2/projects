package vn.conyeu.ts.odcore.app;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient.RequestBodySpec;
import org.springframework.web.reactive.function.client.WebClient.RequestBodyUriSpec;
import org.springframework.web.util.UriBuilder;
import vn.conyeu.common.exception.BaseException;
import vn.conyeu.common.exception.Unauthorized;
import vn.conyeu.commons.beans.ObjectMap;
import vn.conyeu.restclient.ClientBuilder;
import vn.conyeu.restclient.RClient;
import vn.conyeu.ts.odcore.domain.ClsApiCfg;
import vn.conyeu.ts.odcore.domain.ClsHelper;
import vn.conyeu.ts.odcore.domain.ClsRequest;

import java.net.URI;
import java.util.Map;
import java.util.function.Function;

@Slf4j
public abstract class OdClient2 {
    protected final ClsApiCfg cfg;

    public OdClient2(ClsApiCfg apiConfig) {
        this.cfg = apiConfig;
    }

    public BaseException notLogin() {
        return new Unauthorized("ts_api").detail("ts_api", cfg.getAppUID())
                .detail("site_name", cfg.getAppUID()).detail("service_name", cfg.getAppName())
                .message("Bạn chưa cấu hình tài khoản kết nối hệ thống '%s'", cfg.getTitle());
    }

    /**
     * Create RClient ClientBuilder
     */
    public ClientBuilder clientBuilder() {
        return RClient.builder().baseUrl(cfg.getBaseUrl())
                .defaultContentType(MediaType.APPLICATION_JSON)
                .defaultQueries(cfg.getQueries())
                .defaultHeaders(cfg.getHeaders())
                .defaultHeader("cookie", cfg.getCookie())
                .defaultCsrfToken(cfg.getCsrfToken())
                .defaultHeader("ts-service-name", cfg.getAppName())
                .defaultHeader("ts-account-id", cfg.getAccountId());
    }

    /**
     * Send post without the URI using an absolute
     */
    public ObjectMap post(Object bodyValue, URI uri) {
        return sendBodyValue(bodyValue, spec -> spec.uri(uri));
    }

    /**
     * Send post without the URI for the request using a URI template and URI variables.
     */
    public ObjectMap post(Object bodyValue, String uri, Object... uriVariables) {
        return sendBodyValue(bodyValue, spec -> spec.uri(uri, uriVariables));
    }

    /**
     * Send post without the URI for the request using a URI template and URI variables.
     */
    public ObjectMap post(Object bodyValue, String uri, Map<String, ?> uriVariables) {
        return sendBodyValue(bodyValue, spec -> spec.uri(uri, uriVariables));
    }

    /**
     * Send post without the URI starting with a URI template and finishing off with a
     * {@link UriBuilder} created from the template.
     */
    public ObjectMap post(Object bodyValue, String uri, Function<UriBuilder, URI> uriFunction) {
        return sendBodyValue(bodyValue, spec -> spec.uri(uri, uriFunction));
    }

    /**
     * Send post without the URI by through a {@link UriBuilder}.
     */
    public ObjectMap post(Object bodyValue, Function<UriBuilder, URI> uriFunction) {
        return sendBodyValue(bodyValue, spec -> spec.uri(uriFunction));
    }

    private ObjectMap sendBodyValue(Object body, Function<RequestBodyUriSpec, RequestBodySpec> consumer) {
        RequestBodyUriSpec uriSpec = clientBuilder().build().post();
        ObjectMap responseMap = consumer.apply(uriSpec)
                .bodyValue(ClsRequest.fromObject(body))
                .retrieve().bodyToMono(ObjectMap.class)
                .blockOptional().orElseThrow();

        ClsHelper.checkResponse(cfg, responseMap);
        return responseMap;
    }




}