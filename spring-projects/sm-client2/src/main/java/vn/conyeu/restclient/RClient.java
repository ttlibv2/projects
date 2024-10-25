package vn.conyeu.restclient;

import io.micrometer.observation.ObservationRegistry;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.http.client.*;
import org.springframework.http.client.observation.ClientRequestObservationConvention;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.*;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClient.ResponseSpec;
import org.springframework.web.client.RestClient.RequestHeadersSpec.ExchangeFunction;
import org.springframework.web.client.RestClient.ResponseSpec.ErrorHandler;
import org.springframework.web.util.UriBuilder;
import org.springframework.web.util.UriBuilderFactory;

import java.io.OutputStream;
import java.net.URI;
import java.nio.charset.Charset;
import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

public interface RClient {

    /**
     * Start building an HTTP GET request.
     * @return a spec for specifying the target URL
     */
    HeadersUriSpec<?> get();

    /**
     * Start building an HTTP GET request.
     * @return a spec for specifying the target URL
     */
    default HeadersSpec get(String uri) {
        return get().uri(uri);
    }

    /**
     * Start building an HTTP GET request.
     * @return a spec for specifying the target URL
     */
    default HeadersSpec get(String uri, Object...uriVariables) {
        return get().uri(uri, uriVariables);
    }

    /**
     * Start building an HTTP GET request.
     * @return a spec for specifying the target URL
     */
    default HeadersSpec get(String uri, Map<String, ?> uriVariables) {
        return get().uri(uri, uriVariables);
    }

    /**
     * Start building an HTTP GET request.
     * @return a spec for specifying the target URL
     */
    default HeadersSpec get(String uri, Function<UriBuilder, URI> uriFunction) {
        return get().uri(uri, uriFunction);
    }

    /**
     * Start building an HTTP GET request.
     * @return a spec for specifying the target URL
     */
    default HeadersSpec get(Function<UriBuilder, URI> uriFunction) {
        return get().uri(uriFunction);
    }

    /**
     * Start building an HTTP HEAD request.
     * @return a spec for specifying the target URL
     */
    HeadersUriSpec<?> head();

    /**
     * Start building an HTTP POST request.
     * @return a spec for specifying the target URL
     */
    BodyUriSpec post();

    /**
     * Start building an HTTP POST request.
     * @return a spec for specifying the target URL
     */
    default BodySpec post(Object payload, String uri) {
        return post().uri(uri).body(payload);
    }

    /**
     * Start building an HTTP POST request.
     * @return a spec for specifying the target URL
     */
    default BodySpec post(Object payload, String uri, Map<String, ?> uriVariables) {
        return post().uri(uri, uriVariables).body(payload);
    }

    /**
     * Start building an HTTP POST request.
     * @return a spec for specifying the target URL
     */
    default BodySpec post(Object payload, String uri, Object... uriVariables) {
        return post().uri(uri, uriVariables).body(payload);
    }

    /**
     * Start building an HTTP POST request.
     * @return a spec for specifying the target URL
     */
    default BodySpec post(Object payload, String uri, Function<UriBuilder, URI> uriFunction) {
        return post().uri(uri, uriFunction).body(payload);
    }

    /**
     * Start building an HTTP POST request.
     * @return a spec for specifying the target URL
     */
    default BodySpec post(Object payload, Function<UriBuilder, URI> uriFunction) {
        return post().uri(uriFunction).body(payload);
    }

    /**
     * Start building an HTTP PUT request.
     * @return a spec for specifying the target URL
     */
    BodyUriSpec put();

    /**
     * Start building an HTTP PUT request.
     * @return a spec for specifying the target URL
     */
    default BodySpec put(Object payload, String uri) {
        return put().uri(uri).body(payload);
    }

    /**
     * Start building an HTTP PUT request.
     * @return a spec for specifying the target URL
     */
    default BodySpec put(Object payload, String uri, Map<String, ?> uriVariables) {
        return put().uri(uri, uriVariables).body(payload);
    }

    /**
     * Start building an HTTP PUT request.
     * @return a spec for specifying the target URL
     */
    default BodySpec put(Object payload, String uri, Object... uriVariables) {
        return put().uri(uri, uriVariables).body(payload);
    }

    /**
     * Start building an HTTP PUT request.
     * @return a spec for specifying the target URL
     */
    default BodySpec put(Object payload, String uri, Function<UriBuilder, URI> uriFunction) {
        return put().uri(uri, uriFunction).body(payload);
    }

    /**
     * Start building an HTTP PUT request.
     * @return a spec for specifying the target URL
     */
    default BodySpec put(Object payload, Function<UriBuilder, URI> uriFunction) {
        return put().uri(uriFunction).body(payload);
    }

    /**
     * Start building an HTTP PATCH request.
     * @return a spec for specifying the target URL
     */
    BodyUriSpec patch();

    /**
     * Start building an HTTP DELETE request.
     * @return a spec for specifying the target URL
     */
    HeadersUriSpec<?> delete();

    /**
     * Start building an HTTP OPTIONS request.
     * @return a spec for specifying the target URL
     */
    HeadersUriSpec<?> options();

    /**
     * Start building a request for the given {@code HttpMethod}.
     * @return a spec for specifying the target URL
     */
    BodyUriSpec method(HttpMethod method);

    /**
     * Return a builder to create a new {@code RestClient} whose settings are
     * replicated from this {@code RestClient}.
     */
    Builder mutate();

    /**
     * Create a new {@code RestClient}.
     * @see #create(String)
     * @see #builder()
     */
    static RClient create() {
        return new DefaultRClientBuilder().build();
    }

    /**
     * @param baseUrl the base URI for all requests
     * @see RestClient#create(String)
     * @see #builder()
     */
    static RClient create(String baseUrl) {
        return new DefaultRClientBuilder().baseUrl(baseUrl).build();
    }

    /**
     * Obtain a {@code RClient} builder.
     */
    static RClient.Builder builder() {
        return new DefaultRClientBuilder();
    }

    /**
     * A mutable builder for creating a {@link RClient}.
     */
    interface Builder {

        /**
         * Configure a base URL for requests.
         * @see RestClient.Builder#baseUrl(String)
         */
        Builder baseUrl(String baseUrl);

        /**
         * Configure a base URL for requests.
         * @see RestClient.Builder#baseUrl(String)
         */
        Builder baseUrl(Function<UriBuilder, URI> uriFunction);

        /**
         * Configure default URL variable values to use when expanding URI templates with a {@link Map}.
         * @see RestClient.Builder#defaultUriVariables(Map)
         */
        Builder defaultUriVariables(Map<String, ?> defaultUriVariables);

        /**
         * Provide a pre-configured {@link UriBuilderFactory} instance.
         * @param uriBuilderFactory the URI builder factory to use
         * @see RestClient.Builder#uriBuilderFactory(UriBuilderFactory) 
         */
        Builder uriBuilderFactory(UriBuilderFactory uriBuilderFactory);

        /**
         * Global option to specify a header to be added to every request,
         * if the request does not already contain such a header.
         * @param header the header name
         * @param values the header values
         * @see RestClient.Builder#defaultHeader(String, String...)
         */
        Builder defaultHeader(String header, String... values);

        /**
         * Provide a consumer to access to every {@linkplain #defaultHeader(String, String...)
         * default header} declared so far, with the possibility to add, replace, or remove.
         * @param headersConsumer the consumer
         * @see RestClient.Builder#defaultHeaders(Consumer)
         */
        Builder defaultHeaders(Consumer<HttpHeaders> headersConsumer);

        /**
         * Global option to specify a cookie to be added to every request,
         * if the request does not already contain such a cookie.
         * @param cookie the cookie name
         * @param values the cookie values
         */
        Builder defaultCookie(String cookie, String... values);

        /**
         * Global option to specify a cookie to be added to every request,
         * if the request does not already contain such a cookie.
         * @param cookie the cookie name
         * @param values the cookie values
         */
        Builder defaultCookie(String cookie, List<String> values);

        /**
         * Provides access to every {@link #defaultCookie(String, String...)}
         * declared so far with the possibility to add, replace, or remove.
         * @param cookiesConsumer a function that consumes the cookies map
         */
        Builder defaultCookies(Consumer<MultiValueMap<String, String>> cookiesConsumer);

        /**
         * Custom user-agent header
         *
         * @param agent the user agent
         */
        Builder userAgent(String agent);

        /**
         * Custom header `Authorization`
         *
         * @param username the username
         * @param password the password
         */
        Builder basicAuth(String username, String password);

        /**
         * Custom header `Authorization`
         *
         * @param username the username
         * @param password the password
         * @param charset  the charset to use to convert the credentials into an octet sequence. Defaults to ISO-8859-1.
         */
        Builder basicAuth(String username, String password, Charset charset);

        /**
         * Custom header `Authorization`
         *
         * @param encodedCredentials String
         */
        Builder basicAuth(String encodedCredentials);

        /**
         * Configure default bearer token
         *
         * @param bearerToken String
         */
        Builder bearerToken(String bearerToken);

        Builder defaultContentType(MediaType mediaType);


        /**
         * Provide a consumer to customize every request being built.
         * @param defaultRequest the consumer to use for modifying requests
         * @return this builder
         */
        Builder defaultRequest(Consumer<HeadersSpec<?>> defaultRequest);

        /**
         * Register a default {@linkplain ResponseSpec#onStatus(Predicate, ErrorHandler) status handler}
         * to apply to every response.
         * @param statusPredicate to match responses with
         * @param errorHandler handler that typically, though not necessarily, throws an exception
         * @see RestClient.Builder#defaultStatusHandler(ResponseErrorHandler)
         */
        Builder defaultStatusHandler(Predicate<HttpStatusCode> statusPredicate, ErrorHandler errorHandler);

        /**
         * Register a default {@linkplain ResponseSpec#onStatus(ResponseErrorHandler) status handler} to apply to every response.
         * @param errorHandler handler that typically, though not necessarily, throws an exception
         * @see RestClient.Builder#defaultStatusHandler(ResponseErrorHandler)
         */
        Builder defaultStatusHandler(ResponseErrorHandler errorHandler);

        /**
         * Add the given request interceptor to the end of the interceptor chain.
         * @param interceptor the interceptor to be added to the chain
         * @return this builder
         */
        Builder requestInterceptor(ClientHttpRequestInterceptor interceptor);

        /**
         * Manipulate the interceptors with the given consumer.
         * @param interceptorsConsumer a function that consumes the interceptors list
         * @see RestClient.Builder#requestInterceptors(Consumer)
         */
        Builder requestInterceptors(Consumer<List<ClientHttpRequestInterceptor>> interceptorsConsumer);

        /**
         * Add the given request initializer to the end of the initializer chain.
         * @param initializer the initializer to be added to the chain
         * @see RestClient.Builder#requestInitializer(ClientHttpRequestInitializer)
         */
        Builder requestInitializer(ClientHttpRequestInitializer initializer);

        /**
         * Manipulate the initializers with the given consumer.
         * @param initializersConsumer a function that consumes the initializers list
         * @see RestClient.Builder#requestInitializers(Consumer)
         */
        Builder requestInitializers(Consumer<List<ClientHttpRequestInitializer>> initializersConsumer);

        /**
         * Configure the {@link ClientHttpRequestFactory} to use.
         * @param requestFactory the request factory to use
         * @see RestClient.Builder#requestFactory(ClientHttpRequestFactory)
         */
        Builder requestFactory(ClientHttpRequestFactory requestFactory);
        Builder requestFactory(DelegateRequestFactory requestFactory);

        Builder disableCookie(boolean disabled);

        Builder maxRedirects(int maxRedirects);

        Builder followRedirects(boolean follow);

        /**
         * Set the underlying response timeout in milliseconds.
         * A value of 0 specifies an infinite timeout.
         * <p>Default is 5 seconds.
         */
        Builder responseTimeout(int timeout);

        /**
         * Set the underlying response timeout in milliseconds.
         * A value of 0 specifies an infinite timeout.
         * <p>Default is 5 seconds.
         */
        Builder responseTimeout(Duration timeout);

        /**
         * Set the underlying connect timeout in milliseconds.
         * A value of 0 specifies an infinite timeout.
         * <p>Default is 5 seconds.
         */
        Builder connectTimeout(int timeout);

        /**
         * Set the underlying connect timeout in milliseconds.
         * A value of 0 specifies an infinite timeout.
         * <p>Default is 5 seconds.
         */
        Builder connectTimeout(Duration timeout);

        /**
         * Set the underlying read timeout in milliseconds
         * <p>Default is 10 seconds.
         */
        Builder readTimeout(int readTimeout);

        /**
         * Set the underlying read timeout as {@code Duration}.
         * <p>Default is 10 seconds.
         */
        Builder readTimeout(Duration readTimeout);

        /**
         * Configure the message converters for the {@code RClient} to use.
         * @param configurer the configurer to apply
         * @see RestClient.Builder#messageConverters(Consumer)
         */
        Builder messageConverters(Consumer<List<HttpMessageConverter<?>>> configurer);

        /**
         * Configure the {@link io.micrometer.observation.ObservationRegistry} to use
         * for recording HTTP client observations.
         * @param observationRegistry the observation registry to use
         * @see RestClient.Builder#observationRegistry(ObservationRegistry)
         */
        Builder observationRegistry(ObservationRegistry observationRegistry);

        /**
         * Configure the {@link io.micrometer.observation.ObservationConvention} to use
         * for collecting metadata for the request observation.
         * @param observationConvention the observation convention to use
         * @see RestClient.Builder#observationConvention(ClientRequestObservationConvention)
         */
        Builder observationConvention(ClientRequestObservationConvention observationConvention);

        /**
         * Apply the given {@code Consumer} to this builder instance.
         * @param builderConsumer the consumer to apply
         * @see RestClient.Builder#apply(Consumer)
         */
        Builder apply(Consumer<Builder> builderConsumer);

        /**
         * Clone this {@code RClient.Builder}.
         */
        Builder clone();

        /**
         * Build the {@link RClient} instance.
         */
        RClient build();
    }

    /**
     * Contract for specifying the URI for a request.
     * @param <S> a self reference to the spec type
     */
    interface UriSpec<S extends HeadersSpec<?>> {

        /**
         * Specify the URI using an absolute, fully constructed {@link URI}.
         */
        S uri(URI uri);

        /**
         * Specify the URI for the request using a URI template and URI variables.
         * <p>If a {@link UriBuilderFactory} was configured for the client (e.g.
         * with a base URI) it will be used to expand the URI template.
         */
        S uri(String uri, Object... uriVariables);

        /**
         * Specify the URI for the request using a URI template and URI variables.
         * <p>If a {@link UriBuilderFactory} was configured for the client (e.g.
         * with a base URI) it will be used to expand the URI template.
         */
        S uri(String uri, Map<String, ?> uriVariables);

        /**
         * Specify the URI starting with a URI template and finishing off with a
         * {@link UriBuilder} created from the template.
         */
        S uri(String uri, Function<UriBuilder, URI> uriFunction);

        /**
         * Specify the URI by through a {@link UriBuilder}.
         * @see #uri(String, Function)
         */
        S uri(Function<UriBuilder, URI> uriFunction);
    }

    /**
     * Contract for specifying request headers leading up to the exchange.
     * @param <S> a self reference to the spec type
     */
    interface HeadersSpec<S extends HeadersSpec<S>> {

        /**
         * Set the list of acceptable {@linkplain MediaType media types}, as
         * specified by the {@code Accept} header.
         * @param acceptableMediaTypes the acceptable media types
         * @return this builder
         */
        S accept(MediaType... acceptableMediaTypes);

        /**
         * Set the list of acceptable {@linkplain Charset charsets}, as specified
         * by the {@code Accept-Charset} header.
         * @param acceptableCharsets the acceptable charsets
         * @return this builder
         */
        S acceptCharset(Charset... acceptableCharsets);

        /**
         * Set the value of the {@code If-Modified-Since} header.
         * @param ifModifiedSince the new value of the header
         * @return this builder
         */
        S ifModifiedSince(ZonedDateTime ifModifiedSince);

        /**
         * Set the values of the {@code If-None-Match} header.
         * @param ifNoneMatches the new value of the header
         * @return this builder
         */
        S ifNoneMatch(String... ifNoneMatches);

        /**
         * Add a cookie with the given name and value.
         * @param name the cookie name
         * @param values the cookie value
         * @return this builder
         */
        S cookie(String name, String... values);

        /**
         * Provides access to every cookie declared so far with the possibility
         * to add, replace, or remove values.
         * @param cookiesConsumer the consumer to provide access to
         * @return this builder
         */
        S cookies(Consumer<MultiValueMap<String, String>> cookiesConsumer);

        /**
         * Add the given, single header value under the given name.
         * @param headerName the header name
         * @param headerValues the header value(s)
         * @return this builder
         */
        S header(String headerName, String... headerValues);

        /**
         * Provides access to every header declared so far with the possibility
         * to add, replace, or remove values.
         * @param headersConsumer the consumer to provide access to
         * @return this builder
         */
        S headers(Consumer<HttpHeaders> headersConsumer);

        /**
         * Callback for access to the {@link ClientHttpRequest} that in turn
         * provides access to the native request of the underlying HTTP library.
         * <p>This could be useful for setting advanced, per-request options that
         * are exposed by the underlying library.
         * @param requestConsumer a consumer to access the
         * {@code ClientHttpRequest} with
         * @return this builder
         */
        S httpRequest(Consumer<ClientHttpRequest> requestConsumer);

        /**
         * Proceed to declare how to extract the response. For example to extract
         * a {@link ResponseEntity} with status, headers, and body:
         * <pre class="code">
         * ResponseEntity&lt;Person&gt; entity = client.get()
         *     .uri("/persons/1")
         *     .accept(MediaType.APPLICATION_JSON)
         *     .retrieve()
         *     .toEntity(Person.class);
         * </pre>
         * <p>Or if interested only in the body:
         * <pre class="code">
         * Person person = client.get()
         *     .uri("/persons/1")
         *     .accept(MediaType.APPLICATION_JSON)
         *     .retrieve()
         *     .body(Person.class);
         * </pre>
         * <p>By default, 4xx response code result in a
         * {@link HttpClientErrorException} and 5xx response codes in a
         * {@link HttpServerErrorException}. To customize error handling, use
         * {@link ResponseSpec#onStatus(Predicate, ErrorHandler) onStatus} handlers.
         * @return {@code ResponseSpec} to specify how to decode the body
         */
        ResponseSpec retrieve();

        /**
         * Exchange the {@link ClientHttpResponse} for a type {@code T}. This
         * can be useful for advanced scenarios, for example to decode the
         * response differently depending on the response status:
         * <pre class="code">
         * Person person = client.get()
         *     .uri("/people/1")
         *     .accept(MediaType.APPLICATION_JSON)
         *     .exchange((request, response) -&gt; {
         *         if (response.getStatusCode().equals(HttpStatus.OK)) {
         *             return deserialize(response.getBody());
         *         }
         *         else {
         *             throw new BusinessException();
         *         }
         *     });
         * </pre>
         * <p><strong>Note:</strong> The response is
         * {@linkplain ClientHttpResponse#close() closed} after the exchange
         * function has been invoked.
         * @param exchangeFunction the function to handle the response with
         * @param <T> the type the response will be transformed to
         * @return the value returned from the exchange function
         */
        default <T> T exchange(ExchangeFunction<T> exchangeFunction) {
            return exchange(exchangeFunction, true);
        }

        /**
         * Exchange the {@link ClientHttpResponse} for a type {@code T}. This
         * can be useful for advanced scenarios, for example to decode the
         * response differently depending on the response status:
         * <pre class="code">
         * Person person = client.get()
         *     .uri("/people/1")
         *     .accept(MediaType.APPLICATION_JSON)
         *     .exchange((request, response) -&gt; {
         *         if (response.getStatusCode().equals(HttpStatus.OK)) {
         *             return deserialize(response.getBody());
         *         }
         *         else {
         *             throw new BusinessException();
         *         }
         *     });
         * </pre>
         * <p><strong>Note:</strong> If {@code close} is {@code true},
         * then the response is {@linkplain ClientHttpResponse#close() closed}
         * after the exchange function has been invoked. When set to
         * {@code false}, the caller is responsible for closing the response.
         * @param exchangeFunction the function to handle the response with
         * @param close {@code true} to close the response after
         * {@code exchangeFunction} is invoked, {@code false} to keep it open
         * @param <T> the type the response will be transformed to
         * @return the value returned from the exchange function
         */
        <T> T exchange(ExchangeFunction<T> exchangeFunction, boolean close);
    }

    /**
     * Contract for specifying request headers and body leading up to the exchange.
     */
    interface BodySpec extends HeadersSpec<BodySpec> {

        /**
         * Set the length of the body in bytes, as specified by the
         * {@code Content-Length} header.
         * @param contentLength the content length
         * @return this builder
         * @see HttpHeaders#setContentLength(long)
         */
        BodySpec contentLength(long contentLength);

        /**
         * Set the {@linkplain MediaType media type} of the body, as specified
         * by the {@code Content-Type} header.
         * @param contentType the content type
         * @return this builder
         * @see HttpHeaders#setContentType(MediaType)
         */
        BodySpec contentType(MediaType contentType);

        /**
         * Set the body of the request to the given {@code Object}.
         * For example:
         * <pre class="code">
         * Person person = ... ;
         * ResponseEntity&lt;Void&gt; response = client.post()
         *     .uri("/persons/{id}", id)
         *     .contentType(MediaType.APPLICATION_JSON)
         *     .body(person)
         *     .retrieve()
         *     .toBodilessEntity();
         * </pre>
         * @param body the body of the request
         * @return this builder
         */
        BodySpec body(Object body);

        /**
         * Set the body of the request to the given {@code Object}.
         * The parameter {@code bodyType} is used to capture the generic type.
         * @param body the body of the request
         * @param bodyType the type of the body, used to capture the generic type
         * @return this builder
         */
        <T> BodySpec body(T body, ParameterizedTypeReference<T> bodyType);

        /**
         * Set the body of the request to the given function that writes to
         * an {@link OutputStream}.
         * @param body a function that takes an {@code OutputStream} and can
         * throw an {@code IOException}
         * @return this builder
         */
        BodySpec body(StreamingHttpOutputMessage.Body body);
    }

    /**
     * Contract for specifying request headers and URI for a request.
     * @param <S> a self reference to the spec type
     */
    interface HeadersUriSpec<S extends HeadersSpec<S>> extends UriSpec<S>, HeadersSpec<S> {}

    /**
     * Contract for specifying request headers, body and URI for a request.
     */
    interface BodyUriSpec extends BodySpec, HeadersUriSpec<BodySpec> {}
}