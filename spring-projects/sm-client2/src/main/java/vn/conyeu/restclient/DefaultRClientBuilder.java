package vn.conyeu.restclient;

import io.micrometer.observation.ObservationRegistry;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.client.*;
import org.springframework.http.client.observation.ClientRequestObservationConvention;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.util.ClassUtils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClient.ResponseSpec.ErrorHandler;
import org.springframework.web.util.DefaultUriBuilderFactory;
import org.springframework.web.util.UriBuilder;
import org.springframework.web.util.UriBuilderFactory;
import vn.conyeu.commons.utils.Asserts;
import vn.conyeu.restclient.factory.DelegateJettyClientHttpRequestFactory;
import vn.conyeu.restclient.factory.DelegateReactorNettyClientRequestFactory;
import vn.conyeu.restclient.factory.DelegateSimpleClientHttpRequestFactory;

import java.net.URI;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

class DefaultRClientBuilder implements RClient.Builder {
    private final RestClient.Builder delegate;
    private Consumer<RClient.HeadersSpec<?>> defaultRequest;
    private MultiValueMap<String, String> defaultCookies;
    private Map<String, ?> defaultUriVariables;
    private ClientHttpRequestFactory requestFactory;

    private String baseUrl;
    private Function<UriBuilder, URI> uriFunction;
    private UriBuilderFactory uriBuilderFactory;

    private Boolean disableCookie;
    private Boolean followRedirects;
    private Integer maxRedirects;
    private Duration readTimeout;
    private Duration connectTimeout;
    private Duration responseTimeout;

    DefaultRClientBuilder() {
        delegate = RestClient.builder();
    }

    DefaultRClientBuilder(DefaultRClientBuilder other) {
        delegate = other.delegate.clone();
        defaultRequest = other.defaultRequest;
        baseUrl = other.baseUrl;
        uriFunction = other.uriFunction;
        uriBuilderFactory = other.uriBuilderFactory;
        disableCookie = other.disableCookie;
        followRedirects = other.followRedirects;
        maxRedirects = other.maxRedirects;
        readTimeout = other.readTimeout;
        connectTimeout = other.connectTimeout;
        responseTimeout = other.responseTimeout;
        requestFactory = other.requestFactory;

        if(other.defaultCookies != null) {
            defaultCookies = new LinkedMultiValueMap<>(other.defaultCookies);
        }

        if(other.defaultUriVariables != null) {
            defaultUriVariables = new HashMap<>(other.defaultUriVariables);
        }
    }

    @Override
    public RClient.Builder baseUrl(String baseUrl) {
       this.baseUrl = baseUrl;
       return this;
    }

    @Override
    public RClient.Builder baseUrl(Function<UriBuilder, URI> uriFunction) {
        this.uriFunction = uriFunction;
        return this;
    }


    @Override
    public DefaultRClientBuilder defaultUriVariables(Map<String, ?> defaultUriVariables) {
        this.defaultUriVariables = defaultUriVariables;
        return this;
    }

    @Override
    public RClient.Builder uriBuilderFactory(UriBuilderFactory uriBuilderFactory) {
        this.uriBuilderFactory = uriBuilderFactory;
        return this;
    }

    private MultiValueMap<String, String> initCookies() {
        if(defaultCookies == null) defaultCookies = new LinkedMultiValueMap<>();
        return defaultCookies;
    }

    @Override
    public RClient.Builder defaultCookie(String cookie, String... values) {
        initCookies().put(cookie, Arrays.asList(values));
        return this;
    }

    @Override
    public RClient.Builder defaultCookie(String cookie, List<String> values) {
        initCookies().put(cookie, new ArrayList<>(values));
        return this;
    }

    @Override
    public RClient.Builder defaultCookies(Consumer<MultiValueMap<String, String>> cookiesConsumer) {
        cookiesConsumer.accept(initCookies());
        return this;
    }

    @Override
    public RClient.Builder userAgent(String agent) {
        return defaultHeader(HttpHeaders.USER_AGENT, agent);
    }

    @Override
    public RClient.Builder basicAuth(String username, String password) {
        return basicAuth(username, password, StandardCharsets.ISO_8859_1);
    }

    @Override
    public RClient.Builder basicAuth(String username, String password, Charset charset) {
        return basicAuth(HttpHeaders.encodeBasicAuth(username, password, charset));
    }

    @Override
    public RClient.Builder basicAuth(String encodedCredentials) {
        Asserts.notBlank(encodedCredentials, "encodedCredentials must not be null or blank");
        return defaultHeader(HttpHeaders.AUTHORIZATION, "Basic " + encodedCredentials);
    }

    @Override
    public RClient.Builder bearerToken(String bearerToken) {
        return defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer " + bearerToken);
    }

    @Override
    public RClient.Builder defaultContentType(MediaType mediaType) {
        return defaultHeader(HttpHeaders.CONTENT_TYPE, mediaType.toString());
    }

    @Override
    public RClient.Builder defaultHeader(String header, String... values) {
        delegate.defaultHeader(header, values);
        return this;
    }

    @Override
    public RClient.Builder defaultHeaders(Consumer<HttpHeaders> headersConsumer) {
        delegate.defaultHeaders(headersConsumer);
        return this;
    }

    public RClient.Builder defaultRequest(Consumer<RClient.HeadersSpec<?>> defaultRequest) {
        this.defaultRequest = this.defaultRequest != null ?
                this.defaultRequest.andThen(defaultRequest) : defaultRequest;
        return this;
    }

    @Override
    public RClient.Builder defaultStatusHandler(Predicate<HttpStatusCode> statusPredicate, ErrorHandler errorHandler) {
        delegate.defaultStatusHandler(statusPredicate, errorHandler);
        return this;
    }

    @Override
    public RClient.Builder defaultStatusHandler(ResponseErrorHandler errorHandler) {
        delegate.defaultStatusHandler(errorHandler);
        return this;
    }

    @Override
    public RClient.Builder requestInterceptor(ClientHttpRequestInterceptor interceptor) {
        delegate.requestInterceptor(interceptor);
        return this;
    }

    @Override
    public RClient.Builder requestInterceptors(Consumer<List<ClientHttpRequestInterceptor>> interceptorsConsumer) {
        delegate.requestInterceptors(interceptorsConsumer);
        return this;
    }

    @Override
    public RClient.Builder requestInitializer(ClientHttpRequestInitializer initializer) {
        delegate.requestInitializer(initializer);
        return this;
    }

    @Override
    public RClient.Builder requestInitializers(Consumer<List<ClientHttpRequestInitializer>> initializersConsumer) {
        delegate.requestInitializers(initializersConsumer);
        return this;
    }

    @Override
    public RClient.Builder requestFactory(ClientHttpRequestFactory requestFactory) {
        this.requestFactory = requestFactory;
        return this;
    }

    @Override
    public RClient.Builder requestFactory(DelegateRequestFactory requestFactory) {
        this.requestFactory = requestFactory;
        return this;
    }

    @Override
    public RClient.Builder disableCookie(boolean disabled) {
        this.disableCookie = disabled;
        return this;
    }

    @Override
    public RClient.Builder maxRedirects(int maxRedirects) {
        this.maxRedirects = maxRedirects;
        return this;
    }

    @Override
    public RClient.Builder followRedirects(boolean follow) {
        this.followRedirects = follow;
        return this;
    }

    @Override
    public RClient.Builder responseTimeout(int timeout) {
        this.responseTimeout = Duration.ofMillis(timeout);
        return this;
    }

    @Override
    public RClient.Builder responseTimeout(Duration timeout) {
        this.responseTimeout = timeout;
        return this;
    }

    @Override
    public RClient.Builder connectTimeout(int timeout) {
        this.connectTimeout =  Duration.ofMillis(timeout);
        return this;
    }

    @Override
    public RClient.Builder connectTimeout(Duration timeout) {
        this.connectTimeout = timeout;
        return this;
    }

    @Override
    public RClient.Builder readTimeout(int readTimeout) {
        this.readTimeout = Duration.ofMillis(readTimeout);
        return this;
    }

    @Override
    public RClient.Builder readTimeout(Duration readTimeout) {
        this.readTimeout = readTimeout;
        return this;
    }

    @Override
    public RClient.Builder messageConverters(Consumer<List<HttpMessageConverter<?>>> configurer) {
        delegate.messageConverters(configurer);
        return this;
    }

    @Override
    public RClient.Builder observationRegistry(ObservationRegistry observationRegistry) {
        delegate.observationRegistry(observationRegistry);
        return this;
    }

    @Override
    public RClient.Builder observationConvention(ClientRequestObservationConvention observationConvention) {
        delegate.observationConvention(observationConvention);
        return this;
    }

    public RClient.Builder apply(Consumer<RClient.Builder> builderConsumer) {
        builderConsumer.accept(this);
        return this;
    }

    @Override
    public RClient.Builder clone() {
        return new DefaultRClientBuilder(this);
    }

    @Override
    public RClient build() {
        UriBuilderFactory uriBuilderFactory = initUriBuilderFactory();
        ClientHttpRequestFactory requestFactory = initDelegateRequestFactory();
        MultiValueMap<String, String> defaultCookies = copyCookies();

        RestClient rClient = delegate
                .uriBuilderFactory(uriBuilderFactory)
                .requestFactory(requestFactory).build();

        return new DefaultRClient(rClient, clone(), defaultRequest, defaultCookies);
    }

    private ClientHttpRequestFactory initDelegateRequestFactory() {
        ClientHttpRequestFactory factory = initRequestFactory();
        DelegateRequestFactory delegateFactory = null;

        if(factory instanceof DelegateRequestFactory df) {
            delegateFactory = df;
        }

        if(factory instanceof ReactorNettyClientRequestFactory netty) {
            delegateFactory = new DelegateReactorNettyClientRequestFactory(netty);
        }

        if(factory instanceof JettyClientHttpRequestFactory jetty) {
            delegateFactory = new DelegateJettyClientHttpRequestFactory(jetty);
        }

        if(factory instanceof SimpleClientHttpRequestFactory simple) {
            delegateFactory = new DelegateSimpleClientHttpRequestFactory(simple);
        }

        if(delegateFactory == null) return factory;

        else {
            delegateFactory.setConnectTimeout(connectTimeout);
            delegateFactory.setReadTimeout(readTimeout);
            return delegateFactory;
        }
    }

    private MultiValueMap<String, String> copyCookies() {
        return defaultCookies == null ? new LinkedMultiValueMap<>()
                : new LinkedMultiValueMap<>(defaultCookies);
    }

    private ClientHttpRequestFactory initRequestFactory() {
        if (requestFactory != null) {
            return requestFactory;
        }
        else if(reactorNettyClient) {
            return new ReactorNettyClientRequestFactory();
        }
        else if (httpComponentsClientPresent) {
            return new HttpComponentsClientHttpRequestFactory();
        }
        else if (jettyClientPresent) {
            return new JettyClientHttpRequestFactory();
        }
        else if (jdkClientPresent) {
            // java.net.http module might not be loaded, so we can't default to the JDK HttpClient
            return new JdkClientHttpRequestFactory();
        }
        else {
            return new SimpleClientHttpRequestFactory();
        }
    }

    private UriBuilderFactory initUriBuilderFactory() {

        if(uriBuilderFactory != null) {
            return uriBuilderFactory;
        }

        DefaultUriBuilderFactory factory = baseUrl == null ? new DefaultUriBuilderFactory()
                : new DefaultUriBuilderFactory(baseUrl);

        factory.setDefaultUriVariables(defaultUriVariables);

        if(uriFunction != null) {
            URI uri = uriFunction.apply(factory.builder());
            return new DefaultUriBuilderFactory(uri.toString());
        }

        return factory;
    }

    //------------------------------
    private static final boolean httpComponentsClientPresent;
    private static final boolean jettyClientPresent;
    private static final boolean jdkClientPresent;
    private static final boolean reactorNettyClient;

    static {
        ClassLoader loader = DefaultRClientBuilder.class.getClassLoader();

        httpComponentsClientPresent = ClassUtils.isPresent("org.apache.hc.client5.http.classic.HttpClient", loader);
        jettyClientPresent = ClassUtils.isPresent("org.eclipse.jetty.client.HttpClient", loader);
        jdkClientPresent = ClassUtils.isPresent("java.net.http.HttpClient", loader);
        reactorNettyClient = ClassUtils.isPresent("reactor.netty.http.client", loader);

    }
}