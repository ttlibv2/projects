package vn.conyeu.ts.odcore.service;

import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriBuilder;
import vn.conyeu.commons.beans.ObjectMap;
import vn.conyeu.ts.odcore.domain.ClsApiCfg;

import java.net.URI;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;


public abstract class OdApp {
    protected final ClsApiCfg clsCfg;
    protected final OdClient client;

    protected OdApp(ClsApiCfg clsCfg, OdClient client) {
        this.clsCfg = clsCfg;
        this.client = client;
    }

    /**
     * Returns the clsCfg
     */
    public final ClsApiCfg getClsCfg() {
        return clsCfg;
    }

    public String getAppName() {
        return getAppUID();
    }

    public abstract String getAppUID();


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

    private ObjectMap sendBody(Object body, Consumer<WebClient.RequestBodyUriSpec> consumer) {
        return client.sendBody(body, consumer);
    }
}