package vn.conyeu.restclient;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.http.client.ClientHttpRequest;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClient.RequestBodyUriSpec;
import org.springframework.web.client.RestClient.RequestHeadersSpec.ExchangeFunction;
import org.springframework.web.client.RestClient.ResponseSpec;
import org.springframework.web.util.UriBuilder;
import vn.conyeu.commons.utils.Objects;

import java.net.URI;
import java.nio.charset.Charset;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

class DefaultRClient implements RClient {
    private final RestClient rClient;
    private final RClient.Builder builder;
    private final Consumer<HeadersSpec<?>> defaultRequest;
    private final MultiValueMap<String, String> defaultCookies;

    DefaultRClient(RestClient rClient, Builder builder, Consumer<HeadersSpec<?>> defaultRequest, MultiValueMap<String, String> defaultCookies) {
        this.rClient = rClient;
        this.builder = builder;
        this.defaultRequest = defaultRequest;
        this.defaultCookies = defaultCookies;
    }

    public BodyUriSpec methodInternal(HttpMethod method) {
        RestClient.RequestBodyUriSpec rSpec = rClient.method(method);
        return new DefaultBodyUriSpec(rSpec);
    }

    class DefaultBodyUriSpec implements BodyUriSpec {
        private final RequestBodyUriSpec spec;
        private MultiValueMap<String, String> cookies;
        private ClientURI baseUri;

        DefaultBodyUriSpec(RequestBodyUriSpec spec) {
            this.spec = spec;
        }

        @Override
        public RClient.BodySpec uri(URI uri) {
            spec.uri(uri);
            return this;
        }

        @Override
        public RClient.BodySpec uri(String uri, Object... uriVariables) {
            spec.uri(uri, uriVariables);
            return this;
        }

        @Override
        public RClient.BodySpec uri(String uri, Map<String, ?> uriVariables) {
            spec.uri(uri, uriVariables);
            return this;
        }

        @Override
        public RClient.BodySpec uri(String uri, Function<UriBuilder, URI> uriFunction) {
            spec.uri(uri, uriFunction);
            return this;
        }

        @Override
        public RClient.BodySpec uri(Function<UriBuilder, URI> uriFunction) {
            spec.uri(uriFunction);
            return this;
        }

        @Override
        public RClient.BodySpec accept(MediaType... acceptableMediaTypes) {
            spec.accept(acceptableMediaTypes);
            return this;
        }

        @Override
        public RClient.BodySpec acceptCharset(Charset... acceptableCharsets) {
            spec.acceptCharset(acceptableCharsets);
            return this;
        }

        @Override
        public RClient.BodySpec ifModifiedSince(ZonedDateTime ifModifiedSince) {
            spec.ifModifiedSince(ifModifiedSince);
            return this;
        }

        @Override
        public RClient.BodySpec ifNoneMatch(String... ifNoneMatches) {
            spec.ifNoneMatch(ifNoneMatches);
            return this;
        }

        @Override
        public RClient.BodySpec header(String headerName, String... headerValues) {
            spec.header(headerName, headerValues);
            return this;
        }

        @Override
        public RClient.BodySpec contentLength(long contentLength) {
            spec.contentLength(contentLength);
            return this;
        }

        @Override
        public RClient.BodySpec contentType(MediaType contentType) {
            spec.contentType(contentType);
            return this;
        }

        @Override
        public RClient.BodySpec headers(Consumer<HttpHeaders> headersConsumer) {
            spec.headers(headersConsumer);
            return this;
        }

        @Override
        public BodySpec cookie(String name, String... values) {
            getCookies().put(name, Arrays.asList(values));
            return this;
        }

        @Override
        public BodySpec cookies(Consumer<MultiValueMap<String, String>> cookiesConsumer) {
            cookiesConsumer.accept(getCookies());
            return this;
        }

        @Override
        public RClient.BodySpec httpRequest(Consumer<ClientHttpRequest> requestConsumer) {
            spec.httpRequest(requestConsumer);
            return this;
        }

        @Override
        public RClient.BodySpec body(Object body) {
            spec.body(body);
            return this;
        }

        @Override
        public <T> RClient.BodySpec body(T body, ParameterizedTypeReference<T> bodyType) {
            spec.body(body, bodyType);
            return this;
        }

        @Override
        public RClient.BodySpec body(StreamingHttpOutputMessage.Body body) {
            spec.body(body);
            return this;
        }

        @Override
        public ResponseSpec retrieve() {
            applyAllBeforeExchange();
            ResponseSpec spec = this.spec.retrieve();
            return new DefaultResponseSpec(spec);
        }

        @Override
        public <T> T exchange(ExchangeFunction<T> exchangeFunction, boolean close) {
            applyAllBeforeExchange();
            return spec.exchange(exchangeFunction, close);
        }

        private void applyAllBeforeExchange() {

            if(defaultRequest != null) {
                defaultRequest.accept(this);
            }

            MultiValueMap<String, String> cookies = new LinkedMultiValueMap<>();
            if (Objects.notEmpty(defaultCookies)) cookies.putAll(defaultCookies);
            if (Objects.notEmpty(this.cookies)) cookies.putAll(this.cookies);

            if (Objects.notEmpty(cookies)) {
                List<String> cks = new ArrayList<>();
                cookies.forEach((key, list) -> list.forEach(v -> cks.add(key + "=" + v)));
                headers(h -> h.addAll(HttpHeaders.COOKIE, cks));
            }
        }

        private MultiValueMap<String, String> getCookies() {
            if (cookies == null) cookies = new LinkedMultiValueMap<>();
            return cookies;
        }

    }

    static class DefaultResponseSpec implements ResponseSpec {
        final ResponseSpec delegate;

        DefaultResponseSpec(ResponseSpec delegate) {
            this.delegate = delegate;
        }

        @Override
        public ResponseSpec onStatus(Predicate<HttpStatusCode> statusPredicate, ErrorHandler errorHandler) {
            delegate.onStatus(statusPredicate, errorHandler);
            return this;
        }

        @Override
        public ResponseSpec onStatus(ResponseErrorHandler errorHandler) {
            delegate.onStatus(errorHandler);
            return this;
        }

        @Override
        public <T> T body(Class<T> bodyType) {
            return delegate.body(bodyType);
        }

        @Override
        public <T> T body(ParameterizedTypeReference<T> bodyType) {
            return delegate.body(bodyType);
        }

        @Override
        public <T> ResponseEntity<T> toEntity(Class<T> bodyType) {
            return delegate.toEntity(bodyType);
        }

        @Override
        public <T> ResponseEntity<T> toEntity(ParameterizedTypeReference<T> bodyType) {
            return delegate.toEntity(bodyType);
        }

        @Override
        public ResponseEntity<Void> toBodilessEntity() {
            return delegate.toBodilessEntity();
        }
    }

}