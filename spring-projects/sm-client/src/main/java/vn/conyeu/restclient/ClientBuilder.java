package vn.conyeu.restclient;

import io.micrometer.observation.ObservationRegistry;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ClientHttpConnector;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.http.codec.ClientCodecConfigurer;
import org.springframework.lang.Nullable;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.*;
import org.springframework.web.util.UriBuilderFactory;
import reactor.core.publisher.Mono;
import vn.conyeu.commons.beans.ObjectMap;
import vn.conyeu.commons.utils.Asserts;
import vn.conyeu.commons.utils.Classes;
import vn.conyeu.commons.utils.Objects;

import java.nio.charset.Charset;
import java.time.Duration;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;


public class ClientBuilder implements WebClient.Builder {
    private final WebClient.Builder delegate;
    private MultiValueMap<String, String> defaultQueries;
    private Map<String, Object> defaultUriVariables;
    private UriBuilderFactory uriBuilderFactory;
    private Consumer<SimpleUriFactory> uriBuilderFactoryConsumer;

    private Boolean disableCookieManagement = true;
    private Boolean followRedirect = true;
    private Duration responseTimeout = Duration.ofMinutes(2);
    private Duration readTimeout = Duration.ofMinutes(2);
    private Duration connectTimeout = Duration.ofMinutes(2);
    private Integer maxRedirects = -1;
    private Boolean trustAllCertificate;
    private String baseUrl;

    public ClientBuilder() {
        delegate = WebClient.builder();
        setupDefaultConfig();
    }

    public ClientBuilder(ClientBuilder other) {
        delegate = other.delegate.clone();
        baseUrl = other.baseUrl;
        uriBuilderFactory = other.uriBuilderFactory;
        disableCookieManagement = other.disableCookieManagement;
        followRedirect = other.followRedirect;
        responseTimeout = other.responseTimeout;
        readTimeout = other.readTimeout;
        connectTimeout = other.connectTimeout;
        maxRedirects = other.maxRedirects;
        trustAllCertificate = other.trustAllCertificate;
        uriBuilderFactoryConsumer = other.uriBuilderFactoryConsumer;

        if(other.defaultQueries != null) {
            defaultQueries = new LinkedMultiValueMap<>(other.defaultQueries);
        }
        
        if(other.defaultUriVariables != null) {
            defaultUriVariables = new LinkedHashMap<>(other.defaultUriVariables);
        }
        
    }

    public ClientBuilder setupDefaultConfig() {
        clientConnector(new ReactorClientHttpConnector());
        return this;
    }

    /**{@inheritDoc}*/
    public ClientBuilder baseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
        return this;
    }

    public ClientBuilder trustAllCertificate(Boolean trustAllCertificate) {
        this.trustAllCertificate = trustAllCertificate;
        return this;
    }

    public ClientBuilder responseTimeout(Duration responseTimeout) {
        this.responseTimeout = responseTimeout;
        return this;
    }

    public ClientBuilder readTimeout(Duration readTimeout) {
        this.readTimeout = readTimeout;
        return this;
    }

    public ClientBuilder connectTimeout(Duration connectTimeout) {
        this.connectTimeout = connectTimeout;
        return this;
    }

    public ClientBuilder disableCookieManagement(Boolean disableCookieManagement) {
        this.disableCookieManagement = disableCookieManagement;
        return this;
    }

    public ClientBuilder followRedirect(Boolean followRedirect) {
        this.followRedirect = followRedirect;
        return this;
    }

    /**{@inheritDoc}*/
    public ClientBuilder uriBuilderFactory(UriBuilderFactory uriBuilderFactory) {
        this.uriBuilderFactory = uriBuilderFactory;
        return this;
    }

    public ClientBuilder uriBuilderFactory(Consumer<SimpleUriFactory> uriBuilderFactoryConsumer) {
        this.uriBuilderFactoryConsumer = uriBuilderFactoryConsumer;
        return this;
    }

    /**
     * {@inheritDoc}
     */
    public ClientBuilder defaultUriVariables(Map<String, ?> defaultUriVariables) {
        delegate.defaultUriVariables(defaultUriVariables);
        return this;
    }

    /**
     * Custom user-agent header
     *
     * @param agent the user agent
     */
    public ClientBuilder userAgent(String agent) {
        defaultHeader(HttpHeaders.USER_AGENT, agent);
        return this;
    }

    /**
     * Custom header `Authorization`
     *
     * @param username the username
     * @param password the password
     */
    public ClientBuilder basicAuth(String username, String password) {
        return basicAuth(username, password, null);
    }

    /**
     * Custom header `Authorization`
     *
     * @param username the username
     * @param password the password
     * @param charset  the charset to use to convert the credentials into an octet sequence. Defaults to ISO-8859-1.
     */
    public ClientBuilder basicAuth(String username, String password, @Nullable Charset charset) {
        return basicAuth(HttpHeaders.encodeBasicAuth(username, password, charset));
    }

    /**
     * Custom header `Authorization`
     *
     * @param encodedCredentials String
     */
    public ClientBuilder basicAuth(String encodedCredentials) {
        Asserts.notBlank(encodedCredentials, "encodedCredentials must not be null or blank");
        return defaultHeader(HttpHeaders.AUTHORIZATION, "Basic " + encodedCredentials);
    }

    /**
     * Configure default bearer token
     *
     * @param bearerToken String
     */
    public ClientBuilder bearerToken(String bearerToken) {
        return defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer " + bearerToken);
    }

    public ClientBuilder defaultContentType(MediaType mediaType) {
        delegate.defaultHeader(HttpHeaders.CONTENT_TYPE, mediaType.toString());
        return this;
    }

    public ClientBuilder defaultCsrfToken(String csrfToken) {
       return defaultCsrfToken("csrf_token", csrfToken);
    }

    public ClientBuilder defaultCsrfToken(String tokenKey, String csrfToken) {
        defaultHeader(tokenKey, csrfToken);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    public ClientBuilder defaultHeader(String header, String... values) {
        delegate.defaultHeader(header, values);
        return this;
    }

    public ClientBuilder defaultHeader(String header, Object... values) {
        List<String> list = Stream.of(values).filter(Objects::nonNull).map(Object::toString).toList();
        delegate.defaultHeaders(headers -> headers.addAll(header, list));
        return this;
    }

    /**
     * {@inheritDoc}
     */
    public ClientBuilder defaultHeaders(Consumer<HttpHeaders> headersConsumer) {
        delegate.defaultHeaders(headersConsumer);
        return this;
    }

    /**
     * Add header from ObjectMap
     *
     * @param map the header values
     */
    public ClientBuilder defaultHeaders(ObjectMap map) {
        MultiValueMap<String, String> valueMap = new LinkedMultiValueMap<>();
        delegate.defaultHeaders(h -> h.putAll(ClientUtil.toMultiValueMap(map)));
        return this;
    }

    /**
     * Global option to specify a cookie to be added to every request,
     * if the request does not already contain such a cookie.
     *
     * @param cookieName the cookie name
     * @param values     the cookie values
     */
    public ClientBuilder defaultCookie(String cookieName, String... values) {
       delegate.defaultCookie(cookieName, values);
        return this;
    }

    /**
     * Provides access to every {@link #defaultCookie(String, String...)}
     * declared so far with the possibility to add, replace, or remove.
     *
     * @param cookiesConsumer a function that consumes the cookies map
     */
    public ClientBuilder defaultCookies(Consumer<MultiValueMap<String, String>> cookiesConsumer) {
        delegate.defaultCookies(cookiesConsumer);
        return this;
    }
    
    /**
     * Custom query
     *
     * @param queryConsumer the consumer query
     */
    public ClientBuilder defaultQuery(Consumer<MultiValueMap<String, String>> queryConsumer) {
        queryConsumer.accept(initQuery());
        return this;
    }

    /**
     * Custom query
     *
     * @param params the query
     */
    public ClientBuilder defaultQueries(MultiValueMap<String, String> params) {
        initQuery().putAll(params);
        return this;
    }

    /**
     * Custom query
     *
     * @param params the query
     */
    public ClientBuilder defaultQueries(ObjectMap params) {
        initQuery().putAll(ClientUtil.toMultiValueMap(params));
        return this;
    }

    /**
     * Custom query
     */
    public ClientBuilder defaultQuery(String field, List<String> value) {
        initQuery().put(field, value);
        return this;
    }

    /**
     * Custom query
     */
    public ClientBuilder defaultQuery(String field, Object value) {
        MultiValueMap<String, String> query = initQuery();
        if (value == null) {
            query.remove(field);
            return this;
        }

        //isPrimitiveOrWrapper
        if (Classes.isPrimitiveOrWrapper(value.getClass())) {
            query.set(field, value.toString());
        }//
        else if (value.getClass().isArray()) {
            List<String> values = Objects.toStringList(value);
            query.put(field, values);
        }//
        else if (value instanceof Collection<?> list) {
            List<String> values = Objects.toStringList(list);
            query.put(field, values);
        }//
        else {
            String msg = "The value `%s` only support type [Primitive, Array, Collection]";
            throw Objects.newIllegal(msg, value.getClass().getName());
        }

        return this;
    }

    private MultiValueMap<String, String> initQuery() {
        if (defaultQueries == null) defaultQueries = new LinkedMultiValueMap<>();
        return defaultQueries;
    }


    /**
     * {@inheritDoc}
     */
    public ClientBuilder defaultRequest(Consumer<WebClient.RequestHeadersSpec<?>> defaultRequest) {
        delegate.defaultRequest(defaultRequest);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    public ClientBuilder defaultStatusHandler(Predicate<HttpStatusCode> statusPredicate, Function<ClientResponse, Mono<? extends Throwable>> exceptionFunction) {
        delegate.defaultStatusHandler(statusPredicate, exceptionFunction);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    public ClientBuilder filter(ExchangeFilterFunction filter) {
        delegate.filter(filter);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    public ClientBuilder filters(Consumer<List<ExchangeFilterFunction>> filtersConsumer) {
        delegate.filters(filtersConsumer);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    public ClientBuilder clientConnector(ClientHttpConnector connector) {
        delegate.clientConnector(connector);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    public ClientBuilder codecs(Consumer<ClientCodecConfigurer> configurer) {
        delegate.codecs(configurer);
        return this;
    }

    /**
     * Configure a limit on the number of bytes that can be buffered whenever
     * the input stream needs to be aggregated.All codecs are limited to 256K by default.
     *
     * @param byteCount the max number of bytes to buffer, or -1 for unlimited
     */
    public ClientBuilder maxInMemorySize(int byteCount) {
        return codecs(config -> config.defaultCodecs()
                .maxInMemorySize(byteCount));
    }

    /**
     * {@inheritDoc}
     */
    public ClientBuilder exchangeStrategies(ExchangeStrategies strategies) {
        delegate.exchangeStrategies(strategies);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Deprecated
    public ClientBuilder exchangeStrategies(Consumer<ExchangeStrategies.Builder> configurer) {
        throw new UnsupportedOperationException("@Deprecated");
    }

    /**
     * {@inheritDoc}
     */
    public ClientBuilder exchangeFunction(ExchangeFunction exchangeFunction) {
        delegate.exchangeFunction(exchangeFunction);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    public ClientBuilder observationRegistry(ObservationRegistry observationRegistry) {
        delegate.observationRegistry(observationRegistry);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    public ClientBuilder observationConvention(ClientRequestObservationConvention observationConvention) {
        delegate.observationConvention(observationConvention);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    public ClientBuilder apply(Consumer<WebClient.Builder> builderConsumer) {
        delegate.apply(builderConsumer);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    public ClientBuilder clone() {
        return new ClientBuilder(this);
    }

    /**{@inheritDoc}*/
    public RestClient build() {
        delegate.uriBuilderFactory(initUriBuilderFactory());
        WebClient clientDelegate = delegate.build();
        return new RestClient(clientDelegate, clone());
    }

    protected UriBuilderFactory initUriBuilderFactory() {

        if(uriBuilderFactory != null) {
            return uriBuilderFactory;
        }

        SimpleUriFactory factory = SimpleUriFactory.fromUriString(baseUrl);

        if(uriBuilderFactoryConsumer != null) {
            uriBuilderFactoryConsumer.accept(factory);
        }

        factory.setDefaultUriVariables(defaultUriVariables);
        factory.setDefaultQuery(defaultQueries);
        return factory;
    }

}