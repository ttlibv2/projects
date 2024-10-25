package vn.conyeu.restclient;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpRequest;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.DefaultRestClient;
import org.springframework.web.util.UriBuilder;

import java.net.URI;
import java.nio.charset.Charset;
import java.time.ZonedDateTime;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;

public class SimpleClient implements RestClient {

    @Override
    public HeadersUriSpec<?> get() {
        return methodInternal(HttpMethod.GET);
    }

    @Override
    public HeadersUriSpec<?> head() {
        return null;
    }

    @Override
    public BodyUriSpec post() {
        return null;
    }

    @Override
    public BodyUriSpec put() {
        return null;
    }

    @Override
    public BodyUriSpec patch() {
        return null;
    }

    @Override
    public HeadersUriSpec<?> delete() {
        return null;
    }

    @Override
    public HeadersUriSpec<?> options() {
        return null;
    }

    @Override
    public BodyUriSpec method(HttpMethod method) {
        return null;
    }

    @Override
    public Builder mutate() {
        return null;
    }

    private BodyUriSpec methodInternal(HttpMethod httpMethod) {

    }
}