package vn.conyeu.restclient;

import io.micrometer.observation.ObservationRegistry;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.ClientHttpRequestInitializer;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.observation.ClientRequestObservationConvention;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.lang.Nullable;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.web.client.RestClient.RequestHeadersSpec;
import org.springframework.web.client.RestClient.ResponseSpec.ErrorHandler;
import org.springframework.web.util.UriBuilderFactory;
import vn.conyeu.commons.beans.ObjectMap;
import vn.conyeu.commons.utils.Asserts;
import vn.conyeu.commons.utils.Classes;
import vn.conyeu.commons.utils.Objects;

import java.nio.charset.Charset;
import java.time.Duration;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class ClientBuilder implements org.springframework.web.client.RestClient.Builder {
    private final org.springframework.web.client.RestClient.Builder delegate;
    private MultiValueMap<String, String> defaultQueries;
    private Map<String, ?> defaultUriVariables;
    private UriBuilderFactory uriBuilderFactory;
    private HttpHeaders defaultHeaders;
    private Consumer<RUriFactory> uriBuilderFactoryConsumer;
    private MultiValueMap<String, String> defaultCookies;

    private Boolean disableCookieManagement = true;
    private Boolean followRedirect = true;
    private Duration responseTimeout = Duration.ofMinutes(2);
    private Duration readTimeout = Duration.ofMinutes(2);
    private Duration connectTimeout = Duration.ofMinutes(2);
    private Integer maxRedirects = -1;
    private Boolean trustAllCertificate;
    private String baseUrl;

    public ClientBuilder() {
        delegate = org.springframework.web.client.RestClient.builder();
        setupDefaultConfig();
    }

    public ClientBuilder(ClientBuilder other) {
        delegate = other.delegate.clone().defaultHeaders(HttpHeaders::clear);
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
        defaultCookies = other.defaultCookies;
        defaultHeaders = other.defaultHeaders;

        if (other.defaultQueries != null) {
            defaultQueries = new LinkedMultiValueMap<>(other.defaultQueries);
        }

        if (other.defaultUriVariables != null) {
            defaultUriVariables = new LinkedHashMap<>(other.defaultUriVariables);
        }

    }

    public ClientBuilder setupDefaultConfig() {
        return this;
    }

    /**
     * {@inheritDoc}
     */
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

    /**
     * {@inheritDoc}
     */
    public ClientBuilder uriBuilderFactory(UriBuilderFactory uriBuilderFactory) {
        this.uriBuilderFactory = uriBuilderFactory;
        return this;
    }

    public ClientBuilder uriBuilderFactory(Consumer<RUriFactory> uriBuilderFactoryConsumer) {
        this.uriBuilderFactoryConsumer = uriBuilderFactoryConsumer;
        return this;
    }

    /**
     * {@inheritDoc}
     */
    public ClientBuilder defaultUriVariables(Map<String, ?> defaultUriVariables) {
        this.defaultUriVariables = defaultUriVariables;
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
        defaultHeader(HttpHeaders.CONTENT_TYPE, mediaType.toString());
        return this;
    }

    public ClientBuilder defaultCsrfToken(String csrfToken) {
        return defaultCsrfToken("csrf_token", csrfToken);
    }

    public ClientBuilder defaultCsrfToken(String tokenKey, String csrfToken) {
        if (csrfToken == null) defaultHeaders(h -> h.remove(tokenKey));
        else defaultHeader(tokenKey, csrfToken);
        return this;
    }

    private HttpHeaders initHeaders() {
        if(defaultHeaders == null) defaultHeaders = new HttpHeaders();
        return defaultHeaders;
    }

    /**
     * {@inheritDoc}
     */
    public ClientBuilder defaultHeader(String header, String... values) {
        if (values.length == 0 || values[0] == null) defaultHeaders(h -> h.remove(header));
        else initHeaders().put(header, Arrays.asList(values));
        return this;
    }

    /**
     * {@inheritDoc}
     */
    public ClientBuilder defaultHeaders(Consumer<HttpHeaders> headersConsumer) {
       headersConsumer.accept(initHeaders());
        return this;
    }

    /**
     * Add header from ObjectMap
     *
     * @param map the header values
     */
    public ClientBuilder defaultHeaders(ObjectMap map) {
        MultiValueMap<String, String> valueMap = new LinkedMultiValueMap<>();
        defaultHeaders(h -> h.putAll(ClientUtil.toMultiValueMap(map)));
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
        initCookies().put(cookieName, Arrays.asList(values));
        return this;
    }

    /**
     * Provides access to every {@link #defaultCookie(String, String...)}
     * declared so far with the possibility to add, replace, or remove.
     *
     * @param cookiesConsumer a function that consumes the cookies map
     */
    public ClientBuilder defaultCookies(Consumer<MultiValueMap<String, String>> cookiesConsumer) {
        cookiesConsumer.accept(initCookies());
        return this;
    }

    private MultiValueMap<String, String> initCookies() {
        if (defaultCookies == null) defaultCookies = new LinkedMultiValueMap<>();
        return defaultCookies;
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
    public ClientBuilder defaultRequest(Consumer<RequestHeadersSpec<?>> defaultRequest) {
        delegate.defaultRequest(defaultRequest);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    public ClientBuilder defaultStatusHandler(Predicate<HttpStatusCode> statusPredicate, ErrorHandler errorHandler) {
        delegate.defaultStatusHandler(statusPredicate, errorHandler);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    public ClientBuilder defaultStatusHandler(ResponseErrorHandler errorHandler) {
        delegate.defaultStatusHandler(errorHandler);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    public ClientBuilder requestInterceptor(ClientHttpRequestInterceptor interceptor) {
        delegate.requestInterceptor(interceptor);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    public ClientBuilder requestInterceptors(Consumer<List<ClientHttpRequestInterceptor>> interceptorsConsumer) {
        delegate.requestInterceptors(interceptorsConsumer);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    public ClientBuilder requestInitializer(ClientHttpRequestInitializer initializer) {
        delegate.requestInitializer(initializer);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    public ClientBuilder requestInitializers(Consumer<List<ClientHttpRequestInitializer>> initializersConsumer) {
        delegate.requestInitializers(initializersConsumer);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    public ClientBuilder requestFactory(ClientHttpRequestFactory requestFactory) {
        delegate.requestFactory(requestFactory);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    public ClientBuilder messageConverters(Consumer<List<HttpMessageConverter<?>>> configurer) {
        delegate.messageConverters(configurer);
        return this;
    }

    /**
     * Configure the {@link io.micrometer.observation.ObservationRegistry} to use
     * for recording HTTP client observations.
     *
     * @param observationRegistry the observation registry to use
     * @return this builder
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
    public ClientBuilder apply(Consumer<org.springframework.web.client.RestClient.Builder> builderConsumer) {
        delegate.apply(builderConsumer);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    public ClientBuilder clone() {
        return new ClientBuilder(this);
    }

    /**
     * Build the {@link RestClient} instance.
     */
    public RestClient build() {
        applyUriBuilderFactory();
        applyDefaultHeaders();
        org.springframework.web.client.RestClient client = delegate.build();
        return new RestClient(client, clone());
    }


    @Nullable
    private void applyDefaultHeaders() {
        delegate.defaultHeaders(HttpHeaders::clear);

        if(Objects.notEmpty(defaultHeaders)) {
            delegate.defaultHeaders(h -> h.putAll(defaultHeaders));
        }

        if(Objects.notEmpty(defaultCookies)) {
            throw new UnsupportedOperationException("applyDefaultHeaders");
            //delegate.defaultHeaders(h -> h.addAll(defaultCookies));
        }
    }

    private void applyUriBuilderFactory() {

        if (uriBuilderFactory != null) {
           delegate.uriBuilderFactory(uriBuilderFactory);
        }
        else {
            RUriFactory factory = RUriFactory.fromUriString(baseUrl);

            if (uriBuilderFactoryConsumer != null) {
                uriBuilderFactoryConsumer.accept(factory);
            }

            factory.setDefaultUriVariables(defaultUriVariables);
            factory.setDefaultQuery(defaultQueries);
            delegate.uriBuilderFactory(factory);
        }
    }


}