package vn.conyeu.restclient;

import org.springframework.core.ResolvableType;
import org.springframework.core.log.LogFormatUtils;
import org.springframework.http.*;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.util.FileCopyUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.web.client.*;
import org.springframework.web.client.RestClient.ResponseSpec.ErrorHandler;
import vn.conyeu.commons.utils.Asserts;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

public class StatusHandler {
    private final ResponsePredicate predicate;
    private final ErrorHandler errorHandler;

    private StatusHandler(ResponsePredicate predicate, ErrorHandler errorHandler) {
        this.predicate = predicate;
        this.errorHandler = errorHandler;
    }

    public static StatusHandler of(Predicate<HttpStatusCode> predicate, ErrorHandler errorHandler) {
        Asserts.notNull(predicate, "Predicate must not be null");
        Asserts.notNull(errorHandler, "ErrorHandler must not be null");
        return new StatusHandler(response -> predicate.test(response.getStatusCode()), errorHandler);
    }

    public static StatusHandler fromErrorHandler(ResponseErrorHandler errorHandler) {
        Asserts.notNull(errorHandler, "ResponseErrorHandler must not be null");
        return new StatusHandler(errorHandler::hasError, (request, response) ->
                errorHandler.handleError(request.getURI(), request.getMethod(), response));
    }

    public static StatusHandler defaultHandler(List<HttpMessageConverter<?>> messageConverters) {
        return new StatusHandler(response -> response.getStatusCode().isError(),
                (request, response) -> {
                    HttpStatusCode statusCode = response.getStatusCode();
                    String statusText = response.getStatusText();
                    HttpHeaders headers = response.getHeaders();
                    byte[] body = Utils.getBody(response);
                    Charset charset = Utils.getCharset(response);
                    String message = getErrorMessage(statusCode.value(), statusText, body, charset);
                    RestClientResponseException ex;

                    if (statusCode.is4xxClientError()) {
                        ex = HttpClientErrorException.create(message, statusCode, statusText, headers, body, charset);
                    }
                    else if (statusCode.is5xxServerError()) {
                        ex = HttpServerErrorException.create(message, statusCode, statusText, headers, body, charset);
                    }
                    else {
                        ex = new UnknownHttpStatusCodeException(message, statusCode.value(), statusText, headers, body, charset);
                    }
                    if (!CollectionUtils.isEmpty(messageConverters)) {
                        ex.setBodyConvertFunction(initBodyConvertFunction(response, body, messageConverters));
                    }
                    throw ex;
                });
    }

    private static Function<ResolvableType, ?> initBodyConvertFunction(ClientHttpResponse response, byte[] body, List<HttpMessageConverter<?>> messageConverters) {
        Asserts.state(!CollectionUtils.isEmpty(messageConverters), "Expected message converters");
        return resolvableType -> {
            try {
                HttpMessageConverterExtractor<?> extractor =
                        new HttpMessageConverterExtractor<>(resolvableType.getType(), messageConverters);

                return extractor.extractData(new ClientHttpResponseDecorator(response) {
                    @Override
                    public InputStream getBody() {
                        return new ByteArrayInputStream(body);
                    }
                });
            }
            catch (IOException ex) {
                throw new RestClientException("Error while extracting response for type [" + resolvableType + "]", ex);
            }
        };
    }


    private static String getErrorMessage(int rawStatusCode, String statusText, @Nullable byte[] responseBody,
                                          @Nullable Charset charset) {

        String preface = rawStatusCode + " " + statusText + ": ";

        if (ObjectUtils.isEmpty(responseBody)) {
            return preface + "[no body]";
        }

        charset = (charset != null ? charset : StandardCharsets.UTF_8);

        String bodyText = new String(responseBody, charset);
        bodyText = LogFormatUtils.formatValue(bodyText, -1, true);

        return preface + bodyText;
    }



    public boolean test(ClientHttpResponse response) throws IOException {
        return this.predicate.test(response);
    }

    public void handle(HttpRequest request, ClientHttpResponse response) throws IOException {
        this.errorHandler.handle(request, response);
    }


    @FunctionalInterface
    private interface ResponsePredicate {

        boolean test(ClientHttpResponse response) throws IOException;
    }

    /**
     * Wrap and delegate to an existing {@link ClientHttpResponse}.
     */
    static class ClientHttpResponseDecorator implements ClientHttpResponse {

        private final ClientHttpResponse delegate;


        public ClientHttpResponseDecorator(ClientHttpResponse delegate) {
            Assert.notNull(delegate, "ClientHttpResponse delegate is required");
            this.delegate = delegate;
        }


        /**
         * Return the wrapped response.
         */
        public ClientHttpResponse getDelegate() {
            return this.delegate;
        }


        @Override
        public HttpStatusCode getStatusCode() throws IOException {
            return this.delegate.getStatusCode();
        }

        @Override
        public String getStatusText() throws IOException {
            return this.delegate.getStatusText();
        }

        @Override
        public HttpHeaders getHeaders() {
            return this.delegate.getHeaders();
        }

        @Override
        public InputStream getBody() throws IOException {
            return this.delegate.getBody();
        }

        @Override
        public void close() {
            this.delegate.close();
        }

    }

    static abstract class Utils {

        public static byte[] getBody(HttpInputMessage message) {
            try {
                return FileCopyUtils.copyToByteArray(message.getBody());
            }
            catch (IOException ignore) {
            }
            return new byte[0];
        }

        @Nullable
        public static Charset getCharset(HttpMessage response) {
            HttpHeaders headers = response.getHeaders();
            MediaType contentType = headers.getContentType();
            return (contentType != null ? contentType.getCharset() : null);
        }
    }

}