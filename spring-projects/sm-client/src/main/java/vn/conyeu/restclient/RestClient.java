package vn.conyeu.restclient;

import org.springframework.http.HttpMethod;
import org.springframework.web.reactive.function.BodyInserter;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriBuilder;
import vn.conyeu.commons.beans.ObjectMap;

import java.net.URI;
import java.util.Map;
import java.util.function.Function;

public class RestClient implements WebClient {
    private final WebClient delegate;
    private final ClientBuilder builder;

    public RestClient(WebClient delegate, ClientBuilder builder) {
        this.delegate = delegate;
        this.builder = builder;
    }

    /**{@inheritDoc}*/
    public RequestHeadersUriSpec<?> get() {
        return methodInternal(HttpMethod.GET);
    }

    /**{@inheritDoc}*/
    public RequestHeadersUriSpec<?> head() {
        return methodInternal(HttpMethod.HEAD);
    }

    /**{@inheritDoc}*/
    public RequestBodyUriSpec post() {
        return methodInternal(HttpMethod.POST);
    }

    /**
     * Send post without the URI using an absolute
     */
    public RequestHeadersSpec post(Object bodyValue, URI uri) {
        return postImpl(bodyValue, spec -> spec.uri(uri));
    }

    /**
     * Send post without the URI for the request using a URI template and URI variables.
     */
    public RequestHeadersSpec post(Object bodyValue, String uri, Object... uriVariables) {
        return postImpl(bodyValue, spec -> spec.uri(uri, uriVariables));
    }

    /**
     * Send post without the URI for the request using a URI template and URI variables.
     */
    public RequestHeadersSpec post(Object bodyValue, String uri, Map<String, ?> uriVariables) {
        return postImpl(bodyValue, spec -> spec.uri(uri, uriVariables));
    }

    /**
     * Send post without the URI starting with a URI template and finishing off with a
     * {@link UriBuilder} created from the template.
     */
    public RequestHeadersSpec post(Object bodyValue, String uri, Function<UriBuilder, URI> uriFunction) {
        return postImpl(bodyValue, spec -> spec.uri(uri, uriFunction));
    }

    /**
     * Send post without the URI by through a {@link UriBuilder}.
     */
    public RequestHeadersSpec post(Object bodyValue, Function<UriBuilder, URI> uri) {
        return postImpl(bodyValue, spec -> spec.uri(uri));
    }

    private RequestHeadersSpec postImpl(Object body, Function<RequestBodyUriSpec, RequestBodySpec> function) {
        RequestBodySpec uriSpec = function.apply(post());
        if(body instanceof BodyInserter bi) return uriSpec.body(bi);
        else return uriSpec.bodyValue(body);
    }

    /**{@inheritDoc}*/
    public RequestBodyUriSpec put() {
        return methodInternal(HttpMethod.PUT);
    }

    /**{@inheritDoc}*/
    public RequestBodyUriSpec patch() {
        return methodInternal(HttpMethod.PATCH);
    }

    /**{@inheritDoc}*/
    public RequestHeadersUriSpec<?> delete() {
        return methodInternal(HttpMethod.DELETE);
    }

    /**{@inheritDoc}*/
    public RequestHeadersUriSpec<?> options() {
        return methodInternal(HttpMethod.OPTIONS);
    }

    /**{@inheritDoc}*/
    public RequestBodyUriSpec method(HttpMethod method) {
        return methodInternal(method);
    }

    private RequestBodyUriSpec methodInternal(HttpMethod httpMethod) {
        return delegate.method(httpMethod);
    }

    /**{@inheritDoc}*/
    public ClientBuilder mutate() {
        return new ClientBuilder(builder);
    }

    /**
     * Create a new {@code RestClient} with Reactor Netty by default.
     * @see #create(String)
     * @see #builder()
     */
    public static RestClient create() {
        return new ClientBuilder().build();
    }

    /**
     * Variant of {@link #create()} that accepts a default base URL. For more
     * details see {@link ClientBuilder#baseUrl(String) ClientBuilder.baseUrl(String)}.
     * @param baseUrl the base URI for all requests
     * @see #builder()
     */
    public static RestClient create(String baseUrl) {
        return new ClientBuilder().baseUrl(baseUrl).build();
    }

    /**
     * Obtain a {@code RestClient} builder.
     */
    public static ClientBuilder builder() {
        return new ClientBuilder();
    }
}