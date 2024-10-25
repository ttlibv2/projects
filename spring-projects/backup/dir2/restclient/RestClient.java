package vn.conyeu.restclient;

import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriBuilder;

import java.net.URI;
import java.util.Map;
import java.util.function.Function;

public class RestClient implements org.springframework.web.client.RestClient {
    private final org.springframework.web.client.RestClient delegate;
    private final ClientBuilder builder;

    RestClient(org.springframework.web.client.RestClient delegate, ClientBuilder builder) {
        this.delegate = delegate;
        this.builder = builder;
    }

    @Override
    public RequestHeadersUriSpec<?> get() {
        return methodInternal(HttpMethod.GET);
    }

    /**
     * Retrieve a representation by doing a GET on the specified URL.
     * The response (if any) is converted and returned.
     * <p>URI Template variables are expanded using the given URI variables, if any.
     * @param url the URL
     * @param responseType the type of the return value
     * @param uriVariables the variables to expand the template
     * @return the converted object
     */
    public <T> T getForObject(String url, Class<T> responseType, Object...uriVariables) {
        return get().uri(url, uriVariables).retrieve().body(responseType);
    }

    /**
     * Retrieve a representation by doing a GET on the URI template.
     * The response (if any) is converted and returned.
     * <p>URI Template variables are expanded using the given map.
     * @param url the URL
     * @param responseType the type of the return value
     * @param uriVariables the map containing variables for the URI template
     * @return the converted object
     */
    public <T> T getForObject(String url, Class<T> responseType, Map<String, ?> uriVariables) {
        return get().uri(url, uriVariables).retrieve().body(responseType);
    }

    /**
     * Retrieve a representation by doing a GET on the URI template.
     * The response (if any) is converted and returned.
     * <p>Specify the URI starting with a URI template and finishing off with a {@link UriBuilder} created from the template.
     * @param url the URL
     * @param responseType the type of the return value
     * @param uriFunction the uri function
     * @return the converted object
     */
    public <T> T getForObject(String url, Class<T> responseType, Function<UriBuilder, URI> uriFunction) {
        return get().uri(url, uriFunction).retrieve().body(responseType);
    }

    /**
     * Retrieve a representation by doing a GET on the URI template.
     * The response (if any) is converted and returned.
     * <p>Specify the URI starting with a URI template and finishing off with a {@link UriBuilder} created from the template.
     * @param uriFunction the uri function
     * @param responseType the type of the return value
     * @return the converted object
     */
    public <T> T getForObject(Function<UriBuilder, URI> uriFunction, Class<T> responseType) {
        return get().uri(uriFunction).retrieve().body(responseType);
    }

    /**
     * Retrieve a representation by doing a GET on the URL.
     * The response (if any) is converted and returned.
     * @param url the URL
     * @param responseType the type of the return value
     * @return the converted object
     */
    public <T> T getForObject(URI url, Class<T> responseType) {
        return get().uri(url).retrieve().body(responseType);
    }

    public <T> ResponseEntity<T> getForEntity(String url, Class<T> responseType, Object...uriVariables) {
        return get().uri(url, uriVariables).retrieve().toEntity(responseType);
    }

    public <T> ResponseEntity<T> getForEntity(String url, Class<T> responseType, Map<String, ?> uriVariables) {
        return get().uri(url, uriVariables).retrieve().toEntity(responseType);
    }

    public <T> ResponseEntity<T> getForEntity(String url, Class<T> responseType, Function<UriBuilder, URI> uriFunction) {
        return get().uri(url, uriFunction).retrieve().toEntity(responseType);
    }

    public <T> ResponseEntity<T> getForEntity(Function<UriBuilder, URI> uriFunction, Class<T> responseType) {
        return get().uri(uriFunction).retrieve().toEntity(responseType);
    }

    public <T> ResponseEntity<T> getForEntity(URI url, Class<T> responseType) {
        return get().uri(url).retrieve().toEntity(responseType);
    }

    @Override
    public RequestHeadersUriSpec<?> head() {
        return methodInternal(HttpMethod.HEAD);
    }

    @Override
    public RequestBodyUriSpec post() {
        return methodInternal(HttpMethod.POST);
    }

    @Override
    public RequestBodyUriSpec put() {
        return methodInternal(HttpMethod.PUT);
    }

    @Override
    public RequestBodyUriSpec patch() {
        return methodInternal(HttpMethod.PATCH);
    }

    @Override
    public RequestHeadersUriSpec<?> delete() {
        return methodInternal(HttpMethod.DELETE);
    }

    @Override
    public RequestHeadersUriSpec<?> options() {
        return methodInternal(HttpMethod.OPTIONS);
    }

    @Override
    public RequestBodyUriSpec method(HttpMethod method) {
        Assert.notNull(method, "HttpMethod must not be null");
        return methodInternal(method);
    }

    @Override
    public ClientBuilder mutate() {
        return new ClientBuilder(builder);
    }

    private RequestBodyUriSpec methodInternal(HttpMethod httpMethod) {
        return delegate.method(httpMethod);
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