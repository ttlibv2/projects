package vn.conyeu.restclient;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.http.client.ClientHttpRequest;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.web.client.RestClient.RequestBodyUriSpec;
import org.springframework.web.client.RestClient.ResponseSpec;
import org.springframework.web.client.RestClient.RequestHeadersSpec.ExchangeFunction;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.util.UriBuilder;
import org.springframework.web.util.UriBuilderFactory;
import vn.conyeu.commons.utils.Asserts;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import java.nio.charset.Charset;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

public class RestRequest<T> {
    private final Builder builder;

    RestRequest(Builder builder) {
        this.builder = builder;
    }

    public static Builder builder() {
        return new Builder();
    }

    public void execute() {

        ClientBuilder clientBuilder = RestClient.builder();

        if (builder.builderConsumer != null) {
            builder.builderConsumer.accept(clientBuilder);
        }

        RequestBodyUriSpec uriSpec = clientBuilder.build().method(builder.httpMethod);

        // RequestHeadersUriSpec
        builder.requestSpecConsumer.accept(uriSpec);

        ResponseSpec responseSpec = uriSpec.retrieve();
        if(builder.responseSpecConsumer != null) {
            builder.responseSpecConsumer.accept(responseSpec);
        }







    }


    public static class Builder {
        private Consumer<ClientBuilder> builderConsumer;
        private MultiValueMap<String, String> cookies;
        private Map<String, Object> attributes;
        private HttpMethod httpMethod;
        private PayloadBuilder payloadBuilder;
        private ResponseBuilder responseBuilder;
        //private HttpHeaders headers;

        private Consumer<RequestBodyUriSpec> requestSpecConsumer;
        private Consumer<ResponseSpec> responseSpecConsumer;

        public Builder() {
        }

        private Builder(Builder other) {
            builderConsumer = other.builderConsumer;
            requestSpecConsumer = other.requestSpecConsumer;
            payloadBuilder = other.payloadBuilder;
            cookies = other.cookies;
            attributes = other.attributes;
            httpMethod = other.httpMethod;
        }

        public Builder applyBuilder(Consumer<ClientBuilder> consumer) {
            this.builderConsumer = builderConsumer == null ? consumer : builderConsumer.andThen(consumer);
            return this;
        }

        public <T> RestRequest<T> build() {
            Asserts.notNull(httpMethod, "@HttpMethod");
            return new RestRequest<>(this);
        }

        public Builder clone() {
            return new Builder(this);
        }

        private void applyRequestSpec(Consumer<RequestBodyUriSpec> consumer) {
            requestSpecConsumer = requestSpecConsumer == null ? consumer : requestSpecConsumer.andThen(consumer);
        }


        /**
         * Start building a request for the given {@code HttpMethod}.
         */
        public Builder httpMethod(HttpMethod httpMethod) {
            this.httpMethod = httpMethod;
            return this;
        }

        /**
         * Specify the URI using an absolute, fully constructed {@link URI}.
         */
        public Builder uri(URI uri) {
            applyRequestSpec(spec -> spec.uri(uri));
            return this;
        }

        /**
         * Specify the URI for the request using a URI template and URI variables.
         * <p>If a {@link UriBuilderFactory} was configured for the client (e.g.
         * with a base URI) it will be used to expand the URI template.
         */
        public Builder uri(String uri, Object... uriVariables) {
            applyRequestSpec(spec -> spec.uri(uri, uriVariables));
            return this;
        }

        /**
         * Specify the URI for the request using a URI template and URI variables.
         * <p>If a {@link UriBuilderFactory} was configured for the client (e.g.
         * with a base URI) it will be used to expand the URI template.
         */
        public Builder uri(String uri, Map<String, ?> uriVariables) {
            applyRequestSpec(spec -> spec.uri(uri, uriVariables));
            return this;
        }

        /**
         * Specify the URI starting with a URI template and finishing off with a
         * {@link UriBuilder} created from the template.
         */
        public Builder uri(String uri, Function<UriBuilder, URI> uriFunction) {
            applyRequestSpec(spec -> spec.uri(uri, uriFunction));
            return this;
        }

        /**
         * Specify the URI by through a {@link UriBuilder}.
         *
         * @see #uri(String, Function)
         */
        public Builder uri(Function<UriBuilder, URI> uriFunction) {
            applyRequestSpec(spec -> spec.uri(uriFunction));
            return this;
        }

        /**
         * Set the list of acceptable {@linkplain MediaType media types}, as
         * specified by the {@code Accept} header.
         *
         * @param acceptableMediaTypes the acceptable media types
         * @return this builder
         */
        public Builder accept(MediaType... acceptableMediaTypes) {
            applyRequestSpec(spec -> spec.accept(acceptableMediaTypes));
            return this;
        }

        /**
         * Set the list of acceptable {@linkplain Charset charsets}, as specified
         * by the {@code Accept-Charset} header.
         *
         * @param acceptableCharsets the acceptable charsets
         * @return this builder
         */
        public Builder acceptCharset(Charset... acceptableCharsets) {
            applyRequestSpec(spec -> spec.acceptCharset(acceptableCharsets));
            return this;
        }

        /**
         * Set the value of the {@code If-Modified-Since} header.
         *
         * @param ifModifiedSince the new value of the header
         * @return this builder
         */
        public Builder ifModifiedSince(ZonedDateTime ifModifiedSince) {
            applyRequestSpec(spec -> spec.ifModifiedSince(ifModifiedSince));
            return this;
        }

        /**
         * Add the given, single header value under the given name.
         *
         * @param headerName   the header name
         * @param headerValues the header value(s)
         * @return this builder
         */
        public Builder header(String headerName, String... headerValues) {
            applyRequestSpec(spec -> spec.header(headerName, headerValues));
            return this;
        }

        /**
         * Provides access to every header declared so far with the possibility
         * to add, replace, or remove values.
         *
         * @param headersConsumer the consumer to provide access to
         * @return this builder
         */
        public Builder headers(Consumer<HttpHeaders> headersConsumer) {
            applyRequestSpec(spec -> spec.headers(headersConsumer));
            return this;
        }

        /**
         * Add a cookie with the given name and value.
         *
         * @param name  the cookie name
         * @param value the cookie value
         * @return this builder
         */
        public Builder cookie(String name, String value) {
            initCookies().set(name, value);
            return this;
        }

        /**
         * Provides access to every cookie declared so far with the possibility
         * to add, replace, or remove values.
         *
         * @param cookiesConsumer the consumer to provide access to
         * @return this builder
         */
        public Builder cookies(Consumer<MultiValueMap<String, String>> cookiesConsumer) {
            cookiesConsumer.accept(initCookies());
            return this;
        }

        /**
         * Set the attribute with the given name to the given value.
         *
         * @param name  the name of the attribute to add
         * @param value the value of the attribute to add
         * @return this builder
         */
        public Builder attribute(String name, Object value) {
            initAttributes().put(name, value);
            return this;
        }

        /**
         * Provides access to every attribute declared so far with the
         * possibility to add, replace, or remove values.
         *
         * @param attributesConsumer the consumer to provide access to
         * @return this builder
         */
        public Builder attributes(Consumer<Map<String, Object>> attributesConsumer) {
            attributesConsumer.accept(initAttributes());
            return this;
        }

        /**
         * Callback for access to the {@link ClientHttpRequest} that in turn
         * provides access to the native request of the underlying HTTP library.
         * <p>This could be useful for setting advanced, per-request options that
         * are exposed by the underlying library.
         *
         * @param requestConsumer a consumer to access the
         *                        {@code ClientHttpRequest} with
         * @return this builder
         */
        public Builder httpRequest(Consumer<ClientHttpRequest> requestConsumer) {
            applyRequestSpec(spec -> spec.httpRequest(requestConsumer));
            return this;
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
         * <p><strong>Note:</strong> The response is
         * {@linkplain ClientHttpResponse#close() closed} after the exchange
         * function has been invoked.
         *
         * @param exchangeFunction the function to handle the response with
         * @return the value returned from the exchange function
         */
        public <T> Builder exchange(ExchangeFunction<T> exchangeFunction) {
            applyRequestSpec(spec -> spec.exchange(exchangeFunction));
            return this;
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
        public <T> Builder exchange(ExchangeFunction<T> exchangeFunction, boolean close) {
            applyRequestSpec(spec -> spec.exchange(exchangeFunction, close));
            return this;
        }

        public <T> PayloadBuilder payload() {
            if(payloadBuilder == null) payloadBuilder = new PayloadBuilder<>(this);
            return payloadBuilder;
        }

        public ResponseBuilder response() {
            if(responseBuilder == null) responseBuilder = new ResponseBuilder();
            return responseBuilder;
        }

        private PayloadBuilder initPayLoad() {
            if(payloadBuilder == null) payloadBuilder = new PayloadBuilder<>(this);
            return payloadBuilder;
        }
        private MultiValueMap<String, String> initCookies() {
            if (cookies == null) cookies = new LinkedMultiValueMap<>();
            return cookies;
        }

        private Map<String, Object> initAttributes() {
            if (attributes == null) attributes = new HashMap<>();
            return attributes;
        }


        public class ResponseBuilder {
            private void applyResponseSpec(Consumer<ResponseSpec> consumer) {
                responseSpecConsumer = responseSpecConsumer == null ? consumer : responseSpecConsumer.andThen(consumer);
            }

            private ResponseBuilder() {
            }


            /**
             * Provide a function to map specific error status codes to an error handler.
             * <p>By default, if there are no matching status handlers, responses with
             * status codes &gt;= 400 wil throw a {@link RestClientResponseException}.
             * <p>Note that {@link IOException IOExceptions},
             * {@link java.io.UncheckedIOException UncheckedIOExceptions}, and
             * {@link org.springframework.http.converter.HttpMessageNotReadableException HttpMessageNotReadableExceptions}
             * thrown from {@code errorHandler} will be wrapped in a
             * {@link RestClientException}.
             * @param statusPredicate to match responses with
             * @param errorHandler handler that typically, though not necessarily,
             * throws an exception
             * @return this builder
             */
            public ResponseBuilder onStatus(Predicate<HttpStatusCode> statusPredicate,
                                            ResponseSpec.ErrorHandler errorHandler) {
                applyResponseSpec(spec -> spec.onStatus(statusPredicate, errorHandler));
                return this;
            }

            /**
             * Provide a function to map specific error status codes to an error handler.
             * <p>By default, if there are no matching status handlers, responses with
             * status codes &gt;= 400 wil throw a {@link RestClientResponseException}.
             * <p>Note that {@link IOException IOExceptions},
             * {@link java.io.UncheckedIOException UncheckedIOExceptions}, and
             * {@link org.springframework.http.converter.HttpMessageNotReadableException HttpMessageNotReadableExceptions}
             * thrown from {@code errorHandler} will be wrapped in a
             * {@link RestClientException}.
             * @param errorHandler the error handler
             * @return this builder
             */
            public ResponseBuilder onStatus(ResponseErrorHandler errorHandler) {
                applyResponseSpec(spec -> spec.onStatus(errorHandler));
                return this;
            }
        }


    }

    public static class PayloadBuilder<T> {
       private final Builder builder;

        private PayloadBuilder(Builder builder) {
            this.builder = builder;
        }

        /**
         * Set the length of the body in bytes, as specified by the
         * {@code Content-Length} header.
         *
         * @param contentLength the content length
         * @return this builder
         * @see HttpHeaders#setContentLength(long)
         */
        public PayloadBuilder contentLength(long contentLength) {
            builder.applyRequestSpec(spec -> spec.contentLength(contentLength));
            return this;
        }

        /**
         * Set the {@linkplain MediaType media type} of the body, as specified
         * by the {@code Content-Type} header.
         *
         * @param contentType the content type
         * @return this builder
         * @see HttpHeaders#setContentType(MediaType)
         */
        public PayloadBuilder contentType(MediaType contentType) {
            builder.applyRequestSpec(spec -> spec.contentType(contentType));
            return this;
        }

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
         *
         * @param body the body of the request
         * @return this builder
         */
        public PayloadBuilder body(Object body) {
            builder.applyRequestSpec(spec -> spec.body(body));
            return this;
        }

        /**
         * Set the body of the request to the given {@code Object}.
         * The parameter {@code bodyType} is used to capture the generic type.
         *
         * @param body     the body of the request
         * @param bodyType the type of the body, used to capture the generic type
         * @return this builder
         */
        public PayloadBuilder body(T body, ParameterizedTypeReference<T> bodyType) {
            builder.applyRequestSpec(spec -> spec.body(body, bodyType));
            return this;
        }

        /**
         * Set the body of the request to the given function that writes to
         * an {@link OutputStream}.
         *
         * @param body a function that takes an {@code OutputStream} and can
         *             throw an {@code IOException}
         * @return this builder
         */
        public PayloadBuilder body(StreamingHttpOutputMessage.Body body) {
            builder.applyRequestSpec(spec -> spec.body(body));
            return this;
        }
    }


}