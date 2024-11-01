package vn.conyeu.restclient;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.ClientRequest;
import org.springframework.web.reactive.function.client.ClientResponse;
import vn.conyeu.commons.beans.ObjectMap;
import vn.conyeu.commons.exception.ConvertException;
import vn.conyeu.commons.utils.Objects;

import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;

@Slf4j
public class ClientLogger {
    private final String loggerId;
    private final RequestLog request;
    private final ResponseLog response;

    private Consumer<RequestLog> submitRequestConsumer;
    private Consumer<ResponseLog> submitResponseConsumer;
    private Consumer<ClientLogger> submitLoggerConsumer;

    private Function<ClientLogger, String> modelNameFnc;
    private Function<ClientLogger, String> modelIdFnc;

    private final Long userLogin;

    public ClientLogger(final String loggerId) {
        this(loggerId, null);
    }

    public ClientLogger(final String loggerId, final Long userLogin) {
        this.loggerId = loggerId.replace("]", "").replace("[", "").trim();
        this.userLogin = userLogin;
        this.request = new RequestLog();
        this.response = new ResponseLog();
    }

    public void submitRequestConsumer(Consumer<RequestLog> submitRequestConsumer) {
        this.submitRequestConsumer = submitRequestConsumer;
    }

    public void submitResponseConsumer(Consumer<ResponseLog> submitResponseConsumer) {
        this.submitResponseConsumer = submitResponseConsumer;
    }

    /**
     * Set the submitLoggerConsumer
     *
     * @param submitLoggerConsumer the value
     */
    public void submitLoggerConsumer(Consumer<ClientLogger> submitLoggerConsumer) {
        this.submitLoggerConsumer = submitLoggerConsumer;
    }

    /**
     * Set the modelNameFnc
     *
     * @param modelNameFnc the value
     */
    public ClientLogger modelNameFnc(Function<ClientLogger, String> modelNameFnc) {
        this.modelNameFnc = modelNameFnc;
        return this;
    }

    /**
     * Set the modelIdFnc
     *
     * @param modelIdFnc the value
     */
    public ClientLogger modelIdFnc(Function<ClientLogger, String> modelIdFnc) {
        this.modelIdFnc = modelIdFnc;
        return this;
    }

    public String loggerId() {
        return loggerId;
    }

    public RequestLog request() {
        return request;
    }

    private boolean isSubmitAll() {
        return submitLoggerConsumer != null;
    }

    public ResponseLog response() {
        return response;
    }

    public void submitRequest() {
        if (submitRequestConsumer != null && !isSubmitAll()) {
            submitRequestConsumer.accept(request());
        }
    }

    public void submitResponse() {
        if (submitResponseConsumer != null && !isSubmitAll()) {
            submitResponseConsumer.accept(response());
        }
    }

    public void submitAll() {
        if (submitLoggerConsumer != null) {
            submitLoggerConsumer.accept(this);
        }
    }

    public void forRequest(ClientRequest request) {
        RequestLog log = request();
        log.url(request.url().toString());
        log.headers(request.headers());
        log.method(request.method().name());
        log.cookies(request.cookies());
    }

    public void forResponse(ClientResponse response) {
        ResponseLog log = response();
        log.cookies(response.cookies());
        log.headers(response.headers().asHttpHeaders());
        log.status(response.statusCode().value());
    }

    @Getter
    public class RequestLog {
        private String url;
        private String method;
        private MultiValueMap<String, String> headers;
        private MultiValueMap<String, String> cookies;
        private Object payload;
        private Object throwable;

        /**
         * Returns the headers
         */
        public MultiValueMap<String, String> getHeaders() {
            if (headers == null) headers = new LinkedMultiValueMap<>();
            return headers;
        }

        /**
         * Set the cookies
         *
         * @param cookies the value
         */
        public RequestLog cookies(MultiValueMap<String, String> cookies) {
            this.cookies = new LinkedMultiValueMap<>(cookies);
            return this;
        }

        /**
         * Set the headers
         *
         * @param headers the value
         */
        public RequestLog headers(MultiValueMap<String, String> headers) {
            this.headers = new LinkedMultiValueMap<>();
            headers.forEach((k, v) -> this.headers.addAll(k, v));
            return this;
        }


        public void header(String headerKey, Object headerValue) {
            if(headerValue == null) getHeaders().remove(headerKey);
            else getHeaders().add(headerKey, headerValue.toString());
        }

        /**
         * Set the method
         *
         * @param method the value
         */
        public RequestLog method(String method) {
            this.method = method;
            return this;
        }

        /**
         * Set the payload
         *
         * @param payload the value
         */
        public RequestLog payload(Object payload) {
            if(contentTypeIsJson(getHeaders()) && Objects.nonNull(payload)) {
                payload = ObjectMap.fromJson(payload);
            }
            this.payload = payload;
            return this;
        }

        /**
         * Set the url
         *
         * @param url the value
         */
        public RequestLog url(String url) {
            this.url = url;
            return this;
        }

        public RequestLog throwable(Throwable throwable) {
            this.throwable = throwable;
            return this;
        }

        public Long userLogin() {
            return userLogin;
        }

        public ObjectMap map() {
            return ObjectMap.fromJson(this);
        }

        public String headerRemoveFirst(String headerKey) {
            List<String> list = getHeaders().get(headerKey);
            return Objects.notEmpty(list) ? list.removeFirst(): null;
        }

    }

    @Getter
    public class ResponseLog {
        private Integer status = 0;
        private Object payload;
        private MultiValueMap<String, String> headers;
        private ObjectMap cookies;

        /**
         * Set the cookies
         *
         * @param cookies the value
         */
        public ResponseLog cookies(Map<String, ?> cookies) {
            this.cookies = ObjectMap.fromMap(cookies);
            return this;
        }

        /**
         * Set the headers
         *
         * @param headers the value
         */
        public ResponseLog headers(MultiValueMap<String, String> headers) {
            this.headers = new LinkedMultiValueMap<>();
            headers.forEach((k, v) -> this.headers.addAll(k, v));
            return this;
        }

        /**
         * Set the payload
         *
         * @param payload the value
         */
        public ResponseLog payload(Object payload) {
            if(contentTypeIsJson(getHeaders()) && Objects.nonNull(payload)) {
               try{
                   this.payload = ObjectMap.fromJson(payload);
               }
               catch (ConvertException ignored) {
                    if(payload instanceof String str) {
                        if(this.payload == null) this.payload = str;
                        else this.payload = this.payload + str;
                    }
               }
            }
            else {
                this.payload = payload;
            }
            return this;
        }

        /**
         * Set the status
         *
         * @param status the value
         */
        public ResponseLog status(Integer status) {
            this.status = status;
            return this;
        }

        public void error(Throwable error) {
        }

        public ObjectMap map() {
            return ObjectMap.fromJson(this);
        }

    }

    static  boolean contentTypeIsJson(MultiValueMap<String, String> headers) {
        String contentType = headers.getFirst("Content-Type");
        return contentType != null && contentType.startsWith("application/json");
    }


}