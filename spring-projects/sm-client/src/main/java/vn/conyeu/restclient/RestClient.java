package vn.conyeu.restclient;

import org.springframework.http.HttpMethod;
import org.springframework.web.reactive.function.client.WebClient;

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